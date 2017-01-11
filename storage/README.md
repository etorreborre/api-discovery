# Storage of API Discovery Solution

This is the storage system of API Discovery. It stores a given API definition, enriches it
with some metadata like `last_changed` information and offers reading endpoints for UI integration.
You can find the API definition of this storage system [here](src/main/resources/api/storage-api.yaml).

This storage system also implements automatic API lifecycle management: if an API definition could
not be crawled for a configured amount of time it will be marked as inactive. If an inactive API
could not be crawled for an even longer configured amount of time it will be marked as decommissioned.
All of thos lifecycle states of an API are accessible via the [storage api](src/main/resources/api/storage-api.yaml).


## Building

    $ ./gradlew build


## Configuration

Configuration is provided via environment variables during start. Per default all endpoints are OAuth2 protected.
You can disable this by providing `SPRING_PROFILES_ACTIVE = dev` as environment variable. This will also enable
an in-memory database which you can override by `SPRING_DATASOURCE_URL` parameter.

Variable                            | Default                                     | Description
----------------------------------- | ------------------------------------------- | -----------
SPRING_DATASOURCE_URL               | jdbc:hsqldb:mem:storage;sql.syntax_pgs=true | Database URL
SPRING_DATASOURCE_USERNAME          | sa                                          | Database Username
SPRING_DATASOURCE_PASSWORD          |                                             | Database User Password
SPRING_OAUTH2_RESOURCE_TOKENINFOURI | https://info.services.auth.zalando.com/oauth2/tokeninfo | OAuth2 Token Info endpoint
LIFECYCLE_CHECK_DELAY               | 300000                                      | Interval of crawling in ms
INACTIVE_TIME                       | 600                                         | Seconds after an active API is marked as inactive
DECOMMISSIONED_TIME                 | 3600                                        | Seconds after an inactive API is marked as decomissioned
