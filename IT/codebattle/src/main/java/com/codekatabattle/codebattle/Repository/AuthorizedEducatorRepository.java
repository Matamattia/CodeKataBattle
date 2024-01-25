package com.codekatabattle.codebattle.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codekatabattle.codebattle.Model.AuthorizedEducator;
import com.codekatabattle.codebattle.Model.AuthorizedEducator.AuthorizedEducatorsId;

public interface AuthorizedEducatorRepository extends JpaRepository<AuthorizedEducator,AuthorizedEducatorsId >{
    
}
