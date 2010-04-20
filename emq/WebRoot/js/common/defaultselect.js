var formatBlank = '     ';
function createYearOptions(domId,beginYear,offset) {
  var yearOptions = new Array();
  yearOptions[yearOptions.length] = { name:'--«Î—°‘Ò--', id:'-1' };
  yearOptions[yearOptions.length] = { name:formatBlank + beginYear, id:beginYear };
  for (var i = 1; i <= offset; i ++) {
    yearOptions[yearOptions.length] = { name:formatBlank + (beginYear + i) , id:(beginYear + i) };
  }
  DWRUtil.addOptions(domId, yearOptions, 'id', 'name');
  DWRUtil.setValue(domId, '2008');
}

function createMonthOptions(domId) {
  var monthOptions = new Array();
  monthOptions[monthOptions.length] = { name:'--«Î—°‘Ò--', id:'-1' };
  for (var i = 1; i <= 12; i ++) {
    monthOptions[monthOptions.length] = { name:formatBlank + i , id:i };
  }
  DWRUtil.addOptions(domId, monthOptions, 'id', 'name');
  DWRUtil.setValue(domId, '2');
}