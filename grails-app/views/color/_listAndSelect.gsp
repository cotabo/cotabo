<script type="text/javascript" src="${resource(dir:'js/jquery-plugins', file:'jquery.farbtastic.js')}"></script>
<link rel="stylesheet" href="${resource(dir:'css',file:'farbtastic.css') }"/>
<jq:jquery>

	//Add Color button & functionality
	var addColorTpl = function() {
      	return [
      		"li", {}, [
      			"div", {class: "color_close" }, [
      				"span", { class: "color_close_button ui-icon ui-icon ui-icon-closethick" },[]
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
    	var idx =$("#color_list > li").size() + 1
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
	
	$('.color_close_button').live('click', function(event) {
		$(this).parents('li').remove()
	});
			
</jq:jquery>
<ul id="color_list">
	<!-- TODO: change this to use colors from the boardInstance -->
	<g:each in="${colorInstances}" var="color" status="i">
	<li>
		<div class="color_close">
			<span class="color_close_button ui-icon ui-icon ui-icon-closethick"/>
		</div>
		<label>Color code:</label>
		<input type="text" 
			id="color_picker_${i }" 
			class="color_picker_input" 
			name="color[]" 
			value="${color.colorCode}"/>
		<div id="colorpicker_${i }" class="colorpicker"></div>
	</li>
	</g:each>
</ul>
<div style="clear:both;"/>
<button id="add_color" type="button">add color</button>

