/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.service;

import util.GeoTest;
import com.google.maps.model.LatLng;
import metier.modele.Adherent;
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
    
}
