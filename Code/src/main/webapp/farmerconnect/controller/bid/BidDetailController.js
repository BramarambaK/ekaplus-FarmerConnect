Ext.define("EkaSmartPlt.ekacac.farmerconnect.controller.bid.BidDetailController", {
	extend: "EkaSmartPlt.platform.controller.AbstractEkaController",
	alias: "controller.ekasmartplt-farmerconnect-controller-bid-biddetailcontroller",
	init: function() {
		return this.callParent(arguments);
	},
	
	onRateNowClick : function(bidJSON){
		this.getView().hide();
		this.addComponentToContentPanelHandler({
			componentType : 'ekasmartplt-farmerconnect-view-bid-bidratingview',
			bidJSON : bidJSON,
			parentComponent : this.getView()
		});
	},
	
	onBidLogsHandler : function(button){
		this.createComponentHandler({
			componentType : 'ekasmartplt-farmerconnect-view-bid-bidlogsview',
			bidJSON : button.bidJSON
		});
	},
	backButtonHandler : function(){
		var view = this.getView();
		if(view.componentData && view.componentData.isNotificationDetails){
			var contentPanel =  Ext.ComponentQuery.query('#contentPanelItemId')[0];
			var items = contentPanel.getItems().items;
			if(items.length >0 ){
				items[items.length-2].show();
			}
			this.getView().destroy();   
		}
		else{
			this.superclass.backButtonHandler();
		}
    },
});