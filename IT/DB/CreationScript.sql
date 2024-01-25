
CREATE TABLE Student (
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    name VARCHAR(255),
    surname VARCHAR(255),
    PRIMARY KEY (email)
);


CREATE TABLE Educator (
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    name VARCHAR(255),
    surname VARCHAR(255),
    PRIMARY KEY (email)
);


CREATE TABLE Tournament (
    id INT NOT NULL,
    isOpen BOOLEAN,
    name VARCHAR(255),
    registrationDeadline DATE,
    description TEXT,
    PRIMARY KEY (id)
);

CREATE TABLE AuthorizedEducators(
	tournamentId INT,
	authorizedEducator VARCHAR(255),
	PRIMARY KEY (tournamentId,authorizedEducator),
    FOREIGN KEY (authorizedEducator) REFERENCES Educator(email),
    FOREIGN KEY (tournamentId) REFERENCES Tournament(id)
    
);

CREATE TABLE Battle (
    battleId INT NOT NULL,
    tournamentId INT NOT NULL,
    linkRepository TEXT,
    minStudent INT,
    maxStudent INT,
    descriptionCodeKata TEXT,
	codeKataTests bytea,
    registrationDeadline DATE,
    submissionDeadline DATE,
    isEvaluatedManual boolean,
    PRIMARY KEY (tournamentId, battleId),
    FOREIGN KEY (tournamentId) REFERENCES Tournament(id)
);


CREATE TABLE Team (
    teamId INT NOT NULL,
    name VARCHAR(255),
    battleId INT NOT NULL,
	tournamentId INT NOT NULL,
    PRIMARY KEY (teamId),
    FOREIGN KEY (battleId, tournamentId) REFERENCES Battle(battleId, tournamentId)
);


CREATE TABLE TeamParticipant (
    teamId INT NOT NULL,
    studentEmail VARCHAR(255) NOT NULL,
    PRIMARY KEY (teamId, studentEmail),
    FOREIGN KEY (teamId) REFERENCES Team(teamId),
    FOREIGN KEY (studentEmail) REFERENCES Student(email)
);


CREATE TABLE Project (
    projectId INT NOT NULL,
    teamId INT NOT NULL,
    githubRepository TEXT,
	CodeKataTeam bytea,
    PRIMARY KEY (projectId),
    FOREIGN KEY (teamId) REFERENCES Team(teamId)
);




CREATE TABLE AutomatedEvaluation (
    projectId INT NOT NULL,
    functionalScore FLOAT,
    timelinessScore FLOAT,
    totalScore FLOAT,
    PRIMARY KEY (projectId),
    FOREIGN KEY (projectId) REFERENCES Project(projectId)
);


CREATE TABLE ManualEvaluation (
    automatedEvaluationId INT NOT NULL,
    educatorEmail VARCHAR(255) NOT NULL,
    personalScore FLOAT,
    PRIMARY KEY (automatedEvaluationId),
    FOREIGN KEY (automatedEvaluationId) REFERENCES AutomatedEvaluation(projectId),
    FOREIGN KEY (educatorEmail) REFERENCES Educator(email)
);


CREATE TABLE TournamentRanking (
    tournamentId INT NOT NULL,
    studentEmail VARCHAR(255) NOT NULL,
	score FLOAT,
    PRIMARY KEY (tournamentId, studentEmail),
    FOREIGN KEY (tournamentId) REFERENCES Tournament(id),
    FOREIGN KEY (studentEmail) REFERENCES Student(email)
);


CREATE TABLE BattleRanking (
    battleId INT NOT NULL,
    tournamentId INT NOT NULL,
    teamId INT NOT NULL,
	score FLOAT,
    PRIMARY KEY (battleId, tournamentId,teamId),
    FOREIGN KEY (battleId, tournamentId) REFERENCES Battle(battleId, tournamentId),
    FOREIGN KEY (teamId) REFERENCES Team(teamId)
);