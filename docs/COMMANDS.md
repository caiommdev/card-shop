# 🚀 Comandos Rápidos - CI/CD Card Shop

Referência rápida de comandos para trabalhar com o pipeline CI/CD.

## 📦 Maven

```bash
# Build completo
./mvnw clean install

# Build sem testes
./mvnw clean package -DskipTests

# Apenas testes unitários
./mvnw test -Dtest=!*Selenium*

# Apenas testes Selenium
./mvnw test -Dtest=*Selenium*

# Verificar dependências vulneráveis
./mvnw org.owasp:dependency-check-maven:check

# Análise SpotBugs
./mvnw spotbugs:check

# Relatório de cobertura
./mvnw jacoco:report
```

## 🐳 Docker

```bash
# Build da imagem
docker build -t card-shop:local .

# Build com logs detalhados
docker build --progress=plain -t card-shop:local .

# Executar container
docker run -p 8080:8080 card-shop:local

# Executar em background
docker run -d -p 8080:8080 --name card-shop card-shop:local

# Ver logs
docker logs -f card-shop

# Parar container
docker stop card-shop

# Remover container
docker rm card-shop

# Remover imagem
docker rmi card-shop:local

# Health check
docker inspect --format='{{.State.Health.Status}}' card-shop
```

## ☸️ Kubernetes

```bash
# Aplicar manifests
kubectl apply -f k8s-deployment.yaml

# Ver pods
kubectl get pods -n card-shop

# Ver detalhes do pod
kubectl describe pod <pod-name> -n card-shop

# Ver logs
kubectl logs -f <pod-name> -n card-shop

# Ver logs de todos os pods
kubectl logs -f deployment/card-shop -n card-shop

# Ver eventos
kubectl get events -n card-shop --sort-by='.lastTimestamp'

# Ver services
kubectl get svc -n card-shop

# Ver deployments
kubectl get deployments -n card-shop

# Ver configmaps
kubectl get configmap -n card-shop

# Ver secrets
kubectl get secrets -n card-shop

# Port forward
kubectl port-forward svc/card-shop-service 8080:80 -n card-shop

# Rollout status
kubectl rollout status deployment/card-shop -n card-shop

# Rollout history
kubectl rollout history deployment/card-shop -n card-shop

# Rollback
kubectl rollout undo deployment/card-shop -n card-shop

# Scale
kubectl scale deployment/card-shop --replicas=3 -n card-shop

# Deletar deployment
kubectl delete -f k8s-deployment.yaml
```

## 🔐 Secrets Management

```bash
# Gerar senha segura
openssl rand -base64 32

# Gerar JWT secret
openssl rand -hex 64

# Encoder para Kubernetes
echo -n "my-secret" | base64

# Decoder
echo "bXktc2VjcmV0" | base64 -d

# GitHub CLI - Adicionar secret
gh secret set SECRET_NAME

# Listar secrets
gh secret list

# Remover secret
gh secret remove SECRET_NAME

# Kubernetes - Criar secret
kubectl create secret generic card-shop-secrets \
  --from-literal=DATABASE_PASSWORD='password' \
  --from-literal=JWT_SECRET='jwt-secret' \
  --namespace=card-shop

# Ver secret (encodado)
kubectl get secret card-shop-secrets -n card-shop -o yaml

# Ver secret (decodado)
kubectl get secret card-shop-secrets -n card-shop -o jsonpath='{.data.DATABASE_PASSWORD}' | base64 -d
```

## 🚀 GitHub Actions

```bash
# Ver workflows
gh workflow list

# Ver runs recentes
gh run list --limit 10

# Ver detalhes da run
gh run view <run-id>

# Ver logs
gh run view <run-id> --log

# Ver logs de job específico
gh run view <run-id> --log --job <job-id>

# Baixar artefatos
gh run download <run-id>

# Baixar artefato específico
gh run download <run-id> --name artifact-name

# Cancelar run
gh run cancel <run-id>

# Re-executar run
gh run rerun <run-id>

# Re-executar apenas jobs falhados
gh run rerun <run-id> --failed

# Executar workflow manualmente
gh workflow run ci-cd-complete.yml --ref main

# Executar com input
gh workflow run ci-cd-complete.yml \
  --ref main \
  --field deploy_environment=staging

# Ver status do workflow
gh workflow view ci-cd-complete.yml

# Ativar workflow
gh workflow enable ci-cd-complete.yml

# Desativar workflow
gh workflow disable ci-cd-complete.yml
```

