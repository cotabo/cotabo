<html>
    <head>        
        <meta http-equiv="content-type" content="text/html;charset=UTF-8">
        <title><g:layoutTitle default="Cotabo"/></title>        
        <link rel="stylesheet" href="${resource(dir:'css',file:'jquery-ui-1.8.12.custom.css')}" />        
        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
        <link rel="stylesheet" href="${resource(dir:'css',file:'jquery.superfish.css')}" />        
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:javascript library="jquery" plugin="jquery"/>
        <script type="text/javascript" src="${resource(dir:'js/jquery-ui', file:'jquery-ui-1.8.6.custom.min.js')}"></script>
        <script type="text/javascript" src="${resource(dir:'js/jquery-plugins', file:'jquery.superfish.js')}"></script>        
        <script type="text/javascript" src="${resource(dir:'js', file:'base.js')}"></script>                
        <g:layoutHead />        
    </head>
    <body>    
		<div class="head-back">
            <div class="head">
                <ul id="menu" class="sf-menu">

                    <li>
                        <g:link controller="board">
                            <span class="ui-icon ui-state-default ui-icon-image"></span>
                            my boards
                        </g:link>
                        <ul>
                            <li>                                
                                <g:link controller="board" action="create">
                                    <span class="ui-icon ui-state-default ui-icon-document"></span>
                                    create
                                </g:link>
                            </li>
                            <li>                                
                                <g:link controller="board" action="list">
                                    <span class="ui-icon ui-state-default ui-icon-note"></span>
                                    list
                                </g:link>
                            </li>
                        </ul>
                    </li>
                    <sec:ifNotLoggedIn>
                    <li>                        
                        <g:link controller="login" >
                            <span class="ui-icon ui-state-default ui-icon-arrowreturnthick-1-e"></span>
                            login
                        </g:link>
                    </li>
                    </sec:ifNotLoggedIn>
                    <sec:ifLoggedIn>
                    <li>       
                                                          
                        <g:link controller="user" action="edit" >
                            <span class="ui-icon ui-state-default ui-icon-person"></span>
                            my profile
                        </g:link>
                    </li>
                    <li>                        
                        <g:link controller="logout">
                            <span class="ui-icon ui-state-default ui-icon-arrowreturnthick-1-w"></span>
                            logout
                        </g:link>
                    </li>
                    </sec:ifLoggedIn>
                </ul>  
                <!--                    
                <div class="menu" class="ui-widged">
                    <sec:ifNotLoggedIn>
                    <span class="ui-state-default ui-corner-all">
                        <g:link controller="login" >Login</g:link>
                    </span>
                    </sec:ifNotLoggedIn>
                    <span class="ui-state-default">
                        <g:link controller="board">Cotabos</g:link>
                    </span>
                </div>
                -->     
                <div class="logo">  
                    <a href="${request.contextPath}">               
                    <img src="${resource(dir:'images',file:'logo.png')}"/>
                    </a>
                </div>
                 
            </div>    
		</div>		
        <div class="page-wrap">

			<div class="sub-head">
					<!-- every sub page can specivy a content tag with the name 'toolbar' -->
					<!-- see http://grails.org/doc/latest/guide/6.%20The%20Web%20Layer.html#6.2.5%20Sitemesh%20Content%20Blocks -->
					<g:pageProperty name="page.toolbar"/>        		
			</div>   			

        	<g:layoutBody />
        	

        </div>
    </body>
</html>