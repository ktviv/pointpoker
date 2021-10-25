package com.ktviv.pointpoker.app.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.base.Strings;
import com.ktviv.pointpoker.app.http.responses.UserVoteResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import javax.persistence.Transient;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@Configuration
public class TestConfiguration {

    @Bean
    public HttpMessageConverter<SseEmitterEvent> sseEmitterEventConverter() {

        return new SseEmitterEventConverter();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class SseEmitterEvent {

        @JsonProperty
        private List<UserVoteResponse> data;
        @Transient
        private String error;

        public SseEmitterEvent(List<UserVoteResponse> data) {
            this.data = data;
        }

        public SseEmitterEvent() {
        }

        public List<UserVoteResponse> getData() {
            return data;
        }

        public void setData(List<UserVoteResponse> data) {
            this.data = data;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public boolean hasError() {

            return !Strings.isNullOrEmpty(this.error);
        }
    }

    static class SseEmitterEventConverter
            extends AbstractHttpMessageConverter<SseEmitterEvent> {

        public SseEmitterEventConverter() {
            super(new MediaType("text", "event-stream"));
        }

        @Override
        protected boolean supports(Class<?> clazz) {
            return SseEmitterEvent.class.isAssignableFrom(clazz);
        }

        @Override
        protected SseEmitterEvent readInternal(Class<? extends SseEmitterEvent> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

            return toSseEmitterEvent(inputMessage.getBody());
        }

        @Override
        protected void writeInternal(SseEmitterEvent emitterEvent, HttpOutputMessage outputMessage)
                throws JsonProcessingException, HttpMessageNotWritableException {
            try {

                OutputStream outputStream = outputMessage.getBody();
                final ObjectWriter objectWriter = new ObjectMapper().writer();
                outputStream.write(objectWriter.writeValueAsString(emitterEvent).getBytes());
                outputStream.close();
            } catch (Exception e) {

                throw new RuntimeException("Failed to parse UserVoteResponse Object to string", e);
            }
        }

        private static SseEmitterEvent toSseEmitterEvent(InputStream inputStream) throws JsonProcessingException {

            Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNext()) {

                stringBuilder.append(scanner.next());
            }
            if (stringBuilder.toString().trim().equals("{\"errors\":[{\"msg\":\"poker-session-not-found\"}]}") || stringBuilder.toString().trim().equals("{\"errors\":[{\"msg\":\"participant-unassociated-with-session\"}]}")) {

                SseEmitterEvent sseEmitterEvent = new SseEmitterEvent(Collections.emptyList());
                sseEmitterEvent.setError(stringBuilder.toString().trim());
                return sseEmitterEvent;
            }
            String[] values = stringBuilder.toString().split("\n");
            stringBuilder = new StringBuilder();
            stringBuilder.append("{\"data\": [");
            for (String value : values) {

                if (value.isEmpty()) {

                    continue;
                }
                stringBuilder.append(value.split("data:")[1]);
                stringBuilder.append(",");
            }
            String jsonString = stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(",")).append("]}").toString();
            SseEmitterEvent sseEmitterEvent = new ObjectMapper().readValue(jsonString, SseEmitterEvent.class);
            return sseEmitterEvent;
        }
    }
}
