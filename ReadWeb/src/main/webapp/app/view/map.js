/**
 * A wrapper around a Google map. Use centerMap(latitude, longitude) to center
 * the map at the specified location.
 */
Ext.define('webapp.view.map', {
    extend : 'Ext.panel.Panel', //'Ext.Component',
    renderTo: Ext.getBody(),
    xtype : 'gpsmappanel',
    height: 500,
	width: 1000,
    config : {
        store : null
    },
    
    padding : 8,
    
    html : '<p>Google Map Area</p>',

    initComponent : function() {
    	this.markers = [];
    	this.callParent();
    	
    	// 41.8894° N, 87.5906° W Chicago Lighthouse
    	this.centerMap(41.8894, -87.5906);
    },

    renderMap : function() {
  		//debugger;
   		//var image = "BlackNYelloCircle.png"; //'http://google.com/mapfiles/kml/paddle/pause.png';

        // Assert : centerMap() has been run, and therefore, 
        // this.latitude and this.longitude are set.
        var cfg = {
            zoom : this.zoom,
            center : new google.maps.LatLng(this.latitude, this.longitude),
            mapTypeId : google.maps.MapTypeId.ROADMAP
        };
        this.map = new google.maps.Map(this.body.dom, cfg); //(this.getEl().dom, cfg);
        this.renderMarkers();
    },

    centerMap : function(latitude, longitude) {
  		//debugger;
        // Save the latitude/longitude
        this.latitude = latitude;
        this.longitude = longitude;

        // If we're visible, render the map right away. Else
        // wait until someone clicks on the tab.
        if (this.isVisible()) {
            this.renderMap();
        } else {
          	//debugger;
            this.on('show', this.renderMap, this, {
                single : true
            });
        }
    },
    
    saveCenterAndZoomMap : function(latitude, longitude, zoom) {
  		//debugger;
        // Save the latitude/longitude
        this.latitude = latitude;
        this.longitude = longitude;
        this.zoom = zoom;
    },
    
    highlight : function(record){
        if (this.selection === record){
            return;
        }
        this.selection = record;
        var markers = this.getMarkers();
        for (var i = 0 ; i < markers.length ; i++){
            var marker = markers[i];
            if (marker.record === record){
                marker.setIcon('resources/images/YellowStar.png');
            } else {
                marker.setIcon();
            }
        }
    },

    addMarker : function(lat,long) {
    	//debugger;
    	
        var ll = new google.maps.LatLng(lat,long);
        var marker = new google.maps.Marker({
            position : ll,
            title : 'new point',
            // animation: google.maps.Animation.DROP,
            listeners: {
                click: function(e){
                    Ext.Msg.alert('It\'s fine', 'and it\'s art.');
                }
            }
        });

		//marker.setIcon('resources/images/BlackNYelloCircle.png');
        var me = this;
        marker.setMap(me.map);
        this.markers.push(marker);
	}, 
	
    getMarkers : function() {
        return (this.markers || []);
    },

   	removeMarkers : function() { //businesses) {
    	//debugger;
    	
    	var tmarkers = this.getMarkers();
        var tme = this;
    	var tmap = tme.map;
    	
        // Hide the previously saved markers
        var markers = this.getMarkers();
        Ext.Array.forEach(markers, function(marker) {
            marker.setMap(null);
        });
	},


    setMarkers : function() { //businesses) {
    	//debugger;
        // Hide the previously saved markers
        var markers = this.getMarkers();
        Ext.Array.forEach(markers, function(marker) {
            marker.setMap(null);
        });

/*
        this.markers = [];

        var me = this;
        var markers = this.getMarkers();
        Ext.Array.forEach(markers, function(marker) {
	        me.markers.push(marker);
            //marker.setMap(null);
        });
*/

/*
        // For each business, push a new marker onto the array
        var me = this;
        var ll = new google.maps.LatLng(41.8,-87.6); //(r.data.latitude, r.data.longitude);
        var marker = new google.maps.Marker({
            position : ll,
            title: 'test'
        });
        me.markers.push(marker);
*/
        // If we're visible, render the markers right away. Else
        // wait until someone clicks on the tab.
        if (this.isVisible()) {
            this.renderMarkers();
        } else {
            this.on('show', this.renderMarkers, this, {
                single : true
            });
        }

    },

    // @private
    renderMarkers : function() {
        //debugger;
        // This method must always be run after setMarkers()
        // Assert: this.map is set.
        var me = this;
        var t = this.getMarkers();

        Ext.Array.forEach(this.getMarkers(), function(marker) {
            marker.setMap(me.map);
        });
    }

});