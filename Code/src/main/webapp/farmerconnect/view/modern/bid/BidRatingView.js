Ext.define('EkaSmartPlt.farmerconnect.view.bid.BidRatingView', {
	extend: 'Ext.Panel',
	alias : 'widget.ekasmartplt-farmerconnect-view-bid-bidratingview',
	controller : 'ekasmartplt-farmerconnect-controller-bid-bidratingcontroller',
	requires : ['EkaSmartPlt.ekacac.farmerconnect.controller.bid.BidRatingController'],
	layout : 'vbox',
	cls:'setup-screen background-white tbar-normal tbar-white',
	scrollable : true,
	initialize : function(){
		var bidJSON = this.componentData.bidJSON;
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
				iconCls: 'icon-close'
			},{
				xtype : 'label',
				html : Localization.FARMER_CONNECT.RATING + ' (' + bidJSON.bidId + ' - ' + bidJSON.refId + ')',
				cls:'card-title'
			}]
		});
		this.add({
			xtype: 'toolbar',
			itemId : 'slicerButtonsItemId',	
			cls:'footer-toolbar',
			height:54,
			docked: 'bottom',
			items: [			
			{
				xtype : 'button',
				width:'100%',
				itemId : 'doneButtonItemId',
				handler : 'doneButtonHandler',
				text: 'Done',
				disabled : true,
				cls:'button-blue',
				scope : 'controller'
			}]
		})
		
		var items = [{			
			xtype : 'label',
			style:'margin-top:16px;',
			cls:'bids-rating-text',
			html  : Localization.FARMER_CONNECT.RATE_EXPERIENCE_QUESTION + '<br> <span class="bids-rating-user-name">' + bidJSON.offerorName +'</span>'
		},{
			xtype : 'container',
			cls:'rating-button-container',
			layout: {
				type: 'hbox',
				align: 'center',
				pack: 'center'
			}, 
			defaults : {
				xtype : 'button',
				allowDepress : false,
				handler : 'onRatingHandler',
				cls:'button-transparent',
				scope : 'controller'
			},
			items : [{
				itemId : 'buttonItemId1',
				//custom field
				buttonPosition : 1,
				text : '',
				iconCls:'rating-star-icon'
			},{
				itemId : 'buttonItemId2',
				//custom field
				buttonPosition : 2,
				text : '',
				iconCls:'rating-star-icon'
			},{
				itemId : 'buttonItemId3',
				//custom field
				buttonPosition : 3,
				text : '',
				iconCls:'rating-star-icon'
			},{
				itemId : 'buttonItemId4',
				//custom field
				buttonPosition : 4,
				text : '',
				iconCls:'rating-star-icon'
			},{
				itemId : 'buttonItemId5',
				//custom field
				buttonPosition : 5,
				text : '',
				iconCls:'rating-star-icon'
			}]
		},{
			xtype : 'label',
			style:'margin-top:20px;',
			itemId : 'ratingValueItemId',
			cls:'bids-rating-value',
			html : 'Please Rate'
		},{
			xtype : 'label',
			html : Localization.FARMER_CONNECT.RATE_WHAT_WENT_BAD,
			style:'margin-top:20px;',
			hidden : true,
			cls:'bids-rating-text',
			itemId : 'ratingQuestionItemId'
		},{
			xtype : 'container',
			style:'margin-top:20px',	
			cls:'feedback-button-container',			
			layout: {
				type: 'hbox',
				align: 'center',
				pack: 'center'
			}, 
			defaults : {
				xtype : 'button',
				allowDepress : false,
				handler : 'onFeedbackHandler',				
				scope : 'controller'
			},
			items : [{
				itemId : 'pricingButtonItemId',
				text : Localization.FARMER_CONNECT.PRICING
			},{
				itemId : 'quantityButtonItemId',
				text : Localization.FARMER_CONNECT.QUANTITY
			},{
				itemId : 'qualityButtonItemId',
				text : Localization.FARMER_CONNECT.QUALITY
			}]
		},{
			xtype : 'container',
			style:'margin-top:6px',
			cls:'feedback-button-container',							
			layout: {
				type: 'hbox',
				align: 'center',
				pack: 'center'
			}, 
			defaults : {
				xtype : 'button',
				allowDepress : false,
				handler : 'onFeedbackHandler',
				scope : 'controller'
			},
			items : [{
				itemId : 'shipmentButtonItemId',
				text : Localization.FARMER_CONNECT.SHIPMENT
			},{
				itemId : 'allButtonItemId',
				text : Localization.FARMER_CONNECT.ALL
			}]
		},{
			xtype : 'textareafield',
			style:'padding:10px;margin-top:10px',
			placeholder: Localization.FARMER_CONNECT.REMARKS,
			emptyText : Localization.FARMER_CONNECT.REMARKS,
			itemId : 'remarksItemId'
		}];
		
		this.add({
			xtype : 'container',
			height : window.innerHeight - 50, //total height - title bar height
			scrollable : true,
			layout : 'vbox',
			items : items
		});
		this.callParent(arguments);
	}
});