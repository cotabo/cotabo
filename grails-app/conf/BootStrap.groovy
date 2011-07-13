import app.taskboard.*
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
		
		def env = Environment.currentEnvironment.name			
		if (env == 'test' || env == 'development') {
			log.info 'Creating test data...'			
			
			def user = User.findByUsername('user')
			def admin = User.findByUsername('admin')
			def column1 = new Column(name:'ToDo', limit:15, workflowStartColumn:true)
			def column2 = new Column(name:'In Progress', limit:4)
			def column3 = new Column(name:'Done!')
			def board = new Board(
				name:'My Test Board',
				description:'This test board is to track the tasks of our Test project',
				users: [user,admin],
				admins: [admin],
			)
			board.addToColumns(column1)
				.addToColumns(column2)
				.addToColumns(column3)
			board.save(flush:true)
			
			def testTasks

			use(TimeCategory) {	
				testTasks = [
					wip: 	[
						[column:column2,  name:'Bootstrap Webserver', description:'Bootstrap machine and apply WebServer profile.',durationHours:3.5, creator:user, assignee:user, sortorder:1, priority:'Critical', color:'#fa7a88', workflowStartDate: new Date() - 10.hours],
						[column:column2, name:'Bootstrap App server', description:'Bootstrap machine and apply Java Appserver profile', durationHours:4.0, creator:user, assignee:user, sortorder:2, priority:'Normal', color:'#faf77a', workflowStartDate: new Date() - 9.hours]
					],
					todo: 	[
						[column:column1, name:'Setup Webserver', description:'Install & configure Apache',durationHours:1.5, creator:user, assignee:user, sortorder:1, priority:'Major', color:'#faf77a', workflowStartDate: new Date() - 9.hours],					 
						[column:column1, name:'Setup WebLogic', description:'Install base software and configure domain.',durationHours:6.0, creator:user, assignee:user, sortorder:2, priority:'Low', color:'#f9d7a9', workflowStartDate: new Date() - 7.hours],
						[column:column1, name:'Install application', description:'Install the application software as describes by the Vendor',durationHours:8.0, creator:user, assignee:user, sortorder:3, priority:'Critical', color:'#faf77a', workflowStartDate: new Date() - 4.hours],
						[column:column1, name:'Configure Monitoring', description:'Setup all monitors (Filesystem, proceses, logs etc)',durationHours:16.0, creator:user, assignee:user, sortorder:4, priority:'Normal', color:'#bcbcf5', workflowStartDate: new Date() - 2.hours],
						[column:column1, name:'Apply Configuration Management', description:'Apply configuration management',durationHours:8.0, creator:user, assignee:user, sortorder:5, priority:'Normal', color:'#faf77a', workflowStartDate: new Date() - 12.hours]
					],
					done: 	[
						[column:column3, name:'Request machines', description:'Request machines at the DataCenter',durationHours:48.0, creator:user, assignee:user, sortorder:1, priority:'Normal', color:'#bcbcf5', workflowStartDate: new Date() - 10.hours, workflowEndDate: new Date() - 1.hour]
					]
				]
			}
			SpringSecurityUtils.doWithAuth('user') {
				testTasks.each {k, v -> v.each {taskService.saveTask(new Task(it))}}
			}
			def tmpIdList = []
			SpringSecurityUtils.doWithAuth('user') {	
				testTasks.wip.each {
					def persistedTask = Task.findByName(it.name)
					tmpIdList << persistedTask.id
					def movementMessage = [
						task: persistedTask.id,
						fromColumn: persistedTask.column.id,
						toColumn: Column.findByName('In Progress').id,
						newTaskOrderIdList: tmpIdList
					]
					taskService.moveTask movementMessage
				}
				testTasks.done.each {
					def persistedTask = Task.findByName(it.name)
					tmpIdList << persistedTask.id
					def movementMessage = [
						task: persistedTask.id,
						fromColumn: persistedTask.column.id,
						toColumn: Column.findByName('In Progress').id,
						newTaskOrderIdList: tmpIdList
					]
					taskService.moveTask movementMessage
				}
				testTasks.done.each {
					def persistedTask = Task.findByName(it.name)
					tmpIdList << persistedTask.id
					def movementMessage = [
						task: persistedTask.id,
						fromColumn: persistedTask.column.id,
						toColumn: Column.findByName('Done!').id,
						newTaskOrderIdList: tmpIdList
					]
					taskService.moveTask movementMessage
				}				
			}	
			
	
			//Now we create some tasks and move them across the board in order 
			//to get some proper event data that we can test with
			def eventTestTasks = []
			for (i in 1..80) {
				eventTestTasks << new Task(
					name:"Task $i", 
					description:'Description $i',
					durationHours:3.5, 
					creator:user, 
					assignee:user, 
					sortorder:15+i, 
					priority:'Critical', 
					color:'#fa7a88',
					column: Column.findByName('ToDo')
				)
			}
			SpringSecurityUtils.doWithAuth('user') {
				eventTestTasks.each { taskService.saveTask(it) }
			}
			def eventTasks = Task.findAllByNameLike('Task %')
			assert eventTasks.size() == 80
			def secondColumnTaskIdList = Column.findByName('In Progress').tasks.collect{it.id}
			//First we move all out 80 tasks to the second column
			for (task in eventTasks) {
				SpringSecurityUtils.doWithAuth('user') {
					secondColumnTaskIdList << task.id
					def movementMessage = [
						task: task.id,
						fromColumn: task.column.id,
						toColumn: Column.findByName('In Progress').id,
						newTaskOrderIdList: secondColumnTaskIdList
					]
					taskService.moveTask movementMessage
				}				
			}
			eventTasks = Task.findAllByNameLike('Task %')
			//than all to the last column
			for (task in eventTasks) {
				SpringSecurityUtils.doWithAuth('user') {
					secondColumnTaskIdList << task.id
					def movementMessage = [
						task: task.id,
						fromColumn: task.column.id,
						toColumn: Column.findByName('Done!').id,
						newTaskOrderIdList: secondColumnTaskIdList
					]
					taskService.moveTask movementMessage
				}
			}					
		}
    }
    def destroy = {
    }
}
