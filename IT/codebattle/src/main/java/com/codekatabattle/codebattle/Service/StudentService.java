package com.codekatabattle.codebattle.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codekatabattle.codebattle.Model.Student;
import com.codekatabattle.codebattle.Repository.StudentRepository;

import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findById(email);
    }
}
