# Relationship Hub API
A Relationship Hub API é uma solução moderna e eficiente para centralizar e otimizar o atendimento ao cliente. Esta API automatiza a distribuição de solicitações para as equipes de atendimento, gerencia filas de espera e monitora a capacidade de atendimento, garantindo uma experiência de suporte ágil e eficaz.

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=souluanf_relationship-hub-api&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=souluanf_relationship-hub-api)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=souluanf_relationship-hub-api&metric=coverage)](https://sonarcloud.io/summary/new_code?id=souluanf_relationship-hub-api)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=souluanf_relationship-hub-api&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=souluanf_relationship-hub-api)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=souluanf_relationship-hub-api&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=souluanf_relationship-hub-api)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=souluanf_relationship-hub-api&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=souluanf_relationship-hub-api)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=souluanf_relationship-hub-api&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=souluanf_relationship-hub-api)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=souluanf_relationship-hub-api&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=souluanf_relationship-hub-api)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=souluanf_relationship-hub-api&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=souluanf_relationship-hub-api)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=souluanf_relationship-hub-api&metric=bugs)](https://sonarcloud.io/summary/new_code?id=souluanf_relationship-hub-api)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=souluanf_relationship-hub-api&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=souluanf_relationship-hub-api)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=souluanf_relationship-hub-api&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=souluanf_relationship-hub-api)


## Sumário

- [Funcionalidades](#funcionalidades)
- [Requisitos](#requisitos)
- [Configuração](#configuração)
- [Acesso ao Banco de Dados](#acesso-ao-banco-de-dados)
  - [Credenciais](#credenciais)
- [Contato](#contato)


## Funcionalidades

- **Distribuição Automatizada de Solicitações:** Encaminha solicitações para as equipes apropriadas.
- **Gerenciamento de Filas de Espera:** Gerencia solicitações durante picos de demanda.
- **Monitoramento de Capacidade de Atendimento:** Limita o número de clientes por atendente.
- **Flexibilidade de Atendentes:** Permite atuação em diferentes categorias conforme necessidade.

## Requisitos

- JDK 17
- Maven 3.6 ou superior


## Configuração

1. **Instalação do JDK e Maven:**
    - [Instruções para instalação do JDK](https://docs.oracle.com/en/java/javase/11/install/overview-jdk-installation.html)
    - [Instruções para instalação do Maven](https://maven.apache.org/install.html)

2. **Executando o Projeto:**
   ```bash
   mvn spring-boot:run

Para acessar a API, utilize as seguinte URL:

- **Swagger UI (Local):** [http://http://localhost:8080](http://http://localhost:8080)
- **Swagger UI (Produção):** [https://supporthub.luanfernandes.dev](https://supporthub.luanfernandes.dev)


## Acesso ao Banco de Dados Local

Para acessar o banco de dados H2 utilizado pelo projeto, utilize a seguintes URL:

- **Console H2 (local):** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)



### Credenciais

|              **URL**               |      **Username**      |      **Password**      |
|:----------------------------------:|:----------------------:|:----------------------:|
| `jdbc:h2:mem:relationship_hub_api` | `relationship_hub_api` | `relationship_hub_api` |

## Observações
- **Spring Boot:** Proporciona desenvolvimento ágil e configuração simplificada, melhorando a eficiência em aplicações Java.
- **CI/CD com GitHub Actions:** Automatiza o deploy, assegurando entregas eficientes e confiáveis.
- **Design Pattern Listener e Event:** Eficiente na gestão de solicitações; alternativas como RabbitMQ ou Kafka são opções futuras para escalabilidade.
- **Testes Unitários e de Integração:** Garantem a funcionalidade e confiabilidade do sistema em todos os níveis.
- **Análise de Qualidade com Jacoco e Sonar:** Assegura código de alta qualidade com 100% de cobertura e sem falhas ou vulnerabilidades.

## Contato
Para suporte ou feedback:

- **Nome:** Luan Fernandes
- **Email:**  [hello@luanfernandes.dev](mailto:hello@luanfernandes.dev)
- **Website:** [https://luanfernandes.dev](https://luanfernandes.dev)
- **LinkedIn:** [https://www.linkedin.com/in/souluanf](https://www.linkedin.com/in/souluanf)
