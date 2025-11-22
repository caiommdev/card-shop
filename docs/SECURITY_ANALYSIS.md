# 🔒 Análise de Segurança SAST/DAST - Requisito do Projeto

## 📋 Requisitos Obrigatórios

Para entrega do projeto, é **obrigatório** ter:

✅ **SAST** (Static Application Security Testing)
✅ **DAST** (Dynamic Application Security Testing)  
✅ **Testes automatizados pós-deploy**

---

## 🛡️ Ferramentas Implementadas

### SAST - Análise Estática

#### 1. OWASP Dependency Check
**O que faz**: Verifica vulnerabilidades em dependências Maven

**Execução**:
```yaml
Job: sast-owasp
Tempo: ~5-7 minutos
Quando: Após build, em paralelo com CodeQL
```

**Localmente**:
```bash
mvn org.owasp:dependency-check-maven:check
open target/dependency-check-report.html
```

**Relatório**: 
- `owasp-report/` nos artifacts do GitHub Actions
- Falha build se CVSS >= 7

#### 2. CodeQL (GitHub Advanced Security)
**O que faz**: Análise semântica de código para vulnerabilidades

**Execução**:
```yaml
Job: sast-codeql
Tempo: ~5-10 minutos
Quando: Após build, em paralelo com OWASP
```

**Queries**: `security-and-quality`

**Resultados**: GitHub Security → Code scanning alerts

**Nota**: Para repositórios privados, requer GitHub Advanced Security (pago). Para repositórios públicos é **grátis**.

---

### DAST - Análise Dinâmica

#### 1. OWASP ZAP
**O que faz**: Testa a aplicação em execução procurando vulnerabilidades

**Execução**:
```yaml
Job: dast-zap
Tempo: ~8-12 minutos
Quando: Após deploy em staging
```

**Testes realizados**:
- ✅ XSS (Cross-Site Scripting)
- ✅ SQL Injection
- ✅ CSRF (Cross-Site Request Forgery)
- ✅ Security Headers
- ✅ Cookie Security
- ✅ Path Traversal

**Configuração**: `.zap/rules.tsv`

**Relatório**: 
- `zap-scan-report/report_html.html` nos artifacts

**Localmente** (opcional):
```bash
docker run -t owasp/zap2docker-stable \
  zap-baseline.py -t http://localhost:8080
```

---

### Testes Pós-Deploy

#### Selenium E2E Tests
**O que faz**: Testa a aplicação completa após deploy

**Execução**:
```yaml
Job: selenium-tests
Tempo: ~5-8 minutos
Quando: Após deploy em staging, antes de DAST
```

**Testes**:
1. `testAddCard()` - Adicionar card
2. `testEditCard()` - Editar card
3. `testDeleteCard()` - Deletar card

**Ambiente**:
- PostgreSQL em service container
- App em service container
- Firefox headless

---

## 🔄 Fluxo Completo

```
Push em Main
  ↓
Build & Unit Tests (3-5 min)
  ↓
[Paralelo]
├── SAST: OWASP Dependency Check (5-7 min)
└── SAST: CodeQL Analysis (5-10 min)
  ↓
Docker Build & Push (3-5 min)
  ↓
Deploy Staging (1-2 min)
  ↓
[Paralelo]
├── Selenium E2E Tests (5-8 min)
└── DAST: OWASP ZAP Scan (8-12 min)
  ↓
⏸️ Aguarda Aprovação Manual
  ↓
Deploy Production (1-2 min)

TOTAL: ~20-30 minutos (até aprovação)
```

---

## 📊 Pull Request vs Push Main

### Pull Request (Validação)
```
✅ Build & Unit Tests
✅ SAST: OWASP Dependency Check
✅ SAST: CodeQL Analysis
❌ Não faz Docker build
❌ Não faz deploy
❌ Não roda Selenium
❌ Não roda DAST
```

### Push Main (Pipeline Completo)
```
✅ Build & Unit Tests
✅ SAST: OWASP + CodeQL
✅ Docker Build & Push
✅ Deploy Staging
✅ Selenium E2E Tests
✅ DAST: OWASP ZAP
✅ Deploy Production (com aprovação)
```

---

## 📈 Requisitos Atendidos

| Requisito | Status | Ferramenta |
|-----------|--------|------------|
| **Análise Estática (SAST)** | ✅ | OWASP Dependency Check |
| **Análise Estática (SAST)** | ✅ | CodeQL |
| **Análise Dinâmica (DAST)** | ✅ | OWASP ZAP |
| **Testes Pós-Deploy** | ✅ | Selenium E2E |
| **Detecção de Vulnerabilidades** | ✅ | Todas as ferramentas |
| **Relatórios de Segurança** | ✅ | Artifacts do GitHub |

---

## 🔍 O que Cada Ferramenta Detecta

### OWASP Dependency Check (SAST)
```
✅ Vulnerabilidades conhecidas (CVEs)
✅ Dependências desatualizadas
✅ Bibliotecas com problemas de segurança
✅ Versões vulneráveis
```

