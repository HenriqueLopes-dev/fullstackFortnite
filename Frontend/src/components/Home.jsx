import React, { useState, useEffect } from "react";
import { SearchFilters } from "./SearchFilters";
import { api } from "../api/AxiosConfig";
import { useSearchParams, useNavigate } from "react-router-dom";

export const Home = () => {
  const [cosmetics, setCosmetics] = useState({ content: [] });
  const [searchParams, setSearchParams] = useSearchParams();
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  // Parâmetros de filtro da URL
  const name = searchParams.get("name") || "";
  const type = searchParams.get("type") || "";
  const rarity = searchParams.get("rarity") || "";
  const inclusionDate = searchParams.get("inclusion-date") || "";
  const isNew = searchParams.get("new") || "";
  const shop = searchParams.get("shop") || "";
  const sale = searchParams.get("sale") || "";
  const page = parseInt(searchParams.get("page") || "0", 10);
  const pageSize = parseInt(searchParams.get("page-size") || "20", 10);

  // Verifica se está em modo bundle (shop ou sale ativos)
  const isBundleMode = shop || sale;

  async function applyFilters(filters = {}) {
    setLoading(true);
    try {
      const params = new URLSearchParams();

      // Adiciona todos os filtros aos parâmetros
      if (name) params.append("name", name);
      if (type) params.append("type", type);
      if (rarity) params.append("rarity", rarity);
      if (inclusionDate) params.append("inclusion-date", inclusionDate);
      if (isNew) params.append("new", isNew);
      if (shop) params.append("shop", shop);
      if (sale) params.append("sale", sale);

      // Parâmetros de paginação
      params.append("page", String(page));
      params.append("page-size", String(pageSize));

      const response = await api.get("/cosmetics", { params });
      setCosmetics(
        response.data && response.data.content ? response.data : { content: [] }
      );
    } catch (error) {
      console.error("Erro ao buscar cosméticos:", error);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    // Aplica filtros quando os parâmetros da URL mudam
    applyFilters({ name, type, rarity, inclusionDate, isNew, shop, sale });
  }, [name, type, rarity, inclusionDate, isNew, shop, sale, page, pageSize]);

  // Atualiza a página na URL
  const handlePageChange = (newPage) => {
    const next = new URLSearchParams(searchParams);
    next.set("page", String(newPage));
    setSearchParams(next);
  };

  // Função para cor da raridade
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

  // Função para formatar data
  const formatDate = (dateString) => {
    if (!dateString) return "-";
    const date = new Date(dateString);
    return date.toLocaleDateString("pt-BR");
  };

  // Renderiza card de cosmético individual
  const renderCosmeticCard = (cos) => (
    <div key={cos.id || cos.externalId} className="col-md-6 col-lg-4 mb-3">
      <div className="card h-100">
        <div
          className="d-flex justify-content-center align-items-center p-3 position-relative"
          style={{
            height: "160px",
            background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
            overflow: "hidden",
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
                "radial-gradient(circle, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0) 70%)",
              transform: "rotate(30deg)",
            }}
          />
          <img
            src={cos.imageUrl}
            className="position-relative z-1 img-fluid"
            style={{
              maxHeight: "400px",
              width: "auto",
              filter: "drop-shadow(0 4px 8px rgba(0,0,0,0.3))",
            }}
            alt={cos.name}
            onError={(e) => {
              e.target.src =
                "https://via.placeholder.com/80x80/ffffff/667eea?text=🎮";
              e.target.style.filter = "none";
            }}
          />
        </div>

        <div className="card-body">
          <h6 className="card-title fw-bold">{cos.name}</h6>
          {cos.description && (
            <p className="card-text small text-muted mb-2">{cos.description}</p>
          )}
          <div className="d-flex justify-content-between align-items-center mb-2">
            <span className={`badge bg-${getRarityColor(cos.rarity)}`}>
              {cos.rarity}
            </span>
            {cos.isNew && <span className="badge bg-success">Novo</span>}
          </div>
          <small className="text-muted text-capitalize d-block">
            {cos.type}
          </small>
          {cos.added && (
            <small className="text-muted">{formatDate(cos.added)}</small>
          )}
        </div>
      </div>
    </div>
  );

  // Renderiza card de bundle - Versão Minimalista
  const renderBundleCard = (bundle) => {
    const bundleName =
      bundle.bundleName ||
      (bundle.cosmetics && bundle.cosmetics[0]?.name + " Bundle") ||
      "Bundle";

    const bundleImage =
      bundle.bundleImageUrl ||
      (bundle.cosmetics && bundle.cosmetics[0]?.imageUrl) ||
      "https://via.placeholder.com/80x80/ffffff/667eea?text=🎮";

    const hasDiscount = bundle.finalPrice < bundle.regularPrice;

    const handleBundleClick = () => {
      navigate(`/bundle/${bundle.bundleId}`);
    };

    return (
      <div key={bundle.bundleId} className="col-md-6 col-lg-4 mb-3">
        <div className="card h-100">
          {/* Container da imagem igual aos cosméticos */}
          <div
            className="d-flex justify-content-center align-items-center p-3 position-relative cursor-pointer"
            style={{
              height: "300px",
              background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
              overflow: "hidden",
              cursor: "pointer",
            }}
            onClick={handleBundleClick}
          >
            <div
              style={{
                position: "absolute",
                top: "-50%",
                left: "-50%",
                width: "500%",
                height: "500%",
                background:
                  "radial-gradient(circle, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0) 70%)",
                transform: "rotate(30deg)",
              }}
            />

            <img
              src={bundleImage}
              className="position-relative z-1 img-fluid"
              style={{
                maxHeight: "1000px",
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

            {/* Badges */}
            <span className="position-absolute top-0 start-0 badge bg-dark m-2 small">
              {bundle.cosmetics?.length || 0}
            </span>
            {hasDiscount && (
              <span className="position-absolute top-0 end-0 badge bg-danger m-2 small">
                Sale
              </span>
            )}
          </div>

          <div className="card-body">
            <h6
              className="card-title fw-bold cursor-pointer mb-2"
              style={{ cursor: "pointer" }}
              onClick={handleBundleClick}
            >
              {bundleName}
            </h6>

            <div className="d-flex justify-content-between align-items-center mb-2">
              <span className={`fw-bold ${hasDiscount ? "text-success" : ""}`}>
                {bundle.finalPrice} V-Bucks
              </span>
              {hasDiscount && (
                <small className="text-decoration-line-through text-muted">
                  {bundle.regularPrice}
                </small>
              )}
            </div>

            <button
              className="btn btn-primary w-100 btn-sm"
              onClick={handleBundleClick}
            >
              Ver Detalhes
            </button>
          </div>
        </div>
      </div>
    );
  };

  // Detecta se o item é um bundle ou cosmético individual
  const isBundle = (item) => item.bundleId !== undefined;

  // Paginação inteligente - mostra apenas páginas próximas
  const renderPaginationButtons = () => {
    if (!cosmetics.page || cosmetics.page.totalPages <= 1) return null;

    const totalPages = cosmetics.page.totalPages;
    const currentPage = page;
    const buttons = [];

    // Botão Anterior
    buttons.push(
      <button
        key="prev"
        disabled={currentPage <= 0}
        onClick={() => handlePageChange(currentPage - 1)}
        className={`btn ${
          currentPage <= 0 ? "btn-outline-secondary" : "btn-primary"
        }`}
      >
        ← Anterior
      </button>
    );

    // Primeira página
    if (currentPage > 2) {
      buttons.push(
        <button
          key={0}
          onClick={() => handlePageChange(0)}
          className="btn btn-outline-primary"
        >
          1
        </button>
      );
      if (currentPage > 3) {
        buttons.push(
          <span key="ellipsis1" className="px-2">
            ...
          </span>
        );
      }
    }

    // Páginas ao redor da atual
    for (
      let i = Math.max(0, currentPage - 2);
      i <= Math.min(totalPages - 1, currentPage + 2);
      i++
    ) {
      buttons.push(
        <button
          key={i}
          onClick={() => handlePageChange(i)}
          className={`btn ${
            i === currentPage ? "btn-primary" : "btn-outline-primary"
          }`}
        >
          {i + 1}
        </button>
      );
    }

    // Última página
    if (currentPage < totalPages - 3) {
      if (currentPage < totalPages - 4) {
        buttons.push(
          <span key="ellipsis2" className="px-2">
            ...
          </span>
        );
      }
      buttons.push(
        <button
          key={totalPages - 1}
          onClick={() => handlePageChange(totalPages - 1)}
          className="btn btn-outline-primary"
        >
          {totalPages}
        </button>
      );
    }

    // Botão Próxima
    buttons.push(
      <button
        key="next"
        disabled={currentPage >= totalPages - 1}
        onClick={() => handlePageChange(currentPage + 1)}
        className={`btn ${
          currentPage >= totalPages - 1
            ? "btn-outline-secondary"
            : "btn-primary"
        }`}
      >
        Próxima →
      </button>
    );

    return buttons;
  };

  return (
    <div className="container mt-4">
      <h2 className="mb-4">
        {isBundleMode ? "Bundles na Loja" : "Catálogo de Cosméticos"}
      </h2>

      <SearchFilters
        onApply={(filters) => {
          const next = new URLSearchParams();
          Object.entries(filters).forEach(([key, value]) => {
            if (value) next.set(key, value);
          });
          next.set("page", "0"); // Reset para primeira página
          setSearchParams(next);
        }}
      />

      {/* Resultados */}
      <div className="mt-4">
        {loading ? (
          <div className="d-flex justify-content-center">
            <div className="spinner-border text-primary" role="status">
              <span className="visually-hidden">Carregando...</span>
            </div>
          </div>
        ) : cosmetics.content.length === 0 ? (
          <p className="text-muted text-center">
            {isBundleMode
              ? "Nenhum bundle encontrado."
              : "Nenhum cosmético encontrado."}
          </p>
        ) : (
          <>
            {/* Informações da página */}
            <div className="row mb-3">
              <div className="col">
                <small className="text-muted">
                  Mostrando {cosmetics.content.length} de{" "}
                  {cosmetics.page?.totalElements || 0}{" "}
                  {isBundleMode ? "bundles" : "itens"}
                  {cosmetics.page && (
                    <>
                      {" "}
                      - Página {page + 1} de {cosmetics.page.totalPages}
                    </>
                  )}
                </small>
              </div>
            </div>

            {/* Grid dinâmico - bundles ou cosméticos */}
            <div className="row">
              {cosmetics.content.map((item) =>
                isBundle(item)
                  ? renderBundleCard(item)
                  : renderCosmeticCard(item)
              )}
            </div>

            {/* Paginação com Bootstrap - Versão inteligente */}
            {cosmetics.page && cosmetics.page.totalPages > 1 && (
              <div className="d-flex justify-content-center align-items-center gap-2 mt-4 flex-wrap">
                {renderPaginationButtons()}
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
};
