package com.ktviv.pointpoker.app.http.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {

    private String username;
    private String password;
//    private String rememberme;
}
