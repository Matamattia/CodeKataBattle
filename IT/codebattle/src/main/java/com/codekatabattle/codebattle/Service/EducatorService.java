package com.codekatabattle.codebattle.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codekatabattle.codebattle.Model.Educator;
import com.codekatabattle.codebattle.Repository.EducatorRepository;
@Service
public class EducatorService {
    @Autowired
    EducatorRepository educatorRepository;

    public Optional<Educator> getEducatorByEmail(String email) {
        return educatorRepository.findById(email);
    }
}
