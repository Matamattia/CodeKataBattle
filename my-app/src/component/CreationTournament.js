// import React, { useState } from 'react';
// import axios from 'axios';
// import '../style/CreationTournament.css'; 
// import HeaderEducator from './HeaderEducator';
// import { useNavigate } from 'react-router-dom';

// const CreateTournamentForm = () => {
//   const navigate = useNavigate();

//   const [formData, setFormData] = useState({
//     name: '',
//     registrationDeadline: '',
//     description: '',
//     isOpen: true,
//   });

//   const [educatorEmailsInput, setEducatorEmailsInput] = useState(''); // Casella di testo per le email degli educator
//   const [educatorEmails, setEducatorEmails] = useState([]); // Lista delle email degli educator
//   const [educatorCreator, setEducatorCreator] = useState(''); // Mail dell'educator che compila

//   const [errorMessage, setErrorMessage] = useState('');

//   const handleChange = (event) => {
//     const { name, value, type, checked } = event.target;
//     setFormData({
//       ...formData,
//       [name]: type === 'checkbox' ? checked : value,
//     });
//   };

//   // Funzione per ottenere la mail dell'educator dal token JWT
//   function getEmailFromJwt() {
//     const token = localStorage.getItem('jwtToken');
//     if (token) {
//       const base64Url = token.split('.')[1];
//       const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
//       const payload = JSON.parse(window.atob(base64));
//       return payload.sub;
//     }
//     return null;
//   }

//   const handleEmailChange = (event) => {
//     const { value } = event.target;
//     // Imposta il valore della casella di testo per le email
//     setEducatorEmailsInput(value);
//     // Divide l'input in una lista di email separate da virgole
//     const emailsList = value.split(',');
//     // Rimuovi spazi in eccesso e aggiungi le email alla lista delle email degli educator
//     const cleanedEmails = emailsList.map((email) => email.trim());
//     setEducatorEmails(cleanedEmails);
//   };

//   const handleSubmit = async (event) => {
//     event.preventDefault();

//     try {
//       // Imposta la mail dell'educator creatore
//       const creatorEmail = getEmailFromJwt();
//       setEducatorCreator(creatorEmail);

//       // Invia sia i dati del torneo che la lista delle email degli educator e la mail dell'educator creatore
//       const response = await axios.post('/tournament/create', {
//         tournament: formData,
//         educatorEmails: educatorEmails,
//         educatorCreator: creatorEmail,
//       });
//       console.log(response.data); // Gestisci la risposta come preferisci
//       navigate('/tournamentsEducator'); 
//     } catch (error) {
//       console.error('Errore durante la creazione del torneo:', error);
//       setErrorMessage('Errore nel processo di creazione');
//     }
//   };

//   return (
//     <div className="tournament-form-container">
//       <div className="header-container">
//                 <HeaderEducator />
//             </div>
//       <form onSubmit={handleSubmit} className="tournament-form">
//         <h2>Create a new Tournament</h2>
//         <input
//           type="text"
//           placeholder="Name of Tournament"
//           name="name"
//           value={formData.name}
//           onChange={handleChange}
//           required
//         />
//         <input
//           type="datetime-local"
//           name="registrationDeadline"
//           value={formData.registrationDeadline}
//           onChange={handleChange}
//           required
//         />
//         <textarea
//           placeholder="Description of Tournament"
//           name="description"
//           value={formData.description}
//           onChange={handleChange}
//           required
//         ></textarea>
//         <label>
//           isOpen:
//           <input
//             type="checkbox"
//             name="isOpen"
//             checked={formData.isOpen}
//             onChange={handleChange}
//           />
//         </label>
//         {/* Aggiungi campo per inserire email degli educator */}
//         <input
//           type="text"
//           placeholder="Emails of Educators (comma-separated)"
//           value={educatorEmailsInput}
//           onChange={handleEmailChange}
//         />
//         {/* Mostra la lista delle email degli educator */}
//         <div className="educator-email-list">
//           {educatorEmails.map((email, index) => (
//             <div key={index}>{email}</div>
//           ))}
//         </div>
//         <button type="submit" className="submit-button">Crea Torneo</button>
//         {errorMessage && <p className="error-message">{errorMessage}</p>}
//       </form>
//     </div>
//   );
// };

