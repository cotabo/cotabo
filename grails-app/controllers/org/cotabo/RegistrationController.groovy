package org.cotabo

class RegistrationController {

	def springSecurityService
	def jcaptchaService
	
    def index = { 
		if(springSecurityService.isLoggedIn()) {
			//TODO: implement as resource bundle message
			flash.message = "You're already logged in - why do you want to register?"
			render(controller:"board", action:"list")
		}
		else {
			render(view:"registration", model:[roles:Role.list()])
		}
	}
	
	//TODO: use captcha verification on registration (http://www.grails.org/plugin/simple-captcha)?
	def register = { RegisterCommand command ->		
		//Return to list view of boards when user is already logged in an tries to register.
		if(springSecurityService.isLoggedIn()) {
			//TODO: implement as resource bundle message
			flash.message = "You're already logged in - why do you want to register?"
			render(controller:"board", action:"list")
			return		
		}
		if(command.hasErrors()) {						
			//using the command object as a User object in the view to render errors.
			render(view:"registration", model:[userInstance:command])
		}
		else {	
			if (jcaptchaService.validateResponse("imageCaptcha", session.id, params.captcha)) {	
				//TODO: send a mail with the registration details
				//TODO: implement the saving of a user
				def user = new User()
				//binding the command data to the user 
				bindData(user, command.properties, [exclude:['roles', 'password']])
				//setting the encoded password
				user.password = springSecurityService.encodePassword(command.password)
				
				if(user.save()) {
					//Setting ROLE_USER by default				
					UserRole.create(user, Role.findByAuthority('ROLE_USER'))
					render(view:"welcome", model:[userInstance:user])
				}
				else {
					render(view:"registration", userInstance:user)
				}
			}
			else {
				flash.message = "The code that you entered doesn't match."
				render(view:"registration", model:[userInstance:command])
			}					
		}
		
	} 
}
