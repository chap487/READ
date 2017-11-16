Ext.define('webapp.view.Main', {
    extend: 'Ext.container.Container',
    requires:[
        'Ext.tab.Panel',
        'Ext.layout.container.Border',
		'webapp.view.GpsDataGrid', 
		'webapp.view.SessionPersistGrid',
		'Ext.ux.GMapPanel',
		'webapp.view.map',
    ],
    
    xtype: 'Main',
    height: 300,
    width: 1100,
    id: 'mainPanelId',
	itemId: 'mainPanelId',
	border: 5,
	style: {
	    borderColor: 'black',
	    borderStyle: 'solid'
	},
	
    layout: 'vbox',

    items: 
    [{
        xtype: 'container',
        layout: 'hbox',
        region: 'north',
        height: 200,
	    width: 1000,
	    id: 'gpsDataPanelId',
		itemId: 'gpsDataPanelId',
		border: 5,
		style: {
		    borderColor: 'black',
		    borderStyle: 'solid'
		},
		
		items: 
		[{
	        region: 'west',
	        xtype: 'SessionPersistGrid',
	        store: 'SessionPersist',
	        id: 'sessionPersistGridId',
	        itemId: 'sessionPersistGridId'
	    },
	    {
	        region: 'east',
	        xtype: 'GpsDataGrid',
	        //store: 'SessionPersist'
		}]
    },{
        xtype: 'container',
        layout: 'hbox',
        region: 'south',
        height: 600,
	    width: 1000,
	    id: 'southPanelId',
		itemId: 'soutthPanelId',
		border: 5,
		style: {
		    borderColor: 'black',
		    borderStyle: 'solid'
		},
		items: 
		[{
		    xtype: 'gpsmappanel',
		    title: 'Map Pannel',
		    id: 'gpsMapPanelId',
			itemId: 'gpsMapPanelId'
		    /*
		    id: 'gpsMapPanelId',
			itemId: 'gpsMapPanelId',
			width: 150,
	        height: 250,
	        unstyled: true,
	        title: 'Map Pannel',
	        bodyPadding: 0,
	        html: 'Hello'
	        */
		}]
	}]
});