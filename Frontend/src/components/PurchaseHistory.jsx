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
import { useNavigate } from "react-router-dom";
import { useAuth } from "./AuthContext";
import api from "../api/AxiosConfig";

export const PurchaseHistory = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [bundles, setBundles] = useState({ content: [] });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [refundLoading, setRefundLoading] = useState(null);

  useEffect(() => {
    if (user) {
      fetchPurchaseHistory();
    }
  }, [user]);

  const fetchPurchaseHistory = async () => {
    setLoading(true);
    setError("");

    try {
      const response = await api.get(`/users/${user.id}/purchase-history`);
      setBundles(
        response.data && response.data.content ? response.data : { content: [] }
      );
    } catch (err) {
      setError(
        err.response?.data?.message || "Erro ao carregar histórico de compras"
      );
      setBundles({ content: [] });
    } finally {
      setLoading(false);
    }
  };

  const handleRefund = async (bundleId) => {
    setRefundLoading(bundleId);
    setError("");

    try {
      await api.post(`/users/me/acquired-cosmetics/${bundleId}/refund`);

      // Atualiza o bundle para refund: true
      setBundles((prev) => ({
        ...prev,
        content: prev.content.map((bundle) =>
          bundle.id === bundleId ? { ...bundle, refund: true } : bundle
        ),
      }));
    } catch (err) {
      setError(err.response?.data?.message || "Erro ao realizar reembolso");
    } finally {
      setRefundLoading(null);
    }
  };

  // Função para formatar data
  const formatDate = (dateString) => {
    if (!dateString) return "-";
    const date = new Date(dateString);
    return date.toLocaleDateString("pt-BR");
  };

  // Renderiza card de bundle
  const renderBundleCard = (bundle) => {
    const bundleName = bundle.bundleName || "Bundle";
    const bundleImage =
      bundle.bundleImage ||
      (bundle.cosmetics && bundle.cosmetics[0]?.imageUrl) ||
      "https://via.placeholder.com/80x80/ffffff/667eea?text=🎮";

    const isRefunded = bundle.refund === true;

    return (
      <div key={bundle.id} className="col-md-6 col-lg-4 mb-4">
        <Card
          className="h-100 shadow"
          style={{
            opacity: isRefunded ? 0.6 : 1,
            filter: isRefunded ? "grayscale(50%)" : "none",
          }}
        >
          {/* Header do Bundle */}
          <div
            className="position-relative"
            style={{
              height: "200px",
              background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
              overflow: "hidden",
            }}
          >
            <img
              src={bundleImage}
              className="w-100 h-100"
              style={{
                objectFit: "cover",
              }}
              alt={bundleName}
              onError={(e) => {
                e.target.src =
                  "https://via.placeholder.com/80x80/ffffff/667eea?text=🎮";
              }}
            />

            <Badge bg="dark" className="position-absolute top-0 start-0 m-2">
              {bundle.cosmetics?.length || 0} itens
            </Badge>

            {bundle.price && (
              <Badge bg="success" className="position-absolute top-0 end-0 m-2">
                {bundle.price} V-Bucks
              </Badge>
            )}

            {isRefunded && (
              <Badge
                bg="secondary"
                className="position-absolute bottom-0 start-0 m-2"
              >
                REEMBOLSADO!
              </Badge>
            )}
          </div>

          <Card.Body>
            <Card.Title className="fw-bold mb-2">{bundleName}</Card.Title>

            <div className="mb-3">
              <small className="text-muted">
                Comprado em: {formatDate(bundle.createdAt)}
              </small>
            </div>

            <div className="d-flex gap-2">
              <Button
                variant="outline-primary"
                size="sm"
                className="flex-grow-1"
                onClick={() =>
                  navigate(`/bundle-history/${bundle.id}`, {
                    state: { bundle },
                  })
                }
              >
                Ver Detalhes
              </Button>

              {!isRefunded && (
                <Button
                  variant="outline-danger"
                  size="sm"
                  onClick={() => handleRefund(bundle.id)}
                  disabled={refundLoading === bundle.id}
                >
                  {refundLoading === bundle.id ? (
                    <Spinner animation="border" size="sm" />
                  ) : (
                    "Reembolsar"
                  )}
                </Button>
              )}
            </div>
          </Card.Body>
        </Card>
      </div>
    );
  };

  if (!user) {
    return (
      <Container className="mt-4">
        <div className="text-center">
          <Spinner animation="border" role="status" className="text-primary">
            <span className="visually-hidden">Carregando...</span>
          </Spinner>
        </div>
      </Container>
    );
  }

  return (
    <Container className="mt-4">
      <Row className="justify-content-center">
        <Col lg={10}>
          <div className="d-flex justify-content-between align-items-center mb-4">
            <h1 className="fw-bold">Histórico de Compras</h1>
            <Button
              variant="outline-secondary"
              onClick={() => navigate("/profile")}
            >
              ← Voltar ao Perfil
            </Button>
          </div>

          {error && <Alert variant="danger">{error}</Alert>}

          <Card className="shadow">
            <Card.Body>
              {loading ? (
                <div className="text-center p-4">
                  <Spinner
                    animation="border"
                    role="status"
                    className="text-primary"
                  >
                    <span className="visually-hidden">Carregando...</span>
                  </Spinner>
                  <p className="mt-3">Carregando histórico de compras...</p>
                </div>
              ) : bundles.content.length === 0 ? (
                <div className="text-center p-4">
                  <p className="text-muted">Nenhuma compra encontrada</p>
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
