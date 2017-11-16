Ext.define('webapp.view.SessionPersistGrid', {
    extend : 'Ext.grid.Panel',
    xtype: 'SessionPersistGrid',
    requires:['Ext.grid.column.Date',
    'webapp.store.GpsData'],
    title: 'GPS Sessions' ,
    width: 300,
    height: 200,
    id: 'sessionPersistGridId',
	itemId: 'sessionPersistGridId',
 
    columns: 
    [{
        text: 'ID',
        dataIndex: 'sessionPersistId',
        hidden: true
    }, {
        text: 'Session Date',
        xtype : 'datecolumn',
        dataIndex: 'endDateTime',
        format: 'Y-m-d'
        //dataIndex: 'sessionName'
    }, {
        text: 'Start Time',
        xtype : 'datecolumn',
        dataIndex: 'startDateTime',
        format: 'H:i:s' //'Y-m-d H:i:s'
    }, {
        text: 'End Time',
        xtype : 'datecolumn',
        dataIndex: 'endDateTime',
        format: 'H:i:s'
    }]
});