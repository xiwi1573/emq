/*
 * Ext JS Library 2.1
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

/**
 * @class Ext.menu.DateItemEx
 * @extends Ext.menu.DateItem
 * Simple date picker class.
 * @constructor
 * Create a new DatePickerEx
 * @param {Object} config The config object
 */
 
Ext.menu.DateItemEx = function(config){
	Ext.menu.DateItemEx.superclass.constructor.call(this, new Ext.DatePickerEx(config), config);
    /** The Ext.DatePicker object @type Ext.DatePicker */
    this.picker = this.component;
    this.addEvents('select');
    
    this.picker.on("render", function(picker){
        picker.getEl().swallowEvent("click");
        picker.container.addClass("x-menu-date-item");
    });

    this.picker.on("select", this.onSelect, this);
};

Ext.extend(Ext.menu.DateItemEx, Ext.menu.Adapter, {
    // private
    onSelect : function(picker, date){
        this.fireEvent("select", this, date, picker);
        Ext.menu.DateItemEx.superclass.handleClick.call(this);
    }
});