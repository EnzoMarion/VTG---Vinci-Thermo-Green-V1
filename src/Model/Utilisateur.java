package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Utilisateur {
    private Map<String, String> utilisateurs;

    public Utilisateur(String cheminFichierUtilisateurs) {
        utilisateurs = new HashMap<>();
        chargerUtilisateurs(cheminFichierUtilisateurs);
    }

    private void chargerUtilisateurs(String cheminFichierUtilisateurs) {
        try (BufferedReader br = new BufferedReader(new FileReader(cheminFichierUtilisateurs))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] parts = ligne.split(",");
                if (parts.length == 3) {
                    String nomUtilisateur = parts[0].trim();
                    String motDePasse = parts[1].trim();
                    String chemin = parts[2].trim();
                    utilisateurs.put(nomUtilisateur, motDePasse + "," + chemin);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean verifierConnexion(String nomUtilisateur, String motDePasse) {
        String utilisateurEnregistre = utilisateurs.get(nomUtilisateur);
        if (utilisateurEnregistre != null) {
            String[] infosUtilisateur = utilisateurEnregistre.split(",");
            if (infosUtilisateur.length == 2) {
                String motDePasseEnregistre = infosUtilisateur[0].trim();
                String chemin = infosUtilisateur[1].trim();
                return motDePasse.equals(motDePasseEnregistre);
            }
        }
        return false;
    }

    public String getCheminUtilisateur(String nomUtilisateur) {
        String utilisateurEnregistre = utilisateurs.get(nomUtilisateur);
        if (utilisateurEnregistre != null) {
            String[] infosUtilisateur = utilisateurEnregistre.split(",");
            if (infosUtilisateur.length == 2) {
                return infosUtilisateur[1].trim();
            }
        }
        return null;
    }
}
