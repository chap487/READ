Ext.define('webapp.controller.Main', {
    extend: 'Ext.app.Controller',
    
    views: [
    	'Main',
    	'GpsDataGrid',
    	'SessionPersistGrid'
    ],
	stores: [
		'SessionPersist',
		'GpsData'
	],
	models: [
		'SessionPersist',
		'GpsData'
	],
	
	init : function()
    {
    
       this.control({

            '#sessionPersistGridId':
            {
                selectionchange: this.loadGpsDataAfterRowSelect
            }
        }),
    
    /*
        this.getSessionPersistStore().addListener('load', function(store, records, success)
        {
        	debugger;
            var record = records[0];
            var name = record.get('sessionName');
	        //var gpslist = record.gpsDatas();
	        
            console.log('In webapp.controller.Main and record session count: ' + records.length + ' session persist count: ' );
            
            
	        gpslist.each(function(gpsData) {
                console.log("Latitude: " + gpsData.get('latitude') + " longitude: " + gpsData.get('longitude'));
	    	})
            
        }, 
        this);
	*/
        this.getGpsDataStore().addListener('load', function(store, records, success)
        {
            var record = records[0];
            console.log('In webapp.controller.Main load listener function');
            //var grid = Ext.ComponentManager.get('Main');
            //grid.render(); 
        }, 
        this);
    },
    
       loadGpsDataAfterRowSelect: function (record, selected, eOpts) {
		    //get a reference to the User model class
			var gpsDataModel = this.getSessionPersistModel();//getGpsDataModel(); //Ext.getCmp('gpsDataStore');
			var sessionPersistId = selected[0].data.sessionPersistId;
 
			gpsDataModel.load(sessionPersistId, {
			    success: function(gpslist2, operationT) { 
        			//debugger;
			        var gpslist = gpslist2.gpsDatas();
		
			        var panel = Ext.getCmp('gpsDataPanelId');
			        var oldGpsGridPanel = Ext.getCmp('gpsDataPointsPanelId');
			        panel.remove(oldGpsGridPanel);
		
			        var grid = Ext.create('webapp.view.GpsDataGrid');
			        var store = grid.getStore();  
		            store.loadData(gpslist.data.items,false);
		
			        panel.add(grid);
			        panel.doLayout();
			        
			        //debugger;
			        panel = Ext.getCmp('southPanelId');
			        gpsMapPanel = Ext.getCmp('gpsMapPanelId');
			        
			        var totalLat = 0;
			        var totalLong = 0;
			        var totalPoints = 0;
		    		gpslist.each(function(gpsData) {
		    			totalPoints++;
		    		   	totalLat += gpsData.get('latitude');
						totalLong += gpsData.get('longitude');
					})
					totalLat /= totalPoints;
					totalLong /= totalPoints;	        
			        gpsMapPanel.saveCenterAndZoomMap(totalLat,totalLong,13);
		
			        gpsMapPanel.renderMap();
		    		gpsMapPanel.removeMarkers();
		    		// Add new markers
		    		gpslist.each(function(gpsData) {
		    		   	var lat = gpsData.get('latitude');
						var long = gpsData.get('longitude');
					    gpsMapPanel.addMarker(lat,long);
						//var locDesc = "lat: " + lat  + " long: " + long;
						// console.log("Latitude: " + gpsData.get('latitude') + " longitude: " + gpsData.get('longitude'));
			    	})
				    
				    gpsMapPanel.addMarker(totalLat,totalLong);
				    
				    
				    
		
			    },
			    failure: function(record, operation) {
			    	debugger;
			    	console.log('SessionPersist failure in load method'); 
			    },
			});
        },
        
        displayGpsDataAfterLoad : function() {
    		debugger;
    		var gpsDataStore = this.getGpsDataStore()
        }
    
});
