import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import Form from "react-bootstrap/Form";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import NavDropdown from "react-bootstrap/NavDropdown";
import { useAuth } from "./AuthContext";
import { useNavigate } from "react-router-dom";
import api from "../api/AxiosConfig";

export const NavScroll = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const logout = async () => {
    await api.post("/auth/logout");
    window.location.reload(false);
  };
  return (
    <Navbar
      expand="lg"
      className="bg-light"
      variant="light"
      style={{ borderBottom: "5px solid #e9ecef" }}
    >
      <Container fluid>
        <Navbar.Brand
          href="/"
          className="d-flex align-items-center fw-bold text-dark"
        >
          🎮 <span className="ms-2">Fortnite Shop</span>
        </Navbar.Brand>

        <Navbar.Toggle aria-controls="navbarScroll" />

        <Navbar.Collapse id="navbarScroll">
          <Nav className="me-auto">
            <Nav.Link href="/" className="text-dark fw-medium">
              Home
            </Nav.Link>
            <Nav.Link href="#shop" className="text-dark fw-medium">
              Loja
            </Nav.Link>
            <Nav.Link href="#bundles" className="text-dark fw-medium">
              Bundles
            </Nav.Link>
          </Nav>

          <div className="d-flex align-items-center gap-3">
            {/* Balance e usuário */}
            {user && (
              <>
                <div className="text-warning bg-warning bg-opacity-25 rounded px-3 py-2 border border-warning">
                  <span className="fw-bold text-dark">
                    {user.balance.toLocaleString()}
                  </span>
                  <span className="text-warning ms-2">V-Bucks</span>
                </div>
                <div className="text-dark">
                  <small className="text-muted">Olá,</small>
                  <br />
                  <strong>{user.name}</strong>
                </div>
              </>
            )}

            {user ? (
              <Button variant="outline-danger" size="sm" onClick={logout}>
                Sair
              </Button>
            ) : (
              <Button
                variant="primary"
                size="sm"
                onClick={() => navigate("/login")}
              >
                Entrar
              </Button>
            )}
          </div>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};
