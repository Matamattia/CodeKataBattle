package com.codekatabattle.codebattle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.codekatabattle.codebattle.Model.Educator;
import com.codekatabattle.codebattle.Repository.EducatorRepository;
import com.codekatabattle.codebattle.Repository.StudentRepository;
import com.codekatabattle.codebattle.Service.LoginService;

public class LoginServiceTest {

    private LoginService loginService;
    private EducatorRepository educatorRepository;
    private StudentRepository studentRepository;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        // creo oggetti simulati 
        educatorRepository = mock(EducatorRepository.class);
        studentRepository = mock(StudentRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        loginService = new LoginService(educatorRepository, studentRepository, passwordEncoder);
    }

    @Test
    public void testLoginEducatorSuccess() {
        String email = "educator@example.com";
        String password = "password123";
        Educator educator = new Educator();
        educator.setEmail(email);
        educator.setPassword(passwordEncoder.encode(password));

        when(educatorRepository.findById(email)).thenReturn(java.util.Optional.of(educator));
        when(passwordEncoder.matches(password, educator.getPassword())).thenReturn(true);

        Educator result = loginService.loginEducator(email, password);
        assertNotNull(result);
    }

    @Test
    public void testLoginEducatorInvalidEmail() {
        String email = "invalid@example.com";
        String password = "password123";

        when(educatorRepository.findById(email)).thenReturn(java.util.Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            loginService.loginEducator(email, password);
        });
    }

    @Test
    public void testLoginEducatorInvalidPassword() {
        String email = "educator@example.com";
        String password = "invalidpassword";
        Educator educator = new Educator();
        educator.setEmail(email);
        educator.setPassword(passwordEncoder.encode("password123"));

        when(educatorRepository.findById(email)).thenReturn(java.util.Optional.of(educator));
        when(passwordEncoder.matches(password, educator.getPassword())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            loginService.loginEducator(email, password);
        });
    }

   
}
