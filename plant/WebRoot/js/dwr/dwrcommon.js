/**
 * dwr自定义加载图片
 */
function useLoadingImage(imageSrc) {
  var loadingImage;
  if (imageSrc) loadingImage = imageSrc;
  // 这个图片没放进来
  else loadingImage = "ajax-loader.gif";
  DWREngine.setPreHook(function() {
    var disabledImageZone = $('disabledImageZone');
    if (!disabledImageZone) {
      disabledImageZone = document.createElement('div');
      disabledImageZone.setAttribute('id', 'disabledImageZone');
      disabledImageZone.style.position = "absolute";
      disabledImageZone.style.zIndex = "1000";
      disabledImageZone.style.left = "0px";
      disabledImageZone.style.top = "0px";
      disabledImageZone.style.width = "100%";
      disabledImageZone.style.height = "100%";
      var imageZone = document.createElement('img');
      imageZone.setAttribute('id','imageZone');
      imageZone.setAttribute('src',imageSrc);
      imageZone.style.position = "absolute";
      imageZone.style.top = "0px";
      imageZone.style.right = "0px";
      disabledImageZone.appendChild(imageZone);
      document.body.appendChild(disabledImageZone);
    }
    else {
      $('imageZone').src = imageSrc;
      disabledImageZone.style.visibility = 'visible';
    }
  });
  DWREngine.setPostHook(function() {
    $('disabledImageZone').style.visibility = 'hidden';
  });
}	

/**
 * dwr自定义加载信息
 */
function useLoadingMessage(message) {
  var loadingMessage;
  if (message) loadingMessage = message;
  else loadingMessage = "正在加载数据，请稍候...";

  DWREngine.setPreHook(function() {
    var disabledZone = $('disabledZone');
    if (!disabledZone) {
      disabledZone = document.createElement('div');
      disabledZone.setAttribute('id', 'disabledZone');
      disabledZone.style.position = "absolute";
      disabledZone.style.zIndex = "1000";
      disabledZone.style.left = "0px";
      disabledZone.style.top = "0px";
      disabledZone.style.width = "100%";
      disabledZone.style.height = "100%";
      document.body.appendChild(disabledZone);
      var messageZone = document.createElement('div');
      messageZone.setAttribute('id', 'messageZone');
      messageZone.style.position = "absolute";
      messageZone.style.top = "0px";　　//定义显示加载信息层的位置
      messageZone.style.right = "0px";　 //定义显示加载信息层的位置
      messageZone.style.width = "150";　　//定义显示加载信息层的宽度
      messageZone.style.height = "20";    //定义显示加载信息层的高度
      messageZone.style.background = "red";//定义显示加载信息层的颜色
      messageZone.style.color = "white";
      messageZone.style.fontFamily = "宋体";
      messageZone.style.fontSize="9pt";
      messageZone.style.padding = "4px";
      disabledZone.appendChild(messageZone);
      var text = document.createTextNode(loadingMessage);
      messageZone.appendChild(text);
    }
    else {
      $('messageZone').innerHTML = loadingMessage;
      disabledZone.style.visibility = 'visible';
    }
  });

  DWREngine.setPostHook(function() {
    $('disabledZone').style.visibility = 'hidden';
  });
}