
import React, { useState, useEffect } from 'react';
import '../style/Registration.css';
import Header from './Header';
import axios from 'axios';
import { Link, useNavigate } from 'react-router-dom';

const Registration = () => {
  const [formData, setFormData] = useState({
    name: '',
    surname: '',
    email: '',
    password: '',
    confirmPassword: '',
    userType: 'educator', // Imposta il valore predefinito su 'educator'
  });

  const [registrationError, setRegistrationError] = useState('');
  const [registrationSuccess, setRegistrationSuccess] = useState(false);
  const navigate = useNavigate();

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    // Validazione delle password
    if (formData.password.length < 6) {
      setRegistrationError('La password deve essere lunga almeno 6 caratteri.');
      return;
    }

    if (formData.password !== formData.confirmPassword) {
      setRegistrationError('Le password non corrispondono.');
      return;
    }

    // Se la validazione delle password ha successo, pulisci gli errori precedenti
    setRegistrationError('');

    try {
      // Determina quale endpoint API chiamare in base al tipo di utente selezionato
      const endpoint = formData.userType === 'educator' ? '/api/auth/signup/educator' : '/api/auth/signup/student';

      // Invia una richiesta POST al backend per la registrazione
      const response = await axios.post(endpoint, formData);

      // Esegui qui le azioni necessarie dopo la registrazione con successo
      console.log('Registrazione avvenuta con successo:', response.data);

      // Imposta lo stato per mostrare il messaggio di conferma
      setRegistrationSuccess(true);
    } catch (error) {
      // Gestisci eventuali errori durante la registrazione
      if (error.response && error.response.status === 409) {
        setRegistrationError('Email already in use. Please choose a different email.');
      } else {
        console.error('Error during registration:', error);
      }
    }
  };

  useEffect(() => {
    // Dopo un certo periodo di tempo (ad esempio, 3 secondi), reindirizza l'utente
    if (registrationSuccess) {
      const timeoutId = setTimeout(() => {
        navigate('/login'); 
      }, 3000); // 3000 millisecondi (3 secondi)

      return () => clearTimeout(timeoutId); // Pulisce il timeout in caso di unmount del componente
    }
  }, [registrationSuccess, navigate]);

  return (
    <div className="registration-container">
      <Header />
      <form onSubmit={handleSubmit} className="registration-form">
        <h2>Register for CKB</h2>
        <div className="user-type">
          
          <label>
            <div className='select-user-type'>Select User Type:</div>
            <select
              name="userType"
              value={formData.userType}
              onChange={handleChange}
            >
              <option value="educator">Educator</option>
              <option value="student">Student</option>
            </select>
          </label>
        </div>
        <input
          type="text"
          placeholder="Name"
          name="name"
          value={formData.name}
          onChange={handleChange}
          required
        />
        <input
          type="text"
          placeholder="Surname"
          name="surname"
          value={formData.surname}
          onChange={handleChange}
          required
        />
        <input
          type="email"
          placeholder="Email"
          name="email"
          value={formData.email}
          onChange={handleChange}
          required
        />
        <input
          type="password"
          placeholder="Password"
          name="password"
          value={formData.password}
          onChange={handleChange}
          required
        />
        <input
          type="password"
          placeholder="Confirm Password"
          name="confirmPassword"
          value={formData.confirmPassword}
          onChange={handleChange}
          required
        />
     
        <button type="submit" className="submit-button">
          Register
        </button>
        {registrationError && <p className="error-message">{registrationError}</p>}
        {registrationSuccess && <p className="success-message">Registration successful! You will be redirected to the login page shortly.</p>}
        <div className="login-text">
          {/* Rimuovi l'attributo href o fornisci un URL valido */}
          <Link to="/login"><span>Already have an account? Sign in</span></Link>
        </div>
      </form>
    </div>
  );
};

export default Registration;
