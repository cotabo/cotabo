<%@page import="app.taskboard.Color"%>
<script type="text/javascript" src="${resource(dir:'js/jquery-plugins', file:'jquery.farbtastic.js')}"></script>
<link rel="stylesheet" href="${resource(dir:'css',file:'farbtastic.css') }"/>
<jq:jquery>
	//Add Color button & functionality
	var addColorTpl = function() {
      	return [
      		"li", {}, [
				"div", {class:'portlet-header ui-widget-header ui-state-default'}, [
					"span", {class:'close_button ui-icon ui-icon ui-icon-closethick'}, []
				],	
      			"label", {}, "Color Code:", 
      			"input", {
      				type: "text", 
      				id: "color_picker_"+this.index, 
      				class: "color_picker_input",
      				name: "color[]",
      				value: this.value
      			}, [], 
      			"div", {id: "colorpicker_"+this.index, class: "colorpicker"}, []      				 
      		] 
      	]
     }
     
	$("#add_color").button({
		icons: { primary: "ui-icon-plusthick" }
    }).click(function(event){
    	var idx =$("#color_list > li").size()
    	if (idx < 20) {
	    	var tplData = {
	    		index: idx,
	    		value: '#ffffff'
	    	};    	
	    	//Append a list item and apply the colorpicker to the last element
	    	$("#color_list").tplAppend(tplData, addColorTpl)
	    		.children().last().children("div.colorpicker").farbtastic('#color_picker_'+idx);
   		}
   		return false;    	
    });    
    
    //Color picker makeup
  	$(".colorpicker").each (function(index, element) {
		var index = $(element).attr('id').split('_')[1];
		$('#colorpicker_'+index).farbtastic('#color_picker_'+index);		
	});
			
</jq:jquery>
<ul id="color_list">
	<g:if test="${boardInstance?.colors && boardInstance.colors.size() > 0}">
		<g:each in="${boardInstance.colors}" var="color" status="i">
			<g:set var="colorCode" value="${color.colorCode}"/>
			<li>
				<g:render template="/color/colorpicker" model="[index:i , colorCode:colorCode]"/>
			</li>
		</g:each>
	</g:if>
	<g:else>
		<g:each in="0..3" var="color" status="i">
			<g:set var="colorCode" value="#ffffff"/>
			<li>
				<g:render template="/color/colorpicker" model="[index:i , colorCode:colorCode]"/>
			</li>
		</g:each>
	</g:else>
</ul>
<div style="clear:both;"></div>
<button id="add_color" type="button">add color</button>

