$(window).ready(function(){
    $("#uploadFileButton").on('click', function(){
        var form = $('#newFileForm')[0];
        var data = new FormData(form);

        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: 'Materials',
            processData: false,  // Important!
            contentType: false,
            cache: false,
            data: data,
            success: function(responseText){
                if(responseText !== "success"){
                    $("#fileMessage").removeClass("text-success");
                    $("#fileMessage").addClass("text-danger");
                    $("#fileMessage").text(responseText);
                }else{
                    $("#fileMessage").removeClass("text-danger");
                    $("#fileMessage").addClass("text-success");
                    $("#fileMessage").text("Uspješno ste dodali materijal");
                    $("#material-list-div").load("Materials?action=all #material-list-div");
                }
            }
        })
    })
})