package com.example.demo.service.export;

import com.example.demo.entity.Client;
import com.example.demo.repository.ClientRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public class ClientExportXLSXService {

    @Autowired
    private ClientRepository clientRepository;

    public void export(OutputStream outputSteam) {
        try {
            // Apache POI (aide à l'adresse : https://poi.apache.org/components/spreadsheet/quick-guide.html)
            // (autre aide : http://www.codeurjava.com/2015/04/lecture-ecriture-dans-un-fichier-excel-apache-poi.html)
            //Création document vide
            Workbook wb = new HSSFWorkbook();

            //Créer une feuille vide et son titre
            Sheet listeDeClient = wb.createSheet("Liste de client");

            //Créer une ligne et la positionne
            Row rowTitle = listeDeClient.createRow(0);

            //Créer une cellule et la positionne
            Cell cell = rowTitle.createCell(0);
            Cell cell1 = rowTitle.createCell(1);
            Cell cell2 = rowTitle.createCell(2);

            //Mettre une "vraie" valeur dans la cellule
            cell.setCellValue("Nom");
            cell1.setCellValue("Prénom");
            cell2.setCellValue("Age");


            //Ajout des Clients au fichier
            //Liste client
            List<Client> listClients = clientRepository.findAll();
            int index = 0;

            // pour chaque client
            for (Client client:listClients){
                //on crée une ligne et l'on remplit les cellules
                index += 1;
                Row rowClient = listeDeClient.createRow(index);
                rowClient.createCell(0).setCellValue(client.getNom());
                rowClient.createCell(1).setCellValue(client.getPrenom());
                rowClient.createCell(2).setCellValue(client.calculAge() + " ans");

            }

            //Forcer la taille automatique des colonnes
            Sheet sheet = wb.getSheetAt(0);
            sheet.autoSizeColumn(0); // valable uniquement pour la première colonne




            //Formatage des bordures des cellules////////////////////////////////
            //Création des nouveaux styles
            CellStyle styleHead = wb.createCellStyle();
            CellStyle styleBody = wb.createCellStyle();

            //Formatage du styleBody
            styleBody.setBorderTop(BorderStyle.THICK);
            styleBody.setBorderBottom(BorderStyle.THICK);
            styleBody.setBorderLeft(BorderStyle.THICK);
            styleBody.setBorderRight(BorderStyle.THICK);
            styleBody.setTopBorderColor(IndexedColors.BLUE.getIndex());
            styleBody.setBottomBorderColor(IndexedColors.BLUE.getIndex());
            styleBody.setLeftBorderColor(IndexedColors.BLUE.getIndex());
            styleBody.setRightBorderColor(IndexedColors.BLUE.getIndex());
            /////////////////////////////////////////////////////

            //Création d'un nouveau font
            Font font = wb.createFont();

            //Formatage du font
            font.setBold(true); // en gras
            font.setColor(IndexedColors.PINK.getIndex()); // en rose

            //Formatage styleHead
            styleBody.setFont(font);

            //On applique le formatage à la première ligne seulement
            //rowTitle.getRowStyle().setFont(styleHead);



            //On parcourt toutes les lignes remplies de la feuille
            for (Row ligne : listeDeClient){

                //On parcourt les colonnes
                for (Cell cellule : ligne){

                    //On l'applique le style aux cellules
                    cellule.setCellStyle(styleBody);
                }
            }
            /////////////////////////////////////////////////////



            //écriture du document excel
            wb.write(outputSteam);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
