/**
 * @example Associations and Validations
 *
 * This example demonstrates associations and validations on a {@link Ext.data.Model}.
 * See console for output.
 */



Ext.define('SessionPersist', 
{
    extend: 'Ext.data.Model',
    requires : ['Ext.data.proxy.Rest'],
/*
    proxy : {
        type : 'ajax',
        url : 'data/sp.json'
    },
*/
    proxy: {
	    type: 'rest',
	    url: 'http://localhost:8080/read-webservice/rest/SessionPersist/',
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
	,hasMany: {model: 'GpsData', name: 'gpsDatas',  associationKey: 'gpsDataList'}
});


Ext.define('GpsData', 
{
    extend: 'Ext.data.Model',
    requires : ['Ext.data.proxy.Rest'],
	    proxy : {
	        type : 'ajax',
	        url : 'data/gps.json',
	        root : 'data'
	    },
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
	    idProperty : 'gpsDataId'
});




Ext.require('Ext.data.Store');
Ext.onReady(function() {

    console.log('onReady() started'); 

   SessionPersist.load(27421, {
        success: function(sessionPersist) {
            console.log("SessionPersist success. sessionName: " + sessionPersist.get('sessionName'));

	    	sessionPersist.gpsDatas().each(function(gpsData) {
                console.log("Latitude: " + gpsData.get('latitude') + " longitude: " + gpsData.get('longitude'));
	    	})

        },
        
                
        failure: function(record, operation) {
	    	console.log('SessionPersist failure in load method'); 
	    },

	    callback: function(record, operation, success) {
	    	console.log('SessionPersist callback method'); 
	    }
        
    });

   GpsData.load(27421, {
        success: function(gpsData) {
            console.log("GpsData success. sessionName: ");

//	    	sessionPersist.gpsDatas().each(function(gpsData) {
//                console.log("Latitude: " + gpsData.get('latitude') + " longitude: " + gpsData.get('longitude'));
//	    	})

        },
        
                
        failure: function(record, operation) {
	    	console.log('GpsData failure in load method'); 
	    },

	    callback: function(record, operation, success) {
	    	console.log('GpsData callback method'); 
	    }
        
    });

    var store = Ext.create('Ext.data.Store', {
        model: 'SessionPersist'
    });



	store.load({
	    scope: this,
	    callback: function(records, operation, success) {
	        // the operation object
	        // contains all of the details of the load operation
	        
   	        debugger;
	        var record = records[0];
	        var name = record.get('sessionName');
	        var gpslist = record.gpsDatas();
	        
	        gpslist.each(function(gpsData) {
                console.log("Latitude: " + gpsData.get('latitude') + " longitude: " + gpsData.get('longitude'));
	    	})
	        
	        
	        //var store = Ext.ComponentManager.get('webapp.store.GpsData');
	        //store.loadData(gpslist);
	        
	        console.log(records);
	    }
	});



});
