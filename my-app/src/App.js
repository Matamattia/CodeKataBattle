import React from 'react';
import './App.css';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Homepage from './component/Homepage';
import Login from './component/Login';
import Registration from './component/Registration';
import WelcomePage from './component/WelcomePage';
import WelcomeEducator from './component/WelcomeEducator';
import Tournaments from './component/Tournaments';
import TournamentsEducator from './component/TournamentsEducator';
import CreationTournament from './component/CreationTournament';
import CreationBattle from './component/CreationBattle';
import TournamentDetails from './component/TournamentDetails';
import FinalSubmission from './component/FinalSubmission';
import Battle from './component/Battle';
import InvitoTeam from './component/InvitoTeam';
import ProjectDetails from './component/ProjectDetails';
import axios from 'axios';
import ProtectedRoute  from './ProtectedRoute';


function App() {


  axios.interceptors.request.use((config) => {
    const token = localStorage.getItem('jwtToken');
    config.headers.Authorization = token ? `Bearer ${token}` : '';
    return config;
  });

  axios.interceptors.response.use(
    response => response,
    error => {
      if (error.response && error.response.status === 401) {
        localStorage.removeItem('jwtToken');
        // Reindirizza all'accesso o gestisci l'errore
      }
      return Promise.reject(error);
    }
  );

  return (
    <div className="App">
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Homepage />} />
                    <Route path="login" element={<Login />} />
                    <Route path="registration" element={<Registration />} />
                    <Route path="/welcomeEducator" element={
                        <ProtectedRoute>
                            <WelcomeEducator />
                        </ProtectedRoute>} />
                            <Route path="/welcome" element={
                        <ProtectedRoute>
                            <WelcomePage />
                        </ProtectedRoute>
                    } />
                    <Route path="accetta-invito" element={
                        <ProtectedRoute>
                        <InvitoTeam />
                        </ProtectedRoute>
                    } />
                    <Route path="tournaments" element={
                        <ProtectedRoute>
                            <Tournaments />
                        </ProtectedRoute>
                    } />
                    <Route path="tournamentsEducator" element={
                        <ProtectedRoute>
                            <TournamentsEducator />
                        </ProtectedRoute>
                    } />
                    <Route path="createTournament" element={
                        <ProtectedRoute>
                            <CreationTournament/>
                        </ProtectedRoute>
                    } />
                    <Route path="createBattle/:tournamentId" element={
                        <ProtectedRoute>
                            <CreationBattle/>
                        </ProtectedRoute>
                    } />
                    <Route path="/tournament/:id" element={
                        <ProtectedRoute>
                            <TournamentDetails />
                        </ProtectedRoute>
                    } />
                    <Route path="/tournament/:tournamentId/battle/:battleId" element={
                        <ProtectedRoute>
                        <Battle />
                        </ProtectedRoute>
                    } />
                    <Route path="/finalSubmission/:tournamentId/:battleId" element={
                        <ProtectedRoute>
                        <FinalSubmission />
                        </ProtectedRoute>
                    } />
                    
                    <Route path="/projectDetails/:projectId"element={
                        <ProtectedRoute>
                        <ProjectDetails />
                        </ProtectedRoute>
                    } />
                </Routes>
            </BrowserRouter>
        </div>
  );
}

export default App;

