package app.taskboard

class ColorController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [colorInstanceList: Color.list(params), colorInstanceTotal: Color.count()]
    }

    def create = {
        def colorInstance = new Color()
        colorInstance.properties = params
        return [colorInstance: colorInstance]
    }

    def save = {
        def colorInstance = new Color(params)
        if (colorInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'color.label', default: 'Color'), colorInstance.id])}"
            redirect(action: "show", id: colorInstance.id)
        }
        else {
            render(view: "create", model: [colorInstance: colorInstance])
        }
    }

    def show = {
        def colorInstance = Color.get(params.id)
        if (!colorInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'color.label', default: 'Color'), params.id])}"
            redirect(action: "list")
        }
        else {
            [colorInstance: colorInstance]
        }
    }

    def edit = {
        def colorInstance = Color.get(params.id)
        if (!colorInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'color.label', default: 'Color'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [colorInstance: colorInstance]
        }
    }

    def update = {
        def colorInstance = Color.get(params.id)
        if (colorInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (colorInstance.version > version) {
                    
                    colorInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'color.label', default: 'Color')] as Object[], "Another user has updated this Color while you were editing")
                    render(view: "edit", model: [colorInstance: colorInstance])
                    return
                }
            }
            colorInstance.properties = params
            if (!colorInstance.hasErrors() && colorInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'color.label', default: 'Color'), colorInstance.id])}"
                redirect(action: "show", id: colorInstance.id)
            }
            else {
                render(view: "edit", model: [colorInstance: colorInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'color.label', default: 'Color'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def colorInstance = Color.get(params.id)
        if (colorInstance) {
            try {
                colorInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'color.label', default: 'Color'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'color.label', default: 'Color'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'color.label', default: 'Color'), params.id])}"
            redirect(action: "list")
        }
    }
}
