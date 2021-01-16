package com.example.demo.service;

import com.example.demo.entity.Article;
import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.entity.LigneFacture;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;

/**
 * Classe permettant d'insérer des données dans l'application.
 */
@Service
@Transactional
public class InitData implements ApplicationListener<ApplicationReadyEvent> {

    private EntityManager entityManager;

    public InitData(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        insertTestData();
    }

    private void insertTestData() {
        Article a1 = createArticle("Chargeurs de téléphones Portables", 22.98, 9, "Chargeurs de téléphones Portables; 22.98; Samsung EP-P1100. Type de chargeur: Intérieur; " +
                "Type de source d'alimentation: Secteur; " +
                "Compatibilité de chargeur: Smartphone; " +
                "Charge rapide. Couleur du produit: Noir");
        Article a2 = createArticle("Playmobil Hydravion de Police", 14.39, 2, "L'intérieur de l'avion peut contenir deux personnages et une valise.");
        Article a3 = createArticle("Distributeur de croquettes pour chien", 12.99, 0, "Distributeur de nourriture croquettes, biscuits ou snacks pour chats et chiens Plastique robuste avec couvercle amovible");

        Client cl1 = createClient("John", "Doe", LocalDate.parse("2000-08-02"));
        Client cl2 = createClient("Jane", "Doe", LocalDate.parse("2000-01-02"));


        Facture f1 = createFacture(cl1);
        Facture f2 = createFacture(cl2);


        /*LigneFacture lf1 = createLigneFacture(a1, 2, f1);
        LigneFacture lf2 = createLigneFacture(a2, 1, f1);
        LigneFacture lf3 = createLigneFacture(a3, 1, f2);
        LigneFacture Lf4 = createLigneFacture(a2, 3, f3);*/


    }

    private Client createClient(String prenom, String nom, LocalDate dateNaissance) {
        Client client = new Client();
        client.setPrenom(prenom);
        client.setNom(nom);
        client.setDateNaissance(dateNaissance);
        entityManager.persist(client);
        return client;
    }

    private Article createArticle(String libelle, double prix, int stock, String description) {
        Article a1 = new Article();
        a1.setLibelle(libelle);
        a1.setPrix(prix);
        a1.setStock(stock);
        a1.setDescription(description);
        entityManager.persist(a1);
        return a1;
    }

    private Facture createFacture(Client client){
        Facture facture = new Facture();
        facture.setClient(client);
        entityManager.persist(facture);
        return facture;
    }


    /*private LigneFacture createLigneFacture(Article article, int quantite, Facture facture){
        LigneFacture ligneFacture = new LigneFacture();
        ligneFacture.setArticle(article);
        ligneFacture.setQuantite(quantite);
        ligneFacture.setFacture(facture);
        entityManager.persist(ligneFacture);
        return ligneFacture;
    }*/

}
