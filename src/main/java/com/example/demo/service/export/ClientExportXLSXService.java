package com.example.demo.service.export;

import com.example.demo.repository.ClientRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;

@Service
public class ClientExportXLSXService {

    @Autowired
    private ClientRepository clientRepository;

    public void export(OutputStream outputSteam) {
        try {
            // Apache POI
            //Création document vide
            Workbook wb = new HSSFWorkbook();
            // TODO

            //Créer une feuille vide et son titre
            Sheet listClient = wb.createSheet("Liste de client");

            //Créer une ligne et la positionner
            Row row = listClient.createRow(0);

            //Créer une cellule et la positionner
            Cell cell = row.createCell(0);
            Cell cell1 = row.createCell(1);

            //Mettre une "vraie" valeure dans la cellule
            cell.setCellValue("test cellule!!!!!!!!!!!!!!!!!!!!!!!!");
            cell1.setCellValue("autre test");


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