## 🏷️ Git & Releases

```bash
# Criar tag
git tag -a v1.0.0 -m "Release v1.0.0"

# Push tag
git push origin v1.0.0

# Listar tags
git tag -l

# Deletar tag local
git tag -d v1.0.0

# Deletar tag remota
git push --delete origin v1.0.0

# Criar release
gh release create v1.0.0 \
  --title "Release v1.0.0" \
  --notes "Release notes"

# Criar release com artefatos
gh release create v1.0.0 \
  --title "Release v1.0.0" \
  --notes "Release notes" \
  target/card-shop-*.jar

# Listar releases
gh release list

# Ver release
gh release view v1.0.0

# Deletar release
gh release delete v1.0.0

# Fazer download de release
gh release download v1.0.0
```

## 🧪 Testes

```bash
# Executar aplicação local
./mvnw spring-boot:run

# Com profile específico
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Testes com coverage
./mvnw clean test jacoco:report

# Ver relatório coverage
open target/site/jacoco/index.html

# Testes de integração
./mvnw verify

# Testes com logs debug
./mvnw test -X

# Skip tests
./mvnw install -DskipTests

# Executar teste específico
./mvnw test -Dtest=CardControllerTest

# Executar método específico
./mvnw test -Dtest=CardControllerTest#testAddCard
```

## 🔍 Health Checks

```bash
# Health endpoint
curl http://localhost:8080/actuator/health

# Liveness
curl http://localhost:8080/actuator/health/liveness

# Readiness
curl http://localhost:8080/actuator/health/readiness

# Metrics
curl http://localhost:8080/actuator/metrics

# Info
curl http://localhost:8080/actuator/info

# Prometheus metrics
curl http://localhost:8080/actuator/prometheus

# All endpoints
curl http://localhost:8080/actuator
```

## 🐛 Debugging

```bash
# Ver versão Java
java -version

# Ver versão Maven
./mvnw -version

# Ver versão Docker
docker --version

# Ver versão kubectl
kubectl version --client

# Ver versão gh CLI
gh --version

# Limpar cache Maven
rm -rf ~/.m2/repository

# Limpar target
./mvnw clean

# Limpar Docker cache
docker system prune -a

# Ver uso de disco Docker
docker system df

# Ver logs do sistema (Linux)
sudo journalctl -u docker -f

# Verificar conectividade Kubernetes
kubectl cluster-info

# Verificar nodes
kubectl get nodes

# Verificar namespaces
kubectl get namespaces

# Debug pod
kubectl debug <pod-name> -n card-shop --image=busybox

# Executar comando em pod
kubectl exec -it <pod-name> -n card-shop -- /bin/sh
```

## 📊 Monitoramento

```bash
# Top pods (CPU/Memory)
kubectl top pods -n card-shop

# Top nodes
kubectl top nodes

# Descrição completa
kubectl describe deployment card-shop -n card-shop

# Verificar HPA
kubectl get hpa -n card-shop

# Logs com timestamps
kubectl logs -f <pod-name> -n card-shop --timestamps=true

# Logs últimos N linhas
kubectl logs <pod-name> -n card-shop --tail=100

# Logs desde tempo
kubectl logs <pod-name> -n card-shop --since=1h

# Stream de eventos
kubectl get events -n card-shop --watch
```

## 🔄 CI/CD Operations

```bash
# Status do último workflow
gh run list --limit 1

# Watch run em progresso
gh run watch

# Ver conclusão da run
gh run list --json conclusion \
  --jq '.[] | select(.conclusion=="success") | .url'

# Tempo de execução
gh run list --json durationMs,conclusion \
  --jq '.[] | .durationMs / 60000'

# Taxa de sucesso
gh run list --limit 50 --json conclusion \
  --jq '[.[] | .conclusion] | group_by(.) | 
        map({status: .[0], count: length})'
```

## 📦 Package Management

```bash
# Atualizar dependências
./mvnw versions:display-dependency-updates

# Atualizar plugins
./mvnw versions:display-plugin-updates

# Dependency tree
./mvnw dependency:tree

# Analisar dependências
./mvnw dependency:analyze

# Listar dependências
./mvnw dependency:list

# Verificar CVEs
./mvnw org.owasp:dependency-check-maven:check

# Ver relatório OWASP
open target/dependency-check-report.html
```

