import React from 'react'
import { Link } from 'react-router-dom'

const Home = () => {
  return (
    <div className='d-flex'>
        <div className="logo col-6 p-5">
            <img src="https://digital-lync.konalms.com/assets/logo.ab024049.png" alt="Logo"/>
            <h1 className='pt-5'>Welcome</h1>
            <p className='pt-2'>Please sign in to your account below</p>
            <Link to="/login">
                        <button className="btn btn-primary w-50 mt-5">
                            SignIn
                        </button></Link>
        </div>
        <div className="image col-6" style={{height: '100vh'}}>
            <img src="https://digital-lync.konalms.com/assets/illustration-login.a3c562cb.jpg" alt="image" width="100%" height='100%'/>
        </div>
    </div>
  )
}

export default Home
