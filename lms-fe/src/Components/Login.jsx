import React from 'react'
import { Link } from 'react-router-dom'
import '../Styles/Login.css'

const Login = () => {
  return (
    <div className='bg-dark d-flex justify-content-center align-items-center '>
        <div className='bg-white rounded-3 col-3'>
          <img src="https://digital-lync.konalms.com/assets/logo.ab024049.png" className='py-4' alt="logo" width={"200px"}/>
          <h4 className='fw-normal pb-2'>Welcome</h4>
          <p>Login to continue to Digital Lync LMS.</p>
          <div className='px-1 py-2 d-flex flex-column gap-3'>
              <input type="email" placeholder='Email address' className='col-12 p-3 rounded-1' required/>
              <input type="password" placeholder='Password' className='col-12 p-3 rounded-1' required/>
              <p className='text-start mb-1' style={{fontSize:'14px', fontWeight:'700'}}><Link to="/forgot" className='text-decoration-none'>Forgot password?</Link></p>
              <Link to="/dashboard" className='mb-4'>
                          <button type='submit' className="col-12 p-2 btn btn-primary">
                              Continue
                          </button></Link>
          </div>
        </div>
    </div>
  )
}

export default Login
