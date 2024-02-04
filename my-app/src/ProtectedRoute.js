import React from 'react';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ children }) => {
    const token = localStorage.getItem('jwtToken');
    
    if (!token) {
        // Se non c'è un token, reindirizza all'accesso
        return <Navigate to="/login" replace />;
    }

    // Aggiungi qui eventuali altre verifiche, come la validità del token

    return children;
};

export default ProtectedRoute;
