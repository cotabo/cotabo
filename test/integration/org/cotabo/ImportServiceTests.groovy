package org.cotabo

import grails.test.*

class ImportServiceTests extends GrailsUnitTestCase {
	
	def importService 
	
    protected void setUp() {
		super.setUp()
		//Mocking the getPrincipal on the SpringSecurityService to always return 'user' on getUsername
		def springSecurityExpando = new Expando()
		springSecurityExpando.metaClass.getPrincipal = {return ['username':'user']}
        importService = new ImportService()
		importService.springSecurityService = springSecurityExpando
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testImportBoards() {
		//Exported from out bootstrapped data
		//Only changed the board name because of uniqueness
		def importXml = '''
<boards>
  <board id='1'>
    <columns>
      <column id='1'>
        <description />
        <limit>15</limit>
        <name>ToDo</name>
        <tasks>
          <task id='3'>
            <archived>false</archived>
            <assignee>user</assignee>
            <blocks />
            <color>#fafaa8</color>
            <creator>user</creator>
            <description>Install &amp; configure Apache</description>
            <details />
            <name>Setup Webserver</name>
            <priority>Major</priority>
            <sortorder>1</sortorder>
            <workflowEndDate />
            <workflowStartDate>2011-09-06 20:31:09.145</workflowStartDate>
          </task>
          <task id='4'>
            <archived>false</archived>
            <assignee>user</assignee>
            <blocks />
            <color>#fafaa8</color>
            <creator>user</creator>
            <description>Install base software and configure domain.</description>
            <details />
            <name>Setup WebLogic</name>
            <priority>Low</priority>
            <sortorder>2</sortorder>
            <workflowEndDate />
            <workflowStartDate>2011-09-06 20:31:09.174</workflowStartDate>
          </task>
          <task id='5'>
            <archived>false</archived>
            <assignee>user</assignee>
            <blocks />
            <color>#faaca8</color>
            <creator>user</creator>
            <description>Install the application software as describes by the Vendor</description>
            <details />
            <name>Install application</name>
            <priority>Critical</priority>
            <sortorder>3</sortorder>
            <workflowEndDate />
            <workflowStartDate>2011-09-06 20:31:09.204</workflowStartDate>
          </task>
          <task id='6'>
            <archived>false</archived>
            <assignee>user</assignee>
            <blocks />
            <color>#81b6fd</color>
            <creator>user</creator>
            <description>Setup all monitors (Filesystem, proceses, logs etc)</description>
            <details />
            <name>Configure Monitoring</name>
            <priority>Normal</priority>
            <sortorder>4</sortorder>
            <workflowEndDate />
            <workflowStartDate>2011-09-06 20:31:09.225</workflowStartDate>
          </task>
          <task id='7'>
            <archived>false</archived>
            <assignee>user</assignee>
            <blocks />
            <color>#faaca8</color>
            <creator>user</creator>
            <description>Apply configuration management</description>
            <details />
            <name>Apply Configuration Management</name>
            <priority>Normal</priority>
            <sortorder>5</sortorder>
            <workflowEndDate />
            <workflowStartDate>2011-09-06 20:31:09.255</workflowStartDate>
          </task>
        </tasks>
        <workflowEndColumn>false</workflowEndColumn>
        <workflowStartColumn>true</workflowStartColumn>
      </column>
      <column id='2'>
        <description />
        <limit>4</limit>
        <name>In Progress</name>
        <tasks>
          <task id='1'>
            <archived>false</archived>
            <assignee>user</assignee>
            <blocks>
              <block id='1'>
                <dateClosed />
                <dateCreated>2011-09-06 20:31:09.405</dateCreated>
              </block>
            </blocks>
            <color>#85fd81</color>
            <creator>user</creator>
            <description>Bootstrap machine and apply WebServer profile.</description>
            <details />
            <name>Bootstrap Webserver</name>
            <priority>Critical</priority>
            <sortorder>0</sortorder>
            <workflowEndDate />
            <workflowStartDate>1969-12-31 15:00:00.0</workflowStartDate>
          </task>
          <task id='2'>
            <archived>false</archived>
            <assignee>user</assignee>
            <blocks>
              <block id='2'>
                <dateClosed />
                <dateCreated>2011-09-06 20:31:09.455</dateCreated>
              </block>
            </blocks>
            <color>#fafaa8</color>
            <creator>user</creator>
            <description>Bootstrap machine and apply Java Appserver profile</description>
            <details />
            <name>Bootstrap App server</name>
            <priority>Normal</priority>
            <sortorder>1</sortorder>
            <workflowEndDate />
            <workflowStartDate>1969-12-31 16:00:00.0</workflowStartDate>
          </task>
        </tasks>
        <workflowEndColumn>false</workflowEndColumn>
        <workflowStartColumn>false</workflowStartColumn>
      </column>
      <column id='3'>
        <description />
        <limit>0</limit>
        <name>Done!</name>
        <tasks>
          <task id='8'>
            <archived>false</archived>
            <assignee>user</assignee>
            <blocks />
            <color>#fafaa8</color>
            <creator>user</creator>
            <description>Request machines at the DataCenter</description>
            <details />
            <name>Request machines</name>
            <priority>Normal</priority>
            <sortorder>3</sortorder>
            <workflowEndDate>2011-09-06 20:31:09.515</workflowEndDate>
            <workflowStartDate>1969-12-31 15:00:00.0</workflowStartDate>
          </task>
          <task id='9'>
            <archived>false</archived>
            <assignee>user</assignee>
            <blocks />
            <color>#fafaa8</color>
            <creator>user</creator>
            <description>Description $i</description>
            <details />
            <name>Task 1</name>
            <priority>Normal</priority>
            <sortorder>12</sortorder>
            <workflowEndDate>2011-09-06 20:31:10.343</workflowEndDate>
            <workflowStartDate>2011-09-06 20:31:09.899</workflowStartDate>
          </task>
          <task id='10'>
            <archived>false</archived>
            <assignee>user</assignee>
            <blocks />
            <color>#fafaa8</color>
            <creator>user</creator>
            <description>Description $i</description>
            <details />
            <name>Task 2</name>
            <priority>Normal</priority>
            <sortorder>13</sortorder>
            <workflowEndDate>2011-09-06 20:31:10.385</workflowEndDate>
            <workflowStartDate>2011-09-06 20:31:09.941</workflowStartDate>
          </task>
          <task id='11'>
            <archived>false</archived>
            <assignee>user</assignee>
            <blocks />
            <color>#fafaa8</color>
            <creator>user</creator>
            <description>Description $i</description>
            <details />
            <name>Task 3</name>
            <priority>Normal</priority>
            <sortorder>14</sortorder>
            <workflowEndDate>2011-09-06 20:31:10.425</workflowEndDate>
            <workflowStartDate>2011-09-06 20:31:09.981</workflowStartDate>
          </task>
          <task id='12'>
            <archived>false</archived>
            <assignee>user</assignee>
            <blocks />
            <color>#fafaa8</color>
            <creator>user</creator>
            <description>Description $i</description>
            <details />
            <name>Task 4</name>
            <priority>Normal</priority>
            <sortorder>15</sortorder>
            <workflowEndDate>2011-09-06 20:31:10.545</workflowEndDate>
            <workflowStartDate>2011-09-06 20:31:10.031</workflowStartDate>
          </task>
          <task id='13'>
            <archived>false</archived>
            <assignee>user</assignee>
            <blocks />
            <color>#fafaa8</color>
            <creator>user</creator>
            <description>Description $i</description>
            <details />
            <name>Task 5</name>
            <priority>Normal</priority>
            <sortorder>16</sortorder>
            <workflowEndDate>2011-09-06 20:31:10.575</workflowEndDate>
            <workflowStartDate>2011-09-06 20:31:10.071</workflowStartDate>
          </task>
          <task id='14'>
            <archived>false</archived>
            <assignee>user</assignee>
            <blocks />
            <color>#fafaa8</color>
            <creator>user</creator>
            <description>Description $i</description>
            <details />
            <name>Task 6</name>
            <priority>Normal</priority>
            <sortorder>17</sortorder>
            <workflowEndDate>2011-09-06 20:31:10.615</workflowEndDate>
            <workflowStartDate>2011-09-06 20:31:10.111</workflowStartDate>
          </task>
          <task id='15'>
            <archived>false</archived>
            <assignee>user</assignee>
            <blocks />
            <color>#fafaa8</color>
            <creator>user</creator>
            <description>Description $i</description>
            <details />
            <name>Task 7</name>
            <priority>Normal</priority>
            <sortorder>18</sortorder>
            <workflowEndDate>2011-09-06 20:31:10.655</workflowEndDate>
            <workflowStartDate>2011-09-06 20:31:10.166</workflowStartDate>
          </task>
          <task id='16'>
            <archived>false</archived>
            <assignee>user</assignee>
            <blocks />
            <color>#fafaa8</color>
            <creator>user</creator>
            <description>Description $i</description>
            <details />
            <name>Task 8</name>
            <priority>Normal</priority>
            <sortorder>19</sortorder>
            <workflowEndDate>2011-09-06 20:31:10.685</workflowEndDate>
            <workflowStartDate>2011-09-06 20:31:10.207</workflowStartDate>
          </task>
          <task id='17'>
            <archived>false</archived>
            <assignee>user</assignee>
            <blocks />
            <color>#fafaa8</color>
            <creator>user</creator>
            <description>Description $i</description>
            <details />
            <name>Task 9</name>
            <priority>Normal</priority>
            <sortorder>20</sortorder>
            <workflowEndDate>2011-09-06 20:31:10.725</workflowEndDate>
            <workflowStartDate>2011-09-06 20:31:10.245</workflowStartDate>
          </task>
          <task id='18'>
            <archived>false</archived>
            <assignee>user</assignee>
            <blocks />
            <color>#fafaa8</color>
            <creator>user</creator>
            <description>Description $i</description>
            <details />
            <name>Task 10</name>
            <priority>Normal</priority>
            <sortorder>21</sortorder>
            <workflowEndDate>2011-09-06 20:31:10.765</workflowEndDate>
            <workflowStartDate>2011-09-06 20:31:10.293</workflowStartDate>
          </task>
        </tasks>
        <workflowEndColumn>false</workflowEndColumn>
        <workflowStartColumn>false</workflowStartColumn>
      </column>
    </columns>
    <description>This test board is to track the tasks of our Test project</description>
    <name>Import Test Board</name>
  </board>
</boards>
'''

		importService.importBoards(importXml)
		
		def board = Board.findByName('Import Test Board')		
		
		assertNotNull board
		assertEquals 3, board.columns.size()		
		assertEquals 18, board.columns.sum { it.tasks.size() }
		
		assertEquals 'ToDo', board.columns[0].name
		assertEquals 'In Progress', board.columns[1].name
		assertEquals 'Done!', board.columns[2].name
		
		assertEquals 5, board.columns[0].tasks.size()
		assertEquals 2, board.columns[1].tasks.size()
		assertEquals 11, board.columns[2].tasks.size()
		
		def lastTask = board.columns.last().tasks.last()
		assertFalse lastTask.archived
		assertEquals User.findByUsername('user'), lastTask.creator
		assertEquals User.findByUsername('user'), lastTask.assignee
		assertEquals Date.parse("yyyy-MM-dd HH:mm:ss.SSS", '2011-09-06 20:31:10.293'), lastTask.workflowStartDate
		assertEquals Date.parse("yyyy-MM-dd HH:mm:ss.SSS", '2011-09-06 20:31:10.765'), lastTask.workflowEndDate
		assertEquals 'Task 10', lastTask.name
		
		def blockTask = board.columns[1].tasks.find { it.name == 'Bootstrap App server'}
		assertEquals 1, blockTask.blocks.size()
		assertTrue blockTask.isBlocked()
		
    }
}
