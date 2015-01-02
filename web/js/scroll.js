
$("#left-button").click(function () {
  $('#tabs').animate({marginLeft: "+=100px"}, "fast");
});

$('#right-button').click(function () {
  $('#tabs').animate({marginLeft: "-=100px"}, "fast");
});

$(function() {
   $("a").click(function() {
      // remove classes from all
      $("a").removeClass("active");
      // add class to the one we clicked
      $(this).addClass("active");
   });
});