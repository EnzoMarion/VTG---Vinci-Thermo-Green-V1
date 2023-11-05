-- Créer la table "utilisateurs"
CREATE TABLE utilisateurs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom_utilisateur VARCHAR(255) NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    chemin VARCHAR(255) NOT NULL
);

-- Créer la table "mesure"
CREATE TABLE mesure (
    id INT AUTO_INCREMENT PRIMARY KEY,
    zone VARCHAR(255) NOT NULL,
    date_heure DATETIME NOT NULL,
    temperature DECIMAL(5, 2) NOT NULL
);
