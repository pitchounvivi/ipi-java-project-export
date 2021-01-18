package com.example.demo.service.export;

import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.entity.LigneFacture;
import com.example.demo.repository.ClientRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class FactureExportXLSXService {

    @Autowired
    private ClientRepository clientRepository;


    /**
     * Méthode pour exporter un client en excel
     * @param outputSteam
     * @param id
     */
    public void exportUnClient(OutputStream outputSteam, Long id) {
        try {
            // Apache POI (aide à l'adresse : https://poi.apache.org/components/spreadsheet/quick-guide.html)
            // (autre aide : http://www.codeurjava.com/2015/04/lecture-ecriture-dans-un-fichier-excel-apache-poi.html)
            //Création document vide
            Workbook wb = new HSSFWorkbook();

            //Récupération des infos du client
            Optional<Client> client = clientRepository.findById(id);

            int index = 0; //initialisation du compteur de cellule

            ///////////////////////////FEUILLE Client///////////////////////////////////////////
            //Créer une feuille vide et son titre
            Sheet listeFactureClient = wb.createSheet(client.get().getNom() +" "+ client.get().getPrenom());


            //créer une ligne
            Row rowNom = listeFactureClient.createRow(0);
            Row rowPrenom = listeFactureClient.createRow(1);
            Row rowAnnee = listeFactureClient.createRow(2);
            Row rowFacture = listeFactureClient.createRow(3);


            //Création des cellules de la première colonne
            Cell cellNom = rowNom.createCell(0);
            Cell cellPrenom = rowPrenom.createCell(0);
            Cell cellAnnee = rowAnnee.createCell(0);
            Cell cellFacture = rowFacture.createCell(0);


            //Création des cellules pour le client (colonne 2)
            Cell cellNomClient = rowNom.createCell(1);
            Cell cellPrenomClient = rowPrenom.createCell(1);
            Cell cellAnneeClient = rowAnnee.createCell(1);


            //Remplissage des cellules client avec les valeurs client
            cellNomClient.setCellValue(client.get().getNom());
            cellPrenomClient.setCellValue(client.get().getPrenom());
            cellAnneeClient.setCellValue(client.get().getDateNaissance().getYear());

            //Remplissage des cellules factures avec les numéros(id) des factures
            for (Facture facture : client.get().getFactures()){
                index += 1;
                rowFacture.createCell(index).setCellValue(facture.getId());
            }


            //Remplissage de la première colonne avec le nom des lignes
            cellNom.setCellValue("Nom :");
            cellPrenom.setCellValue("Prénom :");
            cellAnnee.setCellValue("Année de Naissance :");
            cellFacture.setCellValue(index +" Facture(s) :");

            /////////////////////////////////////FIN FEUILLE Client/////////////////////////////////////////////

            //Méthode pour créer un onglet par facture client
            exportUneFacture(outputSteam, wb, client);



            ////////////////Création du style pour la cellule facture////////////////
            //Création d'un nouveau font
            Font font = wb.createFont();

            //Formatage du font
            font.setBold(true); // en gras

            //Création du style gras
            CellStyle styleGras = wb.createCellStyle();

            //Ajout du font à styleGras
            styleGras.setFont(font);

            //Application à la cellule facture
            cellFacture.setCellStyle(styleGras);

            /////////////////////////////////////////////////////////////////////////


            //Forcer la taille automatique des colonnes
            Sheet sheet = wb.getSheetAt(0); // Feuille Client
            sheet.autoSizeColumn(0); // valable uniquement pour la première colonne
            sheet.autoSizeColumn(1);


            //écriture du document excel
            wb.write(outputSteam);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Méthode pour avoir un onglet facture du client en excel
     * @param outputSteam
     * @param wb
     * @param client
     */
    public void exportUneFacture(OutputStream outputSteam, Workbook wb, Optional<Client> client) {
        try {
            //Pour chaque facture on crée un nouvel onglet
            int indexFacture = 0; //initialisation du compteur de feuille facture
            for (Facture facture : client.get().getFactures()){
                //incrémentation index pour itérer sur chaque facture
                indexFacture += 1;

                //Créer une feuille vide et son titre
                Sheet factureClient = wb.createSheet("Facture N° "+ facture.getId());

                //Création ligne en-tête
                Row rowEnTete = factureClient.createRow(0);

                //Création des cellules en-tête
                Cell cellDesignation = rowEnTete.createCell(0);
                Cell cellQuantite = rowEnTete.createCell(1);
                Cell cellPrixUnitaire = rowEnTete.createCell(2);

                //Remplissage avec le nom des colonnes
                cellDesignation.setCellValue("Désignation");
                cellQuantite.setCellValue("Quantité :");
                cellPrixUnitaire.setCellValue("Prix unitaire :");


                //Récupération des lignes de chaque facture
                int indexLigne = 0; //initialisation
                Double calculFacture = 0d;
                for (LigneFacture ligneFacture : facture.getLigneFactures()){
                    //incrémentation index pour itérer sur chaque ligne de la facture
                    indexLigne += 1;

                    Row rowLigneFacture = factureClient.createRow(indexLigne);
                    rowLigneFacture.createCell(0).setCellValue(ligneFacture.getArticle().getLibelle());
                    rowLigneFacture.createCell(1).setCellValue(ligneFacture.getQuantite());
                    rowLigneFacture.createCell(2).setCellValue(ligneFacture.getArticle().getPrix());

                    //Calcul pour la somme total de la facture
                    calculFacture += ligneFacture.getQuantite() * ligneFacture.getArticle().getPrix();
                }


                //Création de la ligne de total
                Row rowTotal = factureClient.createRow(indexLigne+1);
                Cell cellTotal = rowTotal.createCell(0);
                cellTotal.setCellValue("Total : ");


                //Création de la cellule du calcul total
                Cell cellCalcul = rowTotal.createCell(2);
                cellCalcul.setCellValue(calculFacture);


                //////////////////////////////Création Style des cellules //////////////////////////
                //Création d'une Map pour regrouper les futures propriétés
                Map<String, Object> properties = new HashMap<String, Object>();

                //Création des propriétés d'alignement dans la cellule
                properties.put(CellUtil.ALIGNMENT, HorizontalAlignment.RIGHT);


                ///////////////Création d'un nouveau font////////////////////
                Font font = wb.createFont();

                //Formatage du font
                font.setBold(true); // en gras

                //Création du style pour l'entête du tableau
                CellStyle styleBold = wb.createCellStyle();

                //Ajout du font à styleBold
                styleBold.setFont(font);

                ////////////////////////////////////////////////////////////

                //////////////////////Fusion de la cellule totale/////////////////
                factureClient.addMergedRegion(new CellRangeAddress(
                        indexLigne+1,
                        indexLigne+1,
                        0,
                        1));

                ////////////////////////////////////////////////////////////////////
                //Application des différents styles

                //On applique le formatage du font aux cellules de la première ligne seulement
                for (Cell cell : rowEnTete){
                    cell.setCellStyle(styleBold);
                }

                //Application à la cellule total
                cellTotal.setCellStyle(styleBold);

                //Application à la cellule totale
                CellUtil.setCellStyleProperties(cellTotal, properties);

                ////////////////////////////////////////////////////////////////////

                //Taille automatique des colonnes
                Sheet sheetFacture = wb.getSheetAt(indexFacture);
                sheetFacture.autoSizeColumn(0); // valable uniquement pour la première colonne
                sheetFacture.autoSizeColumn(1);
                sheetFacture.autoSizeColumn(2);
            }


            //écriture du document excel
            wb.write(outputSteam);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
