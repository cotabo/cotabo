/**
 * Containg reporting related JavaScript.
 */

/********************************************************
 * Section data-retrieving functions
 ********************************************************/	

/**
 * Gets the data for the Colulative Flow Diagram for a single column 
 * and splits the returning CSV data <timestamp,value>
 * into a series of Arrays so that it can be used directly by Flot
 * to be inserted as a data series.
 *
 * @param columnId - the column ID that you want to get data for
 * @return Array of arrays for each data point
 */
var getCsvCDFDataForColumn = function (columnId) {
	return syncGetOnCSVReturningController(columnId, 'getCdfDataForColumn');
}

/**
 * Get the data for displaying the lead-time and receives the CSV data
 * <timestamp,value> into a series of Arrays so that is can be used directly
 * by jQuery Flot as time-series data.
 * 
 * @param boardId - the board the data should be got for.
 * @return Array of arrays for each data point.
 */
var getLeadTimeData = function(boardId) {
	return syncGetOnCSVReturningController(boardId, 'getLeadTimeData');
}

/**
 * Get the data for displaying a courve of how many tasks are inside
 * the bonderies of the workflow.
 *
 * @param boardId - the board the data should be got for.
 * @return Array of arrays for each data point.
 */
var getWorkflowTaskAmountData = function(boardId) {
	return syncGetOnCSVReturningController(boardId, 'getWorkflowTaskAmountData');
}
/**
 * Helper function for getting CSV data where we need to
 * raise synchronours get requests for.
 * 
 * @param objectId - the id of the object that the action needs (e.g. board id or column id)
 * @param action - the action on the BoardController that should be requested
 * @return an array of arrays (one for each returned data point / CSV line)
 */
var syncGetOnCSVReturningController = function(objectId, action) {
	//Setting asynchronous ajax to false because we want to load the data
	//before executing the diagram-generation code.
	$.ajaxSetup({async:false});
	var resultData;
	$.get('../'+action+'/'+objectId, function(data) {
		resultData = $.csv(',', 0)(data);
	});
	$.ajaxSetup({async:true});
	return resultData;
}