Ext.define('EkaSmartPlt.ekacac.farmerconnect.view.common.RatingWindow', {
    extend: 'Ext.window.Window', 
    height:500,
    width:600,
	closable:true,
	header: false,
    cls: 'smart-window custom-form-panel',
    modal : true,
    controller:"ekasmartplt-farmerconnect-controller-common-ratingwindowcontroller",
    requires:["EkaSmartPlt.ekacac.farmerconnect.controller.common.RatingWindowController"],
    onBoxReady : function(){
		var bidJSON = this.componentData;
		this.parentComponent.hide();
		var items = [{			
			xtype : 'label',
			style:'margin-top:16px;',
			cls:'bids-rating-text',
			html  : 'How was your recent transaction with ' + '<br><span class="bids-rating-user-name">' + bidJSON.offerorName +'</span>'
		},{
			xtype : 'segmentedbutton',
			allowMultiple:true,
			cls:'rating-button-container',
			defaults : {
				handler : 'onRatingHandler',
			    cls:'button-transparent-bg',
			 	scope : 'controller'
			},
			items: [{
				itemId : 'buttonItemId1',
				//custom field
				buttonPosition : 1,
				text: '',
				iconCls:'rating-star-icon'		
		   },{
			itemId : 'buttonItemId2',
			//custom field
			buttonPosition : 2,
			text: '',
			iconCls:'rating-star-icon'	
		   },{
			itemId : 'buttonItemId3',
			//custom field
			buttonPosition : 3,
			text: '',
			iconCls:'rating-star-icon'	
		   },{
			itemId : 'buttonItemId4',
			//custom field
			buttonPosition : 4,
			text: '',
			iconCls:'rating-star-icon'	
		   },{
			itemId : 'buttonItemId5',
			//custom field
			buttonPosition : 5,
			text: '',
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
			html : "What Went Bad.",
			style:'margin-top:20px;',
			hidden : true,
			cls:'bids-rating-text',
			itemId : 'ratingQuestionItemId'
		},{
			xtype : 'segmentedbutton',
			allowMultiple:true,
			style:'margin-top:20px',	
			cls:'feedback-button-container',			
		
			defaults : {
				handler : 'onFeedbackHandler',				
				scope : 'controller',
				style: 'margin-right:6px;'
			},
			items : [{
				itemId : 'pricingButtonItemId',
				text : 'Pricing'
			},{
				itemId : 'quantityButtonItemId',
				text : 'Quantity'
			},{
				itemId : 'qualityButtonItemId',
				text : 'Quality'
			},{
				itemId : 'shipmentButtonItemId',
				text :'Shipment'
			},{
				itemId : 'allButtonItemId',
				text :'All'
			}]
		},{
			xtype : 'textareafield',
			style:'margin-top:20px',
			width:510,
			height:40,
			placeholder: 'REMARKS',
			emptyText :'Remarks',
			itemId : 'remarksItemId'
		}
		/*,{
			xtype: 'container',
            itemId : 'slicerButtonsItemId',	
            layout:'hbox',
			items: [{
				xtype : 'button',
				itemId : 'laterButtonItemId',
				handler : 'laterButtonHandler',
				text: 'Wil Do it Later',
				scope : 'controller'
			},			
			{
				xtype : 'button',
				itemId : 'doneButtonItemId',
				handler : 'doneButtonHandler',
				text: 'Done',
				scope : 'controller'
			}]
		}*/
	];

		
		this.add({
			xtype : 'container',
			height : window.innerHeight - 50, //total height - title bar height
			scrollable : true,
			padding:10,
			items : items,
			layout: {
                align: 'middle',
                //pack: 'center',
                type: 'vbox'
			}
			
			
		});
		this.callParent(arguments);
	},
	buttons: [{
		xtype : 'button',
		itemId : 'laterButtonItemId',
		handler : 'laterButtonHandler',
		text: 'WILL DO IT LATER',
		cls : 'button-inverse',
		scope : 'controller'
	},
	{
		xtype: 'tbfill'
	}	,		
	{
		xtype : 'button',
		itemId : 'doneButtonItemId',
		cls: 'button-others',
		handler : 'doneButtonHandler',
		text: 'DONE',
		scope : 'controller'
	}]
});