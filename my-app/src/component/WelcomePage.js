import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../style/WelcomePage.css';
import HeaderStudent from './HeaderStudent';

function WelcomePage() {
    const [studentInfo, setStudentInfo] = useState({ student: null, tournaments: [], battles: [] });
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
            axios.get(`/api/students/${email}`)
                .then(response => {
                    setStudentInfo(response.data);
                    setLoading(false);
                })
                .catch(err => {
                    console.error('Error fetching student info:', err);
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
        return <p>Error loading student data.</p>;
    }

    return (
        <div className="welcomePage">
            <HeaderStudent />
            <div className="mainContent">
                {studentInfo.student ? (
                    <>
                        <h2>Welcome, {studentInfo.student.name} {studentInfo.student.surname}!</h2>
                        <p>Engage in exciting coding battles and improve your skills.</p>
                        
                        <div className="dashboardSection">
                            <div className="dashboardCard">
                                <h3>Your Tournaments</h3>
                                <ul>
                                    {studentInfo.tournaments.map(tournament => (
                                        <li key={tournament.id}>{tournament.name}</li>
                                    ))}
                                </ul>
                            </div>
                            <div className="dashboardCard">
                                <h3>Your Battles</h3>
                                <ul>
                                    {studentInfo.battles.map(battle => (
                                        <li key={battle.battleId}>{battle.descriptionCodeKata}</li>
                                    ))}
                                </ul>
                            </div>
                        </div>
                    </>
                ) : (
                    <p>Student data not found.</p>
                )}
            </div>
        </div>
    );
}

export default WelcomePage;
