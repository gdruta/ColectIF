package dao;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import metier.modele.Demande;
import metier.modele.Evenement;
import metier.modele.Lieu;

public class EvenementDAO {
    
    public Evenement findById(long id) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Evenement activite = null;
        try {
            activite = em.find(Evenement.class, id);
        }
        catch(Exception e) {
            throw e;
        }
        return activite;
    }
    
    public List<Evenement> findAll() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Evenement> activites = new ArrayList<Evenement>();
        try {
            Query q = em.createQuery("SELECT a FROM Evenement a");
            activites = (List<Evenement>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return activites;
    }
    //Evenements futurs
    public List<Evenement> findAllFutur() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Evenement> activites = new ArrayList<Evenement>();
		//date d'aujourd'hui
		GregorianCalendar gregorianCalendar=new GregorianCalendar();
		//même format que ce qu'on rentre
		GregorianCalendar date = new GregorianCalendar(GregorianCalendar.YEAR,GregorianCalendar.MONTH,GregorianCalendar.DAY_OF_MONTH);

        try {
			// a tester, vérifier que ça rend bien tout les évenment après aujourd'hui
            Query q = em.createQuery("SELECT a FROM Evenement a WHERE a.date >= date1");
			q.setParameter("date1", date);
            activites = (List<Evenement>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return activites;
    }
    
    public void persister (Evenement e){
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist(e);
    }
    
    public void updatePAF(Evenement e,Double d){
        EntityManager em = JpaUtil.obtenirEntityManager();
        e.setPAF(d);
        em.merge(e);
    }
    
    public void updateLieu(Evenement e,Lieu l){
        EntityManager em = JpaUtil.obtenirEntityManager();
        e.setLieu(l);
        em.merge(e);
    }
    
}
