import React from 'react';
import { Link } from 'react-router-dom';
import { useLocation } from 'react-router-dom';
import '../style/Homepage.css';
const Header = () => {
  const location = useLocation();

  return (
    <header className="header">
      <nav className="navbar">
      <Link to="/blog" className="nav-link">Blog</Link>
        <Link to="/foreducator" className="nav-link">For Educators</Link>
        <Link to="/forstudent" className="nav-link">For Students</Link>
      </nav>
      {location.pathname === '/login' || location.pathname === '/registration' ? (
        <Link to="/" className="nav-link">
          <button className="access-button">Go Home</button>
        </Link>
      ) : (
        
        <Link to="/login" className="nav-link">
          <button className="access-button">Sign in</button>
        </Link>

      )}
    </header>
  );
};

export default Header;
