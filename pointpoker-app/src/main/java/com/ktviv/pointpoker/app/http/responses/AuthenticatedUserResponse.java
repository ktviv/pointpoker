package com.ktviv.pointpoker.app.http.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ktviv.pointpoker.domain.entity.SessionUser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticatedUserResponse {

    @JsonProperty
    private SessionUser sessionUser;
}
