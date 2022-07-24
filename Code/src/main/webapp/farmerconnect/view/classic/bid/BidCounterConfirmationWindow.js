Ext.define('EkaSmartPlt.ekacac.farmerconnect.view.bid.BidCounterConfirmationWindow', {
	extend: 'Ext.window.Window',
	requires:['EkaSmartPlt.ekacac.farmerconnect.view.common.RatingWindow'],
	cls: 'smart-window custom-form-panel',
	modal : true,
	width : 618,
	height : 550,
	scrollable:'y',
	
	resizable:false,
	defaultListenerScope : true,
	layout : 'hbox',
    initComponent: function() {
		let _this = this;
		var isModeBidLog = this.mode == 'BIDLOG';
		let bidDetail={};
		if(_this.parentView.reference == "publishedOffersGrid"){

			 bidDetail=this.record.data
		}
        else{
			if(_this.parentView.reference == "myOffersGrid"){
				bidDetail = EkaAjaxService.performSyncAjaxCall('/spring/bids/'+this.record.get('bidId'),{
				method : 'GET'
			}); 
		}
			else{
				    bidDetail = EkaAjaxService.performSyncAjaxCall('/spring/bids/'+this.record.get('refId'),{
					method : 'GET'
				}); 
			}
			var store = new Ext.data.Store({
				data : this.reverseArray(bidDetail.negotiationLogs)
			});
		}

		_this.bidDetail=bidDetail;
		var quantityUnit = bidDetail.priceUnit.substring(bidDetail.priceUnit.indexOf('/')+1);
		var priceUnit = bidDetail.priceUnit;
		let deliveryFromDate=new Date(bidDetail.deliveryFromDateInMillis).toLocaleDateString();
        let deliveryToDate=new Date(bidDetail.deliveryToDateInMillis).toLocaleDateString();
		Ext.apply(this, {
			items : [
				{
				xtype : 'panel',
				width : 266,
				height:462,
				cls:'bid-counter-left-region',
				layout : 'vbox',
				items : [{
					xtype : 'panel',
					layout : 'vbox',
					hidden:_this.parentView.reference!="manageBidsOfferor" || PlatformAppConstants.FarmerConnectSettings.offerorInfoRestricted,
					cls:'panel-bid-basic-details bid-product-info bid-info-hide-border',
					defaults:{
						width:238,
						labelWidth:87
					},
					items : [{
						xtype : 'label',
						html : '<b>Bidder</b>',
						cls:'bid-user-name',
						margin : '0 5 5 2'
					},{
						xtype: 'displayfield',
						margin : '5 5 4 2',
						fieldLabel: 'Name',
						value: bidDetail.name
					},{
						xtype: 'displayfield',
						margin : '5 5 4 2',
						fieldLabel: 'Email ID',
						value : '<a href="mailto:' + bidDetail.email + '">' + bidDetail.email + '</a>'
					},{
						xtype: 'displayfield',
						margin : '5 5 4 2',
						fieldLabel: 'Phone',
						value : bidDetail.phoneNumber
					},{
						xtype: 'displayfield',
						margin : '5 5 4 2',
						fieldLabel: 'Applicable Role(s)',
						value: Ext.util.Format.ellipsis(bidDetail.applicableRoles, 100),
						inputAttrTpl: "data-qtip='"+bidDetail.applicableRoles+"'"
					},{
						xtype: 'displayfield',
						margin : '5 5 4 2',
						itemId:'bidRating',
						fieldLabel: 'Rating',
						value: bidDetail.rating
					}]
				},{
					xtype : 'panel',
					layout : 'vbox',
					hidden:_this.parentView.reference=="manageBidsOfferor" || PlatformAppConstants.FarmerConnectSettings.offerorInfoRestricted ,
					cls:'panel-bid-basic-details bid-product-info bid-info-hide-border',
					defaults:{
						width:238,
						labelWidth:87
					},
					items : [{
						xtype : 'label',
						html : '<b>Offeror</b>',
						cls:'bid-user-name',
						margin : '0 5 5 2'
					},{
						xtype: 'displayfield',
						margin : '5 5 4 2',
						fieldLabel: 'Name',
						value: _this.record.data.offerorName
					},{
						xtype: 'displayfield',
						margin : '5 5 4 2',
						fieldLabel: 'Email ID',
						value : '<a href="mailto:' + _this.record.data.username + '">' +  _this.record.data.username + '</a>'
					},{
						xtype: 'displayfield',
						margin : '5 5 4 2',
						fieldLabel: 'Phone',
						value : _this.record.data.phoneNumber
					},{
						xtype: 'displayfield',
						margin : '5 5 4 2',
						fieldLabel: 'Applicable Role(s)',
						value: Ext.util.Format.ellipsis(bidDetail.applicableRoles, 100),
						inputAttrTpl: "data-qtip='"+bidDetail.applicableRoles+"'"
					}]},/*{
						xtype:'button',
						value: Ext.util.Format.ellipsis(bidDetail.applicableRoles, 100),
						itemId:'bidRatingButton',
						hidden: !(PlatformAppConstants.FarmerConnectSettings.offerRatingAllowed && bidDetail.status=="Accepted") ,
						text:'Rating',
						handler : 'onRatingHandler'
					},{
						xtype: 'displayfield',
						margin : '5 5 4 2',
						itemId:'bidRating',
						hidden: !(PlatformAppConstants.FarmerConnectSettings.offerRatingAllowed) || bidDetail.status=="Accepted",
						fieldLabel: 'Rating',
						value: bidDetail.rating
					}]
				}*/,{
						xtype : 'container',
						hidden: _this.isRatingAllowed(bidDetail),
				     	layout : 'hbox',
						style:'margin-top:8px;',
						defaults:{
							labelWidth:87
						},
                        items: [
							{
							xtype: 'displayfield',
						    cls: Ext.isEmpty(bidDetail.ratingInfo)?'color-red':'',
						    reference:'ratingInfo',
						    fieldLabel: 'Rating',
						    value: Ext.isEmpty(bidDetail.ratingInfo)?'pending':bidDetail.ratingInfo.rating
							},							
							{
								xtype : 'button',
								text : 'Rate Now',
								reference:'ratingLink',
								hidden: !(Ext.isEmpty(bidDetail.ratingInfo)),
								cls : 'button-hyperlink',
								handler:'onRatingHandler'
                        }]
				},{
					xtype : 'panel',
					layout : 'vbox',
					style:'margin-top:8px;',
					cls:'panel-bid-basic-details bid-product-info',
					defaults:{
						width:238,
						labelWidth:87
					},
					items : [{
						xtype: 'displayfield',
						margin : '5 5 4 2',
						fieldLabel: 'Offer',
						value: bidDetail.offerType
					},{
						xtype: 'displayfield',
						margin : '5 5 4 2',
						fieldLabel: 'Product',
						value: bidDetail.product
					},{
						xtype: 'displayfield',
						margin : '5 5 4 2',
						fieldLabel: 'Quality',
						value: bidDetail.quality
					},{
						xtype: 'displayfield',
						margin : '5 5 4 2',
						fieldLabel: 'Crop Year',
						value: bidDetail.cropYear
					},{
						xtype: 'displayfield',
						margin : '5 5 4 2',
						fieldLabel: 'Location',
						value: bidDetail.location
					},{
						xtype: 'displayfield',
						margin : '5 5 4 2',
						fieldLabel: 'Quantity',
						value: bidDetail.quantity + bidDetail.quantityUnit
					},{
						xtype: 'displayfield',
						hidden: PlatformAppConstants.FarmerConnectSettings.offerType=="basic",
						margin : '5 5 4 2',
						fieldLabel: 'Payment Term',
						value: bidDetail.paymentTerms
					},{
						xtype: 'displayfield',
						margin : '5 5 4 2',
						hidden: PlatformAppConstants.FarmerConnectSettings.offerType=="basic",
						fieldLabel: 'Packing Type',
						value: bidDetail.packingType
					},{
						xtype: 'displayfield',
						margin : '5 5 4 2',
						hidden: PlatformAppConstants.FarmerConnectSettings.offerType=="basic",
						fieldLabel: 'Packing Size',
						value: bidDetail.packingSize
					},{
						xtype: 'displayfield',
						margin : '5 5 4 2',
						fieldLabel: 'Delivery Period',
						value: Ext.Date.format(new Date(bidDetail.deliveryFromDateInMillis), 'd-m-Y') +' to '+ Ext.Date.format(new Date(bidDetail.deliveryToDateInMillis), 'd-m-Y')
					}]
				}]
			},{
				xtype : 'panel',
				width : 360,
				height:412,
				padding:'10',
				scrollable : true,
				layout : 'vbox',
				items : [{
					xtype : 'label',
					cls:'bid-basic-details',
					html : 'Bid ID: ' + bidDetail.bidId,
					margin : '2 2 4 2'
				},{
					xtype : 'label',
					cls:'bid-basic-details',
					html : 'Published Bid Price: <b>' + bidDetail.publishedPrice + ' ' + bidDetail.priceUnit  + '</b>',
					margin : '8 2 10 2'
				},{
					xtype : 'panel',
					hidden: _this.parentView.reference=="publishedOffersGrid",
					collapsible : !isModeBidLog,
					collapsed : !isModeBidLog,
					title:'Bid Log',
					cls:'bid-log-panel',
					closable : false,
					width : 330,
					items :[{
						xtype : 'dataview',
						width : '100%',
						itemSelector: 'div.thumb-wrap',
						store : store,
						tpl : new Ext.XTemplate(
							'<tpl for=".">',
								'<div class = "thumb-wrap item-log-flow {[this.getClassName(values)]}">',
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
							'</tpl>',
							{
								
								getDateAndPrice : function(values){
									var html = '';
									if(values.logType == 0)
										html = '<div class="log-flow-normal-label">Published Bid Price</div>';
									else {
										html += '<div class="log-flow-normal-label">'+Ext.Date.format(new Date(values.date), 'd-M-Y H:i:s T')+'</div>';
									}
									if(values.logType == 1)
										html += '<div class="log-flow-bold-label">Accepted</div>';
									else if(values.logType == -1)
										html += '<div class="log-flow-bold-label">Rejected</div>';
									else if(values.logType == -2)
										html += '<div class="log-flow-bold-label">Cancelled</div>';
									else
										html += '<div class="log-flow-bold-label">' + values.price +' ' +priceUnit + '</div>';
									
									return  html;
								},
								populateDetails : function(values){
									if(values.logType == 0)
										return '';
									if(values.name){
								     if(PlatformAppConstants.FarmerConnectSettings.offerorInfoRestricted){
										return '<span class="log-flow-remarks-label">'
										+ ((values.remarks && values.remarks.length > 0)
											? values.remarks : 'No Remarks')+'</span>';
									 }else{
										return '<span class="log-flow-normal-label">'+values.name 
										+ '</span>, ' + '<span class="log-flow-normal-label">'+ values.by 
										+ '</span>: <span class="log-flow-remarks-label">'
										+ ((values.remarks && values.remarks.length > 0)
											? values.remarks : 'No Remarks')+'</span>';
									 }
									}else{
										return bidDetail.name;

									}
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
									}else if(values.by == 'Bidder' || values.by == 'Agent'){
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
				},{
					xtype : 'label',
					html : 'Latest Bidder Price: <b>'+bidDetail.latestBidderPrice + ' ' + bidDetail.priceUnit  + '</b>',
					cls:'bid-basic-details',
					margin : '12 2 10 2',
					hidden : isModeBidLog
				},{
					xtype : 'container',
					layout : 'hbox',
					width : '70%',
					items :[{
						xtype : 'textfield',
						margin : '2 2 2 2',
						width : '70%',
						itemId : 'quantityItemId',
						fieldLabel : 'Quantity',
						reference:'quantityItemId',
						labelAlign : 'top',
						emptyText : 'Enter counter bid price',
						hidden : isModeBidLog,
						value: bidDetail.quantity,
						readOnly: !(_this.operation=='Counter') && PlatformAppConstants.FarmerConnectSettings.bidQuantityLocked,
						validator : function(value){
							if(!value || value.trim().length == 0)
								return 'This is mandatory field';
							else if(isNaN(value))
								return 'Please enter numeric value';
							else if(parseFloat(value) <= 0)
								return 'Please enter value greater than 0'
							return true;
						}
					},{
						xtype : 'label',
						html : '<b>'+quantityUnit+'</b>',
						style : 'margin:32px 0 0 5px',
						cls:'bid-basic-details',
						hidden : isModeBidLog
					}]
				},{ 
					xtype: 'container',
					layout: 'vbox',
					hidden : isModeBidLog ||!(_this.parentView.reference=="publishedOffersGrid"),
					items: [{
						xtype: 'label',
						html:'Delivery Period',
						style : 'margin:32px 0 0 5px',
						cls:'bid-basic-details',
					}, {
						xtype: 'container',
						layout: 'hbox',
						width:250,
						padding: '0px',
						items: [{
							xtype: 'datefield',
							width: 120,
							labelSeparator: ' ',
							margin:'0 5 0 0 ',
							name: 'deliveryFromDateISOString',
							reference: 'deliveryFromDateISOString',
							value:deliveryFromDate,
							minValue: new Date(),
							allowBlank: false,
							labelAlign: 'top',
						},{
							xtype: 'datefield',
							width: 120,
							labelSeparator: ' ',
							name: 'deliveryToDateISOString',
							reference: 'deliveryToDateISOString',
							value:deliveryToDate,
							minValue: new Date(),
							allowBlank: false,
							labelAlign: 'top',
						}]
					}]						
				},{
					xtype: 'checkbox',
					reference: 'checkCounter',
					boxLabel : 'Counter',
					boxLabelAlign :'after',
					checked:_this.operation=='Counter',
					hidden:!(_this.operation=='Counter' || _this.operation=='Accept'),
					handler: function(){
						_this.counterHandler(this.value)
					}
				},{
					xtype : 'container',
					layout : 'hbox',
					width : '70%',
					items :[{
						xtype : 'textfield',
						margin : '2 2 2 2',
						width : '70%',
						itemId : 'counterPriceItemId',
						fieldLabel : '',
						reference:'counterPriceItemId',
						disabled:!(_this.operation=='Counter'),
						emptyText : 'Enter counter bid price',
						hidden : isModeBidLog,
						validator : function(value){
							if(!value || value.trim().length == 0)
								return 'This is mandatory field';
							else if(isNaN(value))
								return 'Please enter numeric value';
							else if(parseFloat(value) <= 0)
								return 'Please enter value greater than 0'
							return true;
						}
					},{
						xtype : 'label',
						html : '<b>'+priceUnit+'</b>',
						style : 'margin:11px 0 0 5px',
						cls:'bid-basic-details',
						hidden : isModeBidLog
					}]
				},{
					xtype : 'textfield',
					margin : '2 2 2 2',
					fieldLabel : 'Remark',
					itemId : 'remarkItemId',
					width : '70%',
					labelAlign : 'top',
					hidden : isModeBidLog,
					enforceMaxLength : true,
					maxLength : 250
				}]
			}],
			bbar : {
				items : [{
					xtype : 'button',
					text : 'CLOSE',
					cls : 'button-inverse',
					handler : 'cancelButtonHandler'
				},'->',{
					xtype : 'button',
					text : (_this.operation=='Counter') ? 'Send':'Accept',
					hidden : isModeBidLog,
					cls: 'button-primary tabpanel-button',
					itemId : 'mainActionButtonItemId',
					handler : 'okButtonHandler'
				}]
			}
		});
		this.callParent(arguments);
    },
	
	getActionMainLabel : function(){
		if(this.confirmationType == 'ACCEPT')
			return 'You are accepting following bids at below final price:';
		return 'You are rejecting following bids:';
	},
	
	okButtonHandler : function(){
		let _this=this;

        if(_this.checkDeliveryDateValidation()){
		let remarkItem = _this.down('#remarkItemId');
		let operation=_this.down('#mainActionButtonItemId').getText();
		let recordData=_this.record.data;



		let successCallBack=function(){
			_this.successCallback();
			_this.close();
		}

		let errorCallBack=function(){
			Ext.Msg.alert("Error");

		}
		let remarks=remarkItem.getValue();
		if(PlatformAppConstants.FarmerConnectSettings.personalInfoSharingRestricted){

			if(_this.validateComments(remarks)){
				recordData['remarks']=remarks;

			}
			else{
				Ext.Msg.alert("Alert","Kindly do not enter any email ids or phone numbers");
				return
			}
		}else{
			recordData['remarks']=remarks;

		}
		if(operation!="Accept"){
			let  counterPriceItem = _this.down('#counterPriceItemId');
			if(!counterPriceItem.isValid()){
				return;
			}
			recordData['status']='In-Progress';
			recordData['price']= counterPriceItem.getValue();
		}
		else{
			recordData['status']='Accepted'
		}

		if(_this.parentView.reference == "publishedOffersGrid"){
			recordData.deliveryFromDateInMillis=Ext.Date.add(_this.down('[reference=deliveryFromDateISOString]').getValue(),Ext.Date.HOUR,23.99).getTime();
			recordData.deliveryToDateInMillis=Ext.Date.add(_this.down('[reference=deliveryToDateISOString]').getValue(),Ext.Date.HOUR,23.99).getTime();

			FarmerConnectService.counterOrAcceptPublishedBids(recordData,successCallBack,errorCallBack)
		}
		else{
			let isOfferor=_this.parentView.reference=="manageBidsOfferor";
			FarmerConnectService.updateBid(recordData.refId,recordData,successCallBack,errorCallBack,isOfferor)
		}
	}
	else{
		Ext.Msg.alert("Alert","Kindly enter proper delivery dates");
	}
	},
	checkDeliveryDateValidation:function(){
		let _this=this;
		let validation=true;
  
  
		if(_this.parentView.reference=="publishedOffersGrid"){
		  let deliveryFrom=Ext.Date.add(_this.down('[reference=deliveryFromDateISOString]').getValue(),Ext.Date.HOUR,23.99).toISOString();
		  let deliveryTo=Ext.Date.add(_this.down('[reference=deliveryToDateISOString]').getValue(),Ext.Date.HOUR,23.99).toISOString();
			if(new Date(deliveryFrom)<new Date() ||new Date(deliveryTo)<new Date() || new Date(deliveryFrom)>new Date(deliveryTo)){
			  validation=false
			}
		}
		return validation;

	},
	
	cancelButtonHandler : function(){
		this.close();
	},
	
	reverseArray : function(inputArr){
		var outputArr = [];
		var j = 0;
		for(var i = inputArr.length -1; i>=0; i--){
			outputArr[i] = inputArr[j++];
		}
		return outputArr;
	},
	show:function(){
		let _this=this;
		if(_this.record.data.status=="Accepted" && PlatformAppConstants.FarmerConnectSettings.offerRatingAllowed){
			if(!Ext.isEmpty(_this.bidDetail.ratingInfo)){
				_this.down('[reference=ratingLink]').setHidden(true);
				_this.down('[reference=ratingInfo]').setValue(_this.bidDetail.ratingInfo.rating);
				}	
			}
		
		_this.callParent(arguments);

	},
	onRatingHandler:function(){
		var _this=this;
		Ext.create('EkaSmartPlt.ekacac.farmerconnect.view.common.RatingWindow',{
			componentData:_this.bidDetail,
			parentComponent:_this
		}).show();
		
	},
	counterHandler: function(value){
		let _this = this;
		_this.down('#counterPriceItemId').setDisabled(!value);
		_this.down('#mainActionButtonItemId').setText(value?'SEND':'Accept');
		_this.down('#deliveryFromDateISOString').setReadOnly(!value);
		_this.down('#deliveryToDateISOString').setReadOnly(!value);
		_this.down('#quantityItemId').setReadOnly(!value);

	},
	validateComments:function(comment){
		
		let emailRegex= /[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;

		let emailPresent=emailRegex.test(comment);

		//let mobileRegex=/^\( *\d{3}\) \d{3}-\d{4}(-\d{4})?$/;
		
		let mobileRegex=/\d{10}/;

	//	let mobileNumberPresent= (comment.match(/\d/gi)!=null && comment.match(/\d/gi).length>7)?true:false;

	    let mobileNumberPresent=mobileRegex.test(comment)

		return !(emailPresent || mobileNumberPresent);

	},
	isRatingAllowed:function(bidDetail){

		       let _this=this;
		if(PlatformAppConstants.FarmerConnectSettings.offerRatingAllowed){
			if(_this.parentView.reference=="manageBidsBidder" && bidDetail.status=="Accepted"){
				return false
			}
			else{
				return true
			}
		} 
		else{
			return true
		}
	}
	
});
