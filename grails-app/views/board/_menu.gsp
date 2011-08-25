<content tag="toolbar">
    <div id="new_task_helper"></div>	
	<span id="toolbar">	   
		<button id="b_new_task"><u>n</u>ew task</button>
		<button id="b_chat">chat <u>m</u>essage</button>
		<button id="b_collapse"><u>c</u>ollapse all tasks</button>
		<button id="b_expand"><u>e</u>xpand all tasks</button>		
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