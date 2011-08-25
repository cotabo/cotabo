<%@ page import="org.cotabo.User" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />     
        <link rel="stylesheet" href="${resource(dir:'css',file:'user.css')}" />          
        <title>Cotabo - edit ${userInstance.username}'s profile</title>
    </head>
    <body>
        <div>
            <span>
                <div class="avatar-big">
                    <g:if test="${userInstance.avatar}" >
                    <img src="${createLink(controller:'user', action:'avatar', id:userInstance.username)}" width="50" height="50"/>
                    </g:if>
                </div>
                <h1>${userInstance.username}'s profile</h1>
            </span>
            <g:if test="${flash.message}">
            <g:render template="/info" model="[message: flash.message]"/>              
            </g:if>
            <g:hasErrors bean="${userInstance}">            
            <div class="ui-state-error">                
                <g:renderErrors bean="${userInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" id="${userInstance.id}" enctype="multipart/form-data">
	            <table>
	                <tbody>     
	                    <tr>
	                        <td>
	                            <label for="firstname"><g:message code="user.firstname.label" default="Firstname" /></label>
	                        </td>
	                        <td class="${hasErrors(bean: userInstance, field: 'firstname', 'errors')}">
	                            <g:textField name="firstname" value="${userInstance?.firstname}" />
	                        </td>
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
	                        <td>
	                            <label for="avatar">Avatar</label>
	                        </td>
	                        <td class="${hasErrors(bean: userInstance, field: 'avatar', 'errors') }">
	                            <input type="file" name="avatar" />
	                        </td>
	                    </tr>                    
	                </tbody>
	            </table>            
	            <div class="buttons">
	                    <span class="button"><g:submitButton name="save" value="update profile" /></span>
	                </div>                
	            </g:form>
        </div>
    </body>
</html>
