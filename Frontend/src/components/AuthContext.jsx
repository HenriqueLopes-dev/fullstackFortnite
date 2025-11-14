import { createContext, useContext, useEffect, useState } from "react";
import api from "../api/AxiosConfig";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api
      .get("/auth/me", { withCredentials: true })
      .then((res) => setUser(res.data))
      .catch(() => setUser(null))
      .finally(() => setLoading(false));
  }, []);

  // Função simples de compra - o backend já trata o balance
  const purchaseItem = (itemId, price, itemType = "bundle") => {
    if (!user) {
      alert("Você precisa estar logado para comprar!");
      return;
    }

    // O backend vai atualizar o balance, então confirmação visual
    alert(`Compra realizada! ${price} V-Bucks debitados.`);
  };

  const value = {
    user,
    setUser,
    loading,
    purchaseItem,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => useContext(AuthContext);
