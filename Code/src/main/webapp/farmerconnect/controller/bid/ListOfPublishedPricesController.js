Ext.define("EkaSmartPlt.ekacac.farmerconnect.controller.bid.ListOfPublishedPricesController", {
    extend: "EkaSmartPlt.platform.controller.AbstractEkaController",
    alias: "controller.ekasmartplt-farmerconnect-controller-bid-listofpublishedpricescontroller",
    init: function() {
        return this.callParent(arguments);
    },
    onChildTap: function(view, itemLocation) {
        if ( FarmerConnectConfigurationObject.role=="Offerer") {
            itemLocation.record.data['prevView'] = "Published Prices";
            this.createComponentHandler({
                componentType: 'ekasmartplt-farmerconnect-view-offer-offerpreview',
                bidJSON: itemLocation.record.data
            });
        } else {
            this.createComponentHandler({
                componentType: 'ekasmartplt-farmerconnect-view-bid-bideditview',
                bidJSON: itemLocation.record.data
            });
        }

    },
    onViewPainted: function() {

        var params = {
            sortBy: Ext.clone(UserPreferences.getSortBy('publishedprices')),
            filters: Ext.clone(UserPreferences.getFilters('publishedprices'))
        }
        var extraparam = {
            "columnName": "User Name",
            "columnType": 1,
            "type": "basic",
            "columnId": "username",
            "value": [sessionStorage.username]
        }
        if ( FarmerConnectConfigurationObject.role=="Offerer") {
            extraparam['operator'] = "in";
            params.filters.push(extraparam);
        } else {
            extraparam['operator'] = "nin";
            params.filters.push(extraparam)
        }


        this.getView().setStore(new Ext.data.Store({
            autoLoad: true,
            proxy: {
                type: 'ajax',
                url: ModernAppConstants.baseURL + 'published-bids',
                reader: {
                    type: 'json'
                },
                extraParams: {
                    requestParams: Ext.encode(params),
                }
            },
            listeners: {
                load: function() {
                    EkaLoader.unmask();
                }
            }

        }));
    }
});