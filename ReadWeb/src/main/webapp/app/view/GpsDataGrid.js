Ext.define('webapp.view.GpsDataGrid', {
    extend : 'Ext.grid.Panel',  // tried Ext.grid.GridPanel, but no scroll bar
    xtype: 'GpsDataGrid',
    requires:[
    	'Ext.grid.column.Date',
    	'Ext.ux.GMapPanel',
    	'webapp.store.GpsData'
    ],
    id: 'gpsDataPointsPanelId',
    itemId: 'gpsDataPointsPanelId',
    title: 'GPS Data' ,
    region: 'east',
    width: 700,
    height: 200,
    //layout:'fit',
    //autoScroll:true,
    columns: [{
        text: 'ID',
        dataIndex: 'GpsDataId',
        hidden: true
    }, {
        text: 'Date/Time',
        xtype : 'datecolumn',
        dataIndex: 'dateTime',
        format: 'h:i:s.u'
    }, {
        text: 'Latitude',
        dataIndex: 'latitude'
    }, {
        text: 'Longitude',
        dataIndex: 'longitude'
    }, {
        text: 'Altitude',
        dataIndex: 'altitude'
    }, {
        text: 'Leg Speed',
        dataIndex: 'legSpeed'
    }, {
        text: 'Leg Heading',
        dataIndex: 'legHeading'
    }],
    
   	listeners: {
   		selectionchange: function (view, selections, options) {
   			GpsSelectionchange(view, selections, options);
   		}
   	}
});

function GpsSelectionchange (view, selections, options) {
	// when gps row clicked, add point to map
	var record = selections[0];
	var lat = record.get('latitude');
	var long = record.get('longitude');
	var locDesc = "lat: " + lat  + " long: " + long;
	
	gpsMapPanel = Ext.getCmp('gpsMapPanelId');
    gpsMapPanel.addMarker(lat,long);
}
