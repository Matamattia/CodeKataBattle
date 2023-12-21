open util/boolean

sig Text{}
sig Day{}
sig Month{}
sig Year{}
sig Hour{}
sig Minute{}
sig Second{}
sig Date{
  day: one Day,
  month: one Month,
  year: one Year,
  hour: one Hour,
  minute: one Minute,
  second: one Second
}
sig Score{}

abstract sig User {
  email: one Text,
  password: one Text,
  name: one Text,
  surname: one Text
}

sig Student extends User {
  // A student can subscribe to zero or more tournaments
  subscribedTournaments: set Tournament,
  hasTeam: set Team
}

sig Educator extends User {
  personalEvaluation: set MantotEvaluation,
  // An educator creates one or more tournaments
  createdTournaments: set Tournament,
  // An educator creates one or more battle
  createdBattle: set Battle
}



sig AutomatedEvaluation {
  functionalScore: one Score,
  timelinessScore: one Score,
  qualityScore: one Score,
  totalScore: one Score,
  // An automated evaluation is related to one project
  project: one Project
}

sig MantotEvaluation extends AutomatedEvaluation{
	personalScore: one Score,
       doneBy: one Educator
}

sig Project {
  //A project is associated to one battle
  associatedBattle: one Battle,
  // A project has a GitHub repository
  repository: one GithubRepository,
  // A project has one automated evaluation
  automatedEvaluation: one AutomatedEvaluation
}

sig GithubRepository {
  // A GitHub repository can be forked one project
  forkedProjects: one Project,
  // Each GithubRepository is associated to one team
  hasTeam: one Team
}

sig Tournament {
  isOpen: one Bool,
  //only educators that are authorized by the creator
  authorizedEducator: set Educator,
  registrationDeadline: one Date,
  // A tournament has one ranking
  tournamentRanking: one TournamentRanking,
  // A tournament is composed of zero or more battles
  battles: set Battle,
  // A tournament can have zero or more participating students
  participatingStudents: set Student,
  // A tournament is created by one educator
  creator: one Educator
}

abstract sig Ranking {
  // This will contain common fields or relations for all types of rankings
}

sig BattleRanking extends Ranking {
  // A battle ranking is generated from one battle
  generatedFrom: one Battle
}

sig TournamentRanking extends Ranking {
  // A tournament ranking is generated from one tournament
  generatedFrom: one Tournament
}

sig Battle {
  minStud: one Int,
  maxStud: one Int,
  registrationDeadline: one Date,
  submissionDeadline: one Date,
  
  // A battle contains one or more projects
  projects: set Project,
  // A battle generates one battle ranking
  battleRanking: one BattleRanking,
  // A battle has zero or more participating teams
  participatingTeams: set Team,
  // A battle is created by one educator
  creator: one Educator,
  // A battle is associated to one tournament
  associatedTournament: one Tournament

}{minStud <= maxStud && minStud >= 1}

sig Team {
  // A team has one or more students
  members: some Student,
  // A team participates in a battle
  participate: one Battle,
  //Each team can have a gitHub repository
  associatedRepository: lone GithubRepository
}
  fact {
// Two user can ’t have the same email address
  no disj u1 , u2: User | u1 . email = u2 . email
}

// A student can be enrolled in a team only if they are enrolled in at least one tournament.
fact {
  all s: Student | (some t: Team | s in t.members) => (some t: Tournament | s in t.participatingStudents)
}

// if an educator is a creator of the tournament, he is in the list of authorized educators.
fact {
  all t: Tournament | t.creator in t.authorizedEducator
}

//An Educator can create battles only if they have tournament permissions
fact {
  all e: Educator, b: Battle | e in b.creator => e in b.associatedTournament.authorizedEducator 
}




//For every team participating in a battle, each member of that team must also be a 
//registered participant in the tournament associated with the battle.
fact {
  all t: Team, b: Battle |
   t in b.participatingTeams =>
     all s: t.members | s in b.associatedTournament.participatingStudents
}




