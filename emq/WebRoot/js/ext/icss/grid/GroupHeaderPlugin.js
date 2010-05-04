Ext.namespace("Ext.ux.plugins");

Ext.ux.plugins.GroupHeaderGrid = function(config) {
	Ext.apply(this, config);
};

Ext.extend(Ext.ux.plugins.GroupHeaderGrid, Ext.util.Observable, {
	init: function(grid) {
		var v = grid.getView();
		v.beforeMethod('initTemplates', this.initTemplates);
		v.renderHeaders = this.renderHeaders.createDelegate(v, [v.renderHeaders]);
        v.afterMethod('onColumnWidthUpdated', this.updateGroupStyles);
        v.afterMethod('onAllColumnWidthsUpdated', this.updateGroupStyles);
		v.afterMethod('onColumnHiddenUpdated', this.updateGroupStyles);
		v.getHeaderCell = this.getHeaderCell;
		v.updateSortIcon = this.updateSortIcon;
		v.getGroupStyle = this.getGroupStyle;
	},

	initTemplates: function() {
		var ts = this.templates || {};
		if (!ts.gheader) {
			ts.gheader = new Ext.Template(
				'<table border="0" cellspacing="0" cellpadding="0" class="ux-grid-group-table" style="{tstyle}">',
				'<thead>{rows}{header}</thead>',
				'</table>'
			);
		}
		if (!ts.header) {
			ts.header = new Ext.Template(
				'<tr class="x-grid3-hd-row">{cells}</tr>'
			);
		}
		if (!ts.gcell) {
			ts.gcell = new Ext.Template(
				'<td class="x-grid3-hd {cls} x-grid3-td-{id}" style="{style}" rowspan="{rowspan}" colspan="{colspan}">',
				'<div {tooltip} class="x-grid3-hd-inner x-grid3-hd-{id}" unselectable="on" style="{istyle}">{value}</div>',
				'</td>'
			);
		}
		this.templates = ts;
	},

	renderHeaders: function(renderHeaders) {
		var ts = this.templates, rows = [], table = [];
		for (var i = 0; i < this.cm.rows.length; i++) {
			var r = this.cm.rows[i], cells = [], col = 0;
			for (var j = 0; j < r.length; j++) {
				var c = r[j];
				c.colspan = c.colspan || 1;
				c.rowspan = c.rowspan || 1;
				while (table[i] && table[i][col]) {
					col++;
				}
				c.col = col;
				for (var rs = i; rs < i + c.rowspan; rs++) {
					if (!table[rs]) {
						table[rs] = [];
					}
					for (var cs = col; cs < col + c.colspan; cs++) {
						table[rs][cs] = true;
					}
				}
				var gs = this.getGroupStyle(c);
				cells[j] = ts.gcell.apply({
					id: c.id || i + '-' + col,
					cls: c.header ? 'ux-grid-hd-group-cell' : 'ux-grid-hd-nogroup-cell',
					style: 'width:' + gs.width + ';' + (gs.hidden ? 'display:none;' : '') + (c.align ? 'text-align:' + c.align + ';' : ''),
					rowspan: c.rowspan,
					colspan: gs.colspan,
					tooltip: c.tooltip ? (Ext.QuickTips.isEnabled() ? 'ext:qtip' : 'title') + '="' + c.tooltip + '"' : '',
					value: c.header || '&#160;',
					istyle: c.align == 'right' ? 'padding-right:16px' : ''
				});
			}
			rows[i] = ts.header.apply({
				cells: cells.join('')
			});
		}
		return ts.gheader.apply({
			tstyle: 'width:' + this.getTotalWidth() + ';',
			rows: rows.join(''),
			header: renderHeaders.call(this)
		});
	},

	getGroupStyle: function(c) {
		var w = 0, h = true, cs = 0;
		for (var i = c.col; i < c.col + c.colspan; i++) {
			if (!this.cm.isHidden(i)) {
				var cw = this.cm.getColumnWidth(i);
				if(typeof cw == 'number'){
					w += cw;
				}
				h = false;
				cs++;
			}
		}
		return {
			width: (Ext.isBorderBox ? w : Math.max(w - this.borderWidth, 0)) + 'px',
			hidden: h,
			colspan: cs || 1
		}
	},

	updateGroupStyles: function(col) {
		var rows = this.mainHd.query('tr.x-grid3-hd-row');
		for (var i = 0; i < rows.length - 1; i++) {
			var cells = rows[i].childNodes;
			for (var j = 0; j < cells.length; j++) {
				var c = this.cm.rows[i][j];
				if ((typeof col != 'number') || (col >= c.col && col < c.col + c.colspan)) {
					var gs = this.getGroupStyle(c);
					cells[j].style.width = gs.width;
					cells[j].style.display = gs.hidden ? 'none' : '';
					cells[j].colSpan = gs.colspan;
				}
			}
		}
	},

	getHeaderCell : function(index){
		return this.mainHd.query('td.x-grid3-cell')[index];
	},

	updateSortIcon : function(col, dir){
		var sc = this.sortClasses;
		var hds = this.mainHd.select('td.x-grid3-cell').removeClass(sc);
		hds.item(col).addClass(sc[dir == "DESC" ? 1 : 0]);
	}
});