**Exemplo de detecção**:
```
CVE-2024-12345 - Spring Framework RCE
CVSS Score: 9.8 (Critical)
Affected: spring-core 6.1.10
Fix: Upgrade to 6.1.11
```

### CodeQL (SAST)
```
✅ SQL Injection
✅ XSS (Cross-Site Scripting)
✅ Path Traversal
✅ Command Injection
✅ Sensitive Data Exposure
✅ Insecure Deserialization
✅ Weak Cryptography
```

**Exemplo de detecção**:
```
SQL Injection vulnerability
Location: CardService.java:42
Severity: High
User input flows to SQL query without sanitization
```

### OWASP ZAP (DAST)
```
✅ XSS (Reflected & Stored)
✅ SQL Injection
✅ CSRF
✅ Broken Authentication
✅ Security Misconfigurations
✅ Missing Security Headers
✅ Insecure Cookies
✅ Directory Traversal
```

**Exemplo de detecção**:
```
Missing Security Header: X-Content-Type-Options
URL: http://localhost:8080/
Risk: Low
Recommendation: Add X-Content-Type-Options: nosniff
```

---

## 📦 Artifacts Gerados

Todos disponíveis em **GitHub Actions → Workflow Run → Artifacts**:

1. **app-jar** (30 dias)
   - JAR da aplicação

2. **owasp-report** (30 dias)
   - `dependency-check-report.html`
   - Lista de CVEs encontrados

3. **selenium-results** (30 dias)
   - Relatórios JUnit dos testes E2E

4. **zap-scan-report** (30 dias)
   - `report_html.html`
   - Vulnerabilidades encontradas por ZAP

**CodeQL**: Resultados em **GitHub Security → Code scanning**

---

## 🚀 Como Usar

### Ver Resultados de Segurança

#### OWASP Dependency Check
```
1. GitHub Actions → Workflow run
2. Artifacts → owasp-report
3. Download e abra dependency-check-report.html
```

#### CodeQL
```
1. GitHub → Security tab
2. Code scanning
3. Ver alertas por severidade
```

#### OWASP ZAP
```
1. GitHub Actions → Workflow run
2. Artifacts → zap-scan-report
3. Download e abra report_html.html
```

### Executar Localmente

#### OWASP Dependency Check
```bash
mvn org.owasp:dependency-check-maven:check
open target/dependency-check-report.html
```

#### Selenium Tests
```bash
# Iniciar app
docker-compose up -d

# Executar testes
mvn test -Dtest=*Selenium*
```

#### OWASP ZAP (Docker)
```bash
# Iniciar app
docker-compose up -d

# Executar ZAP
docker run -t owasp/zap2docker-stable \
  zap-baseline.py \
  -t http://host.docker.internal:8080 \
  -r zap-report.html
```

---

## ⚙️ Configuração

### Suprimir Falsos Positivos (OWASP)

Edite `dependency-check-suppressions.xml`:

```xml
<suppress>
    <notes>
        False positive - CVE não aplicável ao nosso uso
    </notes>
    <gav regex="true">^org\.example:.*$</gav>
    <cve>CVE-2024-12345</cve>
</suppress>
```

### Ajustar Regras ZAP

Edite `.zap/rules.tsv`:

```
# Ignorar alerta específico
IGNORE	0	10049	Content Security Policy Header Not Set

# Falhar em vulnerabilidade crítica
FAIL	3	40012	Cross Site Scripting (Reflected)
```

### CodeQL Queries

Customizar em `.github/workflows/ci-cd.yml`:

```yaml
- name: Initialize CodeQL
  uses: github/codeql-action/init@v3
  with:
    languages: java
    queries: security-and-quality  # ou security-extended
```

---

## 🎯 Conformidade com Requisitos

### ✅ SAST Implementado
- [x] OWASP Dependency Check
- [x] CodeQL Analysis
- [x] Execução automática no CI
- [x] Relatórios disponíveis

### ✅ DAST Implementado
- [x] OWASP ZAP Baseline Scan
- [x] Testes na aplicação rodando
- [x] Execução pós-deploy
- [x] Relatórios disponíveis

### ✅ Testes Pós-Deploy
- [x] Selenium E2E Tests
- [x] Testes após deploy staging
- [x] Service containers (PostgreSQL + App)
- [x] Resultados disponíveis

---

## 📚 Referências

- [OWASP Dependency Check](https://owasp.org/www-project-dependency-check/)
- [CodeQL Documentation](https://codeql.github.com/docs/)
- [OWASP ZAP](https://www.zaproxy.org/)
- [Selenium WebDriver](https://www.selenium.dev/)

---

## ✅ Checklist de Entrega

Para validar conformidade com requisitos:

- [x] Pipeline executa SAST automaticamente
- [x] Pipeline executa DAST automaticamente
- [x] Testes pós-deploy implementados
- [x] Relatórios de segurança gerados
- [x] Vulnerabilidades detectadas e reportadas
- [x] Artifacts disponíveis para auditoria
- [x] Documentação completa

**Status**: ✅ **TODOS OS REQUISITOS ATENDIDOS**

---

**🔒 Segurança completa implementada e documentada!**

