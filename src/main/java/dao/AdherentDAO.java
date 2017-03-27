package dao;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import metier.modele.Adherent;
import metier.modele.Demande;

public class AdherentDAO {
        
    public Adherent findById(long id) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Adherent adherent = null;
        try {
            adherent = em.find(Adherent.class, id);
        }
        catch(Exception e) {
            throw e;
        }
        return adherent;
    }
    
    public Adherent findByMail(String mail) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Adherent> adherents = new ArrayList<Adherent>();;
        mail = mail.toLowerCase();
        try {
            Query q = em.createQuery("SELECT a FROM Adherent a WHERE a.mail = '"+mail+"'" );
            adherents = (List<Adherent>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        Adherent adherent=null;
        if (!adherents.isEmpty()){
            adherent=adherents.get(0);
        }        
        return adherent;
    }
    
    public List<Adherent> findAll() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Adherent> adherents = null;
        try {
            Query q = em.createQuery("SELECT a FROM Adherent a");
            adherents = (List<Adherent>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return adherents;
    }
    
    
    public void persister (Adherent a){
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist(a);
    }
    
    public void merge (Adherent a){
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.merge(a);
    }
    
    public List<Adherent> updateAll (List<Adherent> aList){
        EntityManager em = JpaUtil.obtenirEntityManager();
        for ( Adherent adherent : aList)
        {                        
            em.merge(adherent);
        }
        return aList;
    }
    
}
