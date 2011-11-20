<sec:ifAllGranted roles="ROLE_ADMIN">
<content tag="toolbar">    
         <a id="export" class="btn pull-left" href="${createLink(controller:'board', action:'export')}" title="Exports all boards from Cotabo">export boards</a>
         <a id="import" class="btn pull-left" href="${createLink(controller:'board', action:'importBoards')}" title="Imports a set of board into Cotabo">import boards</a>    
</content>
</sec:ifAllGranted>