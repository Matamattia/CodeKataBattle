package com.codekatabattle.codebattle.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "AutomatedEvaluation")
public class AutomatedEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "automatedid")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "projectid", referencedColumnName = "projectid")
    private Project project;

    @Column(name = "functionalScore")
    private Float functionalScore;

    @Column(name = "timelinessScore")
    private Float timelinessScore;

    @Column(name = "totalScore")
    private Float totalScore;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Float getFunctionalScore() {
        return functionalScore;
    }

    public void setFunctionalScore(Float functionalScore) {
        this.functionalScore = functionalScore;
    }

    public Float getTimelinessScore() {
        return timelinessScore;
    }

    public void setTimelinessScore(Float timelinessScore) {
        this.timelinessScore = timelinessScore;
    }

    public Float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Float totalScore) {
        this.totalScore = totalScore;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((project == null) ? 0 : project.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AutomatedEvaluation other = (AutomatedEvaluation) obj;
        if (project == null) {
            if (other.project != null)
                return false;
        } else if (!project.equals(other.project))
            return false;
        return true;
    }

    
}
