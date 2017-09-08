# Appu
Voice Controlled Desktop Assistant

# Build
Appu needs Java 8 (or greater) to build.

<pre>
git clone https://github.com/vishpat/appu.git
mvn package
</pre>

# Run
Appu uses the Google Speech API and requires you to have Google developer account to try it out. Information about setting an dev account and obtaining a ServiceAccount can be found [here](https://developers.google.com/identity/protocols/application-default-credentials). Once the service account is created download the json for it store it in your home directory. Now edit the run.sh file andi change the parameter GOOGLE_APPLICATION_CREDENTIALS point it to the downloaded JSON. Now run the app as follows. 

<pre>
./run.sh
</pre>
