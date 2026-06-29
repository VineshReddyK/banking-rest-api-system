# Contributing to Banking REST API System

Thanks for your interest in contributing!

## Getting Started

1. Fork the repository and clone your fork
2. Make sure you have Java 21+, Maven 3.9+, MySQL 8+, and Docker installed
3. Set your local DB credentials in `src/main/resources/application.properties`
4. Run the full stack: `docker-compose up -d`
5. Run tests: `mvn test`

## Branching

- Branch from `main`
- Use descriptive branch names: `feat/transfer-limit`, `fix/jwt-expiry`

## Making Changes

- Keep changes focused — one feature or fix per PR
- Add unit tests for any logic changes
- Run `mvn verify` before opening a PR
- If you change an API endpoint, update `docs/api-endpoints.md` too

## Pull Request Guidelines

- Write a clear PR title explaining the **why**, not just the what
- Link related issues with `Closes #<issue>`
- Auth or token-related changes need a note on the security impact

## Code Style

- Standard Java conventions
- All request bodies should use `@Valid` with appropriate annotations
- Don't log passwords, tokens, or account numbers

## Reporting Issues

Open a GitHub Issue with:
- What you expected vs what actually happened
- Steps to reproduce
- Relevant logs (remove any real credentials first)

## Questions

Open a Discussion on GitHub or email vineshreddyy.k@gmail.com.
