

$(document).ready(function() {
    var max_fields  = 10;
    var wrapper     = $(".table_bottom");
    var add_button  = $(".add_field_button");
    
    var hint = 'For Pitch/Yaw, this parameter means rads rel. to the horizon '
              + '(e.g Pitch 0.0 flies parallel to ground, Yaw 0.0 flies parallel '
              + 'to equator). For thrust, enter a % value.';
    
    var x = 5;
    $(add_button).click(function(e){
        e.preventDefault();
        if(x < max_fields){
            x++;
            var row = '<tr>';
            row += '<td><select name="correction1" class="form-control">';
            row += '<option value="" selected disabled>Stage</option>';
            row += '<option value="0">1</option>';
            row += '<option value="1">2</option></select></td>';
            row += '<td><select name="correction'+x+'" class="form-control">';
            row += '<option value="" selected disabled>Correction Type</option>';
            row += '<option value="PITCH">Pitch</option>';
            row += '<option value="YAW">Yaw</option>';
            row += '<option value="THROTTLE">Throttle</option></select></td>';
            row += '<td> @ T<input type="text" size="10" placeholder="Time" name="correction"></td>';
            row += '<td><input title="'+hint+'" type="text" size="10" placeholder="Parameter" name="correction"></td>';
            row += '<td class="remove_field"><a href="#"><i class="glyphicon glyphicon-remove"/></a></td></tr>\n';
            $(wrapper).append(row);
        }
    });
    
    $(wrapper).on("click",".remove_field", function(e){
        e.preventDefault(); $(this).parent('tr').remove(); x--;
    });
});
