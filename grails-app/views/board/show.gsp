<%@ page import="app.taskboard.Board" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <link rel="stylesheet" href="${resource(dir:'css',file:'board.css')}" />
        <title>TaskBoard - ${boardInstance?.name}</title>
        <jq:jquery>            	        	             
        	var setElementCountOnColumn = function() {
				$(".column").each(function(index){
					var size = $(this).children("ul").children("li").size()
					if (size == null) {
						size = 0 
					}
					//Set the current number of elements to the element count for each column
					var pValueDom = $(this).children("span").children("span").children("p.value");
					var pLimitDom = $(this).children("span").children("span").children("p.limit");
					pValueDom.text(size);
					if (pValueDom.text() == pLimitDom.text() || parseInt(pValueDom.text()) > parseInt(pLimitDom.text())) {
						$(pValueDom).addClass('red-font');
					}
					else {
						if ($(pValueDom.hasClass('red-font'))) {
							pValueDom.removeClass('red-font');
						}
					}
				});
			}
			
			var handleClickHeader = function(event) {
				$(this).parent('div').next('div').toggle();
				if ($(this).hasClass('ui-icon-arrowthickstop-1-n')) {
					$(this).removeClass('ui-icon-arrowthickstop-1-n').addClass('ui-icon-arrowthickstop-1-s');
				}
				else if ($(this).hasClass('ui-icon-arrowthickstop-1-s')){
					$(this).removeClass('ui-icon-arrowthickstop-1-s').addClass('ui-icon-arrowthickstop-1-n');
					
				}		
				return false;
			}	
				
			setElementCountOnColumn();			
			$(".column > ul").each(function(index) {			
				$(this).sortable({			
					//Connect the current sortable only with the next column
					//connectWith:'.column ul:gt('+index+'):first',
					connectWith:'.column ul',
					appendTo: 'body',
					containment:"#board",
					cursor:"move",
					distance:30,
					opacity:0.7,
					revert:true,
					placeholder:'ui-state-highlight',
					receive: setElementCountOnColumn
				});
			});	
			$('.task-header .ui-icon').click(handleClickHeader);
			$('.column:first > ul > li > .task-header > .ui-icon').click()
        </jq:jquery>
    </head>
    <body>
    	<tb:board board="${boardInstance}">
    		<g:each in="${boardInstance.columns}" var="column">
    		<tb:column column="${column}">
    			<g:each in="${column.tasks}" var="task">
    			<tb:task task="${task}"/>
    			</g:each>
    		</tb:column>
    		</g:each>
    	</tb:board>    	
    </body>
</html>
