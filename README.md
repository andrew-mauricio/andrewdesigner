# Andrew Mauricio — Portfólio

Site pessoal oficial de Andrew Mauricio em Java 25, Quarkus, Qute, HTMX e CSS compartilhado.

## Stack

- Java 25
- Quarkus 3.27
- Qute
- HTMX 2
- CSS compartilhado
- Docker Compose
- Caddy como proxy reverso

## Desenvolvimento

```bash
mvn quarkus:dev
```

O site abre em `http://localhost:8080`.

## Produção

```bash
docker compose up -d --build
docker compose ps
curl -fsS http://127.0.0.1:8188/q/health
```

O container publica apenas em `127.0.0.1:8188`. O domínio público deve apontar para essa porta por meio do Caddy. Um bloco pronto está em `Caddyfile.example`.

## Dados do GitHub

O endpoint `/fragments/github` busca os repositórios públicos reais de `andrew-mauricio`, mantém cache de 10 minutos e entrega um fragmento Qute carregado por HTMX. Se o GitHub estiver indisponível, o layout mostra links de fallback sem inventar estatísticas.
