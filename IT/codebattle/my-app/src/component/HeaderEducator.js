import React from 'react';
import '../style/HeaderEducator.css'; 
import { Link } from 'react-router-dom';
import { useLocation } from 'react-router-dom';

function HeaderEducator() {
    const location = useLocation();
    const isTournamentPage = location.pathname === '/tournamentsEducator';

    function handleLogout() {
        localStorage.removeItem('jwtToken'); // Rimuovi il token
        // Reindirizza l'utente al login
    }

    return (
        <div className="headerEducator">
            <h1>CodeKataBattle</h1>
            <nav>
                <ul>
                    <li><Link to="/welcomeEducator">Welcome Page</Link></li>
                    <li>
                    
                        {isTournamentPage ? (
                            <Link to="/tournaments">All Tournaments</Link>
                        ) : (
                            <Link to="/tournamentsEducator">My Tournaments</Link>
                        )}
                    </li>
                    <li className="logout"><Link to="/login" onClick={handleLogout}>Logout</Link></li>
                </ul>
            </nav>
        </div>
    );
}

export default HeaderEducator;
