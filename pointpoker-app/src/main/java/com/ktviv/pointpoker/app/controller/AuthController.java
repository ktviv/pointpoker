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
@CrossOrigin// CrossOrigin for testing purposes when angular is running in a separate container.
public class AuthController {

    /**
     * @param user {@link Principal} If the call from GUI has valid Authorization header
     *             then spring security will add the corresponding authenticated user object(Principal) to the
     *             application security context, and that object would be auto-injected as an argument to this method.
     * @return ResponseEntity Returning {@link AuthenticatedUserResponse} which contains skinny user object (refer {@link com.ktviv.pointpoker.domain.entity.SessionUser})
     */
    @PostMapping("/login")
    public ResponseEntity<?> user(Principal user) {

        ApplicationUser applicationUser = ((ApplicationUser) ((UsernamePasswordAuthenticationToken) user).getPrincipal());
        return ResponseEntity.ok(new AuthenticatedUserResponse(ApplicationUserMapper.fromApplicationUser(applicationUser)));
    }
}
