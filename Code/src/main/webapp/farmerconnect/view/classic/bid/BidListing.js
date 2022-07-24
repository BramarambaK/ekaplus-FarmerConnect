Ext.define('EkaSmartPlt.ekacac.farmerconnect.view.bid.BidListing', {
    extend: 'EkaSmartPlt.platform.view.general.grid.base.BaseGridWithGrouping',
    alias: 'widget.ekacacapp-farmerconnect-view-bid-bidlisting',
	controller: "ekacacapp-farmerconnect-controller-bid-bidlistingcontroller",
	requires: ['EkaSmartPlt.ekacac.farmerconnect.controller.bid.BidListingController',
		'EkaSmartPlt.ekacac.farmerconnect.view.bid.BidConfirmationWindow',
		'EkaSmartPlt.ekacac.farmerconnect.view.bid.BidCounterConfirmationWindow'],
	defaults: {
       frame: false
    },
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
	selModel: {
		selType : 'checkboxmodel',
		mode : 'SIMPLE'
	},
	applicableOpertions : {
		"Accepted":['cancelBid'],
		"Rejected":[],
		"In-Progress":['acceptBid','rejectBid','counterBid'],
		"Canceled":[],
	},
	tbar :{
		cls: 'breadcrumb-toolbar breadcrumb-toolbar-background-white toolbar-dataview',
		items : [{
			xtype : 'label',
			cls:'label-main-title',
			html : 'Bids'
		},{
			xtype: 'tbfill'
		}, {
			text: 'Operations',
			cls: 'button-outline',
			iconCls: 'operation-arrow',
			iconAlign: 'right',
			xtype: 'ekacacapp-button',
			itemId: 'operations_id',
			perm_code : 'STD_APP_BIDS_ACCEPT',
			menu: [{
				text: 'Accept',
				operation: 'Accept',
				itemId:'acceptButtonItemId' ,
				perm_code : 'STD_APP_BIDS_ACCEPT',
				hidden : true,
				scope : 'controller',
				handler	:'acceptButtonHandler',
				cls: 'button-primary tabpanel-button',
				reference : 'acceptBid'
			}, {
				text: 'Reject',
				operation: 'Reject',
				itemId:'rejectButtonItemId' ,
				perm_code : 'STD_APP_BIDS_REJECT',
				scope : 'controller',
				handler	:'rejectButtonHandler',
				hidden : true,
				cls: 'button-primary tabpanel-button',
				reference : 'rejectBid'
			},{
				text: 'Counter',
				operation: 'Counter',
				itemId:'counterButtonItemId' ,
				perm_code : 'STD_APP_BIDS_COUNTER',
				scope : 'controller',
				handler	:'counterButtonHandler',
				hidden : true,
				cls: 'button-primary tabpanel-button',
				reference : 'counterBid'
			},{
                                text: 'Cancel this deal',
				operation: 'Cancel',
				itemId:'cancelButtonItemId' ,
				perm_code : 'STD_APP_BIDS_CANCEL',
				scope : 'controller',
				handler	:'cancelButtonHandler',
				hidden : true,
				cls: 'button-primary tabpanel-button',
				reference : 'cancelBid'
			}],
			scope: this
		},/*{
			xtype: 'ekacacapp-button', 
			text: 'Accept',
			style:'margin:3px 10px 0px 0px',
			itemId:'acceptButtonItemId' ,
			cls: 'button-primary tabpanel-button',
			disabled : true,
			perm_code : 'STD_APP_BIDS_ACCEPT',
			scope : 'controller',
			handler	:'acceptButtonHandler'
		},{
			xtype: 'ekacacapp-button', 
			text: 'Reject',
			style:'margin:3px 10px 0px 0px',
			itemId:'rejectButtonItemId' ,
			perm_code : 'STD_APP_BIDS_REJECT',
			cls: 'button-primary tabpanel-button',
			disabled : true,
			scope : 'controller',
			handler	:'rejectButtonHandler'
		},{
			xtype: 'ekacacapp-button', 
			text: 'Counter',
			style:'margin:3px 10px 0px 0px',
			itemId:'counterButtonItemId' ,
			perm_code : 'STD_APP_BIDS_COUNTER',
			cls: 'button-primary tabpanel-button',
			disabled : true,
			scope : 'controller',
			handler	:'counterButtonHandler'
		}*/]
	},
    initComponent: function() {
		Ext.apply(this, {
			store : new Ext.data.Store({
				fields : [{
					name: 'updatedDate', 
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
			text: 'Ref Id',
			flex : 0.5,
			dataIndex: 'refId',
			renderer : function(value, column, record){
				return '<a href="#">' +record.get('refId') + '</a>';
			}
		},{
            text : 'Bid Id',
            dataIndex : 'bidId',
			flex : 0.3,
            filter : {
				type: 'list'
			},
			multiValueTextData : true
		},{
			text : "Bidder's Id",
			dataIndex : 'customerId',
			flex : 0.5,
            filter : {
				type: 'list'
			},
			multiValueTextData : true
		},{
			text : "Applicable Role(s)",
			dataIndex : 'applicableRoles',
			flex : 0.5,
            filter : {
				type: 'list'
			},
			multiValueTextData : true,
			renderer : function(value, column, record){
				return "<span title='"+record.get('applicableRoles')+"'>" + record.get('applicableRoles') + "</span>";
			}
		},{
			text : "Agent's Id",
			dataIndex : 'agentId',
			flex : 0.5,
            filter : {
				type: 'list'
			},
			multiValueTextData : true,
			renderer : function(value, column, record){
				return record.get('agentId') ? record.get('agentId') : 'NA';
			}
		},{
			text : 'Quality',
            dataIndex : 'quality',
			flex : 0.5,
            filter : {
				type: 'list'
            },
			multiValueTextData : true
		},{
			text : 'Quantity',
            dataIndex : 'quantity',
			flex : 0.3,
			filter : {
				type: 'number'
			}
		},{
			text : 'Location',
			dataIndex : 'location',
			flex : 0.4,
            filter : {
				type: 'list'
			},
			multiValueTextData : true
		},{
			text : 'Status',
			tdCls:'bid-status-icons',
			dataIndex : 'status',
			flex : 0.4,
            renderer : function(value, column, record){
				var html = '';
				//use pending On field
				if(value == 'Accepted'){
					html = "<span title='"+value+"' >"+'<img src = "classic/resources/images/farmerConnect/bid-success.png" alt = "'+ value +'" />'+"</span>";
				}else if(value == 'Rejected'){
					html = "<span title='"+value+"' >"+'<img src = "classic/resources/images/farmerConnect/bid-rejected.png" alt = "'+ value +'" />'+"</span>";
				}else if(value == 'Cancelled'){
					html = "<span title='"+value+"' >"+'<img src = "classic/resources/images/farmerConnect/bid-cancelled.png" alt = "'+ value +'" />'+"</span>";
				}else if(record.data.pendingOn == 'Offeror'){
					html = "<span title='Received' >"+'<img src = "classic/resources/images/farmerConnect/bid-received.png" alt = "'+ value +'" />'+"</span>";
				}else if(record.data.pendingOn == 'Bidder'){
					html = "<span title='Sent' >"+'<img src = "classic/resources/images/farmerConnect/bid-sent.png" alt = "'+ value +'" />'+"</span>";
				}return html;
			}
		},{
			text : 'Published Price',
			dataIndex : 'publishedPrice',
			flex : 0.4,
            filter : {
				type: 'number'
			}
		},{
			text : 'Bidder Price',
			dataIndex : 'latestBidderPrice',
			flex : 0.4,
            filter : {
				type: 'number'
			}
		},{
			text : 'Offeror Price',
			dataIndex : 'latestOfferorPrice',
			flex : 0.4,
            filter : {
				type: 'number'
			}
		},{
			text : 'Price Unit',
			dataIndex : 'priceUnit',
			flex : 0.5,
            filter : {
				type: 'list'
			},
			multiValueTextData : true
		},{
			dataIndex: 'updatedDate',
			text: 'Updated Date',
			flex : 0.5,
			xtype: 'datecolumn',
			format: GlobalConstants.timeZoneConstants.DATE_FORMAT,
			filter: {
				type: 'date'
			}
		}];
	},
	
	refreshHandler : function(){
		var response = EkaAjaxService.performSyncAjaxCall('/spring/bids/offeror',{
			method : 'GET',
			params : {
				requestParams : Ext.encode({
					sortBy : "",
					filters : "",
					pagination : ""
				})
			}
		});
		this.store.loadData(response);
	}
});
