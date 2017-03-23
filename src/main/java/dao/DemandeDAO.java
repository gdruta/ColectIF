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
     public List<Demande> findComparable(GregorianCalendar date, String moment, Activite activite) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Demande> dem = new ArrayList<Demande>();
        try {
            String query = "SELECT a FROM Demande a WHERE a.date=:date1 AND a.activite=:activite1 AND a.moment=:moment1" ;
            Query q = em.createQuery(query);
            q.setParameter("date1", date);
            q.setParameter("activite1", activite);
            q.setParameter("moment1", moment);
            dem = (List<Demande>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return dem;
    }
     
     public List<Demande> findComparableSameAuthor(GregorianCalendar date, String moment, Activite activite, Adherent adherent) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Demande> dem = new ArrayList<Demande>();
        try {
            String query = "SELECT a FROM Demande a WHERE a.date=:date1 AND a.activite=:activite1 AND a.moment=:moment1 AND a.demandeur=:adherent1" ;
            Query q = em.createQuery(query);
            q.setParameter("date1", date);
            q.setParameter("activite1", activite);
            q.setParameter("moment1", moment);
            q.setParameter("adherent1", adherent);
            dem = (List<Demande>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return dem;
    }
    
    
        public void persister (Demande d){
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist(d);
    }
    
}
