package com.codekatabattle.codebattle;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.codekatabattle.codebattle.Model.Educator;
import com.codekatabattle.codebattle.Model.Student;
import com.codekatabattle.codebattle.Repository.EducatorRepository;
import com.codekatabattle.codebattle.Repository.StudentRepository;
import com.codekatabattle.codebattle.Service.LoginService;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private EducatorRepository educatorRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginService loginService;

    @Test
    //Verificheremo il successo del login e il lancio di un'eccezione in caso di credenziali non valide.
public void loginEducatorSuccess() {
    
    String email = "educator@example.com";
    String rawPassword = "password";
    String encodedPassword = "encodedPassword";
    
    Educator educator = new Educator();
    educator.setEmail(email);
    educator.setPassword(encodedPassword);
    
    when(educatorRepository.findById(email)).thenReturn(Optional.of(educator));
    when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
    
    // Esecuzione & Verifica
    Educator result = loginService.loginEducator(email, rawPassword);
    assertEquals(email, result.getEmail());
}

@Test
public void loginEducatorInvalidEmail() {
    // Configurazione
    when(educatorRepository.findById(anyString())).thenReturn(Optional.empty());
    
    // Esecuzione & Verifica
    assertThrows(IllegalArgumentException.class, () -> loginService.loginEducator("wrong@example.com", "password"));
}


//Similmente al test per loginEducator, verificheremo il successo del login e il caso di fallimento.

@Test
public void loginStudentSuccess() {
    // Configurazione
    String email = "student@example.com";
    String rawPassword = "password";
    String encodedPassword = "encodedPassword";
    
    Student student = new Student();
    student.setEmail(email);
    student.setPassword(encodedPassword);
    
    when(studentRepository.findById(email)).thenReturn(Optional.of(student));
    when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
    
    // Esecuzione & Verifica
    Student result = loginService.loginStudent(email, rawPassword);
    assertEquals(email, result.getEmail());
}

@Test
public void loginStudentInvalidPassword() {
    // Configurazione
    String email = "student@example.com";
    String rawPassword = "wrongPassword";
    String encodedPassword = "encodedPassword";
    
    Student student = new Student();
    student.setEmail(email);
    student.setPassword(encodedPassword);
    
    when(studentRepository.findById(email)).thenReturn(Optional.of(student));
    when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);
    
    // Esecuzione & Verifica
    assertThrows(IllegalArgumentException.class, () -> loginService.loginStudent(email, rawPassword));
}
/*Questi test verificano che il LoginService gestisca correttamente i casi di login riuscito e fallito, basandosi sull'esistenza
dell'utente nel database e sulla corrispondenza delle password.  */


}

