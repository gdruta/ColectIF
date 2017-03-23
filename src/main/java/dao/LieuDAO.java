package dao;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import metier.modele.Demande;
import metier.modele.Lieu;

public class LieuDAO {
    
    public Lieu findById(long id) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Lieu lieu = null;
        try {
            lieu = em.find(Lieu.class, id);
        }
        catch(Exception e) {
            throw e;
        }
        return lieu;
    }
    
    public List<Lieu> findAll() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Lieu> lieux = new ArrayList<Lieu>();;
        try {
            Query q = em.createQuery("SELECT l FROM Lieu l");
            lieux = (List<Lieu>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return lieux;
    }
    
        public void persister (Lieu l){
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist(l);
    }
}
