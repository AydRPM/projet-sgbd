# Projet SGBD : Stages - Ayden Mortier

## Description
Travail de fin de module pour le cours de projet SGBD.

## Enoncé
Faire un petit projet du même type que le projet Employee.

Fonctionnalités à prévoir :

- CRUD des 3 tables.
- Dans la liste des inscriptions, il faut afficher aussi la dénomination du stage et le id-nom-prénom de l’enfant.
- Pour ajouter une nouvelle inscription, il faut pouvoir choisir la dénomination du stage et le id-nom-prénom de l’enfant via des combobox.
- Pouvoir filtrer les inscriptions sur le nom du stage et sur le fait que ce soit payé ou non.
- Vérifier bien la validité des données entrées par l'utilisateur (uniquement des lettres pour les champs alphabétiques, uniquement des chiffres pour les champs numériques, dates valides...).
- Pour la suppression d’un enfant ou d’un stage, demander d’abord une confirmation (fonction confirm en javascript). Un enfant/un stage ne peut être supprimé que s’il n’est lié à aucune inscription (sinon message d’erreur).
- Règles de gestion à implémenter : 
  - Au moment d’entrer une nouvelle inscription, il faut vérifier que l’enfant a l’âge requis pour le stage. Il faut également vérifier que la date de début du stage est postérieure ou égale à la date d’aujourd’hui.
  - Si on essaie d’entrer une autre inscription pour un enfant qui est déjà inscrit à ce stage, il faut afficher un message d’erreur (cf contrainte d’unicité).
  - A la création d’un nouveau stage, il faut vérifier que la date de fin soit postérieure à la date de début, que les âges soient compris entre 3 et 18, et que l’âge max soit plus grand que l’âge min.

## Notes Importantes
- Le diagramme de classe de l'enoncé mentionne l'attribut prix mais celui-ci n'est pas repris dans le dictionnaire de données.
Il a été implémenté.

## Contact
[Ayden Mortier](mailto:student@arpm.dev)