/**
 * Extends the jQuery FlyDOM pluin by the method tplInsertBefore
 */
jQuery.fn.tplInsertBefore = function(json, tpl) {

    // This will allow 'this' to go inside of a .each() loop
    var self = this[0];

    // Make sure that we have an array to work with
    if (json.constructor != Array) { json = [ json ]; };

    // Don't try to do anything if we have nothing to do
    if (json.length == 0) { return false; };

    // Here we create a div and insert it before the element we're
    // prepending to
    var div = document.createElement('div');

    // Loop through our JSON "rows"
    for (var i = 0; i < json.length; i++) {

        // Apply the data to the template and get our results
        var results = tpl.apply(json[i]);

        // Just like with createAppend/createPrepend; this is the best way to
        // loop through and create our new element(s).
        for (var j = 0; j < results.length; j = j + 3) {

            jQuery(div).createAppend(results[j], results[j + 1], results[j + 2]);

        };

    };

    // Apply each child node of the div container starting from the last one
    // This will ensure that all elements get applied as expected, and still
    // be readable from top to bottom.
    for (i = div.childNodes.length - 1; i >= 0; i--) {

        if (jQuery.browser.msie && self.nodeName.toLowerCase() == 'table' && div.childNodes[i].nodeName.toLowerCase() == 'tr') {

            if (self.getElementsByTagName('tbody')[0]) {

                var tbodyElement = self.getElementsByTagName('tbody')[0];
                tbodyElement.insertBefore(div.childNodes[i], tbodyElement.firstChild);

            } else {

                var tbodyElement = self.insertBefore(document.createElement('tbody'), self.firstChild);
                tbodyElement.appendChild(tbodyElement.appendChild(div.childNodes[i]));

            };
        } else {
        	//The only thing that I changed here is the .parentNode
        	//Everything else is copied from jQuery.fn.tplPrepend
            self.parentNode.insertBefore(div.childNodes[i], self);

        };

    };

    // Return ourself for chaining
    return this;

};