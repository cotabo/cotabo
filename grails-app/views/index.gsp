<html>
    <head>
        <title>Welcome to Cotabo</title>
        <meta name="layout" content="main" />
    </head>
    <body>
	    <content tag="herounit.hook">
	    	<div class="hero-unit">
	    		<h1>Welcome to Cotabo</h1>
		    	<i>version: <g:meta name="app.version"/></i>
	 			<p>
		            The aim of <b>Cotabo</b> is to provide you with a collaborative way of implementing Kanban like processes 
		            across distributed teams. 
		        <p>
		        
		        <p>		        	
		        	<sec:ifLoggedIn>
		        		<g:link controller="board" action="create" class="btn primary large">Create your Board</g:link>
		        		<g:link controller="board" action="list" class="btn large">List Boards</g:link>
		        	</sec:ifLoggedIn> 
		        	<sec:ifNotLoggedIn>
		        		<g:link controller="registration" class="btn primary large">Register</g:link>
		        		or
		        		<g:link controller="login" class="btn large">Login</g:link>
		        	</sec:ifNotLoggedIn>
		        </p>
	        </div>
	   	</content>
            
        <div>                       

            <p>
	            Congratulations, you are on the best way to improve the management of your Tasks.
	        </p>		
	        <p>        		        		       
	            Visualizing your work is one of the most basic things to improve your efficiency.
	            How can you manage something that is not visible?
	        </p>
	                                
            <p>Here is a list of features that are currently implemented:</p>
            <ul style="margin-left:50px;margin-top:10px;">
                <li>Create boards with a dynamic amount of columns & task limits on them</li>
                <li>Basic user-management (decide who should collaborate with you on the same board)</li>
                <li>Create / Update tasks - all clients that view your board will be updated on the fly (test it out by opening 2 browser instances)</li>
                <li>Marking tasks as blocked</li>
                <li>Basic chat functionality - send chat messages to everyone that looks at the boad (history won't be saved')</li>
                <li>Auto assignment of tasks (whoever pulls a task in your process flow will be the assignee)</li>
                <li>Upload an avatar for your user to easily see who is working on which task</li>
                <li>Experimental: Reporting - Comulative Flow Diagram, Average Throughput Diagram (will be improved & extended)</li>                    
            </ul>            
            <p>The project is hosted on <a href="https://github.com/cotabo/cotabo" target="_blank">GitHub</a>.<p>
            <p>Please submit any Issues & Feature requests as an <a href="https://github.com/cotabo/cotabo/issues">issue on GitHub</a></p>
        </div>
    </body>
</html>
