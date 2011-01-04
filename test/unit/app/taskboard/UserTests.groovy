package app.taskboard

import grails.test.*

class UserTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
		mockDomain(User)
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSuccessfullCreation() {
		def user = new User(
				username: 'testuser',
				password: 'testpassword',
				firstname: 'firstname',
				lastname: 'lastname',
				email: 'e@mail.com'
		)
		assertTrue user.validate()
		def userId = user.save()
		assertNotNull userId		
		assertEquals 1, User.list().size()
    }
	
	void testFailWrongEmail() {
		def user = new User(
			username: 'testuser',
			password: 'testpassword',
			firstname: 'firstname',
			lastname: 'lastname',
			email: 'abc.com'
		)
		assertFalse user.validate()		
		assertNull user.save()
	}
	
	void testFailBlankUsername() {
		def user = new User(
			username: '   ',
			password: 'testpassword',
			firstname: 'firstname',
			lastname: 'lastname',
			email: 'abc@abc.com'
		)
		assertFalse user.validate()
		assertNull user.save()
	}
	
	void testFailBlankPassword() {
		def user = new User(
			username: 'testuser',
			password: ' ',
			firstname: 'firstname',
			lastname: 'lastname',
			email: 'abc@abc.com'
		)
	}
	
	void testFailSmallPassword() {
		def user = new User(
			username: 'testuser',
			//minSize is 5
			password: 'pass',
			firstname: 'firstname',
			lastname: 'lastname',
			email: 'abc@abc.com'
		)
		assertFalse user.validate()
		assertNull user.save()
	}
	
	void testUnUniqueUsername() {
		def user = new User(
			username: 'testuser',
			password: 'testpassword',
			firstname: 'firstname',
			lastname: 'lastname',
			email: 'e@mail.com'
		)
		assertNotNull user.save()
		def user2 = new User(
			username: 'testuser',
			password: 'testpassword',
			firstname: 'firstname',
			lastname: 'lastname',
			email: 'e@mail.com'
		)		
		user2.validate()
		assertNull user2.save()
	}
}
