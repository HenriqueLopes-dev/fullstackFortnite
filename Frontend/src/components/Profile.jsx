import React, { useState, useEffect } from "react";
import {
  Container,
  Row,
  Col,
  Card,
  Form,
  Button,
  Alert,
  Modal,
} from "react-bootstrap";
import { useAuth } from "./AuthContext";
import { useNavigate } from "react-router-dom";
import api from "../api/AxiosConfig";

export const Profile = () => {
  const { user, updateUser } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [showDeleteModal, setShowDeleteModal] = useState(false);

  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    currentPassword: "",
  });

  useEffect(() => {
    if (user) {
      setFormData({
        name: user.name || "",
        email: user.email || "",
        password: "",
        currentPassword: "",
      });
    }
  }, [user]);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    setSuccess("");

    try {
      const payload = {
        name: formData.name,
        email: formData.email,
        password: formData.password,
      };

      const response = await api.put(`/users/${user.id}`, payload);

      updateUser(response.data);
      setSuccess("Perfil atualizado com sucesso!");
      setFormData((prev) => ({
        ...prev,
        password: "",
        currentPassword: "",
      }));
    } catch (err) {
      setError(err.response?.data?.message || "Erro ao atualizar perfil");
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteAccount = async () => {
    try {
      setLoading(true);
      await api.delete(`/users/${user.id}`);
      await api.post("/auth/logout");
      window.location.href = "/";
    } catch (err) {
      setError(err.response?.data?.message || "Erro ao excluir conta");
      setLoading(false);
      setShowDeleteModal(false);
    }
  };

  if (!user) {
    return (
      <Container className="mt-4">
        <div className="text-center">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Carregando...</span>
          </div>
          <p className="mt-3">Carregando perfil...</p>
        </div>
      </Container>
    );
  }

  return (
    <Container className="mt-4">
      <Row className="justify-content-center">
        <Col lg={8}>
          <div className="text-center mb-4">
            <img
              src={`https://ui-avatars.com/api/?name=${encodeURIComponent(
                user.name
              )}&background=667eea&color=fff&size=120`}
              className="rounded-circle mb-3"
              alt="Avatar"
              style={{ width: "120px", height: "120px" }}
            />
            <h1 className="fw-bold">Editar Perfil</h1>
            <p className="text-muted">Atualize suas informações pessoais</p>
          </div>

          {error && <Alert variant="danger">{error}</Alert>}
          {success && <Alert variant="success">{success}</Alert>}

          <Card className="shadow">
            <Card.Body className="p-4">
              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3">
                  <Form.Label>Nome</Form.Label>
                  <Form.Control
                    type="text"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    required
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Email</Form.Label>
                  <Form.Control
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    required
                  />
                </Form.Group>

                <Form.Group className="mb-4">
                  <Form.Label>Nova Senha / Senha Atual</Form.Label>
                  <Form.Control
                    type="password"
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                    placeholder="Mude sua senha ou coloque a senha atual"
                  />
                </Form.Group>

                <div className="d-flex gap-2">
                  <Button type="submit" variant="primary" disabled={loading}>
                    {loading ? "Salvando..." : "Salvar Alterações"}
                  </Button>
                </div>
              </Form>

              <hr className="my-4" />

              <div className="text-center">
                <Button
                  variant="outline-danger"
                  onClick={() => setShowDeleteModal(true)}
                >
                  Excluir Conta
                </Button>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Confirmar Exclusão</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p className="text-danger fw-bold">
            Tem certeza que deseja excluir sua conta?
          </p>
          <p>Todos os seus dados serão perdidos permanentemente.</p>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
            Cancelar
          </Button>
          <Button
            variant="danger"
            onClick={handleDeleteAccount}
            disabled={loading}
          >
            {loading ? "Excluindo..." : "Excluir"}
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
};
