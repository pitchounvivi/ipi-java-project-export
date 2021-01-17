package com.example.demo.service.export;

import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.FactureRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

@Service
public class FactureExportXLSXService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private FactureRepository factureRepository;


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


            ////////////////////////////////////FEUILLE de facture/////////////////////////////////////////////
            //Pour chaque facture on crée une nouvelle feuille
            for (Facture facture : client.get().getFactures()){
                //Créer une feuille vide et son titre
                Sheet factureClient = wb.createSheet("Facture N° "+ facture.getId());
            }











            ////////////////Création du style pour la cellule facture////////////////
            //Création d'un nouveau font
            Font font = wb.createFont();

            //Formatage du font
            font.setBold(true); // en gras

            //Création du style pour l'entête du tableau
            CellStyle styleFacture = wb.createCellStyle();

            //Ajout du font à styleFacture
            styleFacture.setFont(font);

            //Application à la cellule facture
            cellFacture.setCellStyle(styleFacture);

            /////////////////////////////////////////////////////////////////////////



            //Forcer la taille automatique des colonnes
            Sheet sheet = wb.getSheetAt(0); // Feuille 1
            sheet.autoSizeColumn(0); // valable uniquement pour la première colonne
            sheet.autoSizeColumn(1);



            //écriture du document excel
            wb.write(outputSteam);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
