Ext.define('EkaSmartPlt.ekacac.farmerconnect.view.offer.NewOffer', {
    extend: 'Ext.window.Window',
    alias:'widget.ekacacapp-farmerconnect-view-offer-newoffer',
    listeners : {
        boxready : 'onViewPainted',
        createBlankOffer : "createBlankOffer",
        createDuplicateOffer :"createDuplicateOffer",
        exitHandler :"exitHandler"
    },  
    width:1000,
    height:340,
    scrollable:true,
    cls: 'smart-window custom-form-panel offer-window',
	modal : true,
    controller: "ekasmartplt-farmerconnect-controller-offer-newoffercontroller",
    requires: ['EkaSmartPlt.ekacac.farmerconnect.controller.offer.NewOfferController'],
    items:[],
    
    buttons: [{
        xtype: 'button',
        text: GlobalMessageConstants.texts.CANCEL,
        cls: 'button-inverse',
        handler: 'cancelButtonHandler',
        scope: 'controller'
    }, {
        xtype: 'tbfill'
    }, {
        xtype: 'button',	
        text: 'PUBLISH',	
        cls: 'button-secondary',
        handler: 'saveButtonHandler',
        scope: 'controller'
}] 
});