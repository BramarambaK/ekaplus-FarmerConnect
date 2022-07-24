Ext.define("EkaSmartPlt.ekacac.farmerconnect.controller.bid.BidEditController", {
	extend: "EkaSmartPlt.platform.controller.AbstractEkaController",
	alias:"controller.ekasmartplt-farmerconnect-controller-bid-bideditcontroller",
	init: function() {
		return this.callParent(arguments);
	},
	
	acceptButtonHandler : function(button){
		var me = this;
		// Validations
		var requestJSON = this.getView().bidJSON;
		var quantityItem = this.view.down('#quantityItemId');
		var shipmentItem = this.view.down('#shipmentDateItemId');
		var counterValue = this.view.down('#counterValueItemId');
		if(requestJSON.quantity) // validate only if quantity is mapped with template
			if(!this.validateQuantity(quantityItem)){
				return;
			}
		if(!this.validateCounterValue(counterValue))
			return;
		if((shipmentItem.getValue() < shipmentItem.getMinDate())){
			EkaToast.show(Localization.FARMER_CONNECT.PROVIDE_SHIPMENT_DATE);
			return;
		}
		
		// not needed for saving bid; extra params
		this.cleanBidJSON(requestJSON);

		Object.assign(requestJSON, {
			quantity : quantityItem.getValue().toString(),
			shipmentDateInMillis : shipmentItem.getValue().getTime(),
			remarks : '',
			status : 'Accepted'
		});
		var isChecked = this.view.down('#counterCheckboxItemId').getChecked();
		if(isChecked){
			var counterItem = this.view.down('#counterValueItemId');
			if(!counterItem.isValid()){
				EkaToast.show(Localization.FARMER_CONNECT.PROVIDE_COUNTER_VALUE);
				return;
			}
			var counterValue = counterItem.getValue();
			requestJSON['price'] = counterValue;
			requestJSON['status'] = 'In-Progress';
		}
		var remark = this.view.down('#remarksItemId').getValue();
		if(remark)
			requestJSON['remarks'] = remark.trim();
		EkaLoader.mask(Localization.GENERAL.LOADING_MASK);
		// updating the active sub tab to redirect to that page in case it comes from bid screen
		if(FarmerConnectConfigurationObject.activePage == 'bids'){
				FarmerConnectConfigurationObject.activeTabBisScreen = requestJSON.status;
		}
		if(!requestJSON.refId){
			BidService.createBid(requestJSON, function(responseJSON){
				EkaLoader.unmask();
				// show toast
				EkaToast.show(Localization.FARMER_CONNECT.BID_CRATE_SUCCESS);
				me.backButtonHandler();
			}, function(error){
				EkaLoader.unmask();
				// handle error
				EkaToast.show(Localization.FARMER_CONNECT.BID_CREATE_FAILURE);
			});
			 eatracker.trackEvent(EkaGoogleAnayticsUtil.pageTrackingMap.FARMER_CONNECT, EkaGoogleAnayticsUtil.constants.InitiateBid);		} else {
			BidService.updateBid(requestJSON.refId, requestJSON, function(responseJSON){
				EkaLoader.unmask();
				// show toast
				EkaToast.show(Localization.FARMER_CONNECT.BID_UPDATE_SUCCESS);
				me.backButtonHandler();
			}, function(error){
				EkaLoader.unmask();
				// handle error
				EkaToast.show(Localization.FARMER_CONNECT.BID_UPDATE_FAILURE);
			});
			 eatracker.trackEvent(EkaGoogleAnayticsUtil.pageTrackingMap.FARMER_CONNECT, EkaGoogleAnayticsUtil.constants.COUNTER_BID);		}
	},
	
	onCounterCheckChange : function(box, newValue, oldValue){
		var acceptButton = this.view.down('#acceptButtonItemId');
		var counterValue = this.view.down('#counterValueItemId');
		if(newValue){
			acceptButton.setText(Localization.FARMER_CONNECT.SEND);
		} else {
			acceptButton.setText(Localization.FARMER_CONNECT.ACCEPT);
		}
		counterValue.setDisabled(!newValue);
	},
	cleanBidJSON : function(requestJSON){
		delete requestJSON.expiresIn;
		delete requestJSON.shipmentDate;
		delete requestJSON.offerorName;
		delete requestJSON.rating;
		delete requestJSON.offerorMobileNo;
	},
	onBidLogsHandler : function(button){
		this.createComponentHandler({
			componentType : 'ekasmartplt-farmerconnect-view-bid-bidlogsview',
			bidJSON : button.bidJSON
		});
	},
	onBidRejectHandler : function(button){
		// this.getView().hide();
        // this.addComponentToContentPanelHandler({
		// 	componentType : 'ekasmartplt-farmerconnect-view-bid-bidrejectconfirmationpanel',
		// 	bidJSON : button.bidJSON,
		// 	prevComponent : this.getView()
		// });
		this.createComponentHandler({
			componentType : 'ekasmartplt-farmerconnect-view-bid-bidrejectconfirmationpanel',
			bidJSON : button.bidJSON
		});
	},
	validateQuantity : function(object){
		var value = object.getValue();
		if(value){
			if(value < object.getMinValue()){
				EkaToast.show( Localization.FARMER_CONNECT.QUANTITY_MORE_REQUIRED + object.getMinValue());
				return false;
			}
			if(value > object.getMaxValue()){
				EkaToast.show( Localization.FARMER_CONNECT.QUANTITY_LESS_REQUIRED + object.getMaxValue());
				return false;
			}
			return true;
		}
		else{
			EkaToast.show(Localization.FARMER_CONNECT.PROVIDE_QUANTITY);
			return false;
		}
	},

	validateCounterValue : function(object){
		var value = object.getValue();
		var checkboxValue = this.getView().down('#counterCheckboxItemId').getChecked();
		if(value){
			if(value < object.getMinValue()){
				EkaToast.show(Localization.FARMER_CONNECT.COUNTER_MORE_REQUIRED + object.getMinValue());
				return false;
			}
			// if(value > object.getMaxValue()){
			// 	EkaToast.show('Counter value should be less than ' + object.getMaxValue());
			// 	return false;
			// }
			return true;
		}
		else if(checkboxValue){
			EkaToast.show(Localization.FARMER_CONNECT.PROVIDE_COUNTER_VALUE);
			return false;
		}
		return true;
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
    }
});