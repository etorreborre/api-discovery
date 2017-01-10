package org.zalando.apidiscovery.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;
import org.zalando.apidiscovery.crawler.storage.ApiDiscoveryStorageClient;
import org.zalando.stups.clients.kio.ApplicationBase;
import org.zalando.stups.clients.kio.KioOperations;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Component
public class ApiDiscoveryCrawler {

    private static final Logger LOG = LoggerFactory.getLogger(ApiDiscoveryCrawler.class);

    private final KioOperations kioClient;
    private final ApiDiscoveryStorageClient storageClient;
    private final RestOperations schemaClient;
    private final ExecutorService fixedPool;

    @Autowired
    public ApiDiscoveryCrawler(KioOperations kioClient,
                               ApiDiscoveryStorageClient storageClient,
                               RestOperations schemaClient,
                               @Value("${crawler.jobs.pool}") int jobsPoolSize) {
        this.kioClient = kioClient;
        this.storageClient = storageClient;
        this.schemaClient = schemaClient;
        fixedPool = Executors.newFixedThreadPool(jobsPoolSize);
    }

    @Scheduled(fixedDelayString = "${crawler.delay}")
    public void crawlApiDefinitions() {
        LOG.info("Start crawling api definitions");

        final List<ApplicationBase> applications = kioClient.listApplications();
        LOG.info("Found {} applications in kio", applications.size());

        final List<Callable<Void>> crawlJobs = applications.stream()
                .filter(app -> !StringUtils.isEmpty(app.getServiceUrl()))
                .map(app -> new ApiDefinitionCrawlJob(storageClient, schemaClient, app))
                .collect(Collectors.toList());
        LOG.info("Crawling {} api definitions", crawlJobs.size());

        try {
            List<Future<Void>> futures = fixedPool.invokeAll(crawlJobs);
            for (Future<Void> future : futures) {
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("Error while crawling", e);
            // swallow exception to not stop crawler
        }

        LOG.info("Finished crawling api definitions");
    }
}
