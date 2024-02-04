import React from 'react';
import '../style/Homepage.css';
import Header from './Header'; 
import { Link } from 'react-router-dom';

const HomePage = () => {
  return (
    <div className="homepage">
      <div className="header-container">
            <Header/>
            </div>
      <main className="content">
        <div className="circular-text-box">
          <h1>Team Collaboration</h1>
          <p>Engage in real-world coding scenarios and enhance your problem-solving skills.</p>
        </div>
        <div className="circular-text-box">
          <h1>Why choose CKB?</h1>
          <p>Some more text goes here.</p>
          <Link to="/login"><button className="main-button">Get Started</button></Link>
        </div>
        <div className="circular-text-box">
          <h1>Real challenges</h1>
          <p>Engage in real-world coding scenarios and enhance your problem-solving skills.</p>
        </div>
      </main>
    </div>
  );
};

export default HomePage;
