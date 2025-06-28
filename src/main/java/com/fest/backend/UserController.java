package com.fest.backend;

import org.springframework.web.bind.annotation.*;

@CrossOrigin( origins = {
        "https://miracle-fest-website-bimu.vercel.app" ,
        "http://localhost:5173"
                            })
@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public String hii(@RequestBody FestUser user)
    {
       userRepository.save(user);

        return "Email : " + user.getEmail() + "Password : " + user.getPassword() ;
    }
    @GetMapping("/sign")
    public String hello()
    {
        return "Hello Vinay ";
    }

    @PatchMapping("/login/{id}")
    public String hiii( @PathVariable int id , @RequestBody FestUser patchuser)
    {
        FestUser user = userRepository.findById(id).orElseThrow( ()-> new RuntimeException("User Not Found") );

        if (patchuser.getUsername() != null) {
            user.setUsername(patchuser.getUsername());
        }
        if (patchuser.getYear() != null ) {
            user.setYear(patchuser.getYear());
        }
        userRepository.save(user);
        return "username : " + user.getUsername() + "Year : " + user.getYear() ;
    }


}
