  Ext.define('webapp.store.SessionPersist', {
    extend : 'Ext.data.Store',
    model : 'webapp.model.SessionPersist',
    itemId : 'sessionPersistStore', 
    id : 'sessionPersistStore',
    autoLoad: true    
});