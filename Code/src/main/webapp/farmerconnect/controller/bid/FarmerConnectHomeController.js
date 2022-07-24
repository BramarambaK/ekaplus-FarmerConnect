Ext.define("EkaSmartPlt.ekacac.farmerconnect.controller.bid.FarmerConnectHomeController", {
	extend: "EkaSmartPlt.platform.controller.AbstractEkaController",
	alias:"controller.ekasmartplt-farmerconnect-controller-bid-farmerconnecthomecontroller",
	init: function() {
		return this.callParent(arguments);
	},	
	onButtonChange : function(button, newValue, oldValue, eOpts){
		FarmerConnectConfigurationObject.activePage = newValue;
		switch(newValue){
			case 'publishedprices' :
					this.showPublishedPricesComponent();
					 eatracker.trackEvent(EkaGoogleAnayticsUtil.pageTrackingMap.FARMER_CONNECT, EkaGoogleAnayticsUtil.constants.PUBLISHED_PRICES);									break;
			case 'bid' :
					this.showBidComponent();
 					eatracker.trackEvent(EkaGoogleAnayticsUtil.pageTrackingMap.FARMER_CONNECT, EkaGoogleAnayticsUtil.constants.BIDS);
				break;
			default :
				console.log(Localization.DATAVIEW_CONSTANTS.VALUE_NOT_SUPPORTED);
        }
        var sortButton = this.getView().down('#sortButtonItemId');
        sortButton.setButtonState(newValue);
		this.getView().setFilterButtonState();
	},
    showBidComponent : function(){
        var view = this.getView();
		var existingPublishedPricesPanel = view.down('#publishedPricesPanelItemId');
		if(existingPublishedPricesPanel)
            existingPublishedPricesPanel.hide();
		var existingBid = view.down('#bidPanelItemId');
		if(existingBid){
			existingBid.show();
			var existingBidListPanel = existingBid.down('#bidListPanelItemId');
			existingBidListPanel.getStore().load();
			return;
		}
		view.add({
            xtype: 'panel',
            itemId : 'bidPanelItemId',
            items : [{
				xtype: 'segmentedbutton',
				cls:'bids-status-round-buttons',
				itemId : 'bidsStatusButtonItemId',
				style:'padding-top:12px;padding-bottom:0px',
				height:55,
				docked: 'top',
				listeners : {
					change : 'onBidStatusButtonChange',
					scope : this
				},
				items: [{
					text : '',
					align:'centre',
					width:40,
                    height:40,
                    iconCls:'in-progress-bids-icon',
					value : 'In-Progress',
					pressed : FarmerConnectConfigurationObject.activeTabBisScreen == 'In-Progress'
				},{
					text : '',
					align:'centre',
					width:40,
                    height:40,
                    value : 'Accepted',
                    iconCls:'accepted-bids-icon',
					style:'margin-left:65px',
					pressed : FarmerConnectConfigurationObject.activeTabBisScreen == 'Accepted'
				},{
					text : '',
					align:'centre',
					width:40,
					height:40,
					iconCls:'rejected-bids-icon',
					style:'margin-left:65px',
					value : 'Rejected',	
					pressed : FarmerConnectConfigurationObject.activeTabBisScreen == 'Rejected'
				}]
			},{
				xtype: 'ekasmartplt-farmerconnect-view-bid-listofbids',
				itemId : 'bidListPanelItemId',
				componentData : view.componentData
			}],			
		});
	},
	onBidStatusButtonChange : function( object, value, oldValue, eOpts ){
        var view = this.getView();
		if(value){
			FarmerConnectConfigurationObject.activeTabBisScreen = value;
			var pageFilters = [];
			pageFilters.push(value);
			if(value == "Accepted"){
				pageFilters.push("Cancelled");
			}
			var filter = {
                columnId: "status",
				columnName: "status",
				columnType: 1,
				operator: "in",
				type: "basic",
				value: pageFilters
			};
			var existingBid = view.down('#bidListPanelItemId');
			if(existingBid){
				var requestParams = Ext.decode(existingBid.getStore().getProxy().getExtraParams().requestParams);
				var filters = requestParams.filters;
				var newFilters = [];
				for(var i = 0 ; i < filters.length; i++){
					if(filters[i].columnName != "status"){
						// filters.splice(i,1);
						newFilters.push(filters[i]);
					}
				}
				newFilters.push(filter);
				requestParams.filters = newFilters;
				existingBid.getStore().getProxy().setExtraParams({
					requestParams : Ext.encode(requestParams)
				});
				existingBid.getStore().load();

				// existingBid.getStore().clearFilter();
				// existingBid.getStore().addFilter([
				// 	function(item) {
				// 		var status = item.get('status');
				// 		if(value == "Accepted"){
				// 			return status == value || status == "Cancelled";
				// 		}
				// 		return  status == value;
				// 	}
				// ]);
			}
		}		
	},
	showPublishedPricesComponent : function(){
        var view = this.getView();
		var existingBid = view.down('#bidPanelItemId');
		if(existingBid)
            existingBid.hide();
		var existingPublishedPricesPanel = view.down('#publishedPricesPanelItemId');
		if(existingPublishedPricesPanel){
			existingPublishedPricesPanel.show();
			existingPublishedPricesPanel.getStore().load();
			return;
		}
		view.add({
            xtype: 'ekasmartplt-farmerconnect-view-bid-listofpublishedprices',
			itemId : 'publishedPricesPanelItemId',
			componentData : view.componentData
		});
	},
	applySort : function(object, value){
		var activePage = this.getActivePage();
		var requestParams = Ext.decode(activePage.getStore().getProxy().getExtraParams().requestParams);
		var sortBy = {};
		sortBy[object.filterKey] = object.getValue();
        requestParams.sortBy = sortBy;
		// setting update value in local storage
        // var localStorageValues = Ext.decode(localStorage.getItem(this.getActivePageValue()));
        // localStorageValues.sortBy = sortBy;	
		// localStorage.setItem(this.getActivePageValue(), Ext.encode(localStorageValues));
		UserPreferences.setSortBy(this.getActivePageValue(), sortBy);
        //refreshing store
		activePage.getStore().getProxy().setExtraParams({
			requestParams : Ext.encode(requestParams)
		});
		activePage.getStore().load();
	},
	removeSort : function(object, value){
		var activePage = this.getActivePage();
		if(activePage.getStore()){
			var requestParams = Ext.decode(activePage.getStore().getProxy().getExtraParams().requestParams);
			requestParams.sortBy = {};        
			// setting update value in local storage
			// var localStorageValues = Ext.decode(localStorage.getItem(this.getActivePageValue()));
			// localStorageValues.sortBy = {};	
			// localStorage.setItem(this.getActivePageValue(), Ext.encode(localStorageValues));
			UserPreferences.setSortBy(this.getActivePageValue(), {});
			//refreshing store
			activePage.getStore().getProxy().setExtraParams({
				requestParams : Ext.encode(requestParams)
			});
			activePage.getStore().load();
		}	
	},
	getActivePage : function(){
        var farmerConnectSegmentedButtonValue = this.getActivePageValue();
		var activePageItemId = '';
		switch(farmerConnectSegmentedButtonValue){
			case 'publishedprices' : activePageItemId = 'publishedPricesPanelItemId';
									break;
			case 'bid' : activePageItemId = 'bidListPanelItemId';
						break;
		}
		return this.getView().down('#' + activePageItemId);
    },
    getActivePageValue : function(){
        var view = this.getView();
		var farmerConnectSegmentedButton = view.down('#farmerConnectSegmentedButtonItemId');
		return farmerConnectSegmentedButton.getValue();
    },
	setSortFilterValuesInLocalStorage : function(){
		// var userPreference = UserPreferences.currentUserPreference;
		// if(UserPreferences.isJSONEmpty(currentUserPreference)){
		// 	userPreference = {
		// 		filters : {
		// 			publishedPrices : [],
		// 			bids : []
		// 		},
		// 		sortBy : {}
		// 	}
		// }
		// var publishedprices = localStorage.getItem('publishedprices');
		// if(!publishedprices){
		// 	localStorage.setItem('publishedprices',Ext.encode({
		// 		sortBy : {},
		// 		filters: []
		// 	}))
		// }
		// var bid = localStorage.getItem('bid');
		// if(!bid){
		// 	localStorage.setItem('bid',Ext.encode({
		// 		sortBy : {},
		// 		filters: []
		// 	}))
		// }
	},
	showFilters : function(){
        var view = this.getView();
		var farmerConnectSegmentedButtonValue = this.getActivePageValue();
        view.hide();
        var componentData = {
            componentType : 'ekasmartplt-farmerconnect-view-bid-filterviewfarmerconnect',
            dataviewId : view.componentData.dataviewId,
            prevComponent : view,
            sourceValue : farmerConnectSegmentedButtonValue
        };
        this.addComponentToContentPanelHandler(componentData);		
    },
    backButtonHandler : function(){
        FarmerConnectConfigurationObject.activePage = 'publishedprices';
		this.superclass.backButtonHandler();
	},
	newOffer : function(){
		var view = this.getView();
        view.hide();
		var componentData={
			componentType:'ekasmartplt-farmerconnect-view-offer-newoffer',
            prevComponent : view
		}
		this.createComponentHandler(componentData);		

	}
});
