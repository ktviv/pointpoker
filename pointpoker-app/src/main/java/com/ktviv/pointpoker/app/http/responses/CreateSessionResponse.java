package com.ktviv.pointpoker.app.http.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateSessionResponse {

    @JsonProperty private String sessionId;
}
