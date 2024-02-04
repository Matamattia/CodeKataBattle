package com.codekatabattle.codebattle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.codekatabattle.codebattle.Service.ProjectService;
import com.codekatabattle.codebattle.Model.Project;
import com.codekatabattle.codebattle.Model.Team;
import com.codekatabattle.codebattle.Repository.ProjectRepository;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;
/*Questo test verifica che un nuovo progetto venga creato se non esiste un progetto con la stessa URL della repository GitHub. */
@Test
public void testCreateNewProject() {
    Team team = new Team();
    String githubRepositoryUrl = "https://github.com/example/new";
    byte[] codeKataTeam = new byte[]{1, 2, 3};

    when(projectRepository.findByGithubRepository(githubRepositoryUrl)).thenReturn(null);
    when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Project newProject = projectService.createOrUpdateProject(team, githubRepositoryUrl, codeKataTeam);

    assertNotNull(newProject, "Il progetto restituito non dovrebbe essere null");
    assertEquals(githubRepositoryUrl, newProject.getGithubRepository(), "L'URL della repository non corrisponde");
    assertArrayEquals(codeKataTeam, newProject.getCodeKataTeam(), "Il contenuto di codeKataTeam non corrisponde");
}

/*Questo test verifica che un progetto esistente venga correttamente aggiornato se viene trovato un progetto con la stessa URL della repository GitHub. */
@Test
public void testUpdateExistingProject() {
    String githubRepositoryUrl = "https://github.com/example/existing";
    byte[] codeKataTeam = new byte[]{4, 5, 6};
    Team team = new Team();
    Project existingProject = new Project();
    existingProject.setTeam(team);
    existingProject.setGithubRepository(githubRepositoryUrl);
    existingProject.setCodeKataTeam(new byte[]{1, 2, 3});

    when(projectRepository.findByGithubRepository(githubRepositoryUrl)).thenReturn(existingProject);
    when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Project updatedProject = projectService.createOrUpdateProject(team, githubRepositoryUrl, codeKataTeam);

    assertNotNull(updatedProject);
    assertEquals(githubRepositoryUrl, updatedProject.getGithubRepository());
    assertArrayEquals(codeKataTeam, updatedProject.getCodeKataTeam());
    verify(projectRepository, times(1)).save(existingProject);
}


}

