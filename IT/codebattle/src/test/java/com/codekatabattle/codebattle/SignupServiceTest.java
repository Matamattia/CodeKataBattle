package com.codekatabattle.codebattle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.codekatabattle.codebattle.Model.Educator;
import com.codekatabattle.codebattle.Model.Student;
import com.codekatabattle.codebattle.Repository.EducatorRepository;
import com.codekatabattle.codebattle.Repository.StudentRepository;
import com.codekatabattle.codebattle.Service.SignupService;

@ExtendWith(MockitoExtension.class)
public class SignupServiceTest {

    @Mock
    private EducatorRepository educatorRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SignupService signupService;


/*Questo test verifica che un Educator venga registrato correttamente e che la password venga codificata prima del salvataggio. */
@Test
public void testRegisterEducator() {
    Educator educator = new Educator();
    educator.setEmail("educator@example.com");
    educator.setPassword("password");

    when(educatorRepository.existsById(educator.getEmail())).thenReturn(false);
    when(passwordEncoder.encode(educator.getPassword())).thenReturn("encodedPassword");
    when(educatorRepository.save(any(Educator.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Educator registeredEducator = signupService.registerEducator(educator);

    assertNotNull(registeredEducator);
    assertEquals("encodedPassword", registeredEducator.getPassword());
    verify(educatorRepository, times(1)).save(educator);
    verify(passwordEncoder, times(1)).encode("password");
}
/*Questo test verifica che uno Student venga registrato correttamente e che la password venga codificata prima del salvataggio. */
@Test
public void testRegisterStudent() {
    Student student = new Student();
    student.setEmail("student@example.com");
    student.setPassword("password");

    when(studentRepository.existsById(student.getEmail())).thenReturn(false);
    when(passwordEncoder.encode(student.getPassword())).thenReturn("encodedPassword");
    when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Student registeredStudent = signupService.registerStudent(student);

    assertNotNull(registeredStudent);
    assertEquals("encodedPassword", registeredStudent.getPassword());
    verify(studentRepository, times(1)).save(student);
    verify(passwordEncoder, times(1)).encode("password");
}
/*I dati degli utenti (educatori e studenti) vengano validati correttamente.
Le password vengano codificate prima del salvataggio.
Gli oggetti Educator e Student vengano salvati nel repository. */



//Educator con un'email già presente nel sistema.
@Test
public void testRegisterEducatorWithEmailAlreadyInUse() {
    Educator educator = new Educator();
    educator.setEmail("existing@example.com");
    educator.setPassword("password");

    when(educatorRepository.existsById(educator.getEmail())).thenReturn(true);

    assertThrows(IllegalArgumentException.class, () -> signupService.registerEducator(educator));

    verify(educatorRepository, never()).save(any(Educator.class));
}
//Questo test verifica che un'eccezione sia sollevata quando si tenta di registrare uno Student con un'email già presente nel sistema.
@Test
public void testRegisterStudentWithEmailAlreadyInUse() {
    Student student = new Student();
    student.setEmail("existing@example.com");
    student.setPassword("password");

    when(studentRepository.existsById(student.getEmail())).thenReturn(true);

    assertThrows(IllegalArgumentException.class, () -> signupService.registerStudent(student));

    verify(studentRepository, never()).save(any(Student.class));
}

}
