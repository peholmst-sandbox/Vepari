# Hardware and Platforms

The intended end users of Vepari - the voluntary fire departments - don't have the economic resources to buy expensive hardware. Nor is is possible to offer Vepari as a service since that would require a large enough organization to maintain it. In addition, not all voluntary fire department can be expected to have IT professionals in their ranks. Therefore, Vepari must run on easily available consumer hardware and must be easy to set up.

## The Runboard

The runboard application will be running 24/7 on monitors in the fire station. There are three potential platforms to build this on:

1. **Web**: The runboard would be a web app that can be accessed by any device that has a web browser and is connected to the local fire station network.
   * *Pros*: This alternative gives the end users the biggest freedom of choice since all that is needed to access the application is a modern web browser. Updates would be distributed by updating the server and refreshing the browsers.
   * *Cons*: It would not be possible to reuse code from the phone app. Many web browsers do not seem to be stable enough to run for many days at a time without a restart. The runboard devices would need to be equipped with some kind of guarddog feature or regular automatic reboots.
2. **Android**: The runboard would be an Android app that can be installed through Google Play on any Android device.
   * *Pros*: Installing the app would be easy through Google Play. It would be possible to reuse code from the phone app.
   * *Cons*: The end users would have to get Android devices that can be connected to Full HD external monitors. Updates would have to be distributed through Google Play and the device would have to be able to automatically restart after an update has been installed.
3. **Native**: The runboard would be a native Windows or Linux application that requires a full-blown Windows or Linux machine to run on.
   * *Pros*: This approach would only make sense if the runboard is going to run on small hardware such as Raspberry Pis or compute sticks and you need to get the most out of the hardware. For PCs, you might just as well go for the web version.
   * *Cons*: It would not be possible to reuse code from the phone app. Updates would have to be installed manually. There is a risk for vendor lock-in.