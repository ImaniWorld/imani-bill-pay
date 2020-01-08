package com.imani.bill.pay.domain.payment.plaid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Returns information about a Plaid Item.  Items could include accounts etc.
 *
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaidItemInfo {


    private String error;

    @JsonProperty("institution_id")
    private String institutionID;

    @JsonProperty("item_id")
    private String itemID;

    @JsonProperty("webhook")
    private String webHook;

    @JsonProperty("available_products")
    private List<String> availableProducts = new ArrayList<>();

    @JsonProperty("billed_products")
    private List<String> billedProducts = new ArrayList<>();


    public PlaidItemInfo() {

    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getInstitutionID() {
        return institutionID;
    }

    public void setInstitutionID(String institutionID) {
        this.institutionID = institutionID;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getWebHook() {
        return webHook;
    }

    public void setWebHook(String webHook) {
        this.webHook = webHook;
    }

    public List<String> getAvailableProducts() {
        return ImmutableList.copyOf(availableProducts);
    }

    public void setAvailableProducts(List<String> availableProducts) {
        this.availableProducts = availableProducts;
    }

    public List<String> getBilledProducts() {
        return ImmutableList.copyOf(billedProducts);
    }

    public void setBilledProducts(List<String> billedProducts) {
        this.billedProducts = billedProducts;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("error", error)
                .append("institutionID", institutionID)
                .append("itemID", itemID)
                .append("webHook", webHook)
                .append("availableProducts", availableProducts)
                .append("billedProducts", billedProducts)
                .toString();
    }
}
