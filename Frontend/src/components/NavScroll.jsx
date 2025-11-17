import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
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
          <span className="ms-2">Fortnite Shop</span>
        </Navbar.Brand>

        <Navbar.Toggle aria-controls="navbarScroll" />

        <Navbar.Collapse id="navbarScroll">
          <Nav className="me-auto">
            <Nav.Link href="/" className="text-dark fw-medium">
              Home
            </Nav.Link>
            <Nav.Link href="/users" className="text-dark fw-medium">
              Usuários
            </Nav.Link>
          </Nav>

          <div className="d-flex align-items-center">
            {/* Balance separado */}
            {user && (
              <div className="text-warning bg-warning bg-opacity-25 rounded px-3 py-2 border border-warning me-5">
                <span className="fw-bold text-dark">
                  {user.balance.toLocaleString()}
                </span>
                <span className="text-warning ms-1">V-Bucks</span>
              </div>
            )}

            {/* Dropdown do Perfil completamente separado */}
            {user ? (
              <NavDropdown
                title={
                  <div className="d-flex align-items-center justify-content-center pt-4">
                    <img
                      src={`https://ui-avatars.com/api/?name=${encodeURIComponent(
                        user.name
                      )}&background=667eea&color=fff&size=40`}
                      className="rounded-circle"
                      style={{
                        width: "40px",
                        height: "40px",
                        border: "2px solid #dee2e6",
                      }}
                      alt="Perfil"
                    />
                  </div>
                }
                id="profile-dropdown"
                align="end"
                className="profile-dropdown border-0 me-2"
                show={undefined}
              >
                <div className="px-3 py-2 border-bottom">
                  <small className="text-muted">Logado como</small>
                  <div className="fw-bold">{user.name}</div>
                </div>

                <NavDropdown.Item
                  onClick={() => navigate("/profile")}
                  className="d-flex align-items-center"
                >
                  Minha Conta
                </NavDropdown.Item>

                <NavDropdown.Item
                  onClick={() => navigate(`/user/${user.id}`)}
                  className="d-flex align-items-center"
                >
                  Meus Cosméticos
                </NavDropdown.Item>

                <NavDropdown.Item
                  onClick={() => navigate("/purchase-history")}
                  className="d-flex align-items-center"
                >
                  Histórico de Compras
                </NavDropdown.Item>

                <NavDropdown.Divider />

                <NavDropdown.Item
                  onClick={logout}
                  className="d-flex align-items-center text-danger"
                >
                  Sair
                </NavDropdown.Item>
              </NavDropdown>
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
