/*
 * Ext JS Library 2.1
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

/**
 * @class Ext.DatePickerEx
 * @extends Ext.DatePicker
 * Simple date picker class.
 * @constructor
 * Create a new DatePickerEx
 * @param {Object} config The config object
 */
 
Ext.DatePickerEx = function(config){
    Ext.DatePickerEx.superclass.constructor.call(this, config);
};

Ext.DatePickerEx = Ext.extend(Ext.DatePicker, {

	initComponent : function(){
        Ext.DatePickerEx.superclass.initComponent.call(this);
    },

	hideDayPicker : function(disableAnim){
        if(this.dayPicker){
            if(disableAnim === true){
                this.dayPicker.hide();
            }else{
                this.dayPicker.slideOut('t', {duration:.2});
            }
        }
    },
    //private
    onRender : function(container, position){
        var m = [
             '<table cellspacing="0">',
                '<tr><td class="x-date-left"><a href="#" title="', this.prevText ,'">&#160;</a></td><td class="x-date-middle" align="center"></td><td class="x-date-right"><a href="#" title="', this.nextText ,'">&#160;</a></td></tr>',
                '<tr><td colspan="3"><table class="x-date-inner" cellspacing="0"><thead><tr>'];
        var dn = this.dayNames;
        for(var i = 0; i < 7; i++){
            var d = this.startDay+i;
            if(d > 6){
                d = d-7;
            }
            m.push("<th><span>", dn[d].substr(0,1), "</span></th>");
        }
        m[m.length] = "</tr></thead><tbody><tr>";
        for(var i = 0; i < 42; i++) {
            if(i % 7 == 0 && i != 0){
                m[m.length] = "</tr><tr>";
            }
            m[m.length] = '<td><a href="#" hidefocus="on" class="x-date-date" tabIndex="1"><em><span></span></em></a></td>';
        }
        m[m.length] = '</tr></tbody></table></td></tr><tr><td colspan="3" class="x-date-bottom" align="center"></td></tr></table><div class="x-date-mp"></div>';


        var el = document.createElement("div");
        el.className = "x-date-picker";
        el.innerHTML = m.join("");

        container.dom.insertBefore(el, position);

        this.el = Ext.get(el);
        this.eventEl = Ext.get(el.firstChild);

        new Ext.util.ClickRepeater(this.el.child("td.x-date-left a"), {
            handler: this.showPrevMonth,
            scope: this,
            preventDefault:true,
            stopDefault:true
        });

        new Ext.util.ClickRepeater(this.el.child("td.x-date-right a"), {
            handler: this.showNextMonth,
            scope: this,
            preventDefault:true,
            stopDefault:true
        });

        this.eventEl.on("mousewheel", this.handleMouseWheel,  this);

        this.monthPicker = this.el.down('div.x-date-mp');
        this.monthPicker.enableDisplayMode('block');
        
        this.dayPicker = this.el.firstChild;
		
        
        var kn = new Ext.KeyNav(this.eventEl, {
            "left" : function(e){
                e.ctrlKey ?
                    this.showPrevMonth() :
                    this.update(this.activeDate.add("d", -1));
            },

            "right" : function(e){
                e.ctrlKey ?
                    this.showNextMonth() :
                    this.update(this.activeDate.add("d", 1));
            },

            "up" : function(e){
                e.ctrlKey ?
                    this.showNextYear() :
                    this.update(this.activeDate.add("d", -7));
            },

            "down" : function(e){
                e.ctrlKey ?
                    this.showPrevYear() :
                    this.update(this.activeDate.add("d", 7));
            },

            "pageUp" : function(e){
                this.showNextMonth();
            },

            "pageDown" : function(e){
                this.showPrevMonth();
            },

            "enter" : function(e){
                e.stopPropagation();
                return true;
            },

            scope : this
        });

        this.eventEl.on("click", this.handleDateClick,  this, {delegate: "a.x-date-date"});

        this.eventEl.addKeyListener(Ext.EventObject.SPACE, this.selectToday,  this);

        this.el.unselectable();
        
        this.cells = this.el.select("table.x-date-inner tbody td");
        this.textNodes = this.el.query("table.x-date-inner tbody span");

        this.mbtn = new Ext.Button({
            text: "&#160;",
            tooltip: this.monthYearText,
            renderTo: this.el.child("td.x-date-middle", true)
        });

        this.mbtn.on('click', this.showMonthPicker, this);
        this.mbtn.el.child(this.mbtn.menuClassTarget).addClass("x-btn-with-menu");


        var today = (new Date()).dateFormat(this.format);
        this.todayBtn = new Ext.Button({
            renderTo: this.el.child("td.x-date-bottom", true),
            text: String.format(this.todayText, today),
            tooltip: String.format(this.todayTip, today),
            handler: this.selectToday,
            scope: this
        });
        
        if(Ext.isIE){
            this.el.repaint();
        }
        this.update(this.value);
    },
    createMonthPicker : function(){
        if(!this.monthPicker.dom.firstChild){
            var buf = ['<table border="0" cellspacing="0">'];
            for(var i = 0; i < 6; i++){
                buf.push(
                    '<tr><td class="x-date-mp-month"><a href="#">', this.monthNames[i].substr(0, 3), '</a></td>',
                    '<td class="x-date-mp-month x-date-mp-sep"><a href="#">', this.monthNames[i+6].substr(0, 3), '</a></td>',
                    i == 0 ?
                    '<td class="x-date-mp-ybtn" align="center"><a class="x-date-mp-prev"></a></td><td class="x-date-mp-ybtn" align="center"><a class="x-date-mp-next"></a></td></tr>' :
                    '<td class="x-date-mp-year"><a href="#"></a></td><td class="x-date-mp-year"><a href="#"></a></td></tr>'
                );
            }
            buf.push(
                '<tr class="x-date-mp-btns"><td colspan="4"><button type="button" class="x-date-mp-ok">',
                    this.okText,
                    '</button><button type="button" class="x-date-mp-cancel">',
                    this.cancelText,
                    '</button></td></tr>',
                '</table>'
            );
            this.monthPicker.update(buf.join(''));
            this.monthPicker.on('click', this.onMonthClick, this);
            this.monthPicker.on('dblclick', this.onMonthDblClick, this);

            this.mpMonths = this.monthPicker.select('td.x-date-mp-month');
            this.mpYears = this.monthPicker.select('td.x-date-mp-year');
			this.monthBtns = this.monthPicker.select('tr.x-date-mp-btns');
            this.mpMonths.each(function(m, a, i){
                i += 1;
                if((i%2) == 0){
                    m.dom.xmonth = 5 + Math.round(i * .5);
                }else{
                    m.dom.xmonth = Math.round((i-1) * .5);
                }
            });
        }
    },
    showMonthPickerEx : function(disableAnim){
        this.createMonthPicker();
        var size = this.el.getSize();
        this.monthPicker.setSize(size);
        this.monthPicker.child('table').setSize(size);

        this.mpSelMonth = (this.activeDate || this.value).getMonth();
        this.updateMPMonth(this.mpSelMonth);
        this.mpSelYear = (this.activeDate || this.value).getFullYear();
        this.updateMPYear(this.mpSelYear);
		if(true == disableAnim)
		{
			this.monthPicker.show(false);
			this.monthBtns.hide();
		}else{
        	this.monthPicker.slideIn('t', {duration:.2});
		}
    },
    onMonthClick : function(e, t){
        e.stopEvent();
        var el = new Ext.Element(t), pn;
        if(el.is('button.x-date-mp-cancel')){
        	//this.el.parentNode.hide();
            this.hideMonthPicker(true);
        	//this.hide();
        }
        else if(el.is('button.x-date-mp-ok')){
            var d = new Date(this.mpSelYear, this.mpSelMonth, (this.activeDate || this.value).getDate());
            if(d.getMonth() != this.mpSelMonth){
                // "fix" the JS rolling date conversion if needed
                d = new Date(this.mpSelYear, this.mpSelMonth, 1).getLastDateOfMonth();
            }
            if(this.format == 'Y-m')
        	{
        		d.setDate(1);
		        this.handleValue(e,t,d);
        	}
        	else
        	{
	            this.update(d);
	            this.hideMonthPicker();
        	}
        }
        else if(pn = el.up('td.x-date-mp-month', 2)){
            this.mpMonths.removeClass('x-date-mp-sel');
            pn.addClass('x-date-mp-sel');
            this.mpSelMonth = pn.dom.xmonth;
            
        	//·¢ËÍdbclick
            if(this.format == 'Y-m')
        	{
        		var tmpd = new Date(this.mpSelYear, this.mpSelMonth, 1);
        		this.handleValue(e,t,tmpd);
        	}
        	else
        	{
	            var d = new Date(this.mpSelYear, this.mpSelMonth, (this.activeDate || this.value).getDate());
	            if(d.getMonth() != this.mpSelMonth){
	                // "fix" the JS rolling date conversion if needed
	                d = new Date(this.mpSelYear, this.mpSelMonth, 1).getLastDateOfMonth();
	            }
	            this.update(d);
	            this.hideMonthPicker();
        	}
        }
        else if(pn = el.up('td.x-date-mp-year', 2)){
            this.mpYears.removeClass('x-date-mp-sel');
            pn.addClass('x-date-mp-sel');
            this.mpSelYear = pn.dom.xyear;
        }
        else if(el.is('a.x-date-mp-prev')){
            this.updateMPYear(this.mpyear-10);
        }
        else if(el.is('a.x-date-mp-next')){
            this.updateMPYear(this.mpyear+10);
        }
        
    },
    onMonthDblClick : function(e, t){
        e.stopEvent();
        var selDate;
        var el = new Ext.Element(t), pn;
        if(pn = el.up('td.x-date-mp-month', 2)){
        	if(this.format == 'Y-m')
        	{
        		selDate = new Date(this.mpSelYear, pn.dom.xmonth, 1);
		        this.handleValue(e,t,selDate);
        	}
        	else
        	{
        		selDate = new Date(this.mpSelYear, pn.dom.xmonth, (this.activeDate || this.value).getDate());
        		this.update(selDate);
		        this.hideMonthPicker();      
        	}
            //this.update(selDate);
            //this.hideMonthPicker();
        }
        else if (pn = el.up('td.x-date-mp-year', 2)) {
        	if(this.format == 'Y-m')
        	{
        		selDate = new Date(pn.dom.xyear, this.mpSelMonth, 1);
		        this.handleValue(e,t,selDate);
        	}
        	else
        	{
				selDate = new Date(pn.dom.xyear, this.mpSelMonth,(this.activeDate || this.value).getDate());
				this.update(selDate);
				this.hideMonthPicker();
        	}

			// this.update(selDate);
			// this.hideMonthPicker();
		}
       
    },
     handleValue:function(e, t,date){  
     	this.update(date);
    	this.setValue(date);
        this.fireEvent("select", this.eventEl, this.value);
    }
});
Ext.reg('datepickerex', Ext.DatePickerEx);