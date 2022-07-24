Ext.define('EkaSmartPlt.farmerconnect.view.offer.FarmerConnectUserOptionsWindow', {
    extend: 'Ext.window.Window',
    modal: true,
    cls:'mobile-window',
    closeAction: false,
    closable: false,
    layout:'vbox',
    header: false,
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
                    style:'margin-bottom:10px;',
                    cls : 'button-transparent-background button-color-blue button-user-options',
                    action : buttonsConfig[i].action,
                    handler : function(){
                        _this.parentView.fireEvent(this.action , _this);
                    }
                });
            }       
            this.add(items);
        }        
    },
});