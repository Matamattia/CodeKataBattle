package com.codekatabattle.codebattle.Model;
import jakarta.persistence.*;

@Entity
@Table(name = "Team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teamId")
    private int teamId;

    @Column(name = "name")
    private String name;

    @Column(name = "battleId")
    private int battleId;

    @Column(name = "tournamentId")
    private int tournamentId;

}
