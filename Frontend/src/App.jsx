import { useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import { NavScroll } from "./components/NavScroll";
import { Footer } from "./components/Footer";
import { Login } from "./components/Login";
import { Home } from "./components/Home";
import { BundleDetails } from "./components/BundleDetails";
import { Routes, Route } from "react-router-dom";
import { Register } from "./components/Register";
import { CosmeticDetails } from "./components/Cosmetic";
import { Profile } from "./components/Profile";
import { UsersList } from "./components/UsersList";
import { UserDetail } from "./components/UserDetail";
import { BundleHistoryDetails } from "./components/BundleHistoryDetails";
import { PurchaseHistory } from "./components/PurchaseHistory";

function App() {
  return (
    <div className="d-flex flex-column min-vh-100">
      <NavScroll />
      <main className="flex-grow-1">
        <Routes>
          <Route path="" element={<Home />} />
          <Route path="login" element={<Login />} />
          <Route path="register" element={<Register />} />
          <Route path="bundle/:bundleId" element={<BundleDetails />} />
          <Route path="cosmetic/:externalId" element={<CosmeticDetails />} />
          <Route path="user/:id" element={<UserDetail />} />
          <Route path="users" element={<UsersList />} />
          <Route path="profile" element={<Profile />} />
          <Route
            path="bundle-history/:bundleId"
            element={<BundleHistoryDetails />}
          />
          <Route path="/purchase-history" element={<PurchaseHistory />} />
        </Routes>
      </main>
      <Footer />
    </div>
  );
}

export default App;
