package dao;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import metier.modele.*;

public class DemandeDAO {
    
    public Demande findById(long id) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Demande activite = null;
        try {
            activite = em.find(Demande.class, id);
        }
        catch(Exception e) {
            throw e;
        }
        return activite;
    }
    
    public List<Demande> findAll() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Demande> dem = new ArrayList<Demande>();
        try {
            Query q = em.createQuery("SELECT a FROM Demande a");
            dem = (List<Demande>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return dem;
    }
    
    public List<Demande> findByAuthor(Adherent ad) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Demande> dem = new ArrayList<Demande>();
        try {
            String query = "SELECT a FROM Demande a WHERE a.demandeur=:adherent";
            Query q = em.createQuery(query);
            q.setParameter("adherent", ad);
            dem = (List<Demande>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return dem;
    }
    
     public List<Demande> findByDate(GregorianCalendar date) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Demande> dem = new ArrayList<Demande>();
        try {
            String query = "SELECT a FROM Demande a WHERE a.DATE=:date1";
            Query q = em.createQuery(query);
            q.setParameter("date1", date);
            dem = (List<Demande>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return dem;
    }
     
    public List<Demande> getSame(Demande d) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Demande> dem = new ArrayList<Demande>();
        try {
            String query = "SELECT a FROM Demande a WHERE a.date=:date1 AND a.activite=:activite1 AND a.moment=:moment1 AND a.dejaPris:=dejaPris1" ;
            Query q = em.createQuery(query);
            q.setParameter("date1", d.getDate());
            q.setParameter("activite1", d.getActivite());
            q.setParameter("moment1", d.getMoment());
            q.setParameter("dejaPris1", d.isDejaPris());
            dem = (List<Demande>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return dem;
    }
     
    public List<Demande> getDemandesExistant(Demande d) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Demande> dem = new ArrayList<Demande>();
        try {
            String query = "SELECT a FROM Demande a WHERE a.date=:date1 AND a.activite=:activite1 AND a.moment=:moment1 AND a.demandeur=:adherent1" ;
            Query q = em.createQuery(query);
            q.setParameter("date1", d.getDate());
            q.setParameter("activite1", d.getActivite());
            q.setParameter("moment1", d.getMoment());
            q.setParameter("adherent1", d.getDemandeur());
            dem = (List<Demande>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return dem;
    }
    
    public boolean demandeExists(Demande d) throws Exception {
        List<Demande> dList = getDemandesExistant(d);
        if (dList.isEmpty()){
            return false;
        }
        return true;
    }
    
    
    public void persister (Demande d){
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist(d);
    }
    public List<Demande> updateAll (List<Demande> dList){
        EntityManager em = JpaUtil.obtenirEntityManager();
        for ( Demande demande : dList)
        {                        
            em.merge(demande);
        }
        return dList;
    }
}
