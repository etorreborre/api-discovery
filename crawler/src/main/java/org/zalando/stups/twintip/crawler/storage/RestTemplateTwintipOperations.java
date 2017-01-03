package org.zalando.stups.twintip.crawler.storage;

import org.springframework.util.Assert;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RestTemplateTwintipOperations {

    private final RestOperations restOperations;
    private final String baseUrl;

    public RestTemplateTwintipOperations(RestOperations restOperations, String baseUrl) {
        this.restOperations = restOperations;
        this.baseUrl = baseUrl;
    }

    public void createOrUpdateApiDefintion(ApiDefinition request, String applicationId) {
        Assert.hasText(applicationId, "applicationId must not be blank");

        final Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("applicationId", applicationId);
        restOperations.put(baseUrl + "/apps/{applicationId}", request, uriVariables);
    }

    public Optional<ApiDefinition> getApiDefinition(String applicationId) {
        Assert.hasText(applicationId, "applicationId must not be blank");

        return Optional.ofNullable(restOperations.getForObject(URI.create(baseUrl + "/apps/" + applicationId), ApiDefinition.class));
    }
}
