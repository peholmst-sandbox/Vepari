# Hardware and Platforms

The intended end users of Vepari - the voluntary fire departments - don't have the economic resources to buy expensive hardware. Nor is is possible to offer Vepari as a service since that would require a large enough organization to maintain it. In addition, not all voluntary fire department can be expected to have IT professionals in their ranks. Therefore, Vepari must run on easily available consumer hardware and must be easy to set up.

## The Runboard

The runboard application will be running 24/7 on monitors in the fire station. There are three potential platforms to build this on:

1. **Web**: The runboard would be a web app that can be accessed by any device that has a web browser and is connected to the local fire station network.
   * *Pros*: This alternative gives the end users the biggest freedom of choice since all that is needed to access the application is a modern web browser. Updates would be distributed by updating the server and refreshing the browsers.
   * *Cons*: Many web browsers do not seem to be stable enough to run for many days at a time without a restart. The runboard devices would need to be equipped with some kind of guarddog feature or regular automatic reboots. Code reuse may become difficult.
2. **Android**: The runboard would be an Android app running on a tablet that is connected to a Full HD monitor.
   * *Pros*: It would be possible to reuse code from other Android apps. Installing the app would be easy through Google Play. 
   * *Cons*: The end users would have to get Android devices that can be connected to Full HD external monitors.
3. **Native**: The runboard would be a native Windows or Linux application that requires a full-blown Windows or Linux machine to run on.
   * *Pros*: This approach would only make sense if the runboard is going to run on small hardware such as Raspberry Pis or compute sticks and you need to get the most out of the hardware. For PCs, you might just as well go for the web version.
   * *Cons*: Code reuse may become difficult. There is a risk for vendor lock-in.

## The Vehicle App

The vehicle application will be running on devices in the rescue vehicles. There are two potential platforms to build this on:

1. **Android**: The vehicle application would be an Android app running on a tablet. 
   * *Pros*: It would be possible to reuse code from other Android apps. Installing the app would be easy through Google Play. Tablet devices are cheap and come equipped with everything you need in a mobile environment (GPS, mobile internet, touch screen).
   * *Cons*: The rescue authorities have Windows laptops in their vehicles and use applications that run on Windows. If those applications eventually become available to voluntary rescue units, there would have to be both a laptop and a tablet in the vehicle.

2. **Native**: The vehicle application would be a native Windows application that requires a full-blown Windows laptop to run on.
   * *Pros*: The real command system used by rescue authorities runs on Windows. If that ends up in the voluntary rescue units as well, it would make to utilize the same hardware.
   * *Cons*: Code reuse may become difficult. Vehicle laptops are typically pretty expensive. There is a risk for vendor lock-in.

## The Phone App

The phone app will be running on the fire department's members' individual phones. For the time being, **Android** is the only supported platform. Windows phones are rare and I don't have access to a development environment for iPhones.

## The Server App

The server app is running either on a computer in the local fire station network or on a remote server (e.g. in the cloud). It is the core of the entire system and all the other apps communicate with it through some kind of messaging framework. There are two alternative platforms to build this on:

1. **.NET**: The server app would be a headless .NET application that runs either in Microsoft's .NET environment or on Mono.
   * *Pros*: If a voluntary fire department buys a PC, it will run Windows 10, which has the .NET framework built in by default.
   * *Cons*: Not possible to re-use code with the Android apps. Without Mono-support it would be more difficult to find cloud environment to run the app in.
2. **Java**: The server app would be a headless Java application that runs on Java's virtual machine.
   * *Pros*: The android apps will also be written in Java and so some code can be reused. Also I know Java very well.
   * *Cons*: The Java VM would have to be downloaded, installed and maintained on the PC.

## The Administrator App

The administrator app will be used to administer the system, e.g. add new members, configure assignment codes, add new devices, etc. There are two alternative platforms to build this on:

1. **Web**: The administrator app would be built into the server app and accessible through a web browser.
   * *Pros*: The users only need a web browser to administer the system and there is one application less to maintain and install.
   * *Cons*: Depending on the platform chosen to build the server app on, creating a web app may be easier or more difficult.
2. **Native**: The administrator app would be built as a native/.NET Windows app.
   * *Pros*: This would require making an API for managing the application, which in turn could be used by other apps as well if needed.
   * *Cons*: This would require yet another application to install and maintain. Also the UI may end up being more limited than a modern web UI.

## Hardware Alternatives

Voluntary fire department are not rich and so the price of the hardware needed to run this system is an important factor in selecting the correct platform to build on,even though the software itself is open source.

* **RaspberryPi**: Costs around 100 € if you include all the necessary accessories. Requires some skills to assemble and install the operating system. The default Linux operating system is pretty stable, but the web browsers are not. If you want to run Android, you can install *emteria.OS* but it is not free and it crashed all the time when I tried it for the first time. Going for RaspberryPi only makes sense for applications that run natively on its default operating system.
* **Acer Iconia Android Tablet**: Costs between 180 and 300 € depending on whether you get an older or a newer version. Can be connected to an external monitor through HDMI and so could be used as a runboard device. Only WiFi, no mobile internet connection.
* **Android Tablets with Mobile Internet**: There are so many models to choose from, but the cheapest ones probably cost between 150 and 200 €.
* **Panasonic Toughbook**: Costs between 2500 and 4000 € depending on the model and where you buy it. This is the model that seems to be used by the authorities in their command vehicles, but I'm pretty sure it is possible to get a decent vehicle laptop much cheaper as long as it stays inside the car at all times.

## Conclusions

To keep the development as simple as possible, the number of software platforms should be as small as possible. Since the phone app, which is an important part of the system, will be written in Android, it makes sense to build the vehicle app and runboard on Android as well. Finding affordable Android-devices that can be connected to external monitors should not be difficult.

For the server, it seems .NET is the best alternative since it will most likely be running on Windows PCs in fire stations. However, if this turns out to be difficult during actual development I reserve the right to switch to Java.

For the administrator app, a web UI built into the server app makes the most sense at this time.

So to summarize:

* **Android**: Runboard App, Vehicle App and Phone App
* **.NET**: Server App
* **Web**: Admin app (built into the Server App)
