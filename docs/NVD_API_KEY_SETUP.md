# Configuração da NVD API Key para OWASP Dependency Check

## Problema

O OWASP Dependency Check precisa acessar o National Vulnerability Database (NVD) para obter informações sobre vulnerabilidades. Sem uma API key, você pode encontrar:

- **Erro 403 (Forbidden)**: Requisições bloqueadas
- **Rate Limiting**: Limite muito restritivo de requisições
- **Demora extrema**: Atualizações podem levar horas

## Solução Rápida (Projeto Atual)

O projeto foi configurado para **não falhar** quando não há API key disponível:

```bash
# Executar localmente (usará dados em cache se disponíveis)
mvn org.owasp:dependency-check-maven:check -DfailOnError=false
```

## Solução Recomendada: Obter NVD API Key (GRÁTIS)

### Passo 1: Solicitar API Key

1. Acesse: https://nvd.nist.gov/developers/request-an-api-key
2. Preencha o formulário com seu email
3. Confirme o email
4. Você receberá a API key por email (geralmente em minutos)

### Passo 2: Configurar Localmente

#### Opção A: Variável de Ambiente (Recomendado)

**Linux/macOS:**
```bash
# Adicione ao seu ~/.bashrc ou ~/.zshrc
export NVD_API_KEY="sua-api-key-aqui"

# Recarregue o shell
source ~/.bashrc
```

**Windows (PowerShell):**
```powershell
# Adicione ao seu perfil do PowerShell
$env:NVD_API_KEY = "sua-api-key-aqui"

# Para permanente (como administrador):
[System.Environment]::SetEnvironmentVariable('NVD_API_KEY', 'sua-api-key-aqui', 'User')
```

**Windows (CMD):**
```cmd
setx NVD_API_KEY "sua-api-key-aqui"
```

#### Opção B: Arquivo .env (Não commitar!)

Crie um arquivo `.env` na raiz do projeto:

```properties
NVD_API_KEY=sua-api-key-aqui
```

**IMPORTANTE:** Este arquivo já está no `.gitignore`, não o commite!

### Passo 3: Configurar no GitHub Actions

1. Acesse seu repositório no GitHub
2. Vá em **Settings** → **Secrets and variables** → **Actions**
3. Clique em **New repository secret**
4. Nome: `NVD_API_KEY`
5. Valor: cole sua API key
6. Clique em **Add secret**

## Testando a Configuração

### Teste Local

```bash
# Verificar se a variável está configurada
echo $NVD_API_KEY  # Linux/macOS
echo %NVD_API_KEY%  # Windows CMD
$env:NVD_API_KEY   # Windows PowerShell

# Executar o OWASP Dependency Check
mvn clean org.owasp:dependency-check-maven:check
```

### Teste no GitHub Actions

Faça um commit e push. O workflow `ci-cd.yml` agora:
- ✅ Usará a API key se configurada (rápido e confiável)
- ✅ Usará cache para evitar downloads repetidos
- ✅ Não falhará o build se não conseguir atualizar

## Configurações no pom.xml

O `pom.xml` foi configurado com:

```xml
<configuration>
    <!-- Usar API key da variável de ambiente -->
    <nvdApiKeyEnvironmentVariable>NVD_API_KEY</nvdApiKeyEnvironmentVariable>
    
    <!-- Não falhar se não conseguir atualizar -->
    <failOnError>false</failOnError>
    
    <!-- Cache local por 4 horas -->
    <cveValidForHours>4</cveValidForHours>
    
    <!-- Delay entre requisições (respeitar rate limit) -->
    <nvdApiDelay>3000</nvdApiDelay>
    
    <!-- Falhar apenas em vulnerabilidades críticas (CVSS >= 7) -->
    <failBuildOnCVSS>7</failBuildOnCVSS>
</configuration>
```

## Alternativas sem API Key

Se você não pode obter uma API key, o projeto ainda funcionará:

### 1. Usar Dados em Cache
```bash
# Primeira execução demorará, mas criará cache local
mvn clean org.owasp:dependency-check-maven:check -DfailOnError=false
```

### 2. Modo Offline (após ter cache)
```bash
# Não tentará atualizar, usará apenas dados locais
mvn org.owasp:dependency-check-maven:check \
  -DfailOnError=false \
  -DautoUpdate=false
```

### 3. GitHub Actions com Cache
O workflow já está configurado para usar cache entre execuções, então:
- Primeira execução pode falhar/demorar
- Execuções subsequentes usarão dados em cache

## Benefícios da API Key

| Sem API Key | Com API Key |
|-------------|-------------|
| 🐌 Muito lento (horas) | ⚡ Rápido (minutos) |
| ❌ Frequentes erros 403 | ✅ Requisições confiáveis |
| ⏱️ 5 requisições por 30s | ⚡ 50 requisições por 30s |
| 😓 Pode falhar em CI/CD | ✅ Confiável em pipelines |

## Troubleshooting

### Erro: "NVD Returned Status Code: 403"
- ✅ **Já resolvido**: O projeto não falhará mais por esse erro
- Configure API key para melhor experiência
- Use cache local para evitar requisições frequentes

### Erro: "One or more exceptions occurred during analysis"
- ✅ **Já resolvido**: `failOnError=false` configurado
- O relatório será gerado mesmo com erros de atualização

### Build muito lento
- Configure a API key (acelera 10x+)
- Use cache do Maven: `mvn ... -Dmaven.repo.local=~/.m2/repository`

## Comandos Úteis

```bash
# Limpar cache do OWASP
rm -rf ~/.m2/repository/org/owasp/dependency-check-data

# Forçar atualização completa
mvn dependency-check:purge
mvn org.owasp:dependency-check-maven:check

# Ver apenas vulnerabilidades críticas
mvn org.owasp:dependency-check-maven:check -DfailBuildOnCVSS=9

# Gerar apenas relatório (não falhar build)
mvn org.owasp:dependency-check-maven:check \
  -DfailBuildOnCVSS=11 \
  -DfailOnError=false
```

## Links Úteis

- 📝 Solicitar API Key: https://nvd.nist.gov/developers/request-an-api-key
- 📚 Documentação OWASP: https://jeremylong.github.io/DependencyCheck/
- 🔧 Plugin Maven: https://jeremylong.github.io/DependencyCheck/dependency-check-maven/

## Status Atual do Projeto

✅ **Configurado para não falhar** sem API key  
✅ **Cache habilitado** no GitHub Actions  
✅ **Relatórios sempre gerados** (mesmo com erros de atualização)  
⚠️ **API key opcional** mas altamente recomendada  

---

**Próximos passos recomendados:**

1. ✅ Testar build local: `mvn clean verify -DfailOnError=false`
2. 🔑 Obter NVD API key (grátis, leva 5 minutos)
3. 📝 Configurar secret no GitHub
4. 🚀 Push para testar CI/CD completo