## 🌐 Network & Connectivity

```bash
# Test endpoint
curl -I http://localhost:8080

# Test com timeout
curl --max-time 5 http://localhost:8080

# Test POST
curl -X POST http://localhost:8080/add \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","description":"Test","price":10.0}'

# Test em Kubernetes
kubectl run curl --image=curlimages/curl -i --tty --rm \
  -- curl http://card-shop-service.card-shop.svc.cluster.local

# Port forward múltiplas portas
kubectl port-forward <pod-name> 8080:8080 9090:9090 -n card-shop
```

## 🔒 Security Scanning

```bash
# OWASP Dependency Check
./mvnw org.owasp:dependency-check-maven:check

# SpotBugs
./mvnw spotbugs:check

# Trivy scan local
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock \
  aquasec/trivy:latest image card-shop:local

# Trivy filesystem scan
trivy fs .

# Scan Kubernetes manifests
trivy config k8s-deployment.yaml

# OWASP ZAP baseline
docker run -t owasp/zap2docker-stable \
  zap-baseline.py -t http://localhost:8080
```

## 💾 Backup & Restore

```bash
# Backup Kubernetes resources
kubectl get all -n card-shop -o yaml > backup.yaml

# Backup secrets
kubectl get secret -n card-shop -o yaml > secrets-backup.yaml

# Backup configmaps
kubectl get configmap -n card-shop -o yaml > configmap-backup.yaml

# Restore
kubectl apply -f backup.yaml

# Export Docker image
docker save card-shop:local > card-shop.tar

# Import Docker image
docker load < card-shop.tar
```

## 🎯 Quick Fixes

```bash
# Restart deployment
kubectl rollout restart deployment/card-shop -n card-shop

# Limpar recursos não utilizados
kubectl delete pod --field-selector=status.phase==Failed -n card-shop

# Forçar pull de imagem
kubectl set image deployment/card-shop \
  card-shop=ghcr.io/user/card-shop:latest \
  --record -n card-shop
kubectl rollout restart deployment/card-shop -n card-shop

# Rebuild Maven com limpeza total
./mvnw clean install -U

# Resetar Git changes
git reset --hard HEAD
git clean -fd
```

## 📝 Aliases Úteis

Adicione ao seu `~/.bashrc` ou `~/.zshrc`:

```bash
# Maven
alias mci='./mvnw clean install'
alias mcp='./mvnw clean package'
alias mt='./mvnw test'
alias mr='./mvnw spring-boot:run'

# Docker
alias dps='docker ps'
alias dim='docker images'
alias drm='docker rm $(docker ps -aq)'
alias drmi='docker rmi $(docker images -q)'

# Kubernetes
alias k='kubectl'
alias kgp='kubectl get pods'
alias kgs='kubectl get svc'
alias kgd='kubectl get deployments'
alias kdp='kubectl describe pod'
alias kl='kubectl logs -f'

# Git
alias gs='git status'
alias ga='git add .'
alias gc='git commit -m'
alias gp='git push'
alias gl='git log --oneline'

# GitHub CLI
alias ghw='gh workflow view'
alias ghr='gh run list'
alias ghv='gh run view'
```

## 🆘 Emergency Commands

```bash
# Parar tudo
docker stop $(docker ps -aq)

# Remover tudo Docker
docker system prune -a --volumes

# Rollback Kubernetes imediatamente
kubectl rollout undo deployment/card-shop -n card-shop

# Cancelar todos os workflows em execução
for run in $(gh run list --json databaseId -q '.[].databaseId'); do
  gh run cancel $run
done

# Limpar tudo do namespace
kubectl delete namespace card-shop

# Recriar namespace
kubectl create namespace card-shop
kubectl apply -f k8s-deployment.yaml
```

---

## 💡 Dicas

1. Use TAB completion para comandos
2. Adicione aliases para comandos frequentes
3. Use `watch` para monitorar: `watch -n 2 kubectl get pods -n card-shop`
4. Use `tmux` ou `screen` para múltiplas sessões
5. Configure auto-complete do kubectl: `source <(kubectl completion bash)`

---

**Mantenha este arquivo como referência rápida!** 📚

