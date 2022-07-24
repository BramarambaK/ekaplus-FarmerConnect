Ext.define("EkaSmartPlt.ekacac.farmerconnect.controller.offer.OfferPreviewController", {
	extend: "EkaSmartPlt.platform.controller.AbstractEkaController",
	alias: "controller.ekasmartplt-farmerconnect-controller-offer-offerpreviewcontroller",
	init: function() {
		this.bidJSON={};
		return this.callParent(arguments);
	},
	
	onDelete : function(button){
		let _this=this;
		_this.bidJSON=button.bidJSON;
		let button1={
			text : Localization.GENERAL.CANCEL,						
            action : "popwindowDestroy"
	
		};
		let button2={
			text :  Localization.GENERAL.OK,						
            action: "onDeleteDeal"
		};
		let buttons=[button1,button2];
		let components={
			xtype:'label',
			cls: 'offerpage status-message',
			html: Localization.FARMER_CONNECT.DELETE_OFFER
		
		};
	   	 Ext.create("EkaSmartPlt.farmerconnect.view.offer.FarmerConnectConfirmationWindow",{
			buttonsConfig : buttons,
			components : components,
			parentView : this.getView(),
			width:'70%',
			height:'15%',
		}).show(); 
		
	},
	onDeleteDeal:function(window){
		_this=this;
		let successCallBack=function(){
			EkaLoader.unmask();
			_this.backButtonHandler();
			window.destroy();
		}
		let errorCallBack=function(){
			EkaLoader.unmask();
			EkaToast.show( Localization.FARMER_CONNECT.OFFER_CANCEL_FAILURE);
		}
		EkaLoader.mask(Localization.GENERAL.LOADING_MASK);
		OfferService.updateOffer( 'DELETE',_this.bidJSON.bidId,'',successCallBack,errorCallBack);
	},
	onModify : function(button){
		button.bidJSON['status']="Modify";
		this.createComponentHandler({
			componentType : 'ekasmartplt-farmerconnect-view-offer-newoffer',
			bidJSON : button.bidJSON,

		});
	},
	onBidLogsHandler : function(button){
		this.createComponentHandler({
			componentType : 'ekasmartplt-farmerconnect-view-bid-bidlogsview',
			bidJSON : button.bidJSON
		});
	},
	popwindowDestroy : function(window){
		window.destroy();
	},
	cancelDeal : function(window){
		_this=this;
		let successCallBack=function(){
			EkaLoader.unmask();
			_this.backButtonHandler(); 
			window.destroy();
		}
		let errorCallBack=function(){
			EkaLoader.unmask();
			EkaToast.show( Localization.FARMER_CONNECT.OFFER_CANCEL_FAILURE);

		}
		let json={};
		json['remarks']=Ext.ComponentQuery.query('#remarksReference')[0].getValue();
		EkaLoader.mask(Localization.GENERAL.LOADING_MASK);
		OfferService.cancelOffer( _this.bidJSON.refId,json,successCallBack,errorCallBack);

	},
	onCancelOffer:function(button){		
		var _this=this;
		_this.bidJSON=button.bidJSON;
		let button1={
			text : Localization.FARMER_CONNECT.BACK,						
            action : "popwindowDestroy"
	
		};
		let button2={
			text : Localization.FARMER_CONNECT.CANCEL_DEAL,		
            action: "cancelDeal"
		};
		let buttons=[button1,button2];
		let components={
			xtype:'container',
			items:[{
			xtype:'label',
			cls:"offerpage status-message",
			html: Localization.FARMER_CONNECT.BID_CANCEL_MESSAGE
		},{
			xtype:'label',
			cls:"offerpage status-message",
			html: Localization.FARMER_CONNECT.ENTER_REMARKS
		},{
			xtype : 'textareafield',
			margin : '0 5 20 5',
			height : 120,
			keyUp:function(me){
				me.up().up().items.items[1].items.items[1].setDisabled(false)	
					},
			value : '',
			reference:'remarksReference',
			itemId : 'remarksReference',
			label :  Localization.FARMER_CONNECT.REMARKS,
			labelAlign : 'top'
		}]
	};

	   	 Ext.create("EkaSmartPlt.farmerconnect.view.offer.FarmerConnectConfirmationWindow",{
			buttonsConfig : buttons,
			components : components,
			parentView : this.getView(),
			width:'70%',
		}).show(); 
	},
	onBidRejectHandler : function(button){
		this.createComponentHandler({
			componentType : 'ekasmartplt-farmerconnect-view-bid-bidrejectconfirmationpanel',
			bidJSON : button.bidJSON
		});
	},
	onCounterCheckChange : function(box, newValue, oldValue){
		let acceptButton = this.view.down('#acceptButtonItemId');
		let counterValue = this.view.down('#counterValueItemId');
		if(newValue){
			acceptButton.setText(Localization.FARMER_CONNECT.SEND);
		} else {
			acceptButton.setText(Localization.FARMER_CONNECT.ACCEPT);
		}
		counterValue.setDisabled(!newValue);
	},
	validateCounterValue : function(object){
		let value = object.getValue();
		let checkboxValue = this.getView().down('#counterCheckboxItemId').getChecked();
		if(value){
			if(value < object.getMinValue()){
				EkaToast.show(Localization.FARMER_CONNECT.COUNTER_MORE_REQUIRED + object.getMinValue());
				return false;
			}
			return true;
		}
		else if(checkboxValue){
			EkaToast.show(Localization.FARMER_CONNECT.PROVIDE_COUNTER_VALUE);
			return false;
		}
		return true;
	},
	acceptButtonHandler : function(){
		let me = this;
		let requestJSON = this.getView().bidJSON;
		let counterValue = this.view.down('#counterValueItemId');
		
		if(!this.validateCounterValue(counterValue))
			return;

		Object.assign(requestJSON, {		
			remarks : '',
			status : 'Accepted'
		});
		let isChecked = this.view.down('#counterCheckboxItemId').getChecked();
		if(isChecked){
			let counterItem = this.view.down('#counterValueItemId');
			if(!counterItem.isValid()){
				EkaToast.show(Localization.FARMER_CONNECT.PROVIDE_COUNTER_VALUE);
				return;
			}
			let counterValue = counterItem.getValue();
			requestJSON['price'] = counterValue;
			requestJSON['status'] = 'In-Progress';
		}
		let remark = this.view.down('#remarksItemId').getValue();
		if(remark)
			requestJSON['remarks'] = remark.trim();
		EkaLoader.mask(Localization.GENERAL.LOADING_MASK);
	
		OfferService.updateBid(requestJSON.refId, requestJSON, function(responseJSON){
			EkaLoader.unmask();
			// show toast
			EkaToast.show(Localization.FARMER_CONNECT.BID_UPDATE_SUCCESS);
			me.backButtonHandler();
		}, function(error){
			EkaLoader.unmask();
			// handle error
			EkaToast.show(Localization.FARMER_CONNECT.BID_UPDATE_FAILURE);
		});
	},
	
});