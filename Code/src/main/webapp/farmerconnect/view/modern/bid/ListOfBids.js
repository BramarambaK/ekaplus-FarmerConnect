Ext.define('EkaSmartPlt.farmerconnect.view.bid.ListOfBids', {
    extend: 'Ext.dataview.DataView',
    xtype : 'ekasmartplt-farmerconnect-view-bid-listofbids',
	requires : ['EkaSmartPlt.ekacac.farmerconnect.controller.bid.ListOfBidsController',
                 'EkaSmartPlt.farmerconnect.view.offer.OfferPreview'],
    controller : 'ekasmartplt-farmerconnect-controller-bid-listofbidscontroller',
    listeners : {
        select : 'onChildTap',
        painted : 'onViewPainted',
        scope : 'controller'
    },
    scrollable : 'y',
    layout : 'vbox',
	cls:'bid-listing',
	style:'padding:0px 10px 10px 10px;',
	itemSelector : 'div.item',
	initialize : function(){
		var _this = this;		
		var tpl = Ext.create('Ext.XTemplate',
		'<div class="item">',
				'<tpl if="values.status == \'Cancelled\'">',
					'<div class="bid-box cancelled-bid">',
					// '<div class="cancelled-message">'+ Localization.FARMER_CONNECT.CANCELLED + '</div>',
					'<div class="cancelled-message">'+ Localization.FARMER_CONNECT.CANCELLED_WATERMARK + '</div>',
				'<tpl else >',
					'<div class="bid-box">',					
				'</tpl>',			
				'<div class="bid-list-item-header">',
					'<tpl if="values.status == \'In-Progress\'">',
						'<span class="bid-status-icon {[this.getUserRole(values.pendingOn)]}"></span>',
					'<tpl else >',
						'<span class="bid-status-icon {[this.getStatusIcon(values.status)]} {pendingOn}"></span>',
					'</tpl>',
					'<span class="bid-header-items order-data item-id">{[this.getOfferType(values.offerType)]} | {[this.getEllipsis(values.refId,15)]} | {[this.getEllipsis(values.incoTerm,6)]}</span>',
					//'<span class="bid-header-items item-inco"><span class="text-grey-background">{incoTerm}</span></span>',
				'</div>' ,
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
				'</div>',
				'<div class="bid-list-item-footer">',
					'<span class="bid-footer-items bid-item-user">{offerorName}</span>',
					// '<span class="bid-footer-items item-align-right bid-item-rating">Rating:{rating}</span>',
					'<span class="bid-footer-items item-align-right bid-item-rating">'+ Localization.FARMER_CONNECT.RATING +':{[this.getRating(values)]}</span>',
				'</div>' ,
				'</div>',
		'</div>',
		{
			
			getFormattedShipmentDate : function(shipmentDateInMillis){
				return  Ext.Date.format(new Date(shipmentDateInMillis), 'M-Y');
			},
			getRating : function(values){
				if(!values.offerorName){
					return Localization.FARMER_CONNECT.NOT_AVAILABLE;
				}
				return values.rating
			},
			getStatusIcon : function(status){
				if(status == "Cancelled")
					return "Accepted";
				return status;
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
			},
			getUserRole : function(pendingOn){
				if(FarmerConnectConfigurationObject.role=="Offerer"){
					if(pendingOn=="Bidder")
					return 'pendingOther';
					else
					return 'pendingOwn';

				}
				else{
					if(pendingOn=="Bidder")
					return 'pendingOwn';
					else
					return 'pendingOther';


				}
			},
		});
		this.setItemTpl(tpl);
		this.setEmptyText(Localization.FARMER_CONNECT.EMPTY_TEXT);
        this.callParent(arguments);
	}
 });