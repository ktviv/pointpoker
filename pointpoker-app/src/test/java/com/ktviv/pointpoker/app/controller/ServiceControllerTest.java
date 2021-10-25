package com.ktviv.pointpoker.app.controller;

import com.ktviv.pointpoker.app.controller.TestConfiguration.SseEmitterEvent;
import com.ktviv.pointpoker.app.http.requests.VoteRequest;
import com.ktviv.pointpoker.app.http.responses.CreateSessionResponse;
import com.ktviv.pointpoker.domain.events.UserVotedEvent;
import com.ktviv.pointpoker.domain.events.VoteResetEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static java.util.Objects.requireNonNull;
import static org.springframework.test.util.AssertionErrors.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfiguration.class)
public class ServiceControllerTest {

    private static final String TEST_USERNAME = "test_user";
    private static final String TEST_PWD = "test_user@123";
    private static final String TEST_USERNAME2 = "test_user2";
    private static final String TEST_PWD2 = "test_user2@123";


    @Autowired
    private TestRestTemplate template;

    private TestRestTemplate getAuthTemplate(boolean isTwo, boolean useValidCred) {

        return template.withBasicAuth((isTwo) ? TEST_USERNAME2 : TEST_USERNAME, (useValidCred) ? (isTwo) ? TEST_PWD2 : TEST_PWD : "INVALID_PWD");
    }

    private ResponseEntity<CreateSessionResponse> createSession(boolean isTwo, boolean useValidCred) {

        return getAuthTemplate(isTwo, useValidCred).postForEntity("/api/sessions", null, CreateSessionResponse.class);
    }

    private ResponseEntity<String> joinSession(boolean isTwo, String sessionId) {

        return getAuthTemplate(isTwo, true).exchange("/api/sessions/" + sessionId, HttpMethod.PUT, null, String.class);
    }

    private ResponseEntity<String> vote(boolean isTwo, String sessionId, VoteRequest voteRequest) {

        return getAuthTemplate(isTwo, true).postForEntity("/api/sessions/" + sessionId + "/vote", voteRequest, String.class);
    }

    private ResponseEntity<String> voteReset(boolean isTwo, String sessionId) {

        return getAuthTemplate(isTwo, true).exchange("/api/sessions/" + sessionId + "/reset", HttpMethod.PUT, null, String.class);
    }

