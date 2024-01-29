package com.codekatabattle.codebattle.Service;

import com.codekatabattle.codebattle.Model.Project;
import com.codekatabattle.codebattle.Model.Team;
import com.codekatabattle.codebattle.Repository.ProjectRepository;

public class ProjectService {
    private ProjectRepository projectRepository;


    public Project createOrUpdateProject(Team team, String githubRepositoryUrl, byte[] codeKataTeam) {
        // Cerca un progetto esistente basato sull'URL della repository e/o altre proprietà uniche
        Project existingProject = projectRepository.findByGithubRepository(githubRepositoryUrl);

        if (existingProject != null) {
            // Se il progetto esiste, aggiorna le proprietà necessarie
            existingProject.setCodeKataTeam(codeKataTeam);
            // Aggiorna altre proprietà se necessario
             return projectRepository.save(existingProject);
        } else {
            // Se non esiste, crea un nuovo progetto
            Project newProject = new Project();
            newProject.setTeam(team);
            newProject.setGithubRepository(githubRepositoryUrl);
            newProject.setCodeKataTeam(codeKataTeam);
            return projectRepository.save(newProject);
        }
    }

    
}
