# Swagger UI

This is Swagger UI application. It is currently used to browse all crawled API definitions.
It relies on the API definitions which are persisted in the [storage](../storage) service.


## Build and run locally

In order to start the application locally (e.g. for testing or development purposes) you should build and bundle the sources first:

```
cd server && npm install
gulp
```

then, you can start the server locally without OAuth integration:


```
gulp serve
```

or, with OAuth integration:

```
gulp serve serve-with-oauth
```

Now, you can access the frontend <http://localhost:8080>.
