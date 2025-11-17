import React, { useState } from "react";

export const SearchFilters = ({ onApply }) => {
  const [filters, setFilters] = useState({
    name: "",
    type: "",
    rarity: "",
    "inclusion-date": "",
    new: false,
    shop: false,
    sale: false,
  });

  function updateFilter(key, value) {
    setFilters((prev) => ({ ...prev, [key]: value }));
  }

  function resetFilters() {
    const reset = {
      name: "",
      type: "",
      rarity: "",
      "inclusion-date": "",
      new: false,
      shop: false,
      sale: false,
    };
    setFilters(reset);
    onApply?.(reset);
  }

  function handleApply() {
    const filtersToApply = {
      ...filters,
      new: filters["new"] ? "true" : "",
      shop: filters["shop"] ? "true" : "",
      sale: filters["sale"] ? "true" : "",
    };
    onApply?.(filtersToApply);
  }

  return (
    <div className="card p-4 shadow-sm mb-4">
      <h5 className="mb-3">Filtros de Cosméticos</h5>

      <div className="row g-3">
        <div className="col-md-4">
          <label className="form-label">Nome</label>
          <input
            type="text"
            className="form-control"
            placeholder="Digite o nome..."
            value={filters.name}
            onChange={(e) => updateFilter("name", e.target.value)}
          />
        </div>

        <div className="col-md-4">
          <label className="form-label">Tipo</label>
          <select
            className="form-select"
            value={filters.type}
            onChange={(e) => updateFilter("type", e.target.value)}
          >
            <option value="">Todos</option>
            <option value="outfit">Outfit</option>
            <option value="backpack">Backpack</option>
            <option value="pickaxe">Pickaxe</option>
            <option value="emote">Emote</option>
            <option value="glider">Glider</option>
            <option value="wrap">Wrap</option>
          </select>
        </div>

        <div className="col-md-4">
          <label className="form-label">Raridade</label>
          <select
            className="form-select"
            value={filters.rarity}
            onChange={(e) => updateFilter("rarity", e.target.value)}
          >
            <option value="">Todas</option>
            <option value="common">Common</option>
            <option value="uncommon">Uncommon</option>
            <option value="rare">Rare</option>
            <option value="epic">Epic</option>
            <option value="legendary">Legendary</option>
          </select>
        </div>

        <div className="col-md-6">
          <label className="form-label">Data de Inclusão</label>
          <input
            type="date"
            className="form-control"
            value={filters["inclusion-date"]}
            onChange={(e) => updateFilter("inclusion-date", e.target.value)}
          />
        </div>

        <div className="col-12 mt-3">
          <div className="form-check mb-2 ml-2 text-start">
            <input
              type="checkbox"
              className="form-check-input"
              id="onlyNew"
              checked={filters["new"]}
              onChange={(e) => updateFilter("new", e.target.checked)}
            />
            <label htmlFor="onlyNew" className="form-check-label">
              Apenas cosméticos novos
            </label>
          </div>

          <div className="form-check mb-2 ml-2 text-start">
            <input
              type="checkbox"
              className="form-check-input"
              id="onlyOnSale"
              checked={filters["shop"]}
              onChange={(e) => updateFilter("shop", e.target.checked)}
            />
            <label htmlFor="onlyOnSale" className="form-check-label">
              Apenas cosméticos à venda
            </label>
          </div>

          <div className="form-check text-start ml-2">
            <input
              type="checkbox"
              className="form-check-input"
              id="onlyDiscounted"
              checked={filters["sale"]}
              onChange={(e) => updateFilter("sale", e.target.checked)}
            />
            <label htmlFor="onlyDiscounted" className="form-check-label">
              Apenas cosméticos em promoção
            </label>
          </div>
        </div>

        <div className="col-12 mt-3 d-flex gap-2">
          <button className="btn btn-primary" onClick={handleApply}>
            Aplicar Filtros
          </button>

          <button className="btn btn-secondary" onClick={resetFilters}>
            Limpar
          </button>
        </div>
      </div>
    </div>
  );
};
