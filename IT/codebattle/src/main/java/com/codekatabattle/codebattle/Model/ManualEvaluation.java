
package com.codekatabattle.codebattle.Model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;

@Entity
@Table(name = "ManualEvaluation")
public class ManualEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manualid ")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "automatedevaluationid", referencedColumnName = "projectid")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AutomatedEvaluation automatedEvaluation;

    @ManyToOne
    @JoinColumn(name = "educatoremail", referencedColumnName = "email")
    private Educator educator;

    @Column(name = "personalscore")
    private Float personalScore;

    public AutomatedEvaluation getAutomatedEvaluation() {
        return automatedEvaluation;
    }

    public void setAutomatedEvaluation(AutomatedEvaluation automatedEvaluation) {
        this.automatedEvaluation = automatedEvaluation;
    }

    public Educator getEducator() {
        return educator;
    }

    public void setEducator(Educator educator) {
        this.educator = educator;
    }

    public Float getPersonalScore() {
        return personalScore;
    }

    public void setPersonalScore(Float personalScore) {
        this.personalScore = personalScore;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((automatedEvaluation == null) ? 0 : automatedEvaluation.hashCode());
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
        ManualEvaluation other = (ManualEvaluation) obj;
        if (automatedEvaluation == null) {
            if (other.automatedEvaluation != null)
                return false;
        } else if (!automatedEvaluation.equals(other.automatedEvaluation))
            return false;
        return true;
    }
    
}
