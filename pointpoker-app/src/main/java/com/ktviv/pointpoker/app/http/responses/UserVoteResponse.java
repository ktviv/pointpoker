package com.ktviv.pointpoker.app.http.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVoteResponse {

    @JsonProperty String eventType;
    @JsonProperty String displayName;
    @JsonProperty Float storyPoint;

    public static UserVoteResponse of(String eventType, String displayName, Float storyPoint) {

        return new UserVoteResponse(eventType, displayName, storyPoint);
    }
}
