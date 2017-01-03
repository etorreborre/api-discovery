package org.zalando.stups.twintip.crawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;
import org.zalando.stups.clients.kio.KioOperations;
import org.zalando.stups.clients.kio.spring.RestTemplateKioOperations;
import org.zalando.stups.oauth2.spring.client.StupsOAuth2RestTemplate;
import org.zalando.stups.oauth2.spring.client.StupsTokensAccessTokenProvider;
import org.zalando.stups.spring.http.client.ClientHttpRequestFactorySelector;
import org.zalando.stups.spring.http.client.TimeoutConfig;
import org.zalando.stups.tokens.AccessTokens;
import org.zalando.stups.twintip.crawler.storage.RestTemplateTwintipOperations;

@Configuration
public class ClientsConfiguration {

    @Autowired
    private AccessTokens accessTokens;

    @Value("${crawler.timeout.read}")
    private int readTimeout = 5000;

    @Value("${crawler.timeout.connect}")
    private int connectTimeout = 2000;

    @Value("${crawler.timeout.connection-request}")
    private int connectionRequestTimeout = 1000;

    @Bean
    public KioOperations kioOperations(@Value("${jobs.kio.url}") String kioBaseUrl) {
        return new RestTemplateKioOperations(buildOAuth2RestTemplate("kio"), kioBaseUrl);
    }

    @Bean
    public RestTemplateTwintipOperations twintipOperations(@Value("${jobs.twintip.storage.url}") String twintipBaseUrl) {
        return new RestTemplateTwintipOperations(buildOAuth2RestTemplate("twintip"), twintipBaseUrl);
    }

    @Bean
    public RestOperations oauth2Operations() {
        return buildOAuth2RestTemplate("schema");
    }

    private RestOperations buildOAuth2RestTemplate(final String tokenName) {
        return new StupsOAuth2RestTemplate(new StupsTokensAccessTokenProvider(tokenName, accessTokens),
                ClientHttpRequestFactorySelector.getRequestFactory(new TimeoutConfig.Builder()
                        .withReadTimeout(readTimeout)
                        .withConnectTimeout(connectTimeout)
                        .withConnectionRequestTimeout(connectionRequestTimeout)
                        .build()));
    }
}
