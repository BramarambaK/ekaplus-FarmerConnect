Ext.define('EkaSmartPlt.ekacac.farmerconnect.view.offer.ManageBids', {
    extend: 'EkaSmartPlt.platform.view.general.grid.base.BaseGridWithGrouping',
    alias: 'widget.ekacacapp-farmerconnect-view-offer-managebids',
    controller: "ekacacapp-farmerconnect-controller-bid-bidlistingcontroller",
    requires: ['EkaSmartPlt.ekacac.farmerconnect.controller.bid.BidListingController',
        'EkaSmartPlt.ekacac.farmerconnect.view.bid.BidConfirmationWindow',
        'EkaSmartPlt.ekacac.farmerconnect.view.bid.BidCounterConfirmationWindow'
    ],
    defaults: {
        frame: false
    },
    reference: 'manageBidsOfferor',
    scrollable: true,
    cls: 'custom-grid-plane grid-panel-sub-page row-over-style custom-checkbox-selection bids-listing',
    width: window.innerWidth - 16,
    margin: '10',
    showOnlyTotalRecords: true,
    listeners: {
        boxready: 'onListReady',
        cellclick: 'onGridCellClick',
        headerclick: 'onGridHeaderClick',
        scope: 'controller'
    },
    selModel: {
        selType: 'checkboxmodel',
        mode: 'SIMPLE'
    },
    applicableOpertions: {
        "Accepted": ['cancelBid'],
        "Rejected": [],
        "In-Progress": ['acceptBid', 'rejectBid', 'counterBid'],
        "Canceled": [],
    },
    tbar: {
        cls: 'breadcrumb-toolbar breadcrumb-toolbar-background-white toolbar-dataview',
        items: [{
                xtype: 'label',
                cls: 'label-main-title',
                html: 'Offer - Manage Bids'
            }, {
                xtype: 'tbfill'
            }, {
                text: 'Operations',
                cls: 'button-outline',
                iconCls: 'operation-arrow',
                iconAlign: 'right',
                xtype: 'ekacacapp-button',
                itemId: 'operations_id',
                perm_code: 'STD_APP_BIDS_ACCEPT',
                menu: [{
                    text: 'Accept',
                    operation: 'Accept',
                    itemId: 'acceptButtonItemId',
                    perm_code: 'STD_APP_BIDS_ACCEPT',
                    hidden: true,
                    scope: 'controller',
                    handler: 'acceptButtonHandler',
                    cls: 'button-primary tabpanel-button',
                    reference: 'acceptBid'
                }, {
                    text: 'Reject',
                    operation: 'Reject',
                    itemId: 'rejectButtonItemId',
                    perm_code: 'STD_APP_BIDS_REJECT',
                    scope: 'controller',
                    handler: 'rejectButtonHandler',
                    hidden: true,
                    cls: 'button-primary tabpanel-button',
                    reference: 'rejectBid'
                }, {
                    text: 'Counter',
                    operation: 'Counter',
                    itemId: 'counterButtonItemId',
                    perm_code: 'STD_APP_BIDS_COUNTER',
                    scope: 'controller',
                    handler: 'counterButtonHandler',
                    hidden: true,
                    cls: 'button-primary tabpanel-button',
                    reference: 'counterBid'
                }, {
                    text: 'Cancel this deal',
                    operation: 'Cancel',
                    itemId: 'cancelButtonItemId',
                    perm_code: 'STD_APP_BIDS_CANCEL',
                    scope: 'controller',
                    handler: 'cancelButtonHandler',
                    hidden: true,
                    cls: 'button-primary tabpanel-button',
                    reference: 'cancelBid'
                }],
                scope: this
            },
            /*{
            			xtype: 'ekacacapp-button', 
            			text: 'Accept',
            			style:'margin:3px 10px 0px 0px',
            			itemId:'acceptButtonItemId' ,
            			cls: 'button-primary tabpanel-button',
            			disabled : true,
            			perm_code : 'STD_APP_BIDS_ACCEPT',
            			scope : 'controller',
            			handler	:'acceptButtonHandler'
            		},{
            			xtype: 'ekacacapp-button', 
            			text: 'Reject',
            			style:'margin:3px 10px 0px 0px',
            			itemId:'rejectButtonItemId' ,
            			perm_code : 'STD_APP_BIDS_REJECT',
            			cls: 'button-primary tabpanel-button',
            			disabled : true,
            			scope : 'controller',
            			handler	:'rejectButtonHandler'
            		},{
            			xtype: 'ekacacapp-button', 
            			text: 'Counter',
            			style:'margin:3px 10px 0px 0px',
            			itemId:'counterButtonItemId' ,
            			perm_code : 'STD_APP_BIDS_COUNTER',
            			cls: 'button-primary tabpanel-button',
            			disabled : true,
            			scope : 'controller',
            			handler	:'counterButtonHandler'
            		}*/
        ]
    },
    initComponent: function() {
        Ext.apply(this, {
            store: new Ext.data.Store({
                fields: [{
					name: 'deliveryFromDateInMillis', 
					type : 'date', 
					dateFormat : 'time'        
				},{
					name: 'deliveryToDateInMillis', 
					type : 'date', 
					dateFormat : 'time'        
				},]
            }),
            columns: this.createGridColumns()
        });
        this.columnDisplayNames = {
            refId: 'Ref Id',
            bidId: 'Bid Id',
            customerId: "Bidder's Id",
            applicableRoles: "Applicable Role(s)",
            agentId: "Agent's Id",
            quality: 'Quality',
            quantity: 'Quantity',
            location: 'Location',
            publishedPrice: 'Published Price',
            latestBidderPrice: 'Latest Bidder Price',
            latestOfferorPrice: 'Latest Offeror Price',
            updatedDate: 'Updated Date',
            priceUnit: 'Price Unit'
        };
        this.callParent(arguments);
    },

    createGridColumns: function() {
        return [{
                text: 'Offer Type',
			width:'8%',
                dataIndex: 'offerType'
            },
            {
                text: 'Offer Ref Id',
            width:'8%',
                dataIndex: 'bidId',
                renderer: function(value, column, record) {
                    return '<a href="#">' + record.get('refId') + '</a>';
                }
            }, {
                text: "Applicable Role(s)",
                dataIndex: 'rolesToPublish',
                flex: 0.5,
                filter: {
                    type: 'list'
                },
                multiValueTextData: true,
                renderer: function(value, column, record) {
                    return "<span title='" + record.get('applicableRoles') + "'>" + record.get('applicableRoles') + "</span>";
                }
            }, {
                text: 'Product',
                dataIndex: 'product',
			width:'8%',
                filter: {
                    type: 'list'
                },
                multiValueTextData: true
            }, {
                text: 'Quality',
                dataIndex: 'quality',
		    width:'8%',
                filter: {
                    type: 'list'
                },
                multiValueTextData: true
            }, {
                text: 'Crop Year',
                dataIndex: 'cropYear',
		        width:'8%',
                filter: {
                    type: 'list'
                },
                multiValueTextData: true
            }, {
                text: 'Packing Size',
                hidden: PlatformAppConstants.FarmerConnectSettings.offerType == "basic",
                dataIndex: 'packingSize',
                width:'8%',
                filter: {
                    type: 'list'
                },
                multiValueTextData: true
            },
            {
                text: 'Packing Type',
                hidden: PlatformAppConstants.FarmerConnectSettings.offerType == "basic",
                dataIndex: 'packingType',
                width:'8%',
                filter: {
                    type: 'list'
                },
                multiValueTextData: true
            },
            {
                text: 'Payment Term',
                hidden: PlatformAppConstants.FarmerConnectSettings.offerType == "basic",
                dataIndex: 'paymentTerms',
                width:'8%',
                filter: {
                    type: 'list'
                },
                multiValueTextData: true
            }, {
                text: 'Location',
                dataIndex: 'location',
                width:'8%',
                filter: {
                    type: 'list'
                },
                multiValueTextData: true
            }, {
                text: 'Status',
                tdCls: 'bid-status-icons',
                dataIndex: 'status',
                width:'8%',
                renderer: function(value, column, record) {
                    var html = '';
                    //use pending On field
                    if (value == 'Accepted') {
                        html = "<span title='" + value + "' >" + '<img src = "classic/resources/images/farmerConnect/bid-success.png" alt = "' + value + '" />' + "</span>";
                    } else if (value == 'Rejected') {
                        html = "<span title='" + value + "' >" + '<img src = "classic/resources/images/farmerConnect/bid-rejected.png" alt = "' + value + '" />' + "</span>";
                    } else if (value == 'Cancelled') {
                        html = "<span title='" + value + "' >" + '<img src = "classic/resources/images/farmerConnect/bid-cancelled.png" alt = "' + value + '" />' + "</span>";
                    } else if (record.data.pendingOn == 'Offeror') {
                        html = "<span title='Received' >" + '<img src = "classic/resources/images/farmerConnect/bid-received.png" alt = "' + value + '" />' + "</span>";
                    } else if (record.data.pendingOn == 'Bidder') {
                        html = "<span title='Sent' >" + '<img src = "classic/resources/images/farmerConnect/bid-sent.png" alt = "' + value + '" />' + "</span>";
                    }
                    return html;
                }
            }, {
                text: 'Published Price',
                dataIndex: 'publishedPrice',
                width:'8%',
                filter: {
                    type: 'number'
                },
                renderer: function(value, column, record) {
                    return Ext.isEmpty(value) ? value : value + ' ' + record.get('priceUnit');
                }
            },{
                text: 'IncoTerm',
                dataIndex: 'incoTerm',
		    width:'8%',
                filter: {
                    type: 'list'
                },
                multiValueTextData: true
            }, {
                text: 'Quantity',
                dataIndex: 'quantity',
                width:'8%',
                filter: {
                    type: 'number'
                },
                renderer: function(value, column, record) {
                    return Ext.isEmpty(value) ? value : value + ' ' + record.get('quantityUnit');
                }
            }, {
				dataIndex: 'deliveryFromDateInMillis',
				text: 'Delivery Period',
				width:'8%',
				xtype: 'datecolumn',
                format: 'd-m-Y',
                renderer:function(value, column, record){
                    return  new Date(record.get('deliveryFromDateInMillis')).toLocaleDateString() +' to '+new Date(record.get('deliveryToDateInMillis')).toLocaleDateString()
                },
				filter: {
					type: 'date'
				}
			}, {
                text: 'Bidder Price',
                dataIndex: 'latestBidderPrice',
    		width:'8%',
                filter: {
                    type: 'number'
                }
            }, {
                text: 'Offeror Price',
                dataIndex: 'latestOfferorPrice',
		    width:'8%',
                filter: {
                    type: 'number'
                }
            },
            /* {
			text : 'Offeror Name',
            dataIndex : 'offerorName',
		    width:'8%',
            filter : {
				type: 'list'
            },
			multiValueTextData : true
        },{
			text : 'Offeror Mobile No.',
            dataIndex : 'offerorMobileNo',
			flex : 0.3,
			filter : {
				type: 'number'
			}
		},{
            text : 'Bid Id',
            dataIndex : 'bidId',
			flex : 0.3,
            filter : {
				type: 'list'
			},
			multiValueTextData : true
		},{
			text : "Bidder's Id",
			dataIndex : 'customerId',
			flex : 0.5,
            filter : {
				type: 'list'
			},
			multiValueTextData : true
		},{
			text : "Agent's Id",
			dataIndex : 'agentId',
			flex : 0.5,
            filter : {
				type: 'list'
			},
			multiValueTextData : true,
			renderer : function(value, column, record){
				return record.get('agentId') ? record.get('agentId') : 'NA';
			}
		}, */
            {
                text: 'Price Unit',
                dataIndex: 'priceUnit',
                flex: 0.5,
                filter: {
                    type: 'list'
                },
                multiValueTextData: true
            }
        ];
    },

    refreshHandler: function() {
        let url = this.up().componentData.listingUrl;
        let operator = this.up().componentData.operator;
        var extraparam = {
            "columnName": "User Name",
            "columnType": 1,
            "type": "basic",
            "columnId": "username",
            "value": [sessionStorage.userName],
            "operator": operator
        }
        var response = EkaAjaxService.performSyncAjaxCall(url, {
            method: 'GET',
            params: {
                requestParams: Ext.encode({
                    sortBy: "",
                    filters: [extraparam],
                    pagination: ""
                })
            }
        });
        this.store.loadData(response);
    }
});
