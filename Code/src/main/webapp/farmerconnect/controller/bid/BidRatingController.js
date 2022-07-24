Ext.define("EkaSmartPlt.ekacac.farmerconnect.controller.bid.BidRatingController", {
	extend: "EkaSmartPlt.platform.controller.AbstractEkaController",
	alias:"controller.ekasmartplt-farmerconnect-controller-bid-bidratingcontroller",
	init: function() {
	return this.callParent(arguments);
	},
	feedback : {},
	doneButtonHandler : function(button){
		_this = this;
		_this.feedback.remarks = this.getView().down('#remarksItemId').getValue();
		// call api to save this.feedback
		var extraParams = {
			ratedOn : _this.feedback.tag,
			remarks : _this.feedback.remarks
		};
		BidService.createRating(_this.getView().componentData.bidJSON.refId, _this.feedback.rating, extraParams, function(responseJSON){
			EkaToast.show(Localization.FARMER_CONNECT.RATING_SUCCESS);
			var parentComponent = _this.getView().componentData.parentComponent;  
			parentComponent.lookupReference('currentRatingReference').setHtml(_this.feedback.rating);
			parentComponent.lookupReference('currentRatingButton').setHidden(true);
			_this.backButtonHandler();
		}, function(error){
			EkaToast.show(Localization.FARMER_CONNECT.RATING_FAILURE);
		});
	},
	backButtonHandler : function(){
        var parentComponent = this.getView().componentData.parentComponent;   
		this.removeFromContentPanel(this.getView());
		parentComponent.show();
    },
	onRatingHandler : function(button){
		var container = button.up('container');
		var doneButton = this.getView().down('#doneButtonItemId');
		var view = this.getView();
		for(var i = 1; i <= 5; i++){
			var buttonItemId = 'buttonItemId'+i;
			if(i <= button.buttonPosition){
				container.down('#'+buttonItemId).setPressed(true);
			} else {
				container.down('#'+buttonItemId).setPressed(false);
			}
		}
		this.feedback.rating = button.buttonPosition;
		doneButton.setDisabled(false);
		var ratingQuestion = view.down('#ratingQuestionItemId');
		ratingQuestion.setHidden(false);
		var ratingValue = view.down('#ratingValueItemId');
		switch(button.buttonPosition){
			case 1 :
				ratingQuestion.setHtml(Localization.FARMER_CONNECT.RATE_WHAT_WENT_BAD);
				ratingValue.setHtml("BAD!");
				break;
			case 2 :
				ratingQuestion.setHtml(Localization.FARMER_CONNECT.RATE_WHAT_WENT_BAD);
				ratingValue.setHtml("NOT GOOD!");
				break;
			case 3 :
				ratingQuestion.setHtml(Localization.FARMER_CONNECT.RATE_WHAT_WENT_BAD);
				ratingValue.setHtml("NEUTRAL!");
				break;
			case 4 :
				ratingQuestion.setHtml(Localization.FARMER_CONNECT.RATE_WHAT_CAN_IMPROVE);
				ratingValue.setHtml("GOOD!");
				break;
			case 5 :
				ratingQuestion.setHtml(Localization.FARMER_CONNECT.RATE_WHAT_YOU_LIKED);
				ratingValue.setHtml("EXCELLENT!");
				break;
		}
	},
	
	onFeedbackHandler : function(button){
		let _this=this;
		_this.feedback.tag = [];
		let tag=_this.feedback.tag;
		let buttonItemId = button.getItemId();
		let view = _this.getView();
		let button=view.down('#'+buttonItemId);
		let isPressed=view.down('#'+buttonItemId)._pressed;
		button.setPressed(!isPressed);
	/*	if(buttonItemId != 'pricingButtonItemId')
			view.down('#pricingButtonItemId').setPressed(false);
		else
			view.down('#pricingButtonItemId').setPressed(true);
		if(buttonItemId != 'quantityButtonItemId')
			view.down('#quantityButtonItemId').setPressed(false);
		else
			view.down('#quantityButtonItemId').setPressed(true);
		if(buttonItemId != 'qualityButtonItemId')
			view.down('#qualityButtonItemId').setPressed(false);
		else
			view.down('#qualityButtonItemId').setPressed(true);
		if(buttonItemId != 'shipmentButtonItemId')
			view.down('#shipmentButtonItemId').setPressed(false);
		else
			view.down('#shipmentButtonItemId').setPressed(true);
		if(buttonItemId != 'allButtonItemId'){
			view.down('#allButtonItemId').setPressed(false);
		}
		else{
			view.down('#allButtonItemId').setPressed(true);
			view.down('#shipmentButtonItemId').setPressed(true);
			view.down('#qualityButtonItemId').setPressed(true);
			view.down('#quantityButtonItemId').setPressed(true);
			view.down('#pricingButtonItemId').setPressed(true);
		}*/		
		if(!isPressed){
		    if(buttonItemId == 'allButtonItemId'){
			   view.down('#allButtonItemId').setPressed(true);
			   view.down('#shipmentButtonItemId').setPressed(true);
			   view.down('#qualityButtonItemId').setPressed(true);
			   view.down('#quantityButtonItemId').setPressed(true);
			   view.down('#pricingButtonItemId').setPressed(true);
			   tag.push(view.down('#pricingButtonItemId').getText());
			   tag.push(view.down('#quantityButtonItemId').getText());
			   tag.push(view.down('#qualityButtonItemId').getText());
			   tag.push(view.down('#shipmentButtonItemId').getText());
		   }	
		    else	
			   tag.push(button.getText());
		}
		else{
			delete(tag[button.getText()]);
	}
	}
});