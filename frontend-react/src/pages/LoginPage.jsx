import { useState, useContext } from "react";
import { login as loginApi } from "../api/authApi";
import { AuthContext } from "../context/AuthContext";

export default function LoginPage() {
  const [phone, setPhone] = useState("");
  const [password, setPassword] = useState("");
  const { login } = useContext(AuthContext);

  const handleLogin = async () => {
    const res = await loginApi({ phone, password });
    login(res.data.user, res.data.token);
  };

  return (
    <div>
      <h2>Login</h2>
      <input placeholder="Phone" onChange={e => setPhone(e.target.value)} />
      <input placeholder="Password" type="password"
             onChange={e => setPassword(e.target.value)} />
      <button onClick={handleLogin}>Login</button>
    </div>
  );
}
