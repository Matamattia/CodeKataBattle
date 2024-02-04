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
        String repositoryName = "Battle" + battle.getBattleId(); // Aggiungi l'ID della battaglia al nome
        GHRepository repo = github.createRepository(repositoryName)
        .description(battle.getDescriptionCodeKata())
                                  .autoInit(true)
                                  .create();
 
        
       
        addCodeKataBattleFile(repo,battle);
    }
 
 
    private void addCodeKataBattleFile(GHRepository repo, Battle battle) throws IOException {
 
    String fileType = battle.getFileType();
    String filePath = "CodeKataBattle." + fileType;
 
    
    repo.createContent()
        .path(filePath)
        .content(battle.getCodeKataTests())
        .message("Add CodeKataBattle.java")
        .commit();
    }
}
