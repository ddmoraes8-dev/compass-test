# Gerenciamento de Pautas e Votação

Este projeto é uma API RESTful para gerenciar pautas, usuários (associados), votos e sessões de votação. Ele permite que usuários cadastrem pautas, abram sessões de votação para elas, registrem votos e consultem os resultados.

## Visão Geral

A aplicação é dividida nas seguintes funcionalidades principais:

* **Pautas**: Criação e listagem de pautas para votação.
* **Usuários (Associados)**: Cadastro de usuários que podem participar das votações.
* **Votos**: Registro de votos "Sim" ou "Não" para pautas em sessões abertas, e consulta de resultados.
* **Sessões**: Abertura e controle de sessões de votação para cada pauta, com duração configurável.

## Tecnologias Utilizadas

* **Java**: Linguagem de programação principal.
* **Spring Boot**: Framework para construção de aplicações Spring de forma rápida e eficiente.
* **Spring Data JPA**: Para interação com o banco de dados.
* **Maven**: Ferramenta de automação de build e gerenciamento de dependências.
* **H2 Database (ou outro DB configurado)**: Banco de dados em memória para desenvolvimento/testes (pode ser configurado para PostgreSQL, MySQL, etc. em produção).
* **Lombok**: Para reduzir boilerplate code (getters, setters, builders, etc.).
* **Swagger/OpenAPI**: Para documentação interativa da API.

## Como Rodar a Aplicação

### Pré-requisitos

* JDK 17 ou superior
* Maven

### Passos

1.  **Clone o Repositório:**
    ```bash
    git clone [https://github.com/ddmoraes8-dev/compass-test]
    cd [pautas]
    ```

2.  **Construa o Projeto:**
    ```bash
    mvn clean install
    ```

3.  **Execute a Aplicação:**
    ```bash
    mvn spring-boot:run
    ```
    A aplicação estará disponível em `http://localhost:8080` (ou na porta configurada em `src/main/resources/application.properties`).

## Endpoints da API (Swagger UI)

Após iniciar a aplicação, você pode acessar a documentação interativa da API através do Swagger UI no seu navegador:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

Ou, se você usa a URL padrão do OpenAPI para acesso programático:

[http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### Endpoints Principais

A seguir, uma lista dos principais endpoints disponíveis:

#### Pauta
* `GET /api/pautas`
    * **Descrição**: Lista todas as pautas cadastradas.
* `POST /api/pautas`
    * **Descrição**: Cria uma nova pauta.
    * **Corpo da Requisição (JSON)**: `{"nome": "Nome da Pauta"}`
* `GET /api/pautas/{id}`
    * **Descrição**: Busca uma pauta específica pelo seu ID.
    * **Parâmetros de Path**: `{id}` (ID da pauta)

#### Usuário
* `POST /api/usuarios`
    * **Descrição**: Cria um novo usuário (associado).
    * **Corpo da Requisição (JSON)**: `{"nome": "Nome do Usuário", "cpf": "12345678900"}`

#### Voto
* `POST /api/votos`
    * **Descrição**: Registra um voto em uma pauta.
    * **Parâmetros de Query**:
        * `pautaId`: ID da pauta (Long)
        * `cpf`: CPF do associado (String)
        * `voto`: Voto do associado (`true` para SIM, `false` para NÃO) (Boolean)
* `GET /api/votos/resultado`
    * **Descrição**: Consulta o resultado dos votos de uma pauta.
    * **Parâmetros de Query**:
        * `pautaId`: ID da pauta (Long)

#### Sessão
* `POST /api/sessoes/abrir`
    * **Descrição**: Abre uma sessão de votação para uma pauta.
    * **Parâmetros de Query**:
        * `pautaId`: ID da pauta (Long)
        * `duracaoEmMinutos`: Duração da sessão em minutos (Integer, opcional, padrão: 1 minuto).
* `GET /api/sessoes/status`
    * **Descrição**: Verifica se a sessão de votação para uma pauta está aberta no momento.
    * **Parâmetros de Query**:
        * `pautaId`: ID da pauta (Long)

## Contribuição

Contribuições são sempre bem-vindas! Se você encontrar um bug, tiver uma ideia para uma nova funcionalidade ou quiser melhorar o código, por favor:

1.  Faça um fork deste repositório.
2.  Crie uma nova branch (`git checkout -b feature/nome-da-feature` ou `bugfix/nome-do-bug`).
3.  Faça suas alterações e adicione testes, se aplicável.
4.  Commit suas mudanças (`git commit -m 'feat: Adiciona nova funcionalidade X'`).
5.  Envie suas mudanças para o repositório remoto (`git push origin feature/nome-da-feature`).
6.  Abra um Pull Request descrevendo suas alterações.