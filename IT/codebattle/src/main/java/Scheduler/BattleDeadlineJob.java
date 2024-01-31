package Scheduler;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.codekatabattle.codebattle.Model.Battle;
import com.codekatabattle.codebattle.Model.MailStructure;
import com.codekatabattle.codebattle.Model.Team;
import com.codekatabattle.codebattle.Repository.BattleRepository;
import com.codekatabattle.codebattle.Repository.TeamParticipantRepository;
import com.codekatabattle.codebattle.Repository.TeamRepository;
import com.codekatabattle.codebattle.Service.GitHubService;
import com.codekatabattle.codebattle.Service.MailService;

public class BattleDeadlineJob implements Job {

    @Autowired
    private BattleRepository battleRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private TeamParticipantRepository teamParticipantRepository;

    @Autowired
    private GitHubService gitHubService;

    public void execute(JobExecutionContext context) {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        Battle.BattleId battleId = (Battle.BattleId) dataMap.get("battleId");
        String deadlineType = dataMap.getString("deadlineType");

        Optional<Battle> battleOpt = battleRepository.findById(battleId);
        if (battleOpt.isPresent()) {
            // Battle battle = battleOpt.get();
            if ("Registration".equals(deadlineType)) {
                try {
                    System.out.println("ENTRATO IN  BATTLEDEADLNE JOB");
                    gitHubService.createGitHubRepositoryForBattle(battleOpt.get());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // Esegui l'azione per la registrationDeadline
            } else if ("Submission".equals(deadlineType)) {
                // Esegui l'azione per la submissionDeadline
                // richiamo calculate ranking
                // notify the students via mail
                List<Team> teams = teamRepository.findByBattle_BattleIdAndBattle_Tournament_Id(battleId.getBattleId(),
                        battleId.getTournament());
                for (Team team : teams) {
                    List<String> emailStudent = teamParticipantRepository.findStudentEmailsByTeamId(team.getTeamId());
                    for (String email : emailStudent) {
                        String subj = " ";
                        mailService.sendMail(email, new MailStructure(subj,
                                "Ranking ready for Battle: " + Integer.toString(battleId.getBattleId())));
                    }
                }
            }
        }
    }
}
