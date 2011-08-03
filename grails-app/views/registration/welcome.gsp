<%@ page import="org.cotabo.User" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
        <title>Cotabo - registration</title>
    </head>       
    <body>
        <div>
            <h1>Welcome to Cotabo - ${userInstance.firstname}</h1>
            <p>
                <span>There are two options for you to get started with Cotabo.</span>
                <ul>
                    <li>
                        <a href="${createLink(controller:"board", action:"create")}">
                        Create your own taskboard and invite other users to collaborate with you.
                        </a>
                    <li>
                        <a href="${createLink(controller:"board", action="list")}">
                        Explore existing taskboard and ask for an invitation.
                        </a>
                    </li>
                </ul>
            </p>
        </div>
    </body>
</html>