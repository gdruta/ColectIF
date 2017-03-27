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
import metier.service.ServiceException;




/**
 *
 * @author epetit
 */
public class Main {
	
    
	static public String message(String mes){

            return Saisie.lireChaine(mes);
    }
    
    static public void ajouterAdherent(){        
		
        String prenom = Saisie.lireChaine("Entrez votre prenom: ");
        String nom = Saisie.lireChaine("Entrez votre nom: ");
        String mail = Saisie.lireChaine("Entrez votre mail: ");
        String adresse = Saisie.lireChaine("Entrez votre adresse: ");
		
        Adherent ad= new Adherent(nom, prenom, mail.toLowerCase(), adresse);
            try {                
                ServiceMetier.creerAdherent(ad);
            } catch (ServiceException ex) { 
                ex.printStackTrace();
                //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
	
    static public void afficherAdherents(){
            List<Adherent> listA = getAllAdherent();
            for ( Adherent ad : listA)
            {
                    System.out.println(ad);
            }
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
    
//    static public void testPAFLieu(){
//        System.out.println("On rentre dans test");
//        Evenement e = ServiceMetier.getEvenementByID(2203);
//        System.out.println(e);
//        ServiceMetier.modifyPAF(e, new Double(5));
//        ServiceMetier.modifyLieu(e, ServiceMetier.getLieuByID(1));
//        System.out.println(e);
//    }
     
        
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        JpaUtil.init();
        menuPrincipal();
        
        JpaUtil.destroy();
    }
    
    public static int menu() {
        System.out.println(" ==== MENU PRINCIPAL ==== ");
        int i = 1;
        System.out.println(i + " : Ajouter adhérent");
        i++;
        System.out.println(i + " : Verifier existance mail(connection)");
        i++;
        System.out.println(i + " : Ajouter demande");
        i++;
        System.out.println(i + " : Lister activites");
        i++;        
        System.out.println(i + " : Lister demandes d'un adhérent");
        i++;
        System.out.println(i + " : Lister évènements prohains d'un adhérent");
        i++;
        System.out.println(i + " : Lister evenements");
        i++;
        System.out.println(i + " : Lister lieux");
        i++;        
        System.out.println(i + " : Ajouter PAF");
        i++;
        System.out.println(i + " : Ajouter Lieu");
        i++;
        System.out.println(i + " : Quitter programme");
        i++;

        return getMenuOption(1, i);
    }
    private static void menuPrincipal() {
        boolean quit=false;
        do{
            int option=menu();
            switch(option){
                case 1:
                    ajouterAdherent();
                    break;
                case 2:
                    boolean result=ServiceMetier.verifyMail(message("Connection avec le mail : "));
                    if (result)
                    {
                        System.out.println("Connection etabli");
                    }else{
                        System.out.println("Email introuvable");
                    }
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
                    break;
                case 10:
                    break;
                case 11:
                    quit=true;
                    break;                
            }
                    
        }while(!quit);
    }

    private static int getMenuOption(int i, int i0) {
        return  Saisie.lireInteger("votre option : ");
    }

    

}
