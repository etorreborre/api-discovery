package org.zalando.apidiscovery.storage;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
class ApiDefinitionService {

    private final ApiDefinitionRepository repository;

    @Autowired
    public ApiDefinitionService(ApiDefinitionRepository repository) {
        this.repository = repository;
    }

    @Transactional
    void persistWithMetadata(ApiDefinition apiDefinition) {
        final ApiDefinition persistedDefinition = repository.findOne(apiDefinition.getApplicationId());
        final DateTime now = DateTime.now();

        apiDefinition.setStatus(apiDefinition.getStatus().toUpperCase());

        if (persistedDefinition == null) {

            apiDefinition.setCreated(now);
            apiDefinition.setLastChanged(now);
            apiDefinition.setLastPersisted(now);

            if (apiDefinition.getStatus().equals("SUCCESS")) {
                apiDefinition.setLifecycleState(ApiLifecycleManager.ACTIVE);
            } else {
                apiDefinition.setLifecycleState(ApiLifecycleManager.INACTIVE);
            }

        } else {

            apiDefinition.setCreated(persistedDefinition.getCreated());
            apiDefinition.setLastPersisted(now);
            if (!persistedDefinition.equals(apiDefinition)) {
                apiDefinition.setLastChanged(now);
            } else {
                apiDefinition.setLastChanged(persistedDefinition.getLastChanged());
            }

            if (apiDefinition.getStatus().equals("SUCCESS")
                    || StringUtils.isEmpty(persistedDefinition.getLifecycleState())) {
                apiDefinition.setLifecycleState(ApiLifecycleManager.ACTIVE);
            } else {
                apiDefinition.setLifecycleState(persistedDefinition.getLifecycleState());
            }
        }

        repository.save(apiDefinition);
    }
}
