Ext.define('EkaSmartPlt.farmerconnect.view.offer.FarmerConnectConfirmationWindow', {
    extend: 'Ext.window.Window',
    modal: true,
    closeAction: false,
    closable: false,
    layout:'vbox',
    scrollable:true,
    header: false,
    cls:'mobile-window',
	initialize : function(){
        let _this = this;
        let buttonsConfig = this.buttonsConfig;
        this.add(this.components);
        if(buttonsConfig && buttonsConfig.length >0){
            let items = [];
            for(var i=0;i<buttonsConfig.length;i++){ 
                items.push({ 
                    xtype: 'button',
                    text : buttonsConfig[i].text,
                    disabled: !Ext.isEmpty(buttonsConfig[i].disabled)?buttonsConfig[i].disabled:false,
                    action : buttonsConfig[i].action,
                    handler : function(){
                        _this.parentView.fireEvent(this.action , _this);
                    },
                    width:'50%'
                });
            }
            var bottomButtons={           
                xtype : 'toolbar',
                height : 54,
                docked : 'bottom',
                items: items
            }
            this.add(bottomButtons);
        }        
    },
});