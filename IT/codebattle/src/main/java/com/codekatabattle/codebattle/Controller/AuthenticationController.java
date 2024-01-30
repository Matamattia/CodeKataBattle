package com.codekatabattle.codebattle.Controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codekatabattle.codebattle.DTO.JwtResponse;
import com.codekatabattle.codebattle.Model.Educator;
import com.codekatabattle.codebattle.Model.Student;
import com.codekatabattle.codebattle.Service.JwtUtils;
import com.codekatabattle.codebattle.Service.LoginService;
import com.codekatabattle.codebattle.Service.SignupService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final LoginService loginService;
    private final SignupService signupService;
    private final JwtUtils jwtUtils; // Servizio per la gestione dei JWT

    

    

    public AuthenticationController(LoginService loginService, SignupService signupService, JwtUtils jwtUtils) {
        this.loginService = loginService;
        this.signupService = signupService;
        this.jwtUtils = jwtUtils;
    }

    // Login Educator
    @PostMapping("/login/educator")
  public ResponseEntity<?> loginEducator(@RequestBody Educator educatorDTO) {
        Educator educator = loginService.loginEducator(educatorDTO.getEmail(), educatorDTO.getPassword());
        if (educator != null) {
            // Genera il JWT per l'utente autenticato
            String token = jwtUtils.generateToken(educator.getEmail(),"educator");//aggiunto
            return ResponseEntity.ok(new JwtResponse(token)); // Crea una classe di risposta per il JWT
        }
        return ResponseEntity.status(401).build(); // Non autorizzato
    }

    // Login Student
    @PostMapping("/login/student")
    public ResponseEntity<?> loginStudent(@RequestBody Student studentDTO) {
        Student student = loginService.loginStudent(studentDTO.getEmail(), studentDTO.getPassword());
        if (student != null) {
            String token = jwtUtils.generateToken(student.getEmail(),"student");
            return ResponseEntity.ok(new JwtResponse(token));
        }
        return ResponseEntity.status(401).build();
    }

    // Signup Educator
    @PostMapping("/signup/educator")
    public ResponseEntity<Educator> registerEducator(@RequestBody Educator educatorDTO) {
        Educator newEducator = new Educator();
        newEducator.setEmail(educatorDTO.getEmail());
        newEducator.setName(educatorDTO.getName());
        newEducator.setPassword(educatorDTO.getPassword());
        newEducator.setSurname(educatorDTO.getSurname());
        Educator registeredEducator = signupService.registerEducator(newEducator);
        return ResponseEntity.ok(registeredEducator);
    }

    // Signup Student
    @PostMapping("/signup/student")
    public ResponseEntity<Student> registerStudent(@RequestBody Student studentDTO) {
        Student newStudent = new Student();
        newStudent.setEmail(studentDTO.getEmail());
        newStudent.setName(studentDTO.getName());
        newStudent.setPassword(studentDTO.getPassword());
        newStudent.setSurname(studentDTO.getSurname());
        Student registeredStudent = signupService.registerStudent(newStudent);
        return ResponseEntity.ok(registeredStudent);
    }


  

    
}
