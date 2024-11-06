INSERT INTO enfant (`prenom`, `nom`, `sexe`, `date_naiss`)
VALUES ("Xenos", "Marchand", "F", "2012-10-12"),
       ("Jerome", "Lachapelle", "F", "2020-02-24"),
       ("Kieran", "Lane", "M", "2019-04-12"),
       ("Chancellor", "Koopman", "F", "2018-05-05"),
       ("Byron", "Aarle", "M", "2017-06-06"),
       ("Marc", "Tropvieux", "M", "1999-07-04"),
       ("Pol", "Tropjeune", "M", "2021-07-04"),
       ("Reece", "Hagen", "M", "2016-06-25"),
       ("Georgia", "Baardwijk", "F", "2015-07-13"),
       ("Ralph", "Chastain", "F", "2015-07-22"),
       ("Stuart", "Aaldenberg", "M", "2013-10-26"),
       ("Martin", "Jonker", "M", "2012-06-19"),
       ("Carlos", "Cousineau", "M", "2011-07-15"),
       ("Richard", "Klein", "M", "2010-07-31"),
       ("Chastity", "Haak", "M", "2009-09-08"),
       ("Griffith", "Berger", "F", "2008-04-18"),
       ("Asher", "Van Der Aart", "F", "2007-11-12"),
       ("Josiah", "Tailler", "F", "2006-01-02"),
       ("Josiah", "Bouwmeester", "M", "2006-07-04"),
       ("Jacques", "Tropjeune", "M", "2023-07-04");


INSERT INTO stage (`denom`, `age_min`, `age_max`, `date_deb`, `date_fin`, `prix`)
VALUES ("Natation", 3, 8, "2024-01-20", "2024-04-21", 10),
       ("Hockey", 9, 13, "2024-06-10", "2024-10-09", 5),
       ("Danse", 4, 16, "2024-02-27", "2024-02-28", 30),
       ("Equitation", 6, 10, "2024-01-12", "2024-02-13", 20),
       ("Chant", 3, 4, "2024-03-17", "2024-04-12", 30),
       ("Byronseratropvieux", 6, 7, "2025-06-06", "2025-12-21", 15),
       ("Byronestok", 7, 7, "2024-06-06", "2024-06-10", 15),
       ("Entretroisetdixhuit", 3, 18, "2024-03-14", "2024-12-21", 15),
       ("Handball", 15, 18, "2024-01-29", "2024-03-13", 15),
       ("Basketball", 12, 16, "2024-05-12", "2024-06-11", 10),
       ("Tennis", 10, 14, "2024-01-15", "2024-07-14", 20),
       ("Gymnastique", 8, 12, "2024-02-18", "2024-03-17", 15),
       ("Velo", 12, 13, "2024-01-02", "2024-07-24", 10),
       ("Programmation", 12, 14, "2024-04-24", "2024-05-23", 20),
       ("Football", 9, 14, "2024-10-14", "2024-12-21", 15);

INSERT INTO inscription (`enfant_id`, `paye`, `stage_id`)
VALUES (8, 1, 3),
       (8, 1, 1),
       (12, 0, 12),
       (11, 1, 4),
       (14, 0, 10),
       (15, 1, 10),
       (16, 0, 9),
       (17, 1, 3),
       (14, 0, 3),
       (2, 1, 5);
