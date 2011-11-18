package org.cotabo

import grails.test.*

class BoardControllerIntegrationTests extends GroovyTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testExportWoBlocks() {
		def expected = '''<boards>
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
            <colors>
              <taskColor id='1'>
                <color>#85fd81</color>
                <name>Rethink</name>
              </taskColor>
            </colors>
            <creator>user</creator>
            <description>Install &amp; configure Apache</description>
            <details />
            <name>Setup Webserver</name>
            <priority>Major</priority>
          </task>
          <task id='4'>
            <archived>false</archived>
            <assignee>user</assignee>
            <colors>
              <taskColor id='1'>
                <color>#85fd81</color>
                <name>Rethink</name>
              </taskColor>
            </colors>
            <creator>user</creator>
            <description>Install base software and configure domain.</description>
            <details />
            <name>Setup WebLogic</name>
            <priority>Low</priority>
          </task>
          <task id='5'>
            <archived>false</archived>
            <assignee>user</assignee>
            <colors>
              <taskColor id='1'>
                <color>#85fd81</color>
                <name>Rethink</name>
              </taskColor>
            </colors>
            <creator>user</creator>
            <description>Install the application software as describes by the Vendor</description>
            <details />
            <name>Install application</name>
            <priority>Critical</priority>
          </task>
          <task id='6'>
            <archived>false</archived>
            <assignee>user</assignee>
            <colors>
              <taskColor id='1'>
                <color>#85fd81</color>
                <name>Rethink</name>
              </taskColor>
            </colors>
            <creator>user</creator>
            <description>Setup all monitors (Filesystem, proceses, logs etc)</description>
            <details />
            <name>Configure Monitoring</name>
            <priority>Normal</priority>
          </task>
          <task id='7'>
            <archived>false</archived>
            <assignee>user</assignee>
            <colors>
              <taskColor id='1'>
                <color>#85fd81</color>
                <name>Rethink</name>
              </taskColor>
            </colors>
            <creator>user</creator>
            <description>Apply configuration management</description>
            <details />
            <name>Apply Configuration Management</name>
            <priority>Normal</priority>
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
            <colors>
              <taskColor id='1'>
                <color>#85fd81</color>
                <name>Rethink</name>
              </taskColor>
            </colors>
            <creator>user</creator>
            <description>Bootstrap machine and apply WebServer profile.</description>
            <details />
            <name>Bootstrap Webserver</name>
            <priority>Critical</priority>
          </task>
          <task id='2'>
            <archived>false</archived>
            <assignee>user</assignee>
            <colors>
              <taskColor id='1'>
                <color>#85fd81</color>
                <name>Rethink</name>
              </taskColor>
            </colors>
            <creator>user</creator>
            <description>Bootstrap machine and apply Java Appserver profile</description>
            <details />
            <name>Bootstrap App server</name>
            <priority>Normal</priority>
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
            <colors>
              <taskColor id='1'>
                <color>#85fd81</color>
                <name>Rethink</name>
              </taskColor>
            </colors>
            <creator>user</creator>
            <description>Request machines at the DataCenter</description>
            <details />
            <name>Request machines</name>
            <priority>Normal</priority>
          </task>
          <task id='9'>
            <archived>false</archived>
            <assignee>user</assignee>
            <colors>
              <taskColor id='2'>
                <color>#fafaa8</color>
                <name>App</name>
              </taskColor>
            </colors>
            <creator>user</creator>
            <description>Description $i</description>
            <details />
            <name>Task 1</name>
            <priority>Normal</priority>
          </task>
          <task id='10'>
            <archived>false</archived>
            <assignee>user</assignee>
            <colors>
              <taskColor id='2'>
                <color>#fafaa8</color>
                <name>App</name>
              </taskColor>
            </colors>
            <creator>user</creator>
            <description>Description $i</description>
            <details />
            <name>Task 2</name>
            <priority>Normal</priority>
          </task>
          <task id='11'>
            <archived>false</archived>
            <assignee>user</assignee>
            <colors>
              <taskColor id='2'>
                <color>#fafaa8</color>
                <name>App</name>
              </taskColor>
            </colors>
            <creator>user</creator>
            <description>Description $i</description>
            <details />
            <name>Task 3</name>
            <priority>Normal</priority>
          </task>
          <task id='12'>
            <archived>false</archived>
            <assignee>user</assignee>
            <colors>
              <taskColor id='2'>
                <color>#fafaa8</color>
                <name>App</name>
              </taskColor>
            </colors>
            <creator>user</creator>
            <description>Description $i</description>
            <details />
            <name>Task 4</name>
            <priority>Normal</priority>
          </task>
          <task id='13'>
            <archived>false</archived>
            <assignee>user</assignee>
            <colors>
              <taskColor id='2'>
                <color>#fafaa8</color>
                <name>App</name>
              </taskColor>
            </colors>
            <creator>user</creator>
            <description>Description $i</description>
            <details />
            <name>Task 5</name>
            <priority>Normal</priority>
          </task>
          <task id='14'>
            <archived>false</archived>
            <assignee>user</assignee>
            <colors>
              <taskColor id='2'>
                <color>#fafaa8</color>
                <name>App</name>
              </taskColor>
            </colors>
            <creator>user</creator>
            <description>Description $i</description>
            <details />
            <name>Task 6</name>
            <priority>Normal</priority>
          </task>
          <task id='15'>
            <archived>false</archived>
            <assignee>user</assignee>
            <colors>
              <taskColor id='2'>
                <color>#fafaa8</color>
                <name>App</name>
              </taskColor>
            </colors>
            <creator>user</creator>
            <description>Description $i</description>
            <details />
            <name>Task 7</name>
            <priority>Normal</priority>
          </task>
          <task id='16'>
            <archived>false</archived>
            <assignee>user</assignee>
            <colors>
              <taskColor id='2'>
                <color>#fafaa8</color>
                <name>App</name>
              </taskColor>
            </colors>
            <creator>user</creator>
            <description>Description $i</description>
            <details />
            <name>Task 8</name>
            <priority>Normal</priority>
          </task>
          <task id='17'>
            <archived>false</archived>
            <assignee>user</assignee>
            <colors>
              <taskColor id='2'>
                <color>#fafaa8</color>
                <name>App</name>
              </taskColor>
            </colors>
            <creator>user</creator>
            <description>Description $i</description>
            <details />
            <name>Task 9</name>
            <priority>Normal</priority>
          </task>
          <task id='18'>
            <archived>false</archived>
            <assignee>user</assignee>
            <colors>
              <taskColor id='2'>
                <color>#fafaa8</color>
                <name>App</name>
              </taskColor>
            </colors>
            <creator>user</creator>
            <description>Description $i</description>
            <details />
            <name>Task 10</name>
            <priority>Normal</priority>
          </task>
        </tasks>
        <workflowEndColumn>false</workflowEndColumn>
        <workflowStartColumn>false</workflowStartColumn>
      </column>
    </columns>
    <description>This test board is to track the tasks of our Test project</description>
    <name>My Test Board</name>
  </board>
</boards>'''
		
		def exportablesBackup = Task.exportables
		//Those elements are removed because Date's suck in testing
		Task.exportables = exportablesBackup - ['blocks', 'workflowStartDate', 'workflowEndDate']
		
		def boardcontroller = new BoardController();
		def result = boardcontroller.export();
		
		Task.exportables = exportablesBackup
		
		assertNotNull boardcontroller.response.contentAsString
		println boardcontroller.response.contentAsString
		
		assertEquals expected, boardcontroller.response.contentAsString
    }
}
