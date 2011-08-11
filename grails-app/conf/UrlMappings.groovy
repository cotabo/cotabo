class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
		
		"/user/avatar/$username" (controller: "user", action: "avatar") {
			constraints {
				
			}
		}

		"/"(view:"/index")
		"500"(view:'/error')
	}
}
