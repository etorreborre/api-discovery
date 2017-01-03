#!/bin/bash

npm install
gulp scm-source
gulp
docker build -t $1 . && docker push $1