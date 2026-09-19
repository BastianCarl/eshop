```mermaid
flowchart TD
    Client["Web Client<br/>"]

    App["Spring Boot"]

    Redis[("Redis<br/>")]
    Postgres[("PostgreSQL<br/>")]

    Client -->|HTTP| App
    App --> Redis
    App -->|JDBC| Postgres
```