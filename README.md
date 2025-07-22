
# Itaú Insurance Policy Service API

Microsserviço desenvolvido para gerenciar solicitações de apólices de seguro com arquitetura orientada a eventos (Event-Driven Architecture). A aplicação integra com uma API externa de fraudes (mock), utiliza Kafka para mensageria e PostgreSQL para persistência de dados.

---

## ⚙️ Tecnologias Utilizadas

- Java 17 + Spring Boot 3
- PostgreSQL
- Apache Kafka (Bitnami KRaft)
- Kafka UI
- WireMock (API de fraudes mockada)
- Docker e Docker Compose
- MapStruct
- JUnit + Mockito
- Clean Architecture
- Design Patterns (Strategy, Factory, Mapper)
- Observabilidade via logs estruturados

---

## 🚀 Como Executar o Projeto

### Pré-requisitos

- Docker e Docker Compose instalados
- Arquivo `.env` criado na raiz com os seguintes valores:

```
POSTGRES_DB=policy_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=123

DB_HOST=postgres
DB_PORT=5432
DB_USERNAME=postgres
DB_PASSWORD=123
```

> O arquivo `.env` é obrigatório para inicializar todos os containers corretamente.

### Passo a passo

```bash
docker-compose up --build -d
```

Esse comando irá subir os seguintes serviços:

| Serviço     | Porta | Descrição                                 |
|-------------|-------|-------------------------------------------|
| PostgreSQL  | 5432  | Banco de dados                            |
| Kafka       | 9092  | Broker real com KRaft (Bitnami)           |
| Kafka UI    | 8081  | Interface web para tópicos Kafka          |
| WireMock    | 8089  | Simulação da API externa de fraudes       |
| App (API)   | 8080  | Microsserviço de apólices                 |

Acesse a interface Kafka UI em:  
👉 [`http://localhost:8081/ui/`](http://localhost:8081/ui/)

---

## 📮 Endpoints da API

As collections do Postman foram preparadas com variáveis de ambiente e estão na raiz do projeto.

Os cenários cobrem:

- Criação de apólice com risco simulado
- Consulta por ID
- Cancelamento
- Reprocessamento de eventos
- Gatilho de eventos Kafka simulando pagamento e autorização

---

## 🔄 Fluxo de Negócio

1. Cliente realiza solicitação de apólice via `POST /api/insurance-policies`
2. API envia os dados para a API de Fraudes (mock via WireMock)
3. O retorno é analisado por uma estratégia que aplica regras baseadas na classificação de risco
4. A própria aplicação, ao consumir eventos de Kafka (simulando pagamento/autorização), altera a apólice alterando o status para `APPROVED` ou como `REJECTED`

---

## 📦 Decisões de Implementação

### Clean Architecture

O projeto foi estruturado seguindo Clean Architecture:

- `domain`: entidades e enums do núcleo de negócio
- `application`: casos de uso e lógica de orquestração
- `infrastructure`: Kafka, banco de dados, client externo
- `adapters.controllers`: API REST

### MapStruct

Para garantir separação entre DTOs, entidades de persistência e domínio, foi adotado o MapStruct com `componentModel = "spring"`.

### Design Patterns

- **Strategy**: validações baseadas em classificação de risco
- **Factory**: produção de estratégias
- **Mapper**: transformação entre camadas

### Observabilidade

Os logs foram padronizados em inglês, com contexto relevante para rastreamento de eventos e falhas.

---

## 🧪 Testes

- Testes unitários cobrindo regras de negócio nos `UseCases`
- Teste de integração com MockMvc simulando o fluxo completo de criação
- Estrutura pronta para expansão com novos testes
- Cobertura parcial intencional, priorizando o core do negócio

---

## 🧠 Premissas Assumidas

### Uso do `riskKey`

Como a especificação da API de fraude não definia como controlar a classificação de risco nas respostas, foi assumida a seguinte premissa:

> Um campo adicional chamado `riskKey` (de 1 a 4) foi adicionado ao DTO de entrada.

Esse campo **não é persistido no banco nem no domínio**, servindo apenas para que o WireMock retorne diferentes classificações conforme abaixo:

| riskKey | Classificação de Risco |
|---------|------------------------|
| 1       | HIGH_RISK              |
| 2       | REGULAR                |
| 3       | PREFERENTIAL           |
| 4       | NO_INFORMATION         |

Isso garantiu controle total nos testes dos fluxos de aprovação e rejeição.

---

## ⚠️ Dificuldades Enfrentadas

- **Notebook pessoal com desempenho muito limitado**, travando ao rodar Docker, IDE e build Maven simultaneamente
- **Trabalho presencial 4x por semana**, dificultando a dedicação durante os dias úteis
- Por essas restrições, **priorizei o essencial**: entregar uma arquitetura bem definida, lógica clara e sistema funcional rodando via `docker-compose`

---

## ✍️ Considerações Finais

A experiência foi extremamente valiosa. Desenvolver esse projeto mesmo com restrições técnicas e de tempo me ensinou muito desde arquitetura limpa até integração com mensageria e simulação de APIs externas.

Fico muito empolgado com a possibilidade de contribuir com o time do Itaú.
