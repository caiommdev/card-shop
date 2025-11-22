# 🎯 Card Shop - Pipeline CI/CD Completo

[![CI/CD Pipeline](https://github.com/seu-usuario/card-shop/actions/workflows/ci-cd-complete.yml/badge.svg)](https://github.com/seu-usuario/card-shop/actions/workflows/ci-cd-complete.yml)
[![Security](https://img.shields.io/badge/security-SAST%2FDAST-green)](https://github.com/seu-usuario/card-shop/security)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

## 📋 Visão Geral

Este projeto implementa um **pipeline CI/CD completo e robusto** para a aplicação Card Shop, incluindo:

- ✅ Build automatizado com Maven
- ✅ Testes unitários e de integração
- ✅ Testes E2E com Selenium
- ✅ Análise de segurança estática (SAST)
- ✅ Análise de segurança dinâmica (DAST)
- ✅ Gerenciamento de artefatos
- ✅ Deploy automatizado (Kubernetes)
- ✅ Múltiplos ambientes (Staging/Production)
- ✅ Aprovação manual para produção
- ✅ Gerenciamento seguro de secrets

## 🏗️ Arquitetura do Pipeline

```
┌─────────────────────────────────────────────────────────────────────┐
│                         PIPELINE CI/CD                               │
├─────────────────────────────────────────────────────────────────────┤
│                                                                       │
│  1. BUILD & TEST          2. SECURITY SAST      3. PACKAGE           │
│  ┌─────────────┐         ┌──────────────┐     ┌─────────────┐      │
│  │ Maven Build │──────>  │ OWASP Check  │──>  │ Maven JAR   │      │
│  │ Unit Tests  │         │ SpotBugs     │     │ Docker Image│      │
│  │ JaCoCo      │         │ CodeQL       │     │ Artifacts   │      │
│  └─────────────┘         └──────────────┘     └─────────────┘      │
│        │                        │                     │              │
│        v                        v                     v              │
│  4. DEPLOY STAGING      5. SELENIUM TESTS    6. SECURITY DAST       │
│  ┌─────────────┐         ┌──────────────┐     ┌─────────────┐      │
│  │ Kubernetes  │──────>  │ E2E Tests    │──>  │ OWASP ZAP   │      │
│  │ Staging Env │         │ UI Tests     │     │ Pen Testing │      │
│  └─────────────┘         └──────────────┘     └─────────────┘      │
│        │                                              │              │
│        v                                              v              │
│  7. APPROVAL GATE                    8. DEPLOY PRODUCTION           │
│  ┌─────────────┐                     ┌──────────────┐              │
│  │ Manual      │──────────────────>  │ Kubernetes   │              │
│  │ Approval    │                     │ Production   │              │
│  └─────────────┘                     └──────────────┘              │
│                                                                       │
└─────────────────────────────────────────────────────────────────────┘
```

## 🚀 Gatilhos do Pipeline

O pipeline é acionado automaticamente em:

### 1. **Push na branch main**
```yaml
on:
  push:
    branches: [ "main", "develop" ]
```
- Executa build completo
- Roda todos os testes
- Deploy automático em staging

### 2. **Pull Request**
```yaml
on:
  pull_request:
    branches: [ "main" ]
```
- Executa build e testes
- Análise de segurança
- Não faz deploy

### 3. **Release**
```yaml
on:
  release:
    types: [published, created]
```
- Pipeline completo
- Requer aprovação manual
- Deploy em produção

### 4. **Manual (workflow_dispatch)**
```yaml
on:
  workflow_dispatch:
    inputs:
      deploy_environment: [staging, production]
```
- Executar manualmente via GitHub Actions
- Escolher ambiente de deploy

## 📦 Jobs do Pipeline

### Job 1: Build & Unit Tests
**Duração**: ~3-5 minutos

```bash
# O que faz:
- Checkout do código
- Setup JDK 17
- Cache de dependências Maven
- Build da aplicação
- Execução de testes unitários
- Upload de relatórios de teste
```

**Artefatos gerados**:
- `test-results/` - Relatórios JUnit

### Job 2: Security Analysis (SAST)
**Duração**: ~5-10 minutos

```bash
# Ferramentas utilizadas:
✓ OWASP Dependency Check - Vulnerabilidades em dependências
✓ SpotBugs - Análise estática de bugs
✓ CodeQL - Análise de segurança GitHub
```

**Artefatos gerados**:
- `owasp-dependency-check-report.html`
- `spotbugs-report.xml`
- Security alerts no GitHub Security tab

### Job 3: Package Application
**Duração**: ~2-3 minutos

```bash
# O que faz:
- Package Maven (JAR)
- Extração de versão
- Upload de artefatos
```

**Artefatos gerados**:
- `card-shop-{version}.jar` (90 dias de retenção)
- `application-jar` (1 dia, para Docker)

### Job 4: Docker Build & Push
**Duração**: ~3-5 minutos

```bash
# O que faz:
- Build de imagem Docker multi-stage
- Push para GitHub Container Registry
- Scan de vulnerabilidades com Trivy
- Tags automáticas (latest, SHA, version)
```

**Imagens geradas**:
```
ghcr.io/seu-usuario/card-shop:latest
ghcr.io/seu-usuario/card-shop:main-abc1234
ghcr.io/seu-usuario/card-shop:v1.0.0
```

### Job 5: Deploy Staging
**Duração**: ~2-3 minutos

```bash
# O que faz:
- Deploy automático no Kubernetes
- Ambiente: staging
- URL: http://staging.example.com
```

**Recursos Kubernetes**:
- Namespace: `card-shop`
- Deployment: 2 réplicas
- Service: LoadBalancer
- ConfigMap: variáveis de configuração
- Secrets: credenciais sensíveis

### Job 6: Selenium E2E Tests
**Duração**: ~5-8 minutos

```bash
# Testes executados:
✓ testAddCard() - Adicionar novo card
✓ testEditCard() - Editar card existente
✓ testDeleteCard() - Deletar card
```

**Artefatos gerados**:
- `selenium-test-results/` - Relatórios de teste
- `selenium-screenshots/` - Screenshots em caso de falha

### Job 7: Security DAST
**Duração**: ~5-10 minutos

```bash
# Ferramentas utilizadas:
✓ OWASP ZAP - Dynamic Application Security Testing
✓ Testes de penetração automatizados
✓ Scan de vulnerabilidades em runtime
```

**Artefatos gerados**:
- `zap-scan-report.html`

### Job 8: Approval Gate
**Duração**: Aguarda aprovação manual

```bash
# Requerimentos:
- Aprovação de 1+ revisores
- Ambiente: production-approval
- Apenas para releases e deploys em produção
```

### Job 9: Deploy Production
**Duração**: ~3-5 minutos

```bash
# O que faz:
- Deploy no Kubernetes de produção
- Uso de secrets de produção
- Health checks
- URL: https://card-shop.example.com
```

### Job 10: Notifications
**Duração**: <1 minuto

```bash
# Notificações:
✅ Sucesso: Notificação de deploy bem-sucedido
❌ Falha: Notificação de erro com logs
```

## 🔒 Gerenciamento de Secrets

### Secrets Obrigatórios

Configure no GitHub Settings > Secrets:

```bash
# Produção
PROD_DATABASE_PASSWORD     # Senha do banco de dados
PROD_JWT_SECRET           # Chave JWT para autenticação

# Staging (Recomendado)
STAGING_DATABASE_PASSWORD
STAGING_JWT_SECRET

# Kubernetes (Opcional)
KUBECONFIG                # Config do cluster K8s
```

### Como Gerar Secrets Seguros

```bash
# Gerar senha forte
openssl rand -base64 32

# Gerar JWT secret
openssl rand -hex 64

# Encoder kubeconfig
cat ~/.kube/config | base64 -w 0
```

📚 **Guia Completo**: Veja [SECRETS_GUIDE.md](SECRETS_GUIDE.md)

## 🌍 Ambientes

### Staging
- **URL**: http://staging.example.com
- **Deploy**: Automático após testes
- **Branch**: main, develop
- **Propósito**: Testes finais antes de produção

### Production
- **URL**: https://card-shop.example.com
- **Deploy**: Manual com aprovação
- **Branch**: main, tags
- **Propósito**: Ambiente de produção

## 🛠️ Configuração Local

### Pré-requisitos

```bash
- Java 17+
- Maven 3.8+
- Docker 20+
- kubectl (opcional)
```

### Build Local

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/card-shop.git
cd card-shop

# Build com Maven
./mvnw clean package

# Executar localmente
./mvnw spring-boot:run

# Acessar aplicação
open http://localhost:8080
```

### Testes Locais

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

### Docker Local

```bash
# Build da imagem
docker build -t card-shop:local .

# Executar container
docker run -p 8080:8080 card-shop:local

# Verificar health
curl http://localhost:8080/actuator/health
```

### Kubernetes Local (Minikube)

```bash
# Iniciar Minikube
minikube start

# Aplicar configurações
kubectl apply -f k8s-deployment.yaml

# Verificar pods
kubectl get pods -n card-shop

# Port forward
kubectl port-forward svc/card-shop-service 8080:80 -n card-shop

# Acessar aplicação
open http://localhost:8080
```

## 📊 Monitoramento e Métricas

### Endpoints do Actuator

```bash
# Health check
GET /actuator/health

# Informações da aplicação
GET /actuator/info

# Métricas
GET /actuator/metrics

# Prometheus metrics
GET /actuator/prometheus
```

### Health Checks Kubernetes

```yaml
# Liveness Probe
GET /actuator/health/liveness
# Intervalo: 10s
# Timeout: 3s

# Readiness Probe
GET /actuator/health/readiness
# Intervalo: 5s
# Timeout: 3s
```

## 🐛 Troubleshooting

### Pipeline Falha no Build
```bash
# Verificar logs
GitHub Actions > Workflow run > Job logs

# Testar localmente
./mvnw clean install
```

### Pipeline Falha nos Testes Selenium
```bash
# Verificar screenshots
GitHub Actions > Artifacts > selenium-screenshots

# Executar localmente com UI
./mvnw test -Dtest=*Selenium* -Dheadless=false
```

### Docker Push Falha
```bash
# Verificar permissões
Settings > Actions > General > Workflow permissions
☑ Read and write permissions

# Verificar token
${{ secrets.GITHUB_TOKEN }}
```

### Deploy Kubernetes Falha
```bash
# Verificar secrets
kubectl get secrets -n card-shop

# Verificar logs do pod
kubectl logs -f deployment/card-shop -n card-shop

# Verificar eventos
kubectl get events -n card-shop --sort-by='.lastTimestamp'
```

## 📈 Métricas do Pipeline

### Tempos Médios
- Build completo: **20-30 minutos**
- Build + testes: **8-12 minutos**
- Deploy staging: **2-3 minutos**
- Deploy production: **3-5 minutos**

### Taxa de Sucesso
- Build: **>95%**
- Testes: **>90%**
- Deploy: **>98%**

## 🔐 Segurança

### SAST (Static Application Security Testing)
- ✅ OWASP Dependency Check
- ✅ SpotBugs
- ✅ CodeQL

### DAST (Dynamic Application Security Testing)
- ✅ OWASP ZAP
- ✅ Trivy container scanning

### Best Practices
- ✅ Least privilege access
- ✅ Secrets rotation
- ✅ Branch protection
- ✅ Required approvals
- ✅ Audit logs

## 📚 Documentação Adicional

- [Guia de Secrets](SECRETS_GUIDE.md) - Configuração de segredos
- [Docker](Dockerfile) - Configuração de container
- [Kubernetes](k8s-deployment.yaml) - Deploy em K8s
- [Pipeline](.github/workflows/ci-cd-complete.yml) - Workflow completo

## 🤝 Contribuindo

1. Fork o projeto
2. Crie sua feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📝 Checklist de Deploy

Antes de fazer deploy em produção:

- [ ] Todos os testes passando
- [ ] Análise de segurança sem issues críticos
- [ ] Secrets configurados
- [ ] Environments criados
- [ ] Required reviewers configurados
- [ ] Branch protection rules ativadas
- [ ] Teste em staging bem-sucedido
- [ ] Documentação atualizada
- [ ] Plano de rollback preparado

## 📞 Suporte

- **Issues**: [GitHub Issues](https://github.com/seu-usuario/card-shop/issues)
- **Email**: seu-email@example.com
- **Slack**: #card-shop-support

## 📄 Licença

Este projeto está sob a licença MIT. Veja [LICENSE](LICENSE) para mais detalhes.

---

⭐ **Made with ❤️ by DevOps Team**

