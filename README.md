# Sistema de Gestão de Portfólio de Projetos

API REST para gerenciamento de portfólio de projetos desenvolvida com Spring Boot 3.5.7, incluindo controle de membros, status, orçamentos e classificação de risco automatizada.

## Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Funcionalidades](#funcionalidades)
- [Arquitetura](#arquitetura)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Configuração](#configuração)
- [Executando a Aplicação](#executando-a-aplicação)
- [Executando os Testes](#executando-os-testes)
- [Documentação da API](#documentação-da-api)
- [Endpoints](#endpoints)
- [Regras de Negócio](#regras-de-negócio)
- [Segurança](#segurança)
- [Cobertura de Testes](#cobertura-de-testes)
- [Melhorias Futuras](#melhorias-futuras)
- [Autor](#autor)
- [Licença](#licença)

## Sobre o Projeto

Sistema backend desenvolvido para gerenciar portfólios de projetos empresariais, permitindo controle completo sobre projetos, membros de equipe, classificação de riscos e transições de status através de máquina de estados.

O projeto implementa princípios de Clean Architecture, segregando responsabilidades em camadas (Controller, Service, Repository), utilizando DTOs para comunicação externa e tratamento global de exceções.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.5.7
- Spring Data JPA
- Spring Security (Basic Authentication)
- PostgreSQL 16
- H2 Database (ambiente de testes)
- Lombok
- Swagger/OpenAPI 3
- JaCoCo (análise de cobertura)
- JUnit 5
- Mockito
- Maven 3.8+

## Funcionalidades

### Gestão de Projetos

- CRUD completo de projetos
- Cálculo automático de classificação de risco baseado em orçamento e duração
- Máquina de estados para controle de status do projeto
- Validação de transições sequenciais de status
- Restrições de exclusão baseadas em status
- Gerenciamento de alocação de membros (1 a 10 membros por projeto)
- Paginação de resultados
- Geração de relatório consolidado do portfólio

### Gestão de Membros

- CRUD completo de membros da equipe
- Tipos de atribuição: FUNCIONARIO, TERCERIZADO, ACIONISTA
- Validação de limite de 3 projetos ativos simultâneos por membro
- Integração com API externa para busca de membros

### Recursos Adicionais

- Autenticação via Basic Auth
- Documentação interativa com Swagger UI
- Tratamento global de exceções
- Validação de entrada com Bean Validation

## Arquitetura

src/main/java/com/jtarcio/portfolioapi/
├── config/
│ └── SecurityConfig # Configuração de segurança Spring Security
├── controller/
│ └── mock/
│ └── MembroMockController # Controller mock para API externa
│ ├── MembroController # Endpoints REST de membros
│ └── ProjetoController # Endpoints REST de projetos
├── dto/
│ ├── request/
│ │ ├── MembroRequestDTO # DTO de requisição de membro
│ │ └── ProjetoRequestDTO # DTO de requisição de projeto
│ └── response/
│ ├── MembroResponseDTO # DTO de resposta de membro
│ └── ProjetoResponseDTO # DTO de resposta de projeto
├── exception/
│ ├── ErrorResponse # Objeto de resposta de erro
│ ├── GlobalExceptionHandler # Handler global de exceções
│ └── PortfolioException # Exceção customizada do domínio
├── mapper/
│ ├── MembroMapper # Conversor Membro ↔ DTO
│ └── ProjetoMapper # Conversor Projeto ↔ DTO
├── model/
│ └── entity/
│ ├── enums/
│ │ ├── AtribuicaoEnum # Enum de tipos de atribuição
│ │ ├── ClassificacaoRiscoEnum # Enum de classificação de risco
│ │ └── StatusProjetoEnum # Enum de status do projeto
│ ├── Membro # Entidade JPA Membro
│ └── Projeto # Entidade JPA Projeto
├── repository/
│ ├── MembroRepository # Repository JPA de Membro
│ └── ProjetoRepository # Repository JPA de Projeto
├── service/
│ ├── MembroService # Regras de negócio de Membro
│ └── ProjetoService # Regras de negócio de Projeto
└── PortfolioApiApplication # Classe principal Spring Boot

src/main/resources/
├── static/ # Recursos estáticos
├── templates/ # Templates (se houver)
└── application.yml # Configurações da aplicação

src/test/java/com/jtarcio/portfolioapi/
├── controller/
│ ├── MembroControllerTest # Testes de integração do MembroController
│ └── ProjetoControllerTest # Testes de integração do ProjetoController
├── service/
│ ├── MembroServiceTest # Testes unitários do MembroService
│ └── ProjetoServiceTest # Testes unitários do ProjetoService
└── PortfolioApiApplicationTests # Teste de contexto da aplicação

text

## Pré-requisitos

- Java Development Kit (JDK) 21 ou superior
- PostgreSQL 16 ou superior
- Maven 3.8 ou superior
- Git

## Instalação

### 1. Clone o repositório

git clone https://github.com/josivantarcio/api-portfolio.git
cd api-portfolio

text

### 2. Configure o banco de dados

Crie o banco de dados no PostgreSQL:

CREATE DATABASE db_portfolio;

text

## Configuração

Edite o arquivo `src/main/resources/application.yml`:

spring:
datasource:
url: jdbc:postgresql://localhost:5432/db_portfolio
username: seu_usuario
password: sua_senha

jpa:
hibernate:
ddl-auto: update
show-sql: true

text

## Executando a Aplicação

### Utilizando Maven Wrapper

./mvnw spring-boot:run

text

### Gerando e executando o JAR

./mvnw clean package
java -jar target/portfolioApi-0.0.1-SNAPSHOT.jar

text

A aplicação estará disponível em `http://localhost:8080`

## Executando os Testes

### Executar suite completa de testes

./mvnw test

text

### Gerar relatório de cobertura JaCoCo

./mvnw jacoco:report

text

O relatório será gerado em `target/site/jacoco/index.html`

## Documentação da API

A documentação interativa da API está disponível através do Swagger UI:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

Utilize as credenciais de autenticação para testar os endpoints:
- Username: `admin`
- Password: `admin123`

## Endpoints

### Projetos

| Método HTTP | Endpoint | Descrição |
|-------------|----------|-----------|
| GET | `/api/projetos` | Lista todos os projetos (paginado) |
| GET | `/api/projetos/all` | Lista todos os projetos sem paginação |
| GET | `/api/projetos/{id}` | Busca projeto por ID |
| POST | `/api/projetos` | Cria novo projeto |
| PUT | `/api/projetos/{id}` | Atualiza projeto existente |
| DELETE | `/api/projetos/{id}` | Remove projeto (com validação de status) |
| PATCH | `/api/projetos/{id}/status` | Altera status do projeto |
| PATCH | `/api/projetos/{id}/avancar-status` | Avança para próximo status |
| POST | `/api/projetos/{id}/membros/{membroId}` | Adiciona membro ao projeto |
| DELETE | `/api/projetos/{id}/membros/{membroId}` | Remove membro do projeto |
| GET | `/api/projetos/relatorio` | Gera relatório consolidado do portfólio |

### Membros

| Método HTTP | Endpoint | Descrição |
|-------------|----------|-----------|
| GET | `/api/membros` | Lista todos os membros |
| GET | `/api/membros/{id}` | Busca membro por ID |
| POST | `/api/membros` | Cria novo membro |
| PUT | `/api/membros/{id}` | Atualiza membro existente |
| DELETE | `/api/membros/{id}` | Remove membro |
| GET | `/api/membros/mock` | Busca membros da API externa (mock) |

## Regras de Negócio

### Classificação Automática de Risco

A classificação de risco é calculada automaticamente com base no orçamento total e duração do projeto:

| Classificação | Critério |
|---------------|----------|
| BAIXO_RISCO | Orçamento ≤ R$ 100.000,00 E duração ≤ 3 meses |
| ALTO_RISCO | Orçamento > R$ 500.000,00 OU duração > 6 meses |
| MEDIO_RISCO | Casos intermediários |

### Máquina de Estados

Os projetos seguem transições sequenciais obrigatórias:

EM_ANALISE → ANALISE_REALIZADA → ANALISE_APROVADA →
INICIADO → PLANEJADO → EM_ANDAMENTO → ENCERRADO
└→ CANCELADO

text

### Restrições de Exclusão

Projetos nos seguintes status não podem ser excluídos:
- INICIADO
- EM_ANDAMENTO
- ENCERRADO

### Alocação de Membros

- Mínimo de 1 membro por projeto
- Máximo de 10 membros por projeto
- Cada membro pode estar alocado em no máximo 3 projetos ativos simultaneamente
- O gerente responsável deve estar entre os membros do projeto

## Segurança

A API utiliza Spring Security com autenticação Basic Auth. Todas as requisições devem incluir credenciais:

**Credenciais padrão:**
- Username: `admin`
- Password: `admin123`

**Exemplo de requisição com autenticação:**

curl -X GET http://localhost:8080/api/projetos
-u admin:admin123
-H "Content-Type: application/json"

text

## Cobertura de Testes

A aplicação possui 35 testes automatizados:

- Testes de unidade (Services): 21 testes
- Testes de integração (Controllers): 13 testes
- Teste de inicialização (Application): 1 teste

**Cobertura atual:** 42% de cobertura de código

**Distribuição por pacote:**
- controller: 83%
- service: 39%
- model.entity.enums: 68%
- config: 100%

## Melhorias Futuras

- Implementar autenticação JWT
- Adicionar cache com Redis
- Implementar auditoria de alterações
- Criar endpoints de relatórios avançados
- Adicionar testes de carga
- Implementar rate limiting
- Dockerizar a aplicação
- Configurar CI/CD pipeline
- Aumentar cobertura de testes para 70%+

## Autor

**Josevan Oliveira**
- GitHub: [@josivantarcio](https://github.com/josivantarcio)
- LinkedIn: [josevanoliveira](https://www.linkedin.com/in/josevanoliveira/)

## Licença

Este projeto foi desenvolvido como parte de um desafio técnico para avaliação de competências em desenvolvimento backend com Spring Boot.
