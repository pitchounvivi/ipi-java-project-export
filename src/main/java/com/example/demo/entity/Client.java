package com.example.demo.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity représentant un client.
 */
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String nom;

    @Column
    private String prenom;

    @OneToMany(mappedBy = "client")
    private Set<Facture> factures = new HashSet<>();

    @Column
    private LocalDate dateNaissance;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Set<Facture> getFactures() {
        return factures;
    }

    public void setFactures(Set<Facture> factures) {
        this.factures = factures;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    //Calcul age
    public Integer calculAge (){
        // Cette écriture permet de calculer l'âge exacte en fonction de la date du jour actuelle
        return Period.between(this.getDateNaissance(), LocalDate.now()).getYears();
    }


}


