docker run \
    -it \
    -p 8080:8080 \
    -e SUIENV_STORAGE_BASE_URL=http://localhost:5001 \
    -e SUIENV_OAUTH_REALM=realm \
    -e SUIENV_OAUTH_CLIENT_ID=swagger \
    -e SUIENV_OAUTH_AUTH_URL=http://localhost:5002/auth \
    -e SUIENV_OAUTH_REDIRECT_URL=http://192.168.59.103:8080 \
    -e SUIENV_OAUTH_SCOPES=uid \
    -u 998 \
    $1
