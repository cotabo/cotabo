<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html>
    <head>        
        <meta http-equiv="content-type" content="text/html;charset=UTF-8">
        <title><g:layoutTitle default="Cotabo"/></title>              
        <link rel="stylesheet" href="${resource(dir:'css',file:'bootstrap.min.css')}">   
        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />        
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:javascript library="jquery" plugin="jquery"/>                                         
        <g:layoutHead />        
    </head>
    <body style="padding-top: 40px;">        
	    <div class="topbar">
	      	<div class="topbar-inner">
	        	<div class="container">
	          		<a href="${resource(dir:'')}"><img class="brand" src="${resource(dir:'images',file:'cotabo_head_small.png')}"></img></a>
	          		<ul class="nav">	          			
	            		<li><g:link controller="board">Boards</g:link></li>
	            		<li><g:link controller="board" action="create">New Board</g:link></li>	            		     	
	          		</ul>   
	          		<g:pageProperty name="page.topbar.hook"/>  
      			    <ul class="nav secondary-nav">
		            		<sec:ifNotLoggedIn>
		            			<li><g:link controller="login">Login</g:link></li>
		            		</sec:ifNotLoggedIn>
		            		<sec:ifLoggedIn>
		            			<li><g:link controller="user" action="edit">My Profile (<sec:loggedInUserInfo field="username"/>)</g:link></li>
		            			<li><g:link controller="logout">Logout</g:link></li>
		            		</sec:ifLoggedIn>
	            		</ul>            		
	        	</div>
	      	</div>
	    </div>
	    
	    <!-- 
        <div class="logo">  
            <a href="${request.contextPath}">               
            <img src="${resource(dir:'images',file:'logo.png')}"/>
            </a>
        </div>
        -->    
	    
	    <div class="container">
	    	<!-- every sub page can specivy a content tag with the name 'header' to hook into the header bar-->
           	<!-- see http://grails.org/doc/latest/guide/6.%20The%20Web%20Layer.html#6.2.5%20Sitemesh%20Content%20Blocks -->
			<g:pageProperty name="page.herounit.hook"/>			
			<!-- every sub page can specivy a content tag with the name 'toolbar' -->
			<g:pageProperty name="page.toolbar"/>	 
	    </div>
	    <div class="container<g:pageProperty name="page.container.hook"/>">	    		    			    	
	    		    	   	
	    	<g:layoutBody />
	    				    	
	    </div>
    </body>
</html>