Ext.define('EkaSmartPlt.farmerconnect.view.bid.BidDetailView', {
	extend: 'Ext.Panel',
	alias : 'widget.ekasmartplt-farmerconnect-view-bid-biddetailview',
	controller : 'ekasmartplt-farmerconnect-controller-bid-biddetailcontroller',
	requires : ['EkaSmartPlt.ekacac.farmerconnect.controller.bid.BidDetailController',
					'EkaSmartPlt.farmerconnect.view.bid.BidLogsView'],
	layout : 'vbox',
	cls : 'tbar-normal',
	scrollable : true,
	
	/*
		// bid JSON structure
		bidJSON = {
			bidId : '12BD56',
			quality : 'SLN795',
			location : 'Chikmagalur',
			incoTerm : 'CIF',
			publishedPrice : 136,
			priceUnit : 'Rs/Kg',
			offerorName : 'Nepa Kumar',
			offerorRating : 4.5,
			offerorPhone : '+91 20 7123 1234',
			quantity : 500,
			quantityUnit : 'Kg',
			shipmentDate : 1567449000000,
			updatedDate : 1567449000000,
			latestRemarks : 'Remarks'
		}
	*/
	initialize : function(){
		var me = this;
		EkaLoader.mask(Localization.GENERAL.LOADING_MASK);
		me.addItems(this.componentData.bidJSON);
		EkaLoader.unmask();
		// BidService.getBidDetailsByRefId(this.componentData.refId, function(bidJSON){
		// 	me.addItems(bidJSON);
		// 	EkaLoader.unmask();
		// }, function(error){
		// 	//handle error
		// 	EkaLoader.unmask();
		// 	EkaToast.show('Unable to fetch Bid Details');
		// });
		eatracker.trackEvent(EkaGoogleAnayticsUtil.pageTrackingMap.FARMER_CONNECT, EkaGoogleAnayticsUtil.constants.BID_DETAIL);
		this.callParent(arguments);
	},
	
	addItems : function(bidJSON){
		var isAccepted = bidJSON.status == 'Accepted';
		var isCancelled = bidJSON.status == 'Cancelled';
		var isRejected = bidJSON.status == 'Rejected';
		this.add({
			xtype: 'titlebar',
			docked: 'top',
			itemId : 'headerTitleItemId',
			cls:'secondary-titlebar',
			//title: bidJSON.bidId + ' - ' +  bidJSON.refId,
			style : 'overflow: visible;',
			height : 50,
			items: [{
				xtype: 'button',
				handler: 'backButtonHandler',
				scope : 'controller',
				width:30,
				height:40,
				cls: 'button-image',
				style:'margin-right:0px;',
				iconCls: 'icon-back-blue'
			},{
				xtype : 'label',
				html : bidJSON.refId,
				cls:'card-title'
			},{
				xtype : 'button',
				text : '...',
				align : 'right',
				cls:'button-transparent-background button-color-blue button-dotted',
				arrow: false,
				menu: [{
					 text: Localization.FARMER_CONNECT.BID_LOGS,
					 bidJSON : bidJSON,
					 handler : 'onBidLogsHandler'
				}]
			}]
		});
		var items = this.getStaticContent(bidJSON);
		items.push({
			xtype : 'container',
			cls:'bid-info-table',
			margin : '10 5 0 5',
			items : [{
				xtype : 'container',
				layout : 'hbox',
				items : [{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : Localization.FARMER_CONNECT.PUBLISHED_PRICE + ':'
				},{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : '<b>' + (bidJSON.publishedPrice ? (bidJSON.publishedPrice + ' ' + bidJSON.priceUnit) : '') + '</b>'
				}]
			},{
				xtype : 'container',
				layout : 'hbox',
				// hidden : isRejected,
				items : [{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : Localization.FARMER_CONNECT.LATEST_BIDDER_PRICE + ':'
				},{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : '<b>' + (bidJSON.latestBidderPrice ? (bidJSON.latestBidderPrice + ' ' + bidJSON.priceUnit) :  Localization.FARMER_CONNECT.NOT_INITIATED) + '</b>'
				}]
			},{
				xtype : 'container',
				layout : 'hbox',
				// hidden : isRejected,
				items : [{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : Localization.FARMER_CONNECT.LATEST_OFFEROR_PRICE + ':'
				},{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : '<b>' + (bidJSON.latestOfferorPrice ? (bidJSON.latestOfferorPrice + ' ' + bidJSON.priceUnit) :  Localization.FARMER_CONNECT.NOT_INITIATED) + '</b>'
				}]
			}]
		});
		var acceptedPrice = bidJSON.updatedBy == 'Bidder' ? (bidJSON.latestOfferorPrice? bidJSON.latestOfferorPrice : bidJSON.publishedPrice) : (bidJSON.latestBidderPrice? bidJSON.latestBidderPrice: bidJSON.publishedPrice);
		var acceptedBy = bidJSON.updatedBy == 'Bidder' ? Localization.FARMER_CONNECT.YOU : Localization.FARMER_CONNECT.OFFEROR;
		var message = Localization.FARMER_CONNECT.DEAL_ACCEPETED_BY + acceptedBy + Localization.FARMER_CONNECT.ON + Ext.Date.format(new Date(bidJSON.updatedDate), 'd-M-Y') + ' at ' + acceptedPrice + " " + bidJSON.priceUnit;
		if(!isAccepted)
			message = Localization.FARMER_CONNECT.DEAL_REJECTED_BY + acceptedBy + Localization.FARMER_CONNECT.ON + Ext.Date.format(new Date(bidJSON.updatedDate), 'd-M-Y');
		if(isCancelled){
			message = Localization.FARMER_CONNECT.DEAL_CANCELLED_BY + acceptedBy + Localization.FARMER_CONNECT.ON + Ext.Date.format(new Date(bidJSON.updatedDate), 'd-M-Y') + ' at ' + acceptedPrice + " " + bidJSON.priceUnit;
		}
		items.push({
			xtype : 'label',
			hidden : !(isRejected || isAccepted || isCancelled),
			margin : '15 0 5 5',
			cls:'deal-message',
			html : message
		});
		this.add({
			xtype : 'container',
			style:'padding:0 10px',
			cls:'container-white common-form',
			height : window.innerHeight - 50, //total height - title bar height
			scrollable : true,
			layout : 'vbox',
			items : items
		});
	},
	
	getStaticContent : function(bidJSON){
		var me = this;
		var isAccepted = bidJSON.status == 'Accepted';
		var isUserPresent = bidJSON.username;
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
					html : bidJSON.product
				}]
			},{
				xtype : 'container',
				layout : 'hbox',
				items : [{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : Localization.FARMER_CONNECT.QUALITY + ':'
				},{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : bidJSON.quality
				}]
			},{
				xtype : 'container',
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
					html : bidJSON.quantity + " "+ (bidJSON.quantityUnit ? bidJSON.quantityUnit: bidJSON.priceUnit.split('/')[1])
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
					html : bidJSON.location
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
					html : bidJSON.incoTerm
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
					html : bidJSON.cropYear
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
					html : bidJSON.bidId
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
					html : bidJSON.offerType ? bidJSON.offerType : Localization.FARMER_CONNECT.PURCHASE
				}]
			},{
				xtype : 'container',
				layout : 'hbox',
				items : [{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : Localization.FARMER_CONNECT.REF_ID + ':'
				},{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : bidJSON.refId
				}]
			},{
				xtype : 'container',
				layout : 'hbox',
				items : [{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : Localization.FARMER_CONNECT.SHIPMENT_DATE + ':'
				},{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : Ext.Date.format(new Date(bidJSON.shipmentDateInMillis), 'd-M-Y')
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
					html : bidJSON.offerorName ? bidJSON.offerorName : Localization.FARMER_CONNECT.NOT_AVAILABLE
				}]
			},{
				xtype : 'container',
				layout : 'hbox',
				// rating should not be there if user is not there
				hidden : !isUserPresent,
				items : [{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : Localization.FARMER_CONNECT.OFFEROR_RATING + ':'
				},{
					xtype : 'label',
					margin : '2 0 2 5',
					flex : 1,
					html : bidJSON.rating 
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
					cls:'color-blue',
					margin : '2 0 2 5',
					flex : 1,
					html : bidJSON.offerorMobileNo ? 
								('<a href="tel:' + bidJSON.offerorMobileNo + '">' + bidJSON.offerorMobileNo + '</a>')
								: Localization.FARMER_CONNECT.NOT_AVAILABLE
				}]
			},{
				xtype : 'container',
				layout : 'hbox',
				hidden : !(isAccepted && isUserPresent),
				margin : '2 0 2 5',
				defaults : {
					margin : '0 0 0 2',
				},
				items : [{
					xtype : 'label',
					flex : 1,
					html : Localization.FARMER_CONNECT.YOUR_RATING + ' : '
				},{
					xtype : 'label',
					cls:'label-error',
					reference:'currentRatingReference',
					flex : bidJSON.offerorName ? (bidJSON.currentBidRating ? 1 : 0.5) :  1,
					html : bidJSON.offerorName ? (bidJSON.currentBidRating ? bidJSON.currentBidRating : Localization.FARMER_CONNECT.PENDING) : Localization.FARMER_CONNECT.NOT_AVAILABLE
				},{
					xtype : 'label',
					reference:'currentRatingButton',
					hidden : bidJSON.offerorName ? (bidJSON.currentBidRating ? true : false) :  true,
					flex : 0.5,
					cls:'hyperlink',
					html : '<a> '+ Localization.FARMER_CONNECT.RATE_NOW +' </a>',
					listeners : {
						initialize : function(label){
							this.element.on({
								tap : function(){
									me.controller.onRateNowClick(bidJSON);
								}
							});
						}
					}
				}]
		}];
	}
});