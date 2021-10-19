package com.ktviv.pointpoker.app.http.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VoteRequest {

    @JsonProperty
    private float storyPoint;
}
