package com.codekatabattle.codebattle.DTO;

public class EvaluationRequestDTO {
    private Integer projectId;
    private Float personalScore;
    private String educatorEmail;

    public Integer getProjectId() {
        return projectId;
    }
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
    public Float getPersonalScore() {
        return personalScore;
    }
    public void setPersonalScore(Float personalScore) {
        this.personalScore = personalScore;
    }
    public String getEducatorEmail() {
        return educatorEmail;
    }
    public void setEducatorEmail(String educatorEmail) {
        this.educatorEmail = educatorEmail;
    }
    
}
