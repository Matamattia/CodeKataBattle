import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams, Link } from 'react-router-dom';
import '../style/FinalSubmission.css';
import HeaderEducator from './HeaderEducator';


const FinalSubmission = () => {
    const [projects, setProjects] = useState([]);
    const { tournamentId, battleId } = useParams();
    

    useEffect(() => {
        const fetchProjects = async () => {
            try {
                const response = await axios.get(`/battle/projects?battleId=${battleId}&tournamentId=${tournamentId}`);
                setProjects(response.data);
            } catch (error) {
                console.error('Errore nel recupero dei progetti:', error);
            }
        };

        fetchProjects();
    }, [battleId, tournamentId]); // Riesegue l'effetto se battleId o tournamentId cambiano

    return (
        
        <div className="final-submission">
            <div className="header-container">
                <HeaderEducator />
            </div>
            <h2>Final Submission Projects</h2>
            <div className="projects-list">
                {projects.length > 0 ? (
                    projects.map((project) => (
                        <div key={project.projectId} className="project-item">
                            <h3>Id project : {project.projectId}</h3>
                            <h3>Team Id : {project.team.teamId}</h3>
                           
                            <Link to={`/projectDetails/${project.projectId}`}>View Details</Link>
                        </div>
                    ))
                ) : (
                    <p>No projects found for this battle.</p>
                )}
            </div>
        </div>
    );
};

export default FinalSubmission;
