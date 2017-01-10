package org.zalando.apidiscovery.storage;

import org.assertj.core.api.Assertions;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.zalando.apidiscovery.storage.TestDataHelper.createBasicApiDefinition;
import static org.zalando.apidiscovery.storage.TestDataHelper.createInactiveApiDefinition;
import static org.zalando.apidiscovery.storage.TestDataHelper.createUnsuccessfulApiDefinition;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DatabaseIntegrationTest {

    @Autowired
    private ApiDefinitionRepository repository;

    @Before
    public void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    public void persistGivenApiDefinitionAndRetrieveItFromDatabase() {
        ApiDefinition apiDefinition = createBasicApiDefinition();

        apiDefinition = repository.save(apiDefinition);

        assertThat(repository.findOne(apiDefinition.getApplicationId())).isEqualTo(apiDefinition);
    }

    @Test
    public void persistApiDefinitionWithMetadataFields() {
        ApiDefinition apiDefinition = createBasicApiDefinition();
        apiDefinition.setCreated(DateTime.now());
        apiDefinition.setLastChanged(DateTime.now());
        apiDefinition.setLastPersisted(DateTime.now());

        apiDefinition = repository.save(apiDefinition);

        assertThat(repository.findOne(apiDefinition.getApplicationId())).isEqualTo(apiDefinition);
    }

    @Test
    public void persistApiDefinitionWithServiceUrl() {
        ApiDefinition apiDefinition = createBasicApiDefinition();
        apiDefinition.setServiceUrl("http://service.de");

        apiDefinition = repository.save(apiDefinition);

        assertThat(repository.findOne(apiDefinition.getApplicationId())).isEqualTo(apiDefinition);
    }

    @Test
    public void persistApiDefinitionWithLifecycleState() {
        ApiDefinition apiDefinition = repository.save(createBasicApiDefinition());

        apiDefinition.setLifecycleState(ApiLifecycleManager.INACTIVE);
        repository.save(apiDefinition);

        assertThat(repository.findOne(apiDefinition.getApplicationId()).getLifecycleState()).isEqualTo(ApiLifecycleManager.INACTIVE);
    }

    @Test
    public void shouldFindAllOldApiDefinitionsWhichAreUnsuccessful() {
        ApiDefinition apiDefinition = createUnsuccessfulApiDefinition();
        apiDefinition.setLastChanged(DateTime.now().minusSeconds(10));
        apiDefinition = repository.save(apiDefinition);

        repository.save(createUnsuccessfulApiDefinition());
        repository.save(createUnsuccessfulApiDefinition());

        List<ApiDefinition> foundApi = repository.findOlderThanAndUnsuccessful(DateTime.now().minusSeconds(5));
        Assertions.assertThat(foundApi).containsOnly(apiDefinition);
    }

    @Test
    public void shouldFindAllOldApiDefinitionsWhichAreUnsuccessfulAndHaveNoLastChangedDateTimeSet() {
        ApiDefinition apiDefinition = createUnsuccessfulApiDefinition();
        apiDefinition.setLastChanged(null);
        apiDefinition = repository.save(apiDefinition);

        repository.save(createUnsuccessfulApiDefinition());
        repository.save(createUnsuccessfulApiDefinition());

        List<ApiDefinition> foundApi = repository.findOlderThanAndUnsuccessful(DateTime.now().minusSeconds(10));
        Assertions.assertThat(foundApi).containsOnly(apiDefinition);
    }

    @Test
    public void shouldFindAllNotUpdatedApiDefinitions() {
        ApiDefinition apiDefinition = createBasicApiDefinition();
        apiDefinition.setLastPersisted(DateTime.now().minusSeconds(10));
        apiDefinition = repository.save(apiDefinition);

        ApiDefinition apiDefinitionWithNullLastPersisted = createUnsuccessfulApiDefinition();
        apiDefinitionWithNullLastPersisted.setLastPersisted(null);
        apiDefinitionWithNullLastPersisted = repository.save(apiDefinitionWithNullLastPersisted);

        repository.save(createBasicApiDefinition());
        repository.save(createUnsuccessfulApiDefinition());

        List<ApiDefinition> foundApi = repository.findNotUpdatedSince(DateTime.now().minusSeconds(5));
        Assertions.assertThat(foundApi).containsOnly(apiDefinition, apiDefinitionWithNullLastPersisted);
    }

    @Test
    public void shouldFindAllNotUpdatedAndInactiveApiDefinitions() {
        ApiDefinition apiDefinition = createInactiveApiDefinition();
        apiDefinition.setLastPersisted(DateTime.now().minusSeconds(10));
        apiDefinition = repository.save(apiDefinition);

        ApiDefinition apiDefinitionWithNullLastPersisted = createInactiveApiDefinition();
        apiDefinitionWithNullLastPersisted.setLastPersisted(null);
        apiDefinitionWithNullLastPersisted = repository.save(apiDefinitionWithNullLastPersisted);

        repository.save(createBasicApiDefinition());
        repository.save(createUnsuccessfulApiDefinition());

        List<ApiDefinition> foundApi = repository.findNotUpdatedSince(DateTime.now().minusSeconds(5));
        Assertions.assertThat(foundApi).containsOnly(apiDefinition, apiDefinitionWithNullLastPersisted);
    }
}
