Ext.define('EkaSmartPlt.farmerconnect.view.bid.ListOfPublishedPrices', {
    extend: 'Ext.dataview.DataView',
    xtype : 'ekasmartplt-farmerconnect-view-bid-listofpublishedprices',
	requires : ['EkaSmartPlt.ekacac.farmerconnect.controller.bid.ListOfPublishedPricesController',
				'EkaSmartPlt.farmerconnect.view.offer.OfferPreview'
			],
    controller : 'ekasmartplt-farmerconnect-controller-bid-listofpublishedpricescontroller',
    listeners : {
        childtap : 'onChildTap',
		painted : 'onViewPainted',
        scope : 'controller'
    },
    scrollable : 'y',
    layout : 'vbox',
	cls:'bid-listing',
	style:'padding:10px',
    itemSelector : 'div.item',
	initialize : function(){
		var tpl = Ext.create('Ext.XTemplate',
				'<div class="item">' ,
				'<tpl if="values.length == \'0\'">',
					'<div class="cancelled-message">'+ Localization.FARMER_CONNECT.EMPTY_TEXT +'</div>',
				'<tpl else >',
					'<div class="bid-list-item-header">',
						'<span class="bid-header-items order-data item-id">{[this.getOfferType(values.offerType)]} | {[this.getEllipsis(values.bidId,15)]} | {[this.getEllipsis(values.incoTerm,6)]}</span>',
						//'<span class="bid-header-items item-inco"><span class="text-grey-background">{incoTerm}</span></span>',
						// '<span class="bid-header-items item-expiry">Exp. :&nbsp;{expiresIn}</span>',
						'<span class="bid-header-items item-expiry">{[this.getExpriesContent(values.expiresIn)]}</span>',
					'</div>',
					'<div class="bid-list-item-details">',
						'<div class="bid-detail-row bdr-row1">',
							'<span class="bid-item-values">'+ Localization.FARMER_CONNECT.LOCATION +'<br><span class="value">{location}</span></span>',
							'<span class="bid-item-values">'+ Localization.FARMER_CONNECT.QUALITY +'<br><span class="value">{quality}</span></span>',
							'<span class="bid-item-values">'+ Localization.FARMER_CONNECT.CROP_YEAR +'<br><span class="value">{cropYear}</span></span>',						
						'</div>',
						'<div class="bid-detail-row bdr-row2">',
							'<span class="bid-item-values">'+ Localization.FARMER_CONNECT.QUANTITY +'<br><span class="value">{quantity}&nbsp;{quantityUnit}</span></span>',
							'<span class="bid-item-values">'+ Localization.FARMER_CONNECT.SHIPMENT_PERIOD +'<br><span class="value">{[this.getFormattedShipmentDate(values.shipmentDateInMillis)]}</span></span>',
						'</div>', 
						'<div class="bid-detail-row bdr-row3">',
							'<span class="published-price">'+ Localization.FARMER_CONNECT.PUBLISHED_PRICE +' <span class="value">{publishedPrice} {priceUnit}</span></span>',
						'</div>',
					'</div>',
					'<div class="bid-list-item-footer">',
						'<span class="bid-footer-items bid-item-user">{offerorName}</span>',
						// '<span class="bid-footer-items item-align-right bid-item-rating">Rating:{rating}</span>',
						'<span class="bid-footer-items item-align-right bid-item-rating">'+ Localization.FARMER_CONNECT.RATING +':{[this.getRating(values)]}</span>',			
					'</div>' ,
				'</tpl>',
			'</div>',
			{
				getFormattedShipmentDate : function(shipmentDateInMillis){
					return  Ext.Date.format(new Date(shipmentDateInMillis), 'M-Y');
				},
				getExpriesContent : function(expiresIn){
					var staticContent = Localization.FARMER_CONNECT.EXPIRES + " " + Localization.FARMER_CONNECT.IN;
					if(expiresIn == 'Today' || expiresIn == 'Tomorrow')
						staticContent = Localization.FARMER_CONNECT.EXPIRES;
					return staticContent+ expiresIn;
				},
				getRating : function(values){
					if(!values.offerorName){
						return Localization.FARMER_CONNECT.NOT_AVAILABLE;
					}
					return values.rating
				},
				getEllipsis : function(value, count){
					if(value){
						value = value.substring(0, count);
						if (value.length >= count) {
							return value+ '...';
						}
					}	
					return value;
				},
				getOfferType : function(offerType){
					if(!offerType)
						return Localization.FARMER_CONNECT.PURCHASE;
					return offerType;
				}

			});
		this.setItemTpl(tpl);
        this.callParent(arguments);
	}
});