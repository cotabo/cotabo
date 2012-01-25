package org.cotabo

import grails.test.*

class TaskServiceTests extends GrailsUnitTestCase {
	protected void setUp() {
		super.setUp()
	}

	protected void tearDown() {
		super.tearDown()
	}
	
	void testSaveTask(){
		def taskService = new TaskService();
		//Mocking the security service (only principal as this is enough for our purposes
		def springSecurityExpando = new Expando()				
		springSecurityExpando.metaClass.getPrincipal = {return ['username':'testuser']}
		taskService.springSecurityService = springSecurityExpando
		assertEquals 'testuser', springSecurityExpando.principal.username
		
		//Mocking the configuration that we need
		mockConfig '''
		taskboard.colors = ['#faf77a', '#fa7a88', '#bcbcf5', '#f9d7a9']
		taskboard.priorities = ['Critical', 'Major', 'Normal', 'Low']
		// Added by the Spring Security Core plugin:
		grails.plugins.springsecurity.userLookup.userDomainClassName = 'org.cotabo.User'
		grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'org.cotabo.UserRole'
		grails.plugins.springsecurity.authority.className = 'org.cotabo.Role'
		grails.plugins.springsecurity.securityConfigType = SecurityConfigType.Annotation
		'''
		
		def user=new User(
				username: 'testuser',
				password: 'testpassword',
				firstname: 'firstname',
				lastname: 'lastname',
				email: 'e@mail.com'
			)
		
		mockDomain(User,[user])
		
		mockDomain(Role)
		mockDomain(UserRole)
		mockDomain(Board)
		mockDomain(Column)
		mockDomain(Task)
		mockDomain(ColumnStatusEntry)
		
		def board = new Board(name:"board", columns:[])
		def column = new Column(name:"column", board:board, limit:10)
		board.addToColumns(column)
		def task = new Task(name:"task", durationHours:1D, column:column, creator:user, assignee:user, priority:'Normal')
		
		assertTrue board.validate()
		board.save(flush:true)
		
		assertTrue column.validate()
		column.save(flush:true)
		
		assertTrue task.validate()
		task.save(flush:true)
		
		assertEquals 1, Board.list().size()
		assertEquals 1, Column.list().size()
		assertEquals 1, Task.list().size()
		
		assertEquals 1, Task.findAllByColumn(column).size()
		
		def task2 = new Task(name:"task", durationHours:1D, column:column, creator:user, assignee:user, priority:'Normal')
		
		taskService.saveTask(task2)
		
		assertEquals 2, Task.list().size()
		
		assertEquals 2, Task.findAllByColumn(column).size()
		
		assertEquals 1, Column.get(1).tasks.size()
	}
}