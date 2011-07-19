package org.cotabo

import grails.test.*

class BootstrapTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

	void testSeedRoleCount() {
		assertEquals 3, Role.count()
	}
    void testSeedUserCount() {
		assertEquals 3, User.count()		
    }
	
	void testAdminUserExistance() {
		assertNotNull User.findByUsername('admin')
	}
	
	void testUserRoleMapping() {
		def userRoles = UserRole.list()
		assertEquals 5, userRoles.size()
		
		userRoles.each {
			assertNotNull it.user
			assertNotNull it.role
		}
	}
}
