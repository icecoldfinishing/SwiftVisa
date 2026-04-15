# Architecture MVC - SwiftVisa

Ce projet suit une organisation Spring Boot MVC stricte avec separation par couche et par domaine metier.

## Package racine

- `com.visa.management`

## Nomenclature retenue

- Couche modele (entites JPA): `model/<domaine>`
- Couche repository (acces donnees): `repository/<domaine>`
- Couche service (logique metier): `service/<domaine>`
- Couche controller (routes MVC): `controller/<domaine>`

## Domaine Passport

- Modele: `com.visa.management.model.passport.Passport`
- Repository: `com.visa.management.repository.passport.PassportRepository`
- Service: `com.visa.management.service.passport.PassportService`
- Service impl: `com.visa.management.service.passport.PassportServiceImpl`
- Controller: `com.visa.management.controller.passport.PassportController`
- Vue Thymeleaf: `src/main/resources/templates/passport/list.html`

## Flux d'affichage

1. Le navigateur appelle `/` ou `/passports`.
2. `PassportController` invoque `PassportService`.
3. `PassportServiceImpl` recupere la liste depuis `PassportRepository`.
4. Le controller injecte la liste dans le modele sous la cle `passports`.
5. Thymeleaf rend la page `passport/list`.

## Bonnes pratiques appliquees

- Noms de classes explicites et suffixes standards (`Repository`, `Service`, `Controller`).
- Un sous-package metier `passport` dans chaque couche.
- Entite JPA mappee sur la table SQL `passport`.
