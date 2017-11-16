/*
    This file is generated and updated by Sencha Cmd. You can edit this file as
    needed for your application, but these edits will have to be merged by
    Sencha Cmd when upgrading.
*/

// DO NOT DELETE - this directive is required for Sencha Cmd packages to work.
//@require @packageOverrides

Ext.application({
    name: 'webapp',

    extend: 'webapp.Application',
    
    launch : function() {
		console.log('READ: ' + 'Ext.application#launch()');
        // Start-up code can go here.
    },
    
    controllers: ['Main'],

     // globals:{ },
    
    autoCreateViewport: true
});
