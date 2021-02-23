package com.imani.bill.pay.service.property.builder;

import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayResponse;
import com.imani.bill.pay.domain.geographical.Borough;
import com.imani.bill.pay.domain.geographical.City;
import com.imani.bill.pay.domain.geographical.repository.IBoroughRepository;
import com.imani.bill.pay.domain.geographical.repository.ICityRepository;
import com.imani.bill.pay.domain.property.Floor;
import com.imani.bill.pay.domain.property.IHasPropertyData;
import com.imani.bill.pay.domain.property.Property;
import com.imani.bill.pay.domain.property.gateway.PropertyBuilderRequest;
import com.imani.bill.pay.domain.property.repository.IPropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(PropertyBuilderService.SPRING_BEAN)
public class PropertyBuilderService implements IPropertyBuilderService {


    @Autowired
    private ICityRepository iCityRepository;

    @Autowired
    private IBoroughRepository iBoroughRepository;

    @Autowired
    private IPropertyRepository iPropertyRepository;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PropertyBuilderService.class);

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.property.builder.PropertyBuilderService";


    @Transactional
    @Override
    public Optional<Property> buildAndRecordProperty(IHasPropertyData iHasPropertyData) {
        Assert.notNull(iHasPropertyData, "IHasPropertyData cannot be null");

        LOGGER.info("Building Property from iHasPropertyData:> {}", iHasPropertyData);

        boolean hasValidPropertyData = hasValidPropertyData(iHasPropertyData);

        if (hasValidPropertyData) {
            LOGGER.debug("Property data has been validated, checking to see if this property already exists.");
            String propertyNumber = iHasPropertyData.getPropertyNumber();
            String streetName = iHasPropertyData.getStreetName();
            String zipCode = iHasPropertyData.getZipCode();

            // Try to find a matching property that exists with the same information. Theoretically this should always be exactly 1 match if found.
            Optional<Borough> borough = iBoroughRepository.findById(iHasPropertyData.getBoroID());
            Optional<City> city = iCityRepository.findById(iHasPropertyData.getCityID());
            List<Property> properties = new ArrayList<>();//iPropertyRepository.findUniqueProperties(propertyNumber, streetName, zipCode, borough.get());

            if(properties == null || properties.size() == 0) {
                LOGGER.info("No matching property already saved, proceeding to build and save new property data.....");
                Property property = createProperty(iHasPropertyData, borough);
                iPropertyRepository.save(property);
                return Optional.of(property);
            } else {
                LOGGER.warn("Matching property found with these details, skipping recording of new property....");
            }
        }

        LOGGER.warn("Property data is not valid, cannot build and record property");

        return Optional.empty();
    }

    @Override
    public APIGatewayEvent<PropertyBuilderRequest, GenericAPIGatewayResponse> buildProperty(PropertyBuilderRequest propertyBuilderRequest) {
        Assert.notNull(propertyBuilderRequest, "propertyBuilderRequest cannot be null");
        Optional<Property> builtProperty = buildAndRecordProperty(propertyBuilderRequest);
        return null;
    }

    Property createProperty(IHasPropertyData iHasPropertyData, Optional<Borough> borough) {
        Property property = Property.builder()
                .block(iHasPropertyData.getBlock())
                .lot(iHasPropertyData.getLot())
                .buildingIdentificationNumber(iHasPropertyData.getBin())
                .propertyTypeE(iHasPropertyData.getPropertyTypeE())
                .build();

        // Initialize all legal floors on the property
        initializePropertyFloors(property, iHasPropertyData);
        return property;
    }

    void initializePropertyFloors(Property property, IHasPropertyData iHasPropertyData) {
        LOGGER.debug("Initializing and building all legal floors information for new property.....");

        int totalFloors = iHasPropertyData.getLegalStories();

        for(int i=1; i<= totalFloors; i++) {
            Floor floor = Floor.builder()
                    .floorNumber(i)
                    .property(property)
                    .build();
            property.addToFloors(floor);
        }
    }

    boolean hasValidPropertyData(IHasPropertyData iHasPropertyData) {
        LOGGER.debug("Validating property data for iHasPropertyData:=> {}", iHasPropertyData);

        if(iHasPropertyData.getPropertyNumber() != null
                && iHasPropertyData.getStreetName() != null
                && iHasPropertyData.getBoroID() != null
                && iHasPropertyData.getLegalStories() !=null
                && iHasPropertyData.getZipCode() != null
                && iHasPropertyData.getPropertyTypeE() != null
                && iHasPropertyData.getPropertyTypeE() != null) {
            return true;
        }

        return false;
    }


}
