package com.imani.bill.pay.domain.geographical;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

/**
 * @author manyce400
 */
@SolrDocument(solrCoreName = "BoroughIndexCore")
public class BoroughIndex {


    @Indexed(name = "id", type = "long")
    private Long id;


    @Indexed(name = "boroughId", type = "Integer")
    private Integer boroughId;


    @Indexed(name = "name", type = "string")
    private String name;


    @Indexed(name = "city", type = "string")
    private String city;

    @Indexed(name = "state", type = "string")
    private String state;


    public BoroughIndex() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBoroughId() {
        return boroughId;
    }

    public void setBoroughId(Integer boroughId) {
        this.boroughId = boroughId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("boroughId", boroughId)
                .append("name", name)
                .append("city", city)
                .append("state", state)
                .toString();
    }


    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final BoroughIndex boroughIndex = new BoroughIndex();

        public Builder id(Long id) {
            boroughIndex.id = id;
            return this;
        }

        public Builder boroughId(Integer boroughId) {
            boroughIndex.boroughId = boroughId;
            return this;
        }

        public Builder name(String name) {
            boroughIndex.name = name;
            return this;
        }

        public Builder city(String city) {
            boroughIndex.city = city;
            return this;
        }

        public Builder state(String state) {
            boroughIndex.state = state;
            return this;
        }

        public BoroughIndex build() {
            return boroughIndex;
        }
    }
}
