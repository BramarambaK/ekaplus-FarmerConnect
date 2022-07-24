Ext.define('EkaSmartPlt.farmerconnect.view.bid.BidRejectConfirmationPanel', {
	extend: 'Ext.Panel',
	alias : 'widget.ekasmartplt-farmerconnect-view-bid-bidrejectconfirmationpanel',
	controller : 'ekasmartplt-farmerconnect-controller-bid-bidrejectconfirmationcontroller',
	requires : ['EkaSmartPlt.ekacac.farmerconnect.controller.bid.BidRejectConfirmationController'],
	layout : 'vbox',
	cls:'setup-screen background-white bid-rejection-screen tbar-normal tbar-white',
	style:'padding:10px',
	initialize : function(){
		var bidJSON = this.componentData.bidJSON;	
		var items = [{			
			xtype : 'label',
			cls:'bid-rejection-heading',
			html  : Localization.FARMER_CONNECT.REJECT_CONFIRMATION
		},{			
			xtype : 'label',
			style:'text-align:center;margin-top:20px',
			cls:'bid-reject-message',
			html  : localStorage.Offerer!="true"?Localization.FARMER_CONNECT.LATEST_OFFEROR_PRICE:Localization.FARMER_CONNECT.LATEST_BIDDER_PRICE
		},{			
			xtype : 'label',
			cls:'bid-rejected-price',
			style:'margin-top:10px',
			html  : localStorage.Offerer!="true"?bidJSON.latestOfferorPrice + " " + bidJSON.priceUnit:bidJSON.latestBidderPrice + " " + bidJSON.priceUnit
		},{			
			xtype : 'label',
			style:'margin-top:20px',
			html  : Localization.FARMER_CONNECT.BID_REJECT_MESSAGE
		},{			
			xtype : 'label',
			style:'margin-top:20px',
			html  : Localization.FARMER_CONNECT.BID_REJECT_REASON_MESSAGE
		},{			
            xtype : 'textareafield',
			style:'padding:10px',
            itemId : 'rejectionReasonItemId',
            listeners : {
                focusleave : 'validateRejectionReason',
                scope :  'controller'
            }
        }];
        this.add(items);
        this.add({
            xtype: 'toolbar',
            cls:'footer-toolbar',
            height:54,
            docked: 'bottom',
            items: [{
                xtype : 'button',
                itemId : 'cancelButtonItemId',
                text: Localization.GENERAL.CANCEL,
                handler : 'backButtonHandler',
				flex : 1,
                cls:'button-transparent-background button-color-blue',
                scope : 'controller'
            },		
            {
                xtype : 'button',
                itemId : 'applyButtonItemId',
                text: Localization.GENERAL.REJECT,
                disabled : true,
                handler :  'applyHandler',
				flex : 1,
                cls:'button-blue',
                scope : 'controller'
            }]
        });
		this.callParent(arguments);
	}
});