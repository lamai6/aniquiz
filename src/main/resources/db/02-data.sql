-- -----------------------------------------------------
-- Data for table `aniquiz`.`contributor`
-- -----------------------------------------------------
START TRANSACTION;
USE `aniquiz`;
INSERT INTO `aniquiz`.`contributor` (`id`, `username`, `email`, `password`, `roles`, `created_at`) VALUES (1, 'admin', 'admin@aniquiz.fr', '$2a$10$qA4lVR1NsHqVa2AuDgH9HOicEvoGAe5Q0tpLxg3dghWLqQyfJYoKO', 'ADMIN,CONTRIBUTOR', '2022-12-16 22:08:46');

COMMIT;
