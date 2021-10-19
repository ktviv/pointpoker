package com.ktviv.pointpoker.app.controller;

import com.ktviv.pointpoker.app.http.responses.AuthenticatedUserResponse;
import com.ktviv.pointpoker.app.utils.ApplicationUserMapper;
import com.ktviv.pointpoker.domain.entity.ApplicationUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@CrossOrigin
public class AuthController {


    @PostMapping("/login")
    public ResponseEntity<?> user(Principal user) {

        ApplicationUser applicationUser = ((ApplicationUser) ((UsernamePasswordAuthenticationToken) user).getPrincipal());
        return ResponseEntity.ok(new AuthenticatedUserResponse(ApplicationUserMapper.fromApplicationUser(applicationUser)));
    }

//    @RequestMapping("/logout")
//    public boolean login(@RequestBody LoginRequest loginRequest) {
//
//        System.out.println(loginRequest);
//        return true;
////                user.getUserName().equals("user") && user.getPassword().equals("password");
//
//        //read this : https://spring.io/guides/tutorials/spring-security-and-angular-js/
//    }


}
