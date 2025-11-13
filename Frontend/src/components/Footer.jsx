import React from "react";

export const Footer = () => (
  <footer className="bg-dark text-light py-4 mt-auto">
    <div className="container text-center mb-0">
      <div className="row">
        <div className="col-md-4 mb-3 mb-md-0">
          <h5>Minha Empresa</h5>
          <p className="small">Soluções inteligentes para o seu negócio.</p>
        </div>

        <div className="col-md-4 mb-3 mb-md-0">
          <h6>Links úteis</h6>
          <ul className="list-unstyled">
            <li>
              <a href="/" className="text-light text-decoration-none">
                Início
              </a>
            </li>
            <li>
              <a href="/sobre" className="text-light text-decoration-none">
                Sobre
              </a>
            </li>
            <li>
              <a href="/contato" className="text-light text-decoration-none">
                Contato
              </a>
            </li>
          </ul>
        </div>

        <div className="col-md-4">
          <h6>Redes sociais</h6>
          <div>
            <a href="#" className="text-light me-3">
              <i className="bi bi-facebook"></i>
            </a>
            <a href="#" className="text-light me-3">
              <i className="bi bi-instagram"></i>
            </a>
            <a href="#" className="text-light">
              <i className="bi bi-github"></i>
            </a>
          </div>
        </div>
      </div>

      <hr className="border-secondary my-3" />
      <p className="small mb-0">
        &copy; {new Date().getFullYear()} Minha Empresa. Todos os direitos
        reservados.
      </p>
    </div>
  </footer>
);
