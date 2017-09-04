#!/bin/sh

export GOOGLE_APPLICATION_CREDENTIALS=/home/vpati011/.Jarvis.googleapi.json
mvn exec:java -Dexec.mainClass="com.akruty.pappu.App" 
