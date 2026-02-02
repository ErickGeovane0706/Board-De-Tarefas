# 📋 Board de Tarefas API

API robusta para gerenciamento de fluxo Kanban, desenvolvida com **Java 17** e **Spring Boot**. O sistema oferece controle total sobre quadros e cards, com validações de segurança e regras de negócio integradas.

---

## 🏗️ Infraestrutura e Tecnologias

* **Linguagem:** Java 17
* **Framework:** Spring Boot 3
* **Banco de Dados:** MySQL 8.0 rodando em container **Docker**
* **Persistência:** Spring Data JPA / Hibernate
* **Documentação:** Swagger UI (OpenAPI 3)

---

## 🚀 Como Executar

1.  **Subir o Banco de Dados:** Na raiz do projeto, execute:
    ```bash
    docker-compose up -d
    ```
2.  **Acessar Documentação:** Com a aplicação rodando, acesse o Swagger:
    `http://localhost:8081/swagger-ui/index.html`

---

## 🧪 Roteiro de Testes (JSONs Prontos)

### 1. Criar um Quadro (Board)
* **Endpoint:** `POST /boards`
```json
🏗️ 1. Gerenciamento de Quadros (Boards)
Criar Novo Quadro
POST /boards

Descrição: Cria o board e gera automaticamente as 4 colunas iniciais.

JSON

{
  "name": "Sprint de Desenvolvimento Janeiro"
}
Atualizar Nome do Quadro
PUT /boards/{id}

Descrição: Altera apenas o título do board sem afetar as colunas.

JSON

{
  "name": "Sprint de Desenvolvimento - Versão Final"
}
🏛️ 2. Gerenciamento de Colunas (Board Columns)
Adicionar Coluna Personalizada
POST /columns

Descrição: Adiciona uma coluna extra (ex: REVISÃO) em um board existente.

JSON

{
  "name": "REVISÃO",
  "columnOrder": 2,
  "kind": "PENDING",
  "board": { "id": 5 }
}
🗂️ 3. Gerenciamento de Cards (Ciclo de Vida)
Criar Card (Apenas em colunas INITIAL)
POST /cards

Descrição: Vincula uma nova tarefa à coluna inicial (ex: ID 15).

JSON

{
  "title": "Configurar Docker Compose",
  "description": "Criar arquivo para subir MySQL e a aplicação",
  "boardColumn": { "id": 15 }
}
Bloquear Card (Obrigatório Motivo)
PUT /cards/{id}

Descrição: Ativa a trava de segurança. O campo blockedReason não pode ser nulo.

JSON

{
  "title": "Configurar Docker Compose",
  "description": "Criar arquivo para subir MySQL e a aplicação",
  "blocked": true,
  "blockedReason": "Aguardando definição das variáveis de ambiente"
}
Mover Card (Regra de Sequência)
PATCH /cards/{id}/mover?novaColunaId={id}

Descrição: Move o card para a próxima ordem (Ex: 0 -> 1) ou para o Cancelamento.

Atenção: Se o card estiver bloqueado, este endpoint retornará erro 422.

Atualizar Dados do Card (Desbloquear)
PUT /cards/{id}

Descrição: Altera informações ou desbloqueia o card (limpa o motivo automaticamente).

JSON

{
  "title": "Configurar Docker Compose (Finalizado)",
  "description": "Arquivo compose.yaml criado com sucesso",
  "blocked": false
}
🗑️ 4. Remoção de Dados
DELETE /cards/{id}: Remove um card específico.

DELETE /columns/{id}: Remove uma coluna (Cuidado: remove os cards vinculados).
```
🛡️ Regras de Negócio Implementadas
Validação de Início: Cards só podem ser criados em colunas de estado inicial.

Fluxo Sequencial: Impede que cards "pulem" etapas do processo Kanban.

Trava de Segurança: Cards bloqueados não podem ser movidos.

Recursividade Zero: Uso de @JsonManagedReference e @JsonBackReference.


---
