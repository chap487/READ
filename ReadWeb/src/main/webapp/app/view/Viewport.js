Ext.define('webapp.view.Viewport', {
    extend: 'Ext.container.Viewport',
    requires:[
        'Ext.layout.container.Fit',
        'webapp.view.Main',
        'webapp.view.SessionPersistGrid',
        'webapp.view.GpsDataGrid'
    ],

    layout: {
        type: 'fit'
    },

    items: [{
        //xtype: 'app-main'
        xtype: 'Main'
    }]

});
