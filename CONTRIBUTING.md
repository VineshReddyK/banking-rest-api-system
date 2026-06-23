# Contributing to Banking REST API System

Thanks for your interest in contributing!

## Getting Started

1. Fork the repository and clone your fork
2. Ensure you have Java 17+, Maven 3.9+, MySQL 8+, and Docker installed
3. Copy `.env.example` to `.env` and configure your local database credentials
4. Run the full stack: `docker-compose up -d`
5. Run tests: `mvn test`

## Branching

- Branch from `main`
- Use descriptive branch names: `feat/transfer-limit`, `fix/jwt-expiry`, `chore/upgrade-jjwt`

## Making Changes

- Keep changes focused — one feature or fix per PR
- Add unit and integration tests for any logic changes
- Run `mvn verify` before opening a PR
- Ensure Swagger docs stay accurate for any API changes (`/swagger-ui.html`)

## Pull Request Guidelines

- Write a clear PR title and description explaining the **why**
- Link related issues with `Closes #<issue>`
- Security-sensitive changes (auth, token handling, input validation) require extra scrutiny — describe the threat model in your PR description

## Code Style

- Follow standard Java conventions
- All request bodies must use `@Valid` with appropriate validation annotations
- Never log sensitive data (passwords, tokens, account numbers)

## Reporting Issues

Open a GitHub Issue with:
- Clear title and description
- Steps to reproduce (for bugs)
- Expected vs actual behavior
- Relevant logs (sanitized — no real credentials)

## Questions

Reach out via [email](mailto:vineshreddyy.k@gmail.com) or open a Discussion on GitHub.
