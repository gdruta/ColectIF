/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.service;

import dao.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import metier.modele.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import metier.modele.Demande;
import com.google.maps.model.LatLng;

/**
 *
 * @author epetit
 */
public class ServiceMetier {
    
    static public void CreerAdherent(Adherent ad){

        ad = ServiceTechnique.miseAJourAdherent(ad);
        
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();       
        AdherentDAO AdDAO = new AdherentDAO();
        AdDAO.persister(ad);
        try {            
            JpaUtil.validerTransaction();
        } catch (Exception ex) {
            JpaUtil.annulerTransaction();
        }       
        
        JpaUtil.fermerEntityManager();
    }
    
    static public void CreerActivite(Activite a){

        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();

        ActiviteDAO AcDAO = new ActiviteDAO();
        AcDAO.persister(a);
        JpaUtil.validerTransaction();
        
        JpaUtil.fermerEntityManager();
    }
    
    static public void CreerLieu(Lieu l){

        l = ServiceTechnique.miseAJourLieu(l);
        
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();

        LieuDAO LDAO = new LieuDAO();
        LDAO.persister(l);
        JpaUtil.validerTransaction();
        
        JpaUtil.fermerEntityManager();
    }
    
    static public void CreerEvenement(Evenement e){
        
        System.out.println("Je suis dans CreerEvenement");
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();

        EvenementDAO LDAO = new EvenementDAO();
        LDAO.persister(e);
        JpaUtil.validerTransaction();
        
        JpaUtil.fermerEntityManager();
    }
    
