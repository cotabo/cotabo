<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />        
        <meta name="layout" content="main" />
    </head>
    <body>
        <g:if test="${flash.message}">
           <g:render template="/info" model="[message:flash.message]"/>        
        </g:if>
        <h5>
            <g:render template="/info" model="[message:'Please note that only XML in the format how it was exported is supported during import.']"/>
        </h5>
        <g:form controller="board" action="importBoards" name="boardImportForm">
        <table>
            <tbody>
                <tr>
                    <td><label for="xml">Cotabo XML</label></td>
                    <td><g:textArea name="xml" cols="80" rows="15" value="${xml}" /></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><input type="submit" value="Import"/></td>
                </tr>
            </tbody>
        </table>
        </g:form>
    </body>
</html>