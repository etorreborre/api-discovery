package org.zalando.apidiscovery.crawler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestOperations;
import org.zalando.stups.clients.kio.ApplicationBase;
import org.zalando.apidiscovery.crawler.storage.ApiDefinition;
import org.zalando.apidiscovery.crawler.storage.ApiDiscoveryStorageClient;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.concurrent.Callable;

class ApiDefinitionCrawlJob implements Callable<Void> {

    private static final Logger LOG = LoggerFactory.getLogger(ApiDefinitionCrawlJob.class);

    private final ApiDiscoveryStorageClient storageClient;
    private final RestOperations schemaClient;
    private final ApplicationBase app;

    ApiDefinitionCrawlJob(ApiDiscoveryStorageClient storageClient, RestOperations schemaClient, ApplicationBase app) {
        this.storageClient = storageClient;
        this.schemaClient = schemaClient;
        this.app = app;
    }

    @Override
    public Void call() throws Exception {
        try {
            final String serviceUrl = app.getServiceUrl().endsWith("/") ? app.getServiceUrl() : app.getServiceUrl() + "/";
            final Optional<JsonNode> schemaDiscovery = retrieveSchemaDiscovery(serviceUrl);

            if (schemaDiscovery.isPresent()) {
                final JsonNode schemaDiscoveryInformation = schemaDiscovery.get();
                String apiDefinitionUrl = schemaDiscoveryInformation.get("schema_url").asText();
                if (apiDefinitionUrl.startsWith("/")) {
                    apiDefinitionUrl = apiDefinitionUrl.substring(1);
                }

                final String schemaType = schemaDiscoveryInformation.get("schema_type").asText("");
                final JsonNode apiDefinition = retrieveApiDefinition(serviceUrl + apiDefinitionUrl);

                final ApiDefinition updateApiDefinitionRequest = new ApiDefinition(
                        "SUCCESS",
                        schemaType,
                        apiDefinition.get("info").get("title").asText(),
                        apiDefinition.get("info").get("version").asText(),
                        serviceUrl,
                        apiDefinitionUrl,
                        schemaDiscoveryInformation.has("ui_url") ? schemaDiscoveryInformation.get("ui_url").asText() : null,
                        apiDefinition.toString()
                );

                storageClient.createOrUpdateApiDefintion(updateApiDefinitionRequest, app.getId());
                LOG.info("Successfully crawled api definition of {}", app.getId());
            } else {
                storageClient.createOrUpdateApiDefintion(ApiDefinition.UNSUCCESSFUL, app.getId());
                LOG.info("Api definition unavailable for {}", app.getId());
            }
            return null;
        } catch (Exception e) {
            storageClient.createOrUpdateApiDefintion(ApiDefinition.UNSUCCESSFUL, app.getId());
            LOG.info("Could not crawl {}: {}", app.getId(), e.getMessage());
            return null;
        }
    }

    private Optional<JsonNode> retrieveSchemaDiscovery(String serviceUrl) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "*/*");
            ResponseEntity<JsonNode> responseEntity = schemaClient.exchange(
                    serviceUrl + ".well-known/schema-discovery", HttpMethod.GET, new HttpEntity<>(headers), JsonNode.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return Optional.of(responseEntity.getBody());
            } else if (responseEntity.getStatusCode().value() == 404) {
                LOG.info("Service {} does not implement api discovery", app.getId());
            } else {
                LOG.info("Error while loading api discovery of service {}: {}", app.getId(), responseEntity.getStatusCode());
            }
        } catch (ResourceAccessException e) {
            if (e.getCause() instanceof UnknownHostException) {
                LOG.info("Host for service {} is not reachable: {}", app.getId(), serviceUrl);
            } else {
                LOG.info("Service {} is not reachable: {}", app.getId(), e.getMessage());
            }
        } catch (Exception e) {
            LOG.info("Could not load api discovery info for service {}: {}", app.getId(), e.getMessage());
        }
        return Optional.empty();
    }

    private JsonNode retrieveApiDefinition(String url) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "*/*");
            ResponseEntity<JsonNode> responseEntity = schemaClient.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), JsonNode.class);

            return responseEntity.getStatusCode().is2xxSuccessful()
                    ? responseEntity.getBody()
                    : tryRetrieveApiDefinitionAsYaml(url);
        } catch (Exception e) {
            return tryRetrieveApiDefinitionAsYaml(url);
        }
    }

    private JsonNode tryRetrieveApiDefinitionAsYaml(String url) throws IOException {
        LOG.info("Try to load api definition as yaml for service {}", app.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "*/*");
        ResponseEntity<String> yamlApiDefinition = schemaClient.exchange(url, HttpMethod.GET, new HttpEntity(headers), String.class);

        if (!yamlApiDefinition.getStatusCode().is2xxSuccessful()) {
            throw new HttpClientErrorException(yamlApiDefinition.getStatusCode(), "Could not load yaml api definition");
        }
        return new ObjectMapper(new YAMLFactory()).readValue(yamlApiDefinition.getBody(), JsonNode.class);
    }
}
