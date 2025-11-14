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
        </Routes>
      </main>
      <Footer />
    </div>
  );
}

export default App;
