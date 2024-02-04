package com.codekatabattle.codebattle.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codekatabattle.codebattle.Model.Battle;
import com.codekatabattle.codebattle.Model.Student;
import com.codekatabattle.codebattle.Model.Tournament;
import com.codekatabattle.codebattle.Service.BattleService;
import com.codekatabattle.codebattle.Service.StudentService;
import com.codekatabattle.codebattle.Service.TournamentService;

@RestController
@RequestMapping("/api/students")
public class StudentInfoController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private BattleService battleService;

    @GetMapping("/{email}")
    public ResponseEntity<?> getStudentInfo(@PathVariable String email) {
        Optional<Student> studentOpt = studentService.getStudentByEmail(email);
        if (!studentOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Student student = studentOpt.get();
        List<Tournament> tournaments = tournamentService.myTournaments(email);
        List<Battle> battles = battleService.getBattlesForStudent(email);

        // Crea una risposta con tutte le informazioni necessarie
        Map<String, Object> response = new HashMap<>();
        response.put("student", student);
        response.put("tournaments", tournaments);
        response.put("battles", battles);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }
}