//Every team associated with a GitHub repository used in a project linked to a competition (battle) is listed 
//as a participant in that competition.
fact {
  all t: Team, r: GithubRepository, p: Project, b: Battle |
    r.hasTeam = t and p.repository = r and p.associatedBattle = b => t in b.participatingTeams
}



//if a battle is associated to a tournament, then that tournament contains that battle
fact {
 all b: Battle, t: Tournament | b in t.battles <=> t in b.associatedTournament
}

// If a Github repository is associated to a Team, then the Team has that Github repository
fact{
  all r: GithubRepository, t: Team| r in t.associatedRepository <=> t in r.hasTeam
}

// if a github repository is associated to a project, then the project is associated to that repository
fact{
  all r: GithubRepository, p: Project | r in p.repository <=> p in r.forkedProjects
}

//if  battle generates a battle ranking, then the battle ranking is associated to that battle
fact {
  all b: Battle , r: BattleRanking | b in r.generatedFrom <=> r in b.battleRanking
}

// if a tournament generates a tournament ranking, then the tournament ranking is associated to that tournament
fact {
  all t: Tournament , r: TournamentRanking | t in r.generatedFrom <=> r in t.tournamentRanking
}

// if an educator does a man tot Evaluation, then the man tot evaluation is done by Educator
fact{
  all m: MantotEvaluation, e: Educator | e in m.doneBy <=> m in e.personalEvaluation
}

// if a project has an automated evaluation, then the automated evaluation is associated to that project
fact{
  all a: AutomatedEvaluation, p: Project | p in a.project <=> a in p.automatedEvaluation
}

// if an educator has created the tournament, then the creator of the tournament is that educator
fact {
  all t: Tournament, e: Educator | e in t.creator <=> t in e.createdTournaments
}

// if an educator has created the battle, then the creator of the battle is that educator 
fact {
  all b: Battle, e: Educator | e in b.creator <=> b in e.createdBattle
}

// if the battle has an associated project, then the associatedBattle of the project is that battle 
fact {
  all b: Battle, p: Project | p in b.projects <=> b = p.associatedBattle
}

// if the battle has a team as a participant, then that battle is the one the team participates in
fact {
  all b: Battle, t: Team | t in b.participatingTeams <=> b in t.participate
}

// if the student partecipates in a tournament, then that student is in the students partecipating to the tournament

fact {
  all s: Student, t: Tournament | s in t.participatingStudents <=> t in s.subscribedTournaments
}

// if the student participates in a team, then that student is one of the team's members
fact {
  all s: Student, t: Team | s in t.members <=> t in s.hasTeam
}

// if a student is part of a team of a battle then he can't be part of other teams participating to the same battle
fact StudentInOnlyOneTeamPerBattle {
  all b: Battle | 
    no disj t1, t2: b.participatingTeams | 
    some t1.members & t2.members
}


// Ensure that every battle created by an educator is part of a tournament that the educator is authorized to manage
assert EducatorAuthorizedForTheirBattles {
  all e: Educator, b: e.createdBattle | 
    e in b.associatedTournament.authorizedEducator
}

//check EducatorAuthorizedForTheirBattles 


// Ensure that every project in a battle is associated with a team participating in that battle
assert ProjectTeamBattleConsistency {
  all b: Battle, p: b.projects | 
    some t: Team | p.repository.hasTeam = t and t in b.participatingTeams
}


//check ProjectTeamBattleConsistency for 6


// world 1 emphasizes the role of the student.
pred world1 {
  #Student =3
  #Educator = 1
  #AutomatedEvaluation = 2 
  #MantotEvaluation = 0
  #Tournament = 1
  #GithubRepository = 2 
  #TournamentRanking = 1
  #BattleRanking = 1
  #Team = 2
  #Battle = 1
  #Project = 2
}

//run world1  for 5


//world 2 emphasizes the role of the educator and the various methods of evaluation and ranking
pred world2 {
   #Student =2
  #Educator = 2
  #AutomatedEvaluation = 2
  #MantotEvaluation = 1
  #Tournament = 2
  #GithubRepository = 2 
  #TournamentRanking = 2
  #BattleRanking = 2
  #Team = 2
  #Battle = 2
  #Project = 2
}
//run world2 for 5
