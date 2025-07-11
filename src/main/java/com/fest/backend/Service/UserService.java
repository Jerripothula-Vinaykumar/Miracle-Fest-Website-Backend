package com.fest.backend.Service;

import com.fest.backend.Entity.FestUser;
import com.fest.backend.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private  final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public ResponseEntity<?> register(FestUser user) {

        try {

            if (userRepository.findByEmail(user.getEmail()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered");
            }


            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            FestUser savedUser = userRepository.save(user);


            String token = jwtService.generateToken(savedUser);


            return ResponseEntity.ok(token);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong during signup");
        }
    }

    public ResponseEntity<?> login(FestUser user) {

        try {
            // Step 1: Check if user exists
            FestUser dbUser = userRepository.findByEmail(user.getEmail());
            if (dbUser == null) {
                throw new UsernameNotFoundException("User not found");
            }

            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(), user.getPassword()
                    )
            );
            String token =  jwtService.generateToken(dbUser);
            return ResponseEntity.ok(token);
        }
        catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }


    public ResponseEntity<String> setMyProfile(FestUser user, HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if( authHeader == null || !authHeader.startsWith("Bearer "))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or Invalid Token");
        }
        String token  = authHeader.substring(7);

        String userEmail = jwtService.extractEmail(token);
        FestUser festUser = userRepository.findByEmail(userEmail);

        if (user.getUsername() != null)
        {
            festUser.setUsername(user.getUsername());
        }
        if (user.getBranch() != null )
        {
            festUser.setBranch(user.getBranch());
        }
        if (user.getDistrict() != null)
        {
            festUser.setDistrict(user.getDistrict());
        }
        if (user.getInstaid() != null)
        {
            festUser.setInstaid(user.getInstaid());
        }
        if (user.getLinkedin() != null)
        {
            festUser.setLinkedin(user.getLinkedin());
        }
        if (user.getMobileno() != null)
        {
            festUser.setMobileno(user.getMobileno());
        }
        if (user.getSection() != null)
        {
            festUser.setSection(user.getSection());
        }
        if (user.getState() != null)
        {
            festUser.setState(user.getState());
        }
        if (user.getVillage() != null)
        {
            festUser.setVillage(user.getVillage());
        }
        if (user.getYear() != null)
        {
            festUser.setYear(user.getYear());
        }

        userRepository.save(festUser);

        return ResponseEntity.ok("Profile Updated");


    }

    public ResponseEntity<FestUser> getMyProfile(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {

        String token = authHeader.substring(7);
        String userEmail = jwtService.extractEmail(token);
        FestUser festUser = userRepository.findByEmail(userEmail);
        return  ResponseEntity.ok(festUser);

        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


    }
}
