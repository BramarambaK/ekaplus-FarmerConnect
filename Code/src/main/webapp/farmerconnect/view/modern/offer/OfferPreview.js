Ext.define('EkaSmartPlt.farmerconnect.view.offer.OfferPreview', {
    extend: 'Ext.Panel',
    alias: 'widget.ekasmartplt-farmerconnect-view-offer-offerpreview',
    requires: ['EkaSmartPlt.ekacac.farmerconnect.controller.offer.OfferPreviewController',
        'EkaSmartPlt.farmerconnect.view.offer.NewOffer'
    ],
    controller: 'ekasmartplt-farmerconnect-controller-offer-offerpreviewcontroller',
    layout: 'vbox',
    listeners: {
        popwindowDestroy: "popwindowDestroy",
        cancelDeal: "cancelDeal",
        onDeleteDeal: "onDeleteDeal",
        scope: "controller"
    },
    initialize: function() {
        EkaLoader.mask(Localization.GENERAL.LOADING_MASK);
        let _this = this;
        _this.bidJSON = _this.componentData.bidJSON;
        _this.isAccepted = _this.bidJSON.status == 'Accepted';
        _this.isCancelled = _this.bidJSON.status == 'Cancelled';
        _this.isRejected = _this.bidJSON.status == 'Rejected';

        _this.add([{
            xtype: 'toolbar',
            cls: 'footer-toolbar',
            height: 54,
            hidden: _this.bidJSON.status == "In-Progress" && _this.bidJSON.pendingOn == "Offeror" ? false : true,
            docked: 'bottom',
            items: [{
                    xtype: 'button',
                    text: Localization.GENERAL.CANCEL,
                    cls: 'button-transparent-background button-color-blue',
                    handler: 'backButtonHandler',
                    flex: 1,
                    scope: 'controller'
                },
                {
                    xtype: 'button',
                    text: Localization.GENERAL.ACCEPT,
                    cls: 'button-blue',
                    flex: 1,
                    itemId: 'acceptButtonItemId',
                    handler: 'acceptButtonHandler',
                    scope: 'controller'
                }
            ]
        }]);
        _this.addItems();
        EkaLoader.unmask();
        this.callParent(arguments);
    },

    addItems: function() {
        let bidJSON = this.bidJSON;
        let _this = this;
        let buttonOptions = this.getUserOptions(bidJSON);
        this.add({
            xtype: 'titlebar',
            cls: 'secondary-titlebar',
            docked: 'top',
            style: 'overflow: visible;',
            height: 50,
            items: [{
                xtype: 'button',
                handler: 'backButtonHandler',
                scope: 'controller',
                width: 30,
                height: 40,
                cls: 'button-image',
                iconCls: 'icon-close'
            }, {
                xtype: 'label',
                html: bidJSON.bidId,
                cls: 'card-title'
            }, buttonOptions],
        });
        let items = this.getStaticContent(bidJSON);
        if (bidJSON != null) {
            let getConditionalContent = this.getConditionalContent(bidJSON);
            items.push(getConditionalContent);
        }

        if (_this.isAccepted || _this.isRejected || _this.isCancelled) {
            let summaryContent = this.getSummaryContent(bidJSON);
            items.push(summaryContent);
        }
        this.add({
            xtype: 'container',
            height: window.innerHeight - 100, //total height - title bar height
            scrollable: true,
            layout: 'vbox',
            style: 'padding:0 10px',
            cls: 'container-white common-form',
            items: items
        });
    },
    getConditionalContent: function(bidJSON) {
        let actionPending = bidJSON.status == "In-Progress" && bidJSON.pendingOn == "Offeror" ? false : true
        return {
            xtype: 'container',
            layout: 'vbox',
            style: 'padding:0 10px',
            cls: 'container-white common-form',
            hidden: actionPending,
            items: [{
                    xtype: 'checkboxfield',
                    margin: '0 5 0 5',
                    itemId: 'counterCheckboxItemId',
                    label: Localization.FARMER_CONNECT.COUNTER,
                    width: 150,
                    labelAlign: 'right',
                    listeners: {
                        change: 'onCounterCheckChange',
                        scope: 'controller'
                    }
                }, {
                    xtype: 'container',
                    layout: 'hbox',
                    hidden: bidJSON.status == "In-Progress" ? false : true,
                    margin: '0 5 0 5',
                    items: [{
                        xtype: 'numberfield',
                        itemId: 'counterValueItemId',
                        flex: 2,
                        disabled: true,
                        decimals: 5,
                        minValue: 0.01,
                        allowBlank: false
                    }, {
                        xtype: 'label',
                        flex: 1,
                        style: 'margin-left:5px',
                        html: bidJSON.priceUnit
                    }]
                },
                {
                    xtype: 'textareafield',
                    margin: '0 5 20 5',
                    height: 120,
                    value: bidJSON.remarks ? bidJSON.remarks : '',
                    itemId: 'remarksItemId',
                    label: Localization.FARMER_CONNECT.REMARKS + ':',
                    labelAlign: 'top'
                }
            ]
        }
    },

    getStaticContent: function(bidJSON) {
        let statusNull = Ext.isEmpty(bidJSON.status) == true;
        let _this = this;

        return [{
            xtype: 'container',
            layout: 'hbox',
            items: [{
                xtype: 'label',
                margin: '2 0 2 5',
                flex: 1,
                html: Localization.FARMER_CONNECT.OFFER_TYPE + ':'
            }, {
                xtype: 'label',
                margin: '2 0 2 5',
                flex: 1,
                html: bidJSON.offerType ? bidJSON.offerType : Localization.FARMER_CONNECT.PURCHASE
            }]
        }, {
            xtype: 'container',
            layout: 'hbox',
            items: [{
                xtype: 'label',
                margin: '2 0 2 5',
                flex: 1,
                html: Localization.FARMER_CONNECT.OFFER_REF_NUM + ':'
            }, {
                xtype: 'label',
                margin: '2 0 2 5',
                flex: 1,
                html: bidJSON.bidId
            }]
        }, {
            xtype: 'container',
            layout: 'hbox',
            items: [{
                xtype: 'label',
                margin: '2 0 2 5',
                flex: 1,
                html: Localization.FARMER_CONNECT.PRODUCT + ':'
            }, {
                xtype: 'label',
                margin: '2 0 2 5',
                flex: 1,
                html: bidJSON.product
            }]
        }, {
            xtype: 'container',
            layout: 'hbox',
            items: [{
                xtype: 'label',
                margin: '0 0 2 5',
                flex: 1,
                html: Localization.FARMER_CONNECT.QUALITY + ':'
            }, {
                xtype: 'label',
                margin: '0 0 2 5',
                flex: 1,
                html: bidJSON.quality
            }]
        }, {
            xtype: 'container',
            layout: 'hbox',
            items: [{
                xtype: 'label',
                margin: '2 0 2 5',
                flex: 1,
                html: Localization.FARMER_CONNECT.CROP_YEAR + ':'
            }, {
                xtype: 'label',
                margin: '2 0 2 5',
                flex: 1,
                html: bidJSON.cropYear
            }]
        }, {
            xtype: 'container',
            layout: 'hbox',
            items: [{
                xtype: 'label',
                margin: '2 0 2 5',
                flex: 1,
                html: Localization.FARMER_CONNECT.LOCATION + ':'
            }, {
                xtype: 'label',
                margin: '2 0 2 5',
                flex: 1,
                html: bidJSON.location
            }]
        }, {
            xtype: 'container',
            layout: 'hbox',
            hidden: statusNull ? true : false,
            items: [{
                xtype: 'label',
                margin: '2 0 2 5',
                flex: 1,
                html: Localization.FARMER_CONNECT.PUBLISHED_BID_PRICE + ':'
            }, {
                xtype: 'label',
                margin: '2 0 2 5',
                flex: 1,
                html: bidJSON.publishedPrice + ' ' + bidJSON.priceUnit
            }]
        }, {
            xtype: 'container',
            layout: 'hbox',
            items: [{
                xtype: 'label',
                margin: '2 0 2 5',
                flex: 1,
                html: Localization.FARMER_CONNECT.INCO_TERM + ':'
            }, {
                xtype: 'label',
                margin: '2 0 2 5',
                flex: 1,
                html: bidJSON.incoTerm
            }]
        }, {
            xtype: 'container',
            layout: 'hbox',
            hidden: !_this.isRejected,
            items: [{
                xtype: 'label',
                margin: '2 0 2 5',
                flex: 1,
                html: Localization.FARMER_CONNECT.OFFEROR_RATING + ':'
            }, {
                xtype: 'label',
                margin: '2 0 2 5',
                flex: 1,
                html: bidJSON.rating ? bidJSON.rating : Localization.FARMER_CONNECT.NOT_AVAILABLE
            }]
        }, {
            xtype: 'container',
            layout: 'hbox',
            items: [{
                xtype: 'label',
                margin: '2 0 2 5',
                flex: 1,
                html: Localization.FARMER_CONNECT.QUANTITY + ':'
            }, {
                xtype: 'label',
                margin: '2 0 2 5',
                flex: 1,
                html: bidJSON.quantity + " " + (bidJSON.quantityUnit ? bidJSON.quantityUnit : bidJSON.priceUnit.split('/')[1]),
            }]
        }, {
            xtype: 'container',
            layout: 'hbox',
            items: [{
                xtype: 'label',
                margin: '2 0 2 5',
                flex: 1,
                html: Localization.FARMER_CONNECT.SHIPMENT_DATE + ':',
            }, {
                xtype: 'label',
                margin: '2 0 2 5',
                flex: 1,
                html: Ext.Date.format(new Date(bidJSON.shipmentDateInMillis), 'F jS, Y')
            }]
        }, {
            xtype: 'container',
            hidden: !Ext.isEmpty(bidJSON.status),
            items: [{
                xtype: 'container',
                layout: 'hbox',
                items: [{
                    xtype: 'label',
                    margin: '2 0 2 5',
                    flex: 1,
                    html: Localization.FARMER_CONNECT.PUBLISHED_BID_PRICE + ':'
                }, {
                    xtype: 'label',
                    margin: '2 0 2 5',
                    flex: 1,
                    html: '<b>' + (bidJSON.publishedPrice + " " + bidJSON.priceUnit) + '</b>'
                }]
            }]
        }, {
            xtype: 'container',
            layout: 'vbox',
            hidden: Ext.isEmpty(bidJSON.status),
            items: [{
                xtype: 'container',
                layout: 'hbox',
                items: [{
                    xtype: 'label',
                    margin: '2 0 2 5',
                    flex: 1,
                    html: Localization.FARMER_CONNECT.PUBLISHED_BID_PRICE + ':'
                }, {
                    xtype: 'label',
                    margin: '2 0 2 5',
                    flex: 1,
                    html: bidJSON.publishedPrice + ' ' + bidJSON.priceUnit
                }]
            }, {

                xtype: 'container',
                layout: 'hbox',
                items: [{
                    xtype: 'label',
                    margin: '2 0 2 5',
                    flex: 1,
                    html: Localization.FARMER_CONNECT.LATEST_BIDDER_PRICE
                }, {
                    xtype: 'label',
                    margin: '2 0 2 5',
                    flex: 1,
                    html: bidJSON.latestBidderPrice + ' ' + bidJSON.priceUnit
                }]
            }, {
                xtype: 'container',
                layout: 'hbox',
                items: [{
                    xtype: 'label',
                    margin: '2 0 2 5',
                    flex: 1,
                    html: Localization.FARMER_CONNECT.LATEST_OFFEROR_PRICE
                }, {
                    xtype: 'label',
                    margin: '2 0 2 5',
                    flex: 1,
                    html: bidJSON.latestOfferorPrice + ' ' + bidJSON.priceUnit
                }]
            }]
        }]
    },
    getSummaryContent: function(bidJSON) {
        var _this = this;
        let acceptedPrice = bidJSON.updatedBy == 'Bidder' ? (bidJSON.latestOfferorPrice ? bidJSON.latestOfferorPrice : bidJSON.publishedPrice) : (bidJSON.latestBidderPrice ? bidJSON.latestBidderPrice : bidJSON.publishedPrice);
        let acceptedBy = bidJSON.updatedBy == 'Bidder' ? Localization.FARMER_CONNECT.YOU : Localization.FARMER_CONNECT.OFFEROR;
        let message = Localization.FARMER_CONNECT.DEAL_ACCEPETED_BY + acceptedBy + Localization.FARMER_CONNECT.ON + Ext.Date.format(new Date(bidJSON.updatedDate), 'd-M-Y') + ' at ' + acceptedPrice + " " + bidJSON.priceUnit;
        if (!_this.isAccepted)
            message = Localization.FARMER_CONNECT.DEAL_REJECTED_BY + acceptedBy + Localization.FARMER_CONNECT.ON + Ext.Date.format(new Date(bidJSON.updatedDate), 'd-M-Y');
        if (_this.isCancelled) {
            message = Localization.FARMER_CONNECT.DEAL_CANCELLED_BY + acceptedBy + Localization.FARMER_CONNECT.ON + Ext.Date.format(new Date(bidJSON.updatedDate), 'd-M-Y') + ' at ' + acceptedPrice + " " + bidJSON.priceUnit;
        }
        return {
            xtype: 'label',
            hidden: !(_this.isRejected || _this.isAccepted || _this.isCancelled),
            margin: '15 0 5 5',
            cls: 'deal-message',
            html: message
        }
    },
    getUserOptions: function(bidJSON) {
        let _this = this;
        let buttons = [];
        let statusNull = Ext.isEmpty(bidJSON.status) == true;
        if (statusNull) {
            buttons = {
                xtype: 'button',
                text: '...',
                align: 'right',
                cls: 'button-transparent-background button-color-blue button-dotted',
                arrow: false,
                menu: [{
                    text: 'Modify',
                    bidJSON: bidJSON,
                    handler: 'onModify'
                }, {
                    text: 'Delete',
                    bidJSON: bidJSON,
                    handler: 'onDelete'
                }]
            }
        } else {
            if (bidJSON.status == "In-Progress") {
                if (bidJSON.pendingOn == "Offeror") {
                    buttons = {
                        xtype: 'button',
                        text: '...',
                        align: 'right',
                        cls: 'button-transparent-background button-color-blue button-dotted',
                        arrow: false,
                        menu: [{
                            text: 'Reject',
                            bidJSON: bidJSON,
                            handler: 'onBidRejectHandler'
                        }, {
                            text: 'Bid Log',
                            bidJSON: bidJSON,
                            handler: 'onBidLogsHandler'
                        }]
                    }
                } else {

                    buttons = {
                        xtype: 'button',
                        text: '...',
                        align: 'right',
                        cls: 'button-transparent-background button-color-blue button-dotted',
                        arrow: false,
                        menu: [{
                            text: 'Bid Log',
                            bidJSON: bidJSON,
                            handler: 'onBidLogsHandler'
                        }]
                    }
                }
            } else {

                if (bidJSON.status == "Accepted") {
                    buttons = {
                        xtype: 'button',
                        text: '...',
                        align: 'right',
                        cls: 'button-transparent-background button-color-blue button-dotted',
                        arrow: false,
                        menu: [{
                            text: 'Cancel',
                            bidJSON: bidJSON,
                            handler: 'onCancelOffer'
                        }, {
                            text: 'Bid Log',
                            bidJSON: bidJSON,
                            handler: 'onBidLogsHandler'
                        }]
                    }

                } else {
                    buttons = {
                        xtype: 'button',
                        text: '...',
                        align: 'right',
                        cls: 'button-transparent-background button-color-blue button-dotted',
                        arrow: false,
                        menu: [{
                            text: 'Bid Log',
                            bidJSON: bidJSON,
                            handler: 'onBidLogsHandler'
                        }]
                    }
                }
            }
        }
        return buttons;

    }
});