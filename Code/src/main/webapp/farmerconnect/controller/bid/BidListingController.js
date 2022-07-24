Ext.define("EkaSmartPlt.ekacac.farmerconnect.controller.bid.BidListingController", {
	extend: "EkaSmartPlt.platform.controller.AbstractEkaController",
	alias:"controller.ekacacapp-farmerconnect-controller-bid-bidlistingcontroller",
	control: {
		'ekacacapp-farmerconnect-view-bid-bidlisting menuitem[operation="Accept"]':{
			  click: function(item, event, options){
				this.acceptButtonHandler(item, event, options)
			  }
		}, 'ekacacapp-farmerconnect-view-bid-bidlisting menuitem[operation="Reject"]':{
			  click: function(item, event, options){
				  this.rejectButtonHandler(item, event, options)
			  }
		},'ekacacapp-farmerconnect-view-bid-bidlisting menuitem[operation="Counter"]':{
			  click: function(item, event, options){
				  this.counterButtonHandler(item, event, options)
			  }
		},'ekacacapp-farmerconnect-view-bid-bidlisting menuitem[operation="Cancel"]':{
			  click: function(item, event, options){
				  this.cancelButtonHandler(item, event, options)
			  }
		},
		/** My Offers */
		
	'ekacacapp-farmerconnect-view-offer-myoffers menuitem[operation="Delete"]':{
			click: function(item, event, options){
				this.deleteButtonHandler(item, event, options)
			}
		},'ekacacapp-farmerconnect-view-offer-myoffers menuitem[operation="Modify"]':{
			click: function(item, event, options){
				this.modifyButtonHandler(item, event, options)
			}
		},
		/** Bids- ManageBids */

		'ekacacapp-farmerconnect-view-bid-managebids menuitem[operation="Accept"]':{
			click: function(item, event, options){
			  this.acceptButtonHandler(item, event, options)
			}
		}, 'ekacacapp-farmerconnect-view-bid-managebids menuitem[operation="Reject"]':{
				click: function(item, event, options){
					this.rejectButtonHandler(item, event, options)
				}
		},'ekacacapp-farmerconnect-view-bid-managebids menuitem[operation="Counter"]':{
				click: function(item, event, options){
					this.counterButtonHandler(item, event, options)
				}
		},'ekacacapp-farmerconnect-view-bid-managebids menuitem[operation="Cancel"]':{
				click: function(item, event, options){
					this.cancelButtonHandler(item, event, options)
				}
		},

		/** offer- ManageBids */

		'ekacacapp-farmerconnect-view-offer-managebids menuitem[operation="Accept"]':{
			click: function(item, event, options){
			  this.acceptButtonHandler(item, event, options)
			}
		}, 'ekacacapp-farmerconnect-view-offer-managebids menuitem[operation="Reject"]':{
				click: function(item, event, options){
					this.rejectButtonHandler(item, event, options)
				}
		},'ekacacapp-farmerconnect-view-offer-managebids menuitem[operation="Counter"]':{
				click: function(item, event, options){
					this.counterButtonHandler(item, event, options)
				}
		},'ekacacapp-farmerconnect-view-offer-managebids menuitem[operation="Cancel"]':{
				click: function(item, event, options){
					this.cancelButtonHandler(item, event, options)
				}
		},
		/** Bids - Published Offers */
		
		'ekacacapp-farmerconnect-view-bid-publishedoffers menuitem[operation="Accept"]':{
			click: function(item, event, options){
				this.acceptButtonHandler(item, event, options)
			}
		},'ekacacapp-farmerconnect-view-bid-publishedoffers menuitem[operation="Counter"]':{
			click: function(item, event, options){
				this.counterButtonHandler(item, event, options)
			}
		}
	},
	init: function() {
		//this.getAppPermissions();

		return this.callParent(arguments);

	},
	onListReady : function(){
	
		this.view.store.loadData(this.fetchBidList());

		var toolbar = Ext.ComponentQuery.query('#toolbarItemId')[0];
		var menutoolbarComboContainer= Ext.ComponentQuery.query('#menutoolbarComboContainerId')[0];
		if(toolbar){
			toolbar.remove(menutoolbarComboContainer);
			toolbar.insert(0, {
				xtype : 'container',
				itemId : 'menutoolbarComboContainerId',
				layout : {
					type : 'hbox'
				},
				items : [{
							xtype: 'button',
							icon: 'resources/images/page-back-button.png', 
							cls: 'button-back menu-icon',
							style: 'padding: 16px 0px 5px 8px',
							tooltip: 'Back to Insight Home',
							itemId:'iconBackToApps',
							componentData : {
								itemType:"GRID",
								isStdAppContext:true,
								componentType:"ekacacapp-view-insight-insightslayoutview"
							}					
						}
				]
			});	
		}
	},
	
	acceptButtonHandler : function(){
		var view = this.view;
		var _this = this;
		var selectedRecords = view.getSelection();
		if(selectedRecords.length>1){
			Ext.Msg.alert('Alert', 'More than one record cannot be selected for this operation!');
					return;
		}
		else{
			Ext.create('EkaSmartPlt.ekacac.farmerconnect.view.bid.BidCounterConfirmationWindow',{
				confirmationType : 'ACCEPT',
				title : Ext.isEmpty(selectedRecords[0].get('refId'))?selectedRecords[0].get('bidId'):selectedRecords[0].get('refId'),
				record : selectedRecords[0],
				operation: 'Accept',
				parentView:_this.getView(),
				successCallback : function(){
					view.getSelectionModel().deselectAll();
					view.store.loadData(_this.fetchBidList());
				}
			}).show(); 
					}
	},
	
	rejectButtonHandler : function(){
		var _this = this;
		var view = this.view;
		var selectedRecords = view.getSelection();
		Ext.create('EkaSmartPlt.ekacac.farmerconnect.view.bid.BidConfirmationWindow',{
			confirmationType : 'REJECT',
			records : selectedRecords,
			parentView:_this.getView(),
			successCallback : function(){
				view.getSelectionModel().deselectAll();
				view.store.loadData(_this.fetchBidList());
			}
		}).show();
	},
	
	counterButtonHandler : function(){
		var _this = this;
		var view = this.view;
		var selectedRecords = view.getSelection();
		if(selectedRecords.length>1){
			Ext.Msg.alert('Alert', 'More than one record cannot be selected for this operation!');
					return;
		}
		else{
			Ext.create('EkaSmartPlt.ekacac.farmerconnect.view.bid.BidCounterConfirmationWindow',{
				title : Ext.isEmpty(selectedRecords[0].get('refId'))?selectedRecords[0].get('bidId'):selectedRecords[0].get('refId'),
				record : selectedRecords[0],
				parentView:_this.getView(),
				operation: 'Counter',
				successCallback : function(){
					view.getSelectionModel().deselectAll();
					view.store.loadData(_this.fetchBidList());
				}
			}).show();
		}
	},
	cancelButtonHandler : function(){
      if(PlatformAppConstants.FarmerConnectSettings.bidCancellationAllowed){
		var _this = this;
		var view = this.view;
		var selectedRecords = view.getSelection();
		Ext.create('EkaSmartPlt.ekacac.farmerconnect.view.bid.BidConfirmationWindow',{
			confirmationType : 'CANCEL',
			parentView:_this.getView(),
			records : selectedRecords,
			successCallback : function(msg){
				Ext.isEmpty(msg) ? '': _this.showToast(msg);
				view.getSelectionModel().deselectAll();
				view.store.loadData(_this.fetchBidList());
			}
		}).show();
	  }
	  else{
		Ext.Msg.alert('Alert', 'Operation Not Allowed');
		return;
	  }
		

		
	},
	deleteButtonHandler : function(){
		var _this = this;
		var view = this.view;
		var selectedRecords = view.getSelection();

		/* Ext.MessageBox.confirm('Delete Confirmation', 'Are you sure you want to Delete the Deal with order Ref Id : '+selectedRecords[0].data['bidId'], this); */
		 Ext.create('EkaSmartPlt.ekacac.farmerconnect.view.offer.OfferConfirmationWindow',{
			confirmationType : 'DELETE',
			records : selectedRecords,
			parentView:_this.getView(),
			successCallback : function(msg){
				Ext.isEmpty(msg) ? '': _this.showToast(msg);
				view.getSelectionModel().deselectAll();
				view.store.loadData(_this.fetchBidList());
			}
		}).show();
	},
	onGridSelectionChange : function(grid, selectedRecords){
		let recordsLen = selectedRecords.length;
		let operations = [];
		let bidNotPendingOnOfferer=true;
		var _this = this;
		var view = this.view;
		var selectedRecords = view.getSelection();
		Ext.Object.each(this.view.getReferences(),
			function(key,value){
				value.hide()
			});
		
		if(recordsLen == 0){
			return;
		}
		for(let key in selectedRecords){
			let pendingOn = selectedRecords[key].get('pendingOn');
			let status = selectedRecords[key].get('status');
			if(_this.getView().reference == "publishedOffersGrid" || _this.getView().reference == "myOffersGrid"){
				Ext.Object.each(this.view.getReferences(),
				function(key,value){
					value.show()
				});
			}
			else{
				let operationArr = status?(this.view.applicableOpertions[status]?this.view.applicableOpertions[status]:[]):[];
				let filteredOperations = [];
				if(_this.getView().reference =="manageBidsOfferor"){
				if(pendingOn != 'Offeror'){
					bidNotPendingOnOfferer=true;
					filteredOperations  = operationArr.filter(function(value, index, arr){
						return (value != 'acceptBid' && value != 'rejectBid' && value != 'counterBid');
					});
				}else{
					bidNotPendingOnOfferer=false;
					filteredOperations = operationArr;
				}
			}
				else{
					if(pendingOn != 'Bidder'){
						bidNotPendingOnOfferer=true;
						filteredOperations  = operationArr.filter(function(value, index, arr){
							return (value != 'acceptBid' && value != 'rejectBid' && value != 'counterBid');
						});
					}else{
						bidNotPendingOnOfferer=false;
						filteredOperations = operationArr;
					}
				}
				operations.push(filteredOperations);
				let operationsToShow = this.getArrayIntersection(operations);
				if(operationsToShow.length){
					for(let id in operationsToShow){
						let operation = operationsToShow[id];
						this.view.getReferences()[operation].show();
					}
				}else {
					Ext.Msg.alert('Alert', 'No '+(recordsLen > 1 ? 'Common' : '')+' Operation applicable for '+ (recordsLen > 1 ? 'the Selected Bid(s)!' : 'the Selected Bid!'));
					return;
				}
			}
			}

		/*this.view.down('#acceptButtonItemId').show();
		this.view.down('#rejectButtonItemId').show();
		selectedRecords.length > 1 ? this.view.down('#counterButtonItemId').hide() : this.view.down('#counterButtonItemId').show();*/
	},
	getArrayIntersection : function(dataArr){
		return dataArr.reduce((a, b) => a.filter(c => b.includes(c)));
	},
	fetchBidList : function(){
		let _this = this;
		let url=_this.getView().componentData.listingUrl;
		let operator=_this.getView().componentData.operator;
		var extraparam = {
            "columnName": "User Name",
            "columnType": 1,
            "type": "basic",
            "columnId": "username",
			"value": [sessionStorage.userName],
			"operator":operator
        }


		return EkaAjaxService.performSyncAjaxCall(url,{
			method : 'GET',
			params : {
				requestParams : Ext.encode({
					sortBy : {},
					filters : [extraparam],
					pagination : {},
					
				})
			}
		});
	},
	
	onGridCellClick : function( grid, td, cellIndex, record, tr, rowIndex){
		let _this = this;
		if(_this.getView().reference == "publishedOffersGrid" || _this.getView().reference == "myOffersGrid"){
			_this.onGridSelectionChange(grid, grid.getSelection());

		}else{
			if(cellIndex == 2){
				grid.deselect(record);
				Ext.create('EkaSmartPlt.ekacac.farmerconnect.view.bid.BidCounterConfirmationWindow',{
										parentView : _this.getView(),
										title : Ext.isEmpty(record.get('refId'))?record.get('bidId'):record.get('refId'),
										record : record,
										mode : 'BIDLOG',
										successCallback : Ext.emptyFn
									}).show();
			} else {
				_this.onGridSelectionChange(grid, grid.getSelection());
			}
		}
		
	},
	onGridHeaderClick : function(gridHeader, column, e, dom, eOpts){
		
		/* As event is fired before actual selection/deselection is done
		 * Select All : if currentSelection != totalRecords
		 * Otherwise it's deselect all
		 */
		if(dom.innerText === "" // Will be empty only for Select All Check Box
				&& gridHeader.grid.getSelection().length !== gridHeader.grid.store.count()){
			this.onGridSelectionChange(gridHeader.grid, gridHeader.grid.store.data.items);	
		}		
	},
	
	createNewOfferHandler:function(){

		let _this=this;
		let view=_this.getView();
	Ext.create('EkaSmartPlt.ekacac.farmerconnect.view.offer.NewOffer',{
		componentData:'',
		title:'New Offer',
		successCallback : function(){
			view.getSelectionModel().deselectAll();
			view.store.loadData(_this.fetchBidList());
		}
	}).show();
  },
  modifyButtonHandler:function(){
	var _this = this;
	var view = _this.getView();
	var selectedRecords = view.getSelection();
	let record={};
	record['bidJSON']=selectedRecords[0].getData();
	record.bidJSON.status="Modify";
	if(selectedRecords.length>1){
		Ext.Msg.alert('Alert', 'More than one record cannot be selected for this operation!');
				return;
	}
	else{
		Ext.create('EkaSmartPlt.ekacac.farmerconnect.view.offer.NewOffer',{
			componentData:record,
			title:'Modify Offer',
			successCallback : function(){
				view.getSelectionModel().deselectAll();
				view.store.loadData(_this.fetchBidList());
			}
		}).show();
	}
  },
  showToast: function(msg) {
	Ext.toast({
		html: msg,
		closable: false,
		align: 't',
		slideDuration: 400,
		maxWidth: 400
	});
}
});