import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
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
import { useAuth } from "./AuthContext";
import api from "../api/AxiosConfig";

export const BundleHistoryDetails = () => {
  const { bundleId } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();
  const [bundle, setBundle] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    if (user && bundleId) {
      fetchBundleDetails();
    }
  }, [user, bundleId]);

  const fetchBundleDetails = async () => {
    setLoading(true);
    setError("");

    try {
      const response = await api.get(
        `/users/${user.id}/purchase-history/${bundleId}`
      );
      setBundle(response.data);
    } catch (err) {
      setError(
        err.response?.data?.message || "Erro ao carregar detalhes do bundle"
      );
    } finally {
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
      frozen: "info",
      lava: "danger",
      shadow: "dark",
    };
    return colors[rarity?.toLowerCase()] || "secondary";
  };

  const handleBack = () => {
    navigate(-1);
  };

  const formatDate = (dateString) => {
    if (!dateString) return "-";
    const date = new Date(dateString);
    return date.toLocaleDateString("pt-BR");
  };

  if (loading) {
    return (
      <Container className="mt-4">
        <div className="text-center py-5">
          <Spinner animation="border" role="status" className="text-primary">
            <span className="visually-hidden">Carregando...</span>
          </Spinner>
          <p className="mt-3">Carregando detalhes do bundle...</p>
        </div>
      </Container>
    );
  }

  if (error || !bundle) {
    return (
      <Container className="mt-4">
        <Alert variant="danger">
          {error || "Bundle não encontrado"}
          <div className="mt-3">
            <Button variant="primary" onClick={handleBack}>
              ← Voltar
            </Button>
          </div>
        </Alert>
      </Container>
    );
  }

  const bundleName = bundle.bundleName || "Bundle";
  const bundleImage = bundle.bundleImage || bundle.cosmetics?.[0]?.imageUrl;

  return (
    <Container className="mt-4">
      <Button variant="outline-secondary mb-4" onClick={handleBack}>
        ← Voltar
      </Button>

      <div className="row">
        {/* Imagem do Bundle - Lado Esquerdo */}
        <div className="col-lg-6 mb-4">
          <Card className="border-0 shadow-lg">
            <div
              className="d-flex justify-content-center align-items-center p-5 position-relative"
              style={{
                background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
              }}
            >
              <img
                src={bundleImage}
                className="img-fluid"
                style={{
                  maxHeight: "500px",
                }}
                alt={bundleName}
                onError={(e) => {
                  e.target.src =
                    "https://via.placeholder.com/300x300/ffffff/667eea?text=🎮";
                  e.target.style.filter = "none";
                }}
              />
            </div>

            {/* Informações de compra */}
            <Card.Footer className="bg-transparent border-0 pt-0">
              <div className="d-flex justify-content-between align-items-center">
                <Badge bg="success" className="fs-6 px-3 py-2">
                  Comprado por {bundle.price} V-Bucks
                </Badge>
                <small className="text-muted">
                  Em {formatDate(bundle.createdAt)}
                </small>
              </div>
              {bundle.refund && (
                <Badge bg="secondary" className="mt-2">
                  Reembolsado
                </Badge>
              )}
            </Card.Footer>
          </Card>
        </div>

        <div className="col-lg-6 mb-4">
          <Card className="border-0 shadow-lg h-100">
            <Card.Body className="d-flex flex-column p-4">
              <h1 className="card-title display-6 fw-bold text-dark mb-3">
                {bundleName}
              </h1>

              <div className="border-top pt-4 mt-auto">
                <h5 className="fw-bold mb-3">Detalhes da Compra</h5>

                <div className="row text-center">
                  <div className="col-4">
                    <div className="border-end">
                      <div className="fw-bold text-primary fs-4">
                        {bundle.cosmetics?.length || 0}
                      </div>
                      <small className="text-muted">Itens</small>
                    </div>
                  </div>
                  <div className="col-4">
                    <div className="border-end">
                      <div className="fw-bold text-success fs-4">
                        {bundle.price}
                      </div>
                      <small className="text-muted">V-Bucks</small>
                    </div>
                  </div>
                  <div className="col-4">
                    <div>
                      <div className="fw-bold text-warning fs-4">
                        {bundle.cosmetics?.filter((cos) => cos.isNew)?.length ||
                          0}
                      </div>
                      <small className="text-muted">Novos</small>
                    </div>
                  </div>
                </div>
              </div>
            </Card.Body>
          </Card>
        </div>
      </div>

      {/* Lista de Cosméticos do Bundle */}
      <div className="row mt-2">
        <div className="col-12">
          <Card className="border-0 shadow-lg">
            <Card.Header className="bg-transparent border-0 py-4">
              <h2 className="mb-0 fw-bold">Itens do Bundle</h2>
              <p className="text-muted mb-0 mt-2">
                Todos os itens que você adquiriu neste bundle
              </p>
            </Card.Header>
            <Card.Body className="p-4">
              <div className="row g-4">
                {bundle.cosmetics?.map((cosmetic) => (
                  <div key={cosmetic.externalId} className="col-xl-4 col-lg-6">
                    <Card
                      className="h-100 border-0 shadow-sm"
                      style={{
                        cursor: "pointer",
                        transition: "all 0.3s ease",
                      }}
                      onClick={() =>
                        navigate(`/cosmetic/${cosmetic.externalId}`)
                      }
                      onMouseEnter={(e) => {
                        e.currentTarget.style.transform = "translateY(-5px)";
                      }}
                      onMouseLeave={(e) => {
                        e.currentTarget.style.transform = "translateY(0)";
                      }}
                    >
                      <Card.Body className="p-0">
                        <div className="d-flex">
                          {/* Imagem do Cosmético */}
                          <div
                            className="d-flex align-items-center justify-content-center p-3"
                            style={{
                              width: "100px",
                              background:
                                "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                            }}
                          >
                            <img
                              src={
                                cosmetic.imageUrl ||
                                "https://via.placeholder.com/60x60/ffffff/667eea?text=🎮"
                              }
                              className="img-fluid"
                              style={{
                                maxHeight: "60px",
                                width: "auto",
                                filter:
                                  "drop-shadow(0 4px 8px rgba(0,0,0,0.3))",
                              }}
                              alt={cosmetic.name}
                              onError={(e) => {
                                e.target.src =
                                  "https://via.placeholder.com/60x60/ffffff/667eea?text=🎮";
                                e.target.style.filter = "none";
                              }}
                            />
                          </div>

                          {/* Informações do Cosmético */}
                          <div className="flex-grow-1 p-3">
                            <h6 className="fw-bold mb-1 text-truncate">
                              {cosmetic.name}
                            </h6>
                            <p
                              className="text-muted small mb-2"
                              style={{
                                display: "-webkit-box",
                                WebkitLineClamp: 2,
                                WebkitBoxOrient: "vertical",
                                overflow: "hidden",
                              }}
                            >
                              {cosmetic.description}
                            </p>

                            <div className="d-flex justify-content-between align-items-center">
                              <Badge bg={getRarityColor(cosmetic.rarity)}>
                                {cosmetic.rarity}
                              </Badge>
                              <div className="d-flex gap-1">
                                {cosmetic.isNew && (
                                  <Badge bg="success" className="small">
                                    Novo
                                  </Badge>
                                )}
                                <Badge
                                  bg="outline-secondary"
                                  className="small text-capitalize"
                                >
                                  {cosmetic.type}
                                </Badge>
                              </div>
                            </div>

                            {cosmetic.added && (
                              <small className="text-muted d-block mt-2">
                                Adicionado: {formatDate(cosmetic.added)}
                              </small>
                            )}
                          </div>
                        </div>
                      </Card.Body>
                    </Card>
                  </div>
                ))}
              </div>
            </Card.Body>
          </Card>
        </div>
      </div>
    </Container>
  );
};
