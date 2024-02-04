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
import com.codekatabattle.codebattle.Model.Battle.BattleId;
import com.codekatabattle.codebattle.Repository.BattleRepository;
import com.codekatabattle.codebattle.Repository.TeamParticipantRepository;
import com.codekatabattle.codebattle.Repository.TeamRepository;
import com.codekatabattle.codebattle.Service.GitHubService;
import com.codekatabattle.codebattle.Service.MailService;
import com.codekatabattle.codebattle.Service.RankingService;

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
  @Autowired
  private RankingService rankingService;

  public void execute(JobExecutionContext context) {

    JobDataMap dataMap = context.getJobDetail().getJobDataMap();
    // Battle.BattleId battleId = (Battle.BattleId) dataMap.get("battleId");
    BattleId battleId = new BattleId();
    battleId.setBattleId((Integer) dataMap.get("battleId"));
    battleId.setTournament((Integer) dataMap.get("tournamentId"));

    String deadlineType = dataMap.getString("deadlineType");

    Optional<Battle> battleOpt = battleRepository.findById(battleId);
    if (battleOpt.isPresent()) {
      String fileType = battleOpt.get().getFileType();

      if (fileType == null || fileType.isEmpty()) {
        fileType = "txt"; // Valore predefinito se non specificato
      }

      if ("Registration".equals(deadlineType)) {
        try {
          // Crea il repository su GitHub per la battaglia
          gitHubService.createGitHubRepositoryForBattle(battleOpt.get());

          // Ottieni tutti i team iscritti alla battaglia
          List<Team> teams = teamRepository.findByBattle_BattleIdAndBattle_Tournament_Id(
              battleId.getBattleId(), battleId.getTournament());

          // Assicurati di ottenere l'URL effettivo del repository
          String repositoryLink = String.format("https://github.com/codekatabattlese/Battle%d", battleId.getBattleId());

          // Contenuto YAML da configurare
          String yamlContentTemplate = """
              name: Notify Platform on Push

              on:
                push:
                  branches:
                    - main  # Sostituisci con il nome del tuo branch principale se necessario

              jobs:
                notify-platform:
                  runs-on: ubuntu-latest

                  steps:
                  - name: Checkout code
                    uses: actions/checkout@v2

                  - name: Encode file content to Base64
                    id: file_content
                    run: |
                      content=$(base64 -w 0 CodeKataBattle.%s)  # Codifica il contenuto del file in Base64
                      echo "::set-output name=content::$content"

                  - name: Format push time for LocalDateTime
                    id: formatted_time
                    run: |
                      push_time=$(date -u -d "${{ github.event.head_commit.timestamp }}" +"%%Y-%%m-%%dT%%H:%%M:%%S")
                      echo "::set-output name=push_time::$push_time"

                  - name: Extract repository name
                    id: repo_name
                    run: |
                      repo_name="${GITHUB_REPOSITORY##*/}"
                      echo "::set-output name=repo_name::$repo_name"

                  - name: Notify CKB Platform
                    env:
                      NGROK_URL: "https://14f9-151-73-39-240.ngrok-free.app/webhook"
                      CONTENT: ${{ steps.file_content.outputs.content }}
                      PUSH_TIME: ${{ steps.formatted_time.outputs.push_time }}
                      REPO_NAME: ${{ steps.repo_name.outputs.repo_name }}
                    run: |
                      payload='{
                        "eventId": "ID_EVENTO_O_UNIVOCO",
                        "repoUrl": "'"${REPO_NAME}"'",
                        "timestamp": "'"${PUSH_TIME}"'",
                        "functionalScore": 0,
                        "codeKataBattle": "'"${CONTENT}"'"
                      }'

                      curl -X POST "$NGROK_URL" \
                        -H "Content-Type: application/json" \
                        -d "$payload"
              """;

          String yamlContent = String.format(yamlContentTemplate, fileType);

          // Istruzioni per configurare il file YML
          String yamlConfigurationInstructions = String.format(
              """
                  Gentile team,

                  Congratulazioni! La battaglia con ID %d è ufficialmente iniziata. Per partecipare, segui attentamente i passaggi sottostanti:

                  1. Assicurati che il nome della tua repository su GitHub corrisponda esattamente al nome del tuo team.
                  2. Configura GitHub Actions inserendo il seguente contenuto YAML al percorso .github/workflows/main.yml nella tua repository GitHub:
                  ```
                  %s
                  ```
                  3. Sostituisci `NGROK_URL` con l'URL effettivo della tua web app. Questo è essenziale per garantire la corretta comunicazione con la piattaforma durante la competizione.

                  Puoi accedere al repository della battaglia qui: %s

                  Per qualsiasi domanda o necessità, non esitare a contattarci. Buona fortuna!

                  Cordiali saluti,
                  CodeKataBattle
                  """,
              battleId.getBattleId(), yamlContent, repositoryLink);

          // Invia l'email a tutti gli studenti dei team iscritti
          for (Team team : teams) {
            List<String> studentEmails = teamParticipantRepository
                .findStudentEmailsByTeamId(team.getTeamId());
            for (String email : studentEmails) {
              String subject = "La battaglia è iniziata!";
              String message = String.format(
                  "La battaglia con ID %d è ora iniziata. %s",
                  battleId.getBattleId(), yamlConfigurationInstructions);
              mailService.sendMail(email, new MailStructure(subject, message));
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      else if ("Submission".equals(deadlineType)) {
        // Esegui l'azione per la submissionDeadline
        // richiamo calculate ranking
        // notify the students via mail
        rankingService.createTournamentRanking(battleId.getTournament());
        List<Team> teams = teamRepository.findByBattle_BattleIdAndBattle_Tournament_Id(battleId.getBattleId(),
            battleId.getTournament());
        for (Team team : teams) {
          List<String> emailStudent = teamParticipantRepository.findStudentEmailsByTeamId(team.getTeamId());
          for (String email : emailStudent) {
            String subj = "Ranking ";
            mailService.sendMail(email, new MailStructure(subj,
                "Ranking ready for Battle: " + Integer.toString(battleId.getBattleId())));
          }
        }
      }
    }
  }
}