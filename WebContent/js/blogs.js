$(window).ready(function(){
    $("#createBlogButton").on('click', function(e){
        $.ajax({
            type: "POST",
            url: "Blogs",
            data: $("#newBlogForm").serialize(),
            success: function(resultText){
                if(resultText !== "success"){
                    $("#blogMessage").removeClass("text-success");
                    $("#blogMessage").addClass("text-danger");
                    $("#blogMessage").text(resultText);
                }else{
                    $("#blogMessage").removeClass("text-danger");
                    $("#blogMessage").addClass("text-success");
                    $("#blogMessage").text("Uspješno ste kreirali blog");
                    //ucitati blog u pozadini
                    $("#blog-list-div").load("Blogs?action=all #blog-list-div", function(){
                        console.log('ucitani komentari');
                    });
                }
            }
        })
    })
})