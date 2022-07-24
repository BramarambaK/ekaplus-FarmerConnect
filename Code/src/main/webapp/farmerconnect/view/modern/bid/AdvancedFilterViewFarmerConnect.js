Ext.define('EkaSmartPlt.farmerconnect.view.bid.AdvancedFilterViewFarmerConnect', {
	extend : 'EkaSmartPlt.view.filter.AdvancedFilterView',
	xtype : 'ekasmartplt-farmerconnect-view-bid-advancedfilterviewfarmerconnect',
	layout : 'fit',
	height : window.innerHeight - 100, // -100 because titlebar and segmented button section
    cls:'filter-container',
    
	applyButtonHandler: function(button, event) {		
		var i;
		var componentData = this.componentData;
		var firstPanel = this.down('#firstFilterPanelItemId');
		var operator = firstPanel.down('#filterOperatorItemId').getValue();
		var inputValueField = firstPanel.down('#filterInputItemId');
		var inputValue = inputValueField.getValue();	   
		
		var filterObect = {
			type: "advanced",
			columnId: componentData.columnId,
			columnType: componentData.columnType
		};

		// These operators does not take any input values
		var operatorSet = new Set(['isBlank', 'isNotBlank']);
		// Added condition to pass filtered object based on selected operator and input value for first panel
		if (operatorSet.has(operator)) {
			inputValueField.setValue('');
			inputValue = '';

			filterObect["firstFilter"] = {
				columnId: componentData.columnId,
				columnType: componentData.columnType,
				operator: operator,
				value: [inputValue],
			};
		} else {
			if (inputValue && (inputValue instanceof Date || inputValue.trim().length > 0)) {
				filterObect["firstFilter"] = {
					columnId: componentData.columnId,
					columnType: componentData.columnType,
					operator: operator,
					value: [inputValue],
				};
				if (componentData.columnType == 3) {
					filterObect.firstFilter['dateFormat'] = "yyyy-MM-dd\'T\'HH:mm:ss";
				}
				if (componentData.columnType == 2) {
					filterObect.firstFilter.value =  [parseInt(inputValue)];
				}
			}
		}

		var secondPanel = this.down('#secondFilterPanelItemId');
		operator = secondPanel.down('#filterOperatorItemId').getValue();
		inputValueField = secondPanel.down('#filterInputItemId');
		inputValue = inputValueField.getValue();

		// Added condition to pass filtered object based on selected operator and input value for second panel
		if (operatorSet.has(operator)) {
			inputValueField.setValue('');
			inputValue = '';
			
			filterObect["logicalOperator"] = this.down('#logicalOperatorItemId').getValue();
			filterObect["secondFilter"] = {
				columnId: componentData.columnId,
				columnType: componentData.columnType,
				operator: operator,
				value: [inputValue],
			};
		} else {
			if (inputValue && (inputValue instanceof Date || inputValue.trim().length > 0)) {
				filterObect["logicalOperator"] = this.down('#logicalOperatorItemId').getValue();
				filterObect["secondFilter"] = {
					columnId: componentData.columnId,
					columnType: componentData.columnType,
					operator: operator,
					value: [inputValue],
				};
				if (componentData.columnType == 3) {
					filterObect.secondFilter['dateFormat'] = "yyyy-MM-dd\'T\'HH:mm:ss";
				}
				if (componentData.columnType == 2) {
					filterObect.secondFilter.value =  [parseInt(inputValue)];
				}
			}
		}
		
		
		var inputValFirstFilter = this.down('#firstFilterPanelItemId').down('#filterInputItemId');
		var inputValSecondFilter = this.down('#secondFilterPanelItemId').down('#filterInputItemId');
		var firstFilterOperator = this.down('#firstFilterPanelItemId').down('#filterOperatorItemId');
		var secondFilterOperator = this.down('#secondFilterPanelItemId').down('#filterOperatorItemId');
		
        
		if(!operatorSet.has(firstFilterOperator.getValue())){
			if (!inputValFirstFilter.getValue() && inputValSecondFilter.getValue()) {
				filterObect["firstFilter"] = filterObect.secondFilter;
				delete filterObect["secondFilter"];
				inputValFirstFilter.setValue(inputValSecondFilter.getValue());
				inputValSecondFilter.setValue('');
				if (firstFilterOperator.getValue() && secondFilterOperator.getValue()) {
					firstFilterOperator.setValue(secondFilterOperator.getValue());
				}
				secondFilterOperator.reset();
			}
		}		
		if(!this.validateFilters(filterObect)){
			EkaToast.show(Localization.DATAVIEW_CONSTANTS.INVALID_FILTERS);
			return;
		}
        // var localStorageValues = Ext.decode(localStorage.getItem(this.componentData.sourceValue));	
		// var filters = localStorageValues.filters;
		var filters = UserPreferences.getFilters(this.componentData.sourceValue);
		if (filterObect) {
			var needToAdd = true;
			if (filters.length == 0)
				filters.push(filterObect);
			else {
				for(i in filters) {
					var filter = filters[i];
					if (filterObect.columnId == filter.columnId) {
						filters[i] = filterObect;
						needToAdd = false;
					}
				}
				if (needToAdd)
					dataviewJSON.visualizations.filters.push(filterObect);
			}
		}
		// to update selected filter (i.e basic or advanced) and tick on the listing of filters
		this.componentData.filterValuePanel.componentData.filterOnView = true;
		var filterValuePanel = this.componentData.filterValuePanel;
		var filterValueImage = filterValuePanel.down('#filterUsedImageItemId');
		filterValueImage.setHidden(this.isReset?true:false);

		//resetting if required i.e if reset is pressed
		if(this.isReset){
			for(var i in filters){
				if(filters[i].columnId == this.componentData.columnId 
					&& filters[i].type ){				
					if( filters[i].type === 'advanced'){
						filters.splice(i,1);
					}			
				}
			}
			// to update selected filter (i.e basic or advanced)
			this.componentData.filterValuePanel.componentData.filterOnView = false;
        }
		// localStorage.setItem(this.componentData.sourceValue, Ext.encode(localStorageValues));
		UserPreferences.setFilters(this.componentData.sourceValue, filters);
		// to close the window
		this.up().backButtonHandler();
	},
	
	getOperators : function(){
		var columnType = this.componentData.columnType;
		var operators = [];
		if(columnType == 1){
			operators = [{value:Localization.FILTER_VALUE.CONTAINS,id:'contains'},
					{value:Localization.FILTER_VALUE.DOES_NOT_CONTAINS, id:'notContains'},			
					{value:Localization.FILTER_VALUE.START_WITH, id:'startsWith'},
					{value:Localization.FILTER_VALUE.DOES_NOT_START_WITH, id:'notStartsWith'},
					{value:Localization.FILTER_VALUE.IS, id:'equal'},
					{value:Localization.FILTER_VALUE.IS_NOT, id:'notEqual'},
					{value:Localization.FILTER_VALUE.IS_BLANK, id:'isBlank'},
					{value:Localization.FILTER_VALUE.IS_NOT_BLANK, id:'isNotBlank'}];
		}
		else if(columnType == 2 || columnType == 5 || columnType == -1){
			operators = [{value:Localization.FILTER_VALUE.LESS_THAN,id:'lessThan'},
				{value:Localization.FILTER_VALUE.GREATER_THAN_OR_EQUAL,id:'greaterThanOrEqual'},
				{value:Localization.FILTER_VALUE.IS,id:'equal'},
				{value:Localization.FILTER_VALUE.IS_NOT,id:'notEqual'},
				{value:Localization.FILTER_VALUE.IS_BLANK, id:'isBlank'},
				{value:Localization.FILTER_VALUE.IS_NOT_BLANK, id:'isNotBlank'},
				{value:Localization.FILTER_VALUE.TOP_N, id:'top'},
				{value:Localization.FILTER_VALUE.Bottom_N, id:'bottom'}];
			
		}
		return operators;
	},
	onBoxReady : function(){
        // var localStorageValues = Ext.decode(localStorage.getItem(this.componentData.sourceValue));	
		// var filters = localStorageValues.filters;
		var filters = UserPreferences.getFilters(this.componentData.sourceValue);
		if(filters && filters.length > 0){
			for(var   i in filters){
				var filter = filters[i];
				if(this.componentData.columnId === filter.columnId &&
					filter.type && filter.type === 'advanced'){
					if(filter.hasOwnProperty('firstFilter')){
						var filterPanel = this.down('#firstFilterPanelItemId');
						var operatorCombo = filterPanel.down('#filterOperatorItemId');
						operatorCombo.setValue(filter.firstFilter.operator);
						var inputField = filterPanel.down('#filterInputItemId');
						if(filter.firstFilter.columnType == 3)
							inputField.setValue(new Date(filter.firstFilter.value[0]));
						else
							inputField.setValue(filter.firstFilter.value[0]);
						if(filter.hasOwnProperty('secondFilter')){
							this.down('#logicalOperatorItemId').setValue(filter.logicalOperator);
							filterPanel = this.down('#secondFilterPanelItemId');
							operatorCombo = filterPanel.down('#filterOperatorItemId');
							operatorCombo.setValue(filter.secondFilter.operator);
							inputField = filterPanel.down('#filterInputItemId');
							if(filter.secondFilter.columnType == 3)
								inputField.setValue(new Date(filter.secondFilter.value[0]));
							else
								inputField.setValue(filter.secondFilter.value[0]);
						}
					}
				}
			}
		}
	}
});