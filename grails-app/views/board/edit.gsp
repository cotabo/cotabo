

<%@ page import="org.cotabo.Board" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'board.label', default: 'Board')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${boardInstance}">
            <div class="errors">
                <g:renderErrors bean="${boardInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${boardInstance?.id}" />
                <g:hiddenField name="version" value="${boardInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="board.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: boardInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" maxlength="25" value="${boardInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="description"><g:message code="board.description.label" default="Description" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: boardInstance, field: 'description', 'errors')}">
                                    <g:textArea name="description" cols="40" rows="5" value="${boardInstance?.description}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="columns"><g:message code="board.columns.label" default="Columns" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: boardInstance, field: 'columns', 'errors')}">
                                    
<ul>
<g:each in="${boardInstance?.columns?}" var="c">
    <li><g:link controller="column" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="column" action="create" params="['board.id': boardInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'column.label', default: 'Column')])}</g:link>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="users"><g:message code="board.users.label" default="Users" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: boardInstance, field: 'users', 'errors')}">
                                    <g:select name="users" from="${org.cotabo.User.list()}" multiple="yes" optionKey="id" size="5" value="${boardInstance?.users*.id}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
