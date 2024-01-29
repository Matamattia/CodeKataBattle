package com.codekatabattle.codebattle.Service;

import org.kohsuke.github.*;
import org.springframework.stereotype.Service;

import com.codekatabattle.codebattle.Model.Battle;

import java.io.IOException;

@Service
public class GitHubService {

    // Per generare un "GitHub Token", dovrai accedere al tuo account GitHub, andare alle impostazioni del tuo profilo, 
    //quindi alla sezione "Developer settings" e quindi "Personal access tokens". Da lì, puoi
    // creare un nuovo token, specificando le autorizzazioni necessarie per il tuo servizio.
    //UNA VOLTA GENERATO VA IMPOSTATO COME VARIABILE D'AMBIENTE SUL SERVER
    // export GITHUB_TOKEN=il_tuo_token
    private final String gitHubToken = System.getenv("GITHUB_TOKEN");

    public void createGitHubRepositoryForBattle(Battle battle) throws IOException {
        GitHub github = GitHub.connectUsingOAuth(gitHubToken);
        GHRepository repo = github.createRepository("Battle")
                                  .description(battle.getDescriptionCodeKata())
                                  .autoInit(true)
                                  .create();

        // Aggiungi il file YAML di GitHub Actions
        addGitHubActionsWorkflow(repo, battle);
        addCodeKataBattleFile(repo,battle);
    }

    private void addGitHubActionsWorkflow(GHRepository repo, Battle battle) throws IOException {
        String workflowYAML = generateWorkflowYAML(battle);
        repo.createContent()
            .path(".github/workflows/main.yml")
            .content(workflowYAML)
            .message("Add GitHub Actions workflow")
            .commit();
    }

    //CONTROLLA ENDPOINT!
    private String generateWorkflowYAML(Battle battle) {
        return 
            "name: Code Kata CI\n" +
            "on: [push]\n" +
            "jobs:\n" +
            "  build-and-test:\n" +
            "    runs-on: ubuntu-latest\n" +
            "    steps:\n" +
            "    - uses: actions/checkout@v2\n" +
            "    - name: Set up JDK 11\n" +
            "      uses: actions/setup-java@v2\n" +
            "      with:\n" +
            "        java-version: '11'\n" +
            "        distribution: 'adopt'\n" +
            "    - name: Build and Run Tests\n" +
            "      run: mvn test\n" +
            "    - name: Calculate Test Results\n" +
            "      run: bash .Scripts/calculate_tests.sh\n" +
            "    - name: Notify CKB Platform\n" +
            "      run: >\n" +
            "        curl -X POST -H \"Content-Type: multipart/form-data\" \\\n" +
            "        -F \"eventId=push\" \\\n" +
            "        -F \"repoUrl=${{ github.repository }}\" \\\n" +
            "        -F \"timestamp=${{ github.event.head_commit.timestamp }}\" \\\n" +
            "        -F \"functionalScore=${{ env.PASSED_TESTS / env.TOTAL_TESTS }}\" \\\n" +
            "        -F \"codeKataBattle=@CodeKataBattle." + battle.getFileType() + "\" \\\n" +
            "        webhook";
    }
    
    





    private void addCodeKataBattleFile(GHRepository repo, Battle battle) throws IOException {

    String fileType = battle.getFileType();
    String filePath = "CodeKataBattle." + fileType;

    // Aggiungi il file al repository
    repo.createContent()
        .path(filePath)
        .content(battle.getCodeKataTests())
        .message("Add CodeKataBattle.java")
        .commit();
    }
}
