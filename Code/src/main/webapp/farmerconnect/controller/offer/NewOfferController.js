Ext.define('EkaSmartPlt.ekacac.farmerconnect.controller.offer.NewOfferController', {

    extend: "EkaSmartPlt.platform.controller.AbstractEkaController",
    alias: "controller.ekasmartplt-farmerconnect-controller-offer-newoffercontroller",
    init: function() {
        this.jsonObject = {};
        this.validate = true;
        this.activeItem ='Sale';
        this.modify = false;
        this.autoFill = false;
        this.fieldValuesJson = {};
        this.fieldValidation=true;
        this.isModern=false;
        this.previousSelectedValues = {}
        return this.callParent(arguments);
    },
    setModifyValues: function(PrevData) {
        this.modify = true;
        this.previousSelectedValues = PrevData;
        if(this.isModern)
        this.lookupReference('newOfferLabel').setHtml(PrevData.bidId);
    },
    onViewPainted: function() {
        let _this = this;
        if(Ext.isModern){
            _this.isModern=true
        }
        else{
            _this.isModern=false
        }
        if (_this.getView().componentData.bidJSON) {
            let bidJSON = _this.getView().componentData.bidJSON;
            if (bidJSON.status == "Modify") {
                _this.setModifyValues(_this.getView().componentData.bidJSON);
                _this.autoFill = true;
                _this.activeItem = bidJSON.offerType;

            }
        }
        let fields = ['cropYear', 'location', 'quantityUnit', 'quality', 'product', 'incoTerm', 'priceUnit','packingType','packingSize','paymentTerms'];
        let sucessCallBack = function(response) {
            let json = {};
            for (let i = 0; i < response.length; i++) {
                json[fields[i]] = response[i];
            }
            _this.fieldValuesJson = json;
            _this.configureFields(json);
         if(_this.isModern)
            EkaLoader.unmask();
        }
        let errorCallBack = function() {
            EkaLoader.unmask();
            EkaToast.show(Localization.FARMER_CONNECT.OFFER_VALUES_REJECT);
        }
        let errorWebCallBack = function() {
            Ext.Msg.alert("Error");
        }
  
     if(_this.isModern){
         EkaLoader.mask(Localization.GENERAL.LOADING_MASK);
         OfferService.getNewOfferFieldValues(fields, sucessCallBack, errorCallBack);
        } else{
        FarmerConnectService.getNewOfferFieldValues(fields, sucessCallBack, errorWebCallBack);
       }
     },

    configureFields: function(json){
        var _this=this;
        if(Ext.isModern){
            _this.constructMobileView(json)
        }
        else{
            _this.constructWebView(json)

        }
    },

    constructWebView:function(json){
        let _this=this;
        _this.getView().dockedItems.items[1].setHidden(false);
        let  offerType={
            xtype: 'container',
            items: [{
                xtype: 'label',
                html: 'Offer Type'
            }, {
                xtype: 'combobox',
                width: 200,
                labelSeparator: ' ',
                name: 'offerType',
                forceSelection: true,
                value: _this.autoFill ? _this.previousSelectedValues.offerType : '',
                readOnly: _this.autoFill,
                store: Ext.create('Ext.data.Store', {
                    data:[{"key":"Sales","value":'Sale'},{"key":'Purchase',"value":'Purchase'}],
                }),
                queryMode: 'local',
                allowBlank:false,
                displayField: 'key',
                valueField: 'value',
            }]
        };
        let formItems=_this.getFirstPageFields(json);
         formItems.unshift(offerType);
         let items= formItems.concat(_this.getSecondPageFieldsWeb(json));
        let formPanel = {
            xtype: 'form',
            width:'100%',
            reference:'newofferpanelreference',
            layout: {
                type: 'table',
                columns: 4
            },
            cls:'newOffer-table',
            defaults: {
                bodyStyle: 'padding:100px,width:1000px',
            },
            items: items
        };
        _this.getView().add(formPanel);
    },
    cancelButtonHandler:function(){
        let _this=this;
        _this.getView().close();

    },
    saveButtonHandler:function(){
       let _this=this;
    let temp = _this.lookupReference('newofferpanelreference');
    let checkflag = _this.validateWeb(temp);
       if(checkflag){
       let request=_this.lookupReference('newofferpanelreference').getValues();

       _this.jsonObject=Ext.clone(request);

       //----Date Format Conversion into UTC Format--//
       let expiryDate=_this.lookupReference('expiryDateISOString').getValue();
       let deliveryFromDate=_this.lookupReference('deliveryFromDateISOString').getValue();
       let deliveryToDate=_this.lookupReference('deliveryToDateISOString').getValue();


       if(deliveryFromDate<=deliveryToDate){
        request.expiryDateISOString=Ext.Date.add(expiryDate, Ext.Date.HOUR,23.99).toISOString();
        request.deliveryFromDateISOString=Ext.Date.add(deliveryFromDate, Ext.Date.HOUR,23.99).toISOString();
        request.deliveryToDateISOString=Ext.Date.add(deliveryToDate, Ext.Date.HOUR,23.99).toISOString();
 
 
        //----Date Format Conversion into UTC Format--//
        _this.jsonObject=request;
        let modifySuccessCallBack=function() {
         _this.getView().successCallback();
           _this.getView().close();
       
       }
        let sucessCallBack = function(response) {
         _this.getView().removeAll();   
         _this.getView().add(_this.getUserOptionsAfterSave(response));
 
     }
     let errorCallBack = function() {
         Ext.Msg.alert("error");
     }
     if(_this.modify){
         delete(request.offerType);
        FarmerConnectService.updateOffer('PUT',_this.previousSelectedValues.bidId,request, modifySuccessCallBack, errorCallBack);
     }else{
         FarmerConnectService.createNewOffer(request, sucessCallBack, errorCallBack);
 
     }
       }
       else{
        Ext.Msg.alert("Alert","kindly enter proper delivery dates");
       }
    }
    else{      
          Ext.Msg.alert("Alert","kindly enter Mandatory Values");

    }


    },
  
    getUserOptionsAfterSave :function(response){

        let _this=this;
        _this.getView().dockedItems.items[1].setHidden(true);

        let components = {
            xtype: 'container',
           // style: 'margin:10px 10px 20px 10px',

           layout: {
                align: 'middle',
                pack: 'center',
                type: 'vbox'
            },


            items: [{
                xtype: 'button',
                disabled: true,
                cls: 'button-transparent-bg icon-height-32 link-button',
                height: 40,
                width: 40,
                margin: '5 0 0 0',
                iconCls: 'icon-success-green-tick'
            },{
                xtype: 'label',
                margin: '6 0 0 0',
                cls: 'offerpage status-message',
                html: '<b>' + response.bidId + '</b>' + ' is Successfully Published'
            },{
                xtype: 'button',
                margin: '25 0 0 0',
                cls: 'button-transparent-bg button-with-background font-14 link-button',
                text: 'Create New Blank Offer',
                handler: "createBlankOfferWeb"
            }, {
                xtype: 'button',
                margin: '10 0 0 0',
                cls: 'button-transparent-bg button-with-background font-14 link-button',
                text: 'Duplicate This Offer',
                handler: "createDuplicateOfferWeb"
            }, {
                xtype: 'button',
                margin: '15 0 0 0',
                cls: 'button-transparent-bg font-14 link-button',
                text: 'Exit',
                handler: "exitHandlerWeb"
            }]
            
        };
        return components;

    },
    createBlankOfferWeb:function(){
        let _this=this;
        _this.getView().removeAll();
        _this.autoFill=false;
        _this.configureFields( _this.fieldValuesJson);  
      },
    createDuplicateOfferWeb:function(){
        let _this=this;
        _this.getView().removeAll();
        let jsonObject =_this.jsonObject;
        _this.autoFill=true;
        _this.previousSelectedValues=jsonObject;
        _this.configureFields( _this.fieldValuesJson);  
      },
    exitHandlerWeb:function(){
        if(this.getView().parentView){
            let componentData={
                'componentType': this.getView().parentView,
                'listingUrl':this.getView().listingUrl,
                'operator':this.getView().operator,
            }
            this.getView().close();
            this.generalComponentHandler(componentData)  
        }
        else{
            this.getView().successCallback();
            this.getView().close();

        }
    
    },
    constructMobileView: function(json) {
        let _this = this;
        _this.addHomeButtons();
        let first = _this.createFirstPage(json);
        let second = _this.createSecondPage(json);
        let panel = {
            xtype: 'panel',
            layout: 'card',
            itemId: 'newofferpanel',
            activeItem: 0,
            reference: 'newofferpanelreference',
            items: [first, second]
        };

        _this.getView().add(panel);
        _this.addFooter();
    },
    homeButtonsClick: function(me) {
        let _this = this;
        _this.lookupReference('newofferpanelreference').destroy();
        _this.lookupReference('newOfferSegmentedButtonsContainerreference').destroy();
        _this.lookupReference('newOfferNavButtonsContainerreference').destroy();
        _this.activeItem = me._value;
        _this.configureFields(_this.fieldValuesJson);
    },
    addFooter: function() {
        let _this = this;
        _this.getView().add([{
            xtype: 'toolbar',
            cls: 'footer-toolbar',
            reference: 'newOfferNavButtonsContainerreference',
            height: 54,
            docked: 'bottom',
            items: [{
                    xtype: 'button',
                    text: Localization.GENERAL.CANCEL,
                    reference: 'newOfferBackButton',
                    cls: 'button-transparent-background button-color-blue',
                    handler: 'navigationBackHandler',
                    flex: 1,
                    scope: 'controller'
                },
                {
                    xtype: 'button',
                    text: Localization.FARMER_CONNECT.NEXT,
                    cls: 'button-blue',
                    flex: 1,
                    itemId: 'acceptButtonItemId',
                    reference: 'newOfferNextButton',
                    handler: 'navigationNextHandler',
                    scope: 'controller'
                }
            ]
        }])


    },
   
    addHomeButtons: function() {
        let _this = this;
        _this.getView().add({
            xtype: 'container',
            cls: 'form-container-items',
            margin: '10px 10px 0px 10px',
            itemId: 'newOfferSegmentedButtonsContainerItemId',
            reference: 'newOfferSegmentedButtonsContainerreference',
            layout: 'vbox',
            items: [{
                xtype: 'label',
                html: Localization.FARMER_CONNECT.OFFER_TYPE + ':'
            }, {
                xtype: 'container',
                layout: 'hbox',
                padding: '0px',
                items: [{
                    xtype: 'button',
                    text: Localization.FARMER_CONNECT.SALES,
                    cls: _this.activeItem == 'Sale' ? 'button-secondary' : 'button-secondary-inactive',
                    iconCls: _this.activeItem == 'Sale' ? 'icon-blue-tick' : '',
                    flex: 1,
                    value: 'Sale',
                    disabled: _this.modify,
                    handler: function(me) {
                        _this.homeButtonsClick(me);
                    },
                }, {
                    xtype: 'button',
                    text: Localization.FARMER_CONNECT.PURCHASE,
                    flex: 1,
                    cls: _this.activeItem == Localization.FARMER_CONNECT.PURCHASE? 'button-secondary' : 'button-secondary-inactive',
                    iconCls: _this.activeItem ==  Localization.FARMER_CONNECT.PURCHASE? 'icon-blue-tick' : '',
                    value: 'Purchase',
                    disabled: _this.modify,
                    handler: function(me) {
                        _this.homeButtonsClick(me);
                    }
                }]
            }]
        });
    },

    navigationNextHandler: function() {
        let _this = this;
        let panel = this.lookupReference('newofferpanelreference');
        let indexofCurrentItem = panel.items.indexOf(panel.getActiveItem());
        let totalItems = panel.items.items.length;
        _this.validate = true;
        _this.fieldValidation=true;
        _this.getFieldValues(panel.getActiveItem());

        if (_this.validate &&  _this.fieldValidation) {
            if (indexofCurrentItem == 0) {
                this.lookupReference('newOfferSegmentedButtonsContainerreference').setHidden(true)
            }

            if (indexofCurrentItem != totalItems - 1) {
                if (indexofCurrentItem == totalItems - 2) {
                    if (_this.modify) {
                        _this.lookupReference('newOfferNextButton').setText(Localization.FARMER_CONNECT.UPDATE);

                    } else {
                        _this.lookupReference('newOfferNextButton').setText(Localization.FARMER_CONNECT.PUBLISH);

                    }
                }
                _this.lookupReference('newOfferBackButton').setText(Localization.FARMER_CONNECT.BACK);
                panel.setActiveItem(indexofCurrentItem + 1)

            } else {
                let sucessCallBack = function(response) {
                    EkaLoader.unmask();
                    if (response.bidId != null) {
                        _this.bidId = response.bidId;
                    }
                    let components = {
                        xtype: 'container',
                        style: 'margin:10px 10px 20px 10px',
                        items: [{
                            xtype: 'button',
                            disabled: true,
                            cls: 'newoffer-confimation-icon',
                            iconCls: 'icon-green-tick'
                        }, {
                            xtype: 'label',
                            style: 'margin-top:10px',
                            cls: 'offerpage status-message',
                            html: '<b>' + _this.bidId + '</b>' + Localization.FARMER_CONNECT.OFFER_CREATED
                        }]
                    };
                    let buttons = [{
                        text: Localization.FARMER_CONNECT.CREATE_BLANK_OFFER,
                        action: "createBlankOffer"
                    }, {

                        text: Localization.FARMER_CONNECT.DUPLICATE_OFFER,
                        action: "createDuplicateOffer"
                    }, {

                        text: Localization.FARMER_CONNECT.EXIT,
                        action: "exitHandler"
                    }];
                    let popWindow = Ext.create("EkaSmartPlt.farmerconnect.view.offer.FarmerConnectUserOptionsWindow", {
                        buttonsConfig: buttons,
                        components: components,
                        parentView: _this.getView(),
                        width: '70%',
                        height: '40%',
                    });
                    popWindow.show();
                };

                let modifySuccessCallBack = function() {
                    EkaLoader.unmask();
                    EkaToast.show(Localization.FARMER_CONNECT.OFFER_UPDATE_SUCCESS);
                    HistoryTracker.pop();
                    var contentPanel = Ext.ComponentQuery.query('#contentPanelItemId')[0];
                    contentPanel.controller.refereshContentPanel(null, HistoryTracker.pop(), Ext.emptyFn);
                }
                let createErrorCallBack = function() {
                    EkaLoader.unmask();
                    EkaToast.show(Localization.FARMER_CONNECT.OFFER_CREATE_FAILURE);

                }
                let modifyErrorCallBack = function() {
                    EkaLoader.unmask();
                    EkaToast.show(Localization.FARMER_CONNECT.OFFER_UPDATE_FAILURE);

                }
                EkaLoader.mask(Localization.GENERAL.LOADING_MASK);
                if (_this.modify) {
                    OfferService.updateOffer('PUT', _this.previousSelectedValues.bidId, _this.jsonObject, modifySuccessCallBack, modifyErrorCallBack);
                } else {
                    _this.jsonObject['offerType'] = _this.activeItem;
                    OfferService.createNewOffer(_this.jsonObject, sucessCallBack, createErrorCallBack);
                }
            }
        } else {
            if(!_this.validate){
            EkaToast.show(Localization.FARMER_CONNECT.PROVIDE_ALL_VALUES);
            }

        }

    },


    navigationBackHandler: function() {
        let _this = this;
        let panel = this.lookupReference('newofferpanelreference');
        let indexofCurrentItem = panel.items.indexOf(panel.getActiveItem());
        if (indexofCurrentItem == 1) {
            this.lookupReference('newOfferSegmentedButtonsContainerreference').setHidden(false)

        }
        if (indexofCurrentItem != 0) {
            if (indexofCurrentItem == 1) {
                _this.lookupReference('newOfferBackButton').setText(Localization.FARMER_CONNECT.CANCEL);
            }
            panel.setActiveItem(indexofCurrentItem - 1);
            _this.lookupReference('newOfferNextButton').setText(Localization.FARMER_CONNECT.NEXT);


        } else {
            _this.backButtonHandler();
        }

    },
    createBlankOffer: function(window) {
        let _this = this;
        _this.destroyingPrevViews(window);
        _this.autoFill=false;
        _this.configureFields( _this.fieldValuesJson);

    },
    destroyingPrevViews:function(window){
        var _this=this;
        window.destroy();
        _this.lookupReference('newofferpanelreference').destroy();
        _this.lookupReference('newOfferSegmentedButtonsContainerreference').destroy();
        _this.lookupReference('newOfferNavButtonsContainerreference').destroy();
    },
    createDuplicateOffer: function(window) {
        let _this = this;
        let jsonObject =_this.jsonObject;
        _this.destroyingPrevViews(window);
        _this.autoFill=true;
        _this.previousSelectedValues=jsonObject;
        _this.configureFields( _this.fieldValuesJson);
    },
    exitHandler: function(window) {
        var _this = this;
        window.destroy();
        _this.backButtonHandler();
    },
    createFirstPage: function(json) {
        let _this = this;
        let page = {
            xtype: 'container',
            cls: 'form-container-items',
            items: _this.getFirstPageFields(json)
        }
        return page;

    },
    getFirstPageFields:function(json){
        var _this=this;
     return([{
        xtype: 'container',
        items: [{
            xtype: 'label',
            html: _this.isModern?Localization.FARMER_CONNECT.PRODUCT + ':':'Product'
        }, {
            xtype: 'combobox',
            width: 200,
            labelSeparator: ' ',
            name: 'product',
            allowBlank:false,
            value: _this.autoFill ? _this.previousSelectedValues.product : '',
            forceSelection: true,
            store: Ext.create('Ext.data.Store', {
                data: json.product.data
            }),
            queryMode: 'local',
            displayField: 'value',
            valueField: 'value',
        }]
    }, {
        xtype: 'container',
        items: [{
            xtype: 'label',
            html:_this.isModern?Localization.FARMER_CONNECT.QUALITY + ':':'Quality'
        }, {
            xtype: 'combobox',
            width: 200,
            labelSeparator: ' ',
            name: 'quality',
            forceSelection: true,
            allowBlank:false,
            value: _this.autoFill ? _this.previousSelectedValues.quality : '',
            store: Ext.create('Ext.data.Store', {
                data: json.quality.data
            }),
            queryMode: 'local',
            displayField: 'value',
            valueField: 'value',
        }]

    }, {
        xtype: 'container',
        items: [{
            xtype: 'label',
            html: _this.isModern?Localization.FARMER_CONNECT.CROP_YEAR + ':':'Crop Year'
        }, {
            xtype: 'combobox',
            width: 200,
            labelSeparator: ' ',
            name: 'cropYear',
            forceSelection: true,
            value: _this.autoFill ? _this.previousSelectedValues.cropYear : '',
            store: Ext.create('Ext.data.Store', {
                data: json.cropYear.data
            }),
            queryMode: 'local',
            allowBlank:false,
            displayField: 'value',
            valueField: 'value',
        }]

    }, {
        xtype: 'container',
        layout: 'vbox',
        items: [{
            xtype: 'label',
            html: _this.isModern?Localization.FARMER_CONNECT.PUBLISHED_PRICE + ':':'Published Price'
        }, {
            xtype: 'container',
            layout: 'hbox',
            padding: '0px',
            items: [{
                xtype: 'numberfield',
                width: 98,
                name: 'publishedPrice',
                value: _this.autoFill ? _this.previousSelectedValues.publishedPrice : '',
                allowBlank:false,
                margin: '0 0 0 0',
                minValue: 0,
                allowBlank: false
            }, {
                xtype: 'combobox',
                width: 100,
                labelSeparator: ' ',
                name: 'priceUnit',
                //flex: 1,
                margin: '0 0 0 0',
                forceSelection: true,
                value: _this.autoFill ? _this.previousSelectedValues.priceUnit : '',
                store: Ext.create('Ext.data.Store', {
                    data: json.priceUnit.data
                }),
                queryMode: 'local',
                displayField: 'value',
                allowBlank:false,
                valueField: 'value',

            }]
        }]
    }, {
        xtype: 'container',
        layout: 'vbox',
        items: [{
            xtype: 'label',
            html: _this.isModern?Localization.FARMER_CONNECT.QUANTITY + ':':'Quantity'
        }, {
            xtype: 'container',
            layout: 'hbox',
            padding: '0px',
            items: [{
                xtype: 'numberfield',
                width: 98,
                name: 'quantity',
                margin: '0 0 0 0',
                minValue: 0,
                value: _this.autoFill ? _this.previousSelectedValues.quantity : '',
                allowBlank: false,
                flex: 2,
            }, {
                xtype: 'combobox',
                width: 100,
                labelSeparator: ' ',
                name: 'quantityUnit',
                //flex: 1,
                margin: '0 0 0 0',
                forceSelection: true,
                value: _this.autoFill || _this.modify ? _this.previousSelectedValues.quantityUnit : '',
                store: Ext.create('Ext.data.Store', {
                    data: json.quantityUnit.data
                }),
                queryMode: 'local',
                displayField: 'value',
                allowBlank:false,
                valueField: 'value',


            }]
        }]
    },{
        xtype: 'container',
        hidden: _this.isModern?FarmerConnectConfigurationObject.appSettings.offerType=="basic": PlatformAppConstants.FarmerConnectSettings.offerType=="basic"?true:false,
        items: [{
            xtype: 'label',
            html:'Payment Term:'
        }, {
            xtype: 'combobox',
            labelSeparator: ' ',
            name: 'paymentTerms',
            width: 200,
            forceSelection: true,
            value: _this.autoFill || _this.modify ?_this.previousSelectedValues.paymentTerms : '',
            store: Ext.create('Ext.data.Store', {
                data: json.paymentTerms.data
            }),
            queryMode: 'local',
            displayField: 'value',
            allowBlank:false,
            valueField: 'value',
           }]

    }])
    },
    createSecondPage: function(json) {
        let _this = this;
        let expiryDate = '';
        let shipmentDate = '';
        if (_this.previousSelectedValues.expiryDateISOString == null) {
            expiryDate = new Date(_this.previousSelectedValues.expiryDate);
            shipmentDate = new Date(_this.previousSelectedValues.shipmentDate);
        } else {
            expiryDate = _this.previousSelectedValues.expiryDateISOString;
            shipmentDate = _this.previousSelectedValues.shipmentDateISOString;
        }
        let page = {
            xtype: 'container',
            cls: 'form-container-items',
            items: _this.getSecondPageFields(json,expiryDate,shipmentDate)
        }
        return page;

    },
    getSecondPageFieldsWeb:function(json){
        var _this=this;
        let expiryDate = '';
        let deliveryToDateISOString='';
        let deliveryFromDateISOString='';
        if (_this.previousSelectedValues.expiryDateISOString == null) {
            expiryDate = new Date(_this.previousSelectedValues.expiryDate).toLocaleDateString();
            deliveryToDateISOString = new Date(_this.previousSelectedValues.deliveryToDate).toLocaleDateString();
            deliveryFromDateISOString = new Date(_this.previousSelectedValues.deliveryFromDate).toLocaleDateString();

        } else {
            expiryDate = new Date(_this.previousSelectedValues.expiryDateISOString).toLocaleDateString();
            deliveryToDateISOString = new Date(_this.previousSelectedValues.deliveryToDateISOString).toLocaleDateString();
            deliveryFromDateISOString = new Date(_this.previousSelectedValues.deliveryFromDateISOString).toLocaleDateString();

        }


        return([{
            xtype: 'container',
            items: [{
                xtype: 'datefield',
                width: 200,
                labelSeparator: ' ',
                format: 'd/m/Y',
                fieldLabel: 'Expiry Date',
                name: 'expiryDateISOString',
                reference:'expiryDateISOString',
                minValue: new Date(),                
                allowBlank:false,
                value: _this.autoFill ? expiryDate:new Date(),
                labelAlign: 'top',
            }]
        }, {
            xtype: 'container',
            items: [{
                xtype: 'label',
                html: 'Location'
            }, {
                xtype: 'combobox',
                width: 200,
                name: 'location',
                labelSeparator: ' ',
                forceSelection: true,
                allowBlank:false,
                value: _this.autoFill ? _this.previousSelectedValues.location : '',
                store: Ext.create('Ext.data.Store', {
                    data: json.location.data
                }),
                queryMode: 'local',
                displayField: 'value',
                valueField: 'value',
            }]
        }, {
            xtype: 'container',
            items: [{
                xtype: 'label',
                html:  'Inco Term'
            }, {
                xtype: 'combobox',
                width: 200,
                labelSeparator: ' ',
                name: 'incoTerm',
                forceSelection: true,
                allowBlank: false,
                value: _this.autoFill ? _this.previousSelectedValues.incoTerm : '',
                store: Ext.create('Ext.data.Store', {
                    data: json.incoTerm.data
                }),
                queryMode: 'local',
                displayField: 'value',
                valueField: 'value',
            }]
        },{
            
        xtype: 'container',
        hidden: PlatformAppConstants.FarmerConnectSettings.offerType=="basic",
        items: [{
            xtype: 'label',
            html: 'Packing Size:'
        }, {
                xtype: 'combobox',
                labelSeparator: ' ',
                name: 'packingSize',
                width: 200,
                forceSelection: true,
                value: _this.autoFill || _this.modify ?_this.previousSelectedValues.packingSize : '',
                store: Ext.create('Ext.data.Store', {
                    data: json.packingSize.data
                }),
                queryMode: 'local',
                displayField: 'value',
                allowBlank:false,
                valueField: 'value',
            }]
        },{
            
        xtype: 'container',
        hidden: PlatformAppConstants.FarmerConnectSettings.offerType=="basic",
        items: [{
            xtype: 'label',
            html: 'Packing Type:'
        }, {
                xtype: 'combobox',
                labelSeparator: ' ',
                name: 'packingType',
                width: 200,
                forceSelection: true,
                value: _this.autoFill || _this.modify ?_this.previousSelectedValues.packingType: '',
                store: Ext.create('Ext.data.Store', {
                    data: json.packingType.data
                }),
                queryMode: 'local',
                displayField: 'value',
                allowBlank:false,
                valueField: 'value',
            }]
        },{
            
        xtype: 'container',
        layout: 'vbox',
        items: [{
            xtype: 'label',
            html:'Delivery Period'
        }, {
            xtype: 'container',
            layout: 'hbox',
            width:250,
            padding: '0px',
            items: [{
                xtype: 'datefield',
                width: 120,
                labelSeparator: ' ',
                margin:'0 5 0 0 ',
                format: 'd/m/Y',
                name: 'deliveryFromDateISOString',
                reference: 'deliveryFromDateISOString',
                value:_this.autoFill?deliveryFromDateISOString:new Date(),
                minValue: new Date(),
                allowBlank: false,
                labelAlign: 'top',
            },{
                xtype: 'datefield',
                width: 120,
                labelSeparator: ' ',
                format: 'd/m/Y',
                name: 'deliveryToDateISOString',
                reference: 'deliveryToDateISOString',
                value:_this.autoFill?deliveryToDateISOString:new Date(),
                minValue: new Date(),
                allowBlank: false,
                labelAlign: 'top',
            }]
        }]

        }])
    },

    getSecondPageFields:function(json,expiryDate,shipmentDate){
        var _this=this;
        return([{
            xtype: 'container',
            items: [{
                xtype: 'datepickerfield',
                itemId: 'expireyDateItemId',
                label: _this.isModern?Localization.FARMER_CONNECT.EXPIRY_DATE + ':':'EXPIRY DATE ',
                name: 'expiryDateISOString',
                minDate: new Date(),
                allowBlank:false,
                value: _this.autoFill ? expiryDate :'',
                labelAlign: 'top',
                picker: {
                    xtype: 'datepicker',
                    xtype: 'datepicker',
                    yearFrom: new Date().getFullYear(),
                    yearTo: new Date().getFullYear() + 50
                }
            }]
        }, {
            xtype: 'container',
            items: [{
                xtype: 'label',
                html:  _this.isModern?Localization.FARMER_CONNECT.LOCATION + ':':'Location'
            }, {
                xtype: 'combobox',
                name: 'location',
                labelSeparator: ' ',
                forceSelection: true,
                allowBlank:false,
                value: _this.autoFill ? _this.previousSelectedValues.location : '',
                store: Ext.create('Ext.data.Store', {
                    data: json.location.data
                }),
                queryMode: 'local',
                displayField: 'value',
                valueField: 'value',
            }]
        }, {
            xtype: 'container',
            items: [{
                xtype: 'label',
                html:  _this.isModern?Localization.FARMER_CONNECT.INCO_TERM + ':':'Inco Term'
            }, {
                xtype: 'combobox',
                labelSeparator: ' ',
                name: 'incoTerm',
                forceSelection: true,
                allowBlank: false,
                value: _this.autoFill ? _this.previousSelectedValues.incoTerm : '',
                store: Ext.create('Ext.data.Store', {
                    data: json.incoTerm.data
                }),
                queryMode: 'local',
                displayField: 'value',
                valueField: 'value',
            }]
        }, {
            xtype: 'container',
            items: [{
                xtype: 'datepickerfield',
                itemId: 'shipmentDateItemId',
                name: 'shipmentDateISOString',
                label: _this.isModern?Localization.FARMER_CONNECT.SHIPMENT_DATE + ':':'Shipment Date',
                value: _this.autoFill ? shipmentDate :'',
                minDate: new Date(),
                allowBlank: false,
                labelAlign: 'top',
                picker: {
                    xtype: 'datepicker',
                    yearFrom: new Date().getFullYear(),
                    yearTo: new Date().getFullYear() + 50
                }
            }]
        }])
    },
    validateWeb:function(form1){
        let formFields=form1.getForm().getFields().items;
        let validation=true;
       
        Ext.Array.each(formFields,function(field){
            if(field.getValue()==null ){
                if(!field.up().isHidden()){
                    validation=false
                }
            }
        })
        return validation

    },
    getFieldValues: function(element) {
        let _this = this;
        if (element.items && element.items.items) {
            let childElements = element.items.items;
            if (childElements.length >= 1) {
                for (let i = 0; i < childElements.length; i++) {
                    _this.getFieldValues(childElements[i])
                }
            }
            if (childElements.items && childElements.items.items) {
                if (childElements.items.items.length > 0) {
                    for (let i = 0; i < childElements.items.items.length; i++) {
                        _this.getFieldValues(childElements.items.items[i])
                    }
                }
            }
        } else {
            if (element.xtype != 'label') {
                if (element.getValue() != null) {
                    if(element.xtype=="numberfield" && (element.getValue()<element.getMinValue())){
                        EkaToast.show( Localization.FARMER_CONNECT.PROVIDE_VALID_VALUE + element.name);
                        _this.fieldValidation = false
                    }else{
                        _this.jsonObject[element.name] = element.getValue();
                    }
                } else {
                    if (!element.allowBlank) {
                        _this.validate = false
                    }
                }
            }
        }
    },

});
