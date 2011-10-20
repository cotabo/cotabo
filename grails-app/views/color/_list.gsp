<g:each in="${colors}" var="color">
	<%--<div style="width:${100/colors.size()}%"> --%>
		<div class="column column-tag" style="background-color:${color}; min-height:0px;margin:3px;padding:3px;font-size:14px; ">
			<a href="${createLink(controller:'taskColor', action:'delete', id:color.id)}"><span class="ui-icon ui-icon-close tag"></span></a>
			${color.name}
		</div>
	<%-- </div> --%>
</g:each>
<jq:jquery>
	$('.column-tag').click(function(){
		this.style.border = this.style.border == ''? '1px solid red' : '';
		var color = jQuery.trim($(this).text());
		var colors = $('div.head_color');
		var tasks = $('li.ui-widget').filter(function(index){
			return $('div.head_color', this).length > 0 && $('.'+color, this).length > 0 ;
		})
		jQuery.each(tasks, function(index, value){
			value.style.display = value.style.display == 'none' ? '' : 'none';
		});
	})
</jq:jquery>