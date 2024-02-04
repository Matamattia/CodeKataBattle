package com.codekatabattle.codebattle.Repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codekatabattle.codebattle.Model.AuthorizedEducator;
import com.codekatabattle.codebattle.Model.AuthorizedEducator.AuthorizedEducatorsId;
import com.codekatabattle.codebattle.Model.Educator;

public interface AuthorizedEducatorRepository extends JpaRepository<AuthorizedEducator,AuthorizedEducatorsId >{
    
    @Query("SELECT ae FROM AuthorizedEducator ae WHERE LOWER(ae.educator.email) = LOWER(:email)")
List<AuthorizedEducator> findByEducatorEmail(String email);

    List<AuthorizedEducator> findByEducator(Educator educator);

}
