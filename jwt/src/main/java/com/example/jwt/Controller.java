package com.example.jwt;

import com.example.jwt.Config.UserService;
import com.example.jwt.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Autowired
    private UserService userService;
    @GetMapping("/")
    public String Home(){
        return "Home page";
    }
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/new")
    public ResponseEntity<String> addNewUser(@RequestBody Users users) {
        String result = userService.addUser(users);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String Admin(){
        return "Admin page";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String User(){
        return "User page";
    }
    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody Authrequest authrequest){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authrequest.getUsername(), authrequest.getPassword()));
        if(authenticate.isAuthenticated())
        return jwtService.generateToken(authrequest.getUsername());
        else throw new UsernameNotFoundException("Invalid User");

    }
}
