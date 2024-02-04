import React from 'react';
import '../style/HeaderStudent.css';
import { Link } from 'react-router-dom';


function HeaderStudent() {

    function handleLogout() {
        localStorage.removeItem('jwtToken'); // Rimuovi il token
        // Reindirizza l'utente al login
    }
    return (
        <div className="headerStudent">
            <h1>CodeKataBattle</h1>
            <nav>
                <ul>
                    <li><Link to="/welcome">Welcome Page</Link></li>
                    <li><Link to="/tournaments">My Tournaments</Link></li>
                    <li className="logout"><Link to="/login" onClick={handleLogout}>Logout</Link></li>
                </ul>
            </nav>
        </div>
    );
}

export default HeaderStudent;
