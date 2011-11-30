package org.cotabo


import grails.test.*
import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec

class BoardTagLibTests extends TagLibUnitTestCase {
    protected void setUp() {
        super.setUp()	
		mockTagLib(BoardTagLib)
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	void testModal() {
		def expected = """
		<div id="delete_dialog" class="modal hide fade">
			<div class="modal-header">
			    <a href="#" class="close">&times;</a>
		    	<h3>Delete Board</h3>
			</div>
	    <div class="modal-body">
	    	<p>Do you really want to delete this board?</p>
	    </div>
			<div class="modal-footer">
	    	<a href="#" class="btn primary">Delete</a>
	  		<a href="#" class="btn secondary">Cancel</a>
		    </div>
		<script type="text/javascript">
		jQuery(function(){
            \$('[data-controls-modal]').click(function() {
            document.modalHelperHref = \$(this).attr('href');
	    });
            \$('#delete_dialog > .modal-footer > a.secondary').click(function() {
		        \$('#delete_dialog').modal('hide');
	        });
			\$('#delete_dialog').bind('show', function(e) {
				\$('#delete_dialog > .modal-footer > a.primary').attr('href', document.modalHelperHref)		
			});
				
			\$('#delete_dialog').bind('hide', function(e) {
				//reset the href
				\$('#delete_dialog > .modal-footer > a:first').attr('href', '#');		
			});
		});
		</script>"""
		
		def tb = new BoardTagLib()
		tb.modal([
				id: "delete_dialog", 
				header:"Delete Board", 
				body: "Do you really want to delete this board?", 
				primary: "Delete", 
				secondary: "Cancel"
			])
		
		assertEquals expected, tb.out.toString()
	}

}
