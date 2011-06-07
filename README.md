cotabo
======
This project is currently in a very very early prototype phase.
It aims to provide an open-source collaborative webbased Kanban-like taskboard 
to make it easier for distributed teams to adopt "Kanabanish" methodologies.

This comes out of a personal interest that there is no really good and free
Kanban application for distributed teams that offers real-time collaboration 
and reporting capabilities.

I'm normally not a developer but as I know a bit of Java, Groovy & Grails - I decided to 
implement something on my own - also in order to learn more about anything related to 
programming at all and everything around Groovy.

The name cotabo is the short-form for "Collaborative Taskboard".
Feel free to come up with new naming suggestions as this was just the first idea
that came to my mind.

Currently I don't have a project page for this but as soon as the time is right
I am going to make a google project page in order to provide some documentation
and also use it for task tracking (of course only as long as cotabo is not 
in the state of being used effectively).


High-level overview of planned features for the first prototype version
-----------------------------------------------------------------

* Stand-alone user-management embedded in the application (no LDAP or other interfaces)
* Basic board creation including a dynamic number of columns, limits, user authorization and other basic stuff
* Single page board view capable of creating , deleting & moving tasks around and asynchronously updating the server state
* Reporting capabilities covering the "standard" Kanban reports (Comulative Flow Diagram, Throughput etc.)
* Using [Atmosphere](http://atmosphere.java.net/) (AJAX Push / Websockets) to enable real-time collaboration by distributing all events on a board to all registered clients
* Full-text search on Tasks (board specific)

Of course there is a lot of basic stuff that needs to be done in order to enable all this above
but I don't want to go into the details here.

As this is currently only a prototype I am just tracking all the things locally 
using the [Simple Kanban](http://www.simple-kanban.com/) project.


Further features that I could imagine
-------------------------------------
* Support for Swimlanes on boards
* Flow improvement / Bottleneck suggestions based on historical data of a board
* Avatars / Images for users
* Easy pluggable architecture to pull new tasks from external systems
* Easy pluggable architecture to update the task stati in external systems
* ... everything else that comes to my or your mind.





