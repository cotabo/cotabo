<head>
<meta name='layout' content='main' />
<title>Login</title>
</head>

<body>
	<div class="row">			
		<g:if test='${flash.message}'>
		<div class='login_message'>${flash.message}</div>
		</g:if>
		<div class="span4">&nbsp;</div>
		<div class="span12">
		<h3>Please Login...</h3>
		<form action='${postUrl}' method='POST' id='loginForm' class='cssform' autocomplete='off'>
			<fieldset>
				<div class="clearfix">
					<label for='username'>Login ID</label>
					<div class="input">
						<input type='text' class='text_' name='j_username' id='username' />
					</div>
				</div>
				<div class="clearfix">
					<label for='password'>Password</label>
					<div class="input">
						<input type='password' class='text_' name='j_password' id='password' />
					</div>
				</div>
				<div class="clearfix">
					<label for='remember_me'>Remember me</label>
					<div class="input">
						<input type='checkbox' class='chk' name='${rememberMeParameter}' id='remember_me'
						<g:if test='${hasCookie}'>checked='checked'</g:if> />
					</div>
				</div>
				<div class="clearfix">
					<div class="input">
						<input type='submit' value='Login' class="btn primary" />
					</div>
				</div>
			</fieldset>
		</form>
       <hp>
           Don't have an account? Please register 
           <a href="<g:createLink controller="registration"/>" >here.</a>
       </hp>		
       </div>        		 
	</div>
<script type='text/javascript'>
<!--
(function(){
	document.forms['loginForm'].elements['j_username'].focus();
})();
// -->
</script>
</body>
