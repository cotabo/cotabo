package org.cotabo

import org.codehaus.groovy.grails.web.pages.exceptions.GroovyPagesException

class BoardTagLib {
	static namespace = "tb"
	
	/**
	 * Renders a twitter bootstrap based DOM modals.
	 * For more information see the <a href="http://twitter.github.com/bootstrap/javascript.html#modal">Twitter Bootstrap documentation</a>
	 * 
	 * The body of this tag will be displayed as part of the dialog body.
	 * 
	 * @attr id - the ID of the dialog - the same should be used e.g. for 'data-controls-modal' attributes on buttons / links
	 * @attr header - [optional] a text to be displayed in the header
	 * @attr primary - [optional] a text for the first button. 
	 *	The link behing is will become the href target of the last clicked element with the data-controls-modal attribute defined.
	 * @attr secondary - [optiponal] a text for a secondary button - a click will just close the dialog.
	 * @arrr sourceSelector - [optional] a jQuery selector for the modal dialog to be triggered on (onClick). Default - all elements that have data-controls-modal defined.
	 * @attr onClick - [optional] a reference to a JavaScript function that should be triggered onClick on the first button.  
	 */
	def modal = { attrs, body ->
		if (!attrs.id) {
			throw new GroovyPagesException('attributes \'id\' is required for tag tb:modal')
		}
		out << """
		<div id="${attrs.id}" class="modal hide fade">"""
		if(attrs.header) {
			out << """
			<div class="modal-header">
			    <a href="#" class="close">&times;</a>
		    	<h3>${attrs.header}</h3>
			</div>"""
		}
		out << """
	    <div class="modal-body">
		"""	   
		out << body()
	    out << """
		</div>"""
		if (attrs.primary || attrs.secondary) {
			out << """
			<div class="modal-footer">
	    	${attrs.primary ? "<a href=\"#\" class=\"btn primary\">${attrs.primary}</a>" : ''}
	  		${attrs.secondary ? "<a href=\"#\" class=\"btn secondary\">${attrs.secondary}</a>" : '' }
		    </div>"""
		}
		out << """
		<script type="text/javascript">
		jQuery(function(){"""
		if(attrs.sourceSelector) {			
			out << """
			\$('${attrs.sourceSelector}').click(function() {"""
		}
		else {
			//for all elements that have the 'data-controls-modal' attribute defined
			out << """
            \$('[data-controls-modal]').click(function() {"""
		}
		//save the href of the selected elements on click
        out << """
            document.modalHelperHref = \$(this).attr('href');
	    });"""
		if (attrs.secondary) {
			//Default behaviour for secnoday button - just close the dialog
			out << """
            \$('#${attrs.id} > .modal-footer > a.secondary').click(function() {
		        \$('#${attrs.id}').modal('hide');
	        });"""
		}		
		//Bind to set the href of the primary button on showing the dialog and reset on close
		out << """
		\$('#${attrs.id}').bind('show', function(e) {"""
		if (attrs.onClick) {
			out << """
			\$('#${attrs.id} > .modal-footer > a.primary').click(${attrs.onClick});"""
		}
		out << """
			\$('#${attrs.id} > .modal-footer > a.primary').attr('href', document.modalHelperHref)		
		});"""			

				
		out << """
			\$('#${attrs.id}').bind('hide', function(e) {
				\$('#${attrs.id} > .modal-footer > a:first').attr('href', '#');		
			});
		});
		</script>"""
	}
	
	/**
	 * Prints a role of org.cotabo.RoleEnum in a user-friendly format
	 * @attr role the RoleEnum value to print
	 */
	def printRole = { attrs ->
		if (!attrs.role)
			return		
		def friendly = attrs.role.toString().toLowerCase()
		friendly = friendly.replaceAll('_', ' ')
		out << friendly
	}
}
