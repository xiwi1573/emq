/**
 * use common combox
 * 
 * @param co(condition)
 *            查询条件数组,fn数据源方法,
 *            isAll:false,是否有全部,默认为没有。hasSelect:false是否包含请选择,默认不包含。 调用示例：var
 *            combo = Ext.form.commonCombox({ id:"combo", fn:Util.getCombo,
 *            valueField:"code",displayField:"text"})
 *            获取值：Ext.getCmp('').getValue(); 获取显示值：Ext.getCmp('').getRawValue();
 * 
 */

Ext.form.commonCombox = function(config) {
	this.initConfig = config || {};
	Ext.form.commonCombox.superclass.constructor.call(this, config);
};

Ext.form.commonCombox = Ext.extend(Ext.form.ComboBox, {
			initComponent : function() {
				if (typeof(this.co) == 'undefined') {
					this.co = null;
				}
				if (typeof(this.fn) == 'undefined') {
					new Ext.Msg.alert("错误!", "请您添加有效的数据源。");
				} else {
					this.store = new Ext.data.DWRStore({
								fn : this.fn,
								fields : [{
											name : this.valueField
										}, {
											name : this.displayField
										}]
							});
				}
				this.typeAhead = true;
				this.selectOnFocus = true;
				this.mode = 'remote';
				this.triggerAction = 'all';
				this.readOnly = true;
				this.on('beforequery', this.onBeforeQuery);
				Ext.form.commonCombox.superclass.initComponent.call(this);
			},
			onBeforeQuery : function(queryEvent) {
				if (this.co!=null) {
					queryEvent.query = [this.co];
				}
			},
			init : function(co) {
				if (co!=null) {
					this.store.load({
								params : [co]
							});
				} 
			}
		});
Ext.reg('commonCombox', Ext.form.commonCombox);

/*
 * 本地数据源下拉示例： var carstore = new Ext.data.SimpleStore({ fields: ['yearflag',
 * 'yearname'], data : [ ['0','全部'], ['1','上半年'], ['2','下半年'] ] }); var
 * halfyearcombo = new Ext.form.ComboBox({ store: carstore, id:'halfyearcombo',
 * editable:false, displayField:'yearname', valueField:'yearflag', typeAhead:
 * true, mode: 'local', triggerAction: 'all', selectOnFocus:true,
 * fieldLabel:'半年标志' });
 */
