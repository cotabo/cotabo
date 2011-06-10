<%@ page import="app.taskboard.Board" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />        
        <link rel="stylesheet" href="${resource(dir:'css',file:'board.css')}" />				
		<script language="javascript" type="text/javascript" src="${resource(dir:'js/jquery-plugins',file:'jquery.flot.min.js')}"></script>
		<script language="javascript" type="text/javascript" src="${resource(dir:'js/jquery-plugins',file:'jquery.flot.stack.js')}"></script>
		<script language="javascript" type="text/javascript" src="${resource(dir:'js/jquery-plugins',file:'jquery.csv.js')}"></script>
        <title>TaskBoard - Comulative Flow Diagram for ${boardInstance?.name}</title>  
	</head>
	<body>
	<h2>Comulative Flow Diagram - ${boardInstance.name}</h2>
	
	<div id="cfd_placeholder" style="width:1000px;height:400px"></div> 
	<div id="legend" style="width:200px;"/>
	
	<jq:jquery>			
				
		var dataArray = new Array()
		<g:each in="${boardInstance.columns}" var="column" status="i">		
		dataArray[${column.id}] = getCsvCDFDataForColumn(${column.id});	
		</g:each>
		
		$.plot($('#cfd_placeholder'), [						
			<g:each in="${boardInstance.columns.sort{a,b->b.id<=>a.id}}" var="column" status="i">
			{
				label: '${column.name}',
				data: dataArray[${column.id}],
				lines: { show: true, fill: true },
				stack:1			
			}
			<g:if test="${i != (boardInstance.columns.size() -1)}">
			,
			</g:if>
			</g:each>
		],
		{
			xaxis: {
				mode: 'time'
				//timeformat: "%d. %b %y"
			},
			legend: {
				position: "nw",
				container: '#legend'
			}			
		});
		
				
		</jq:jquery>     
	</body>
</html>
