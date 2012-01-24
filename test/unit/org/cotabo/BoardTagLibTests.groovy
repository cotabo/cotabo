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
		<div id="delete_dialog" class="modal hide">
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
				\$('#delete_dialog > .modal-footer > a:first').attr('href', '#');
				\$('#delete_dialog > .modal-body > .alert-message.block-message.error').remove();
			});
			//We need to create the modal and than hide it. Otherwhise the jQuery sortable doesn't work.
			\$('#delete_dialog').modal({backdrop:true, keyboard:true, show:true});
			\$('#delete_dialog').modal('hide');
		});
		</script>"""
		
		def tb = new BoardTagLib()
		tb.modal([
				id: "delete_dialog",
				header:"Delete Board",
				primary: "Delete",
				secondary: "Cancel"
			], {'<p>Do you really want to delete this board?</p>'})
		
		assertEquals expected, tb.out.toString()
	}

}