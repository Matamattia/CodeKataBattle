package com.codekatabattle.codebattle.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codekatabattle.codebattle.Model.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer>{
    
}
