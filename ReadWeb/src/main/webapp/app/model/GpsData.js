Ext.define('webapp.model.GpsData', 
{
    extend: 'Ext.data.Model',
    requires : ['Ext.data.proxy.Rest'],
    
        proxy: {
		    type: 'rest',
		    url: 'http://localhost:8080/read-webservice/rest/GpsData',
            //url: 'http://24.13.194.77:5291/read-webservices/rest/GpsData',
            //url: 'http://35.162.237.212:8080/read-webservices/rest/GpsData',
		    reader: {
		        type: 'json',
		        root: 'data'
		    },
		    writer: {
		        type: 'json'
		    }
		},
	    /*
	    proxy : {
	        type : 'ajax',
	        url : 'data/gps.json',
	        root : 'data'
	    },
		*/
	    fields: [
			{name: 'gpsDataId',type: 'int',useNull: true},  
			{name: 'dateTime', type: 'date'},  // try -  dateFormat : 'Y/m/d'
			{name: 'latitude', type: 'float'},
			{name: 'longitude', type: 'float'},
			{name: 'altitude', type: 'float'},
			{name: 'legSpeed', type: 'float'},
			{name: 'legHeading', type: 'float'}
		],
        belongsTo: [{name: 'sessionPersist', model: 'SessionPersist',  associationKey: 'SessionPersist' }],
	    idProperty : 'gpsDataId',
	    id: 'gpsDataId',
		itemId: 'gpsDataId'
	    
});


