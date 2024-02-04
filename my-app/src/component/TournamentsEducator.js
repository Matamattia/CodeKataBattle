import React, { useState, useEffect } from 'react';
import axios from 'axios'; 
import '../style/TournamentsEducator.css';
import HeaderEducator from './HeaderEducator';
import { useNavigate } from 'react-router-dom';

function TournamentsEducator() {
    const [tournaments, setTournaments] = useState([]);
    const navigate = useNavigate();
    const formatDate = (dateString) => {
        const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' };
        return new Date(dateString).toLocaleDateString(undefined, options);
    };

    function getEmailFromJwt() {
        const token = localStorage.getItem('jwtToken');
        if (token) {
            const base64Url = token.split('.')[1];
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const payload = JSON.parse(window.atob(base64));
            return payload.sub;
        }
        return null;
    }


    useEffect(() => {
        const email = getEmailFromJwt();
        axios.get(`/tournament/educator?educatorEmail=${email}`) 
            .then(response => {
                setTournaments(response.data); // Assicurati di usare response.data con Axios
            })
            .catch(error => {
                console.error('Error fetching tournaments:', error);
            });
    }, []);

    const handleTournamentClick = (id) => {
        navigate(`/tournament/${id}`);
    };
    const handleCreateTournament = () => {
        navigate('/createTournament'); // in create tournament, prima creo il torneo e poi salvo tutti gli authorized educators
    };

    return (
        <div className="tournaments">
            <div className="header-container">
                <HeaderEducator />
            </div>
            <div className="tournaments-container">
                <h1>My Tournament</h1>
                <div className="tournament-list">
                    <button onClick={handleCreateTournament} className="create-tournament-button">
                        Create Tournament
                    </button>
                    {tournaments.map(tournament => (
                        <div key={tournament.id} className="tournament-card" onClick={() => handleTournamentClick(tournament.id)}>
                            <div className="tournament-info">
                                <h2>{tournament.name}</h2>
                                <p>{tournament.description}</p>
                                <p className="tournament-deadline">Registration Deadline: {formatDate(tournament.registrationDeadline)}</p>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}
export default TournamentsEducator;