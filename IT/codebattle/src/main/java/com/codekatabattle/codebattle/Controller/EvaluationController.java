package com.codekatabattle.codebattle.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codekatabattle.codebattle.DTO.EvaluationRequestDTO;
import com.codekatabattle.codebattle.Model.ManualEvaluation;
import com.codekatabattle.codebattle.Service.EvaluationService;

@RestController
@RequestMapping("/evaluation")
public class EvaluationController {
    @Autowired
    private EvaluationService evaluationService;

    @PostMapping("/evaluateProject")
    public ResponseEntity<ManualEvaluation> evaluateProject(@RequestBody EvaluationRequestDTO request) {
        try {
            ManualEvaluation evaluation = evaluationService.evaluateProject(request.getProjectId(), request.getPersonalScore(), request.getEducatorEmail());

            if (evaluation != null) {
                return ResponseEntity.ok(evaluation);
            } else {
               
                return ResponseEntity.badRequest().body(null);
            }
        } catch (Exception e) {
            
            return ResponseEntity.internalServerError().build();
        }
    }
}
