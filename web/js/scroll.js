
$(document).ready (function() {

  var style = window.getComputedStyle(document.getElementById("tabs"));
  var lw = document.getElementById("tabs").scrollWidth;
    
  var limit = lw - $(window).width() + parseInt(style.marginLeft);
  $('#tabs').animate({marginLeft: "-=" + limit + "px"}, "1");

});

$(function () {
  $("#left-button").click(function () {
    
    var style = window.getComputedStyle(document.getElementById("tabs"));
    
    if (parseInt(style.marginLeft) <= -600)
      $('#tabs').animate({marginLeft: "+=600px"}, "1000");
    else if (parseInt(style.marginLeft) < 0)
      $('#tabs').animate({marginLeft: "+=" + -1*parseInt(style.marginLeft) + "px"}, "1000");
  });
});

$(function () {
  $('#right-button').click(function () {
    
    var style = window.getComputedStyle(document.getElementById("tabs"));
    var lw = document.getElementById("tabs").scrollWidth;
    
    var limit = lw - $(window).width() + parseInt(style.marginLeft);
    
    if (limit > 600)
      $('#tabs').animate({marginLeft: "-=600px"}, "1000");
    else if(limit > 0)
      $('#tabs').animate({marginLeft: "-=" + limit + "px"}, "1000");
  });
});
