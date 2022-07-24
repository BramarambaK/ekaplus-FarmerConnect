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
		BidService.updateBid(bidJSON.refId, bidJSON, function(responseJSON){
			EkaLoader.unmask();
			EkaToast.show(Localization.FARMER_CONNECT.BID_REJECT_SUCCESS);
			_this.backButtonHandler();
		}, function(error){
            EkaLoader.unmask();
            if(error.msg)
                EkaToast.show(Ext.decode(error.msg).msg);
            else
                EkaToast.show(Localization.FARMER_CONNECT.BID_REJECT_FAILURE);
                _this.backButtonHandler();
		});
    },
    validateRejectionReason : function(object){
        var applyButton = this.getView().down('#applyButtonItemId');
        if(object.getValue() && object.getValue().length > 0){
            applyButton.setDisabled(false);
        }
        else    
            applyButton.setDisabled(true);
    },
    backButtonHandler : function(setDetails){
        var prevComponent = this.getView().componentData.prevComponent;   
        this.getView().destroy();     
        prevComponent.show();
    },
});