Ext.define('EkaSmartPlt.ekacac.farmerconnect.view.bid.PublishedOffers', {
    extend: 'EkaSmartPlt.platform.view.general.grid.base.BaseGridWithGrouping',
    alias: 'widget.ekacacapp-farmerconnect-view-bid-publishedoffers',
	controller: "ekacacapp-farmerconnect-controller-bid-bidlistingcontroller",
	requires: ['EkaSmartPlt.ekacac.farmerconnect.controller.bid.BidListingController',
		'EkaSmartPlt.ekacac.farmerconnect.view.bid.BidConfirmationWindow',
		'EkaSmartPlt.ekacac.farmerconnect.view.bid.BidCounterConfirmationWindow'],
	defaults: {
       frame: false
	},
	scrollable: true,
	cls: 'custom-grid-plane grid-panel-sub-page row-over-style custom-checkbox-selection bids-listing',
	width:window.innerWidth-16,
	margin : '10',
	showOnlyTotalRecords : true,
	listeners : {
		boxready : 'onListReady',
		cellclick : 'onGridCellClick',
		headerclick : 'onGridHeaderClick',
		scope : 'controller'
	},
	reference:'publishedOffersGrid',
	selModel: {
		selType : 'checkboxmodel',
		mode : 'SIMPLE'
	},
	tbar :{
		cls: 'breadcrumb-toolbar breadcrumb-toolbar-background-white toolbar-dataview',
		items : [{
			xtype : 'label',
			cls:'label-main-title',
			html : 'Bids - Published Offers'
		},{
			xtype: 'tbfill'
		}, {
			text: 'Operations',
			cls: 'button-outline',
			iconCls: 'operation-arrow',
			iconAlign: 'right',
			xtype: 'ekacacapp-button',
			itemId: 'operations_id',
			menu: [{
				text: 'Accept',
				operation: 'Accept',
				itemId:'acceptButtonItemId' ,
				hidden : true,
				scope : 'controller',
				handler	:'acceptButtonHandler',
				cls: 'button-primary tabpanel-button',
				reference : 'acceptBid'
			},{
				text: 'Counter',
				operation: 'Counter',
				itemId:'counterButtonItemId' ,
				scope : 'controller',
				handler	:'counterButtonHandler',
				hidden : true,
				cls: 'button-primary tabpanel-button',
				reference : 'counterBid'
			}],
			scope: this
		}]
	},
    initComponent: function() {
		Ext.apply(this, {
			store : new Ext.data.Store({
				fields : [{
					name: 'expiryDate', 
					type : 'date', 
					dateFormat : 'time'        
                },{
					name: 'shipmentDate', 
					type : 'date', 
					dateFormat : 'time'        
                },{
					name: 'deliveryFromDate', 
					type : 'date', 
					dateFormat : 'time'        
				},{
					name: 'deliveryToDate', 
					type : 'date', 
					dateFormat : 'time'        
				}]
			}),
			columns :this.createGridColumns()
		});
		this.columnDisplayNames = {
			refId : 'Ref Id',
			bidId : 'Bid Id',
			customerId : "Bidder's Id",
			applicableRoles : "Applicable Role(s)",
			agentId : "Agent's Id",
			quality : 'Quality',
			quantity : 'Quantity',
			location : 'Location',
			publishedPrice : 'Published Price',
			latestBidderPrice : 'Latest Bidder Price',
			latestOfferorPrice : 'Latest Offeror Price',
			updatedDate : 'Updated Date',
			priceUnit : 'Price Unit'
		};
		this.callParent(arguments);
    },
	
	createGridColumns : function(){
		return [{
			text: 'Offer Type',
			width : '8%',
			dataIndex: 'offerType'
        },
        {
			text: 'Offer Ref Id',
			width : '8%',
			dataIndex: 'bidId',
			renderer : function(value, column, record){
				return '<a href="#">' +record.get('bidId') + '</a>';
			}
        },{
			text : 'Product',
            dataIndex : 'product',
			width : '8%',
            filter : {
				type: 'list'
            },
			multiValueTextData : true
        },{
			text : 'Quality',
            dataIndex : 'quality',
			width : '8%',
            filter : {
				type: 'list'
            },
			multiValueTextData : true
        },{
			text : 'Crop Year',
            dataIndex : 'cropYear',
			width : '8%',
            filter : {
				type: 'list'
            },
			multiValueTextData : true
        },{
			text : 'Location',
			dataIndex : 'location',
			width : '8%',
            filter : {
				type: 'list'
			},
			multiValueTextData : true
		},{
			text : 'Packing Size',
			hidden: PlatformAppConstants.FarmerConnectSettings.offerType=="basic",
            dataIndex : 'packingSize',
			width : '8%',
            filter : {
				type: 'list'
            },
			multiValueTextData : true
		},{
			text : 'Packing Type',
			hidden: PlatformAppConstants.FarmerConnectSettings.offerType=="basic",
            dataIndex : 'packingType',
			width : '8%',
            filter : {
				type: 'list'
            },
			multiValueTextData : true
		},
		{
			text : 'Payment Term',
			hidden: PlatformAppConstants.FarmerConnectSettings.offerType=="basic",
            dataIndex : 'paymentTerms',
			width : '8%',
            filter : {
				type: 'list'
            },
			multiValueTextData : true
        },{
			text : 'Published Price',
			dataIndex : 'publishedPrice',
			width : '8%',
            filter : {
				type: 'number'
			},
			renderer : function(value, column, record){
				return Ext.isEmpty(value) ? value : value +' '+ record.get('priceUnit');
			}
		},{
			dataIndex: 'expiryDate',
			text: 'Expiry Date',
			width : '8%',
			xtype: 'datecolumn',
			format: 'd-m-Y',
			filter: {
				type: 'date'
			}
		},{
			text : 'IncoTerm',
            dataIndex : 'incoTerm',
			width : '8%',
            filter : {
				type: 'list'
            },
			multiValueTextData : true
        },{
			text : 'Quantity',
            dataIndex : 'quantity',
			width : '8%',
			filter : {
				type: 'number'
			},
			renderer : function(value, column, record){
				return Ext.isEmpty(value) ? value : value +' '+ record.get('quantityUnit');
			}
		},{
			dataIndex: 'deliveryFromDate',
			text: 'Delivery Period',
			width : '8%',
			xtype: 'datecolumn',
			format: 'd-m-Y',
			filter: {
				type: 'date'
			},
			renderer:function(value, column, record){
				return  new Date(record.get('deliveryFromDateInMillis')).toLocaleDateString() +' to '+new Date(record.get('deliveryToDateInMillis')).toLocaleDateString()
			},
		},{
			text : 'Publisher Name',
            dataIndex : 'offerorName',
			hidden:PlatformAppConstants.FarmerConnectSettings.offerorInfoRestricted,
			width : '8%',
            filter : {
				type: 'list'
            },
			multiValueTextData : true
        }];
	},
	
	refreshHandler : function(){
		let url=this.up().componentData.listingUrl;
		let operator=this.up().componentData.operator;
		var extraparam = {
            "columnName": "User Name",
            "columnType": 1,
            "type": "basic",
            "columnId": "username",
			"value": [sessionStorage.userName],
			"operator":operator
        }
		var response = EkaAjaxService.performSyncAjaxCall(url,{
			method : 'GET',
			params : {
				requestParams : Ext.encode({
					sortBy : "",
					filters : [extraparam],
					pagination : ""
				})
			}
		});
		this.store.loadData(response);
	}
});
