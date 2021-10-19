package com.ktviv.pointpoker.app.utils;

import com.ktviv.pointpoker.app.http.responses.UserVoteResponse;
import com.ktviv.pointpoker.domain.events.UserVotedEvent;
import com.ktviv.pointpoker.domain.events.VoteResetEvent;

public class UserVoteResponseMapper {

    public static UserVoteResponse from(UserVotedEvent userVotedEvent) {

        return UserVoteResponse.of(userVotedEvent.getType(), userVotedEvent.getDisplayName(), userVotedEvent.getStoryPoint());
    }

    public static UserVoteResponse from(VoteResetEvent voteResetEvent) {

        return UserVoteResponse.of(voteResetEvent.getType(), "", -1F);
    }
}
