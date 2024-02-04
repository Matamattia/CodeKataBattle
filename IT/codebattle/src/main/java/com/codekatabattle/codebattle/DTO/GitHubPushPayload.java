package com.codekatabattle.codebattle.DTO;

import java.time.LocalDateTime;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GitHubPushPayload {
    @JsonProperty("eventId")
    private String eventId;

    @JsonProperty("repoUrl")
    private String repositoryName;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("timestamp")
    private LocalDateTime pushTime;

    @JsonProperty("functionalScore")
    private Float functionalScore;

    @JsonProperty("codeKataBattle")
    private byte[] codeKataBattle;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public LocalDateTime getPushTime() {
        return pushTime;
    }

    public void setPushTime(LocalDateTime pushTime) {
        this.pushTime = pushTime;
    }

    public Float getFunctionalScore() {
        return functionalScore;
    }

    public void setFunctionalScore(Float functionalScore) {
        this.functionalScore = functionalScore;
    }

    public byte[] getCodeKataBattle() {
        return codeKataBattle;
    }

    public void setCodeKataBattle(byte[] codeKataBattle) {
        this.codeKataBattle = codeKataBattle;
    }

    @Override
    public String toString() {
        return "GitHubPushPayload [eventId=" + eventId + ", repositoryName=" + repositoryName + ", pushTime=" + pushTime
                + ", functionalScore=" + functionalScore + ", codeKataBattle=" + Arrays.toString(codeKataBattle) + "]";
    }
    


}


