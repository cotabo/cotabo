package org.cotabo

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_USER'])
class UserController {
	
	def springSecurityService
	
	def avatar = {
		if(params.username) {
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
		else {
			render ''
		}
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
				[exclude:['enabled', 'accountExpired', 'accountLocked', 'passwordExpired', 'password', 'avatar']]
			)

		  	// Get the avatar file from the multi-part request
			def f = request.getFile('avatar')
			if(f.size > 0) {				
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
