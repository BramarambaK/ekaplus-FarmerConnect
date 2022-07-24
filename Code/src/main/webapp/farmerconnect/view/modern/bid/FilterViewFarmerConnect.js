Ext.define('EkaSmartPlt.farmerconnect.view.bid.FilterViewFarmerConnect', {
	extend: 'Ext.Container',
    xtype : 'ekasmartplt-farmerconnect-view-bid-filterviewfarmerconnect',
    controller : 'ekasmartplt-farmerconnect-controller-bid-filterviewfarmerconnectcontroller',
    requires : ['EkaSmartPlt.ekacac.farmerconnect.controller.bid.FilterViewFarmerConnectController',
                'EkaSmartPlt.farmerconnect.view.bid.VisualFilterViewFarmerConnect'
            ],

    layout : 'vbox',
    scrollable : true,
	listeners : {
		added : 'addFilters',
		scope : 'controller'
	},
	initialize : function(){
        this.add([{
			xtype:'titlebar',
			cls:'secondary-titlebar',
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
				html : Localization.DATAVIEW_CONSTANTS.FILTERS,
				cls:'card-title'
			}],
            },{
                xtype : 'panel',
                itemId : 'filterAreaItemId',
                cls:'slicer-container transparent-title-bar',
                emptyText : 'No filters applied'
                // height : window.innerHeight - 150, //total height - title bar height
                // scrollable : true
            },{
                xtype: 'toolbar',
                cls:'footer-toolbar',
                height:54,
                docked: 'bottom',
                items: [{
                    xtype : 'button',
                    itemId : 'removeFilterButtonItemId',
                    text: Localization.DATAVIEW_CONSTANTS.REMOVE_ALL,
                    handler :  'removeFilter',
					flex : 1,
                    cls:'button-transparent-background button-color-blue',
                    scope : 'controller'
                },		
                {
                    xtype : 'button',
                    itemId : 'applyFilterButtonItemId',
                    text: Localization.DATAVIEW_CONSTANTS.APPLY,
                    handler :  'applyFilter',
					flex : 1,
                    cls:'button-blue',
                    scope : 'controller'
                }]
		}]);
		this.callParent(arguments);
	},
});