import React, { useState, useEffect } from 'react';
import axios from 'axios';

const RankingTable = ({ tournamentId }) => {
    const [rankings, setRankings] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(`/rankings/tournaments?tournamentId=${tournamentId}`);
                setRankings(response.data);
            } catch (error) {
                console.error('Errore nel caricamento delle classifiche:', error);
            }
            setIsLoading(false);
        };

        fetchData();
    }, [tournamentId]);

    if (isLoading) return <div>Caricamento...</div>;

    return (
        <table className="ranking-table">
            <thead>
                <tr>
                    <th>Student Name</th>
                    <th>Student Surname</th>
                    <th>Student Score</th>
                </tr>
            </thead>
            <tbody>
                {rankings.map((ranking, index) => (
                    <tr key={index}>
                        <td>{ranking.student.name}</td>
                        <td>{ranking.student.surname}</td>
                        <td>{ranking.score}</td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
};

export default RankingTable;
