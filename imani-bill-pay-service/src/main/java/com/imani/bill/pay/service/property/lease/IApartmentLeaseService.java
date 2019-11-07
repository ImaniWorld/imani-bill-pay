package com.imani.bill.pay.service.property.lease;

import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayResponse;
import com.imani.bill.pay.domain.property.gateway.LeaseAgreementRequest;

/**
 * @author manyce400
 */
public interface IApartmentLeaseService {

    public APIGatewayEvent<LeaseAgreementRequest, GenericAPIGatewayResponse> leaseApartment(APIGatewayEvent<LeaseAgreementRequest, GenericAPIGatewayResponse> apiGatewayEvent);
}
