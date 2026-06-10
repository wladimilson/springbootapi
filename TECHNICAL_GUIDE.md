# Guia Técnico de Evolução e Referência - Springboot API 🚀

Este documento serve como uma referência técnica detalhada sobre a evolução recente do projeto, sua arquitetura e diretrizes para reprodução e aprendizado futuro.

---

## 1. Análise da Evolução (Últimos 7 Commits)

O projeto passou por uma modernização significativa, focando em performance, observabilidade e robustez.

### [a96d7ca] e [2890fac] - Modernização para Java 25
*   **O que mudou:** Atualização do JDK para a versão 25 (LTS).
*   **Inovações Técnicas:**
    *   **Virtual Threads:** Ativação do Project Loom para lidar com alto throughput de requisições de forma leve.
    *   **Sequenced Collections:** Uso de interfaces como `SequencedCollection` para garantir ordem previsível nos retornos da API.
    *   **JEP 467:** Adoção de Javadoc em Markdown (`///`) para documentação de código mais legível.
    *   **Docker Multi-stage:** Refatoração do `Dockerfile` para isolar a etapa de testes e gerar uma imagem final minimalista (JRE customizado via `jlink`).

### [20f15f6] - Limpeza e Documentação
*   **O que mudou:** Remoção de entidades não utilizadas (`Usuario`) e refinamento do README.
*   **Impacto:** Melhora na manutenibilidade e redução de ruído no código-fonte.

### [30d0e76] - Observabilidade com Spring Boot Admin
*   **O que mudou:** Integração do servidor e cliente do Spring Boot Admin.
*   **Detalhe de Segurança:** Implementação do `AdminSecurityConfig`, permitindo que a interface administrativa tenha seu próprio fluxo de autenticação (Base/Form Login) separado da API (JWT).

### [dd23b1f] - Robustez e Validação
*   **O que mudou:** Introdução de validações de Bean (`jakarta.validation`).
*   **Impacto:** A entidade `Pessoa` agora valida campos obrigatórios e tamanhos, prevenindo dados inconsistentes no banco de dados. Testes unitários foram expandidos para cobrir cenários de erro (400, 401, 404).

### [4f7094a] - Segurança e SBOM
*   **O que mudou:** Configuração do `cyclonedx-maven-plugin`.
*   **Impacto:** Geração automática do **SBOM (Software Bill of Materials)**, permitindo auditoria de segurança das dependências diretamente no painel do Spring Boot Admin.

---

## 2. Arquitetura do Sistema

A arquitetura segue o padrão de **Clean Architecture** simplificado, com camadas bem definidas para Segurança, Controle, Negócio e Persistência.

```mermaid
graph TD
    subgraph External_World [Mundo Externo]
        Client[Postman / Browser / Newman]
    end

    subgraph Spring_Boot_App [Aplicação Spring Boot 4 / Java 25]
        direction TB
        
        subgraph Security_Layer [Camada de Segurança]
            WebSec[WebSecurityConfig - JWT/RSA]
            AdminSec[AdminSecurityConfig - Form/Basic]
        end

        subgraph Controllers [Camada de Controle]
            AuthCtrl[AuthController - Login]
            PessoaCtrl[PessoaController - CRUD]
        end

        subgraph Services_Repos [Lógica e Dados]
            TokenSvc[TokenAuthenticationService]
            PessoaRepo[PessoaRepository - Spring Data JPA]
        end

        subgraph Monitoring [Monitoramento]
            Actuator[Spring Actuator]
            SBA[Spring Boot Admin Server]
            SBOM[SBOM Generator]
        end
    end

    subgraph Data_Store [Persistência]
        DB[(MariaDB / H2)]
    end

    %% Fluxos
    Client -->|JWT Request| WebSec
    Client -->|Admin UI| AdminSec
    
    WebSec --> AuthCtrl
    WebSec --> PessoaCtrl
    
    AuthCtrl --> TokenSvc
    PessoaCtrl --> PessoaRepo
    PessoaRepo --> DB
    
    AdminSec --> SBA
    SBA --> Actuator
    Actuator --> SBOM
```

---

## 3. Observações para Reprodução Facilitada

Para replicar este ambiente ou usar como base para novos projetos, atente-se aos seguintes pontos:

### Configuração do Ambiente
1.  **JDK 25:** Essencial para suporte às `Virtual Threads` e sintaxe Markdown no Javadoc.
2.  **Chaves RSA:** O projeto utiliza `private-key.pem` e `public-key.pem` em `src/main/resources/certs/` para assinatura de tokens.
3.  **Docker & Compose:** A forma mais rápida de subir o ecossistema completo (App + DB + Admin).

### Passos para Build e Execução
*   **Via Docker (Recomendado):**
    ```bash
    docker-compose up --build
    ```
*   **Via Maven (Desenvolvimento):**
    ```bash
    ./mvnw spring-boot:run
    ```

### Endpoints Chave para Validação
*   **API Swagger:** `http://localhost:8080/swagger-ui/index.html`
*   **Admin Dashboard:** `http://localhost:8080/admin` (admin/admin)
*   **Health Check:** `http://localhost:8080/actuator/health`

---

## 4. Destaques Técnicos de Aprendizado

*   **Jlink no Docker:** Observe no `Dockerfile` como o `jlink` é usado para reduzir a imagem de ~500MB para ~150MB, incluindo apenas os módulos do JDK necessários.
*   **Segurança Híbrida:** O projeto demonstra como configurar múltiplos `SecurityFilterChain` com ordens diferentes (`@Order`) para tratar API e UI administrativa de formas distintas.
*   **Java Records:** O uso de `records` (como em `LoginRequest`) demonstra a adoção de estruturas de dados imutáveis e concisas, típicas do Java moderno.
*   **Segurança Idiomática:** A implementação utiliza `JwtEncoder` e `JwtDecoder` do ecossistema OAuth2 do Spring Security, abandonando bibliotecas legadas e adotando o padrão de mercado para assinatura RSA.
*   **Virtual Threads:** Configuradas via `application.yaml` com `spring.threads.virtual.enabled=true`, permitindo escalar sem a sobrecarga de threads tradicionais do SO.
*   **SBOM:** A integração do CycloneDX com o Spring Boot Admin é uma funcionalidade avançada de segurança que garante visibilidade total da cadeia de suprimentos de software.

---
*Este documento é parte da iniciativa de tornar o código uma referência de excelência técnica.*
