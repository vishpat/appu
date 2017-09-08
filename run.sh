#!/bin/sh
export http_proxy="http://proxy.lbs.alcatel-lucent.com:8000/"
export https_proxy="https://proxy.lbs.alcatel-lucent.com:8000/"
export GOOGLE_APPLICATION_CREDENTIALS=/home/vpati011/.Jarvis.googleapi.json

mvn exec:java -Dexec.mainClass="com.akruty.appu.App" 
