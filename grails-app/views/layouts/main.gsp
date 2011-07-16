<html>
    <head>        
        <meta http-equiv="content-type" content="text/html;charset=UTF-8">
        <title><g:layoutTitle default="Taskboard"/></title>        
        <link rel="stylesheet" href="${resource(dir:'css',file:'jquery-ui-1.8.12.custom.css')}" />
        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />        
        <g:javascript library="jquery" plugin="jquery"/>
        <script type="text/javascript" src="${resource(dir:'js/jquery-ui', file:'jquery-ui-1.8.6.custom.min.js')}"></script>
        <script type="text/javascript" src="${resource(dir:'js/jquery-plugins', file:'jquery.flydom-3.1.1.js')}"></script>
        <script type="text/javascript" src="${resource(dir:'js/jquery-plugins', file:'jquery.atmosphere.js')}"></script>
        <script type="text/javascript" src="${resource(dir:'js', file:'utils.js')}"></script>
        <g:layoutHead />
        
    </head>
    <body>    
		<div class="head-back"></div>		
        <div class="page-wrap">
        	<div class="head">			
        		<span id="user">     

        				<sec:ifLoggedIn>
        					Welcome <sec:loggedInUserInfo field="username"/>! 
        				</sec:ifLoggedIn>
        				<sec:ifNotLoggedIn>
        					Please <g:link controller="login" class="head_link">login...</g:link>
        				</sec:ifNotLoggedIn>        			
						
        		</span>				
        		<div class="logo">  
        			<a href="${request.contextPath}">      			
        			<img src="${resource(dir:'images',file:'logo.png')}"/>
        			</a>
        		</div>

        	</div>    
			<div class="sub-head">
					<!-- every sub page can specivy a content tag with the name 'toolbar' -->
					<!-- see http://grails.org/doc/latest/guide/6.%20The%20Web%20Layer.html#6.2.5%20Sitemesh%20Content%20Blocks -->
					<g:pageProperty name="page.toolbar"/>        		
			</div>   			

        	<g:layoutBody />
        </div>
    </body>
</html>