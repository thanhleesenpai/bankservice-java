import React, { useState } from 'react';
import { login } from '../services/AuthService.jsx';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import './Form.css';

const Login = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({ email: '', password: '' });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await login(formData);
      const token = res.data.token;
      localStorage.setItem('token', token);

      // Giải mã token để lấy role
      const decoded = jwtDecode(token);
      console.log('Decoded token:', decoded);

      alert('Đăng nhập thành công!');

      // Điều hướng dựa vào role
      if (decoded.role === 'ADMIN') {
        navigate('/admin-home');
      } else {
        navigate('/user-home');
      }
    } catch (err) {
      alert('Đăng nhập thất bại: ' + (err.response?.data?.message || err.message));
    }
  };

  return (
    <div className="form-container">
      <h2>Đăng nhập</h2>
      <form onSubmit={handleSubmit}>
        <input type="email" name="email" placeholder="Email" onChange={handleChange} required />
        <input type="password" name="password" placeholder="Mật khẩu" onChange={handleChange} required />
        <button type="submit">Đăng nhập</button>
        <p className="form-footer">
          Chưa có tài khoản? <span className="form-link" onClick={() => navigate('/register')}>Đăng ký ngay</span>
        </p>
      </form>
    </div>
  );
};

export default Login;
