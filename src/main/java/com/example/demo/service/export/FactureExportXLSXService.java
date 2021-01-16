package com.example.demo.service.export;

import com.example.demo.entity.Client;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.FactureRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
            Sheet listeFactureClient = wb.createSheet(client.get().getNom());


            //Todo


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
