# Storage of API Discovery Solution

This is the storage system of API Discovery. It stores a given API definition, enriches it
with some metadata like `last_changed` information and offers reading endpoints for UI integration.
You can find the API definition of this storage system [here](src/main/resources/api/storage-api.yaml).


## Building

    $ gradle build


## Configuration

Configuration is provided via environment variables during start.

Bold means mandatory, rest is optional.

Variable                            | Default                                     | Description
----------------------------------- | ------------------------------------------- | -----------
SPRING_DATASOURCE_URL               | jdbc:hsqldb:mem:storage;sql.syntax_pgs=true | Database URL
SPRING_DATASOURCE_USERNAME          | sa                                          | Database Username
SPRING_DATASOURCE_PASSWORD          |                                             | Database User Password
SPRING_OAUTH2_RESOURCE_TOKENINFOURI | https://info.services.auth.zalando.com/oauth2/tokeninfo | OAuth2 Token Info endpoint
LIFECYCLE_CHECK_DELAY               | 300000                                      | Interval of crawling in ms
INACTIVE_TIME                       | 600                                         | Seconds after an active API is marked as inactive
DECOMISSIONED_TIME                  | 3600                                        | Seconds after an inactive API is marked as decomissioned
