#!/bin/bash
# Author  : Mark A. Heckler
# Notes   : Run with 'source AsaInitEnv.sh' from your shell/commandline environment
# History : 20231018 Official "version 1"

# REQUIREMENTS
## Azure CLI (az)

# Your Azure ID
# export AZUREID='<your_azure_id>'
export AZUREID='mkheck'
export APP_NAME='neoai'

# Establish seed for random naming
export RANDOMIZER=$RANDOM
# export RANDOMIZER=20303

# Azure subscription to use
# NOTE: The $AZ_SUBSCRIPTION variable must be set outside of this script or you will have to enter it directly below
#       and uncomment this line:
# export AZ_SUBSCRIPTION=<your_Azure_subscription_id_here>

# Where your Azure resources will reside
export AZ_RESOURCE_GROUP=$AZUREID'-'$RANDOMIZER'-rg'
export AZ_LOCATION='eastus'

# Azure Spring Apps
export AZ_ASA_SERVICE=$AZUREID'-'$RANDOMIZER'-service'
export AZ_ASA_APP=$AZUREID'-'$APP_NAME
## Set the project path to the location of your Spring Boot project
export AZ_PROJECT_PATH='.'
export AZ_APP_PATH=$AZ_PROJECT_PATH'/target/'$APP_NAME'-0.0.1-SNAPSHOT.jar'
