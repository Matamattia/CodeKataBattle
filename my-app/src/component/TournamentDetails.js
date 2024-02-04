import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams, useNavigate, Link } from 'react-router-dom';
import '../style/DetailTour.css';
import HeaderStudent from './HeaderStudent';
import HeaderEducator from './HeaderEducator';
import RankingTable from './RankingTable';

const TournamentDetails = () => {
    const { id: tournamentId } = useParams();
    const [tournament, setTournament] = useState(null);
    const [userType, setUserType] = useState(null);
    const [isEducatorAuthorized, setIsEducatorAuthorized] = useState(false);
    const [isTournamentOpen, setIsTournamentOpen] = useState(false);
    const [isRegistrationOpen, setIsRegistrationOpen] = useState(false);

    const [battles, setBattles] = useState([]);
    const [rankings, setRankings] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [isRegistered, setIsRegistered] = useState(false);
    const [showRankings, setShowRankings] = useState(false);

    const navigate = useNavigate();
    

    // Funzioni di utilità da integrare
    const getUserTypeFromJwt = () => {
        const token = localStorage.getItem('jwtToken');
        if (token) {
            const base64Url = token.split('.')[1];
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const payload = JSON.parse(window.atob(base64));
            return payload.userType;
        }
        return null;
    };

    const getEmailFromJwt = () => {
        const token = localStorage.getItem('jwtToken');
        if (token) {
            const base64Url = token.split('.')[1];
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const payload = JSON.parse(window.atob(base64));
            return payload.sub;
        }
        return null;
    };

    const getIsRegisteredFromLocalStorage = () => {
        const storedValue = localStorage.getItem('isRegistered');
        return storedValue === 'true'; // Converte la stringa 'true' in un valore booleano true
    };

    const checkTournamentStatus = () => {
        axios.get(`/tournament/statusTournament?id=${tournamentId}`)
            .then(response => {
                setIsTournamentOpen(response.data);
            })
            .catch(error => {
                console.error('Error fetching tournament status:', error);
            });
    };

    useEffect(() => {
        const fetchTournamentData = async () => {
            try {
                const [tournamentResponse, battlesResponse, rankingsResponse] = await Promise.all([
                    axios.get(`/tournament/myTournament?id=${tournamentId}`),
                    axios.get(`/battle/battlesByTournament?tournamentId=${tournamentId}`),
                    axios.get(`/rankings/tournaments?tournamentId=${tournamentId}`)
                ]);
                setTournament(tournamentResponse.data);
                setBattles(battlesResponse.data);
                setRankings(rankingsResponse.data);
            } catch (error) {
                console.error('Errore nel caricamento dei dettagli del torneo:', error);
            }
            setIsLoading(false);
        };

        const userTypeFromJwt = getUserTypeFromJwt();
        setUserType(userTypeFromJwt);

        if (userTypeFromJwt === 'student') {
            const storedIsRegistered = getIsRegisteredFromLocalStorage();
            setIsRegistered(storedIsRegistered);
        }
        if (userType === 'educator') {
            checkEducatorAuthorization();
        }

        fetchTournamentData();
        checkTournamentStatus();
        const checkStudentRegistration = async () => {
            if (userType === 'student') {
                const email = getEmailFromJwt();
                if (!email) {
                    console.error("Email non trovata nel token");
                    return;
                }
        
                try {
                    const response = await axios.get(`/tournament/checkRegistration?tournamentId=${tournamentId}&studentEmail=${email}`);
        
                    if (response.status === 200) {
                        setIsRegistered(response.data);
                    } else {
                        console.error('Errore durante il controllo della registrazione al torneo:', response);
                    }
                } catch (error) {
                    console.error('Errore durante il controllo della registrazione al torneo:', error);
                }
            }
        };

        const checkRegistrationStatus = async () => {
            try {
                const response = await axios.get(`/tournament/registrationStatus?id=${tournamentId}`);
                setIsRegistrationOpen(response.data);
            } catch (error) {
                console.error('Error fetching registration status:', error);
            }
        };


        
        
    
        // Esegui il controllo dello stato del torneo solo se l'utente è uno studente
        if (userType === 'student') {
            checkStudentRegistration();
            checkRegistrationStatus();
        }
    }, [tournamentId, userType]);




    const createBattle = () => {
        navigate(`/createBattle/${tournamentId}`);
    };

    const deleteTournament = () => {
        if (window.confirm('Are you sure you want to delete this tournament?')) {
            const educatorEm = getEmailFromJwt();
            if (educatorEm) {
                axios.post(`/tournament/delete?id=${tournament.id}`, { educatorEmail :educatorEm })
                    .then(response => {
                        alert('Tournament deletion successful');
                        navigate('/tournamentsEducator');
                    })
                    .catch(error => {
                        console.error('Error deleting tournament:', error);
                    });
            } else {
                console.error('No educator email found in JWT');
            }
        }
    };


    const checkEducatorAuthorization = () => {
        const educatorEmail = getEmailFromJwt(); 
        if (educatorEmail && userType === 'educator') {
            axios.get(`/tournament/isAuthorized?educatorEmail=${educatorEmail}&tournamentId=${tournamentId}`)
                .then(response => {
                    setIsEducatorAuthorized(response.data);
                })
                .catch(error => {
                    console.error('Errore nel controllo dell\'autorizzazione:', error);
                });
        }
    };
    

    const closeTournament = () => {
        if (window.confirm('Are you sure you want to close this tournament?')) {
            const educatorEm = getEmailFromJwt();
            if (educatorEm) {
                axios.post(`/tournament/close?tournamentId=${tournament.id}`, { educatorEmail: educatorEm })
                    .then(response => {
                        console.log('Data from backend:', response.data);

                        if (response.data === true) {
                            // Gestisci qui la risposta positiva
                            alert('Tournament closed successfully');
                            window.location.reload();
                            // Potresti voler aggiornare lo stato del torneo o reindirizzare l'utente
                        } else {
                            // Gestisci qui la risposta negativa
                            alert('There are battle active');
                        }
                    })
                    .catch(error => {
                        console.error('Error closing tournament:', error);
                    });
            } else {
                console.error('No educator email found in JWT');
            }
        }
    };

    const handleJoinTournament = async () => {
        const email = getEmailFromJwt();
        if (!email) {
            console.error("Email non trovata nel token");
            alert('Error during tournament registration: Email not available');
            return;
        }
    
        try {
            // Utilizza i parametri di query per inviare tournamentId e email
            const response = await axios.post(`/tournament/join?tournamentId=${tournamentId}&studentEmail=${email}`);
    
            if (response.status === 200) {
                setIsRegistered(true);
                localStorage.setItem('isRegistered', 'true'); // Salva lo stato di registrazione in localStorage
                alert('You have successfully registered for the tournament!');
            } else {
                alert('Error during tournament registration.');
            }
        } catch (error) {
            console.error('Errore durante la registrazione al torneo:', error);
            alert('Errore durante la registrazione al torneo. ');
        }
    };

    const toggleRankingsVisibility = () => {
        setShowRankings(!showRankings);
    };

    if (isLoading) return <div>Loading...</div>;

    return (
        <div className="tournament-details">
            <div className="header-container">
            {userType === 'student' ? <HeaderStudent /> : <HeaderEducator />}
            </div>
            {tournament && (
                <>
                    <div className="tournament-header">
                        <h1 className="tournament-title">{tournament.name}</h1>
                        <p className="tournament-description">{tournament.description}</p>
                        {userType === 'student' ? (
                        <>
                            {isRegistered ? (
                                <p>You are registered for this tournament</p>
                            ) : (
                                <>
                                    { isTournamentOpen && isRegistrationOpen ? (
                                        <button className="join-button" onClick={handleJoinTournament}>
                                            Register for the Tournament
                                        </button>
                                    ) : (
                                        <p>Tournament registration is closed</p>
                                    )}
                                </>
                            )}
                            </>
                        ) : (
                            <>
                                {userType === 'educator' && isEducatorAuthorized && (
                            <>
                                <button className="create-battle-button" onClick={createBattle}>Create Battle</button>
                                <button className="delete-tournament-button" onClick={deleteTournament}>Delete Tournament</button>
                                {isTournamentOpen && <button className="close-tournament-button" onClick={closeTournament}>Close Tournament</button>}
                            </>
                        )}
                            </>
                        )}
                    </div>
                    <div className="tournament-content">
                        <button onClick={toggleRankingsVisibility} className="ranking-button">
                            {showRankings ? 'Hide Ranking' : 'Show Ranking'}
                        </button>
                        {showRankings && <RankingTable tournamentId={tournamentId} />}
                        <div className="battles-section">
                            <h2>Battles</h2>
                            {battles.length > 0 ? (
                                battles.map(battle => (
                                    <Link key={battle.battleId} to={`/tournament/${tournamentId}/battle/${battle.battleId}`}>
                                        <div className="battle-card">
                                            <h3>{battle.descriptionCodeKata}</h3>
                                            <p>Min students: {battle.minStudent}</p>
                                            <p>Max students: {battle.maxStudent}</p>
                                        </div>
                                    </Link>
                                ))
                            ) : (
                                <p>No battles found</p>
                            )}
                        </div>
                    </div>
                </>
            )}
        </div>
    );
};

export default TournamentDetails;
