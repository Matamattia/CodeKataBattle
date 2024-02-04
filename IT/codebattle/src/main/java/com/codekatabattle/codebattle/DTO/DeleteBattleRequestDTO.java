package com.codekatabattle.codebattle.DTO;

public class DeleteBattleRequestDTO {
    private Integer battleId;
    private Integer tournamentId;
    public Integer getBattleId() {
        return battleId;
    }
    public void setBattleId(Integer battleId) {
        this.battleId = battleId;
    }
    public Integer getTournamentId() {
        return tournamentId;
    }
    public void setTournamentId(Integer tournamentId) {
        this.tournamentId = tournamentId;
    }
    
}
