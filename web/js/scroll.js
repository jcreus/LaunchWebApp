
$("a.left").click(function () {
  $(".images").each(function () {
    $(this).animate({"margin-left": "+=500px"}, 500);
  });
});

$("a.right").click(function () {
  $(".images").each(function () {
    $(this).animate({"margin-left": "-=500px"}, 500);
  });
});

