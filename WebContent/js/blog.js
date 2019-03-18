$(window).ready(function(){
    $(".comment-button").on('click', function(e){
        var button = e.currentTarget;
        var id = button.id;
        $.ajax({
            method:'post',
            url:'Blogs',
            data:{
                action:'newComment',
                blogId: id,
                content: $("#newCommentContent").val(),
            },
            success: function(responseText){
                if(responseText === "success"){
                    $("#newCommentError").text("");
                    $("#collapseNewCommentButton").trigger('click');
                    var url="Blogs?action=view&blogId=" + id;
                    $("#comments-div").load(url + " #comments-div-body");
                    $("#newCommentContent").val("");
                }else{
                    $("#newCommentError").text(responseText);
                }
            }
        })
    })
})