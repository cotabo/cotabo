<g:render template="/info" model="[messagecode: 'board.create.description']"/>
<table>
    <tbody>                        
        <tr>
            <td>
                <label for="name">Name</label>
            </td>
            <td>
                <g:textField name="name" maxlength="25" value="${boardInstance?.name}" />
            </td>
        </tr>
    
        <tr>
            <td>
                <label for="description">Description</label>
            </td>
            <td>
                <g:textArea name="description" cols="40" rows="5" value="${boardInstance?.description}" />
            </td>
        </tr>
    </tbody>
</table>