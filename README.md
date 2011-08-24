cotabo
======
Note: This project is currently in a very very early prototype phase.

Cotabo aims to provide an open-source collaborative webbased Kanban like taskboard 
to make it easier for distributed teams to adopt "Kanabanish" methodologies.

This comes out of a personal interest that there is no really good and free
Kanban application for distributed teams that offers real-time collaboration 
and reporting capabilities.

The name cotabo is the short-form for "Collaborative Taskboard".

For any kind of issue or feature request - please raise an Issue on Github
https://github.com/cotabo/cotabo/issues

Here is a high-level overview of featured that are already implemented
-----------------------------------------------------------------
* Stand-alone user-management embedded in the application (no LDAP or other interfaces)
* Basic board creation including a dynamic number of columns, limits, user authorization and other basic stuff
* Single page board view capable of creating, updating, archiving & moving tasks around and asynchronously updating the server state
* Real-time collaboration. Every event will be distributed to everone else that is currently viewing the page.
* Reporting: Commulative Flow Diagram (expect more in the future)

I always try to keep an updated version on CloudFoundry.
If you want to try it you can checkout http://cotabo.cloudfoundry.com
Just register yourself (link on the login page - you can enter a dummy mail address there) and try it out
by creating you board and view is with 2 different browser sessions and collaborate with yourself :)


Further features that we plan to implement in the futre
-------------------------------------
* Full-text search on Tasks (board specific)
* RSS feeds for all updates on a Board
* Support for Swimlanes on boards
* Flow improvement / Bottleneck suggestions based on historical data of a board
* Easy pluggable architecture to pull new tasks from external systems
* Easy pluggable architecture to update the task stati in external systems
* ... everything else that comes to my or your mind.





