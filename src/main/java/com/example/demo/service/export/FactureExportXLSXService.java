package com.example.demo.service.export;

import com.example.demo.entity.Client;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.FactureRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

@Service
public class FactureExportXLSXService {

    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private ClientRepository clientRepository;

    public void exportUnClient(OutputStream outputSteam, Long id) {
        try {
            // Apache POI (aide à l'adresse : https://poi.apache.org/components/spreadsheet/quick-guide.html)
            // (autre aide : http://www.codeurjava.com/2015/04/lecture-ecriture-dans-un-fichier-excel-apache-poi.html)
            //Création document vide
            Workbook wb = new HSSFWorkbook();

            //Récupération des infos du client
            Optional<Client> client = clientRepository.findById(id);


            //Créer une feuille vide et son titre
            Sheet listeFactureClient = wb.createSheet(client.get().getNom() + client.get().getPrenom());


            //créer une ligne
            Row rowNom = listeFactureClient.createRow(0);
            Row rowPrenom = listeFactureClient.createRow(1);
            Row rowAnnee = listeFactureClient.createRow(2);


            //Création de cellule
            Cell cellNom = rowNom.createCell(0);
            Cell cellPrenom = rowPrenom.createCell(0);
            Cell cellAnnee = rowAnnee.createCell(0);


            //Mettre leur nom
            cellNom.setCellValue("Nom :");
            cellPrenom.setCellValue("Prénom :");
            cellAnnee.setCellValue("Année de Naissance :");


            //Création des cellules pour le client
            Cell cellNomClient = rowNom.createCell(1);
            Cell cellPrenomClient = rowPrenom.createCell(1);



            //Remplissage avec les valeurs client
            cellNomClient.setCellValue(client.get().getNom());
            cellPrenomClient.setCellValue(client.get().getPrenom());







            //Forcer la taille automatique des colonnes
            Sheet sheet = wb.getSheetAt(0);
            sheet.autoSizeColumn(0); // valable uniquement pour la première colonne
            sheet.autoSizeColumn(1);



            //écriture du document excel
            wb.write(outputSteam);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Récupération des infos du client
    public Optional<Client> infoClient(Long id){
        Optional<Client> client = clientRepository.findById(id);
        return client;
    }



}
