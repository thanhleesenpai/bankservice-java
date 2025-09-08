import React, { useState } from 'react';
import { register } from '../services/AuthService.jsx';
import { useNavigate } from 'react-router-dom';
import './Form.css';

const Register = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    phoneNumber: '',
    customerName: '',
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await register(formData);
      alert('Đăng ký thành công!');
      navigate('/login');
    } catch (err) {
      alert('Đăng ký thất bại: ' + err.response?.data?.message || err.message);
    }
  };

  return (
    <div className="form-container">
      <h2>Đăng ký</h2>
      <form onSubmit={handleSubmit}>
        <input type="text" name="customerName" placeholder="Tên khách hàng" onChange={handleChange} required />
        <input type="email" name="email" placeholder="Email" onChange={handleChange} required />
        <input type="password" name="password" placeholder="Mật khẩu" onChange={handleChange} required />
        <input type="text" name="phoneNumber" placeholder="Số điện thoại" onChange={handleChange} required />
        <button type="submit">Đăng ký</button>
      </form>
    </div>
  );
};

export default Register;
