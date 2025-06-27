package com.fest.backend;

import org.springframework.web.bind.annotation.*;

@CrossOrigin( origins = {
        "https://miracle-fest-website-bimu.vercel.app" ,
        "http://localhost:5173"
                            })
@RestController
@RequestMapping("/api/auth")
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("signup")
    public String hello()
    {
        return "Hello from Vina";
    }


}
