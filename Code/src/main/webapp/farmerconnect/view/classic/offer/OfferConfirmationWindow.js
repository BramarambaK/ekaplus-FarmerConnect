Ext.define('EkaSmartPlt.ekacac.farmerconnect.view.offer.OfferConfirmationWindow', {
	extend: 'Ext.window.Window',
	cls: 'smart-window custom-form-panel',
	modal : true,
	width : 340,
	height : 200,
	scrollable : true,
	defaultListenerScope : true,
    initComponent: function() {
		var buttonText = 'DELETE';
		Ext.apply(this, {
			title : 'Delete Confirmation',
			padding : 10,
			items : [{
				xtype : 'label',
				cls:'offer-window-content',
				html : this.getActionMainLabel()
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
	
	getMultiSelectValues: function(records){
		let result='';
			for(i=0;i<records.length;i++){
				if(i==0)
					result = records[i].data['bidId'];
				else
					result = result + ', ' + records[i].data['bidId'];
			}
		return result;
	},
	
	getActionMainLabel : function(){
		if(this.confirmationType == 'DELETE'){
			let records = this.records;
			let result = this.getMultiSelectValues(records);
			return 'Are you sure you want to Delete the Deal with order <br> Ref Id : '+result;
		}
		return 'You are rejecting following bids:';
	},
	
	okButtonHandler : function(){
		let _this=this;
		var reasonItem = _this.down('#reasonTextareaItemId');
		var status = _this.confirmationType == 'ACCEPT' ? 'Accepted' : (_this.confirmationType == 'CANCEL'?'Cancelled':_this.confirmationType == 'DELETE'?'DELETE':'Rejected');
		if(_this.confirmationType != 'DELETE' && _this.confirmationType != 'ACCEPT' && !reasonItem.isValid()){
			return;
		}
		var refIds = [];
		for(var key in _this.records){
			refIds.push(_this.records[key].get('bidId'));
		}
		let successCallback=function(){
			msg = 'Offer with order Ref Id : '+refIds+' has been Deleted';	
			_this.successCallback(msg);
			_this.close();

		}
		let errorCallBack=function(){
			Ext.Msg.alert("Error");

		}
		FarmerConnectService.updateOffer('DELETE',refIds,reasonItem,successCallback,errorCallBack)
	},
	
	cancelButtonHandler : function(){ 
		this.close();
	}/* ,
	showToast: function(msg) {
        Ext.toast({
            html: msg,
            closable: false,
            align: 't',
            slideDUration: 400,
            maxWidth: 400
        });
    } */
});