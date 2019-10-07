package com.imani.bill.pay.service.geographical;

import com.imani.bill.pay.domain.geographical.Borough;
import com.imani.bill.pay.domain.geographical.repository.IBoroughRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(BoroughService.SPRING_BEAN)
public class BoroughService implements IBoroughService {



    @Autowired
    private IBoroughRepository iBoroughRepository;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BoroughService.class);

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.geographical.BoroughService";

    @Override
    public Optional<Borough> findByID(Long id) {
        Assert.notNull(id, "id cannot be null");
        LOGGER.debug("Finding Borough by id: {}", id);
        return iBoroughRepository.findById(id);
    }

    @Override
    public Optional<Borough> findByName(String name) {
        Assert.notNull(name, "name cannot be null");
        LOGGER.debug("Finding Borough by name: {}", name);
        return iBoroughRepository.findByName(name);
    }

    @Override
    public List<Borough> findAll() {
        LOGGER.debug("Returning all Borough data...");
        return iBoroughRepository.findAll();
    }
}