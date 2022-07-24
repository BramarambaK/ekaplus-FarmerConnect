Ext.define("EkaSmartPlt.ekacac.farmerconnect.controller.bid.ListOfBidsController", {
	extend: "EkaSmartPlt.platform.controller.AbstractEkaController",
	alias:"controller.ekasmartplt-farmerconnect-controller-bid-listofbidscontroller",
	init: function() {
		return this.callParent(arguments);
    },
    onChildTap : function(view, itemLocation){
        var bidsStatusButton = this.getView().up().down('#bidsStatusButtonItemId');
        var value = bidsStatusButton.getValue();
        if( FarmerConnectConfigurationObject.role!="Offerer"){
            if(value ==  "In-Progress"){
                //change this condition as per new design
                if(itemLocation.data.pendingOn == 'Offeror')
                    this.createComponentHandler({
                    componentType : 'ekasmartplt-farmerconnect-view-bid-biddetailview',
                    // refId : itemLocation.data.refId
                    bidJSON : itemLocation.data
                });
                else
                    this.createComponentHandler({
                        componentType : 'ekasmartplt-farmerconnect-view-bid-bideditview',
                        bidJSON : itemLocation.data
                    });
            }
            else{
                this.createComponentHandler({
                    componentType : 'ekasmartplt-farmerconnect-view-bid-biddetailview',
                    // refId : itemLocation.data.refId,
                    bidJSON : itemLocation.data
                });
            }      
        }
        else{
            this.createComponentHandler({
                componentType : 'ekasmartplt-farmerconnect-view-offer-offerpreview',
                bidJSON : itemLocation.data
            });  
        }
         
    },
    onViewPainted : function(){
	//	EkaLoader.mask(Localization.GENERAL.LOADING_MASK);
        var bidsStatusButton = this.getView().up().down('#bidsStatusButtonItemId');
        var value = bidsStatusButton.getValue();
        var pageFilters = [];
        pageFilters.push(value);
        if(value == "Accepted"){
            pageFilters.push("Cancelled");
        }
        var params = {
            sortBy : UserPreferences.getSortBy('bid'),
            filters : Ext.clone(UserPreferences.getFilters('bid'))
        };
        params.filters.push({
            columnId: "status",
            columnName: "status",
            columnType: 1,
            operator: "in",
            type: "basic",
            value: pageFilters
        });
        var extraparam = {
            "columnName": "User Name",
            "columnType": 1,
            "type": "basic",
            "operator":  FarmerConnectConfigurationObject.role=="Offerer"?"in":"nin",
            "columnId":  "username",
            "value": [sessionStorage.username]
        }
        params.filters.push(extraparam);
      /*  if (localStorage.Offerer == "true") {
            extraparam['operator']="in";          
            params.filters.push(extraparam);
        } else {
            extraparam['operator']="nin";
            params.filters.push(extraparam)
        }*/
        var url='';
        if( FarmerConnectConfigurationObject.role!="Offerer"){
            url=ModernAppConstants.baseURL + 'bids/farmer';
        }
        else{
            url=ModernAppConstants.baseURL + 'bids/offeror';
        }
       
        this.getView().setStore(new Ext.data.Store({
            autoLoad : true,
            proxy: {
                type: 'ajax',
                url: url,
                reader: {
                    type: 'json'	
                },
                extraParams : {
					requestParams : Ext.encode(params),
				}
            },
			listeners : {
				load : function(){
					EkaLoader.unmask();
				}
			}
        }));
    }
});