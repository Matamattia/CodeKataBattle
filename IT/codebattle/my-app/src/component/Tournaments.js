import React, { useState, useEffect } from 'react';
import axios from 'axios'; // Importa Axios
import '../style/Tournaments.css';
import HeaderStudent from './HeaderStudent';
import HeaderEducator from './HeaderEducator';
import { useNavigate } from 'react-router-dom';

function Tournaments() {
    const [tournaments, setTournaments] = useState([]);
    const navigate = useNavigate();
    const [userType, setUserType] = useState(null);
    const formatDate = (dateString) => {
        const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' };
        return new Date(dateString).toLocaleDateString(undefined, options);
    };
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


    useEffect(() => {
        const type = getUserTypeFromJwt();
        setUserType(type);
        axios.get('/tournament/all') // Usa Axios per la richiesta GET
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

    return (
        <div className="tournaments">
            <div className="header-container">
                {userType === 'student' ? <HeaderStudent /> : <HeaderEducator />}
            </div>
            <div className="tournaments-container">
                <h1>All Tournaments</h1>
                <div className="tournament-list">
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

export default Tournaments;
