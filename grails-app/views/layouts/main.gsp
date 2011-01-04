<html>
    <head>
        <title><g:layoutTitle default="Taskboard"/></title>        
        <link rel="stylesheet" href="${resource(dir:'css',file:'jquery-ui-1.8.7.custom.css')}" />
        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />        
        <g:javascript library="jquery" plugin="jquery"/>
        <script type="text/javascript" src="${resource(dir:'js/jquery-ui', file:'jquery-ui-1.8.6.custom.min.js')}"></script>
        <script type="text/javascript" src="${resource(dir:'js/jquery-plugins', file:'jquery.flydom-3.1.1.js')}"></script>
        <g:layoutHead />
        
    </head>
    <body>    
		<div class="head-back"/>		
        <div class="page-wrap">
        	<div class="head">
        		<span id="user">
        			<p>
        				<sec:ifLoggedIn>
        					Welcome <sec:loggedInUserInfo field="username"/>! 
        				</sec:ifLoggedIn>
        				<sec:ifNotLoggedIn>
        					Please <g:link controller="login" class="head_link">login...</g:link>
        				</sec:ifNotLoggedIn>
        			</p>
        		</span>
        		<div class="logo">
        			<img src="${resource(dir:'images',file:'logo.png')}"/>
        		</div>
        	</div>     
        	<div class="sub-head">
        	</div>   
        	<g:layoutBody />
        </div>
    </body>
</html>