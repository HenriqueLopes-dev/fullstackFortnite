import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { api } from "../api/AxiosConfig";

export const CosmeticDetails = () => {
  const { externalId } = useParams();
  const navigate = useNavigate();
  const [cosmetic, setCosmetic] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchCosmeticDetails = async () => {
      try {
        setLoading(true);
        const response = await api.get(`/cosmetics/${externalId}`);
        setCosmetic(response.data);
      } catch (err) {
        setError("Erro ao carregar detalhes do cosmético");
        console.error("Erro:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchCosmeticDetails();
  }, [externalId]);

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
      icon: "dark",
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
      <div className="container mt-4">
        <div className="d-flex justify-content-center py-5">
          <div
            className="spinner-border text-primary"
            style={{ width: "3rem", height: "3rem" }}
            role="status"
          >
            <span className="visually-hidden">Carregando...</span>
          </div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="container mt-4">
        <div className="alert alert-danger d-flex align-items-center">
          <div className="flex-grow-1">{error}</div>
          <button className="btn btn-secondary ms-3" onClick={handleBack}>
            Voltar
          </button>
        </div>
      </div>
    );
  }

  if (!cosmetic) {
    return (
      <div className="container mt-4">
        <div className="alert alert-warning text-center py-4">
          <h4>Cosmético não encontrado</h4>
          <button className="btn btn-outline-primary mt-2" onClick={handleBack}>
            ← Voltar para a loja
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="container mt-4 mb-5">
      <button className="btn btn-outline-secondary mb-4" onClick={handleBack}>
        ← Voltar
      </button>

      <div className="row">
        {/* Imagem do Cosmético - Lado Esquerdo */}
        <div className="col-lg-6 mb-4">
          <div className="card border-0 shadow-lg">
            <div
              className="d-flex justify-content-center align-items-center p-5 position-relative"
              style={{
                background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                minHeight: "300px",
              }}
            >
              <div
                style={{
                  position: "absolute",
                  top: "-50%",
                  left: "-50%",
                  width: "200%",
                  height: "200%",
                  background:
                    "radial-gradient(circle, rgba(255,255,255,0.15) 0%, rgba(255,255,255,0) 70%)",
                  transform: "rotate(30deg)",
                }}
              />
              <img
                src={cosmetic.imageUrl}
                className="position-relative z-1 img-fluid"
                style={{
                  maxHeight: "400px",
                  width: "auto",
                  filter: "drop-shadow(0 8px 16px rgba(0,0,0,0.4))",
                }}
                alt={cosmetic.name}
                onError={(e) => {
                  e.target.src =
                    "https://via.placeholder.com/300x300/ffffff/667eea?text=🎮";
                  e.target.style.filter = "none";
                }}
              />
            </div>
          </div>
        </div>

        {/* Informações do Cosmético - Lado Direito */}
        <div className="col-lg-6 mb-4">
          <div className="card border-0 shadow-lg h-100">
            <div className="card-body d-flex flex-column p-4">
              <h1 className="card-title display-6 fw-bold text-dark mb-3">
                {cosmetic.name}
              </h1>

              {cosmetic.description && (
                <p className="lead text-muted mb-4">{cosmetic.description}</p>
              )}

              {/* Badges de Informação */}
              <div className="d-flex gap-2 mb-4">
                <span
                  className={`badge bg-${getRarityColor(cosmetic.rarity)} fs-6`}
                >
                  {cosmetic.rarity}
                </span>
                <span className="badge bg-outline-secondary fs-6 text-capitalize">
                  {cosmetic.type}
                </span>
                {cosmetic.isNew && (
                  <span className="badge bg-success fs-6">Novo</span>
                )}
              </div>

              {/* Informações Detalhadas */}
              <div className="border-top pt-4 mt-auto">
                <h5 className="fw-bold mb-3">📋 Detalhes do Cosmético</h5>

                <div className="row mb-3">
                  <div className="col-6">
                    <strong>Tipo:</strong>
                    <br />
                    <span className="text-capitalize text-muted">
                      {cosmetic.type}
                    </span>
                  </div>
                  <div className="col-6">
                    <strong>Raridade:</strong>
                    <br />
                    <span className="text-muted">{cosmetic.rarity}</span>
                  </div>
                </div>

                {cosmetic.added && (
                  <div className="mb-3">
                    <strong>Adicionado em:</strong>
                    <br />
                    <span className="text-muted">
                      {formatDate(cosmetic.added)}
                    </span>
                  </div>
                )}

                {cosmetic.isNew && (
                  <div className="alert alert-success mt-3 mb-0">
                    <strong>✨ Novo Item!</strong> Este cosmético foi adicionado
                    recentemente à loja
                  </div>
                )}
              </div>

              {/* Aviso sobre compra em bundles */}
              <div className="alert alert-info mt-4">
                <strong>💡 Como obter este cosmético:</strong>
                <br />
                <small>
                  Este item está disponível em diversos bundles da loja. Navegue
                  pelos bundles para encontrar ofertas que incluam este
                  cosmético!
                </small>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
