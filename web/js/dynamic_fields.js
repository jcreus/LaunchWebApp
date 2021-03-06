$(document).ready(function() {
    var max_fields  = 5;
    var wrapper     = $(".table_bottom");
    var add_button  = $(".add_field_button");
    
    var stageHint = "Which stage to apply the correction.";
    var typeHint = "What flight parameter do you want to change?";
    var timeHint = "Time (in seconds) after liftoff to begin invoking this correction";
    var paramHint = "For pitch/yaw, this parameter is in radians. Pitch 0.0 flies parallel to ground, positive points up, etc. For yaw, a positive value turns you left, negative turns you right. (It's safe enough to assume the 2nd stage is always flying prograde). For thrust, enter a % value.";

    $(add_button).click(function(e){
        e.preventDefault();
        var x = parseInt($('li.active').attr('x'));
        x = isNaN(x) ? $('div.active .remove_field').length : x;
        if(x < max_fields){
            $('li.active').attr('x',x+1);
            var row = '<tr>';
            row += '<td><select title="'+stageHint+'" name="correction'+x+'" class="form-control">';
            row += '<option value="" selected disabled>Stage</option>';
            row += '<option value="0">1</option>';
            row += '<option value="1">2</option></select></td>';
            row += '<td><select title="'+typeHint+'" name="correction'+x+'" class="form-control">';
            row += '<option value="" selected disabled>Correction Type</option>';
            row += '<option value="PITCH">Pitch</option>';
            row += '<option value="YAW">Yaw</option>';
            row += '<option value="THROTTLE">Throttle</option></select></td>';
            row += '<td> @ T<input title="'+timeHint+'" type="text" size="10" placeholder="Time" name="correction'+x+'"></td>';
            row += '<td><input title="'+paramHint+'" type="text" size="10" placeholder="Parameter" name="correction'+x+'"></td>';
            row += '<td class="remove_field"><a href="#"><i class="glyphicon glyphicon-remove"/></a></td></tr>\n';
            
            var table = $(this).closest('.table-right').children('.table_bottom');
            $(table).append(row);
        }
    });
    
    $(wrapper).on("click",".remove_field", function(e){
        e.preventDefault();
        $(this).closest('tr').remove();
        var x = parseInt($('li.active').attr('x'));
        x = isNaN(x) ? $('div.active .remove_field').length : x;
        $('li.active').attr('x',parseInt($('li.active').attr('x'))-1);
    });
});
