/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.GregorianCalendar;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

/**
 *
 * @author epetit
 */
@Entity
public class Evenement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private GregorianCalendar date;
    private String moment; 
    @ManyToOne
    private Activite activite;
    @ManyToMany
    private List<Adherent> listAdherents;
    @ManyToOne
    private Lieu lieu;
    private Double PAF;

    protected Evenement() {
    }      

    public Evenement(GregorianCalendar date, String moment, Activite activite, List<Adherent> listAdherents) {
        this.date = date;
        this.moment = moment;
        this.activite = activite;
        this.listAdherents = listAdherents;
        this.PAF = null;
        this.lieu = null;
    }

    public void setPAF(double PAF) {
        this.PAF = PAF;
    }

    public void setLieu(Lieu lieu) {
        this.lieu = lieu;
    }
    
    public Long getId() {
        return id;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public String getMoment() {
        return moment;
    }

    public Activite getActivite() {
        return activite;
    }

    public List<Adherent> getListAdherents() {
        return listAdherents;
    }

    public Lieu getLieu() {
        return lieu;
    }

    public double getPAF() {
        return PAF;
    }

    @Override
    public String toString() {
        if ((PAF == null)&&(lieu==null)){
            return "Evenement{" + "id=" + id + ", date=" + date + ", moment=" + moment + ", activite=" + activite.getDenomination() + ", listAdherents=" + listAdherents + ", lieu= null" + ", PAF= null" +'}';
        }
        else if ((PAF != null)&&(lieu==null)){
            return "Evenement{" + "id=" + id + ", date=" + date + ", moment=" + moment + ", activite=" + activite.getDenomination() + ", listAdherents=" + listAdherents + ", lieu= null" + ", PAF"+ PAF +'}';
        }
        else if ((PAF == null)&&(lieu!=null)){
            return "Evenement{" + "id=" + id + ", date=" + date + ", moment=" + moment + ", activite=" + activite.getDenomination() + ", listAdherents=" + listAdherents + ", lieu="+ lieu.getDenomination() + ", PAF = null"+'}';
        }
        else if ((PAF != null)&&(lieu!=null)){
            return "Evenement{" + "id=" + id + ", date=" + date + ", moment=" + moment + ", activite=" + activite.getDenomination() + ", listAdherents=" + listAdherents + ", lieu="+ lieu.getDenomination() + ", PAF="+ PAF +'}';
        }
        else{
            return "Evenement{" + "id=" + id + ", date=" + date + ", moment=" + moment + ", activite=" + activite.getDenomination() + ", listAdherents=" + listAdherents + ", lieu="+ lieu.getDenomination() + ", PAF="+ PAF +'}';
        }
            
       
    }
    
    
    
}
