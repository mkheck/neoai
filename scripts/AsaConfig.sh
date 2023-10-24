#!/bin/bash
# Author  : Mark A. Heckler
# Notes   : Run with 'source AsaConfig.sh' from your shell/commandline environment AFTER AsaInitEnv.sh
# History : 20231018 Official "version 1"

# REQUIREMENTS
## Azure CLI (az)

# Create resource group
echo "az group create -n $AZ_RESOURCE_GROUP -l $AZ_LOCATION --subscription $AZ_SUBSCRIPTION"
az group create -n $AZ_RESOURCE_GROUP -l $AZ_LOCATION --subscription $AZ_SUBSCRIPTION

# Create Spring Apps service
echo "az spring create -n $AZ_ASA_SERVICE -g $AZ_RESOURCE_GROUP -l $AZ_LOCATION --disable-app-insights false"
az spring create -n $AZ_ASA_SERVICE -g $AZ_RESOURCE_GROUP -l $AZ_LOCATION --disable-app-insights false

# Create the ASA app construct
echo "az spring app create -n $AZ_ASA_APP -g $AZ_RESOURCE_GROUP -s $AZ_ASA_SERVICE"
az spring app create -n $AZ_ASA_APP -g $AZ_RESOURCE_GROUP -s $AZ_ASA_SERVICE \
    --instance-count 1 --memory 2Gi --runtime-version Java_17 --assign-endpoint true

# Build the Spring Boot application
cd $AZ_PROJECT_PATH
./mvnw clean package

# Deploy the application
# NOTE: The $SPRING* variables are set outside of these scripts. Please see the README for the parent repo.
echo "Deploying $AZ_ASA_APP\n"
az spring app deploy -n $AZ_ASA_APP \
    -g $AZ_RESOURCE_GROUP -s $AZ_ASA_SERVICE \
    --artifact-path $AZ_APP_PATH \
    --jvm-options='-Xms2048m -Xmx2048m' \
    --env SPRING_AI_AZURE_OPENAI_API_KEY=$SPRING_AI_AZURE_OPENAI_API_KEY \
          SPRING_AI_AZURE_OPENAI_ENDPOINT=$SPRING_AI_AZURE_OPENAI_ENDPOINT \
          SPRING_AI_AZURE_OPENAI_MODEL=$SPRING_AI_AZURE_OPENAI_MODEL \
          SPRING_NEO4J_URI=$SPRING_NEO4J_URI \
          SPRING_NEO4J_AUTHENTICATION_USERNAME=$SPRING_NEO4J_AUTHENTICATION_USERNAME \
          SPRING_NEO4J_AUTHENTICATION_PASSWORD=$SPRING_NEO4J_AUTHENTICATION_PASSWORD
