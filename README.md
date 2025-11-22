# 🎴 Card Shop - Complete CI/CD Pipeline

[![CI/CD Pipeline](https://github.com/seu-usuario/card-shop/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/seu-usuario/card-shop/actions/workflows/ci-cd.yml)
[![Security](https://img.shields.io/badge/security-SAST%2FDAST-green)](https://github.com/seu-usuario/card-shop/security)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.1-brightgreen)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue)](https://www.docker.com/)
[![Kubernetes](https://img.shields.io/badge/Kubernetes-Ready-326CE5)](https://kubernetes.io/)

Sistema de gerenciamento de cards com pipeline CI/CD completo, incluindo build automatizado, testes, análise de segurança (SAST/DAST), deploy automatizado e gerenciamento de artefatos.

> 🎯 **Projeto Profissional**: PostgreSQL persistente, estrutura organizada, docker-compose para dev local, secrets simplificados.
> 📖 **Guia Rápido**: Veja [SETUP.md](SETUP.md) | **Mudanças**: Veja [CHANGELOG.md](CHANGELOG.md)

## 🚀 Quick Start

### Com Docker Compose (Recomendado)

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/card-shop.git
cd card-shop

# Inicie a aplicação e o banco de dados
docker-compose up -d

# Acesse a aplicação
open http://localhost:8080

# Ver logs
docker-compose logs -f app
```

### Localmente (requer PostgreSQL)

```bash
# Certifique-se de ter PostgreSQL rodando
# Configure as variáveis no .env

# Execute a aplicação
./mvnw spring-boot:run

# Acesse a aplicação
open http://localhost:8080
```

## 📁 Estrutura do Projeto

```
card-shop/
├── 📚 docs/                    # Documentação completa
│   ├── IMPLEMENTATION_REPORT.md
│   ├── PIPELINE_README.md
│   ├── SECRETS_GUIDE.md
│   ├── TUTORIAL.md
│   ├── COMMANDS.md
│   └── SUMMARY.md
├── 🐳 docker/                  # Arquivos Docker
│   ├── Dockerfile
│   └── .dockerignore
├── ☸️ k8s/                     # Kubernetes manifests
│   └── k8s-deployment.yaml
├── 📦 src/                     # Código fonte
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
├── 🚀 docker-compose.yml       # Dev local (app + PostgreSQL)
├── 📖 SETUP.md                 # Guia rápido de configuração
├── 📋 CHANGELOG.md             # Log de mudanças
├── 🔧 .env.example             # Template de variáveis
└── 📄 pom.xml                  # Maven configuration
```

## 📚 Documentação Completa

Toda documentação está organizada na pasta `docs/`:

| Documento | Descrição |
|-----------|-----------|
| **[docs/IMPLEMENTATION_REPORT.md](docs/IMPLEMENTATION_REPORT.md)** | ✅ **COMECE AQUI** - Relatório completo da implementação |
| [docs/PIPELINE_README.md](docs/PIPELINE_README.md) | Documentação técnica do pipeline CI/CD |
| [docs/SECRETS_GUIDE.md](docs/SECRETS_GUIDE.md) | Guia de configuração de secrets e variáveis |
| [docs/TUTORIAL.md](docs/TUTORIAL.md) | Tutorial prático com exemplos de uso |
| [docs/COMMANDS.md](docs/COMMANDS.md) | Referência rápida de comandos |
| [.env.example](.env.example) | Template de variáveis de ambiente |

## 🎯 Funcionalidades do Pipeline

### ✅ Build Automatizado
- Maven com cache de dependências
- Build incremental otimizado
- Testes unitários separados dos E2E

### ✅ Testes Automatizados
- **Testes Unitários**: JUnit + Mockito (no build)
- **Testes E2E**: Selenium WebDriver (pós-deploy em staging)
- **Service Containers**: PostgreSQL + App isolados

### ✅ Análise de Segurança SAST (Obrigatório)
- **OWASP Dependency Check**: Vulnerabilidades em dependências Maven
- **CodeQL**: Análise semântica de código (SQL injection, XSS, etc)
- **Execução**: Paralela após build
- **Relatórios**: Artifacts + GitHub Security

### ✅ Análise de Segurança DAST (Obrigatório)
- **OWASP ZAP**: Testes dinâmicos na aplicação rodando
- **Execução**: Após deploy em staging
- **Testes**: XSS, SQL Injection, CSRF, Security Headers
- **Relatórios**: HTML completo nos artifacts

### ✅ Gerenciamento de Artefatos
- JAR versionado (30 dias)
- Imagens Docker no GitHub Container Registry
- Relatórios OWASP, CodeQL, ZAP, Selenium

### ✅ Deploy Automatizado
- Docker multi-stage build
- Kubernetes com PostgreSQL
- Health checks (liveness/readiness)
- Deploy staging automático

### ✅ Múltiplos Ambientes
- **Staging**: Deploy automático após SAST
- **Production**: Requer aprovação manual após DAST

### ✅ Gerenciamento de Secrets
- GitHub Secrets: `PROD_DB_PASSWORD`, `STAGING_DB_PASSWORD`
- GitHub Variables: `PROD_DB_HOST`, `STAGING_DB_HOST`
- Kubernetes Secrets para runtime

## 🏗️ Arquitetura do Pipeline

```
┌──────────────┐
│ Git Push/PR  │
└──────┬───────┘
       ↓
┌──────────────────────────────────────┐
│ Build & Test (3-5 min)               │
│ • Maven build                         │
│ • Unit tests                          │
│ • JaCoCo coverage                     │
└──────┬───────────────────────────────┘
       ↓
┌──────────────────────────────────────┐
│ Security SAST (5-10 min)             │
│ • OWASP Dependency Check              │
│ • SpotBugs                            │
│ • CodeQL                              │
└──────┬───────────────────────────────┘
       ↓
┌──────────────────────────────────────┐
│ Package & Docker Build (3-5 min)     │
│ • Maven package                       │
│ • Docker build & push                 │
│ • Trivy scan                          │
└──────┬───────────────────────────────┘
       ↓
┌──────────────────────────────────────┐
│ Deploy Staging (2-3 min)             │
│ • Kubernetes deploy                   │
│ • Health checks                       │
└──────┬───────────────────────────────┘
       ↓
┌──────────────────────────────────────┐
│ Selenium Tests (5-8 min)             │
│ • E2E tests pós-deploy                │
│ • Screenshots em falhas               │
└──────┬───────────────────────────────┘
       ↓
┌──────────────────────────────────────┐
│ Security DAST (5-10 min)             │
│ • OWASP ZAP scan                      │
│ • Penetration testing                 │
└──────┬───────────────────────────────┘
       ↓
┌──────────────────────────────────────┐
│ ⏸️ Manual Approval                    │
└──────┬───────────────────────────────┘
       ↓
┌──────────────────────────────────────┐
│ Deploy Production (3-5 min)          │
│ • Production deployment               │
│ • Verification                        │
└──────────────────────────────────────┘
```

**Tempo total**: ~25-35 minutos (até aprovação manual)

## 🔧 Como Rodar Localmente

### Pré-requisitos
```bash
- Java 17+
- Maven 3.8+
- Docker 20+ (opcional)
- kubectl (opcional)
```

### Execução Local

```bash
# 1. Clone o repositório
git clone https://github.com/seu-usuario/card-shop.git
cd card-shop

# 2. Execute a aplicação
./mvnw spring-boot:run

# 3. Acesse a aplicação
# http://localhost:8080
```

### Testes

```bash
# Testes unitários
./mvnw test -Dtest=!*Selenium*

# Testes Selenium
./mvnw test -Dtest=*Selenium*

# Cobertura de código
./mvnw jacoco:report
open target/site/jacoco/index.html

# Análise de segurança
./mvnw org.owasp:dependency-check-maven:check
open target/dependency-check-report.html
```

### Docker Compose

```bash
# Iniciar aplicação + PostgreSQL
docker-compose up -d

# Ver logs
docker-compose logs -f

# Parar
docker-compose down

# Limpar volumes (remove dados do banco)
docker-compose down -v
```

### Docker (standalone)

```bash
# Build da imagem
docker build -f docker/Dockerfile -t card-shop:local .

# Executar container (requer PostgreSQL externo)
docker run -p 8080:8080 \
  -e DB_HOST=host.docker.internal \
  -e DB_PASSWORD=cardshop \
  card-shop:local

# Health check
curl http://localhost:8080/actuator/health
```

## 🚀 Deploy

### Deploy Automático

```bash
# Push para main aciona pipeline automaticamente
git add .
git commit -m "feat: New feature"
git push origin main

# Pipeline executa:
# ✅ Build e testes
# ✅ Análise de segurança
# ✅ Deploy em staging
# ⏸️ Aguarda aprovação para produção
```

### Deploy Manual

```bash
# Via GitHub CLI
gh workflow run ci-cd-complete.yml \
  --ref main \
  --field deploy_environment=staging
```

### Criar Release

```bash
# Criar tag e release
git tag -a v1.0.0 -m "Release v1.0.0"
git push origin v1.0.0

gh release create v1.0.0 \
  --title "Release v1.0.0" \
  --notes "Release notes"
```

## 🔐 Configuração de Secrets

### Passo 1: Gerar Secret do Banco

```bash
# Gerar senha segura para o banco
openssl rand -base64 32
```

### Passo 2: Configurar no GitHub

**Repository Secrets:**
```
Settings → Secrets and variables → Actions → New repository secret

Nome: PROD_DB_PASSWORD
Valor: [senha gerada]

Nome: STAGING_DB_PASSWORD
Valor: [senha gerada]
```

**Repository Variables:**
```
Settings → Secrets and variables → Actions → Variables tab

Nome: PROD_DB_HOST
Valor: postgres-prod.example.com

Nome: STAGING_DB_HOST
Valor: postgres-staging.example.com
```

### Passo 3: Configurar Environments

```
Settings → Environments → New environment

1. staging (sem aprovação)
2. production-approval (com aprovação)
3. production (com aprovação)
```

**📖 Guia completo**: [docs/SECRETS_GUIDE.md](docs/SECRETS_GUIDE.md)

## 📊 Workflows do GitHub Actions

### Pipeline CI/CD com SAST/DAST
**Arquivo**: `.github/workflows/ci-cd.yml`

**Gatilhos**:
- Push na branch `main`
- Pull requests para `main`
- Manual (workflow_dispatch)

**Jobs** (9 jobs com segurança completa):
1. **Build & Test** - Maven build e testes unitários
2. **SAST: OWASP** - Análise de dependências (obrigatório)
3. **SAST: CodeQL** - Análise de código (obrigatório)
4. **Docker Build & Push** - Imagem para ghcr.io
5. **Deploy Staging** - Deploy automático
6. **Selenium Tests** - Testes E2E pós-deploy (obrigatório)
7. **DAST: OWASP ZAP** - Análise dinâmica (obrigatório)
8. **Deploy Production** - Deploy com aprovação manual

**Tempo**: ~20-30 minutos

**Requisitos Atendidos**:
- ✅ **SAST** (Static Application Security Testing)
- ✅ **DAST** (Dynamic Application Security Testing)
- ✅ **Testes Automatizados Pós-Deploy**

🔒 **Segurança**: Veja [docs/SECURITY_ANALYSIS.md](docs/SECURITY_ANALYSIS.md)

### Como Interpretar Resultados

1. Acesse **Actions** no GitHub
2. Clique no workflow "Complete CI/CD Pipeline"
3. ✅ Verde = Sucesso
4. ❌ Vermelho = Falha (clique para ver logs)
5. 🟡 Amarelo = Em andamento
6. ⏸️ Cinza = Aguardando aprovação

## 🛡️ Segurança

### SAST (Static Analysis)
- ✅ OWASP Dependency Check
- ✅ SpotBugs
- ✅ CodeQL

### DAST (Dynamic Analysis)
- ✅ OWASP ZAP
- ✅ Trivy container scan

### Best Practices
- ✅ Secrets management
- ✅ Least privilege access
- ✅ Automated scanning
- ✅ Vulnerability alerts

## 📦 Tecnologias Utilizadas

### Backend
- Java 17
- Spring Boot 3.3.1
- Spring Data JPA
- PostgreSQL 16 (production)
- H2 Database (tests only)
- Hibernate

### Frontend
- Thymeleaf
- Bootstrap 4.5.2
- HTML5/CSS3

### Testes
- JUnit 5
- Mockito
- Selenium WebDriver
- JaCoCo (coverage)

### CI/CD
- GitHub Actions
- Maven
- Docker
- Kubernetes

### Segurança
- OWASP Dependency Check
- SpotBugs
- CodeQL
- OWASP ZAP
- Trivy

## 📖 Documentação API

### Endpoints Principais

```bash
# Home - Lista de cards
GET /

# Adicionar card (formulário)
GET /add

# Adicionar card (submit)
POST /add

# Editar card (formulário)
GET /edit/{id}

# Editar card (submit)
POST /edit/{id}

# Deletar card
GET /delete/{id}
```

### Endpoints Actuator

```bash
# Health check
GET /actuator/health

# Liveness probe
GET /actuator/health/liveness

# Readiness probe
GET /actuator/health/readiness

# Metrics
GET /actuator/metrics

# Prometheus metrics
GET /actuator/prometheus
```

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'feat: Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 🐛 Troubleshooting

### Pipeline Falhou?
1. Verifique os logs no GitHub Actions
2. Baixe os artefatos para análise
3. Consulte [TUTORIAL.md](TUTORIAL.md) seção "Debugging"

### Testes Selenium Falhando?
1. Verifique screenshots em Artifacts
2. Execute localmente: `./mvnw test -Dtest=*Selenium*`
3. Verifique se Firefox está instalado

### Deploy Kubernetes Falhou?
1. Verifique secrets: `kubectl get secrets -n card-shop`
2. Verifique logs: `kubectl logs -f deployment/card-shop -n card-shop`
3. Verifique eventos: `kubectl get events -n card-shop`

## 📞 Suporte

- **Issues**: [GitHub Issues](https://github.com/seu-usuario/card-shop/issues)
- **Documentação**: Veja os arquivos markdown neste repositório
- **Email**: seu-email@example.com

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## ✨ Próximos Passos

1. ✅ Leia [IMPLEMENTATION_REPORT.md](IMPLEMENTATION_REPORT.md)
2. ✅ Configure secrets (veja [SECRETS_GUIDE.md](SECRETS_GUIDE.md))
3. ✅ Configure environments no GitHub
4. ✅ Faça primeiro push e acompanhe o pipeline
5. ✅ Aprove deploy em produção quando solicitado

---

⭐ **Made with ❤️ using GitHub Actions, Docker & Kubernetes**

## Principais Mudanças na Refatoração

Durante o processo de desenvolvimento e refatoração, as seguintes mudanças foram implementadas para melhorar a robustez e a automação do projeto:

1.  **Gerenciamento Automático do WebDriver:**
    -   **Antes:** O caminho para o `geckodriver` do Selenium era fixo no código (`System.setProperty`), o que exigia configuração manual em cada ambiente.
    -   **Depois:** Foi adicionada a dependência `WebDriverManager` (`io.github.bonigarcia:webdrivermanager`). Ela gerencia automaticamente o download e a configuração do driver do navegador necessário para os testes, eliminando a necessidade de configuração manual e tornando os testes mais portáteis.

2.  **Execução de Testes em Ambiente sem Interface (Headless):**
    -   **Antes:** Os testes de Selenium eram executados abrindo uma janela real do navegador.
    -   **Depois:** Os testes foram configurados para rodar em modo *headless*. Isso permite que eles sejam executados em ambientes de integração contínua (como o GitHub Actions) que não possuem uma interface gráfica.

3.  **Correção de Dependências e Build:**
    -   Foi corrigida a versão do `spring-boot-starter-parent` no `pom.xml` de uma versão inexistente (`3.5.7`) para uma versão estável (`3.3.1`), resolvendo falhas de build.
    -   Foi removida uma dependência duplicada do `webdrivermanager` no `pom.xml`.

4.  **Criação do Workflow de Integração Contínua:**
    -   Foi criado o arquivo `.github/workflows/maven.yml` para automatizar o processo de build e teste a cada alteração no código, garantindo que novas mudanças não quebrem a funcionalidade existente.

