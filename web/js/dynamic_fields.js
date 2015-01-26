

$(document).ready(function() {
    var max_fields      = 5;
    var wrapper         = $(".table_right");
    var add_button      = $(".add_field_button");
    
    var hint = 'For Pitch/Yaw, this parameter means rads rel. to the horizon (e.g Pitch 0.0 flies parallel to ground, Yaw 0.0 flies parallel to equator). For thrust, enter a % value.';
    
    var x = 0;
    $(add_button).click(function(e){
        e.preventDefault();
        if(x < max_fields){
            x++;
            var row = '<tr><td>';
            row += '<select class="form-control">';
            row += '<option value="" disabled selected>Course Correction</option>';
            row += '<option value="pitch">Pitch</option>';
            row += '<option value="yaw">Yaw</option>';
            row += '<option value="throttle">Throttle</option>';
            row += '</select></td>';
            row += '<td></td><td><input title="' + hint + '" type="text" size="10" placeholder="Parameter" name=""></td>';
            row += '<td> @ T</td><td><input type="text" size="10" placeholder="Time" name=""></td>';
            row += '<td class="remove_field"><a href="#"><i class="glyphicon small glyphicon-remove"/></a></td></tr>\n';
            $(wrapper).append(row);
        }
    });
    
    $(wrapper).on("click",".remove_field", function(e){
        e.preventDefault(); $(this).parent('tr').remove(); x--;
    });
});
