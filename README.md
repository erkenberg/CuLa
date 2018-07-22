# Disable dummy prefil:
* For testing purposes the Room Database is prefilled with some entries for the different tables.
* To disable that:
 * open CulaRepository.java (in package com.sliebald.cula.data) and comment out the following line in the constructor:
  * setDebugState();
 * Afterwards the App is setup without any prefils.


# Setup firebase:
* create a new firebase project:
  * follow firebase instructions on https://console.firebase.google.com/
  * create new project
  * store google-services.json to cula/app/google-services.json
* enable crashlytics:
  * in the firebase console of the project click on Crashlytics
  * should directly work after starting the app once
 * configure google analytics keys in the firebase console:
  
 

# Added Keystore:
* The added keystore is just for Udacity Android nanodegree purpose and not in active use for a real published app!
* The defaults below should already be in the apps build.gradle and should therefore work by default. The text just describes how they were created.
* keystore: 
  * keystore.jks in project directory
  * password: password
  * key alias: cula
  * key password: password
* Usage 
  * set up in app->open module settings-> Signing -> new -> setup with the data from above
  * in module settings -> Build types for release: Signing config set to just created signing config