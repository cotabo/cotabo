modules = {
	base {
		dependsOn 'jquery, bootstrap-css, bootstrap-modal, bootstrap-twipsy'
		
		resource url:'css/main.css'
	}
		
	board {
		dependsOn 'base, atmosphere, quicksearch, jquery-ui, utils'
		
		resource url:'js/objectsandevents.js'
		resource url:'js/atmospherehandling.js'
		resource url:'css/board.css'			 
	}
	
	flydom {
		dependsOn 'jquery'
		
		resource url:'js/jquery-plugins/jquery.flydom-3.1.1.js'
		resource url:'js/jquery-plugins/jquery.flydom.extension.js'
	}
	
	flot {
		dependsOn 'jquery'
		
		resource url:'js/jquery-plugins/jquery.flot.min.js'
		resource url:'js/jquery-plugins/jquery.flot.stack.js'
		resource url:'js/jquery-plugins/jquery.csv.js'
	}	
	
	quicksearch {
		dependsOn 'jquery'
		
		resource url:'js/jquery-plugins/jquery.quicksearch.js'
	}
	utils {
		dependsOn 'jquery, jquery-ui'
		resource 'js/utils.js'
	}

	icons {
		resource url:'images/arrow_down_12x12.png', disposition:'inline'
		resource url:'images/arrow_up_12x12.png', disposition:'inline'
	}
	
}