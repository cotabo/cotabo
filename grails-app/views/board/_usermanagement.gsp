<div id="usermanagement">
    <div id="users_selected" class="um_div">
        <g:each in="${['users', 'admins']}" var="role">            	       
	        <div>
	           <h4>${role}</h4>
	           <ul id="${role}" class="user_droppable user_list ui-state-default">
	           </ul>       
	        </div>
        </g:each>
    </div>
    
    <div id="users_selectable" class="um_div">        
        <ul id="list_selectable" class="user_list ui-state-highlight">
            <g:each in="${users}" var="user" status="i">            
            <li class="user_item ui-state-default ">
                <div class="div_user">
                    ${user}
                    <g:hiddenField name="user[${i}]" value="${user.id}"/>
                </div>
            </li>
            </g:each>
            
        </ul>
    </div>
</div>