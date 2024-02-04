package com.codekatabattle.codebattle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.codekatabattle.codebattle.Controller.StudentInfoController;
import com.codekatabattle.codebattle.Model.Battle;
import com.codekatabattle.codebattle.Model.Student;
import com.codekatabattle.codebattle.Model.Tournament;
import com.codekatabattle.codebattle.Service.BattleService;
import com.codekatabattle.codebattle.Service.StudentService;
import com.codekatabattle.codebattle.Service.TournamentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(StudentInfoController.class)
class StudentInfoControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private TournamentService tournamentService;

    @MockBean
    private BattleService battleService;

    @InjectMocks
    private StudentInfoController studentInfoController;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getStudentInfo_WhenStudentExists() throws Exception {
        Student student = new Student();
        student.setEmail("test@example.com");
        student.setName("TestName");
        student.setSurname("TestSurname");

        List<Tournament> tournaments = new ArrayList<>();
        List<Battle> battles = new ArrayList<>();

        given(studentService.getStudentByEmail("test@example.com")).willReturn(Optional.of(student));
        given(tournamentService.myTournaments("test@example.com")).willReturn(tournaments);
        given(battleService.getBattlesForStudent("test@example.com")).willReturn(battles);

        mockMvc.perform(get("/api/students/{email}", "test@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.student.name").value("TestName"))
                .andExpect(jsonPath("$.tournaments").exists())
                .andExpect(jsonPath("$.battles").exists());
    }

    @Test
    void getAllStudents_ShouldReturnAllStudents() throws Exception {
        List<Student> students = new ArrayList<>();
        students.add(new Student()); // Aggiungi dettagli agli studenti se necessario

        given(studentService.getAllStudents()).willReturn(students);

        mockMvc.perform(get("/api/students/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists());
    }
}
