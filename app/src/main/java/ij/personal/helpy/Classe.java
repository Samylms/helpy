package ij.personal.helpy;


import java.util.List;

public class Classe {

    int id;
    int annee;
    String libelle;


    //variable pour retourner les objets. /!\ NE PAS AJOUTER AU CONSTRUCTEUR OU AUX MODIFICATEUR/ACCESSEURS
    private List<Classe> allClasse;

    public Classe() {
    }

    ;

    public Classe(int id, int annee, String libelle) {
        this.id = id;
        this.annee = annee;
        this.libelle = libelle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    //Methode pour retourner la liste des classes
    public List<Classe> getAllClasse() throws Exception {

        return allClasse;
    }
}
