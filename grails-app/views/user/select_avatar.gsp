<fieldset>
	<legend>Avatar Upload</legend>
	<g:form action="upload_avatar" method="post" enctype="multipart/form-data">
		<label for="avatar">Avatar</label>
		<input type="file" name="avatar" id="avatar" />
		<input type="text" name="username" id="username" />
		<div style="font-size:0.8em; margin: 1.0em;">
			For best results, your avatar should have a width-to-height ratio of 4:5.
		</div>
		<input type="submit" class="buttons" value="Upload" />
	</g:form>
</fieldset>
