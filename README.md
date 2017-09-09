# Appu
Voice Controlled Desktop Assistant

# Build
Appu needs Java 8 (or greater) to build.

<pre>
git clone https://github.com/vishpat/appu.git
mvn package
</pre>

# Setup

## Google Speech API
Appu uses the Google Speech API and requires you to have Google developer account to try it out. Information about setting an dev account and obtaining a ServiceAccount can be found [here](https://developers.google.com/identity/protocols/application-default-credentials). Once the service account is created download the json and store it in your home directory. Now edit the run.sh file and change the parameter GOOGLE_APPLICATION_CREDENTIALS to point it to the downloaded JSON. Now run the app as follows. 

## Command Map

Appu uses a [Java Property File](https://docs.oracle.com/cd/E23095_01/Platform.93/ATGProgGuide/html/s0204propertiesfileformat01.html) (A simple text file) to map the commands to actions. This file needs to be named as the **.appu.commands** and needs to be present in the user's home directory. A sample command file for Linux desktop is shown below

<pre>
browser=/usr/bin/chromium-browser
vnc=/usr/bin/vinagre
vpn=/opt/cisco/anyconnect/bin/vpnui
terminal=xterm
reddit=/usr/bin/chromium-browser www.reddit.com
lower\ volume=amixer -D pulse sset Master 20%-
increase\ volume=amixer -D pulse sset Master 20%+
</pre> 

# Run

<pre>
./run.sh
</pre>

# Demo

![Alt Text](https://raw.githubusercontent.com/wiki/vishpat/appu/images/output.gif)

Youtube [video](https://youtu.be/pfociFyKXQI)
