<g:applyLayout name="main">
<html>
    <head>    	
        <title><g:layoutTitle default="Cotabo - report"/></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'board.css')}" />				
		<script language="javascript" type="text/javascript" src="${resource(dir:'js/jquery-plugins',file:'jquery.flot.min.js')}"></script>
		<script language="javascript" type="text/javascript" src="${resource(dir:'js/jquery-plugins',file:'jquery.flot.stack.js')}"></script>
		<script language="javascript" type="text/javascript" src="${resource(dir:'js/jquery-plugins',file:'jquery.csv.js')}"></script>        		
        <g:layoutHead />        
    </head>
    <body>  
    	<div id="report_container">    
        	<g:layoutBody />
		</div>
    </body>
</html>
</g:applyLayout>