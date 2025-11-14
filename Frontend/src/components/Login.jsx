import React, { useState, useEffect } from "react";
import { api } from "../api/AxiosConfig";
import "bootstrap/dist/css/bootstrap.min.css";
import { useNavigate } from "react-router-dom";

export const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState(null);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  // Auto focus no campo email
  useEffect(() => {
    document.getElementById("email")?.focus();
  }, []);

  async function handleSubmit(e) {
    e.preventDefault();
    setMessage("");
    setLoading(true);

    try {
      const res = await api.post("/auth/login", {
        email,
        password,
      });

      // Nenhum token é manipulado aqui — o cookie chega automaticamente
      setMessage("Login realizado com sucesso! Redirecionando...");

      setTimeout(() => {
        window.location.href = "/";
      }, 800);
    } catch (err) {
      const errorMessage =
        err.response?.data?.message ||
        err.response?.data?.error ||
        "Credenciais inválidas.";

      setMessage(`Erro: ${errorMessage}`);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="container d-flex justify-content-center align-items-center vh-100">
      <div className="card shadow p-4" style={{ width: "380px" }}>
        <h3 className="text-center mb-4">Login</h3>

        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label className="form-label">Email</label>
            <input
              id="email"
              type="email"
              className="form-control"
              placeholder="email@exemplo.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Senha</label>
            <input
              type="password"
              className="form-control"
              placeholder="********"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          {message && (
            <div className={`alert alert-${message.type}`} role="alert">
              {message.text}
            </div>
          )}

          <p className="mt-3">
            Não possui uma conta?{" "}
            <a
              className=" cursor-pointer  p-0 text-primary text-decoration-none"
              onClick={() => navigate("/register")}
            >
              Criar conta
            </a>
          </p>

          <button
            type="submit"
            className="btn btn-primary w-100"
            disabled={loading}
          >
            {loading ? (
              <>
                <span className="spinner-border spinner-border-sm me-2"></span>
                Entrando...
              </>
            ) : (
              "Entrar"
            )}
          </button>
        </form>
      </div>
    </div>
  );
};
