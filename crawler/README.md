# Crawler of API Discovery Solution

This crawler constantly crawls a list of applications for their API definitions. Currently it works in conjunction
with [Kio](https://github.com/zalando-stups/kio) to get a list of all applications and their service endpoints.
In the background [API discovery storage](../storage) acts as storage engine. This storage can then be used
for other integration scenarios.


## Building

    $ gradle build


## Configuration

Configuration is provided via environment variables during start.

Variable                           | Default | Description
---------------------------------- | ------- | -----------
**JOBS_KIO_URL**                   |         | **Base url of Kio**
**JOBS_TWINTIP_STORAGE_URL**       |         | **Base url of Twintip storage**
OAUTH2_ACCESS_TOKEN_URL            |         | Needed for authentication
CRAWLER_DELAY                      |  300000 | Delay of scheduled crawling task in ms
CRAWLER_JOBS                       |      10 | Number of concurrent crawling jobs
CRAWLER_TIMEOUT_READ               |    5000 | Timeout while reading API definition from service
CRAWLER_TIMEOUT_CONNECT            |    2000 | Timeout while connecting from service
CRAWLER_TIMEOUT_CONNECTION_REQUEST |    1000 | Timeout when requesting connection to a service
