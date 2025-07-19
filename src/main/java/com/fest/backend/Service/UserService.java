package com.fest.backend.Service;

import com.fest.backend.CustomUserDetails;
import com.fest.backend.Entity.FestUser;
import com.fest.backend.Entity.Token;
import com.fest.backend.Entity.TokenType;
import com.fest.backend.Repository.TokenRepository;
import com.fest.backend.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                       AuthenticationManager authenticationManager, JwtService jwtService,
                       TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
    }

    public ResponseEntity<?> register(FestUser user) {
        try {
            if (userRepository.findByEmail(user.getEmail()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered");
            }

            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            FestUser savedUser = userRepository.save(user);

            String token = jwtService.generateToken(savedUser);
            revokeAllUserTokens(savedUser);
            saveUserToken(savedUser, token);

            return ResponseEntity.ok(token);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong during signup");
        }
    }

    public ResponseEntity<?> login(FestUser user, HttpServletResponse response) {
        try {
            FestUser dbUser = userRepository.findByEmail(user.getEmail());
            if (dbUser == null) {
                throw new UsernameNotFoundException("User not found");
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );

            String accessToken = jwtService.generateToken(dbUser);
            String refreshToken = jwtService.generateRefreshToken(dbUser);

            revokeAllUserTokens(dbUser);
            saveUserToken(dbUser, accessToken);

            ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge( 24 * 60 * 60)
                    .sameSite("None")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return ResponseEntity.ok(accessToken);

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    public ResponseEntity<String> setMyProfile(FestUser user, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or Invalid Token");
        }
        String token = authHeader.substring(7);
        String userEmail = jwtService.extractEmail(token);
        FestUser festUser = userRepository.findByEmail(userEmail);

        if (user.getUsername() != null) festUser.setUsername(user.getUsername());
        if (user.getBranch() != null) festUser.setBranch(user.getBranch());
        if (user.getDistrict() != null) festUser.setDistrict(user.getDistrict());
        if (user.getInstaid() != null) festUser.setInstaid(user.getInstaid());
        if (user.getLinkedin() != null) festUser.setLinkedin(user.getLinkedin());
        if (user.getMobileno() != null) festUser.setMobileno(user.getMobileno());
        if (user.getSection() != null) festUser.setSection(user.getSection());
        if (user.getState() != null) festUser.setState(user.getState());
        if (user.getVillage() != null) festUser.setVillage(user.getVillage());
        if (user.getYear() != null) festUser.setYear(user.getYear());

        userRepository.save(festUser);
        return ResponseEntity.ok("Profile Updated");
    }

    public ResponseEntity<FestUser> getMyProfile(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            String token = authHeader.substring(7);
            String userEmail = jwtService.extractEmail(token);
            FestUser festUser = userRepository.findByEmail(userEmail);
            return ResponseEntity.ok(festUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public String refreshToken(String oldToken) {
        System.out.println("🔁 [refreshToken] Received token: " + oldToken);
        String userEmail = jwtService.extractEmail(oldToken);
        System.out.println("📧 [refreshToken] Extracted email: " + userEmail);
        FestUser user = userRepository.findByEmail(userEmail);
        System.out.println("👤 [refreshToken] Fetched user: " + (user != null ? user.getEmail() : "null"));
        boolean isValid = jwtService.isTokenValid(oldToken, new CustomUserDetails(user));
        System.out.println("✅ [refreshToken] Token valid: " + isValid);

        if (user == null || !isValid) {
            throw new RuntimeException("Invalid or expired token");
        }

        revokeAllUserTokens(user);
        String newToken = jwtService.generateToken(user);
        saveUserToken(user, newToken);

        System.out.println("🔐 [refreshToken] New token generated");

        return newToken;
    }

    private void saveUserToken(FestUser user, String jwtToken) {
        Token token = new Token();
        token.setUser(user);
        token.setToken(jwtToken);
        token.setTokenType(TokenType.BEARER);
        token.setExpired(false);
        token.setRevoked(false);
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(FestUser user) {
        var validTokens = tokenRepository.findAllValidTokensByUser(user.getEmail());
        if (validTokens == null || validTokens.isEmpty()) return;
        for (Token token : validTokens) {
            token.setExpired(true);
            token.setRevoked(true);
        }
        tokenRepository.saveAll(validTokens);
    }
}
