Ext.define("EkaSmartPlt.ekacac.farmerconnect.controller.bid.BidRejectConfirmationController", {
	extend: "EkaSmartPlt.platform.controller.AbstractEkaController",
	alias: "controller.ekasmartplt-farmerconnect-controller-bid-bidrejectconfirmationcontroller",
	init: function() {
		return this.callParent(arguments);
	},
    applyHandler : function(){
        _this = this;
        var bidJSON = this.getView().componentData.bidJSON;
        bidJSON.status = 'Rejected';
        if( FarmerConnectConfigurationObject.role!="Offerer"){
            BidService.updateBid(bidJSON.refId, bidJSON, function(responseJSON){
                EkaLoader.unmask();
                EkaToast.show(Localization.FARMER_CONNECT.BID_REJECT_SUCCESS);
             _this.createComponentHandler({
                componentType : 'ekasmartplt-farmerconnect-view-bid-farmerconnecthome',
            });
            }, function(error){
                EkaLoader.unmask();
                if(error.msg)
                    EkaToast.show(Ext.decode(error.msg).msg);
                else
                    EkaToast.show(Localization.FARMER_CONNECT.BID_REJECT_FAILURE);
                    _this.backButtonHandler();
            });
        }
        else{
            OfferService.updateBid(bidJSON.refId, bidJSON, function(responseJSON){
                EkaLoader.unmask();
                EkaToast.show(Localization.FARMER_CONNECT.BID_REJECT_SUCCESS);
                HistoryTracker.pop();
                var contentPanel = Ext.ComponentQuery.query('#contentPanelItemId')[0];
                contentPanel.controller.refereshContentPanel(null, HistoryTracker.pop(), Ext.emptyFn);
            }, function(error){
                EkaLoader.unmask();
                if(error.msg)
                    EkaToast.show(Ext.decode(error.msg).msg);
                else
                    EkaToast.show(Localization.FARMER_CONNECT.BID_REJECT_FAILURE);
                    HistoryTracker.pop();
                    var contentPanel = Ext.ComponentQuery.query('#contentPanelItemId')[0];
                    contentPanel.controller.refereshContentPanel(null, HistoryTracker.pop(), Ext.emptyFn);
            });
        }
    },
    validateRejectionReason : function(object){
        var applyButton = this.getView().down('#applyButtonItemId');
        if(object.getValue() && object.getValue().length > 0){
            applyButton.setDisabled(false);
        }
        else    
            applyButton.setDisabled(true);
    }
});