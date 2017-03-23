package dao;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import metier.modele.Activite;
import metier.modele.Demande;

public class ActiviteDAO {
    
    public Activite findById(long id) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Activite activite = null;
        try {
            activite = em.find(Activite.class, id);
        }
        catch(Exception e) {
            throw e;
        }
        return activite;
    }
    
    public List<Activite> findAll() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Activite> activites = new ArrayList<Activite>();;
        try {
            Query q = em.createQuery("SELECT a FROM Activite a");
            activites = (List<Activite>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return activites;
    }
        public void persister (Activite a){
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist(a);
    }
    
}
