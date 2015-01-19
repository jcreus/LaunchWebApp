
$(document).ready(function () {

  var screenHeight = document.getElementById('container').offsetHeight;
  var paneHeight = Math.floor(0.75*screenHeight) + "px";
  var tabs = document.getElementsByClassName("tab-pane");
  
  var i;
  for (i=0;i<tabs.length;i++) {
    tabs[i].style.minHeight = paneHeight;
  }
  
});

