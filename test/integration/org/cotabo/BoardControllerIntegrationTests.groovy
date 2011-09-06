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
            <assignee>User User</assignee>
            <color>#fafaa8</color>
            <creator>User User</creator>
            <description>Install &amp; configure Apache</description>
            <details />
            <name>Setup Webserver</name>
            <priority>Major</priority>
            <sortorder>1</sortorder>
            <workflowEndDate />
            <workflowStartDate>2011-04-02 13:13:13.013</workflowStartDate>
          </task>
          <task id='4'>
            <archived>false</archived>
            <assignee>User User</assignee>
            <color>#fafaa8</color>
            <creator>User User</creator>
            <description>Install base software and configure domain.</description>
            <details />
            <name>Setup WebLogic</name>
            <priority>Low</priority>
            <sortorder>2</sortorder>
            <workflowEndDate />
            <workflowStartDate>2011-04-02 13:13:13.013</workflowStartDate>
          </task>
          <task id='5'>
            <archived>false</archived>
            <assignee>User User</assignee>
            <color>#faaca8</color>
            <creator>User User</creator>
            <description>Install the application software as describes by the Vendor</description>
            <details />
            <name>Install application</name>
            <priority>Critical</priority>
            <sortorder>3</sortorder>
            <workflowEndDate />
            <workflowStartDate>2011-04-02 13:13:13.013</workflowStartDate>
          </task>
          <task id='6'>
            <archived>false</archived>
            <assignee>User User</assignee>
            <color>#81b6fd</color>
            <creator>User User</creator>
            <description>Setup all monitors (Filesystem, proceses, logs etc)</description>
            <details />
            <name>Configure Monitoring</name>
            <priority>Normal</priority>
            <sortorder>4</sortorder>
            <workflowEndDate />
            <workflowStartDate>2011-04-02 13:13:13.013</workflowStartDate>
          </task>
          <task id='7'>
            <archived>false</archived>
            <assignee>User User</assignee>
            <color>#faaca8</color>
            <creator>User User</creator>
            <description>Apply configuration management</description>
            <details />
            <name>Apply Configuration Management</name>
            <priority>Normal</priority>
            <sortorder>5</sortorder>
            <workflowEndDate />
            <workflowStartDate>2011-04-02 13:13:13.013</workflowStartDate>
          </task>
        </tasks>
        <workflowStartColumn>true</workflowStartColumn>
      </column>
      <column id='2'>
        <description />
        <limit>4</limit>
        <name>In Progress</name>
        <tasks>
          <task id='1'>
            <archived>false</archived>
            <assignee>User User</assignee>
            <color>#85fd81</color>
            <creator>User User</creator>
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
            <assignee>User User</assignee>
            <color>#fafaa8</color>
            <creator>User User</creator>
            <description>Bootstrap machine and apply Java Appserver profile</description>
            <details />
            <name>Bootstrap App server</name>
            <priority>Normal</priority>
            <sortorder>1</sortorder>
            <workflowEndDate />
            <workflowStartDate>1969-12-31 16:00:00.0</workflowStartDate>
          </task>
        </tasks>
        <workflowStartColumn>false</workflowStartColumn>
      </column>
      <column id='3'>
        <description />
        <limit>0</limit>
        <name>Done!</name>
        <tasks>
          <task id='8'>
            <archived>false</archived>
            <assignee>User User</assignee>
            <color>#fafaa8</color>
            <creator>User User</creator>
            <description>Request machines at the DataCenter</description>
            <details />
            <name>Request machines</name>
            <priority>Normal</priority>
            <sortorder>3</sortorder>
            <workflowEndDate>2011-04-02 17:13:13.013</workflowEndDate>
            <workflowStartDate>1969-12-31 15:00:00.0</workflowStartDate>
          </task>
          <task id='9'>
            <archived>false</archived>
            <assignee>User User</assignee>
            <color>#fafaa8</color>
            <creator>User User</creator>
            <description>Description $i</description>
            <details />
            <name>Task 1</name>
            <priority>Normal</priority>
            <sortorder>12</sortorder>
            <workflowEndDate>2011-04-02 17:13:13.013</workflowEndDate>
            <workflowStartDate>2011-04-02 13:13:13.013</workflowStartDate>
          </task>
          <task id='10'>
            <archived>false</archived>
            <assignee>User User</assignee>
            <color>#fafaa8</color>
            <creator>User User</creator>
            <description>Description $i</description>
            <details />
            <name>Task 2</name>
            <priority>Normal</priority>
            <sortorder>13</sortorder>
            <workflowEndDate>2011-04-02 17:13:13.013</workflowEndDate>
            <workflowStartDate>2011-04-02 13:13:13.013</workflowStartDate>
          </task>
          <task id='11'>
            <archived>false</archived>
            <assignee>User User</assignee>
            <color>#fafaa8</color>
            <creator>User User</creator>
            <description>Description $i</description>
            <details />
            <name>Task 3</name>
            <priority>Normal</priority>
            <sortorder>14</sortorder>
            <workflowEndDate>2011-04-02 17:13:13.013</workflowEndDate>
            <workflowStartDate>2011-04-02 13:13:13.013</workflowStartDate>
          </task>
          <task id='12'>
            <archived>false</archived>
            <assignee>User User</assignee>
            <color>#fafaa8</color>
            <creator>User User</creator>
            <description>Description $i</description>
            <details />
            <name>Task 4</name>
            <priority>Normal</priority>
            <sortorder>15</sortorder>
            <workflowEndDate>2011-04-02 17:13:13.013</workflowEndDate>
            <workflowStartDate>2011-04-02 13:13:13.013</workflowStartDate>
          </task>
          <task id='13'>
            <archived>false</archived>
            <assignee>User User</assignee>
            <color>#fafaa8</color>
            <creator>User User</creator>
            <description>Description $i</description>
            <details />
            <name>Task 5</name>
            <priority>Normal</priority>
            <sortorder>16</sortorder>
            <workflowEndDate>2011-04-02 17:13:13.013</workflowEndDate>
            <workflowStartDate>2011-04-02 13:13:13.013</workflowStartDate>
          </task>
          <task id='14'>
            <archived>false</archived>
            <assignee>User User</assignee>
            <color>#fafaa8</color>
            <creator>User User</creator>
            <description>Description $i</description>
            <details />
            <name>Task 6</name>
            <priority>Normal</priority>
            <sortorder>17</sortorder>
            <workflowEndDate>2011-04-02 17:13:13.013</workflowEndDate>
            <workflowStartDate>2011-04-02 13:13:13.013</workflowStartDate>
          </task>
          <task id='15'>
            <archived>false</archived>
            <assignee>User User</assignee>
            <color>#fafaa8</color>
            <creator>User User</creator>
            <description>Description $i</description>
            <details />
            <name>Task 7</name>
            <priority>Normal</priority>
            <sortorder>18</sortorder>
            <workflowEndDate>2011-04-02 17:13:13.013</workflowEndDate>
            <workflowStartDate>2011-04-02 13:13:13.013</workflowStartDate>
          </task>
          <task id='16'>
            <archived>false</archived>
            <assignee>User User</assignee>
            <color>#fafaa8</color>
            <creator>User User</creator>
            <description>Description $i</description>
            <details />
            <name>Task 8</name>
            <priority>Normal</priority>
            <sortorder>19</sortorder>
            <workflowEndDate>2011-04-02 17:13:13.013</workflowEndDate>
            <workflowStartDate>2011-04-02 13:13:13.013</workflowStartDate>
          </task>
          <task id='17'>
            <archived>false</archived>
            <assignee>User User</assignee>
            <color>#fafaa8</color>
            <creator>User User</creator>
            <description>Description $i</description>
            <details />
            <name>Task 9</name>
            <priority>Normal</priority>
            <sortorder>20</sortorder>
            <workflowEndDate>2011-04-02 17:13:13.013</workflowEndDate>
            <workflowStartDate>2011-04-02 13:13:13.013</workflowStartDate>
          </task>
          <task id='18'>
            <archived>false</archived>
            <assignee>User User</assignee>
            <color>#fafaa8</color>
            <creator>User User</creator>
            <description>Description $i</description>
            <details />
            <name>Task 10</name>
            <priority>Normal</priority>
            <sortorder>21</sortorder>
            <workflowEndDate>2011-04-02 17:13:13.013</workflowEndDate>
            <workflowStartDate>2011-04-02 13:13:13.013</workflowStartDate>
          </task>
        </tasks>
        <workflowStartColumn>false</workflowStartColumn>
      </column>
    </columns>
    <description>This test board is to track the tasks of our Test project</description>
    <name>My Test Board</name>
  </board>
</boards>'''
		
		def exportablesBackup = Task.exportables
		Task.exportables = ['name', 'description', 'details', 'priority', 'color', 'creator', 'assignee', 'archived', 'sortorder', 'workflowStartDate', 'workflowEndDate']
		
		def boardcontroller = new BoardController();
		def result = boardcontroller.export();
		
		Task.exportables = exportablesBackup
		
		assertNotNull boardcontroller.response.contentAsString
		println boardcontroller.response.contentAsString
		
		assertEquals expected, boardcontroller.response.contentAsString
    }
}
