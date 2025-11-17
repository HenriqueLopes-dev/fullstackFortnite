import React, { useState, useEffect } from "react";
import {
  Container,
  Row,
  Col,
  Card,
  Table,
  Button,
  Pagination,
  Alert,
  Spinner,
} from "react-bootstrap";
import { Link } from "react-router-dom";
import api from "../api/AxiosConfig";

export const UsersList = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);

  const fetchUsers = async (page = 0) => {
    setLoading(true);
    setError("");

    try {
      const response = await api.get("/users", {
        params: { page, size: 20 },
      });

      setUsers(response.data.content || []);
      setTotalPages(response.data.page?.totalPages || 1);
      setCurrentPage(response.data.page?.number || 0);
    } catch (err) {
      setError(err.response?.data?.message || "Erro ao carregar usuários");
      setUsers([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const handlePageChange = (page) => {
    fetchUsers(page);
  };

  const renderPagination = () => {
    if (totalPages <= 1) return null;

    let items = [];
    for (let number = 0; number < totalPages; number++) {
      items.push(
        <Pagination.Item
          key={number}
          active={number === currentPage}
          onClick={() => handlePageChange(number)}
        >
          {number + 1}
        </Pagination.Item>
      );
    }

    return (
      <div className="d-flex justify-content-center mt-4">
        <Pagination>
          <Pagination.Prev
            disabled={currentPage === 0}
            onClick={() => handlePageChange(currentPage - 1)}
          />
          {items}
          <Pagination.Next
            disabled={currentPage === totalPages - 1}
            onClick={() => handlePageChange(currentPage + 1)}
          />
        </Pagination>
      </div>
    );
  };

  return (
    <Container className="mt-4">
      <Row className="justify-content-center">
        <Col lg={8}>
          <div className="text-center mb-4">
            <h1 className="fw-bold">Lista de Usuários</h1>
            <p className="text-muted">Todos os usuários do sistema</p>
          </div>

          {error && <Alert variant="danger">{error}</Alert>}

          <Card className="shadow">
            <Card.Body className="p-0">
              {loading ? (
                <div className="text-center p-5">
                  <Spinner
                    animation="border"
                    role="status"
                    className="text-primary"
                  >
                    <span className="visually-hidden">Carregando...</span>
                  </Spinner>
                  <p className="mt-3">Carregando usuários...</p>
                </div>
              ) : users.length === 0 ? (
                <div className="text-center p-5">
                  <p className="text-muted">Nenhum usuário encontrado</p>
                </div>
              ) : (
                <Table responsive hover className="mb-0">
                  <thead className="bg-light">
                    <tr>
                      <th>Avatar</th>
                      <th>Nome</th>
                      <th>Ações</th>
                    </tr>
                  </thead>
                  <tbody>
                    {users.map((user) => (
                      <tr key={user.id}>
                        <td>
                          <img
                            src={`https://ui-avatars.com/api/?name=${encodeURIComponent(
                              user.name
                            )}&background=667eea&color=fff&size=40`}
                            className="rounded-circle"
                            alt="Avatar"
                            style={{ width: "40px", height: "40px" }}
                          />
                        </td>
                        <td className="align-middle">
                          <strong>{user.name}</strong>
                        </td>
                        <td className="align-middle">
                          <Link to={`/user/${user.id}`}>
                            <Button variant="outline-primary" size="sm">
                              Ver Detalhes
                            </Button>
                          </Link>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              )}
            </Card.Body>
          </Card>

          {renderPagination()}
        </Col>
      </Row>
    </Container>
  );
};
