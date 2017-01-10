package org.zalando.apidiscovery.storage;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ApiLifecycleManagerTest {

    @Test
    public void outdatedApiDefinitionShouldBeMarkedAsInactive() {
        ApiDefinition apiDefinition = spy(new ApiDefinition());
        apiDefinition.setApplicationId("test");
        apiDefinition.setStatus("UNSUCCESSFUL");
        apiDefinition.setLifecycleState(ApiLifecycleManager.ACTIVE);
        List<ApiDefinition> outdatedApis = Collections.singletonList(apiDefinition);

        ApiDefinitionRepository mockedRepository = mock(ApiDefinitionRepository.class);
        when(mockedRepository.findOlderThanAndUnsuccessful(any(DateTime.class))).thenReturn(outdatedApis);

        ApiLifecycleManager lifecycleManager = new ApiLifecycleManager(mockedRepository, 1, Integer.MAX_VALUE);
        lifecycleManager.inactivateApis(DateTime.now());

        verify(mockedRepository).save(outdatedApis);
        verify(apiDefinition).setLifecycleState(ApiLifecycleManager.INACTIVE);
    }

    @Test
    public void outdatedApiDefinitionShouldBeMarkedAsDecommissioned() {
        ApiDefinition apiDefinition = spy(new ApiDefinition());
        apiDefinition.setApplicationId("test");
        apiDefinition.setStatus("UNSUCCESSFUL");
        apiDefinition.setLifecycleState(ApiLifecycleManager.INACTIVE);
        List<ApiDefinition> outdatedApis = Collections.singletonList(apiDefinition);

        ApiDefinitionRepository mockedRepository = mock(ApiDefinitionRepository.class);
        when(mockedRepository.findNotUpdatedSinceAndInactive(any(DateTime.class))).thenReturn(outdatedApis);

        ApiLifecycleManager lifecycleManager = new ApiLifecycleManager(mockedRepository, 1, Integer.MAX_VALUE);
        lifecycleManager.decomissionApis(DateTime.now());

        verify(mockedRepository).save(outdatedApis);
        verify(apiDefinition).setLifecycleState(ApiLifecycleManager.DECOMMISSIONED);
    }
}
