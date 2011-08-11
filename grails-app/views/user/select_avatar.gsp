<fieldset>
	<legend>Avatar Upload</legend>
	<g:form action="upload_avatar" method="post" enctype="multipart/form-data">
		<label for="avatar">Avatar</label>
		<input type="file" name="avatar" id="avatar" />
		<div style="font-size:0.8em; margin: 1.0em;">
			You have to be logged in to be able to upload the avatar image.
		</div>
		<input type="submit" class="buttons" value="Upload" />
	</g:form>
</fieldset>
