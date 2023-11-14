import React from 'react'
import { Link } from 'react-router-dom'
import '../Styles/Login.css'

const Login = () => {
  return (
    <div className='bg-dark d-flex justify-content-center align-items-center '>
        <div className='bg-white rounded-3'>
          <img src="https://digital-lync.konalms.com/assets/logo.ab024049.png" className='py-5' alt="logo" width={"200px"}/>
          <h4 className='fw-normal pb-2'>Welcome</h4>
          <p>Login to Digital Lync to continue to Digital Lync LMS.</p>
          <div className='px-3 py-3 d-flex flex-column gap-3'>
            <input type="email" placeholder='Email address' className='col-12 p-3 rounded-1'/>
            <input type="password" placeholder='Password' className='col-12 p-3 rounded-1'/>
            <Link className='text-decoration-none'><p className='text-start fw-bold'>Forgot password?</p></Link>
            <Link to="/" className='my-2'>
                        <button className="col-12 p-3 btn btn-primary">
                            Continue
                        </button></Link>
          </div>
        </div>
    </div>
  )
}

export default Login
