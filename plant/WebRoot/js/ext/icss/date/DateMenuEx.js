/*
 * Ext JS Library 2.1
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

/**
 * @class Ext.menu.DateMenu
 * @extends Ext.menu.Menu
 * A menu containing a {@link Ext.menu.DateItem} component (which provides a date picker).
 * @constructor
 * Creates a new DateMenu
 * @param {Object} config Configuration options
 */
Ext.menu.DateMenuEx = function(config){
	this.YMD = 'Y-m-d';
	this.YM = 'Y-m';
	
    Ext.menu.DateMenuEx.superclass.constructor.call(this, config);
    this.plain = true;
    var di = new Ext.menu.DateItemEx(config);
    this.add(di);
    /**
     * The {@link Ext.DatePicker} instance for this DateMenu
     * @type DatePicker
     */
    this.picker = di.picker;
    /**
     * @event select
     * @param {DatePicker} picker
     * @param {Date} date
     */
    this.relayEvents(di, ["select"]);

    this.format = config.format;
    this.on('beforeshow', function(){
        if(this.picker){
        	if(this.format == this.YMD)
        	{
            	this.picker.hideMonthPicker(true);
        	}
        	else if(this.format == this.YM)
        	{
        		this.picker.hideDayPicker(true);
        		this.picker.showMonthPickerEx(true);
        	}
        }
    }, this);
};
Ext.extend(Ext.menu.DateMenuEx, Ext.menu.Menu, {
    cls:'x-date-menu',

    // private
    beforeDestroy : function() {
        this.picker.destroy();
    }
});