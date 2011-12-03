<div id="new_task_helper"></div>	
<span id="toolbar">	   
	<button data-controls-modal="task_create_dialog" data-backdrop="static" data-keyboard="true" id="b_new_task" class="btn primary"><u>n</u>ew task</button>	
	<button id="b_collapse" class="btn"><u>c</u>ollapse all tasks</button>
	<button id="b_expand" class="btn"><u>e</u>xpand all tasks</button>	
	<button data-controls-modal="color_create_dialog" data-backdrop="static" data-keyboard="true" id="b_new_tag" class="btn">new <u>t</u>ag</button>	
</span>	

<tb:modal id="task_create_dialog" header="New Task">
	<g:render template="/task/create"/>
</tb:modal>
<tb:modal id="color_create_dialog" header="New Tag">
	<g:render template="/color/create"/>
</tb:modal>