// export default CreateTournamentForm;


import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../style/CreationTournament.css';
import HeaderEducator from './HeaderEducator';
import { useNavigate } from 'react-router-dom';

const CreateTournamentForm = () => {
  const navigate = useNavigate();
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

  const [formData, setFormData] = useState({
    name: '',
    registrationDeadline: '',
    description: '',
    isOpen: true,
  });

  const [allEducators, setAllEducators] = useState([]);
  const [selectedEducator, setSelectedEducator] = useState('');
  const [educatorEmails, setEducatorEmails] = useState([]);
  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    const fetchAllEducators = async () => {
      try {
        const response = await axios.get('/api/educators/all');
        const currentEducatorEmail = getEmailFromJwt(); // Ottieni l'email dell'educatore corrente
        const filteredEducators = response.data.filter(educator => educator.email !== currentEducatorEmail); // Filtra l'educatore corrente dall'elenco
        setAllEducators(filteredEducators);
      } catch (error) {
        console.error('Errore nel recupero degli educator:', error);
        setErrorMessage('Impossibile caricare gli educator');
      }
    };
  
    fetchAllEducators();
  }, []);
  

  const handleChange = (event) => {
    const { name, value, type, checked } = event.target;
    setFormData({
      ...formData,
      [name]: type === 'checkbox' ? checked : value,
    });
  };

  const handleAddEducator = () => {
    if (selectedEducator && !educatorEmails.includes(selectedEducator)) {
      setEducatorEmails([...educatorEmails, selectedEducator]);
    }
    setSelectedEducator('');
  };

  const handleRemoveEducator = (emailToRemove) => {
    setEducatorEmails(educatorEmails.filter((email) => email !== emailToRemove));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    

    try {
      const response = await axios.post('/tournament/create', {
        tournament: formData,
         educatorEmails: educatorEmails,
         educatorCreator: getEmailFromJwt(), 
      });

      console.log(response.data);
      navigate('/tournamentsEducator');
    } catch (error) {
      console.error('Errore durante la creazione del torneo:', error);
      setErrorMessage('Errore nel processo di creazione');
    }
  };

  return (
    <div className="tournament-form-container">
      <HeaderEducator />
      <form onSubmit={handleSubmit} className="tournament-form">
        <h2>Create a new Tournament</h2>
        <div>
      <label htmlFor="registrationDeadline">Tournament name:</label>
        <input
          type="text"
          placeholder="Name of Tournament"
          name="name"
          value={formData.name}
          onChange={handleChange}
          required
        />
        </div>
          <div>
      <label htmlFor="registrationDeadline">Registration Deadline:</label>
      <input
        type="datetime-local"
        name="registrationDeadline"
        id="registrationDeadline"
        value={formData.registrationDeadline}
        onChange={handleChange}
        required
      />
    </div>
    <div>
    <label htmlFor="registrationDeadline">Tournament description:</label>
        <textarea
          placeholder="Description of Tournament"
          name="description"
          value={formData.description}
          onChange={handleChange}
          required
        />
        </div>
        <label>
          <input
            type="checkbox"
            name="isOpen"
            hidden
            default = "true"
            checked={formData.isOpen}
            onChange={handleChange}
          />
        </label>
        <div>
        <label htmlFor="registrationDeadline">Authorized educators:</label>
          <select
            value={selectedEducator}
            onChange={(e) => setSelectedEducator(e.target.value)}
          >
            <option value="">Select an Educator</option>
            {allEducators.map((educator) => (
              <option key={educator.email} value={educator.email}>
                {educator.name}
              </option>
            ))}
          </select>
          <button type="button" className="add-educator-button" onClick={handleAddEducator}>Add Educator</button>
        </div>
        <div>
          {educatorEmails.map((email, index) => (
            <div key={index}>
              {email}
              <button type="button"  className="add-educator-button"onClick={() => handleRemoveEducator(email)}>Remove</button>
            </div>
          ))}
        </div>
        <button type="submit" className="submit-button">Create Tournament</button>
        {errorMessage && <p className="error-message">{errorMessage}</p>}
      </form>
    </div>
  );
};

export default CreateTournamentForm;