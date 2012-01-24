<g:each in="${colors}" var="color">
	<div class="column-tag pull-left" style="background-color:${color};">
		<a href="${createLink(controller:'taskColor', action:'delete', id:color.id)}"><span class="pull-right ui-icon ui-icon-close tag"></span></a>
		${color.name}
	</div>
</g:each>
<jq:jquery>
	$('.column-tag').click(function(){
		this.style.border = this.style.border == ''? '1px solid red' : '';
		var color = jQuery.trim($(this).text());
		var colors = $('div.head_color');
		var tasks = $('li.task').filter(function(index, task){
			var result = true;
			jQuery.each(color.split(' '), function(index, value){
				result = result && $('.'+value.trim(), task).length > 0 ;
			});
			return result && $('div.head_color', this).length > 0 ;
		})
		jQuery.each(tasks, function(index, value){
			value.style.display = value.style.display == 'none' ? '' : 'none';
		});
	})
</jq:jquery>