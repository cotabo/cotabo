<jq:jquery>
	var addPriorityTpl = function() {
		return [
			"li", {}, [
				"div", {class:'ui-widget'}, [
					"div", {class:'portlet-header ui-widget-header ui-state-default'}, [
						"span", {class:'close_button ui-icon ui-icon ui-icon-closethick'}, []
					],					
					"div", {class:'ui-widget-content'}, [
						"table", {}, [
							"tbody", {}, [
								"tr", {}, [
									"td", {}, [
										"label", {}, 'Name'
									],
									"td", {}, [
										"input", {
											type: 'text', 
											name: 'priorities['+this.index+'].name',
											class: 'priority_input'
										}
									]
								]
							]
						]
					]
				]
			]
		]
	}

	$("#add_priority").button({
		icons: { primary: "ui-icon-plusthick" }
    }).click(function(event){
    	var idx =$("#priority_list > li").size()
    	if (idx < 10) {
    		var tplData = {index:idx}
	    	//Append a list item and apply the colorpicker to the last element
	    	$("#priority_list").tplAppend(tplData, addPriorityTpl)
   		}
   		return false;    	
    });   
</jq:jquery>
<ul id="priority_list">
	<g:if test="${boardInstance.priorities && boardInstance.priorities}">
		<g:set var="priorities" value="${boardInstance.priorities*.name}"/>
	</g:if>
	<g:else>
		<g:set var="priorities" value="${['Critical', 'Major', 'Normal', 'Low']}"/>
	</g:else>
	<g:each in="${priorities}" var="priority" status="j">
	<li>
		<div class="ui-widget">
			<div class="portlet-header ui-widget-header ui-state-default">
				<span class="close_button ui-icon ui-icon ui-icon-closethick"/>
			</div>
			<div class="ui-widget-content">
				<table>
					<tbody>
						<tr>
							<td><label>Name</label></td>
							<td><input type="text" name="priorities[${j }].name" value="${priority }" class="priority_input"/></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</li>
	</g:each>
</ul>
<div style="clear:both;"></div>
<button id="add_priority" type="button">add priority</button>
