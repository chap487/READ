Ext.define('webapp.model.SessionPersist', 
{
    extend: 'Ext.data.Model',
    requires : [
    	'Ext.data.proxy.Rest',
    	'webapp.model.GpsData'
    ],
/*
    proxy : {
        type : 'ajax',
        url : 'data/sp.json'
    },
*/
    proxy: {
	    type: 'rest',
	    url: 'http://localhost:8080/read-webservice/rest/SessionPersist/',
        //url: 'http://24.13.194.77:5291/read-webservices/rest/SessionPersist/',
        //url: 'http://35.162.237.212:8080/read-webservices/rest/SessionPersist/',
	    reader: {
	        type: 'json',
	        root: 'data'
	    },
	    writer: {
	        type: 'json'
	    }
	},

    fields: [
		{name: 'sessionPersistId',type: 'int',useNull: true},  
		{name: 'dateTime', type: 'date'},  // try -  dateFormat : 'Y/m/d'
		{name: 'startDateTime', type: 'date'},
		{name: 'endDateTime', type: 'date'},
		{name: 'sessionName', type: 'string', defaultValue: 'New Session Desc'},
		{name: 'sessionDescription', type: 'string', defaultValue: 'New Session Desc'}
	]
	,idProperty : 'sessionPersistId'
	,hasMany: {model: 'webapp.model.GpsData', name: 'gpsDatas',  associationKey: 'gpsDataList'}
	,id: 'sessionPersistId'
	,itemId: 'sessionPersistId'
});


