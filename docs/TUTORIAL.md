# 🎓 Tutorial Prático - Pipeline CI/CD

Este guia fornece exemplos práticos de como usar o pipeline CI/CD do projeto Card Shop.

## 📚 Índice

1. [Primeiro Deploy](#primeiro-deploy)
2. [Deploy Manual](#deploy-manual)
3. [Configurar Secrets](#configurar-secrets)
4. [Criar Release](#criar-release)
5. [Rollback](#rollback)
6. [Debugging](#debugging)

---

## 🚀 Primeiro Deploy

### Passo 1: Configurar Secrets

Acesse: **GitHub Repository → Settings → Secrets and variables → Actions**

```bash
# Gerar secrets seguros
openssl rand -base64 32  # Para DATABASE_PASSWORD
openssl rand -hex 64     # Para JWT_SECRET
```

Adicione os secrets:

| Nome | Valor | Descrição |
|------|-------|-----------|
| `PROD_DATABASE_PASSWORD` | `[senha gerada]` | Senha do banco de produção |
| `PROD_JWT_SECRET` | `[secret gerado]` | Chave JWT de produção |

### Passo 2: Configurar Environments

1. Vá em **Settings → Environments**
2. Crie 3 ambientes:

#### Staging
```yaml
Nome: staging
Deployment branches: Selected branches
  - main
  - develop
```

#### Production Approval
```yaml
Nome: production-approval
Required reviewers: [Adicione seu usuário]
Wait timer: 0 minutes
Deployment branches: Selected branches
  - main
```

#### Production
```yaml
Nome: production
Required reviewers: [Adicione seu usuário]
Wait timer: 0 minutes
Deployment branches: Selected branches
  - main
Deployment protection rules:
  ☑ Required reviewers
```

### Passo 3: Fazer Primeiro Push

```bash
# Na sua máquina local
git checkout main
git add .
git commit -m "Configure CI/CD pipeline"
git push origin main
```

### Passo 4: Acompanhar Pipeline

1. Vá em **Actions** tab no GitHub
2. Clique no workflow "Complete CI/CD Pipeline"
3. Acompanhe os jobs sendo executados

**Sequência esperada**:
```
✅ Build & Unit Tests (3-5 min)
✅ Security Analysis (SAST) (5-10 min)
✅ Package Application (2-3 min)
✅ Build & Push Docker Image (3-5 min)
✅ Deploy to Staging (2-3 min)
✅ Selenium E2E Tests (5-8 min)
✅ Security Analysis (DAST) (5-10 min)
⏸️ Await Production Approval (aguardando)
```

### Passo 5: Aprovar Deploy em Produção

1. Quando o job "Await Production Approval" aparecer:
2. Clique em **Review deployments**
3. Selecione **production-approval**
4. Clique em **Approve and deploy**

```
✅ Await Production Approval (aprovado)
✅ Deploy to Production (3-5 min)
✅ Send Notifications (1 min)
```

🎉 **Deploy completo!**

---

## 🎯 Deploy Manual

### Quando Usar?
- Testar mudanças específicas
- Deploy emergencial
- Escolher ambiente específico

### Como Fazer?

1. Vá em **Actions** → **Complete CI/CD Pipeline**
2. Clique em **Run workflow**
3. Preencha:

```yaml
Branch: main
Environment to deploy: staging  # ou production
```

4. Clique em **Run workflow**

### Exemplo: Deploy Rápido em Staging

```bash
# Via GitHub CLI (gh)
gh workflow run ci-cd-complete.yml \
  --ref main \
  --field deploy_environment=staging
```

### Exemplo: Deploy em Produção

```bash
gh workflow run ci-cd-complete.yml \
  --ref main \
  --field deploy_environment=production
```

⚠️ **Nota**: Deploy em produção sempre requer aprovação manual!

---

## 🔐 Configurar Secrets

### Repository Secrets (Globais)

```bash
# Via GitHub CLI
gh secret set PROD_DATABASE_PASSWORD \
  --body "$(openssl rand -base64 32)"

gh secret set PROD_JWT_SECRET \
  --body "$(openssl rand -hex 64)"
```

### Environment Secrets (Específicos)

```bash
# Secret para staging
gh secret set STAGING_API_KEY \
  --env staging \
  --body "staging-api-key-123"

# Secret para production
gh secret set PROD_API_KEY \
  --env production \
  --body "prod-api-key-xyz"
```

### Verificar Secrets

```bash
# Listar secrets do repositório
gh secret list

# Listar secrets de um environment
gh secret list --env production
```

### Atualizar Secret

```bash
# Atualizar senha do banco
gh secret set PROD_DATABASE_PASSWORD \
  --body "nova_senha_super_segura_456"
```

---

## 📦 Criar Release

### Criar Release Automaticamente

```bash
# Criar tag
git tag -a v1.0.0 -m "Release v1.0.0"
git push origin v1.0.0

# Criar release no GitHub
gh release create v1.0.0 \
  --title "Release v1.0.0" \
  --notes "## Novidades
- Feature X adicionada
- Bug Y corrigido
- Melhoria Z implementada"
```

### O Pipeline Automaticamente:

1. ✅ Build completo
2. ✅ Todos os testes
3. ✅ Análise de segurança
4. ✅ Deploy em staging
5. ⏸️ Aguarda aprovação
6. ✅ Deploy em produção (após aprovação)

### Criar Release com Artefatos

```bash
# Build local
./mvnw clean package

# Criar release com JAR
gh release create v1.0.1 \
  --title "Release v1.0.1" \
  --notes "Bug fixes" \
  target/card-shop-0.0.1-SNAPSHOT.jar
```

---

## ⏪ Rollback

### Cenário: Bug em Produção

#### Opção 1: Rollback Rápido (Git)

```bash
# 1. Reverter commit problemático
git revert <commit-sha>
git push origin main

# 2. Pipeline deploy automaticamente a versão anterior
```

#### Opção 2: Deploy de Versão Anterior

```bash
# 1. Encontrar última versão boa
gh release list

# 2. Deploy manual dessa versão
gh workflow run ci-cd-complete.yml \
  --ref v1.0.0 \
  --field deploy_environment=production
```

#### Opção 3: Rollback Kubernetes Direto

```bash
# 1. Conectar ao cluster
kubectl config use-context production

# 2. Ver histórico de deploys
kubectl rollout history deployment/card-shop -n card-shop

# 3. Rollback para versão anterior
kubectl rollout undo deployment/card-shop -n card-shop

# 4. Ou rollback para revisão específica
kubectl rollout undo deployment/card-shop \
  --to-revision=2 \
  -n card-shop

# 5. Verificar status
kubectl rollout status deployment/card-shop -n card-shop
```

### Verificar Rollback

```bash
# Health check
curl https://card-shop.example.com/actuator/health

# Ver versão atual
kubectl describe deployment card-shop -n card-shop | grep Image
```

---

## 🐛 Debugging

### Pipeline Falhou - Como Investigar?

#### 1. Ver Logs Detalhados

```bash
# Via GitHub CLI
gh run list --workflow=ci-cd-complete.yml --limit 5
gh run view <run-id> --log
```

#### 2. Baixar Artefatos

```bash
# Baixar todos os artefatos
gh run download <run-id>

# Ver relatórios de teste
open test-results/

# Ver relatório OWASP
open owasp-dependency-check-report.html
```

### Testes Selenium Falharam

```bash
# 1. Baixar artefatos
gh run download <run-id> --name selenium-test-results

# 2. Se houver screenshots
gh run download <run-id> --name selenium-screenshots

# 3. Ver screenshots
open selenium-screenshots/*.png
```

### Build Maven Falhou

```bash
# 1. Reproduzir localmente
./mvnw clean install

# 2. Ver erros específicos
./mvnw -X clean install  # Debug mode

# 3. Limpar cache local
rm -rf ~/.m2/repository/*
./mvnw clean install
```

### Docker Build Falhou

```bash
# 1. Build localmente
docker build -t card-shop:debug .

# 2. Build com mais logs
docker build --progress=plain -t card-shop:debug .

# 3. Testar multi-stage
docker build --target build -t card-shop:build-stage .
```

### Análise de Segurança Reportou Vulnerabilidades

```bash
# 1. Ver relatório OWASP
./mvnw org.owasp:dependency-check-maven:check
open target/dependency-check-report.html

# 2. Atualizar dependências
./mvnw versions:display-dependency-updates

# 3. Atualizar versão específica
# Edite pom.xml e atualize a versão

# 4. Suprimir falso positivo
# Edite dependency-check-suppressions.xml
```

### Deploy Kubernetes Falhou

```bash
# 1. Verificar status do cluster
kubectl cluster-info

# 2. Ver pods
kubectl get pods -n card-shop

# 3. Ver logs do pod
kubectl logs -f <pod-name> -n card-shop

# 4. Descrever pod (ver eventos)
kubectl describe pod <pod-name> -n card-shop

# 5. Ver eventos do namespace
kubectl get events -n card-shop --sort-by='.lastTimestamp'

# 6. Verificar secrets
kubectl get secret card-shop-secrets -n card-shop -o yaml
```

---

## 📊 Monitoramento do Pipeline

### Ver Métricas de Sucesso

```bash
# Últimas 10 execuções
gh run list --workflow=ci-cd-complete.yml --limit 10

# Taxa de sucesso
gh run list --workflow=ci-cd-complete.yml \
  --json conclusion \
  --jq '[.[] | .conclusion] | group_by(.) | map({status: .[0], count: length})'
```

### Tempo Médio de Execução

```bash
# Ver duração das últimas runs
gh run list --workflow=ci-cd-complete.yml \
  --json durationMs,conclusion \
  --jq '.[] | select(.conclusion=="success") | .durationMs / 60000'
```

### Artefatos Gerados

```bash
# Listar todos os artefatos da última run
gh run view --log | grep "Upload artifact"

# Baixar artefato específico
gh run download --name card-shop-0.0.1-SNAPSHOT
```

---

## 🎯 Casos de Uso Comuns

### Caso 1: Hotfix em Produção

```bash
# 1. Criar branch de hotfix
git checkout -b hotfix/critical-bug main

# 2. Fazer correção
# ... editar arquivos ...

# 3. Commit e push
git commit -am "Fix critical bug in production"
git push origin hotfix/critical-bug

# 4. Abrir PR
gh pr create --title "Hotfix: Critical Bug" \
  --body "Fixes critical issue in production"

# 5. Após aprovação do PR, merge
gh pr merge --squash

# 6. Deploy automático acontece
# 7. Aprovar deploy em produção quando solicitado
```

### Caso 2: Feature Branch Testing

```bash
# 1. Criar feature branch
git checkout -b feature/new-card-type

# 2. Fazer mudanças
# ... desenvolvimento ...

# 3. Push e PR
git push origin feature/new-card-type
gh pr create

# Pipeline roda automaticamente:
# - Build
# - Testes
# - Análise de segurança
# (Não faz deploy)

# 4. Após aprovação, merge para main
# 5. Deploy automático em staging
```

### Caso 3: Teste de Carga Pré-Produção

```bash
# 1. Deploy manual em staging
gh workflow run ci-cd-complete.yml \
  --ref main \
  --field deploy_environment=staging

# 2. Aguardar deploy completar

# 3. Executar testes de carga
# (use ferramentas como JMeter, K6, etc.)

# 4. Se OK, promover para produção
gh release create v1.0.0
```

---

## 📝 Checklist Pré-Deploy

Antes de cada deploy em produção:

```bash
✅ Todos os testes passando?
   ./mvnw test

✅ Análise de segurança OK?
   ./mvnw org.owasp:dependency-check-maven:check

✅ Código revisado?
   gh pr view --web

✅ Documentação atualizada?
   git log --oneline -10

✅ Secrets configurados?
   gh secret list

✅ Rollback plan preparado?
   kubectl rollout history deployment/card-shop -n card-shop

✅ Stakeholders notificados?
   # Enviar notificação para equipe
```

---

## 🆘 Suporte e Ajuda

### Comandos Úteis

```bash
# Ver status do workflow
gh workflow view ci-cd-complete.yml

# Ver última execução
gh run view

# Cancelar execução em andamento
gh run cancel <run-id>

# Re-executar job falhado
gh run rerun <run-id>

# Re-executar apenas jobs falhados
gh run rerun <run-id> --failed
```

### Links Úteis

- [GitHub Actions Docs](https://docs.github.com/en/actions)
- [Maven Documentation](https://maven.apache.org/guides/)
- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [Docker Documentation](https://docs.docker.com/)

---

## 💡 Dicas e Boas Práticas

### 1. Use Tags Semânticas

```bash
# Versionamento semântico
v1.0.0  # Major.Minor.Patch
v1.0.1  # Bug fixes
v1.1.0  # New features (backwards compatible)
v2.0.0  # Breaking changes
```

### 2. Commit Messages Claras

```bash
# Bom
git commit -m "feat: Add card filtering by price"
git commit -m "fix: Resolve null pointer in CardService"
git commit -m "docs: Update deployment guide"

# Ruim
git commit -m "update"
git commit -m "fixes"
```

### 3. Test Locally First

```bash
# Sempre teste localmente antes de push
./mvnw clean test
docker build -t card-shop:test .
```

### 4. Monitor Logs

```bash
# Durante deploy, monitore logs
kubectl logs -f deployment/card-shop -n card-shop
```

### 5. Keep Secrets Secure

```bash
# Nunca commite secrets
# Use .gitignore
echo "*.env" >> .gitignore
echo "secrets.yaml" >> .gitignore
```

---

**🎉 Agora você está pronto para usar o pipeline CI/CD completo!**

Para dúvidas, consulte [PIPELINE_README.md](PIPELINE_README.md) ou abra uma issue.

