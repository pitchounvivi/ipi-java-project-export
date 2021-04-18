package com.example.demo.service.export;

import com.example.demo.entity.Client;
import com.example.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.List;

@Service
public class ClientExportCSVService {

    @Autowired
    private ClientRepository clientRepository;


    public void export(PrintWriter writer) {
        List<Client> listClients = clientRepository.findAll();
        writer.println("nom;prenom;date;age");
        for(Client client:listClients){
            writer.println(client.getNom()+";"+client.getPrenom()+";"+client.getDateNaissance()+";"+client.calculAge() + " ans");
        }
    }
}
