package com.codekatabattle.codebattle.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codekatabattle.codebattle.Model.Educator;

public interface EducatorRepository extends JpaRepository<Educator, String>{
    
}
