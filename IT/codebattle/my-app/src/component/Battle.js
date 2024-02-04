import React, { useState, useEffect } from 'react';
import axios from 'axios';
import "../style/Battle.css";
import { useParams, useNavigate } from 'react-router-dom';
import HeaderStudent from './HeaderStudent';
import HeaderEducator from './HeaderEducator';

const BattleDetails = () => {
    const [teamName, setTeamName] = useState('');
    const [userType, setUserType] = useState(null);
    const [isEducatorAuthorized, setIsEducatorAuthorized] = useState(false);
    const [isStudentInTeam, setIsStudentInTeam] = useState(false); 
    const [isManualEvaluated, setIsManualEvaluated] = useState(false);
    const [isTerminated, setIsTerminated] = useState(false);
    const [isRegistrationOpen, setIsRegistrationOpen] = useState(false);
    const [selectedStudent, setSelectedStudent] = useState('');
    const [teamMembers, setTeamMembers] = useState([]);
    const [allStudents, setAllStudents] = useState([]);
    const [battleDetails, setBattleDetails] = useState(null);
    const [isCreatingTeam, setIsCreatingTeam] = useState(false);
    const [teamCreated, setTeamCreated] = useState(false);
    const { tournamentId, battleId } = useParams();
    const navigate = useNavigate();
    const [invitationCode, setInvitationCode] = useState('');
    const [invitationSent, setInvitationSent] = useState(false);
    const [battleRanking, setBattleRanking] = useState([]);

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
    const fetchIsRegistrationOpen = async () => {
        try {
            const response = await axios.get(`/battle/isRegistrationOpen?battleId=${battleId}&tournamentId=${tournamentId}`);
            setIsRegistrationOpen(response.data);
            console.log("è aperto" + response.data);
        } catch (error) {
            console.error('Errore nel controllo dello stato di apertura della registrazione:', error);
        }
    };
    const fetchIsTerminated = async () => {
        try {
            const response = await axios.get(`/battle/isTerminated?battleId=${battleId}&tournamentId=${tournamentId}`);
            setIsTerminated(response.data); 
            console.log("è terminato" + response.data);
        } catch (error) {
            console.error('Errore nel recupero dello stato di terminazione:', error);
        }
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

    const fetchBattleRanking = async () => {
        try {
            const response = await axios.get(`/rankings/battles?battleId=${battleId}&tournamentId=${tournamentId}`);
            setBattleRanking(response.data);
        } catch (error) {
            console.error('Errore nel recupero della classifica della battaglia:', error);
        }
    };

    const handleTeamRegistration = () => {
        setIsCreatingTeam(true);
    };
    const checkIfStudentInTeam = async () => {
        const studentEmail = getEmailFromJwt(); 
        if (!studentEmail) return;

        try {
            const response = await axios.get(`/battle/isStudentInTeamForBattle`, {
                params: { studentEmail, battleId, tournamentId }
            });
            setIsStudentInTeam(response.data);
        } catch (error) {
            console.error('Errore nel verificare se lo studente è in un team:', error);
        }
    };
    

    useEffect(() => {
        const fetchAllStudents = async () => {
            try {
                const response = await axios.get('/api/students/all');
                const currentStudentEmail = getEmailFromJwt();
                // Filtra la lista degli studenti per escludere lo studente corrente
                const filteredStudents = response.data.filter(student => student.email !== currentStudentEmail);
                setAllStudents(filteredStudents);
            } catch (error) {
                console.error('Errore nel recupero degli studenti:', error);
            }
        };
        const userTypeFromJwt = getUserTypeFromJwt();
        setUserType(userTypeFromJwt);
        const fetchBattleDetails = async () => {
            try {
                const response = await axios.get(`/battle/infoBattle?battleId=${battleId}&tournamentId=${tournamentId}`);
                setBattleDetails(response.data);
                setIsManualEvaluated(response.data.isEvaluatedManual);
                console.log(isManualEvaluated)
            } catch (error) {
                console.error('Errore nel recupero dei dettagli della battaglia:', error);
            }
        };
        if (userType === 'student') {
            checkIfStudentInTeam(); 
        }
        if (userType === 'educator') {
            checkEducatorAuthorization();
        }
        fetchAllStudents();
        fetchBattleDetails();
        fetchBattleRanking();
        fetchIsTerminated();
        fetchIsRegistrationOpen();
    }, [battleId, tournamentId,userType]);


    const checkEducatorAuthorization = async () => {
        const educatorEmail = getEmailFromJwt(); 
        if (educatorEmail && userType === 'educator') {
            try {
                const response = await axios.get(`/tournament/isAuthorized?educatorEmail=${educatorEmail}&tournamentId=${tournamentId}`);
                setIsEducatorAuthorized(response.data);
            } catch (error) {
                console.error('Errore nel controllo dell\'autorizzazione:', error);
            }
        }
    };
    
    const handleTeamNameChange = (e) => {
        setTeamName(e.target.value);
    };
    const handleDeleteBattle = async () => {
        if(window.confirm('Are you sure to delete the battle?')) {
            try {
                // Aggiornato per usare POST e per inviare i dati nel body
                await axios.post(`/battle/delete`, {
                    battleId: battleId,
                    tournamentId: tournamentId
                });
                navigate('/tournaments'); // Reindirizza l'utente dopo l'eliminazione
            } catch (error) {
                console.error('Error', error);
            }
        }
    };
    
    
    const handleFinalSubmission = () => {
        navigate(`/finalSubmission/${tournamentId}/${battleId}`); 
    };

    const handleAddStudent = () => {
        if (selectedStudent && !teamMembers.includes(selectedStudent)) {
            setTeamMembers([...teamMembers, selectedStudent]);
        }
        setSelectedStudent('');
    };

    const handleRemoveStudent = (studentToRemove) => {
        const updatedTeamMembers = teamMembers.filter(student => student !== studentToRemove);
        setTeamMembers(updatedTeamMembers);
    };
    const handleCloseTeamForm = () => {
        setIsCreatingTeam(false);
    };
    

    const handleCreateTeam = async () => {
        const creatorEmail = getEmailFromJwt();
        if (!creatorEmail) {
            console.error('Errore: Non è possibile identificare l utente');
            return;
        }

        try {
            const response = await axios.post('/battle/createTeam', {
                name: teamName,
                studentEmails: teamMembers,
                creatorEmail: creatorEmail,
                battleId: battleDetails.battleId,
                tournamentId: tournamentId,
                invitationCode: invitationCode 
            });
            if (response.status === 200) {
                // Imposta lo stato per indicare che il team è stato creato con successo
                setTeamCreated(true);
                // Imposta lo stato per indicare che l'invito è stato inviato con successo
                setInvitationSent(true);
            }
            setTimeout(() => {
                navigate('/tournaments');
            }, 1000);
        } catch (error) {
            console.error('Errore nella creazione del team:', error);
        }

        setTeamName('');
        setSelectedStudent('');
        setTeamMembers([]);
    };

    return (
        <div className="battle-details">
            <div className="header-container">
            {userType === 'student' ? <HeaderStudent /> : <HeaderEducator />}
            </div>
            {battleDetails && (
                <div className="battle-info">
                    <h1>{battleDetails.descriptionCodeKata}</h1>
                    <p>{battleDetails.description}</p>
                    <p>Minimum number of students: {battleDetails.minStudent}</p>
                    <p>Maximum number of students {battleDetails.maxStudent}</p>
                </div>
            )}

            {userType === 'student' && !isStudentInTeam && isRegistrationOpen &&(
                <div className="team-registration-section">
                    <h2>Register your Team</h2>
                    <button className="register-team-button" onClick={() => setIsCreatingTeam(true)}>Register Team</button>
                </div>
            )}
            {userType === 'educator' && (isRegistrationOpen || isTerminated) && isEducatorAuthorized && (
                <div className="delete-battle-section">
                    <button onClick={handleDeleteBattle} className="delete-battle-button">Delete Battle</button>
                </div>
            )}

            {userType === 'educator' && isTerminated && isManualEvaluated && isEducatorAuthorized && (
                <div className="final-submission-section">
                    <button onClick={handleFinalSubmission}  className="final-submission-button">Final Submission</button>
                </div>
            )}
            {isCreatingTeam && (
                <div className="team-creation-form">
                    <button className="close-button" onClick={handleCloseTeamForm}>Chiudi</button>
                    <h3>Create your Team </h3>
                    <label>
                        Team name:
                        <input
                            type="text"
                            value={teamName}
                            onChange={handleTeamNameChange}
                        />
                    </label>

                    <h4>Select a student to invite:</h4>
                    <select
                        value={selectedStudent}
                        onChange={(e) => setSelectedStudent(e.target.value)}
                    >
                        <option value="">Select a student</option>
                        {allStudents.map(student => (
                            <option key={student.email} value={student.email}>
                                {student.name}
                            </option>
                        ))}
                    </select>
                    <button className="add-student-button" onClick={handleAddStudent}>Add Student</button>

                    <h4>Students in the team:</h4>
                    <ul>
                        {teamMembers.map(student => (
                            <li key={student}>
                                {student}
                                <button className="remove-student-button" onClick={() => handleRemoveStudent(student)}>Remove</button>
                            </li>
                        ))}
                    </ul>

                    <button className="create-team-button" onClick={handleCreateTeam}>Create Team</button>
                </div>
            )}

            {teamCreated && (
                <div className="team-created-message">
                   You created the team "{teamName}", we will send an email to all guests to join the team!
                </div>
            )}

            <div className="battle-ranking">
                <h2>Battle ranking</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Position</th>
                            <th>Team</th>
                            <th>Score</th>
                        </tr>
                    </thead>
                    <tbody>
                        {battleRanking.map((item, index) => (
                            <tr key={item.team.teamId}>
                                <td>{index + 1}</td>
                                <td>{item.team.name}</td>
                                <td>{item.score}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default BattleDetails;
