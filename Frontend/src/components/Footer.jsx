import React from "react";

export const Footer = () => (
  <footer
    className="bg-light text-dark py-4 mt-5"
    style={{
      borderTop: "5px solid #e9ecef",
    }}
  >
    <div className="container text-center">
      <div className="row">
        <div className="col-md-4 mb-3 mb-md-0">
          <h5 className="fw-bold">Henrique Lopes</h5>
          <p className="small mb-0 text-muted">
            Desenvolvedor Júnior apresentando meu projeto.
          </p>
        </div>

        <div className="col-md-4 mb-3 mb-md-0">
          <h6 className="fw-bold">Links úteis</h6>
          <ul className="list-unstyled">
            <li>
              <a
                href="/"
                className="text-dark text-decoration-none text-muted hover-text-dark"
              >
                Início
              </a>
            </li>
            <li>
              <a
                href="/sobre"
                className="text-dark text-decoration-none text-muted hover-text-dark"
              >
                Sobre
              </a>
            </li>
            <li>
              <a
                href="/contato"
                className="text-dark text-decoration-none text-muted hover-text-dark"
              >
                Contato
              </a>
            </li>
          </ul>
        </div>

        <div className="col-md-4">
          <h6 className="fw-bold">Redes sociais</h6>
          <div>
            <a
              href="https://henriquelopes-dev.github.io/"
              className="text-dark me-3 text-muted hover-text-dark"
            >
              <i className="bi bi-globe"></i> Meu Site
            </a>
            <a
              href="https://www.linkedin.com/in/henrique-luiz-almeida-lopes/"
              className="text-dark me-3 text-muted hover-text-dark"
            >
              <i className="bi bi-linkedin"></i> LinkedIn
            </a>
            <a
              href="https://github.com/HenriqueLopes-dev/"
              className="text-dark text-muted hover-text-dark"
            >
              <i className="bi bi-github"></i> GitHub
            </a>
          </div>
        </div>
      </div>

      <hr className="border-secondary my-3" />
      <p className="small mb-0 text-muted">
        &copy; {new Date().getFullYear()} Henrique Lopes. Todos os direitos
        reservados.
      </p>
    </div>

    <style jsx>{`
      .hover-text-dark:hover {
        color: #212529 !important;
        transition: color 0.2s ease;
      }
    `}</style>
  </footer>
);
