Ext.define('EkaSmartPlt.farmerconnect.view.bid.BidEditView', {
	extend: 'Ext.Panel',
	alias : 'widget.ekasmartplt-farmerconnect-view-bid-bideditview',
	controller : 'ekasmartplt-farmerconnect-controller-bid-bideditcontroller',
	requires : ['EkaSmartPlt.ekacac.farmerconnect.controller.bid.BidEditController',
				'EkaSmartPlt.farmerconnect.view.bid.BidRejectConfirmationPanel'],
	layout : 'vbox',
	//cls : 'tbar-normal',
	/*
		// bid JSON structure
		bidJSON = {
			bidId : '12BD56',
			quality : 'SLN795',
			location : 'Chikmagalur',
			incoTerm : 'CIF',
			publishedPrice : 136,
			priceUnit : 'Rs/Kg',
			offerorName : 'Prateek M',
			offerorRating : 4.5,
			offerorPhone : '+91 20 7123 1234',
			quantity : 500,
			quantityUnit : 'Kg',
			shipmentDate : 1567449000000
		}
	*/
	initialize : function(){
		EkaLoader.mask(Localization.GENERAL.LOADING_MASK);
		var me = this;
		me.add([{
			xtype : 'toolbar',
			cls : 'footer-toolbar',
			height : 54,
			docked : 'bottom',
			items : [{
				xtype: 'button',
				text : Localization.GENERAL.CANCEL,
				cls : 'button-transparent-background button-color-blue',
				handler : 'backButtonHandler',
				flex : 1,
				scope : 'controller'
			},
			{
				xtype: 'button',
				text : Localization.GENERAL.ACCEPT,
				cls : 'button-blue',
				flex : 1,
				itemId : 'acceptButtonItemId',
				handler : 'acceptButtonHandler',
				scope : 'controller'
			}]
		}]);
		// if(this.componentData.bidJSON.refId){
		// 	BidService.getBidDetailsByRefId(this.componentData.bidJSON.refId, function(bidJSON){
		// 		me.bidJSON = bidJSON;
		// 		me.addItems();
		// 		EkaLoader.unmask();
		// 	}, function(error){
		// 		// handle error
		// 		EkaLoader.unmask();
		// 	});
		// } else {
						// uncomment when api is available
						/*BidService.getPublishedBidByBidId(this.componentData.bidId, function(bidJSON){
							me.bidJSON = bidJSON;
							me.addItems();
							EkaLoader.unmask();
						}, function(error){
							// handle error
							EkaLoader.unmask();
						});*/
			me.bidJSON = this.componentData.bidJSON;
			me.addItems();
			EkaLoader.unmask();
		// }
		
		this.callParent(arguments);
	},
	
	addItems : function(){
		var shipmentDate = new Date();
		if(this.bidJSON.shipmentDateInMillis){
			shipmentDate = new Date(this.bidJSON.shipmentDateInMillis);
		}
		var isCreate = (this.bidJSON.status == 'In-Progress' && this.bidJSON.pendingOn == 'Bidder');
		this.add({
			xtype:'titlebar',
			cls:'secondary-titlebar',
			docked: 'top',
			style : 'overflow: visible;',
			height : 50,
			items: [{
				xtype: 'button',
				handler: 'backButtonHandler',
				scope : 'controller',
				width:30,
				height:40,
				cls: 'button-image',
				iconCls: 'icon-close'
			},{
				xtype : 'label',
				html : this.bidJSON.bidId,
				cls:'card-title'
			},{
				xtype : 'button',
				text : '...',
				align : 'right',
				hidden : !isCreate,
				cls:'button-transparent-background button-color-blue button-dotted',
				arrow: false,
				menu: [{
					 text: Localization.FARMER_CONNECT.BID_LOGS,
					 bidJSON : this.bidJSON,
					 handler : 'onBidLogsHandler'
				},{
					text: Localization.FARMER_CONNECT.BID_REJECT,
					bidJSON : this.bidJSON,
					handler : 'onBidRejectHandler'
				}]
			}],
		});
		var items = this.getStaticContent();
		items = items.concat([{
			xtype : 'container',
			layout : 'vbox',
			hidden : isCreate,
			margin : '10 5 0 5',
			items : [{
				xtype : 'label',
				html : Localization.FARMER_CONNECT.QUANTITY + ':'
			},{
				xtype : 'container',
				layout : 'hbox',
				items : [{
					xtype : 'numberfield',
					itemId : 'quantityItemId',
					flex : 2,
					minValue: 1,
					maxValue : this.bidJSON.quantity,
					value : this.bidJSON.quantity,
					emptyText : Localization.FARMER_CONNECT.QUANTITY,
					decimals : 5,
					allowBlank : false
				},{
					xtype : 'label',
					flex : 1,
					style:'margin-left:5px',
					// checking if quantity unit is not there, taking unit from price unit
					html : this.bidJSON.quantityUnit ? this.bidJSON.quantityUnit: this.bidJSON.priceUnit.split('/')[1]
				}]
			}]
		},{
			xtype : 'datepickerfield',
			margin : '0 5 0 5',
			itemId : 'shipmentDateItemId',
			label : Localization.FARMER_CONNECT.SHIPMENT_DATE + ':',
			hidden : isCreate,
			value: shipmentDate,
			minDate : new Date(),
			allowBlank : false,
			labelAlign : 'top',
			picker : {
				xtype : 'datepicker',
				yearFrom: new Date().getFullYear(),
				yearTo: new Date().getFullYear() + 50
			}
		},{
			xtype : 'checkboxfield',
			margin : '0 5 0 5',
			itemId : 'counterCheckboxItemId',
			label : Localization.FARMER_CONNECT.COUNTER,
			width : 150,
			labelAlign : 'right',
			listeners : {
				change : 'onCounterCheckChange',
				scope : 'controller'
			}
		},{
			xtype : 'container',
			layout : 'hbox',
			margin : '0 5 0 5',
			items : [{
				xtype : 'numberfield',
				itemId : 'counterValueItemId',
				flex : 2,
				disabled : true,
				decimals : 5,
				minValue: 0.01,
				allowBlank : false
			},{
				xtype : 'label',
				flex : 1,
				style:'margin-left:5px',
				html : this.bidJSON.priceUnit
			}]
		},
		{
			xtype : 'textareafield',
			margin : '0 5 20 5',
			height : 120,
			value : this.bidJSON.remarks ? this.bidJSON.remarks : '',
			itemId : 'remarksItemId',
			label : Localization.FARMER_CONNECT.REMARKS+ ':',
			labelAlign : 'top'
		}]);
		
		this.add({
			xtype : 'container',
			height : window.innerHeight - 100, //total height - title bar height
			scrollable : true,
			layout : 'vbox',
			style:'padding:0 10px',
			cls:'container-white common-form',
			items : items
		});
	},
	
	getStaticContent : function(){
		var isCreate = (this.bidJSON.status == 'In-Progress' && this.bidJSON.pendingOn == 'Bidder');
		return [{
				xtype : 'container',
				margin : '10 0 0 0',
				layout : 'hbox',
				items : [{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : Localization.FARMER_CONNECT.PRODUCT + ':'
				},{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : this.bidJSON.product
				}]
			},{
				xtype : 'container',
				layout : 'hbox',
				items : [{
					xtype : 'label',
					margin : '0 0 2 5',
					flex : 1,
					html : Localization.FARMER_CONNECT.QUALITY + ':'
				},{
					xtype : 'label',
					margin : '0 0 2 5',
					flex : 1,
					html : this.bidJSON.quality
				}]
			},{
				xtype : 'container',
				layout : 'hbox',
				items : [{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : Localization.FARMER_CONNECT.LOCATION + ':'
				},{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : this.bidJSON.location
				}]
			},{
				xtype : 'container',
				layout : 'hbox',
				items : [{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : Localization.FARMER_CONNECT.INCO_TERM + ':'
				},{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : this.bidJSON.incoTerm
				}]
			},{
				xtype : 'container',
				layout : 'hbox',
				items : [{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : Localization.FARMER_CONNECT.CROP_YEAR + ':'
				},{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : this.bidJSON.cropYear
				}]
			},{
				xtype : 'container',
				layout : 'hbox',
				items : [{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : Localization.FARMER_CONNECT.BID_ID + ':'
				},{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : this.bidJSON.bidId
				}]
			},{
				xtype : 'container',
				layout : 'hbox',
				items : [{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : Localization.FARMER_CONNECT.OFFER_TYPE + ':'
				},{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : this.bidJSON.offerType ? this.bidJSON.offerType : Localization.FARMER_CONNECT.PURCHASE
				}]
			},{
				xtype : 'container',
				layout : 'hbox',
				hidden : isCreate,
				items : [{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : Localization.FARMER_CONNECT.PUBLISHED_BID_PRICE + ':'
				},{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : this.bidJSON.publishedPrice + ' ' + this.bidJSON.priceUnit
				}]
			},{
				xtype : 'container',
				layout : 'hbox',
				items : [{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : Localization.FARMER_CONNECT.OFFEROR_NAME + ":"
				},{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : this.bidJSON.offerorName ? this.bidJSON.offerorName :  Localization.FARMER_CONNECT.NOT_AVAILABLE
				}]
			},{
				xtype : 'container',
				layout : 'hbox',
				items : [{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : Localization.FARMER_CONNECT.OFFEROR_RATING + ':'
				},{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : this.bidJSON.rating ? this.bidJSON.rating :  Localization.FARMER_CONNECT.NOT_AVAILABLE
				}]
			},{
				xtype : 'container',
				layout : 'hbox',
				items : [{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : Localization.FARMER_CONNECT.OFFEROR_PHONE_NO + ":"
				},{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : this.bidJSON.offerorMobileNo ? 
						('<a href="tel:' + this.bidJSON.offerorMobileNo + '">' + this.bidJSON.offerorMobileNo + '</a>') :
						Localization.FARMER_CONNECT.NOT_AVAILABLE
				}]
			},{
				xtype : 'container',
				hidden : !isCreate,
				layout : 'hbox',
				items : [{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : Localization.FARMER_CONNECT.QUANTITY + ':'
				},{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : this.bidJSON.quantity + " "+ (this.bidJSON.quantityUnit ? this.bidJSON.quantityUnit: this.bidJSON.priceUnit.split('/')[1]) ,
				}]
			},{
				xtype : 'container',
				hidden : !isCreate,
				layout : 'hbox',
				items : [{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html :  Localization.FARMER_CONNECT.SHIPMENT_DATE + ':',
				},{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : Ext.Date.format(new Date(this.bidJSON.shipmentDateInMillis), 'F jS, Y')
				}]
			},{
				xtype : 'container',
				cls:'bid-info-table',
				margin : '10 5 0 5',
				hidden : !isCreate,
				items : [{
					xtype : 'container',
					layout : 'hbox',
					items : [{
						xtype : 'label',
						margin : '2 0 2 5',
						flex : 1,
						html : Localization.FARMER_CONNECT.PUBLISHED_BID_PRICE + ':'
					},{
						xtype : 'label',
						margin : '2 0 2 5',
						flex : 1,
						html : '<b>' + (this.bidJSON.publishedPrice + " " + this.bidJSON.priceUnit) + '</b>'
					}]
				},{
					xtype : 'container',
					layout : 'hbox',
					items : [{
						xtype : 'label',
						margin : '2 0 2 5',
						flex : 1,
						html : Localization.FARMER_CONNECT.LATEST_BIDDER_PRICE + ':'
					},{
						xtype : 'label',
						margin : '2 0 2 5',
						flex : 1,
						html : '<b>' + (this.bidJSON.latestBidderPrice ? this.bidJSON.latestBidderPrice  +  " " + this.bidJSON.priceUnit : Localization.FARMER_CONNECT.NOT_INITIATED) + '</b>'
					}]
				},{
					xtype : 'container',
					layout : 'hbox',
					items : [{
						xtype : 'label',
						margin : '2 0 2 5',
						flex : 1,
						html : Localization.FARMER_CONNECT.LATEST_OFFEROR_PRICE + ':'
					},{
						xtype : 'label',
						margin : '2 0 2 5',
						flex : 1,
						html : '<b>' + (this.bidJSON.latestOfferorPrice ? this.bidJSON.latestOfferorPrice +  " " + this.bidJSON.priceUnit: Localization.FARMER_CONNECT.NOT_INITIATED) + '</b>'
					}]
				}]
		}]
	}
});