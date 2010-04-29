/**
 * 下拉框二次封装
 * @author 宋爱林
 */
selectsupport = Class.create();

selectsupport.prototype = {
  initialize: function(selectId, initSql, valueProp, textProp){
    this.selectId = selectId;
    this.initSql = initSql;
    this.valueProp = valueProp;
    this.textProp = textProp;
    this.sendRequestForInit();
  },
  sendRequestForInit: function() {
    if (this.handlingInitRequest) {
      this.pendingInitRequest = true;
      return;
    }
    
    this.handlingInitRequest = true;
    this.callDWRInitEngine();
  },
  callDWRInitEngine: function(){
    // 保存当前对象指针    
    var tempThis = this;
    DOMSelectManager.getInitComboObjects(this.initSql, function(dwrResponse) {
   		tempThis.initSelect(dwrResponse);
      tempThis.handlingInitRequest = false;
      if (tempThis.pendingInitRequest) {
        tempThis.pendingInitRequest = false;
        tempThis.sendRequestForInit();
      }
    });
  },
  initSelect: function(data) {
    DWRUtil.removeAllOptions(this.selectId); // 移除下拉框当前对象
    var defaultSelect = [{ name:'--全部--', id:'-1' }];
		DWRUtil.addOptions(this.selectId, defaultSelect, 'id', 'name');
    DWRUtil.addOptions(this.selectId, data, this.valueProp, this.textProp);
  }
}