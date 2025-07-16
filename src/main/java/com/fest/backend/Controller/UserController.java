package com.fest.backend.Controller;

import com.fest.backend.CustomUserDetails;
import com.fest.backend.Entity.FestUser;
import com.fest.backend.Repository.UserRepository;
import com.fest.backend.Service.JwtService;
import com.fest.backend.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.Objects;



@RestController
@RequestMapping("api/auth")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserController (UserRepository userRepository, UserService userService, JwtService jwtService, BCryptPasswordEncoder bCryptPasswordEncoder) {
            this.userRepository = userRepository;
        this.userService = userService;
        this.jwtService = jwtService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping ("/hello")
    public String hello()
    {
        return "Hello";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody FestUser user, HttpServletResponse response) {
        return userService.login(user, response);
    }


    @PostMapping ("/signup")
    public ResponseEntity<?> signup ( @RequestBody FestUser user )
    {
        return userService.register(user);
    }

    @GetMapping("/getProfile")
    public ResponseEntity<FestUser> getProfile ( HttpServletRequest request)
    {
        return userService.getMyProfile(request);
    }

    @PatchMapping ("/setProfile")
    public ResponseEntity<String> setProfile (@RequestBody FestUser user , HttpServletRequest request)
    {
        return userService.setMyProfile(user , request);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }

        try {
            String oldToken = authHeader.substring(7);
            String newToken = userService.refreshToken(oldToken);
            return ResponseEntity.ok(newToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }





}
