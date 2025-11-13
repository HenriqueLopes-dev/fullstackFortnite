import { useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import { NavScroll } from "./components/NavScroll";
import { Footer } from "./components/Footer";

function App() {
  const [count, setCount] = useState(0);

  return (
    <div className="d-flex flex-column min-vh-100">
      <NavScroll />
      <main className="flex-grow-1">
        <p>sadinhasjfabsuias</p>
      </main>

      <Footer />
    </div>
  );
}

export default App;
