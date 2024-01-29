package com.codekatabattle.codebattle.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.codekatabattle.codebattle.Model.Student;
import com.codekatabattle.codebattle.Service.StudentService;

@RestController
@RequestMapping("/api/students")
public class StudentInfoController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ResponseEntity<Student> getStudentByEmail(@RequestParam String email) {
        return studentService.getStudentByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
