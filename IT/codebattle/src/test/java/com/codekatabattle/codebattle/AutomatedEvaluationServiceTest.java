package com.codekatabattle.codebattle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;


import com.codekatabattle.codebattle.Model.Project;
import com.codekatabattle.codebattle.Model.AutomatedEvaluation;
import com.codekatabattle.codebattle.Service.AutomatedEvaluationService;
import com.codekatabattle.codebattle.Repository.AutomatedEvaluationRepository;

@ExtendWith(MockitoExtension.class)
public class AutomatedEvaluationServiceTest {

    @Mock
    private AutomatedEvaluationRepository automatedEvaluationRepository;

    @InjectMocks
    private AutomatedEvaluationService automatedEvaluationService;
/*Questo test verifica che una nuova valutazione automatizzata venga creata se non esiste una valutazione esistente per il progetto specificato. */
    @Test
public void testCreateNewEvaluation() {
    Project project = new Project();
    project.setProjectId(1);
    Float functionalScore = 80f;
    Float timelinessScore = 90f;

    when(automatedEvaluationRepository.findByProjectId(project.getProjectId())).thenReturn(null);

    automatedEvaluationService.createOrUpdateEvaluation(project, functionalScore, timelinessScore);

    verify(automatedEvaluationRepository).save(argThat(evaluation -> 
        evaluation.getProject().equals(project) &&
        evaluation.getFunctionalScore().equals(functionalScore) &&
        evaluation.getTimelinessScore().equals(timelinessScore) &&
        evaluation.getTotalScore().equals((functionalScore + timelinessScore) / 2)
    ));
}

/*Questo test verifica che una valutazione automatizzata esistente venga correttamente aggiornata con nuovi punteggi. */
@Test
public void testUpdateExistingEvaluation() {
    Project project = new Project();
    project.setProjectId(1);
    Float functionalScore = 85f;
    Float timelinessScore = 95f;
    AutomatedEvaluation existingEvaluation = new AutomatedEvaluation();
    existingEvaluation.setProject(project);
    existingEvaluation.setId(1);

    when(automatedEvaluationRepository.findByProjectId(project.getProjectId())).thenReturn(existingEvaluation);

    automatedEvaluationService.createOrUpdateEvaluation(project, functionalScore, timelinessScore);

    verify(automatedEvaluationRepository).save(argThat(evaluation -> 
        evaluation.getId().equals(existingEvaluation.getId()) &&
        evaluation.getFunctionalScore().equals(functionalScore) &&
        evaluation.getTimelinessScore().equals(timelinessScore) &&
        evaluation.getTotalScore().equals((functionalScore + timelinessScore) / 2)
    ));
}

}

