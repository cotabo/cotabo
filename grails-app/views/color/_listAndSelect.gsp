<script type="text/javascript" src="${resource(dir:'js/jquery-plugins', file:'jquery.farbtastic.js')}"></script>
<link rel="stylesheet" href="${resource(dir:'css',file:'farbtastic.css') }"/>
<jq:jquery>
	var addColorTpl= function() {
      	return [
      		"li", {}, [
      			
      		] 
      	]
     }
	$(".colorpicker").each (function(index, element) {
		var index = $(element).attr('id').split('_')[1];
		$('#colorpicker_'+index).farbtastic('#color_picker_'+index);
	});
			
</jq:jquery>
<ul id="color_list">
	<g:each in="${colorInstances}" var="color" status="i">
	<li>
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