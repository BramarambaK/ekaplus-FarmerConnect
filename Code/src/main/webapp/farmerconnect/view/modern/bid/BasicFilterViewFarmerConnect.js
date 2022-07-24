Ext.define('EkaSmartPlt.farmerconnect.view.bid.BasicFilterViewFarmerConnect', {
    // extend : 'EkaSmartPlt.view.filter.BasicFilterView',	
    extend : 'Ext.Panel',
	xtype : 'ekasmartplt-farmerconnect-view-bid-basicfilterviewfarmerconnect',
	layout : 'fit',
	cls:'filter-container',
    height : window.innerHeight - 100, // -100 because titlebar and segmented button section
    initialize : function () {
		this.add([{
			xtype: 'toolbar',
			cls:'footer-toolbar',
			height:54,
			docked: 'bottom',
			items: [{
				xtype : 'button',
				itemId : 'resetBasicFilterButtonItemId',
				flex : 1,
				text: Localization.DATAVIEW_CONSTANTS.RESET,
				handler : 'onBasicFilterReset',
				cls:'button-transparent-background button-color-blue',
				scope : this
			},		
			{
				xtype : 'button',
				itemId : 'applyBasicFilterButtonItemId',
				text: Localization.DATAVIEW_CONSTANTS.DONE,
				flex : 1,
				handler :  'onBasicFilterSelect',
				cls: 'button-blue',
				scope : this
			}]
		}]);
		this.prepareCheckboxGroupItems();
		this.callParent(arguments);
	},
	prepareCheckboxGroupItems : function(){
		var _this = this;
		if(this.componentData.columnType == 2){
			_this.add({
				xtype : 'label',
				padding : 10,			
                html : Localization.FILTER_VALUE.BASIC_FILTERS_NOT_AVAILABLE,
			})
			return;
		}
        var sourceValue = this.componentData.sourceValue;
        var filterValueMap = [];
        // var localStorageValues = Ext.decode(localStorage.getItem(this.componentData.sourceValue));	
		// var filters = localStorageValues.filters;
		var filters = UserPreferences.getFilters(this.componentData.sourceValue);
        for(var i in filters){
            if(filters[i].columnId ==  this.componentData.columnId){
                filterValueMap = filters[i].value;
            }
        }
        var url = ModernAppConstants.baseURL;
        switch(sourceValue){
            case 'publishedprices' : url += 'published-bids/values/' + this.componentData.columnId;
                                    break;
            case 'bid' : url += 'bids/values/' + this.componentData.columnId;
                                    break;
        };
        var successCallback = function(response){
            var items = [];
            if(response &&  response.length>0){                
                for(var i in response){
                    var item = {
                        xtype : 'checkboxfield',
                        cls:'combo-slicer combo-filter',
                        boxLabel: response[i],
                        name : 'basicFilter' ,
                        // inputValue : response[i] ,
                        value :response[i],
                    };
                    if(filterValueMap && Ext.Array.contains(filterValueMap,response[i])){
                        item['checked'] = true;
                    }
                    items.push(item);
                }
			}
			if(items.length == 0){
				items.push({
					xtype : 'label',
					cls:'combo-slicer combo-filter',
					html : Localization.FILTER_VALUE.BASIC_FILTERS_NOT_AVAILABLE
				});
			}
            _this.add({
                xtype : 'container',			
                scrollable : true,
                itemId : 'basicCheckBoxFieldItemId',
                items : items
            })
        };
        BidService.getColumnUniqueValues(url, successCallback, function(error){
            //failure case, nothing as of now
        });		
	},
	onBasicFilterReset: function(button) {
		var checkboxContainer = this.down('#basicCheckBoxFieldItemId');
		var items = checkboxContainer.getItems().items[0].getSameGroupFields();
		items.forEach(function(b) {
            b.setChecked(false);
		});

		this.isReset = true;
	},
	onBasicFilterSelect : function(button){
		// Google Analytics Code - to record event
		// isreset to show tick
		this.isReset = true;
		var checkboxContainer = this.down('#basicCheckBoxFieldItemId');
		var items = checkboxContainer.getItems();
        var selectedValues = items.items[0].getGroupValues();	
        // var localStorageValues = Ext.decode(localStorage.getItem(this.componentData.sourceValue));	
		// var filters = localStorageValues.filters;
		var filters = UserPreferences.getFilters(this.componentData.sourceValue);
		var basicfilterObj = {
			columnId : this.componentData.columnId,
			columnName : this.componentData.columnId,
			columnType : this.componentData.columnType
		};
		if(selectedValues.length > 0){
			this.isReset = false;			
			basicfilterObj["type"] = "basic";
			basicfilterObj["operator"] = 'in';
			basicfilterObj["value" ] = selectedValues;
			if(basicfilterObj.columnType === 3)
				basicfilterObj['dateFormat'] = 'E MMM dd HH:mm:ss Z yyyy';
			var isAlreadyPresent = false;
			for(var i in filters){
				var filter = filters[i];
				if(filter.columnId == basicfilterObj.columnId){
                    filters[i] = basicfilterObj;
                    isAlreadyPresent = true;
					break;
				}
            }
            if(!isAlreadyPresent){
                filters.push(basicfilterObj);
            }
		}
		if(this.isReset){
			for(var i in filters){
				if(filters[i].columnId == this.componentData.columnId 
					&& filters[i].columnType ){				
					if( filters[i].columnType === 'basic'){
						filters.splice(i,1);
					}			
				}
			}
        }
		// localStorage.setItem(this.componentData.sourceValue, Ext.encode(localStorageValues));
		UserPreferences.setFilters(this.componentData.sourceValue, filters);
		var filterValuePanel = this.componentData.filterValuePanel;
		var filterValueImage = filterValuePanel.down('#filterUsedImageItemId');
		filterValueImage.setHidden(this.isReset?true:false);
		// to close the window
		this.up().backButtonHandler();
	}
});