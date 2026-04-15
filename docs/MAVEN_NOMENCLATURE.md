# Nomenclature Maven - SwiftVisa

## Coordonnees Maven

Les coordonnees suivent le format standard:

- `groupId`: `com.visa`
- `artifactId`: `management`
- `version`: `0.0.1-SNAPSHOT`
- Packaging: `jar` (par defaut Spring Boot)

## Structure Maven standard

- `project/pom.xml`
- `project/src/main/java`
- `project/src/main/resources`
- `project/src/test/java`
- `project/target` (genere)

## Commandes Maven usuelles

Depuis le dossier `project/`:

- Verifier compilation + tests: `mvn clean verify`
- Compiler seulement: `mvn clean compile`
- Construire le jar: `mvn clean package`
- Executer Spring Boot: `mvn spring-boot:run`

## Regles de nommage conseillees

- Packages en minuscules: `com.visa.management...`
- Classes en PascalCase: `PassportController`
- Interfaces metier: `PassportService`
- Implementations: `PassportServiceImpl`

## Gestion des artefacts

Le dossier `target/` ne doit pas etre versionne (deja ignore par `.gitignore`).
