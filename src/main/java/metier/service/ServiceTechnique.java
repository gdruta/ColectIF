/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.service;

import util.GeoTest;
import com.google.maps.model.LatLng;
import java.util.List;
import metier.modele.Adherent;
import metier.modele.Evenement;
import metier.modele.Lieu;

/**
 *
 * @author epetit
 */
public class ServiceTechnique {
    
    public static Adherent miseAJourAdherent(Adherent ad){
        if (ad.getAdresse()!=null){
            LatLng latlong = GeoTest.getLatLng(ad.getAdresse());
            
            if (latlong != null){
                ad.setLatitudeLongitude(latlong.lat, latlong.lng);  
            }else{
                ad.setLatitudeLongitude(new Double(0),new Double(0));  
            } 
            
        }
        return ad;
    } 
    
    public static Lieu miseAJourLieu(Lieu l){
        if (l.getAdresse()!=null){
            LatLng latlong = GeoTest.getLatLng(l.getAdresse());
            
            if (latlong != null){
              l.setLatitudeLongitude(latlong.lat, latlong.lng);  
            } 
            
        }
        return l;
    } 
    
   public static void sendMailInscription(Adherent a){
        
        System.out.println("Expéditeur : collectif@collectif.org");
        System.out.println("Pour : "+ a.getMail());
        System.out.println("Sujet : Bienvenue chez Collect'IF");
        System.out.println("Bonjour " + a.getPrenom());
        System.out.println("Nous vous confirmons votre adhésion à l'association COLLECT ’IF. Votre numéro d'adhérent est : "+ a.getId());
    }
   
    public static void sendMailInscriptionFail(String prenom, String mail){
        
        System.out.println("Expéditeur : collectif@collectif.org");
        System.out.println("Pour : "+ mail);
        System.out.println("Sujet : Echec de l'inscription à Collect'IF");
        System.out.println("Bonjour " + prenom);
        System.out.println("Votre adhésion à l'association COLLECT’IF a malencontreusement échoué...   Merci de recommencer ultérieurement.");       
    }
    
    public static void sendMail(Evenement e){
        List<Adherent> listA= e.getListAdherents();
        for (Adherent a : listA)
        {
            System.out.println("Expéditeur : collectif@collectif.org");
            System.out.println("Pour : "+ a.getMail());
            System.out.println("Sujet : Nouvel Évènement Collect'IF");
            System.out.println("Bonjour " + a.getPrenom());
            System.out.println("Comme  vous  l'aviez souhaité,  C OLLECT ’IF organise un évènement de  Tarot le 20 mars  2017 . Vous  trouverez ci - dessous les détails de cet évènement.");
            System.out.println("Associativement vôtre,");
            System.out.println("Le Responsable de l'Association");
            System.out.println("Evénement : " + e.getActivite().getDenomination());
            System.out.println("Date : " + e.getDate());
            System.out.println("Lieu : " + e.getLieu().getDenomination()+", "+e.getLieu().getAdresse());
            LatLng origin=new LatLng(a.getLatitude(),a.getLongitude());
            LatLng destination=new LatLng(e.getLieu().getLatitude(),e.getLieu().getLongitude());
            System.out.println("(à "+ GeoTest.getFlightDistanceInKm(origin,destination)+" km de chez vous)");
            System.out.println("Vous jouerez avec :");
            for (Adherent adh : listA)
            {
                if (a.getId()!=adh.getId())
                {
                    System.out.println(adh.getPrenom()+" "+adh.getNom().toUpperCase() );
                }
            }
        }    
    }
    

}
