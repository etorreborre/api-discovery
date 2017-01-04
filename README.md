[![Build Status](https://travis-ci.org/zalando-incubator/api-discovery.svg?branch=master)](https://travis-ci.org/zalando-incubator/api-discovery)

# API Discovery Solution for Microservice Architectures

### Purpose

In Microservice architectures many services and APIs are built. Over time it becomes quite hard for each developer to keep track which APIs exist and what services an API offers. As a solution for this problem, this API Discovery solution offers easy to implement [auto discovery](https://github.com/zalando-incubator/api-discovery/tree/master/crawler) of available APIs and gathers all found API specifications in one central [storage](https://github.com/zalando-incubator/api-discovery/tree/master/storage) system. It's built upon OpenAPI specifications (formerly Swagger). It also comes with an adapted [Swagger UI](https://github.com/zalando-incubator/api-discovery/tree/master/swagger-ui) to display all APIs.

IMPORTANT: Currently the crawler is still depending on an application registry, called [Kio](https://github.com/zalando-stups/kio). From this application registry all services, which potentially have an API definition, are crawled. In near future, this will change and retrieving of crawlable services will be configurable. By this you can adapt API Disocvery to your needs. Please contribute if you have requirements or ideas to [this issue](https://github.com/zalando-incubator/api-discovery/issues/2).


### Architecture

    +------------+     +---------+     +---------+     +------+
    | Swagger UI +---->+ Storage +<----+ Crawler +---->+ APIs |
    +------------+     +---------+     +---------+     +------+


### Dependencies

- Java 8
- NPM


### Installation

Please refer to individual installation instructions of sub-projects for more detailed information and configuration parameters.

- TODO add build script to automatically build projects, docker images and start docker compose


### Contributing

We are happy to accept contributions. First, take a look at our [contributing guidelines](CONTRIBUTING.md).


### TODO

Please check the [Issues Page](https://github.com/zalando-incubator/api-discovery/issues)
for contribution ideas.


### Contact

Feel free to contact one the [maintainers](MAINTAINERS)


### License

MIT
