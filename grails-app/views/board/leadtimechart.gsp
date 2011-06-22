<%@ page import="app.taskboard.Board" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="report" />        
        <title>TaskBoard - Comulative Flow Diagram for ${boardInstance?.name}</title>  
	</head>
	<body>		
	<h2>Lead Time Diagram - ${boardInstance.name}</h2>
	<div id="average" class="ui-state-highlight ui-corner-all" style="margin:10px;width:200px;">	
		<p>
			<span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
			<strong>Average Leadtime (hours): <span id="ald"/></strong>
		</p>
	</div>
	<div id="cfd_placeholder" style="width:1000px;height:400px"></div> 
	<div id="legend" style="width:200px;"></div>	 

	<jq:jquery>		
		$.get(
			"${g.createLink(action:'getAverageCycleTime', id:boardInstance.id)}", 
			function(data, textStatus) {
				$("span#ald").append(data);
			}
		);					
		$.plot($('#cfd_placeholder'), [									
			{
				label: 'Lead time (hours)',
				data: getLeadTimeData(${boardInstance.id}),
				lines: { show: true, fill: true }
			}			
		],
		{
			xaxis: {
				mode: 'time'
				//timeformat: "%d. %b %y"
			},
			legend: {				
				container: '#legend'
			}			
		});					
		</jq:jquery>     
	</body>
</html>
