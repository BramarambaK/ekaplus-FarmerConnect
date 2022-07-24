Ext.define("EkaSmartPlt.ekacac.farmerconnect.controller.common.RatingWindowController", {
	extend: "EkaSmartPlt.platform.controller.AbstractEkaController",
	alias:"controller.ekasmartplt-farmerconnect-controller-common-ratingwindowcontroller",
	init: function() {
        this.feedback = {};
	    this.feedback.tag=[];
	return this.callParent(arguments);
	},
	doneButtonHandler : function(button){
		_this = this;
		_this.feedback.remarks = this.getView().down('#remarksItemId').getValue();
		// call api to save this.feedback
		var extraParams = {
			ratedOn : _this.feedback.tag,
			remarks : _this.feedback.remarks
		};
		FarmerConnectService.createRating(_this.getView().componentData.refId, _this.feedback.rating, extraParams, function(responseJSON){
			_this.getView().parentComponent.bidDetail['ratingInfo']={};
			_this.getView().parentComponent.bidDetail.ratingInfo['rating']=_this.feedback.rating
			_this.getView().parentComponent.show();
			_this.getView().close();

		}, function(error){
			Ext.Msg.alert("Error");
		});
    },
    laterButtonHandler:function(){
      let _this=this;
      _this.getView().close();
    },
	onRatingHandler : function(button){
		var container = button.up('container');
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
		var ratingQuestion = view.down('#ratingQuestionItemId');
		ratingQuestion.setHidden(false);
		var ratingValue = view.down('#ratingValueItemId');
		switch(button.buttonPosition){
			case 1 :
				ratingQuestion.setHtml('WHAT WENT BAD');
				ratingValue.setHtml("BAD!");
				break;
			case 2 :
				ratingQuestion.setHtml('WHAT WENT BAD');
				ratingValue.setHtml("NOT GOOD!");
				break;
			case 3 :
				ratingQuestion.setHtml('WHAT WENT BAD');
				ratingValue.setHtml("NEUTRAL!");
				break;
			case 4 :
				ratingQuestion.setHtml('WHAT CAN IMPROVE');
				ratingValue.setHtml("GOOD!");
				break;
			case 5 :
				ratingQuestion.setHtml('WHAT YOU LIKED');
				ratingValue.setHtml("EXCELLENT!");
				break;
		}
	},
	
	onFeedbackHandler : function(button){
		let _this=this;
		let tag=_this.feedback.tag;
		let buttonItemId = button.getItemId();
		let view = _this.getView();
		let isPressed=view.down('#'+buttonItemId).pressed;
		if(isPressed){
		    if(buttonItemId == 'allButtonItemId'){
				_this.feedback.tag=[];
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
			if(buttonItemId == 'allButtonItemId'){
				_this.feedback.tag=[];
				view.down('#allButtonItemId').setPressed(false);
				view.down('#shipmentButtonItemId').setPressed(false);
				view.down('#qualityButtonItemId').setPressed(false);
				view.down('#quantityButtonItemId').setPressed(false);
				view.down('#pricingButtonItemId').setPressed(false);
			}
			else{
				view.down('#allButtonItemId').setPressed(false);
				delete(tag[button.getText()]);
			}
		
	}
	}
});