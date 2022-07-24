Ext.define('EkaSmartPlt.farmerconnect.view.bid.VisualFilterViewFarmerConnect', {
	extend: 'EkaSmartPlt.view.filter.VisualFilterView',
	xtype : 'ekasmartplt-farmerconnect-view-bid-visualfilterviewfarmerconnect',
	requires: [        
        'EkaSmartPlt.farmerconnect.view.bid.BasicFilterViewFarmerConnect',
        // 'EkaSmartPlt.view.filter.AdvancedFilterView'
	],
	layout : 'vbox',
	addFilterButtons : function(){
        var items = [];
		var buttonData = this.getFilterTypes(this.componentData);
		var filterType = this.componentData.type ?  this.componentData.type : buttonData[0].id;
        for(var i in buttonData){
            items.push({
                text : buttonData[i].type,
				value : buttonData[i].id,
				pressed : filterType == buttonData[i].id
            });
        }
        this.add([{
			xtype : 'segmentedbutton',
			style:'margin:10px 0',
			cls:'segmented-button-cls',
            itemId : 'filterTypeButtonItemId',
            // value : buttonData[0].id,
            items : items,
            listeners : {
                change : 'onFilterTypeButtonChange',
                scope : this
            }
		}]);		
		this.onFilterTypeButtonChange(null, filterType);
	},
	showBasicFilterComponent : function(){
		var existingAdvancedFilter = this.down('#advancedFilterSectionItemId');
		if(existingAdvancedFilter)
			existingAdvancedFilter.hide();
		var existingBasicFilter = this.down('#basicFilterSectionItemId');
		if(existingBasicFilter){
			existingBasicFilter.show();
			return;
		}
		this.add({
			xtype: 'ekasmartplt-farmerconnect-view-bid-basicfilterviewfarmerconnect',
			itemId : 'basicFilterSectionItemId',
			componentData : this.componentData
		});
	},
	
	showAdvancedFilterComponent : function(parentPanel){
		var existingBasicFilter = this.down('#basicFilterSectionItemId');
		if(existingBasicFilter)
			existingBasicFilter.hide();
		var existingAdvancedFilter = this.down('#advancedFilterSectionItemId');
		if(existingAdvancedFilter){
			existingAdvancedFilter.show();
			return;
		}
		this.add({
			xtype: 'ekasmartplt-farmerconnect-view-bid-advancedfilterviewfarmerconnect',
			itemId : 'advancedFilterSectionItemId',
			componentData : this.componentData
		});
	},
	getFilterTypes : function(){
        var filterTypes = Ext.clone(this.config.filterTypes);
		return filterTypes;
	},
});