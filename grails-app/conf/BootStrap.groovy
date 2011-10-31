import org.cotabo.*
import grails.util.Environment
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import groovy.time.TimeCategory

class BootStrap {
	def springSecurityService
	def taskService

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
		
		/* Create test data for test purposes (test-app) and running in development mode */
		def env = Environment.currentEnvironment.name			
		if (env == 'test' || env == 'development') {
			println 'Creating test data...'			
			
			def user = User.findByUsername('user')
			def admin = User.findByUsername('admin')
			def column1 = new Column(name:'ToDo', limit:15, workflowStartColumn:true)
			def column2 = new Column(name:'In Progress', limit:4)
			def column3 = new Column(name:'Done!')
			def board = new Board(
				name:'My Test Board',
				description:'This test board is to track the tasks of our Test project'
			).addToColumns(column1)
				.addToColumns(column2)
				.addToColumns(column3)
			board.save()
			
			UserBoard.create(admin, board, RoleEnum.ADMIN)
			UserBoard.create(user, board, RoleEnum.USER)			
			
			def testTasks
			
			def color1 = new TaskColor(color:'#85fd81', name:'Rethink').save(flush:true)
			def color2 = new TaskColor(color:'#fafaa8', name:'App').save(flush:true)
			def color3 = new TaskColor(color:'#faaca8', name:'General').save(flush:true)
			def color4 = new TaskColor(color:'#81b6fd', name:'Web').save(flush:true)
			
			def colors = [color1, color2, color3, color4]
			
			colors.each{color -> board.addToColors(color)}
			board.save(flush:true)

			use(TimeCategory) {	
				def currentDate = Date.parse("dd/MM/yyyy HH:mm:ss SSS z", "02/04/2011 13:13:13 013 GMT+2:00")
				testTasks = [
					wip: 	[
						[column:column2, name:'Bootstrap Webserver', description:'Bootstrap machine and apply WebServer profile.',durationHours:3.5, creator:user, assignee:user, priority:'Critical', workflowStartDate: currentDate - 10.hours],
						[column:column2, name:'Bootstrap App server', description:'Bootstrap machine and apply Java Appserver profile', durationHours:4.0, creator:user, assignee:user, priority:'Normal', workflowStartDate: currentDate - 9.hours]						
					],
					todo: 	[
						[column:column1, name:'Setup Webserver', description:'Install & configure Apache',durationHours:1.5, creator:user, assignee:user, priority:'Major', workflowStartDate: currentDate - 9.hours],					 
						[column:column1, name:'Setup WebLogic', description:'Install base software and configure domain.',durationHours:6.0, creator:user, assignee:user, priority:'Low', workflowStartDate: currentDate - 7.hours],
						[column:column1, name:'Install application', description:'Install the application software as describes by the Vendor',durationHours:8.0, creator:user, assignee:user, priority:'Critical', workflowStartDate: currentDate - 4.hours],
						[column:column1, name:'Configure Monitoring', description:'Setup all monitors (Filesystem, proceses, logs etc)',durationHours:16.0, creator:user, assignee:user, priority:'Normal', workflowStartDate: currentDate - 2.hours],
						[column:column1, name:'Apply Configuration Management', description:'Apply configuration management',durationHours:8.0, creator:user, assignee:user, priority:'Normal', workflowStartDate: currentDate - 12.hours]
					],
					done: 	[
						[column:column3, name:'Request machines', description:'Request machines at the DataCenter',durationHours:48.0, creator:user, assignee:user, priority:'Normal', workflowStartDate: currentDate - 10.hours, workflowEndDate: currentDate - 1.hour]
					]
				]
			}
			SpringSecurityUtils.doWithAuth('user') {
				testTasks.each {k, v -> v.each {
					def task = new Task(it)
					task.addToColors(color1).save()
					taskService.saveTask(task)}}
			}
						
			SpringSecurityUtils.doWithAuth('user') {	
				testTasks.wip.each {
					def persistedTask = Task.findByName(it.name)
					taskService.moveTask persistedTask.column, Column.findByName('In Progress'), persistedTask									
					persistedTask.blocked = true
					persistedTask.save()
				}
				testTasks.done.each {
					def persistedTask = Task.findByName(it.name)					
					taskService.moveTask persistedTask.column, Column.findByName('In Progress'), persistedTask
				}
				testTasks.done.each {
					def persistedTask = Task.findByName(it.name)					
					taskService.moveTask persistedTask.column, Column.findByName('Done!'), persistedTask
				}				
			}	
			
	
			//Now we create some tasks and move them across the board in order 
			//to get some proper event data that we can test with
			def eventTestTasks = []
			for (i in 1..10) {
				def task = new Task(
					name:"Task $i", 
					description:'Description $i',
					durationHours:3.5, 
					creator:user, 
					assignee:user, 					
					priority:'Normal', 
					column: Column.findByName('ToDo')
				)
				task.addToColors(color2).save()
				
				eventTestTasks << task
				
			}
			
			SpringSecurityUtils.doWithAuth('user') {
				eventTestTasks.each {
					taskService.saveTask(it) }
			}
			def eventTasks = Task.findAllByNameLike('Task %')
			assert eventTasks.size() == 10			
			//First we move all out 80 tasks to the second column
			for (task in eventTasks) {
				SpringSecurityUtils.doWithAuth('user') {
					taskService.moveTask task.column, Column.findByName('In Progress'), task
				}				
			}
			eventTasks = Task.findAllByNameLike('Task %')
			//than all to the last column
			for (task in eventTasks) {
				SpringSecurityUtils.doWithAuth('user') {					
					taskService.moveTask task.column, Column.findByName('Done!'), task
				}
			}					
		}
    }
    def destroy = {
    }
}
