import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../style/WelcomePageEducator.css'; 
import HeaderEducator from './HeaderEducator';

function WelcomeEducator() {
    const [educatorInfo, setEducatorInfo] = useState({ educator: null, tournaments: [], battles: [] });
    const [isLoading, setLoading] = useState(true);
    const [error, setError] = useState(null);

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
        if (email) {
            axios.get(`/api/educators/${email}`)
                .then(response => {
                    setEducatorInfo(response.data);
                    setLoading(false);
                })
                .catch(err => {
                    console.error('Error fetching educator info:', err);
                    setError(err);
                    setLoading(false);
                });
        } else {
            setError(new Error("Email not found in token"));
            setLoading(false);
        }
    }, []);

    if (isLoading) {
        return <p>Loading...</p>;
    }

    if (error) {
        return <p>Error loading educator data.</p>;
    }

    return (
        <div className="welcomePageEducator">
            <HeaderEducator /> 
            <div className="mainContentEducator">
                {educatorInfo.educator ? (
                    <>
                        <h2>Welcome, {educatorInfo.educator.name} {educatorInfo.educator.surname}!</h2>
                        <p>Manage your tournaments and battles.</p>
                        
                        <div className="dashboardSection2">
                            <div className="dashboardCard2">
                                <h3>Your Tournaments</h3>
                                <ul>
                                    {educatorInfo.tournaments.map(tournament => (
                                        <li key={tournament.id}>{tournament.name}</li>
                                    ))}
                                </ul>
                            </div>
                            <div className="dashboardCard2">
                                <h3>Your Battles</h3>
                                <ul>
                                    {educatorInfo.battles.map(battle => (
                                        <li key={battle.battleId}>{battle.descriptionCodeKata}</li>
                                    ))}
                                </ul>
                            </div>
                        </div>
                    </>
                ) : (
                    <p>Educator data not found.</p>
                )}
            </div>
        </div>
    );
}
export default WelcomeEducator;



