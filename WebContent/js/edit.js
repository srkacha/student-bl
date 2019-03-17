$(document).ready(function(){
    $("#savePasswordChange").click(function(){
        $.ajax({
            type: "POST",
            url: "PasswordEdit",
            data: $("#editPasswordForm").serialize(),
            success: function(resultText){
                console.log(resultText);
                if(resultText !== "success"){
                    $("#passwordMessage").removeClass("text-success");
                    $("#passwordMessage").addClass("text-danger");
                    $("#passwordMessage").text(resultText);
                }else{
                    $("#passwordMessage").removeClass("text-danger");
                    $("#passwordMessage").addClass("text-success");
                    $("#passwordMessage").text("Uspješno ste izmijenili lozinku");
                }
            }
        })
    })
    
});