    private ResponseEntity<SseEmitterEvent> getVoteEvents(boolean isTwo, String sessionId) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM.getType());
        headers.add(HttpHeaders.ACCEPT, MediaType.TEXT_EVENT_STREAM_VALUE);

        return getAuthTemplate(isTwo, true).exchange("/api/sessions/" + sessionId + "/vote", HttpMethod.GET, new HttpEntity<>(headers), SseEmitterEvent.class);
    }

    @Test
    public void createSession_shouldSucceed200() throws Exception {

        ResponseEntity<CreateSessionResponse> response = createSession(false, true);

        assertEquals("Response Status is OK", HttpStatus.OK, response.getStatusCode());
        assertNotNull("Response body not null", response.getBody());
        assertTrue("Session Id is not blank", !response.getBody().getSessionId().isEmpty());
    }

    @Test
    public void createSession_shouldFailWith401Unauthorized() throws Exception {

        ResponseEntity<CreateSessionResponse> response = createSession(false, false);
        assertEquals("Response Status is Unauthorized", HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull("Response body is null", response.getBody());
    }

    @Test
    public void joinSession_shouldSucceed200() throws Exception {

        ResponseEntity<CreateSessionResponse> createResponse = createSession(false, true);
        ResponseEntity<String> joinResponse = joinSession(false, requireNonNull(createResponse.getBody()).getSessionId());
        assertEquals("Response Status is OK", HttpStatus.OK, joinResponse.getStatusCode());
    }

    @Test
    public void joinSession_shouldFail404() throws Exception {

        ResponseEntity<String> response = joinSession(false, "INVALID_SESSION_ID");
        assertEquals("Response Status is 404", HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error message is poker-session-not-found", "{\"errors\":[{\"msg\":\"poker-session-not-found\"}]}", response.getBody());
    }

    @Test
    public void vote_shouldSucceed200() throws Exception {

        VoteRequest voteRequest = new VoteRequest();
        voteRequest.setStoryPoint(21f);
        ResponseEntity<CreateSessionResponse> createResponse = createSession(false, true);
        ResponseEntity<String> joinResponse = joinSession(false, requireNonNull(createResponse.getBody()).getSessionId());
        ResponseEntity<String> voteResponse = vote(false, createResponse.getBody().getSessionId(), voteRequest);

        assertEquals("Response Status is OK", HttpStatus.OK, voteResponse.getStatusCode());
    }

    @Test
    public void vote_shouldFailForUnknownSession404() throws Exception {

        VoteRequest voteRequest = new VoteRequest();
        voteRequest.setStoryPoint(21f);
        ResponseEntity<String> voteResponse = vote(false, "INVALID_SESSION_ID", voteRequest);

        assertEquals("Response Status is OK", HttpStatus.NOT_FOUND, voteResponse.getStatusCode());
        assertEquals("Error message is poker-session-not-found", "{\"errors\":[{\"msg\":\"poker-session-not-found\"}]}", voteResponse.getBody());
    }


    @Test
    public void vote_shouldFailForInvalidParticipant403() throws Exception {

        VoteRequest voteRequest = new VoteRequest();
        voteRequest.setStoryPoint(21f);

        ResponseEntity<CreateSessionResponse> createResponse = createSession(false, true);
        ResponseEntity<String> joinResponse = joinSession(false, requireNonNull(createResponse.getBody()).getSessionId());
        ResponseEntity<String> voteResponse = vote(true, createResponse.getBody().getSessionId(), voteRequest);

        assertEquals("Response Status is Forbidden", HttpStatus.FORBIDDEN, voteResponse.getStatusCode());
        assertEquals("Error message is participant-unassociated-with-session", "{\"errors\":[{\"msg\":\"participant-unassociated-with-session\"}]}", voteResponse.getBody());
    }

    @Test
    public void voteReset_shouldSucceed200() throws Exception {

        ResponseEntity<CreateSessionResponse> createResponse = createSession(false, true);
        ResponseEntity<String> joinResponse = joinSession(false, requireNonNull(createResponse.getBody()).getSessionId());
        ResponseEntity<String> response = voteReset(false, createResponse.getBody().getSessionId());

        assertEquals("Response Status is OK", HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void voteReset_shouldFailForUnknownSession404() throws Exception {

        ResponseEntity<String> voteResponse = voteReset(false, "INVALID_SESSION_ID");

        assertEquals("Response Status is OK", HttpStatus.NOT_FOUND, voteResponse.getStatusCode());
        assertEquals("Error message is poker-session-not-found", "{\"errors\":[{\"msg\":\"poker-session-not-found\"}]}", voteResponse.getBody());
    }


    @Test
    public void voteReset_shouldFailForInvalidParticipant403() throws Exception {

        ResponseEntity<CreateSessionResponse> createResponse = createSession(false, true);
        ResponseEntity<String> joinResponse = joinSession(false, requireNonNull(createResponse.getBody()).getSessionId());
        ResponseEntity<String> voteResetResponse = voteReset(true, createResponse.getBody().getSessionId());

        assertEquals("Response Status is Forbidden", HttpStatus.FORBIDDEN, voteResetResponse.getStatusCode());
        assertEquals("Error message is participant-unassociated-with-session", "{\"errors\":[{\"msg\":\"participant-unassociated-with-session\"}]}", voteResetResponse.getBody());
    }

    @Test
    public void fetchVoteEvents_shouldSucceed200() throws Exception {

        VoteRequest voteRequest = new VoteRequest();
        voteRequest.setStoryPoint(21f);

        ResponseEntity<CreateSessionResponse> createResponse = createSession(false, true);
        ResponseEntity<String> joinResponse = joinSession(false, requireNonNull(createResponse.getBody()).getSessionId());
        ResponseEntity<String> voteResponse = vote(false, createResponse.getBody().getSessionId(), voteRequest);
        ResponseEntity<String> voteResetResponse = voteReset(false, createResponse.getBody().getSessionId());

        ResponseEntity<SseEmitterEvent> voteEventsResponse = getVoteEvents(false, createResponse.getBody().getSessionId());

        assertEquals("Response Status is OK", HttpStatus.OK, voteEventsResponse.getStatusCode());
        assertTrue("Response should contain two events(user-vote and reset-vote)", requireNonNull(voteEventsResponse.getBody()).getData().size() == 2);
        assertTrue("Response contains user-vote event", requireNonNull(voteEventsResponse.getBody()).getData().stream().anyMatch(e -> e.getEventType().equals(UserVotedEvent.TYPE)));
        assertTrue("Response contains vote-reset event", requireNonNull(voteEventsResponse.getBody()).getData().stream().anyMatch(e -> e.getEventType().equals(VoteResetEvent.TYPE)));
        assertTrue("Vote event matches the vote request story point", requireNonNull(voteEventsResponse.getBody()).getData().stream().filter(e -> e.getEventType().equals(UserVotedEvent.TYPE)).anyMatch(e -> e.getStoryPoint().equals(21f)));
    }

    @Test
    public void fetchVoteEvents_shouldFailForUnknownSession404() throws Exception {

        ResponseEntity<SseEmitterEvent> voteEventsResponse = getVoteEvents(false, "INVALID_SESSION_ID");

        assertEquals("Response Status is OK", HttpStatus.NOT_FOUND, voteEventsResponse.getStatusCode());
        assertEquals("Error message is poker-session-not-found", "{\"errors\":[{\"msg\":\"poker-session-not-found\"}]}", voteEventsResponse.getBody().getError());
    }

    @Test
    public void fetchVoteEvents_shouldFailForInvalidParticipant403() throws Exception {

        ResponseEntity<CreateSessionResponse> createResponse = createSession(false, true);
        ResponseEntity<String> joinResponse = joinSession(false, requireNonNull(createResponse.getBody()).getSessionId());
        ResponseEntity<SseEmitterEvent> voteEventsResponse = getVoteEvents(true, requireNonNull(createResponse.getBody()).getSessionId());

        assertEquals("Response Status is Forbidden", HttpStatus.FORBIDDEN, voteEventsResponse.getStatusCode());
        assertEquals("Error message is participant-unassociated-with-session", "{\"errors\":[{\"msg\":\"participant-unassociated-with-session\"}]}", voteEventsResponse.getBody().getError());
    }
}
