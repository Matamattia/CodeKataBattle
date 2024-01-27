package com.codekatabattle.codebattle.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.codekatabattle.codebattle.Model.Educator;
import com.codekatabattle.codebattle.Model.Student;
import com.codekatabattle.codebattle.Repository.EducatorRepository;
import com.codekatabattle.codebattle.Repository.StudentRepository;

@Service
public class SignupService {
    private final EducatorRepository  educatorRepository;
    private final StudentRepository  studentRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupService(EducatorRepository educatorRepository, StudentRepository studentRepository,
            PasswordEncoder passwordEncoder) {
        this.educatorRepository = educatorRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }



    //REGISTRATION EDUCATOR
    public Educator registerEducator(Educator educator) {
        Assert.notNull(educator, "Educator object cannot be null");
        validateEducatorData(educator);

        educator.setPassword(passwordEncoder.encode(educator.getPassword()));
        return educatorRepository.save(educator);
    }

    private void validateEducatorData(Educator educator) {
        Assert.hasText(educator.getEmail(), "Email cannot be empty");
        Assert.isTrue(educator.getEmail().contains("@"), "Email is not valid");
        Assert.hasLength(educator.getPassword(), "Password must be at least 6 characters long");

        if (educatorRepository.existsById(educator.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
    }


    //REGISTRATION STUDENT

     public Student registerStudent(Student student) {
        Assert.notNull(student, "Student object cannot be null");
        validateStudentData(student);

        student.setPassword(passwordEncoder.encode(student.getPassword()));
        return studentRepository.save(student);
    }

    private void validateStudentData(Student student) {
        Assert.hasText(student.getEmail(), "Email cannot be empty");
        Assert.isTrue(student.getEmail().contains("@"), "Email is not valid");
        Assert.hasLength(student.getPassword(), "Password must be at least 6 characters long");

        if (studentRepository.existsById(student.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }


    }


    }
    
    

