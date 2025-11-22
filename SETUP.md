# 🚀 Guia Rápido de Configuração

## 📁 Estrutura do Projeto

```
card-shop/
├── docs/               # Documentação completa
├── docker/             # Arquivos Docker
│   ├── Dockerfile
│   └── .dockerignore
├── k8s/                # Manifests Kubernetes
│   └── k8s-deployment.yaml
├── src/                # Código fonte
├── docker-compose.yml  # Docker Compose para dev local
├── .env.example        # Template de variáveis
└── pom.xml             # Maven configuration
```

---

## 🗄️ Banco de Dados

### PostgreSQL Configuration

A aplicação usa PostgreSQL. Configuração via variáveis de ambiente:

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| `DB_HOST` | Host do PostgreSQL | `localhost` |
| `DB_PORT` | Porta do PostgreSQL | `5432` |
| `DB_NAME` | Nome do banco | `cardshop` |
| `DB_USER` | Usuário | `cardshop` |
| `DB_PASSWORD` | Senha (SECRET) | `cardshop` |

---

## 🔧 Desenvolvimento Local

### Opção 1: Docker Compose (Recomendado)

```bash
# Iniciar app + PostgreSQL
docker-compose up -d

# Acessar
http://localhost:8080

# Logs
docker-compose logs -f app

# Parar
docker-compose down
```

### Opção 2: Maven + PostgreSQL Local

```bash
# 1. Instalar PostgreSQL localmente
brew install postgresql  # macOS
# ou
sudo apt install postgresql  # Ubuntu

# 2. Criar banco
createdb cardshop
createuser cardshop

# 3. Configurar variáveis
export DB_HOST=localhost
export DB_PASSWORD=cardshop

# 4. Executar
mvn spring-boot:run
```

---

## 🧪 Testes

Os testes usam H2 em memória (não requer PostgreSQL):

```bash
# Todos os testes
mvn test

# Apenas unitários
mvn test -Dtest=!*Selenium*

# Apenas Selenium
mvn test -Dtest=*Selenium*
```

---

## 🐳 Docker

### Build

```bash
docker build -f docker/Dockerfile -t card-shop:latest .
```

### Run

```bash
# Com PostgreSQL no docker-compose
docker-compose up

# Ou standalone (requer PostgreSQL externo)
docker run -p 8080:8080 \
  -e DB_HOST=postgres \
  -e DB_PASSWORD=senha \
  card-shop:latest
```

---

## ☸️ Kubernetes

### Deploy

```bash
# Aplicar todos os manifestos
kubectl apply -f k8s/k8s-deployment.yaml

# Verificar
kubectl get pods -n card-shop
kubectl get svc -n card-shop

# Logs
kubectl logs -f deployment/card-shop -n card-shop
```

### Configuração

O deployment inclui:
- PostgreSQL (com PVC para persistência)
- Card Shop app
- ConfigMap (variáveis)
- Secret (DB_PASSWORD)
- Services
- HPA (auto-scaling)

---

## 🔐 Secrets Configuration

### GitHub Secrets (CI/CD)

**Secrets (dados sensíveis):**
```
PROD_DB_PASSWORD       # Senha PostgreSQL produção
STAGING_DB_PASSWORD    # Senha PostgreSQL staging
```

**Variables (dados não sensíveis):**
```
PROD_DB_HOST          # Host PostgreSQL produção
STAGING_DB_HOST       # Host PostgreSQL staging
```

### Kubernetes Secrets

```bash
# Criar secret do banco
kubectl create secret generic card-shop-secrets \
  --from-literal=DB_PASSWORD='sua-senha-aqui' \
  --namespace=card-shop

# Verificar
kubectl get secret card-shop-secrets -n card-shop
```

---

## 📊 Environments

### Development (Local)
- Docker Compose
- PostgreSQL local
- DB_PASSWORD: `cardshop`
- DB_HOST: `localhost` ou `postgres` (container)

