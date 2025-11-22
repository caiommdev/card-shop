# 🔐 Guia de Configuração de Segredos e Variáveis

Este guia explica como configurar segredos e variáveis de ambiente no GitHub para o pipeline CI/CD.

## 📋 Índice

1. [Secrets do GitHub](#secrets-do-github)
2. [Variáveis de Ambiente](#variáveis-de-ambiente)
3. [Ambientes (Environments)](#ambientes-environments)
4. [Como Configurar](#como-configurar)

---

## 🔒 Secrets do GitHub

### Secrets Obrigatórios

Os seguintes secrets devem ser configurados no GitHub:

#### 1. **PROD_DB_PASSWORD**
- **Descrição**: Senha do banco de dados PostgreSQL de produção
- **Uso**: Deploy em produção
- **Exemplo**: `minha_senha_super_segura_123!`
- **Como gerar**: Use um gerador de senhas fortes
```bash
openssl rand -base64 32
```

#### 2. **STAGING_DB_PASSWORD**
- **Descrição**: Senha do banco de dados PostgreSQL de staging
- **Uso**: Deploy em staging/homologação
- **Como gerar**:
```bash
openssl rand -base64 32
```

### Variables Obrigatórias (Repository Variables)

#### 1. **PROD_DB_HOST**
- **Descrição**: Host do PostgreSQL de produção
- **Uso**: Conexão com banco em produção
- **Exemplo**: `postgres-prod.example.com` ou `postgres-service` (K8s)

#### 2. **STAGING_DB_HOST**
- **Descrição**: Host do PostgreSQL de staging
- **Uso**: Conexão com banco em staging
- **Exemplo**: `postgres-staging.example.com` ou `postgres-service` (K8s)

### Secrets Opcionais (Recomendados)

#### 3. **DOCKER_REGISTRY_TOKEN**
- **Descrição**: Token para registry Docker privado (se não usar GitHub Container Registry)
- **Uso**: Push de imagens Docker
- **Nota**: O pipeline atual usa `GITHUB_TOKEN` que é automático

#### 6. **KUBECONFIG**
- **Descrição**: Configuração do Kubernetes para deploy
- **Uso**: Deploy automatizado
- **Como obter**:
```bash
cat ~/.kube/config | base64
```

#### 7. **SLACK_WEBHOOK_URL** (Opcional)
- **Descrição**: Webhook para notificações no Slack
- **Uso**: Notificações de deploy e falhas

---

## 🌍 Variáveis de Ambiente

### Variáveis Configuradas no Workflow

As seguintes variáveis já estão configuradas no arquivo `.github/workflows/ci-cd-complete.yml`:

```yaml
env:
  JAVA_VERSION: '17'              # Versão do Java
  MAVEN_OPTS: -Xmx1024m          # Opções do Maven
  DOCKER_REGISTRY: ghcr.io        # Registry do Docker
  IMAGE_NAME: ${{ github.repository }}  # Nome da imagem
```

### Variáveis de Ambiente Kubernetes

Configuradas em `k8s-deployment.yaml`:

**ConfigMap** (não sensíveis):
- `APPLICATION_NAME`: Nome da aplicação
- `SPRING_PROFILES_ACTIVE`: Perfil Spring ativo

**Secrets** (sensíveis):
- `DATABASE_PASSWORD`: Senha do banco
- `JWT_SECRET`: Chave JWT

---

## 🏗️ Ambientes (Environments)

O pipeline usa 3 ambientes do GitHub:

### 1. **staging**
- Deploy automático após testes passarem
- Sem necessidade de aprovação
- URL: `http://staging.example.com`

### 2. **production-approval**
- Gate de aprovação manual
- Requer revisão antes do deploy em produção

### 3. **production**
- Deploy em produção
- Requer aprovação manual
- URL: `https://card-shop.example.com`

---

## ⚙️ Como Configurar

### Passo 1: Configurar Secrets no GitHub

1. Acesse seu repositório no GitHub
2. Vá em **Settings** → **Secrets and variables** → **Actions**
3. Clique em **New repository secret**
4. Adicione cada secret:

```
Nome: PROD_DATABASE_PASSWORD
Valor: [sua senha segura]
```

```
Nome: PROD_JWT_SECRET
Valor: [sua chave JWT]
```

### Passo 2: Configurar Environments

1. Vá em **Settings** → **Environments**
2. Clique em **New environment**

#### Configurar "staging":
```
Nome: staging
Deployment branches: Selected branches (main)
```

#### Configurar "production-approval":
```
Nome: production-approval
Required reviewers: [adicione revisores]
Wait timer: 0 minutes
```

#### Configurar "production":
```
Nome: production
Required reviewers: [adicione revisores]
Deployment branches: Selected branches (main, tags)
```

3. Para cada ambiente, adicione secrets específicos se necessário:
   - Clique no ambiente
   - Em **Environment secrets**, adicione secrets específicos

### Passo 3: Configurar Secrets do Kubernetes

Para deploy no Kubernetes, você precisa criar os secrets:

```bash
# Criar namespace
kubectl create namespace card-shop

# Criar secret com senha do banco
kubectl create secret generic card-shop-secrets \
  --from-literal=DATABASE_PASSWORD='sua_senha_aqui' \
  --from-literal=JWT_SECRET='seu_jwt_secret_aqui' \
  --namespace=card-shop

# Verificar
kubectl get secrets -n card-shop
```

### Passo 4: Configurar KUBECONFIG (se usar Kubernetes)

```bash
# Obter kubeconfig encodado
cat ~/.kube/config | base64 -w 0

# Adicionar como secret no GitHub com nome KUBECONFIG
```

---

## 🧪 Testar Configuração

### Teste 1: Verificar se secrets estão disponíveis

Adicione este step temporário no workflow:

```yaml
- name: Test Secrets
  run: |
    echo "Testing secrets configuration..."
    if [ -z "${{ secrets.PROD_DATABASE_PASSWORD }}" ]; then
      echo "❌ PROD_DATABASE_PASSWORD not configured"
    else
      echo "✅ PROD_DATABASE_PASSWORD configured"
    fi
```

### Teste 2: Deploy manual

Vá em **Actions** → **Complete CI/CD Pipeline** → **Run workflow**
- Selecione branch: `main`
- Environment: `staging`

---

## 🔐 Boas Práticas de Segurança

### ✅ FAÇA:

1. **Use secrets diferentes para cada ambiente**
   ```
   DEV_DATABASE_PASSWORD
   STAGING_DATABASE_PASSWORD
   PROD_DATABASE_PASSWORD
   ```

2. **Rotacione secrets regularmente**
   - Troque senhas a cada 90 dias
   - Use ferramentas de gerenciamento de secrets (HashiCorp Vault, AWS Secrets Manager)

3. **Limite acesso aos secrets**
   - Configure proteções de branch
   - Exija revisão de código

4. **Monitore uso de secrets**
   - Revise logs de workflow
   - Configure alertas

### ❌ NÃO FAÇA:

1. ❌ Não commite secrets no código
2. ❌ Não use secrets em logs
3. ❌ Não compartilhe secrets por chat/email
4. ❌ Não use a mesma senha em todos os ambientes

---

## 📊 Exemplo de Secrets Completos

Aqui está uma lista completa de secrets recomendados:

```
Repository Secrets:
├── PROD_DATABASE_PASSWORD          [Obrigatório]
├── PROD_JWT_SECRET                 [Obrigatório]
├── STAGING_DATABASE_PASSWORD       [Recomendado]
├── STAGING_JWT_SECRET             [Recomendado]
├── KUBECONFIG                     [Se usar K8s]
├── DOCKER_REGISTRY_USERNAME       [Se registry privado]
├── DOCKER_REGISTRY_TOKEN          [Se registry privado]
├── SLACK_WEBHOOK_URL              [Opcional]
├── SONAR_TOKEN                    [Se usar SonarQube]
└── NPM_TOKEN                      [Se publicar pacotes]

Environment Secrets (staging):
├── DATABASE_URL
├── API_KEY
└── SERVICE_ACCOUNT_KEY

Environment Secrets (production):
├── DATABASE_URL
├── API_KEY
├── SERVICE_ACCOUNT_KEY
└── MONITORING_API_KEY
```

---

## 🆘 Solução de Problemas

### Problema: "Secret not found"
**Solução**: Verifique se o secret está configurado corretamente no GitHub Settings

### Problema: "Unauthorized" no Docker push
**Solução**: Verifique se `GITHUB_TOKEN` tem permissões de `packages: write`

### Problema: Deploy falha no Kubernetes
**Solução**: Verifique se KUBECONFIG está correto e tem permissões

---

## 📚 Recursos Adicionais

- [GitHub Secrets Documentation](https://docs.github.com/en/actions/security-guides/encrypted-secrets)
- [GitHub Environments](https://docs.github.com/en/actions/deployment/targeting-different-environments/using-environments-for-deployment)
- [Kubernetes Secrets](https://kubernetes.io/docs/concepts/configuration/secret/)
- [OWASP Secrets Management](https://cheatsheetseries.owasp.org/cheatsheets/Secrets_Management_Cheat_Sheet.html)

---

## ✅ Checklist de Configuração

Antes de fazer deploy em produção, verifique:

- [ ] Todos os secrets obrigatórios configurados
- [ ] Environments criados (staging, production-approval, production)
- [ ] Required reviewers configurados para produção
- [ ] Secrets do Kubernetes criados
- [ ] Branch protection rules configuradas
- [ ] Teste manual executado com sucesso em staging
- [ ] Documentação atualizada com URLs reais
- [ ] Monitoramento configurado
- [ ] Backup strategy definida
- [ ] Rollback procedure documentada

---

## 📞 Suporte

Para dúvidas sobre configuração de secrets, consulte:
1. Documentação do GitHub Actions
2. Time de DevOps/SRE
3. Security team para revisão de secrets

