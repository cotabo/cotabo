package org.cotabo

import org.springframework.web.multipart.MultipartHttpServletRequest;

class UserController {
	
	def springSecurityService
	
	def avatar = {
		def avatarUser = User.findByUsername(params.username)
		log.info("avatar_image for user: ${avatarUser}")
		if (!avatarUser || !avatarUser.avatar || !avatarUser.avatarType) {
			response.sendError(404)
			return;
		}
		response.setContentType(avatarUser.avatarType)
		response.setContentLength(avatarUser.avatar.size())
		OutputStream out = response.getOutputStream();
		out.write(avatarUser.avatar);
		out.close();
	}

	def addavatar = {
		render (view:'select_avatar', model:[:])
		return
	}

	def upload_avatar = {
		def user = User.findByUsername(springSecurityService.principal.username)
		if(!user){
			redirect(action:'addavatar')
		}		
	    // Get the avatar file from the multi-part request
		def f = request.getFile('avatar')

		// List of OK mime-types
		def okcontents = ['image/png', 'image/jpeg', 'image/gif']
		if (! okcontents.contains(f.getContentType())) {
			flash.message = "Avatar must be one of: ${okcontents}"
			render(view:'select_avatar', model:[user:user])
			return;
		}

		// Save the image and mime type
		user.avatar = f.getBytes()
		user.avatarType = f.getContentType()
		log.info("File uploaded: " + user.avatarType)
		// Validation works, will check if the image is too big
		if (!user.save()) {
			render(view:'select_avatar', model:[user:user])
			return;
		}
		flash.message = "Avatar (${user.avatarType}, ${user.avatar.size()} bytes) uploaded."
	  redirect(action:'addavatar')
	}

	def save = {
		def userInstance = User.get(params.id)		
		if (userInstance) {			
			if(params.password != params.confimPassword) {
				flash.message = "You seem to have a typo in you password."
				render(view:'edit', model:[userInstance:userInstance])
				return
			}		
			bindData(
				userInstance, params, 
				[exclude:['enabled', 'accountExpired', 'accountLocked', 'passwordExpired', 'password']]
			)

		  	// Get the avatar file from the multi-part request
			def f = request.getFile('avatar')
			if(f) {
				// List of OK mime-types
				def okcontents = ['image/png', 'image/jpeg', 'image/gif']
				if (! okcontents.contains(f.getContentType())) {
					flash.message = "avatar must be one of: ${okcontents}"
					render(view:'edit', model:[userInstance:userInstance])
					return;
				}
				// Save the image and mime type
				userInstance.avatar = f.getBytes()
				userInstance.avatarType = f.getContentType()
			}
			
			if(userInstance.validate() && userInstance.save()) {
				flash.message = "Profile of ${userInstance.username} updated successfully."
				redirect(controller:'board', action:'list')				
			}
			else {
				render(view:'edit', model:[userInstance:userInstance])				
			}
		}
		else {
			render(status: 404, text: "No user found for id ${params.id}")
			return
		}		
	}
	
	def edit = {
		def userInstance = User.findByUsername(springSecurityService.principal.username)
		if (userInstance) {
			render(view:'edit', model:[userInstance:userInstance])
		}
		else {
			render(status: 404, text: "No user found for id ${params.id}")
			return
		}
	}
}
