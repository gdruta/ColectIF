package metier.modele;

import java.io.Serializable;
import java.util.GregorianCalendar;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Adherent demandeur;
    @ManyToOne
    private Activite activite;
    private GregorianCalendar date;
    private String moment;
    private boolean dejaPris;

    protected Demande() {
    }    
    
    public Demande(Adherent demandeur, Activite activite, GregorianCalendar date, String moment) {
        this.demandeur = demandeur;
        this.activite = activite;
        this.date = date;
        this.moment = moment;
        this.dejaPris = false;
    }

    public Long getId() {
        return id;
    }

    public Adherent getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(Adherent demandeur) {
        this.demandeur = demandeur;
    }

    public Activite getActivite() {
        return activite;
    }

    public void setActivite(Activite activite) {
        this.activite = activite;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public boolean isDejaPris() {
        return dejaPris;
    }

    public void setDejaPris(boolean dejaPris) {
        this.dejaPris = dejaPris;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    public String getMoment() {
        return moment;
    }

    public void setMoment(String moment) {
        this.moment = moment;
    }
  
    public boolean compare(Demande d){
        if ((activite.equals(d.getActivite())) && (date.equals(d.getDate())) && (moment.equals(d.getMoment())))
        {
            return true;
        }
        else 
            return false;
    }

    @Override
    public String toString() {
        return "Demande{" + "id=" + id + ", demandeur=" + demandeur.getNom() + ", activite=" + activite.getDenomination() + ", date=" + date + ", moment=" + moment + ", dejaPris=" + dejaPris + '}';
    }
    

}
