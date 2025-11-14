import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { api } from "../api/AxiosConfig";
import { useAuth } from "./AuthContext";

export const BundleDetails = () => {
  const { bundleId } = useParams();
  const navigate = useNavigate();
  const { user, purchaseItem } = useAuth();
  const [bundle, setBundle] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchBundleDetails = async () => {
      try {
        setLoading(true);
        const response = await api.get(`/cosmetics/${bundleId}/bundle-info`);
        setBundle(response.data);
      } catch (err) {
        setError("Erro ao carregar detalhes do bundle");
        console.error("Erro:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchBundleDetails();
  }, [bundleId]);

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
    navigate("/");
  };

  const formatDate = (dateString) => {
    if (!dateString) return "-";
    const date = new Date(dateString);
    return date.toLocaleDateString("pt-BR");
  };

  const handleCosmeticClick = (cosmeticId) => {
    navigate(`/cosmetic/${cosmeticId}`);
  };

  const handlePurchase = () => {
    if (!bundle) return;
    api.post(`cosmetics/${bundle.id}/purchase`);
    window.location.reload(false);
  };

  const hasDiscount = bundle?.finalPrice < bundle?.regularPrice;
  const discountPercent = hasDiscount
    ? Math.round((1 - bundle.finalPrice / bundle.regularPrice) * 100)
    : 0;

  // Verifica se o usuário pode comprar
  const canAfford = user ? user.balance >= (bundle?.finalPrice || 0) : false;

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

  if (!bundle) {
    return (
      <div className="container mt-4">
        <div className="alert alert-warning text-center py-4">
          <h4>Bundle não encontrado</h4>
          <button className="btn btn-outline-primary mt-2" onClick={handleBack}>
            ← Voltar para a loja
          </button>
        </div>
      </div>
    );
  }

  const bundleName =
    bundle.name || bundle.cosmetics?.[0]?.name + " Bundle" || "Bundle";
  const bundleImage = bundle.imageUrl;
  return (
    <div className="container mt-4">
      <button className="btn btn-outline-secondary mb-4" onClick={handleBack}>
        ← Voltar para a Loja
      </button>

      <div className="row">
        {/* Imagem do Bundle - Lado Esquerdo */}
        <div className="col-lg-6 mb-4">
          <div className="card border-0 shadow-lg">
            <div
              className="d-flex justify-content-center align-items-center p-5 position-relative"
              style={{
                background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
              }}
            >
              <div />
              <div className="position-relative" style={{ overflow: "hidden" }}>
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
            </div>

            {/* Badges na parte inferior da imagem */}
            <div className="card-footer bg-transparent border-0 pt-0">
              <div className="d-flex justify-content-between align-items-center">
                {hasDiscount && (
                  <span className="badge bg-danger fs-6 px-3 py-2">
                    -{discountPercent}% OFF
                  </span>
                )}
              </div>
            </div>
          </div>
        </div>

        {/* Informações do Bundle - Lado Direito */}
        <div className="col-lg-6 mb-4">
          <div className="card border-0 shadow-lg h-100">
            <div className="card-body d-flex flex-column p-4">
              <h1 className="card-title display-6 fw-bold text-dark mb-3">
                {bundleName}
              </h1>

              {/* Preços Destaque */}
              <div className="mb-4">
                {hasDiscount ? (
                  <div className="d-flex align-items-center flex-wrap">
                    <span className="text-decoration-line-through text-muted fs-4 me-3">
                      {bundle.regularPrice} V-Bucks
                    </span>
                    <span className="fw-bold text-success display-6 me-3">
                      {bundle.finalPrice} V-Bucks
                    </span>
                  </div>
                ) : (
                  <span className="fw-bold display-6 text-dark">
                    {bundle.finalPrice} V-Bucks
                  </span>
                )}
              </div>

              {/* Botão de Compra Principal */}
              <button
                className={`btn ${
                  canAfford ? "btn-primary" : "btn-secondary"
                } btn-lg w-100 py-3 mb-4 fw-bold fs-5`}
                onClick={handlePurchase}
                disabled={!user || !canAfford}
                style={{
                  cursor: user && canAfford ? "pointer" : "not-allowed",
                }}
              >
                {!user
                  ? "Faça login para comprar"
                  : canAfford
                  ? `Comprar`
                  : "V-Bucks Insuficientes"}
              </button>

              {/* Informações Adicionais */}
              <div className="border-top pt-4 mt-auto">
                <h5 className="fw-bold mb-3">Sobre este Bundle</h5>

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
                        {hasDiscount ? discountPercent + "%" : "-"}
                      </div>
                      <small className="text-muted">Desconto</small>
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
            </div>
          </div>
        </div>
      </div>

      {/* Lista de Cosméticos do Bundle */}
      <div className="row mt-2">
        <div className="col-12">
          <div className="card border-0 shadow-lg">
            <div className="card-header bg-transparent border-0 py-4">
              <h2 className="mb-0 fw-bold">Itens do Bundle</h2>
              <p className="text-muted mb-0 mt-2">
                Clique em qualquer item para ver detalhes completos
              </p>
            </div>
            <div className="card-body p-4">
              <div className="row g-4">
                {bundle.cosmetics?.map((cosmetic) => (
                  <div key={cosmetic.externalId} className="col-xl-4 col-lg-6">
                    <div
                      className="card h-100 border-0 shadow-sm hover-shadow"
                      style={{
                        cursor: "pointer",
                        transition: "all 0.3s ease",
                      }}
                      onClick={() => handleCosmeticClick(cosmetic.externalId)}
                      onMouseEnter={(e) => {
                        e.currentTarget.style.transform = "translateY(-5px)";
                      }}
                      onMouseLeave={(e) => {
                        e.currentTarget.style.transform = "translateY(0)";
                      }}
                    >
                      <div className="card-body p-0">
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
                              <span
                                className={`badge bg-${getRarityColor(
                                  cosmetic.rarity
                                )}`}
                              >
                                {cosmetic.rarity}
                              </span>
                              <div className="d-flex gap-1">
                                {cosmetic.isNew && (
                                  <span className="badge bg-success small">
                                    Novo
                                  </span>
                                )}
                                <span className="badge bg-outline-secondary small text-capitalize">
                                  {cosmetic.type}
                                </span>
                              </div>
                            </div>

                            {cosmetic.added && (
                              <small className="text-muted d-block mt-2">
                                Adicionado: {formatDate(cosmetic.added)}
                              </small>
                            )}
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Call to Action Final */}
      <div className="row mt-5">
        <div className="col-12">
          <div
            className="card border-0 bg-gradient text-white"
            style={{
              background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
            }}
          ></div>
        </div>
      </div>
    </div>
  );
};
