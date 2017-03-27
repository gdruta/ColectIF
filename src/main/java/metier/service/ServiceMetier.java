package metier.service;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/ 


import dao.EvenementDAO;
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

    // c minuscule
    //----Adherent----
    static public void CreerAdherent(Adherent ad) throws ServiceException{


        ad = ServiceTechnique.miseAJourAdherent(ad);
        
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();       
        AdherentDAO AdDAO = new AdherentDAO();
        AdDAO.persister(ad);
        boolean error=false;
        try {            
            JpaUtil.validerTransaction();
            
        } catch (Exception ex) {
            JpaUtil.annulerTransaction();
            ServiceTechnique.sendMailInscriptionFail(ad.getPrenom(), ad.getMail());
            error=true;
            throw new ServiceException("ERREUR : Adhérent n'a pas pu être ajouté.");
        }       
        if (!error){
            ServiceTechnique.sendMailInscription(ad);
        }
        JpaUtil.fermerEntityManager();
    }
    // c minuscule
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

    //----Demande----
    static public void CreerDemande(Demande d) throws ServiceException{        
        JpaUtil.creerEntityManager();
        DemandeDAO dDAO = new DemandeDAO();
        try {
            if (dDAO.demandeExists(d)){
                 throw new ServiceException("ERROR : Vous avez deja creer cette demande");
            }
            boolean errorTransaction=false;
            do {
                //demandes compatibles
                List<Demande> demandesCompatibles = dDAO.getSame(d);
                
                JpaUtil.ouvrirTransaction();
                
                //On cree la demande
                dDAO.persister(d);

                //On l'ajoute du côté de l'adhérent
                Adherent a = d.getDemandeur();
                a.addDemande(d);
                
                //persister/merge BD
                AdherentDAO AdDAO = new AdherentDAO();
                dDAO.persister(d);
                AdDAO.merge(a);
                
                demandesCompatibles.add(d);
                
                if (d.getActivite().getNbParticipants()==demandesCompatibles.size())
                {
                    List<Adherent> aList=new ArrayList<Adherent>();
                    for ( Demande demandeFin : demandesCompatibles)
                    {                        
                        demandeFin.setDejaPris(true);
                        aList.add(demandeFin.getDemandeur());
                    }
                    Evenement evenement = new Evenement(d.getDate(),d.getMoment(),d.getActivite(),aList);
                    EvenementDAO eDAO = new EvenementDAO();
                    eDAO.persister(evenement);
                }  
                
                //mise a jour             
                try {            
                    JpaUtil.validerTransaction();
                } catch (Exception ex) {
                    JpaUtil.annulerTransaction();
                    errorTransaction=true;
                }               
                
            }while(errorTransaction); 
            JpaUtil.fermerEntityManager();
            
        }
         catch (Exception ex) {
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
	
    //à tester
    static public List<Evenement> getAllEvenementPastToday() {

        JpaUtil.creerEntityManager();

        EvenementDAO EvDAO = new EvenementDAO();
        List<Evenement> list=new ArrayList<Evenement>();
        try {     
         //   list= EvDAO.findAllPastToday();
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
        if (e.getLieu()!=null){
            ServiceTechnique.sendMail(e);
        }
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
    }
    
    static public void modifyLieu(Evenement e,Lieu l){
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        EvenementDAO EvDAO = new EvenementDAO();
        EvDAO.updateLieu(e, l);
        //A refaire
        if (e.getPAF()!=null){
            ServiceTechnique.sendMail(e);
        }
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
	
	static public List<Adherent> getAllAdherent() {

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
            dem =DemDAO.getSame(demande);
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
        
        try {
            dem =DemDAO.getDemandesExistant(demande);
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
		List<Adherent> listAdherents = evenement.getListAdherents();
		
		LatLng l=new LatLng(0,0);
		for ( Adherent ad : listAdherents)
            {
				l.lng = ad.getLongitude();
				l.lat = ad.getLatitude();
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
		
	//à tester
	static public List<Evenement> getAllFuturEvenement() {

        JpaUtil.creerEntityManager();

        EvenementDAO EvDAO = new EvenementDAO();
        List<Evenement> list=new ArrayList<Evenement>();
        try {     
            list= EvDAO.findAllFutur();
        } catch (Exception ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        }

        JpaUtil.fermerEntityManager();
        
        return list;
    }
	
		
	//à tester
	static public List<Evenement> getFutureEvenementAdherent(Adherent ad) {

        JpaUtil.creerEntityManager();

        EvenementDAO EvDAO = new EvenementDAO();
        List<Evenement> list=new ArrayList<Evenement>();
        try { 
			//A FAIRE dans EvenementDAO, tout les evenements à venir d'un adherent
            //list= EvDAO.findAllPastToday();
        } catch (Exception ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        }

        JpaUtil.fermerEntityManager();
        
        return list;
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
	
    static public List<Evenement> getAllEvenement() {

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
