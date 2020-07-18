package com.imani.bill.pay.service.geographical;

import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.billing.BillPayFee;
import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import com.imani.bill.pay.domain.billing.FeePaymentChargeTypeE;
import com.imani.bill.pay.domain.billing.FeeTypeE;
import com.imani.bill.pay.domain.billing.repository.IBillPayFeeRepository;
import com.imani.bill.pay.domain.geographical.GeographicalRegion;
import com.imani.bill.pay.domain.geographical.repository.IGeographicalRegionRepository;
import com.imani.bill.pay.domain.leasemanagement.PropertyLeaseAgreement;
import com.imani.bill.pay.domain.leasemanagement.repository.IPropertyLeaseAgreementRepository;
import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.Property;
import com.imani.bill.pay.domain.property.repository.IApartmentRepository;
import com.imani.bill.pay.domain.property.repository.IPropertyRepository;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.billing.IBillGenerationService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

/**
 * @author manyce400
 */
@Service(GeographicalRegionService.SPRING_BEAN)
public class GeographicalRegionService implements IGeographicalRegionService {


//    @Autowired
//    private StripeAPIConfig stripeAPIConfig;
//
//    @Autowired
//    private PlaidAPIConfig plaidAPIConfig;

    @Autowired
    private IGeographicalRegionRepository iGeographicalRegionRepository;

    @Autowired
    private IApartmentRepository iApartmentRepository;

    @Autowired
    private IUserRecordRepository iUserRecordRepository;

    @Autowired
    private IPropertyRepository iPropertyRepository;

    @Autowired
    private IBillPayFeeRepository iBillPayFeeRepository;

    @Autowired
    private IPropertyLeaseAgreementRepository iPropertyLeaseAgreementRepository;


    @Autowired
    private IBillGenerationService iBillGenerationService;



    public static final String SPRING_BEAN = "com.imani.bill.pay.service.geographical.GeographicalRegionService";


    @Override
    public GeographicalRegion findGeographicalRegionByCode(String regionCode) {
        Assert.notNull(regionCode, "regionCode cannot be null");
        GeographicalRegion geographicalRegion = iGeographicalRegionRepository.findGeographicalRegionByCode(regionCode);
        return geographicalRegion;
    }


    @PostConstruct
    public void runPostConstruct() {
//        generateBill();
//        saveBillPayFee();
    }


    public void generateBill() {
        UserRecord userRecord = iUserRecordRepository.getOne(2L);
        iBillGenerationService.generateImaniBill(userRecord);
    }

    public void saveBillPayFee() {
        Property property = iPropertyRepository.getOne(32253L);
        BillPayFee billPayFee = new  BillPayFee();
        billPayFee.setProperty(property);
        billPayFee.setFeeName("Property Late Fee");
        billPayFee.setFeeDescription("Applied monthly whenever late");
        billPayFee.setFeePaymentChargeTypeE(FeePaymentChargeTypeE.FLAT_AMOUNT_FEE);
        billPayFee.setOptionalFlatAmount(15.00);
        billPayFee.setFeeTypeE(FeeTypeE.LATE_FEE);
        iBillPayFeeRepository.save(billPayFee);
    }


    public void savePropertyLeaseAgreement() {
        //32253
        Apartment apartment = iApartmentRepository.getOne(1L);
        DateTime dateTime = DateTime.now().plusDays(2);

        UserRecord userRecord = iUserRecordRepository.getOne(2L);

        EmbeddedAgreement embeddedAgreement = EmbeddedAgreement.builder()
                .agreementInForce(true)
                .billScheduleTypeE(BillScheduleTypeE.MONTHLY)
                .fixedCost(1500.00)
                .effectiveDate(dateTime)
                .terminationDate(dateTime)
                .userRecord(userRecord)
                .build();

        PropertyLeaseAgreement propertyLeaseAgreement = PropertyLeaseAgreement.builder()
                .leasedApartment(apartment)
                .embeddedAgreement(embeddedAgreement)
                .build();

        iPropertyLeaseAgreementRepository.save(propertyLeaseAgreement);
    }

}
