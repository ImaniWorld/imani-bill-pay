package com.imani.bill.pay.service.property;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class MonthlyPropertySvcChargeServiceTest extends AbstractMonthlyRentalBillingTest {


    @InjectMocks
    private MonthlyPropertySvcChargeService monthlyPropertySvcChargeService;

    @Before
    public void before() {
        super.before();
    }

    @Test
    public void testApplyMonthlyPropertyServiceCharge() {
//        // Payment due date
//        DateTime rentalMonth = DateTime.parse("2019-09-01 00:00:00", DateTimeUtilTest.DEFAULT_FORMATTER);
//
//        // Build mock bill
//        MonthlyRentalBill monthlyRentalBill = MonthlyRentalBill.builder()
//                .userResidence(userResidence)
//                .rentalAgreement(userResidence.getLeaseAgreement())
//                .rentalMonth(rentalMonth)
//                .build();
//
//        Optional<List<PropertyServiceChargeExplained>> propertyServiceChargeExplainedList = monthlyPropertySvcChargeService.applyMonthlyPropertyServiceCharge(userResidence, monthlyRentalBill);
//        //System.out.println("propertyServiceChargeExplainedList = " + propertyServiceChargeExplainedList);
//
//        // Verify property service explanation
//        Assert.assertEquals(2, propertyServiceChargeExplainedList.get().size());
//
//        propertyServiceChargeExplainedList.get().forEach(propertyServiceChargeExplained -> {
//            String serviceName = propertyServiceChargeExplained.getServiceName();
//            if(serviceName.equals("Monthly Parking")) {
//                Assert.assertEquals(new Double(150.00), propertyServiceChargeExplained.getServiceMonthlyCost());
//            } else {
//                Assert.assertEquals("Monthly Laundry", propertyServiceChargeExplained.getServiceName());
//                Assert.assertEquals(new Double(50.00), propertyServiceChargeExplained.getServiceMonthlyCost());
//            }
//        });

    }
}
