Ext.define('EkaSmartPlt.farmerconnect.view.bid.FarmerConnectHome', {
	extend: 'Ext.Container',
	xtype : 'ekasmartplt-farmerconnect-view-bid-farmerconnecthome',
	itemId:'farmerConnectHomeItemId',
	controller : 'ekasmartplt-farmerconnect-controller-bid-farmerconnecthomecontroller',
	requires: [
		'EkaSmartPlt.ekacac.farmerconnect.controller.bid.FarmerConnectHomeController',
		'EkaSmartPlt.farmerconnect.view.bid.ListOfPublishedPrices',
		'EkaSmartPlt.farmerconnect.view.bid.BidDetailView',
		'EkaSmartPlt.farmerconnect.view.bid.BidEditView',
		'EkaSmartPlt.farmerconnect.view.bid.BidRatingView',
		'EkaSmartPlt.farmerconnect.view.bid.ListOfBids',
		'EkaSmartPlt.farmerconnect.view.bid.FarmerConnectConfigurationObject',
		'EkaSmartPlt.farmerconnect.view.bid.FilterViewFarmerConnect',
		'EkaSmartPlt.farmerconnect.view.offer.NewOffer'
	],
	layout : 'vbox',
	scrollable : 'y',
	listeners : {
		applySort : 'applySort',
		removeSort : 'removeSort',
		scope : 'controller'
	},
	initialize : function() {
		this.addConfig();
		EkaLoader.mask(Localization.GENERAL.LOADING_MASK);		
		var activeId = this.config.buttonTypes[0].id;
		if(FarmerConnectConfigurationObject.activePage)
			activeId = FarmerConnectConfigurationObject.activePage;
	
		this.addTitleBar();
		this.addHomeButtons(activeId);
		this.add({
			xtype: 'toolbar',
			cls:'app-common-footer',
			itemId : 'bidFooterItemId',
			//style:'margin-bottom:6px',
			height:50,
			docked: 'bottom',
			items: [{
                xtype: 'ekasmartplt-view-basecomponent-sortbutton',
				iconAlign:'top',
				parentView : this,
				menuItems : FarmerConnectConfigurationObject.sortParams,
                text:Localization.DATAVIEW_CONSTANTS.SORT,
				align:'left',
				width:160,
				arrow : false,
                height:46,
                value : false,
                itemId : 'sortButtonItemId',
				cls: 'button-image icon-size-20 sort-icon-cls',
				iconCls: 'icon-sort',
				// scope : 'controller',
			},{ 	
				xtype : 'spacer'
			},{
                xtype: 'button',
                iconAlign:'top',
                text: Localization.DATAVIEW_CONSTANTS.FILTER,
				align:'left',
				width:160,
                height:46,
                itemId : 'fiterButtonItemId',
				cls: 'button-image icon-size-20 filter-icon-cls',
				iconCls: 'icon-filter',
				handler: 'showFilters',
				scope : 'controller',
				enableToggle : true,
				pressed : false
			}]
		});
		this.getController().onButtonChange(null, activeId);
		this.setFilterButtonState();
    	this.callParent(arguments);
	},
    addConfig : function(){
		this.config = {
			buttonTypes : [{type : Localization.FARMER_CONNECT.PUBLISHED_PRICES, id : 'publishedprices'},
			{type : Localization.FARMER_CONNECT.BID, id : 'bid'}]
		}
	},
	addTitleBar:function(){
		this.add({
			xtype: 'titlebar',
			docked: 'top',
			cls : 'card-title app-title-bar '+localStorage.getItem('styleClass'),
			style : 'overflow: visible;',
			height : 50,
			width:'100%',
			items: [{
				xtype: 'button',
				handler: 'backButtonHandler',
				scope : 'controller',
				width:30,
				height:40,
				cls: 'button-image',
				style:'margin-right:0px;',
				iconCls: 'icon-back'
			},{
				xtype : 'label',
				html : FarmerConnectConfigurationObject.role=="Offerer"?Localization.FARMER_CONNECT.FARMER_CONNECT_OFFERER:Localization.FARMER_CONNECT.FARMER_CONNECT_BID,
				cls:'card-title'
			},{
				xtype: 'button',
				handler: 'addNewOffer',
				scope : 'controller',
				hidden:!( FarmerConnectConfigurationObject.role=="Offerer"),
				width:30,
				height:40,
				style:'position:absolute;right:13px',
				handler:'newOffer',
				iconCls: 'icon-plus'
			}]
		});
	},
	addHomeButtons : function(activeId){
        var items = [];
		var buttonData = this.config.buttonTypes;
        for(var i in buttonData){
            items.push({
                text : buttonData[i].type,
				width:150,
				value : buttonData[i].id,
				pressed : buttonData[i].id == activeId
            });
        }
         if(eatracker){
			eatracker.trackEvent(EkaGoogleAnayticsUtil.pageTrackingMap.FARMER_CONNECT, EkaGoogleAnayticsUtil.constants.PUBLISHED_PRICES);
		}		this.add({
			xtype: 'toolbar',
			cls:'center-align-toolbar bid-screen-top-toolbar',
			// itemId : 'farmerConnectItemId',
			height:40,
			docked: 'top',
			items: [{
				xtype : 'segmentedbutton',
				style:'margin:10px 0',
				cls:'segmented-button-cls',
				itemId : 'farmerConnectSegmentedButtonItemId',
				items : items,
				listeners : {
					change : 'onButtonChange',
					scope : 'controller'
				}
			}]
		});
	},
	setFilterButtonState: function(){
		var activePageValue = this.down('#farmerConnectSegmentedButtonItemId').getValue();
		// var retainedValues = Ext.decode(localStorage.getItem(activePageValue));
		var filters = UserPreferences.getFilters(activePageValue);
        // if(retainedValues){
        //     var filters = retainedValues.filters;
            if(filters && filters.length>0){
                this.down('#fiterButtonItemId').setPressed(true);
			}
			else{
				this.down('#fiterButtonItemId').setPressed(false);
			}                
        // }
	}
});