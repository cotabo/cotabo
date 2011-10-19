<sec:ifAllGranted roles="ROLE_ADMIN">
<jq:jquery>
    $('#export').button({
        icons: {
            secondary: 'ui-icon-arrowrefresh-1-s'
        }
    });
    $('#import').button({
       icons: {
           secondary:'ui-icon-arrowrefresh-1-n'
       }
    }).parent().buttonset();
</jq:jquery>
<content tag="toolbar">
    <span id="toolbar">
         <a id="export" href="${createLink(controller:'board', action:'export')}" title="Exports all boards from Cotabo">export boards</a>
         <a id="import" href="${createLink(controller:'board', action:'importBoards')}" title="Imports a set of board into Cotabo">import boards</a>
    </span>
</content>
</sec:ifAllGranted>