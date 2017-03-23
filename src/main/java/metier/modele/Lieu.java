package metier.modele;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Lieu implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String denomination;
    private String type;
    private String adresse;
    private Double longitude;
    private Double latitude;

    protected Lieu() {
    }

    public Lieu(String denomination, String type, String adresse) {
        this.denomination = denomination;
        this.type = type;
        this.adresse = adresse;
        this.longitude = null;
        this.latitude = null;
    }

    public Long getId() {
        return id;
    }

    public String getDenomination() {
        return denomination;
    }

    public String getType() {
        return type;
    }

    public String getAdresse() {
        return adresse;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setLatitudeLongitude(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Lieu{" + "id=" + id + ", denomination=" + denomination + ", type=" + type + ", adresse=" + adresse + ", longitude=" + longitude + ", latitude=" + latitude + '}';
    }
    
}
