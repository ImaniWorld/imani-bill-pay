package com.imani.bill.pay.service.property.lease;

import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.leasemanagement.PropertyLeaseAgreement;
import com.imani.bill.pay.domain.leasemanagement.PropertyLeaseAgreementLite;
import com.imani.bill.pay.domain.leasemanagement.repository.IPropertyLeaseAgreementRepository;
import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.repository.IApartmentRepository;
import com.imani.bill.pay.domain.property.repository.IPropertyManagerRepository;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserRecordLite;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.property.ILeaseAgreementService;
import com.imani.bill.pay.service.property.LeaseAgreementService;
import com.imani.bill.pay.service.user.IUserResidenceService;
import com.imani.bill.pay.service.user.UserResidenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(ApartmentLeaseService.SPRING_BEAN)
public class ApartmentLeaseService implements IResidentialPropertyLeaseService {


    @Autowired
    private IUserRecordRepository userRecordRepository;

    @Autowired
    private IApartmentRepository iApartmentRepository;

    @Autowired
    private IPropertyManagerRepository iPropertyManagerRepository;

    @Autowired
    private IPropertyLeaseAgreementRepository iPropertyLeaseAgreementRepository;

    @Autowired
    @Qualifier(LeaseAgreementService.SPRING_BEAN)
    private ILeaseAgreementService iLeaseAgreementService;

    @Autowired
    @Qualifier(UserResidenceService.SPRING_BEAN)
    private IUserResidenceService iUserResidenceService;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.property.lease.ResidentialPropertyLeaseService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ResidentialPropertyLeaseService.class);


    @Transactional
    @Override
    public ExecutionResult<PropertyLeaseAgreementLite> leaseProperty(UserRecord userRecord, PropertyLeaseAgreementLite propertyLeaseAgreementLite) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        Assert.notNull(propertyLeaseAgreementLite, "PropertyLeaseAgreementLite cannot be null");

        ExecutionResult<PropertyLeaseAgreementLite> executionResult = new ExecutionResult<>();

        // Find the apartment and execute all validations to make sure that it can be rented
        Optional<Apartment> apartment = iApartmentRepository.findById(propertyLeaseAgreementLite.getLeasedApartmentID());
        LOGGER.info("Processing apartment lease request from [{} - {}]", propertyLeaseAgreementLite.getEffectiveDate(), propertyLeaseAgreementLite.getTerminationDate());

        if(apartment.isPresent() && !apartment.get().isRented()) {
            LOGGER.info("Finalizing leasing of apartment with location:=> {}", apartment.get().getDescriptiveLocation());

            EmbeddedAgreement embeddedAgreement = EmbeddedAgreement.builder()
                    .agreementUserRecord(userRecord)
                    .effectiveDate(propertyLeaseAgreementLite.getEffectiveDate())
                    .terminationDate(propertyLeaseAgreementLite.getTerminationDate())
                    .fixedCost(propertyLeaseAgreementLite.getFixedCost())
                    .billScheduleTypeE(propertyLeaseAgreementLite.getBillScheduleTypeE())
                    .agreementInForce(true)
                    .build();

            PropertyLeaseAgreement propertyLeaseAgreement = PropertyLeaseAgreement.builder()
                    .leasedApartment(apartment.get())
                    .embeddedAgreement(embeddedAgreement)
                    .build();

            apartment.get().setRented(true);
            apartment.get().setRentedByUser(userRecord);
            iPropertyLeaseAgreementRepository.save(propertyLeaseAgreement);
            iApartmentRepository.save(apartment.get());
            executionResult.setResult(propertyLeaseAgreement.toPropertyLeaseAgreementLite());
        } else {
            LOGGER.info("Apartment requested is currently not available to rent");
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("Apartment is currently not available to rent."));
        }

        return executionResult;
    }

    @Transactional
    @Override
    public ExecutionResult<PropertyLeaseAgreementLite> leaseProperty(UserRecordLite userRecordLite, PropertyLeaseAgreementLite propertyLeaseAgreementLite) {
        Assert.notNull(userRecordLite, "UserRecordLite cannot be null");
        Assert.notNull(propertyLeaseAgreementLite, "PropertyLeaseAgreementLite cannot be null");
        UserRecord userRecord = userRecordRepository.findByUserEmail(userRecordLite.getEmail());
        return leaseProperty(userRecord, propertyLeaseAgreementLite);
    }
}
