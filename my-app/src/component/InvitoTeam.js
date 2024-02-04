import React, { useEffect, useState } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import "../style/InvitoTeam.css";

const InvitoTeam = () => {
  const [teamId, setTeamId] = useState(null);
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const codiceInvito = searchParams.get('codice');
  const studentEmail = localStorage.getItem('studentEmail'); // Assumi che l'email dello studente sia salvata in localStorage


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
    const confermaCodiceInvito = async () => {
      try {
        const response = await axios.post(`/battle/conferma-invito?codice=${codiceInvito}&studentEmail=${studentEmail}`);
        if (response.data !== -1) {
          setTeamId(response.data);
        } else {
          
          navigate('/welcome');
        }
      } catch (error) {
        console.error('Errore nella conferma del codice di invito:', error);
        navigate('/welcome');
      }
    };
  
    confermaCodiceInvito();
  }, [codiceInvito, studentEmail, navigate]);
  

  const uniscitiAlTeam = async () => {
    const studentEmail = getEmailFromJwt();
    console.log(studentEmail);
    try {
      // Verifica che teamId non sia null o undefined
      if (teamId) {
        const response = await axios.post('/battle/join', {
          studentEmail: studentEmail,
          tournamentId: teamId
        }, {
          headers: {
            'Content-Type': 'application/json'
          }
        });
        if (response.status === 200) {
          navigate('/welcome'); // O gestisci la risposta come necessario
        }
      } else {
        console.error('Errore: teamId è null o undefined');
        // Qui puoi gestire l'errore, ad esempio mostrando un messaggio all'utente
      }
    } catch (error) {
      console.error('Errore nell’unirsi al team:', error.response ? error.response.data : error.message);
      // Qui puoi gestire l'errore, ad esempio mostrando un messaggio all'utente
    }
  };
  
  

  const rifiutaInvito = () => {
    navigate('/welcome');
  };

  // Render del componente
  return (
    <div className="invito-team-container">
          <div className="invito-team-header">
          <h2>Conferma l'adesione al team</h2>
        </div>
        <div className="invito-team-content">
        <p>Sei stato invitato a unirti al team {teamId}. Vuoi unirti al team?</p>
        </div>
      <div className="invito-team-buttons">
        <button onClick={uniscitiAlTeam} disabled={!teamId}>Unisciti al team</button>
        <button onClick={rifiutaInvito}>Non unirti al team</button>
      </div>
    </div>
  );
};

export default InvitoTeam;
