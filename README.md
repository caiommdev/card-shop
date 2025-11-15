# Manual de Execução

Este manual descreve como executar a aplicação, como funcionam os workflows de CI/CD com o GitHub Actions e as principais mudanças realizadas durante a refatoração do projeto.

## Como Rodar a Aplicação Integrada

Para executar a aplicação localmente, você precisará ter o Java 17 e o Maven instalados em sua máquina.

1.  **Clone o repositório:**
    ```bash
    git clone <url-do-repositorio>
    cd card-shop
    ```

2.  **Execute a aplicação com o Maven:**
    Abra um terminal na raiz do projeto e execute o seguinte comando:
    ```bash
    mvn spring-boot:run
    ```
    A aplicação será iniciada e estará acessível em [http://localhost:8080](http://localhost:8080).

3.  **Execute os testes:**
    Para rodar a suíte de testes, incluindo os testes de interface com Selenium, execute o comando:
    ```bash
    mvn test
    ```

## Workflows do GitHub Actions

O projeto está configurado com um workflow de Integração Contínua (CI) usando o GitHub Actions.

### Como Funciona

O workflow está definido no arquivo `.github/workflows/maven.yml`. Ele é acionado automaticamente nos seguintes eventos:
-   `push`: Sempre que um novo commit é enviado para a branch `main`.
-   `pull_request`: Sempre que uma pull request é aberta ou atualizada para a branch `main`.
-   `workflow_dispatch`: Permite a execução manual do workflow a partir da aba "Actions" no GitHub.

O workflow executa um job chamado `build` em um ambiente `ubuntu-latest` e realiza os seguintes passos:
1.  **Checkout:** Clona o código do repositório.
2.  **Set up JDK 17:** Configura o ambiente com Java 17.
3.  **Build with Maven:** Compila o projeto e empacota a aplicação usando o comando `mvn -B package --file pom.xml`.
4.  **Run tests:** Executa todos os testes automatizados com o comando `mvn test`.

### Como Interpretar os Resultados

1.  Navegue até o seu repositório no GitHub e clique na aba **Actions**.
2.  Você verá uma lista de todas as execuções do workflow.
3.  Um ícone de **check verde** (✅) indica que o workflow foi executado com sucesso (build e testes passaram).
4.  Um ícone de **X vermelho** (❌) indica que houve uma falha em algum dos passos. Você pode clicar na execução para ver os logs detalhados e identificar o erro.

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

