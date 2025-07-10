package com.fest.backend.Controller;

import com.fest.backend.Entity.FestUser;
import com.fest.backend.Repository.UserRepository;
import com.fest.backend.Service.UserService;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserController (UserRepository userRepository, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
            this.userRepository = userRepository;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping ("/hello")
    public String hello()
    {
        return "Hello";
    }

    @PostMapping ("/login")
    public ResponseEntity<?>  login(@RequestBody FestUser user)
    {
        return userService.login(user);
    }
    @PostMapping ("/signup")
    public ResponseEntity<?> signup(@RequestBody FestUser user)
    {
        return userService.register(user);
    }

    @PatchMapping("/login/{id}")
    public String hiii( @PathVariable int id , @RequestBody FestUser patchuser)
    {
        FestUser user = userRepository.findById(id).orElseThrow( ()-> new RuntimeException("User Not Found") );

        if (patchuser.getBranch() != null )
        {
            user.setBranch(patchuser.getBranch());
        }
        if (patchuser.getDistrict() != null )
        {
            user.setDistrict(patchuser.getDistrict());
        }
        if (patchuser.getInstaid() != null )
        {
            user.setInstaid(patchuser.getInstaid());
        }
        if ( patchuser.getLinkedin() != null )
        {
            user.setLinkedin(patchuser.getLinkedin());
        }
        if (patchuser.getMobileno() != null )
        {
            user.setMobileno(patchuser.getMobileno());
        }
        if (patchuser.getSection() != null )
        {
            user.setSection(patchuser.getSection());
        }
        if (patchuser.getState() != null )
        {
            user.setState(patchuser.getState());
        }
        if (patchuser.getUsername() != null)
        {
            user.setUsername(patchuser.getUsername());
        }
        if (patchuser.getVillage() != null )
        {
            user.setVillage(patchuser.getVillage());
        }
        if (patchuser.getYear() != null )
        {
            user.setYear(patchuser.getYear());
        }

        userRepository.save(user);
        return "User Details updated Successfully" ;
    }


}
