import React, { useState, useEffect } from "react";
import {
  Container,
  Row,
  Col,
  Card,
  Button,
  Alert,
  Spinner,
  Badge,
} from "react-bootstrap";
import { useParams, useNavigate } from "react-router-dom";
import api from "../api/AxiosConfig";

export const UserDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [user, setUser] = useState(null);
  const [bundles, setBundles] = useState({ content: [] });
  const [loading, setLoading] = useState(true);
  const [bundlesLoading, setBundlesLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchUser();
  }, [id]);

  const fetchUser = async () => {
    setLoading(true);
    setError("");

    try {
      const response = await api.get(`/users/${id}`);
      setUser(response.data);
      fetchUserBundles();
    } catch (err) {
      setError(err.response?.data?.message || "Erro ao carregar usuário");
      setLoading(false);
    }
  };

  const fetchUserBundles = async () => {
    setBundlesLoading(true);
    try {
      const response = await api.get(`/users/${id}/acquired-cosmetics`);
      setBundles(
        response.data && response.data.content ? response.data : { content: [] }
      );
    } catch (err) {
      console.error("Erro ao buscar bundles:", err);
      setBundles({ content: [] });
    } finally {
      setBundlesLoading(false);
      setLoading(false);
    }
  };

  const getRarityColor = (rarity) => {
    const colors = {
      common: "secondary",
      uncommon: "success",
      rare: "primary",
      epic: "warning",
      legendary: "danger",
      marvel: "dark",
      dc: "info",
      icon: "light",
      starwars: "dark",
    };
    return colors[rarity?.toLowerCase()] || "secondary";
  };

  const formatDate = (dateString) => {
    if (!dateString) return "-";
    const date = new Date(dateString);
    return date.toLocaleDateString("pt-BR");
  };

  const renderBundleCard = (bundle) => {
    const bundleName = bundle.bundleName || "Bundle";
    const bundleImage =
      bundle.bundleImage ||
      (bundle.cosmetics && bundle.cosmetics[0]?.imageUrl) ||
      "https://via.placeholder.com/80x80/ffffff/667eea?text=🎮";

    const hasDiscount = false;

    return (
      <div key={bundle.id} className="col-md-6 col-lg-4 mb-4">
        <Card className="h-100 shadow">
          <div
            className="d-flex justify-content-center align-items-center p-3 position-relative cursor-pointer"
            style={{
              height: "230px",
              background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
              overflow: "hidden",
              cursor: "pointer",
            }}
            onClick={() =>
              navigate(`/bundle-history/${bundle.id}`, { state: { bundle } })
            }
          >
            <div
              style={{
                position: "absolute",
                top: "-50%",
                left: "-50%",
                width: "200%",
                height: "200%",
                background:
                  "radial-gradient(circle, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0) 70%)",
                transform: "rotate(30deg)",
              }}
            />

            <img
              src={bundleImage}
              className="position-relative z-1 img-fluid"
              style={{
                maxHeight: "200px",
                width: "auto",
                filter: "drop-shadow(0 4px 8px rgba(0,0,0,0.3))",
              }}
              alt={bundleName}
              onError={(e) => {
                e.target.src =
                  "https://via.placeholder.com/80x80/ffffff/667eea?text=🎮";
                e.target.style.filter = "none";
              }}
            />

            <Badge className="position-absolute top-0 start-0 bg-dark m-2 small">
              {bundle.cosmetics?.length || 0}
            </Badge>
            {bundle.price && (
              <Badge className="position-absolute top-0 end-0 bg-success m-2 small">
                {bundle.price} V-Bucks
              </Badge>
            )}
          </div>

          <Card.Body className="d-flex flex-column">
            <Card.Title
              className="fw-bold cursor-pointer mb-2"
              style={{ cursor: "pointer" }}
              onClick={() =>
                navigate(`/bundle-history/${bundle.id}`, { state: { bundle } })
              }
            >
              {bundleName}
            </Card.Title>

            <div className="d-flex justify-content-between align-items-center mb-2">
              <span className={`fw-bold ${hasDiscount ? "text-success" : ""}`}>
                {bundle.price} V-Bucks
              </span>
              {hasDiscount && (
                <small className="text-decoration-line-through text-muted">
                  {bundle.regularPrice}
                </small>
              )}
            </div>

            <div className="mt-auto">
              <small className="text-muted d-block mb-2">
                Comprado em: {formatDate(bundle.createdAt)}
              </small>
              <Button
                variant="outline-primary"
                size="sm"
                className="w-100"
                onClick={() =>
                  navigate(`/bundle-history/${bundle.id}`, {
                    state: { bundle },
                  })
                }
              >
                Ver Detalhes
              </Button>
            </div>
          </Card.Body>
        </Card>
      </div>
    );
  };

  if (loading) {
    return (
      <Container className="mt-4">
        <div className="text-center">
          <Spinner animation="border" role="status" className="text-primary">
            <span className="visually-hidden">Carregando...</span>
          </Spinner>
          <p className="mt-3">Carregando usuário...</p>
        </div>
      </Container>
    );
  }

  if (error || !user) {
    return (
      <Container className="mt-4">
        <Alert variant="danger">
          {error || "Usuário não encontrado"}
          <div className="mt-3">
            <Button variant="primary" onClick={() => navigate("/users")}>
              ← Voltar para Lista
            </Button>
          </div>
        </Alert>
      </Container>
    );
  }

  const totalCosmetics = bundles.content.reduce((total, bundle) => {
    return total + (bundle.cosmetics?.length || 0);
  }, 0);

  return (
    <Container className="mt-4">
      <Row className="justify-content-center">
        <Col lg={10}>
          <Button
            variant="outline-secondary"
            onClick={() => navigate("/users")}
            className="mb-3"
          >
            ← Voltar para Lista
          </Button>

          <Card className="shadow mb-4">
            <Card.Body className="p-4">
              <Row className="align-items-center">
                <Col md={3} className="text-center">
                  <img
                    src={`https://ui-avatars.com/api/?name=${encodeURIComponent(
                      user.name
                    )}&background=667eea&color=fff&size=120`}
                    className="rounded-circle mb-3"
                    alt="Avatar"
                    style={{ width: "120px", height: "120px" }}
                  />
                </Col>
                <Col md={9}>
                  <h2 className="fw-bold mb-3">{user.name}</h2>
                  <Row>
                    <Col sm={6}>
                      <p>
                        <strong>ID:</strong> <code>{user.id}</code>
                      </p>
                      {user.email && (
                        <p>
                          <strong>Email:</strong> {user.email}
                        </p>
                      )}
                    </Col>
                    <Col sm={6}>
                      {user.balance !== undefined && (
                        <p>
                          <strong>Saldo:</strong>
                          <Badge bg="success" className="ms-2 fs-6">
                            {user.balance} V-Bucks
                          </Badge>
                        </p>
                      )}
                      <p>
                        <strong>Bundles:</strong>
                        <Badge bg="primary" className="ms-2 fs-6">
                          {bundles.content.length}
                        </Badge>
                      </p>
                      <p>
                        <strong>Cosméticos:</strong>
                        <Badge bg="info" className="ms-2 fs-6">
                          {totalCosmetics}
                        </Badge>
                      </p>
                    </Col>
                  </Row>
                </Col>
              </Row>
            </Card.Body>
          </Card>

          <Card className="shadow">
            <Card.Header className="bg-white">
              <h4 className="mb-0 fw-bold">Bundles Adquiridos</h4>
              <small className="text-muted">
                {bundles.content.length} bundles • {totalCosmetics} cosméticos
              </small>
            </Card.Header>
            <Card.Body>
              {bundlesLoading ? (
                <div className="text-center p-4">
                  <Spinner
                    animation="border"
                    role="status"
                    className="text-primary"
                  >
                    <span className="visually-hidden">Carregando...</span>
                  </Spinner>
                  <p className="mt-3">Carregando bundles...</p>
                </div>
              ) : bundles.content.length === 0 ? (
                <div className="text-center p-4">
                  <p className="text-muted">Nenhum bundle adquirido</p>
                </div>
              ) : (
                <Row>
                  {bundles.content.map((bundle) => renderBundleCard(bundle))}
                </Row>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};
