[![Build Status](https://travis-ci.org/zalando-incubator/api-discovery.svg?branch=master)](https://travis-ci.org/zalando-incubator/api-discovery)

# API Discovery Solution for Microservice Architectures

### Purpose

In Microservice architectures many services and APIs are built. Over time it becomes quite hard for each developer to keep track which APIs exist and what services an API offers. As a solution for this problem, this API Discovery offers easy to implement auto discovery of available APIs and gathers all found API specifications in one central storage system. It's built upon OpenAPI specifications (formerly Swagger). It also comes with an adapted Swagger UI to display all APIs which were stored.

* TODO add hint for future plans to open it for other organizations which don't run KIO
* TODO add hint for future plan to add docker compose script

### Architecture

    +------------+     +---------+     +---------+     +------+
    | Swagger UI +----^+ Storage +^----+ Crawler +----^+ APIs |
    +------------+     +---------+     +---------+     +------+

### Dependencies

- Java 8
- NPM

### Installation

Please refer to individual installation instructions of sub-projects for more detailed information and configuration parameters.

TODO
