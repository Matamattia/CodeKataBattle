sig Tournament {
  //The tournament can be closed by the educator
  var isOpen: one Bool,
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



fact StatusTournament { all t: Tournament|
always ( t.isOpen = False implies
after always  t.isOpen= False )
}

// Predicato per chiudere un torneo
pred closeTournament[t: Tournament] {
  t.isOpen = True
  implies
  t.isOpen' = False
}


assert CheckStatusTournament { all t: Tournament |
	always closeTournament[t]
	 implies	
	 always after t.isOpen = False
}
check CheckStatusTournament for 3 steps
