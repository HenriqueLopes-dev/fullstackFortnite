import React from "react";
import { useNavigate } from "react-router-dom";

export const BundleHistoryDetails = () => {
  const navigate = useNavigate();

  const bundle = history.state?.bundle;

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

  if (!bundle) {
    return (
      <div className="container mt-4">
        <div className="alert alert-warning text-center py-4">
          <h4>Bundle não encontrado</h4>
          <button className="btn btn-outline-primary mt-2" onClick={handleBack}>
            ← Voltar
          </button>
        </div>
      </div>
    );
  }

  const bundleName = bundle.bundleName || "Bundle";
  const bundleImage = bundle.bundleImage || bundle.cosmetics?.[0]?.imageUrl;

  return (
    <div className="container mt-4">
      <button className="btn btn-outline-secondary mb-4" onClick={handleBack}>
        ← Voltar
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

            {/* Informações de compra */}
            <div className="card-footer bg-transparent border-0 pt-0">
              <div className="d-flex justify-content-between align-items-center">
                <span className="badge bg-success fs-6 px-3 py-2">
                  Comprado por {bundle.price} V-Bucks
                </span>
                <small className="text-muted">
                  Em {formatDate(bundle.createdAt)}
                </small>
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

              {/* Status de compra */}
              <div className="mb-4">
                <div className="alert alert-success">
                  <i className="fas fa-check-circle me-2"></i>
                  <strong>Bundle adquirido com sucesso!</strong>
                </div>
              </div>

              {/* Informações Adicionais */}
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
                Todos os itens que você adquiriu neste bundle
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
    </div>
  );
};
