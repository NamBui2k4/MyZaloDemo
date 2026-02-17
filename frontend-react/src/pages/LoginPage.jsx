import { useState, useContext } from "react";
import { login as loginApi } from "../api/authApi";
import { AuthContext } from "../context/AuthContext";

import "./login.css";

export default function LoginPage() {
  const [phone, setPhone] = useState("");
  const [password, setPassword] = useState("");
  const { login } = useContext(AuthContext);

  const handleLogin = async () => {
    if (!phone || !password) {
      alert("Please enter phone and password");
      return;
    }

    try {
      const res = await loginApi({ phone, password });
      login(res.data.user, res.data.token);
    } catch (err) {
      alert("Login failed");
    }
  };

  return (
    <div className="login-page">
      <div
        style={{
          width: 56,
          height: 56,
          borderRadius: "50%",
          background: "#4da3ff",
          margin: "0 auto 18px",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          color: "white",
          fontWeight: "bold",
          fontSize: "18px",
          letterSpacing: "1px",
        }}
      >
        Chat
      </div>

      <div className="login-card">
        <div className="login-title">Chat Application</div>
        <div className="login-subtitle">Sign in to continue chatting</div>

        <input
          className="login-input"
          placeholder="Phone number"
          value={phone}
          onChange={(e) => setPhone(e.target.value)}
        />

        <input
          className="login-input"
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <button className="login-button" onClick={handleLogin}>
          Login
        </button>

        <div className="login-footer">© 2026 Chat Application</div>
      </div>
    </div>
  );
}
