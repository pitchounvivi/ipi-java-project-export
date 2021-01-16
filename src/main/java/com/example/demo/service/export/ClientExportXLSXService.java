package com.example.demo.service.export;

import com.example.demo.entity.Client;
import com.example.demo.repository.ClientRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


            //Créer une ligne d'en tête et la positionne
            Row rowTitle = listeDeClient.createRow(0);

            //Créer une cellule et la positionne
            Cell cell0 = rowTitle.createCell(0);
            Cell cell1 = rowTitle.createCell(1);
            Cell cell2 = rowTitle.createCell(2);

            //Mettre une "vraie" valeur dans la cellule
            cell0.setCellValue("Nom");
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


            //Zone de création des stytes des cellules////////////////////////////////
            //Création d'une Map pour regrouper les futures propriétés
            Map<String, Object> properties = new HashMap<String, Object>();

            //Création des propriétés des bordures des cellules
            properties.put(CellUtil.BORDER_TOP, BorderStyle.THICK);
            properties.put(CellUtil.BORDER_BOTTOM, BorderStyle.THICK);
            properties.put(CellUtil.BORDER_LEFT, BorderStyle.THICK);
            properties.put(CellUtil.BORDER_RIGHT, BorderStyle.THICK);

            //Création de la propriété couleurs de la bordure
            properties.put(CellUtil.TOP_BORDER_COLOR, IndexedColors.BLUE.getIndex());
            properties.put(CellUtil.BOTTOM_BORDER_COLOR, IndexedColors.BLUE.getIndex());
            properties.put(CellUtil.LEFT_BORDER_COLOR, IndexedColors.BLUE.getIndex());
            properties.put(CellUtil.RIGHT_BORDER_COLOR, IndexedColors.BLUE.getIndex());

            //////////////////////////////
            //Création d'un nouveau font
            Font font = wb.createFont();

            //Formatage du font
            font.setBold(true); // en gras
            font.setColor(IndexedColors.PINK.getIndex()); // en rose

            //Création du style pour l'entête du tableau
            CellStyle styleHead = wb.createCellStyle();

            //Ajout du font à styleHead
            styleHead.setFont(font);

            /////////////////////////////////////////////////////////////////////

            //On applique le formatage du font aux cellules de la première ligne seulement
            for (Cell cell : rowTitle){
                cell.setCellStyle(styleHead);
            }


            //On applique le formatage des bordures à toutes les cellules
            //Itérer sur toutes les cellules utilisées du document
            for (Row row : listeDeClient){
                for (Cell cell : row){
                    //On l'applique le style aux cellules
                    CellUtil.setCellStyleProperties(cell, properties);
                }
            }


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

}
