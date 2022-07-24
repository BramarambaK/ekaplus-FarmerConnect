Ext.define('EkaSmartPlt.ekacac.farmerconnect.view.bid.BidConfirmationWindow', {
    extend: 'Ext.window.Window',
	cls: 'smart-window custom-form-panel',
	modal : true,
	width : 400,
	height : 400,
	scrollable : true,
	defaultListenerScope : true,
    initComponent: function() {
		var buttonText = 'ACCEPT';
		var textAreaLabel = 'Reason for Acceptance (Optional)';
		if(this.confirmationType == 'REJECT'){
			buttonText = 'REJECT DEAL';
			textAreaLabel = 'Reason for Rejection (Mandatory)';
		}
		if(this.confirmationType == 'CANCEL'){
			buttonText = 'CANCEL DEAL';
			textAreaLabel = 'Reason for Cancellation (Mandatory)';
		}
		var store = new Ext.data.Store();
		store.loadRecords(this.records);
		Ext.apply(this, {
			title : this.confirmationType == 'CANCEL'?'Cancellation':'Confirmation',
			layout : 'vbox',
			items : [{
				xtype : 'label',
				style : 'word-wrap: break-word;margin:10px;',
				html : this.getActionMainLabel()
			},{
				xtype : 'grid',
				cls:'custom-grid-plane grid-panel-sub-page grid-without-border',
				width : '100%',
				style : 'margin:10px 5px 5px 5px;',
				itemId : 'finalPriceGridItemId',
				headerBorders : false,
				disableSelection:true,
				columns: [{
					dataIndex : 'refId',
					text : 'Ref Id',
					flex : 1
				},{
					dataIndex : 'bidId',
					text : 'Bid Id',
					flex : 1
				},{
					dataIndex : 'finalBidPrice',
					text : 'Final Bid Price',
					flex : 1,
					renderer : function(grid, column, record){
						return (record.get('latestBidderPrice') && record.get('latestBidderPrice') > 0 ?
								record.get('latestBidderPrice') : record.get('publishedPrice')) 
									+ ' ' + record.get('priceUnit');
					}
				}],
				store : store
			},{
				xtype : 'ekasmartplt-view-general-widget-ekatextarea',
				width : '75%',
				style : 'margin:20px 10px 10px 10px;',
				fieldLabel : textAreaLabel,
				itemId : 'reasonTextareaItemId',
				labelAlign : 'top',
				validator : function(value){
					if(!value || value.trim().length == 0)
						return 'This is mandatory field';
					return true;
				},
				enforceMaxLength : true,
				maxLength : 250
			}],
			bbar : {
				items : [{
					xtype : 'button',
					text : 'CANCEL',
					cls : 'button-inverse',
					handler : 'cancelButtonHandler'
				},'->',{
					xtype : 'button',
					text : buttonText,
					cls: 'button-secondary',
					itemId : 'mainActionButtonItemId',
					handler : 'okButtonHandler'
				}]
			}
		});
		this.callParent(arguments);
    },
	
	getActionMainLabel : function(){
		if(this.confirmationType == 'ACCEPT'){
			return 'You are accepting following bids at below final price:';
		}
		else{
			if(this.confirmationType == 'CANCEL')
			return 'You are cancelling the following bid:';

			return 'You are rejecting following bids:';
		}
	},
	
	okButtonHandler : function(){
		
		let _this=this;
		var reasonItem = this.down('#reasonTextareaItemId');
		var status = this.confirmationType == 'CANCEL'?'Cancelled':'Rejected';
		let recordData={};
		recordData['remarks']=reasonItem.getValue();
		recordData['status']=status;

		if(!reasonItem.isValid()){
			return;
		}
		var refIds = [];
		for(var key in this.records){
			refIds.push(this.records[key].get('refId'));
		}
		let successCancelCallBack=function(){
			let msg =  "Deal with order Ref ID :"+ refIds+" has been Cancelled";	
			_this.successCallback(msg);
			_this.close();
		}

		let successModifyCallBack=function(){
			_this.successCallback();
			_this.close();
		}

		let errorCallBack=function(){
			Ext.Msg.alert("Error");
		}
		if(this.confirmationType != 'CANCEL'){
			let isOfferor=this.parentView.reference=="manageBidsOfferor";
			FarmerConnectService.updateBid(refIds,recordData,successModifyCallBack,errorCallBack,isOfferor)
		}
		else{
			FarmerConnectService.cancelOffer(refIds,recordData,successCancelCallBack,errorCallBack)
		}
	
	
	},
	
	cancelButtonHandler : function(){
		this.close();
	}
});
