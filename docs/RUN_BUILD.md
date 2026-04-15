# Run et Build - Spring Boot avec Maven

## 1. Prerequis

- Java 17 installe
- Maven installe (`mvn -v`)
- PostgreSQL accessible

## 2. Initialiser la base de donnees

Le script SQL est dans:

- `project/src/main/resources/db/00_init.sql`

Ce script contient des commandes `DROP/CREATE DATABASE` et `\c` (connexion psql), donc il doit etre execute via `psql`, pas via l'initialisation JDBC Spring.

Exemple:

```bash
psql -U postgres -f src/main/resources/db/00_init.sql
```

## 3. Variables de configuration

Le fichier `project/src/main/resources/application.properties` accepte:

- `DB_HOST`, `DB_PORT`, `DB_NAME`
- `DB_USERNAME`, `DB_PASSWORD`
- ou les variables Render (`PGHOST`, `PGPORT`, `PGDATABASE`, `PGUSER`, `PGPASSWORD`)

## 4. Lancer l'application

Depuis `project/`:

```bash
mvn spring-boot:run
```

Application disponible sur:

- `http://localhost:8088/`
- `http://localhost:8088/passports`

## 5. Construire le projet

Depuis `project/`:

```bash
mvn clean package
```

Le jar est genere dans `project/target/`.

## 6. Verifier la qualite

```bash
mvn clean verify
```

Cette commande compile, execute les tests, puis valide le build.
