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
public class EvenementGratuit extends Evenement implements Serializable {

    public EvenementGratuit(GregorianCalendar date, String momentJournee, Activite activite, List<Adherent> adherents) {
        super(date, momentJournee, activite, adherents);
    }

    public EvenementGratuit() {
    }
}
