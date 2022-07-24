Ext.define("EkaSmartPlt.farmerconnect.view.bid.FarmerConnectConfigurationObject", {
	extend: 'Ext.Base',
	alternateClassName: 'FarmerConnectConfigurationObject',
    singleton : true,
    // setting these variables to get store the presses state of button and resume them on back button or while redirecting
    activePage : 'publishedprices',
    activeTabBisScreen : 'In-Progress',    
	sortParams : [],
    filterParams : [],
    role:'Bidder',
    localizeParams : function(){
		this.sortParams = [{
            displayText  : Localization.FARMER_CONNECT.PRODUCT,
            value : 'product'
        },{
            displayText  : Localization.FARMER_CONNECT.QUALITY,
            value : 'quality'
        },{
            displayText  : Localization.FARMER_CONNECT.INCO_TERM,
            value : 'incoTerm'
        },{
            displayText  : Localization.FARMER_CONNECT.CROP_YEAR,
            value : 'cropYear'
        },{
            displayText  : Localization.FARMER_CONNECT.LOCATION,
            value : 'location'
        },{
            displayText  : Localization.FARMER_CONNECT.PUBLISHED_PRICE,
            value : 'publishedPrice'
        },{
            displayText  : Localization.FARMER_CONNECT.QUANTITY,
            value : 'quantity'
        },{
            displayText  : Localization.FARMER_CONNECT.USER_NAME,
            value : 'quantity'
        }];
        this.filterParams = [{
            displayText  : Localization.FARMER_CONNECT.PRODUCT,
            type: 1,
            value : 'product'
        },{
            displayText  : Localization.FARMER_CONNECT.QUALITY,
            type: 1,
            value : 'quality'
        },{
            displayText  : Localization.FARMER_CONNECT.INCO_TERM,
            value : 'incoTerm'
        },{
            displayText  : Localization.FARMER_CONNECT.CROP_YEAR,
            type: 1,
            value : 'cropYear'
        },{
            displayText  : Localization.FARMER_CONNECT.LOCATION,
            type: 1,
            value : 'location'
        },{
            displayText  : Localization.FARMER_CONNECT.PUBLISHED_PRICE,
            type: 2,
            value : 'publishedPrice'
        },{
            displayText  : Localization.FARMER_CONNECT.USER_NAME,
            type: 1,
            value : 'username'
        },{
            displayText  : Localization.FARMER_CONNECT.QUANTITY,
            type: 2,
            value : 'quantity'
        }];
	}
});