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
            <h1>Cotabo registration</h1>
            <g:if test="${flash.message}">
            <div class="ui-state-highlight">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${userInstance}">            
            <div class="ui-state-error">                
                <g:renderErrors bean="${userInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="register" >
                <div>
                    <table>
                        <tbody>     
                            <tr>
                                <td>
                                    <label for="username"><g:message code="user.username.label" default="Username" /></label>
                                </td>
                                <td class="${hasErrors(bean: userInstance, field: 'username', 'errors')}">
                                    <g:textField name="username" value="${userInstance?.username}" />
                                </td>
                            </tr>                   
                            <tr>
                                <td>
                                    <label for="firstname"><g:message code="user.firstname.label" default="Firstname" /></label>
                                </td>
                                <td class="${hasErrors(bean: userInstance, field: 'firstname', 'errors')}">
                                    <g:textField name="firstname" value="${userInstance?.firstname}" />
                                </td>
                            </tr>
                        
                            <tr>
                                <td>
                                    <label for="lastname"><g:message code="user.lastname.label" default="Lastname" /></label>
                                </td>
                                <td class="${hasErrors(bean: userInstance, field: 'lastname', 'errors')}">
                                    <g:textField name="lastname" value="${userInstance?.lastname}" />
                                </td>
                            </tr>
                        
                            <tr>
                                <td>
                                    <label for="email"><g:message code="user.email.label" default="Email" /></label>
                                </td>
                                <td  class="${hasErrors(bean: userInstance, field: 'email', 'errors')}">
                                    <g:textField name="email" value="${userInstance?.email}" />
                                </td>
                            </tr>                        
                        
                            <tr>
                                <td>
                                    <label for="password"><g:message code="user.password.label" default="Password" /></label>
                                </td>
                                <td class="${hasErrors(bean: userInstance, field: 'password', 'errors')}">
                                    <g:passwordField name="password" value="${userInstance?.password}" />
                                </td>
                            </tr>   
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="register" value="${message(code: 'default.button.register.label', default: 'Register')}" /></span>
                </div>                
            </g:form>
        </div>
    </body>
</html>
