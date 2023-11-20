import React from "react";
import {useNavigate } from "react-router-dom";


const Home = () => {
  const navigate = useNavigate();

  const navigateToLogin = () => {
    navigate('/login');
  };
  return (
<div className="home d-flex ">
    <div className="logo col-6 pt-5">
    <img src="https://digital-lync.konalms.com/assets/logo.ab024049.png" alt="Logo"/>
    <h1 className="mt-5" >Welcome</h1>
    <p>Please sign in to your account below</p>
   
                  <button onClick={navigateToLogin} className="btn btn-primary  w-50 mt-5">
                    SignIn
                  </button>
    
    </div>
    <div className="image col-6" style={{height:"100vh"}}>
        <img src="https://digital-lync.konalms.com/assets/illustration-login.a3c562cb.jpg" alt="lms-logo" width='100%' height='100%'/>
    </div>
</div> 
       )};
       
 export default Home;