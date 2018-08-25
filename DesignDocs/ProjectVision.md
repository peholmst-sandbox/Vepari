# Project Vision

First of all, I hope to come up with a better name than 'Vepari' (which is slang for voluntary firefighter and sometimes used in a degradative manner).

That said, this document presents my high-level vision for the Vepari system. It contains quite a lot of features and it will take quite some time to get them all implemented - if they get implemented in the first place.

In short, Vepari is a system for mainly tracking and managing incident responses for a single voluntary fire department. To some extent it can also be used to plan and manage other fire department events such as trainings.

## At The Fire Station

* Details about the incident and the responders are shown on big screen monitors:
  * Incident type and address
  * Alerted units
  * Location plotted on a map
  * Time elapsed since the first unit was dispatched
  * Names and qualities of the responders and an approximate ETA
* When not dispatched to an incident, the big screen monitors show information about upcoming events (such as weekly trainings) and their participants.
  
## In The Rescue Units

* The same information that is seen on the big screen station monitors is also available optimized for tablets.
* Short predefined messages can be sent to predefined member groups by pressing a button.
* Custom text messages can also be sent to predefined member groups.
* If some supported GIS/navigation software is installed on the device, navigation to the incident location can be initated from within Vepari. Vepari will not contain any navigation features of its own.

## In The Responder's Pocket

* Detection and alerting of dispatch messages coming directly from the Emergency Dispatch Center.
* Detection and alerting of limited dispatch messages coming from Vepari (for new members whose phone numbers have not been entered into the Emergency Dispatch Center's system yet).
* Status reporting (responding, out of service, etc.).
  * Can be done either from within the Vepari App or by sending an SMS or calling a specific number
* Information about how many members are responding to the incident.
* Information about upcoming events and the possibility to RSVP.
* Forrest fires: the possibility to track the real-time location of all the responders from the own fire department on a map (provided that they keep their mobile phones with them and opt-in).

## Non-functional Requirements and Restrictions

* The intended end users (the voluntary fire departments) will not have the necessary resources to set up a proper high-availabilty server environment which you would typically expect from a system like this.
* The entire Vepari system must run on easily available and affordable consumer hardware and/or use cloud services that are easy to purchase and configure even if you lack coding skills.
* The most important features of Vepari must work without internet/cloud access, using point-to-point communication within the GSM network (typically SMS and phone calls). Internet/cloud access will however be required for the more advanced features.
* Even when internet access is enabled, it should be possible to use the most important features using an ordinary GSM phone - not all members use Andoid phones.
* The dispatch messages coming from the Emergency Dispatch Center are subject to data protection laws and regulations that control what you can and can't do with them. Extra care must be taken to make sure they don't accidentally end up in the wrong hands.
* Vepari must not store any more data than absolutely necessary and data that is no longer needed must be automatically deleted (GDPR).
* The dispatch messages coming from the Emergency Dispatch Center are often truncated if they contain a lot of details or a lot of units. Vepari must work with these truncated messages as well.
* The Emergency Dispatch Centers are moving to a new software platform and we don't know how that will affect dispatching yet.
* For now, there are no plans to make an app for the iPhone.
* Google Maps are not accurate on the country side and may not contain the most recent roads and streets.
* All kinds of location tracking is disabled by default and must be explicitly enabled by the user (mandatory opt-in). The location tracking will also be disabled automatically after a certain time limit.
* The user interface must be available in both Swedish and Finnish.
