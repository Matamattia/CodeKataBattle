// -testLoginStudentSuccess: This test verifies that when a valid student attempts to log in with the
//  correct email and password, the controller returns a 200 (OK) response with a JWT token in the
//   response body

// -testLoginStudentFailure: This test checks that when a student provides incorrect login credentials,
//  the controller returns a 401 (Unauthorized) response.

// -testRegisterEducator: This test ensures that when an educator registers with valid data, 
// the controller returns a 200 (OK) response with the registered educator's details in the
//  response body. It also checks if the registerEducator method properly 
// calls the signupService to register the educator.



//-testLoginEducatorSuccess : The purpose of this test is to ensure that when a valid educator logs in, the controller
//  correctly processes the login request, generates a JWT token, and returns it in the response.
//   This helps verify that the authentication mechanism
//  is working as expected for educators with valid credentials.















package com.codekatabattle.codebattle;
import com.codekatabattle.codebattle.Controller.AuthenticationController;
import com.codekatabattle.codebattle.DTO.JwtResponse;
import com.codekatabattle.codebattle.Model.Educator;
import com.codekatabattle.codebattle.Model.Student;
import com.codekatabattle.codebattle.Service.JwtUtils;
import com.codekatabattle.codebattle.Service.LoginService;
import com.codekatabattle.codebattle.Service.SignupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class AuthenticationControllerTest {

    private AuthenticationController authenticationController;
    private LoginService loginService;
    private SignupService signupService;
    private JwtUtils jwtUtils;

    @BeforeEach
    public void setUp() {
        loginService = Mockito.mock(LoginService.class);
        signupService = Mockito.mock(SignupService.class);
        jwtUtils = Mockito.mock(JwtUtils.class);
        authenticationController = new AuthenticationController(loginService, signupService, jwtUtils);
    }

    @Test
    public void testLoginEducatorSuccess() {
        // Arrange
        Educator educatorDTO = new Educator();
        educatorDTO.setEmail("educator@example.com");
        educatorDTO.setPassword("password");

        Educator educator = new Educator();
        educator.setEmail("educator@example.com");
        educator.setPassword("password");

        when(loginService.loginEducator(anyString(), anyString())).thenReturn(educator);
        when(jwtUtils.generateToken(anyString(), anyString())).thenReturn("jwt-token");

        // Act
        ResponseEntity<?> responseEntity = authenticationController.loginEducator(educatorDTO);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();
        assertEquals("jwt-token", jwtResponse.getToken());
    }

    @Test
    public void testLoginEducatorFailure() {
        // Arrange
        Educator educatorDTO = new Educator();
        educatorDTO.setEmail("educator@example.com");
        educatorDTO.setPassword("wrong-password");

        when(loginService.loginEducator(anyString(), anyString())).thenReturn(null);

        // Act
        ResponseEntity<?> responseEntity = authenticationController.loginEducator(educatorDTO);

        // Assert
        assertEquals(401, responseEntity.getStatusCodeValue());
    }


    @Test
    public void testLoginStudentSuccess() {
        // Arrange
        Student studentDTO = new Student();
        studentDTO.setEmail("student@example.com");
        studentDTO.setPassword("password");

        Student student = new Student();
        student.setEmail("student@example.com");
        student.setPassword("password");

        when(loginService.loginStudent(anyString(), anyString())).thenReturn(student);
        when(jwtUtils.generateToken(anyString(), anyString())).thenReturn("jwt-token");

        // Act
        ResponseEntity<?> responseEntity = authenticationController.loginStudent(studentDTO);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();
        assertEquals("jwt-token", jwtResponse.getToken());
    }

    @Test
    public void testLoginStudentFailure() {
        // Arrange
        Student studentDTO = new Student();
        studentDTO.setEmail("student@example.com");
        studentDTO.setPassword("wrong-password");

        when(loginService.loginStudent(anyString(), anyString())).thenReturn(null);

        // Act
        ResponseEntity<?> responseEntity = authenticationController.loginStudent(studentDTO);

        // Assert
        assertEquals(401, responseEntity.getStatusCodeValue());
    }

    @Test
public void testRegisterEducator() {
    // Arrange
    Educator educatorDTO = new Educator();
    educatorDTO.setEmail("educator@example.com");
    educatorDTO.setPassword("password");
    educatorDTO.setName("John");
    educatorDTO.setSurname("Doe");

    Educator registeredEducator = new Educator();
    registeredEducator.setEmail("educator@example.com");
    registeredEducator.setPassword("password");
    registeredEducator.setName("John");
    registeredEducator.setSurname("Doe");

    when(signupService.registerEducator(Mockito.any(Educator.class))).thenReturn(registeredEducator);

    // Act
    ResponseEntity<Educator> responseEntity = authenticationController.registerEducator(educatorDTO);

    // Assert
    assertEquals(200, responseEntity.getStatusCodeValue());
    Educator responseEducator = responseEntity.getBody();
    assertEquals("educator@example.com", responseEducator.getEmail());
    // You can add more assertions to check other properties of the registered educator.
}
 }
