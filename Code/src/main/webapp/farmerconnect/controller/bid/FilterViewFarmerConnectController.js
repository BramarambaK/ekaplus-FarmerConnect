Ext.define("EkaSmartPlt.ekacac.farmerconnect.controller.bid.FilterViewFarmerConnectController", {
	extend: "EkaSmartPlt.platform.controller.AbstractEkaController",
	alias:"controller.ekasmartplt-farmerconnect-controller-bid-filterviewfarmerconnectcontroller",
	init: function() {
		return this.callParent(arguments);
    },
    addFilters : function(view){
        this.addVisualFilters(view);
    },      
    addVisualFilters : function(view){
        var _this = this;
        view.userPreferenceFilters = JSON.parse(JSON.stringify(UserPreferences.getFilters(view.componentData.sourceValue)));
        // view.localStorage = JSON.parse(JSON.stringify(localStorage));
        // var localStorageValues = Ext.decode(localStorage.getItem(view.componentData.sourceValue));
        // var selectedFilters = localStorageValues.filters;
        var selectedFilters = UserPreferences.getFilters(view.componentData.sourceValue);
        var selectedFiltersMap = new Ext.util.HashMap();
        for(var i in selectedFilters){
            selectedFiltersMap.add(selectedFilters[i].columnId, selectedFilters[i]);
        }
        var filters = FarmerConnectConfigurationObject.filterParams;
        var filterArea = view.down('#filterAreaItemId');
        for(var i in filters) {
            if(!(FarmerConnectConfigurationObject.role=="Offerer" && filters[i].displayText==Localization.FARMER_CONNECT.USER_NAME)){
                var componentData = {
                    columnId : filters[i].value,
                    columnName : filters[i].displayText,
                    columnType : filters[i].type,
                    prevComponent : view,
                    type : selectedFiltersMap.get(filters[i].value)?selectedFiltersMap.get(filters[i].value).type : null,
                    // source to set because in basic filters for bid screen and published prices different api calls are there
                    sourceValue : view.componentData.sourceValue,
                    componentType : 'ekasmartplt-farmerconnect-view-bid-visualfilterviewfarmerconnect'
                };
                filterArea.add({
                    xtype : 'ekasmartplt-view-basecomponent-clickabletoolbar', 
                    componentData : componentData,
                    layout : 'hbox',
                    cls:'chart-filter-items-area',
                    customElementListeners : {
                        tap : function(eventDetails){                       
                            view.hide();
                            var componentData = this.component.componentData;
                            componentData['filterValuePanel'] = this.component;
                        _this.addComponentToContentPanelHandler(componentData);
                        }
                    },
                    items :[{
                        xtype : 'label',
                        cls:'chart-filter-items',
                        html : filters[i].displayText,
                        height : 40,
                    }, 
                    { xtype: 'spacer'},
                    {
                        xtype:'image',
                        width:20,
                        itemId : 'filterUsedImageItemId',
                        hidden : selectedFiltersMap.get(filters[i].value) ? false:true,
                        height:20,
                        cls:'button-icon icon-tick',
                    }],
                });
            }
        }
    },
    backButtonHandler : function(setDetails){
        var prevComponent = this.getView().componentData.prevComponent; 
        if(setDetails != false){
            UserPreferences.setFilters(this.getView().componentData.sourceValue, this.getView().userPreferenceFilters);
            // localStorage = this.getView().localStorage;             
            var fiterButton = prevComponent.down('#fiterButtonItemId');
            if(fiterButton)
                fiterButton.setPressed(!fiterButton.getPressed());
        }  
        this.getView().destroy();     
        prevComponent.show();
    },
    applyFilter : function(object){
        // on view paint automatically it picks filters from localstorage
        var prevComponent = this.getView().componentData.prevComponent;
        var fiterButton = prevComponent.down('#fiterButtonItemId');
        if(fiterButton){
            // verifying if filters is applied only then icon will be highlighted
            // var localStorageValues = Ext.decode(localStorage.getItem(this.getView().componentData.sourceValue));
            var userPreferenceFilters = UserPreferences.getFilters(this.getView().componentData.sourceValue);
            // if(localStorageValues.filters.length > 0)
            if(userPreferenceFilters.length > 0)            
                fiterButton.setPressed(true);
            else
                fiterButton.setPressed(false);
        }
        this.backButtonHandler(false);
    },
    removeFilter : function(object){
        var view = this.getView();
        // var localStorageValues = Ext.decode(localStorage.getItem(view.componentData.sourceValue));
		// localStorageValues.filters = []
        // localStorage.setItem(view.componentData.sourceValue, Ext.encode(localStorageValues));
        UserPreferences.setFilters(view.componentData.sourceValue, []);
        var prevComponent = this.getView().componentData.prevComponent;
        var fiterButton = prevComponent.down('#fiterButtonItemId');
        if(fiterButton){
            fiterButton.setPressed(false);
        }
        this.backButtonHandler(false);      
    },
});