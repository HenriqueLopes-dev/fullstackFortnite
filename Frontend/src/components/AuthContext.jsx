import { createContext, useContext, useEffect, useState } from "react";
import api from "../api/AxiosConfig";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    checkAuth();
  }, []);

  const checkAuth = async () => {
    try {
      const response = await api.get("/auth/me", { withCredentials: true });
      setUser(response.data);
    } catch (error) {
      setUser(null);
    } finally {
      setLoading(false);
    }
  };

  const updateUser = (userData) => {
    setUser((prevUser) => ({
      ...prevUser,
      ...userData,
    }));
  };

  const purchaseItem = (itemId, price, itemType = "bundle") => {
    if (!user) {
      alert("Você precisa estar logado para comprar!");
      return;
    }
    alert(`Compra realizada! ${price} V-Bucks debitados.`);
  };

  const value = {
    user,
    setUser,
    updateUser,
    loading,
    purchaseItem,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => useContext(AuthContext);
