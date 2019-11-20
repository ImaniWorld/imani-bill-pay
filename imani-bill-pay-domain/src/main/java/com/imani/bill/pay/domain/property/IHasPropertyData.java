package com.imani.bill.pay.domain.property;

/**
 * @author manyce400
 */
public interface IHasPropertyData {

    public Long getBoroID();

    public Long getCityID();

    public String getPropertyNumber();

    public String getStreetName();

    public Integer getLegalStories();

    public String getZipCode();

    public String getBlock();

    public String getLot();

    public String getBin();

    public Boolean isPublicHousing();

    public PropertyTypeE getPropertyTypeE();

}