    static public void CreerDemande(Demande d){        

        System.out.println("Je suis dans CreerDemande");
        try {
            System.out.println("Il existe un doublon : " +verifyDoublonDemande(d));
            if (!verifyDoublonDemande(d))
            {
                JpaUtil.creerEntityManager();
                JpaUtil.ouvrirTransaction();
                DemandeDAO DDAO = new DemandeDAO();
                AdherentDAO AdDAO = new AdherentDAO();
                DDAO.persister(d);
                Adherent a = d.getDemandeur();
                a.addListDemande(d);
                //mise a jourl'adherent qu'on vient de changer
                AdDAO.merge(a); 
                JpaUtil.validerTransaction();
                
                JpaUtil.fermerEntityManager();
                
                try {
                    verifyEvenement(d);
                } catch (Exception ex) {
                    Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }
    
    static boolean verifyDoublonDemande(Demande d) throws Exception
    {
        JpaUtil.creerEntityManager();
        
        Adherent a=d.getDemandeur();
        
        List<Demande> listD = getDemandeComparableSameAuthor(d);

        if (listD.size()>1)
        {
            return true;
        }
        return false;
    }
    
    static public boolean verifyEvenement(Demande d) throws Exception{
               
        System.out.println("Je suis dans VerifyEvenement");
         System.out.println("Demande à comparer : " + d);
        Activite a = d.getActivite();
        List<Demande> listD = getDemandeComparable(d);
        int nbTotNecessaire = a.getNbParticipants();
        int nb = 1;
        List<Demande> listFinale = new ArrayList<Demande>();
        listFinale.add(d);
        
        for ( Demande demande : listD)
        {
            System.out.println("Demande comparable : "+demande);
            if (!demande.isDejaPris()&&demande.getId()!=d.getId()){
                nb++;
                listFinale.add(demande);
            }
        }
        
        if (nb == nbTotNecessaire){
            List<Adherent> listA =new ArrayList<Adherent>();;
            for ( Demande demandeFin : listFinale)
            {
                listA.add(demandeFin.getDemandeur());
                demandeFin.setDejaPris(true);
            }
            Evenement evenement = new Evenement(d.getDate(),d.getMoment(),d.getActivite(),listA);
            CreerEvenement(evenement);
            return true;
        }
                
        return false;
        
    }
	

    
    static public List<Adherent> ConsulterListeAd() {

        JpaUtil.creerEntityManager();

        AdherentDAO AdDAO = new AdherentDAO();
        List<Adherent> list=new ArrayList<Adherent>();;
        try {     
            list= AdDAO.findAll();
        } catch (Exception ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        }

        JpaUtil.fermerEntityManager();
        
        return list;
    }
        
    static public List<Activite> ConsulterListeAc() {

        JpaUtil.creerEntityManager();

        ActiviteDAO AcDAO = new ActiviteDAO();
        List<Activite> list=new ArrayList<Activite>();
        try {     
            list= AcDAO.findAll();
        } catch (Exception ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        }

        JpaUtil.fermerEntityManager();

        return list;
    }

    static public List<Lieu> ConsulterListeLieu() {

        JpaUtil.creerEntityManager();

        LieuDAO lDAO = new LieuDAO();
        List<Lieu> list=new ArrayList<Lieu>();
        try {     
            list= lDAO.findAll();
        } catch (Exception ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        }

        JpaUtil.fermerEntityManager();

        return list;
    }
    
    static public List<Evenement> ConsulterListeEvenement() {

        JpaUtil.creerEntityManager();

        EvenementDAO EvDAO = new EvenementDAO();
        List<Evenement> list=new ArrayList<Evenement>();
        try {     
            list= EvDAO.findAll();
        } catch (Exception ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        }

        JpaUtil.fermerEntityManager();
        
        return list;
    }
	
	//à tester
	static public List<Evenement> getAllEvenementPastToday() {

        JpaUtil.creerEntityManager();

        EvenementDAO EvDAO = new EvenementDAO();
        List<Evenement> list=new ArrayList<Evenement>();
        try {     
            list= EvDAO.findAllPastToday();
        } catch (Exception ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        }

        JpaUtil.fermerEntityManager();
        
        return list;
    }
    
    static public void modifyPAF(Evenement e,Double d){
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        EvenementDAO EvDAO = new EvenementDAO();
        EvDAO.updatePAF(e, d);
        
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
    }
    
    static public void modifyLieu(Evenement e,Lieu l){
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        EvenementDAO EvDAO = new EvenementDAO();
        EvDAO.updateLieu(e, l);
        
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
    }
    
    static public boolean verifyMail (String mail){
        JpaUtil.creerEntityManager();
        AdherentDAO AdDAO = new AdherentDAO();
        Adherent adherent=null;
        try {
            adherent =AdDAO.findByMail(mail);
        } catch (Exception ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        }         
        JpaUtil.fermerEntityManager();
        if (adherent==null)
        {
             return false;
        }
        return true;
    }
    
    static public Adherent getAdherentByMail (String mail){
        JpaUtil.creerEntityManager();
        AdherentDAO AdDAO = new AdherentDAO();
        Adherent adherent=null;
        try {
            adherent =AdDAO.findByMail(mail);
        } catch (Exception ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        }         
        JpaUtil.fermerEntityManager();
        
        return adherent;
    }
    
    static public Adherent getAdherentByID (long id){
        JpaUtil.creerEntityManager();
        AdherentDAO AdDAO = new AdherentDAO();
        Adherent adherent=null;
        try {
            adherent =AdDAO.findById(id);
        } catch (Exception ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        }         
        JpaUtil.fermerEntityManager();
        
        return adherent;
    }
    
    static public List<Demande> getAllDemande (){        
        JpaUtil.creerEntityManager();
        DemandeDAO DemDAO = new DemandeDAO();
        List<Demande> demList = new ArrayList<Demande>();
        try {
            demList =DemDAO.findAll();
        } catch (Exception ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        } 
        JpaUtil.fermerEntityManager();
        return demList;
    }
    
    static public Demande getDemandeByID (long id){
        JpaUtil.creerEntityManager();
        DemandeDAO DemDAO = new DemandeDAO();
        Demande dem=null;
        try {
            dem =DemDAO.findById(id);
        } catch (Exception ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        }         
        JpaUtil.fermerEntityManager();
        
        return dem;
    }
    
     static public List<Demande> getDemandeByAuthor(Adherent ad){
        JpaUtil.creerEntityManager();
        
        DemandeDAO DemDAO = new DemandeDAO();
        List<Demande> dem = new ArrayList<Demande>();
        try {
            dem =DemDAO.findByAuthor(ad);
        } catch (Exception ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        } 
        JpaUtil.fermerEntityManager();
        return dem;
    }
     
      static public List<Demande> getDemandeByDate (GregorianCalendar date)
    {
        JpaUtil.creerEntityManager();
        DemandeDAO DemDAO = new DemandeDAO();
        List<Demande> dem = new ArrayList<Demande>();
        try {
            dem =DemDAO.findByDate(date);
        } catch (Exception ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        } 
        JpaUtil.fermerEntityManager();
        return dem;
    }
      
      static public List<Demande> getDemandeComparable (Demande demande)
    {
        JpaUtil.creerEntityManager();
        DemandeDAO DemDAO = new DemandeDAO();
        List<Demande> dem = new ArrayList<Demande>();
        GregorianCalendar date = demande.getDate();
        Activite ac = demande.getActivite();
        String moment = demande.getMoment();
        try {
            dem =DemDAO.findComparable(date, moment, ac);
        } catch (Exception ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        } 
        JpaUtil.fermerEntityManager();
        return dem;
    }
      
      static public List<Demande> getDemandeComparableSameAuthor (Demande demande)
    {
        JpaUtil.creerEntityManager();
        DemandeDAO DemDAO = new DemandeDAO();
        List<Demande> dem = new ArrayList<Demande>();
        GregorianCalendar date = demande.getDate();
        Activite ac = demande.getActivite();
        String moment = demande.getMoment();
        Adherent ad = demande.getDemandeur();
        try {
            dem =DemDAO.findComparableSameAuthor(date, moment, ac, ad);
        } catch (Exception ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        } 
        JpaUtil.fermerEntityManager();
        return dem;
    }
    

	//à tester
	static public List<LatLng> getAllCoordonneesAdherent(Evenement evenement)
    {
        JpaUtil.creerEntityManager();
		
        List<LatLng> coordonees = new ArrayList<LatLng>();
		List<Adherent> listAdherents = evenement.listAdherents;
		
		LatLng l;
		for ( Adherent ad : listAdherents)
            {
				l.longitude = ad.longitude;
				l.latitude = ad.latitude;
				coordonees.add(l);
			}

        return coordonees;
    }
	
     static public Evenement getEvenementByID (long id)
    {
        JpaUtil.creerEntityManager();
        EvenementDAO EvDAO = new EvenementDAO();
        Evenement ev=null;
        try {
            ev =EvDAO.findById(id);
        } catch (Exception ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        }         
        JpaUtil.fermerEntityManager();
        
        return ev;
    }
    

     static public Activite getActiviteByID (long id)
    {
        JpaUtil.creerEntityManager();
        ActiviteDAO aDAO = new ActiviteDAO();
        Activite ac=null;
        try {
            ac =aDAO.findById(id);
        } catch (Exception ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        }         
        JpaUtil.fermerEntityManager();
        
        return ac;
    }     
     
      static public List<Activite> getAllActivite ()
    {        
        JpaUtil.creerEntityManager();
        ActiviteDAO AcDAO = new ActiviteDAO();
        List<Activite> ac = new ArrayList<Activite>();
        try {
            ac =AcDAO.findAll();
        } catch (Exception ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        } 
        JpaUtil.fermerEntityManager();
        return ac;
    }
      
      static public Lieu getLieuByID (long id)
    {
        JpaUtil.creerEntityManager();
        LieuDAO lDAO = new LieuDAO();
        Lieu lieu=null;
        try {
            lieu =lDAO.findById(id);
        } catch (Exception ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        }         
        JpaUtil.fermerEntityManager();
        
        return lieu;
    }
     
      static public List<Lieu> getAllLieu ()
    {        
        JpaUtil.creerEntityManager();
        LieuDAO lDAO = new LieuDAO();
        List<Lieu> lieu = new ArrayList<Lieu>();
        try {
            lieu =lDAO.findAll();
        } catch (Exception ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        } 
        JpaUtil.fermerEntityManager();
        return lieu;
    }
    
}
