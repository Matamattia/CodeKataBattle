package com.codekatabattle.codebattle.DTO;

public class TournamentRankingDTO {
    private String studentEmail;
    private Float totalScore;

    public TournamentRankingDTO(String studentEmail, Float totalScore) {
        this.studentEmail = studentEmail;
        this.totalScore = totalScore;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public Float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Float totalScore) {
        this.totalScore = totalScore;
    }
    
    
}
