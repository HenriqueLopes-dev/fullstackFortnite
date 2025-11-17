# Fortnite Shop API

Uma API RESTful para simulação de uma loja de itens do Fortnite, desenvolvida em Java Spring Boot com frontend React.

## Sobre o Projeto

Esta aplicação simula uma loja de cosméticos do Fortnite, permitindo:

- **Catálogo de Cosméticos**: Listagem de skins, emotes, picaretas e outros itens
- **Sistema de Compras**: Aquisição de itens usando V-Bucks (moeda virtual)
- **Sistema de Buscas**: Buscar por itens de diferentes categorias, pesquisa por nome, apenas na promoção, entre outros
- **Gestão de Usuários**: Perfis, histórico de compras e saldo
- **Sistema de Reembolso**: Possibilidade de reembolsar bundles adquiridos
- **Autenticação JWT**: Sistema seguro de login e registro

## Tecnologias Utilizadas

### Backend

- **Java 17** + Spring Boot
- **Spring Security** + JWT
- **PostgreSQL** - Banco de dados
- **JPA/Hibernate** - ORM
- **Maven** - Gerenciamento de dependências
- **Docker** - Containerização

### Frontend

- **React** + JavaScript
- **React Router** - Navegação
- **Bootstrap 5** - UI Framework
- **Axios** - Cliente HTTP
- **React Bootstrap** - Componentes

## 🛠 Como Executar o Projeto

### Pré-requisitos

- Docker e Docker Compose instalados
- Git

### Execução com Docker (Recomendado)

1. **Clone o repositório**

```bash
git clone https://github.com/HenriqueLopes-dev/fullstackFortnite.git
# Ou
git clone git@github.com:HenriqueLopes-dev/fullstackFortnite.git
cd fullstackFortnite
```

2. **Execute o Docker Compose**

```bash
docker compose up -d
```

3. **Aguarde os containers subirem (por conta da sincronização com a api do fortnite demora de 3 a 5 minutos para o backend comecar, no terminal do backend no docker é possível observar)**
   - Backend: http://localhost:8080
   - Adminer (Banco de dados): http://localhost:8081

#### Frontend

```bash
cd Frontend
npm install
npm run dev
```

## Acessos e Credenciais

### URLs da Aplicação

- **Frontend**: http://localhost:5173
- **Backend API**: http://localhost:8080
- **Adminer (DB)**: http://localhost:8081

### Credenciais do Banco de Dados

```yaml
Host: localhost:5432
Database: fortnite
Username: admin
Password: admin
```

## Estrutura do Banco de Dados

Principais entidades:

- **Users**: Usuários do sistema
- **Cosmetics**: Itens da loja (skins, emotes, etc.)
- **Bundles**: Pacotes de cosméticos
- **PurchaseHistory**: Histórico de compras
- **AcquiredCosmetics**: Itens adquiridos pelos usuários

## Endpoints Principais da API

### Autenticação

- `POST /auth/register` - Registrar usuário
- `POST /auth/login` - Fazer login
- `GET /auth/me` - Obter dados do usuário logado

### Cosméticos

- `GET /cosmetics` - Listar cosméticos (com filtros e paginação)
- `GET /cosmetics/{id}` - Detalhes do cosmético
- `GET /cosmetics/{id}/bundle-info` - Informações do bundle

### Usuários

- `GET /users` - Listar usuários (paginado)
- `PUT /users/{id}` - Atualizar perfil
- `GET /users/{id}/purchase-history` - Histórico de compras
- `POST /users/me/acquired-cosmetics/{id}/refund` - Reembolsar bundle

### Compras

- `POST /cosmetics/{id}/purchase` - Comprar item

## Funcionalidades Destacadas

### Para Usuários

- Navegar pelo catálogo de cosméticos
- Comprar itens individuais ou bundles
- Gerenciar saldo de V-Bucks
- Ver histórico de compras
- Reembolsar bundles (quando disponível)
- Editar perfil e informações

### Para Desenvolvedores

- Sistema de busca e filtros
- Paginação em todas as listagens
- Autenticação JWT segura
- Fácil deploy com Docker
- API documentada e consistente

## Solução de Problemas

### Problemas Comuns

1. **Container não sobe**

   - Verifique se as portas 8080, 8081 e 5432 estão livres
   - Execute `docker compose logs` para ver os erros

2. **Erro de conexão com banco**
   - Aguarde o PostgreSQL inicializar completamente
   - Verifique se as credenciais estão corretas

## Desenvolvedor

**Henrique Lopes** - Desenvolvedor Júnior/Estagiário
