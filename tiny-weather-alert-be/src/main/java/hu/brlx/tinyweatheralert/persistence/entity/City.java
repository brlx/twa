package hu.brlx.tinyweatheralert.persistence.entity;

import hu.brlx.tinyweatheralert.persistence.TwaEntity;

import javax.persistence.*;

@Entity
@Table(name = "cities")

@NamedQueries({
        @NamedQuery(
                name = "City.query.byName",
                query = "SELECT c FROM City c WHERE c.name LIKE :cityName"
        ),
        @NamedQuery(name = "City.query.distinctIdByAlerts",
                query = "SELECT DISTINCT a.city.id FROM Alert a")
})

public class City implements TwaEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "cityname")
    private String name;

    @Column(name = "country")
    private String country;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        City city = (City) other;
        return id.equals(city.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "City [" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ']';
    }
}
