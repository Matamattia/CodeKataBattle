package com.codekatabattle.codebattle.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.codekatabattle.codebattle.Model.Educator;
import com.codekatabattle.codebattle.Model.Student;
import com.codekatabattle.codebattle.Repository.EducatorRepository;
import com.codekatabattle.codebattle.Repository.StudentRepository;

@Service
public class LoginService {
    private final EducatorRepository  educatorRepository;
    private final StudentRepository  studentRepository;
    private final PasswordEncoder passwordEncoder;

    

    public LoginService(EducatorRepository educatorRepository, StudentRepository studentRepository,
            PasswordEncoder passwordEncoder) {
        this.educatorRepository = educatorRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //LOGIN EDUCATOR
    public Educator loginEducator(String email, String password) {
        Educator educator = educatorRepository.findById(email)
                              .orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        if (passwordEncoder.matches(password, educator.getPassword())) {
            return educator; // Login success
        } else {
            throw new IllegalArgumentException("Invalid password");
        }
    }

    //LOGIN STUDENT
    public Student loginStudent(String email, String password) {
        Student student = studentRepository.findById(email)
                              .orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        if (passwordEncoder.matches(password, student.getPassword())) {
            return student; // Login success
        } else {
            throw new IllegalArgumentException("Invalid password");
        }
    }
    
}
