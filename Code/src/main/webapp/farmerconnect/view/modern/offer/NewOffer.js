Ext.define('EkaSmartPlt.farmerconnect.view.offer.NewOffer', {
    extend: 'Ext.Container',
    xtype: 'ekasmartplt-farmerconnect-view-offer-newoffer',
    itemId:'farmerConnectNewOfferItemId',
    listeners : {
        painted : 'onViewPainted',
        createBlankOffer : "createBlankOffer",
        createDuplicateOffer :"createDuplicateOffer",
        exitHandler :"exitHandler"
    },

    controller: 'ekasmartplt-farmerconnect-controller-offer-newoffercontroller',

    requires: ['EkaSmartPlt.ekacac.farmerconnect.controller.offer.NewOfferController'],

    initialize: function() {
        this.addTitleBar();
        this.callParent(arguments);
    },
    cls:'form-container',

    addTitleBar: function() {
        this.add({
            xtype: 'titlebar',
            cls: 'secondary-titlebar',
            docked: 'top',
            style: 'overflow: visible;',
            height: 50,
            items: [{
                xtype: 'button',
                handler: 'backButtonHandler',
                scope: 'controller',
                width: 30,
                height: 40,
                cls: 'button-image',
                iconCls: 'icon-close'
            }, {
                xtype: 'label',
                reference:'newOfferLabel',
                html: Localization.FARMER_CONNECT.NEW_OFFER,
                cls: 'card-title'
            }]
        });
    },
    
  
})