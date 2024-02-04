import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../style/CreationBattle.css';
import Header from './HeaderEducator';

const BattleForm = () => {
  const { tournamentId } = useParams();
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    battleId: 0,
    tournamentId: tournamentId,
    linkRepository: '',
    minStudent: 0,
    maxStudent: 0,
    descriptionCodeKata: '',
    codeKataTests: null,
    fileType: '',
    registrationDeadline: '',
    submissionDeadline: '',
    isEvaluatedManual: false,
  });
  
  const [errorMessage, setErrorMessage] = useState('');

  const handleChange = (event) => {
    const { name, value, type, checked } = event.target;
    setFormData({
      ...formData,
      [name]: type === 'checkbox' ? checked : value,
    });
  };
  
  const handleFileChange = (event) => {
    setFormData({
      ...formData,
      codeKataTests: event.target.files[0],
    });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    const formDataToSend = new FormData();

    // Crea un oggetto con tutti i dati tranne il file
    const data = {...formData};
    delete data.codeKataTests;

    // Aggiungi i dati non-file come stringa JSON
    formDataToSend.append('battleData', JSON.stringify(data));

    // Aggiungi il file
    if (formData.codeKataTests) {
      formDataToSend.append('codeKataTests', formData.codeKataTests);
    }

    try {
      await axios.post('/battle/create', formDataToSend, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      navigate(`/tournament/${tournamentId}`);
    } catch (error) {
      console.error('Errore durante la creazione della battaglia:', error);
      setErrorMessage('Errore nel processo di creazione');
    }
  };

  return (
    <div className="battle-form-container">
      <div className="header-container">
                <Header />
            </div>
      <form onSubmit={handleSubmit} className="battle-form">
      <h2>Create a new battle</h2>
        <input
          type="text"
          placeholder="Link Repository"
          name="linkRepository"
          value={formData.linkRepository}
          onChange={handleChange}
          hidden
        />
        <label>
          min number of students:
        <input
          type="number"
          placeholder="Numero minimo studenti"
          name="minStudent"
          value={formData.minStudent}
          onChange={handleChange}
          required
        />
        </label>
        <label>
          max number of students:
        <input
          type="number"
          placeholder="Numero massimo studenti"
          name="maxStudent"
          value={formData.maxStudent}
          onChange={handleChange}
          required
        />
        </label>
        <textarea
          placeholder="Description Code Kata"
          name="descriptionCodeKata"
          value={formData.descriptionCodeKata}
          onChange={handleChange}
          required
        ></textarea>
        <input
          type="file"
          name="codeKataTests"
          onChange={handleFileChange}
        />
        <input
          type="text"
          placeholder="Type of file"
          name="fileType"
          value={formData.fileType}
          onChange={handleChange}
          required
        />
        <input
          type="datetime-local"
          name="registrationDeadline"
          value={formData.registrationDeadline}
          onChange={handleChange}
          required
        />
        <input
          type="datetime-local"
          name="submissionDeadline"
          value={formData.submissionDeadline}
          onChange={handleChange}
          required
        />
        <label>
          Manual Evaluation:
          <input
            type="checkbox"
            name="isEvaluatedManual"
            checked={formData.isEvaluatedManual}
            onChange={handleChange}
          />
        </label>
        <button type="submit" className="submit-button">Create battle</button>
        {errorMessage && <p className="error-message">{errorMessage}</p>}
      </form>
    </div>
  );
};

export default BattleForm;
