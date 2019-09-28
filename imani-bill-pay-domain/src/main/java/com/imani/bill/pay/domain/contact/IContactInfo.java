package com.imani.bill.pay.domain.contact;

/**
 * Interface that defines a contact method.  Contact method contains information for the basic types of contact.
 *
 * @author manyce400
 */
public interface IContactInfo {

    /**
     * Returns a valid mobile phone number associated with this contact.
     *
     * @return <code>null</code> if implementation does not support this.
     */
    public Long getMobilePhone();

    /**
     * Primary land line phone number associated with this IContactInfo.
     *
     * @return <code>null</code> if implementation does not support this.
     */
    public Long getPhone();

    /**
     * Primary email associated with this IContactInfo
     *
     * @return <code>null</code> if implementation does not support this.
     */
    public String getEmail();

    /**
     *
     * @return Returns the preferred contact type in this contact info
     */
    public ContactTypeE getPreferredContactType();

}
