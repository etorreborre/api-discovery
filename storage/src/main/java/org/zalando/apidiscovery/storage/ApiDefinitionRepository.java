package org.zalando.apidiscovery.storage;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface ApiDefinitionRepository extends CrudRepository<ApiDefinition, String> {

    @Query("select a from ApiDefinition a where a.status = 'UNSUCCESSFUL' and (a.lastChanged is null or a.lastChanged < ?1)")
    List<ApiDefinition> findOlderThanAndUnsuccessful(DateTime olderThan);

    @Query("select a from ApiDefinition a where a.lastPersisted is null or a.lastPersisted < ?1")
    List<ApiDefinition> findNotUpdatedSince(DateTime olderThan);

    @Query("select a from ApiDefinition a where a.lifecycleState = 'INACTIVE' and (a.lastPersisted is null or a.lastPersisted < ?1)")
    List<ApiDefinition> findNotUpdatedSinceAndInactive(DateTime olderThan);

    List<ApiDefinition> findByLifecycleState(String lifecycleState);
}
