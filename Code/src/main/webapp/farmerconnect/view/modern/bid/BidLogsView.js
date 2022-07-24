Ext.define('EkaSmartPlt.farmerconnect.view.bid.BidLogsView', {
	extend: 'Ext.Panel',
	alias : 'widget.ekasmartplt-farmerconnect-view-bid-bidlogsview',
	layout : 'vbox',
	cls:'setup-screen background-white tbar-normal tbar-white',
	scrollable : true,
	
	initialize : function(){
		EkaLoader.mask(Localization.GENERAL.LOADING_MASK);
		var me = this;
		var bidJSON = this.componentData.bidJSON;
		this.add({
			xtype:'titlebar',
			cls:'secondary-titlebar',
			itemId : 'headerTitleItemId',
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
				html : Localization.FARMER_CONNECT.BID_LOG + ' (' + bidJSON.bidId + ' - ' + bidJSON.refId + ')',
				cls:'card-title'
			}]
		});
		BidService.getBidLogsByRefId(bidJSON.refId, function(bidDetail){
			var items = [];
			var store = new Ext.data.Store({
				data : me.reverseArray(bidDetail.negotiationLogs)
			});
			var priceUnit = bidDetail.priceUnit;
			me.add({
				xtype : 'container',
				height : window.innerHeight - 50, //total height - title bar height
				scrollable : true,
				layout : 'vbox',
				items : [{
					xtype : 'dataview',
					width : '100%',
					//itemSelector: 'div.thumb-wrap',
					store : store,
					padding:10,
					itemTpl : new Ext.XTemplate(
							'<div class = "item-log-flow {[this.getClassName(values)]}">',
							'<div class="image">',
								'<div class="log-flow-icon"></div>',
							'</div>',
							'<div class="details">',
								'<div>',
								'{[this.getDateAndPrice(values)]}',
								'<span>{[this.populateDetails(values)]}</span>',
								'</div>',
							'</div>',
							
							'</div>',
						{
							getDateAndPrice : function(values){
								var html = '';
								if(values.logType == 0)
									html = '<div class="log-flow-normal-label">'+ Localization.FARMER_CONNECT.PUBLISHED_BID_PRICE+'</div>';
								else {
									html += '<div class="log-flow-normal-label">'+Ext.Date.format(new Date(values.date), 'd-M-Y H:i:s T')+'</div>';
								}
								if(values.logType == 1)
									html += '<div class="log-flow-bold-label">'+ Localization.FARMER_CONNECT.ACCEPTED +'</div>';
								else if(values.logType == -1)
									html += '<div class="log-flow-bold-label">'+ Localization.FARMER_CONNECT.REJECTED + '</div>';
								else if(values.logType == -2)
									html += '<div class="log-flow-bold-label">'+ Localization.FARMER_CONNECT.CANCELLED + '</div>';
								else
									html += '<div class="log-flow-bold-label">' + values.price +' ' + priceUnit + '</div>';
								
								return  html;
							},
							populateDetails : function(values){
								if(values.logType == 0)
									return '';
								if(values.name)
									return '<span class="log-flow-normal-label">'+values.name 
										+ '</span>, ' + '<span class="log-flow-normal-label">'+ values.by 
										+ '</span>: <span class="log-flow-remarks-label">'
										+ ((values.remarks && values.remarks.length > 0)
											? values.remarks : Localization.FARMER_CONNECT.NO_REMARKS)+'</span>';
								return bidDetail.name;
							},
							getClassName : function(values){
								var className = '';
								if(values.logType == 0){
									className = 'bid-published';
								}else if(values.logType == 1){
									className = 'bid-success';
								}else if(values.logType == -1){
									className = 'bid-rejected';
								}else if(values.logType == -2){
									className = 'bid-cancelled';
								}
								else if(values.by == 'Bidder' || values.by == 'Agent'){
									className = 'bid-received';
								}else if(values.by == 'Offeror'){
									className = 'bid-sent';
								}else{
									className = 'bid-temp';
								}
								return  className;
							}
						})
					}]
			});
			EkaLoader.unmask();
		}, function(error){
			// handle error
			EkaLoader.unmask();
			EkaToast.show(Localization.FARMER_CONNECT.FETCH_BID_LOGS_FAILURE);
		});
		eatracker.trackEvent(EkaGoogleAnayticsUtil.pageTrackingMap.FARMER_CONNECT, EkaGoogleAnayticsUtil.constants.BIDS_LOG);		
		this.callParent(arguments);
	},
	
	reverseArray : function(inputArr){
		var outputArr = [];
		var j = 0;
		for(var i = inputArr.length -1; i>=0; i--){
			outputArr[i] = inputArr[j++];
		}
		return outputArr;
	}
});