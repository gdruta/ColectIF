/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import dao.JpaUtil;
import util.Saisie;
import metier.modele.*;
import dao.*;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import metier.service.ServiceMetier;
import static metier.service.ServiceMetier.*;
import java.util.List;




/**
 *
 * @author epetit
 */
public class Main {
    
    static Adherent saisieA(){
        
        System.out.println("Hello");
        String prenom = Saisie.lireChaine("Entrez votre prenom: ");
        String nom = Saisie.lireChaine("Entrez votre nom: ");
        String mail = Saisie.lireChaine("Entrez votre mail: ");
        String adresse = Saisie.lireChaine("Entrez votre adresse: ");
        Adherent a= new Adherent(nom, prenom, mail.toLowerCase(), adresse);
         
        return a;
    }
    
    static public void CreerAdherent(Adherent ad){

        ServiceMetier.CreerAdherent(ad);
    }
    
    static public void testDemandes(){
        
        Adherent ad = ServiceMetier.getAdherentByMail("romain.mie@free.fr");
        Adherent ad2 = ServiceMetier.getAdherentByMail("rgoguollot@gmail.com");
        System.out.println(ad);
        System.out.println(ad2);
        
        Activite ac = ServiceMetier.getActiviteByID(27);
        System.out.println(ac);
        Demande d1 = new Demande(ad, ac ,new GregorianCalendar(2017,3,21) , "soir");
        Demande d2 = new Demande(ad2, ac ,new GregorianCalendar(2017,3,21) , "soir");
        
        // ajouter aux methodes DAO des recherches avec facteur, demande de doublons, toute les demandes avec activité x, toute les demandes d'adherent, d'une date...
        
        
        ServiceMetier.PersisterDemande(d1);
        ServiceMetier.PersisterDemande(d2);
        System.out.println(d1);
        System.out.println(d2);       
        
    }

    static public void testDAO(){
        
        Activite ac = ServiceMetier.getActiviteByID(17);
        System.out.println(ac);
        
        Adherent ad = ServiceMetier.getAdherentByID(53);
        System.out.println(ad);
        
        List<Demande> listD = ServiceMetier.getDemandeByAuthor(ad);
        
         for (Demande dem : listD)
        {
            System.out.println(dem);
        }
        
        
    }
    
    static public void testPAFLieu(){
        System.out.println("On rentre dans test");
        Evenement e = ServiceMetier.getEvenementByID(2203);
        System.out.println(e);
        ServiceMetier.modifyPAF(e, new Double(5));
        ServiceMetier.modifyLieu(e, ServiceMetier.getLieuByID(1));
        System.out.println(e);
    }
     
        
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        JpaUtil.init();
        
        //Adherent ad = saisieA();
        //ServiceMetier.CreerAdherent(ad);
        //ServiceMetier.ConsulterListeAd();
        //ConsulterListeAd();
        //ConsulterListeAc();
        //ConsulterListeLieu();
        
        //System.out.println("romain : " +ServiceMetier.verifyMail("romain.mie@free.fr"));
        //System.out.println("rien : " +ServiceMetier.verifyMail("..."));
        
        //trouverActivite();
        //testDemandes();
        //testDAO();
        testPAFLieu();
        
        
        JpaUtil.destroy();
    }
    
}
