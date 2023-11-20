import React from 'react';
import './Login.css';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import { useDispatch } from 'react-redux';
import { loginSuccess, loginFailure } from '../actions/loginActions';
import { useNavigate } from 'react-router-dom';

const Login = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const initialValues = {
    email: '',
    password: '',
  };

  const validationSchema = Yup.object({
    email: Yup.string().email('Invalid email address').required('Email is required'),
    password: Yup.string().required('Password is required'),
  });

  const handleSubmit = async (values, { setSubmitting }) => {
    try {
      const response = await axios.post('http://localhost:8080/user/login', {
        email: values.email,
        password: values.password,
      });
      if (response.data) {
        dispatch(loginSuccess(response.data));
        navigate('/dashboard');
      } else {
        dispatch(loginFailure());
      }
    } catch (error) {
      dispatch(loginFailure());
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="bg-dark d-flex justify-content-center align-items-center">
      <div className="bg-white rounded-3 col-3">
        <img
          src="https://digital-lync.konalms.com/assets/logo.ab024049.png"
          className="py-4"
          alt="logo"
          width={'200px'}
        />
        <h4 className="fw-normal pb-2">Welcome</h4>
        <p>Login to continue to Digital Lync LMS.</p>

        <Formik initialValues={initialValues} validationSchema={validationSchema} onSubmit={handleSubmit}>
          <Form className="px-1 py-2 d-flex flex-column gap-3">
            <Field
              type="text"
              name="email"
              placeholder="Email address"
              required
              className="col-12 p-3 rounded-1"
            />
            <ErrorMessage name="email" component="div" className="text-danger" />

            <Field
              type="password"
              name="password"
              placeholder="Password"
              required
              className="col-12 p-3 rounded-1"
            />
            <ErrorMessage name="password" component="div" className="text-danger" />

            <p className="text-start mb-1" style={{ fontSize: '14px', fontWeight: '700' }}>
              <Link to="/forgot" className="text-decoration-none">
                Forgot password?
              </Link>
            </p>

            <button type="submit" className="btn btn-primary col-12 p-2 mb-4">
              Continue
            </button>
          </Form>
        </Formik>
      </div>
    </div>
  );
};

export default Login;
