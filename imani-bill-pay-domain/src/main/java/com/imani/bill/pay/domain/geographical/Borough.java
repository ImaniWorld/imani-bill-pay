package com.imani.bill.pay.domain.geographical;

import com.imani.bill.pay.domain.AuditableRecord;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * @author manyce400
 */
@Entity
@Table(name="Borough")
public class Borough extends AuditableRecord {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    @Column(name="Name", nullable=false, length = 50)
    private String name;


    @Column(name="BoroughId", nullable=false, length=10)
    private Integer boroughId;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CityID", nullable = false)
    private City city;

    public Borough() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBoroughId() {
        return boroughId;
    }

    public void setBoroughId(Integer boroughId) {
        this.boroughId = boroughId;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public BoroughIndex toBoroughIndex() {
        BoroughIndex boroughIndex = BoroughIndex.builder()
                .id(id)
                .name(name)
                .boroughId(boroughId)
                .build();

        if(city != null) {
            boroughIndex.setCity(city.getName());

            if(city.getState() != null) {
                boroughIndex.setState(city.getState().getName());
            }
        }

        return boroughIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Borough borough = (Borough) o;

        return new EqualsBuilder()
                .append(id, borough.id)
                .append(name, borough.name)
                .append(boroughId, borough.boroughId)
                .append(city, borough.city)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(boroughId)
                .append(city)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("boroughId", boroughId)
                .append("city", city)
                .toString();
    }

}
