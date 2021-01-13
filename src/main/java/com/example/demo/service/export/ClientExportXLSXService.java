package com.example.demo.service.export;

import com.example.demo.entity.Client;
import com.example.demo.repository.ClientRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
            Sheet listClient = wb.createSheet("Liste de client");

            //Créer une ligne et la positionne
            Row row = listClient.createRow(0);

            //Créer une cellule et la positionne
            Cell cell = row.createCell(0);
            Cell cell1 = row.createCell(1);
            Cell cell2 = row.createCell(2);

            //Mettre une "vraie" valeure dans la cellule
            cell.setCellValue("nom");
            cell1.setCellValue("prénom");
            cell2.setCellValue("age");

            //Ajout des Client au fichier
            //Liste client
            List<Client> listClients = clientRepository.findAll();
            int index = 0;

            // pour chaque client
            for (Client client:listClients){
                //on crée une ligne et l'on remplit les cellules
                index += 1;
                Row rowClient = listClient.createRow(index);
                rowClient.createCell(0).setCellValue(client.getNom());
                rowClient.createCell(1).setCellValue(client.getPrenom());
                rowClient.createCell(2).setCellValue(client.calculAge() + " ans");
            }


            //Forcer la taille automatique des colonnes
            Sheet sheet = wb.getSheetAt(0);
            sheet.autoSizeColumn(0); // valable uniquement pour la première colonne

            //écriture du document excel
            wb.write(outputSteam);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
