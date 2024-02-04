package com.codekatabattle.codebattle.DTO;

public class TournamentRankingDTO {
    private String studentEmail;
    private Double totalScore;

    public TournamentRankingDTO(String studentEmail, Double totalScore) {
        this.studentEmail = studentEmail;
        this.totalScore = totalScore;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }
    
    
}
