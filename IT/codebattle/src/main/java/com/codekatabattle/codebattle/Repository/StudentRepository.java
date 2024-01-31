package com.codekatabattle.codebattle.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codekatabattle.codebattle.Model.Student;

public interface StudentRepository extends JpaRepository<Student, String>{
    
    
}
