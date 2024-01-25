package com.codekatabattle.codebattle.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "Project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projectid")
    private Integer projectId;

    @ManyToOne
    @JoinColumn(name = "teamid", referencedColumnName = "teamid")
    private Team team;

    @Column(name = "githubrepository", columnDefinition = "TEXT")
    private String githubRepository;

    @Column(name = "codekatateam")
    private byte[] codeKataTeam;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getGithubRepository() {
        return githubRepository;
    }

    public void setGithubRepository(String githubRepository) {
        this.githubRepository = githubRepository;
    }

    public byte[] getCodeKataTeam() {
        return codeKataTeam;
    }

    public void setCodeKataTeam(byte[] codeKataTeam) {
        this.codeKataTeam = codeKataTeam;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((projectId == null) ? 0 : projectId.hashCode());
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
        Project other = (Project) obj;
        if (projectId == null) {
            if (other.projectId != null)
                return false;
        } else if (!projectId.equals(other.projectId))
            return false;
        return true;
    }

    
}
