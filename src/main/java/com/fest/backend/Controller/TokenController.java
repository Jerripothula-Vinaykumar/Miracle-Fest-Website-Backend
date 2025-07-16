package com.fest.backend.Controller;

import com.fest.backend.Service.JwtService;
import com.fest.backend.Service.UserService;
import com.fest.backend.Entity.FestUser;
import com.fest.backend.Repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/token")
public class TokenController {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserService userService;

    public TokenController(JwtService jwtService, UserRepository userRepository, UserService userService) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
        String refreshToken = null;

        // ✅ Get refresh token from cookie
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            return ResponseEntity.badRequest().body("Refresh token not found in cookies");
        }

        try {
            String newAccessToken = userService.refreshToken(refreshToken);
            return ResponseEntity.ok(newAccessToken);
        } catch (Exception e) {
            return ResponseEntity.status(403).body("Invalid or expired refresh token");
        }
    }
}
