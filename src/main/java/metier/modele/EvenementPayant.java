/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.Entity;

/**
 *
 * @author Gheorghe
 */
@Entity
public class EvenementPayant extends Evenement implements Serializable{
    private double Paf;

    public EvenementPayant() {
    }

    public EvenementPayant(GregorianCalendar date, String momentJournee, Activite activite, List<Adherent> adherents) {
        super(date, momentJournee, activite, adherents);
    }

    public double getPaf() {
        return Paf;
    }

    public void setPaf(double Paf) {
        this.Paf = Paf;
    }
    
}
