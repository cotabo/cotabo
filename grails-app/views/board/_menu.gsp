<content tag="toolbar">
    <div id="new_task_helper"></div>	
	<span id="toolbar">	   
		<button id="b_new_task">new task</button>
		<button id="b_chat">chat message</button>
		<button id="b_collapse">collapse all tasks</button>
		<button id="b_expand">expand all tasks</button>		
	</span>	
	<div id="chat_dialog" title="Chat message">
	   <form id="chat_form">
		   <table>
		       <tbody>
		           <tr>
		               <td><label for="message">Message:&nbsp;</label></td>
		               <td><input type="text" name="message" size="60" maxlength="254"/></td>
		           </tr>
		       </tbody>
		   </table>
	   </form>
	</div>
</content>
<g:render template="/task/create"/>