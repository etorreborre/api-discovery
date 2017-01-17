# Swagger UI

This is Swagger UI application. It is currently used to browse the API definitions. It relies on the API definitions which are persisted in the Storage service.


## Build and run locally

In order to start the application locally (e.g. testing or development purposes) you should build the sources:

```
gulp
```

and build the docker image:


```
docker build -t swagger-ui:1.0-SNAPSHOT .
```

### Run without OAuth 2.0
Now, you can start the docker image and access the frontend <http://localhost:8080>.

```
docker run -it -p 8080:8080 -e SUIENV_STORAGE_BASE_URL=${storage_base_url} swagger-ui:1.0-SNAPSHOT
```

### Run with OAuth 2.0
If you want to test the application with OAuth2.0 security, use `test-docker.sh` script instead:

```
./test-docker.sh swagger-ui:1.0-SNAPSHOT
```
