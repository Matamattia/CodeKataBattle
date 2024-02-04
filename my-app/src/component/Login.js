import React, { useState } from 'react';
import '../style/Login.css';
import Header from './Header';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';

const Login = () => {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    userType: 'educator', // Imposta un valore predefinito (educator o student)
  });

  const [errorMessage, setErrorMessage] = useState(''); // Stato per il messaggio di errore
  const navigate = useNavigate();



  const handleSubmit = async (event) => {
    event.preventDefault();
  
    try {
      const apiPath = formData.userType === 'educator' ? '/api/auth/login/educator' : '/api/auth/login/student';
      const response = await axios.post(apiPath, formData);
      
      if (response.status === 200) {
        localStorage.setItem('jwtToken', response.data.token);
  
        // Estrai i parametri dall'URL corrente
        const urlParams = new URLSearchParams(window.location.search);
        const postLoginRedirect = urlParams.get('redirect');
        const invitoCodice = urlParams.get('codice');
  
        // Reindirizza in base alla presenza di parametri di reindirizzamento e codice d'invito
        if (postLoginRedirect && invitoCodice) {
          navigate(`${decodeURIComponent(postLoginRedirect)}?codice=${invitoCodice}`);
        } else {
          const redirectPath = formData.userType === 'educator' ? '/welcomeEducator' : '/welcome';
          navigate(redirectPath);
        }
      }
    } catch (error) {
      console.error('Errore durante il login:', error);
      setErrorMessage('Incorrect username or password');
    }
  };
  
  
  
  


  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };





 



  return (
    <div className="login-container">
      <div className="header-container">
            <Header/>
            </div>
      <form onSubmit={handleSubmit} className="login-form">
        <h2>Welcome to CKB</h2>
        <div className="user-type">
          <label>Select User Type:</label>
          <select name="userType" value={formData.userType} onChange={handleChange}>
            <option value="educator">Educator</option>
            <option value="student">Student</option>
          </select>
        </div>
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
        <div className="forgot-text">
          <a href="#">Forgot your password?</a>
        </div>
        <button type="submit" className="submit-button">
          Sign in
        </button>
        {errorMessage && <p className="error-message">{errorMessage}</p>} {/* Mostra il messaggio di errore se presente */}
        <div className="login-text">
          <Link to="/registration">Sign up if you haven't an account yet</Link>
        </div>
      </form>
    </div>
  );
};

export default Login;