### Staging
- Kubernetes
- PostgreSQL dedicado
- DB_PASSWORD: `${STAGING_DB_PASSWORD}` (GitHub Secret)
- DB_HOST: `${STAGING_DB_HOST}` (GitHub Variable)

### Production
- Kubernetes
- PostgreSQL em cluster
- DB_PASSWORD: `${PROD_DB_PASSWORD}` (GitHub Secret)
- DB_HOST: `${PROD_DB_HOST}` (GitHub Variable)

---

## 🚀 CI/CD Pipeline

### Trigger

O pipeline é acionado por:
- Push em `main` ou `develop`
- Pull Request para `main`
- Release
- Manual (workflow_dispatch)

### Variáveis Necessárias

**Repository Secrets:**
- `PROD_DB_PASSWORD` - Senha do banco de produção
- `STAGING_DB_PASSWORD` - Senha do banco de staging

**Repository Variables:**
- `PROD_DB_HOST` - Host do banco de produção
- `STAGING_DB_HOST` - Host do banco de staging

### Como Configurar

1. **Secrets:**
   ```
   GitHub → Settings → Secrets and variables → Actions
   → Secrets tab → New repository secret
   ```

2. **Variables:**
   ```
   GitHub → Settings → Secrets and variables → Actions
   → Variables tab → New repository variable
   ```

3. **Gerar senha:**
   ```bash
   openssl rand -base64 32
   ```

---

## 📝 Comandos Úteis

### Docker Compose

```bash
# Iniciar
docker-compose up -d

# Parar
docker-compose down

# Rebuild
docker-compose up -d --build

# Ver logs
docker-compose logs -f

# Executar comando no container
docker-compose exec app bash
docker-compose exec postgres psql -U cardshop
```

### Maven

```bash
# Build
mvn clean package

# Executar
mvn spring-boot:run

# Testes
mvn test

# Limpar
mvn clean
```

### Kubernetes

```bash
# Deploy
kubectl apply -f k8s/k8s-deployment.yaml

# Status
kubectl get all -n card-shop

# Logs
kubectl logs -f deployment/card-shop -n card-shop

# Port forward
kubectl port-forward svc/card-shop-service 8080:80 -n card-shop

# Delete
kubectl delete -f k8s/k8s-deployment.yaml
```

---

## 🆘 Troubleshooting

### App não conecta no banco

```bash
# Verificar se PostgreSQL está rodando
docker-compose ps

# Ver logs do banco
docker-compose logs postgres

# Verificar variáveis
docker-compose exec app env | grep DB_
```

### Testes falhando

```bash
# Testes usam H2, não PostgreSQL
# Verificar se profile test está ativo
mvn test -Dspring.profiles.active=test
```

### Docker build falha

```bash
# Limpar cache
docker system prune -a

# Build com logs
docker build -f docker/Dockerfile --progress=plain .
```

---

## 📚 Documentação Completa

Para mais detalhes, consulte:

- **[docs/IMPLEMENTATION_REPORT.md](docs/IMPLEMENTATION_REPORT.md)** - Relatório completo
- **[docs/SECRETS_GUIDE.md](docs/SECRETS_GUIDE.md)** - Guia de secrets
- **[docs/TUTORIAL.md](docs/TUTORIAL.md)** - Tutorial passo a passo
- **[docs/COMMANDS.md](docs/COMMANDS.md)** - Referência de comandos

---

## ✅ Checklist de Setup

- [ ] PostgreSQL rodando (via docker-compose ou local)
- [ ] Variáveis de ambiente configuradas
- [ ] Testes passando (`mvn test`)
- [ ] App rodando localmente (`docker-compose up` ou `mvn spring-boot:run`)
- [ ] Secrets configurados no GitHub (para CI/CD)
- [ ] Environments criados no GitHub

---

**🎉 Pronto para começar!**

