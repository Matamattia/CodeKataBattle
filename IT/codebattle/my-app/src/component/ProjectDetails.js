import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import '../style/Det.css';
import FileDownload from 'js-file-download';
import HeaderEducator from './HeaderEducator';
import { useNavigate } from 'react-router-dom';


const ProjectDetails = () => {

    const [project, setProject] = useState(null);
    const navigate = useNavigate();
    const [personalScore, setPersonalScore] = useState('');
    const { projectId } = useParams();
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
        const fetchProject = async () => {
            try {
                const response = await axios.get(`/battle/project?projectId=${projectId}`);
                setProject(response.data);
            } catch (error) {
                console.error('Errore nel recupero dei dettagli del progetto:', error);
            }
        };

        fetchProject();
    }, [projectId]);

    const downloadCodeKata = () => {
        axios.get(`/battle/downloadCodeKata?projectId=${projectId}`, {
            responseType: 'blob',
        }).then((res) => {
            FileDownload(res.data, "codeKataTeam.txt"); 
        }).catch((error) => {
            console.error('Errore nel download del file:', error);
        });
    };

    const sendEvaluation = () => {
        
        const educatorEmail = getEmailFromJwt();
        axios.post('/evaluation/evaluateProject', {
            projectId: project.projectId,
            personalScore: parseFloat(personalScore),
            educatorEmail: educatorEmail
        }).then((response) => {
            alert("Evaluation sent successfully!");
            navigate(-1);
        }).catch((error) => {
            console.error('Errore nell\'invio della valutazione:', error);
            alert("Failed to send evaluation.");
        });
    };

    return (
        <div className="project-details-container">
            <div className="header-container">
                <HeaderEducator />
            </div>
            <h2>Project Details</h2>
            {project ? (
                <div>
                    <p className="project-detail"><strong>Project ID:</strong> {project.projectId}</p>
                    <p className="project-detail"><strong>Team ID:</strong> {project.team && project.team.teamId}</p>
                    <p className="project-detail"><strong>Team name:</strong> <a href={project.githubRepository} target="_blank" rel="noopener noreferrer">{project.githubRepository}</a></p>
                    <button className="download-button" onClick={downloadCodeKata}>Download Code Kata</button>
                    <div className="evaluation-section">
                        <input
                            type="number"
                            step="0.01"
                            value={personalScore}
                            onChange={(e) => setPersonalScore(e.target.value)}
                            placeholder="Enter evaluation score"
                            className="evaluation-input"
                        />
                        <button className="evaluation-send-button" onClick={sendEvaluation}>Send Evaluation</button>
                    </div>
                </div>
            ) : (
                <p>Loading project details...</p>
            )}
        </div>
    );
};

export default ProjectDetails;

