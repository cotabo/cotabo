import app.taskboard.*
import grails.util.Environment

class BootStrap {
	def springSecurityService

    def init = { servletContext ->
		def userRole = Role.findByAuthority("ROLE_USER") ?: new Role(authority: "ROLE_USER").save()
		def reporterRole = Role.findByAuthority("ROLE_REPORTER") ?: new Role(authority: "ROLE_REPORTER").save()
		def adminRole = Role.findByAuthority("ROLE_ADMIN") ?: new Role(authority: "ROLE_ADMIN").save()
		
		def seed = [
			[username: 'admin', password: 'admin', firstname: 'Admin', lastname: 'User', email: 'admin@user.com', roles: [adminRole, userRole]],
			[username: 'report', password: 'report', firstname: 'Report', lastname: 'User', email: 'report@user.com', roles: [reporterRole, userRole]],
			[username: 'user', password: 'user', firstname: 'User', lastname: 'User', email: 'user@user.com', roles: [userRole]]
		]
		
		if (!User.list()) {
			println "No userdata found - creating user seed data."
			seed.each { userData ->
				def user = new User(
					username: userData.username,
					password: springSecurityService.encodePassword(userData.password),
					firstname: userData.firstname,
					lastname: userData.lastname,
					email: userData.email					
				)
				user.save(flush: true)
				if (user.validate()) {
					userData.roles.each { role ->
						UserRole.create user, role
					}
				} 
			}			
		}
		
		def env = Environment.currentEnvironment.name			
		if (env == 'test' || env == 'development') {
			log.info 'Creating test data...'			

			
			def user = User.findByUsername('user')
			def admin = User.findByUsername('admin')			
			def testTasks = [
				wip1: 	[name:'Bootstrap Webserver', description:'Bootstrap machine and apply WebServer profile.',durationHours:3.5, creator:user, assignee:user, sortorder:1],
				todo1: 	[name:'Setup Webserver', description:'Install & configure Apache',durationHours:1.5, creator:user, assignee:user, sortorder:1],
				wip2: 	[name:'Bootstrap App server', description:'Bootstrap machine and apply Java Appserver profile', durationHours:4.0, creator:user, assignee:user, sortorder:2],
				todo2: 	[name:'Setup WebLogic', description:'Install base software and configure domain.',durationHours:6.0, creator:user, assignee:user, sortorder:2],
				todo3: 	[name:'Install application', description:'Install the application software as describes by the Vendor',durationHours:8.0, creator:user, assignee:user, sortorder:3],
				todo4: 	[name:'Configure Monitoring', description:'Setup all monitors (Filesystem, proceses, logs etc)',durationHours:16.0, creator:user, assignee:user, sortorder:4],
				todo5: 	[name:'Apply Configuration Management', description:'Apply configuration management',durationHours:8.0, creator:user, assignee:user, sortorder:5],
				done1: 	[name:'Request machines', description:'Request machines at the DataCenter',durationHours:48.0, creator:user, assignee:user, sortorder:1],
			]
			def column1 = new Column(name:'ToDo', limit:15)
			def column2 = new Column(name:'In Progress', limit:4)
			def column3 = new Column(name:'Done!')
			//Relate everything togather
			column1
				.addToTasks(testTasks.todo1)
				.addToTasks(testTasks.todo2)
				.addToTasks(testTasks.todo3)
				.addToTasks(testTasks.todo4)
				.addToTasks(testTasks.todo5)
				
			column2
				.addToTasks(testTasks.wip1)
				.addToTasks(testTasks.wip2)
			column3
				.addToTasks(testTasks.done1)
			def board = new Board(
				name:'My Test Board', 
				description:'This test board is to track the tasks of our Test project', 
				columns:[column1,column2,column3],
				users: [user,admin],
				admins: [admin],				
			)								
			board.save(flush:true)
		}
    }
    def destroy = {
    }
}
