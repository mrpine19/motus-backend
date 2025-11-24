# Motus.IA API (Backend)

Esta é a API central que orquestra a lógica de negócios, persistência de dados e integração com IA para a plataforma de educação adaptativa e gamificada Motus.IA.

## Funcionalidades Chave

*   **Gestão de Alunos e Turmas (CRUD):** Gerenciamento completo de alunos e turmas.
*   **Motor de Gamificação:** Sistema de pontuação, streaks e ranking para engajamento dos alunos.
*   **Nivelamento Adaptativo:** Integração com IA/Python para nivelamento dinâmico dos alunos.
*   **Dashboards Analíticos:** Dashboards para mentoria e análise de ESG/ROI Social.

## Tecnologias Utilizadas

*   **Java 17+**
*   **Quarkus**
*   **Maven**
*   **Hibernate ORM with Panache**
*   **Oracle Database**
*   **RESTEasy Reactive**

## Configuração e Execução

### Pré-requisitos

*   **JDK 17+**
*   **Maven**
*   **Banco de Dados Oracle** rodando e acessível.

### Configuração de Ambiente

Configure o arquivo `src/main/resources/application.properties` com as credenciais do seu banco de dados:

```properties
quarkus.datasource.db-kind=oracle
quarkus.datasource.username=seu usuário
quarkus.datasource.password=sua senha
quarkus.datasource.jdbc.url=jdbc:oracle:thin:@//oracle.fiap.com.br:1521/orcl
```

### Comandos

1.  **Instalar dependências:**
    ```bash
    mvn clean install
    ```

2.  **Rodar em modo desenvolvimento (Hot Reload):**
    ```bash
    mvn quarkus:dev
    ```

3.  **Gerar build (JAR):**
    ```bash
    mvn package
    ```

## Documentação da API

| Recurso             | Verbo HTTP | URI (Path)                                                 | Descrição                                       | Status Code                                       |
| ------------------- | ---------- | ---------------------------------------------------------- | ----------------------------------------------- | ------------------------------------------------- |
| **Alunos**          | `POST`     | `/alunos`                                                  | Cria um novo aluno.                             | `201 Created`, `404 Not Found`                    |
|                     | `PUT`      | `/alunos/{id}`                                             | Atualiza um aluno existente.                    | `200 OK`                                          |
|                     | `DELETE`   | `/alunos/{id}`                                             | Inativa um aluno.                               | `204 No Content`                                  |
|                     | `POST`     | `/alunos/{alunoId}/ajustar-nivel/voluntario/{voluntarioId}` | Ajusta o nível de um aluno manualmente.         | `200 OK`                                          |
|                     | `GET`      | `/alunos`                                                  | Lista todos os alunos.                          | `200 OK`                                          |
| **Dashboard**       | `GET`      | `/dashboard/voluntario/{idVoluntario}`                     | Retorna os dados do dashboard de um voluntário. | `200 OK`, `500 Internal Server Error`             |
| **Voluntários**     | `GET`      | `/voluntarios/nomes`                                       | Lista os nomes de todos os voluntários.         | `200 OK`                                          |
| **Turmas**          | `GET`      | `/turmas`                                                  | Lista todas as turmas.                          | `200 OK`                                          |
|                     | `POST`     | `/turmas`                                                  | Adiciona uma nova turma.                        | `200 OK`, `404 Not Found`                         |
| **Desafios**        | `GET`      | `/desafios`                                                | Lista todos os desafios.                        | `200 OK`                                          |
|                     | `GET`      | `/desafios/{id}`                                           | Busca um desafio pelo ID.                       | `200 OK`, `404 Not Found`                         |
|                     | `POST`     | `/desafios`                                                | Cria novos desafios.                            | `201 Created`                                     |

## Arquitetura

O projeto segue uma arquitetura em camadas (Resource -> Service -> Repository -> Database) inspirada em Domain-Driven Design (DDD), promovendo uma clara separação de responsabilidades e facilitando a manutenção e escalabilidade do sistema.
