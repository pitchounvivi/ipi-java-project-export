package com.example.demo.service.export;

import com.example.demo.entity.Article;
import com.example.demo.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.List;

@Service
public class ArticleExportCVSService {

    @Autowired
    private ArticleRepository articleRepository;

    public void export(PrintWriter writer) {
        List<Article> listArticles = articleRepository.findAll();

        //Ecriture en-tête des colonnes
        writer.println("Libelle;Prix;Description;");

        for (Article article:listArticles){
            //on vérifie si la description à une ; et on l'échappe pour permettre son affichage dans la même cellule que le texte
            writer.println(String.format("\"%s\";\"%s\";\"%s\"", article.getLibelle(), article.getPrix(), article.getDescription()));
        }


    }


}
