$(document).ready(function () {

	$(window).resize(function(){
		var windowWidth = $(window).width();
		if(windowWidth>768){
            $("#small-screen-online-users").hide();
            $("#onlineUsersDiv").show();
		}else{
            $("#small-screen-online-users").show();
            $("#onlineUsersDiv").hide();
		}
    })

    $("#newPostButton").on('click', function(){
        $.ajax({
            method: 'POST',
            url: 'Post',
            data: {
                action: 'new',
                content: $("#newPostContent").val(),
                link: $("#newPostLink").val()
            },
            success: function(responseText){
                if(responseText === "success"){
                    $("#newPostError").text("");
                    $('#newPostCollapse').trigger('click');
                    $("#newPostContent").val("");
                    $("#newPostLink").val("");

                    //refreshujemp postove
                    refreshHomePosts();
                }else if(responseText === "error"){
                    $("#newPostError").text("Došlo je do greške pri kreiranju objave.");
                }else{
                    $("#newPostError").text(responseText);
                }
            }
        })
    })

    refreshEventHandlers();
})

function likeButton(e){
    var target = e.currentTarget;
    var buttonId = target.id;
    var id = buttonId.split('-')[1];
    if(target.childNodes[0].classList.contains("fas")){ //znaci da je post vec lajkovan pa se mora ukloniti like
        $.ajax({
            method:'post',
            url:'Feedback',
            data:{
                action:'remove',
                postId:id
            },
            success: function(responseText){
                if(responseText === 'success'){
                    $(target.childNodes[0]).removeClass("fas");
                    $(target.childNodes[0]).addClass("far");
                    var currentLikes = +$("#likes-" + id).text();
                    $("#likes-" + id).text(currentLikes - 1);
                }
            }
        })
    }else{
        //moramo provjeriti da li je post dislajkovan, ako jeste mora se i to ukloniti
        var dislikeButton = document.getElementById("dislike-" + id);
        var dislikeButtonIcon = dislikeButton.childNodes[0];
        if(dislikeButtonIcon.classList.contains("fas")){
            $.ajax({
                method:'post',
                url:'Feedback',
                data:{
                    action:'remove',
                    postId:id
                },
                success: function(response){
                    if(response === 'success'){
                        $(dislikeButtonIcon).removeClass("fas");
                        $(dislikeButtonIcon).addClass("far");
                        var currentDislikes = +$("#dislikes-" + id).text();
                        $("#dislikes-" + id).text(currentDislikes - 1);
                        $("#like-" + id).trigger('click');
                    }
                }
            })
        }else{
            $.ajax({
                method:'post',
                url:'Feedback',
                data:{
                    action:'like',
                    postId:id
                },
                success: function(response){
                    if(response === 'success'){
                        $(target.childNodes[0]).removeClass("far");
                        $(target.childNodes[0]).addClass("fas");
                        var currentLikes = +$("#likes-" + id).text();
                        $("#likes-" + id).text(currentLikes + 1);
                    }
                }
            })
        }
    }
}

function dislikeButton(e){
    var target = e.currentTarget;
    var buttonId = target.id;
    var id = buttonId.split('-')[1];
    if(target.childNodes[0].classList.contains("fas")){ //znaci da je post vec dislajkovan pa se mora ukloniti dislike
        $.ajax({
            method:'post',
            url:'Feedback',
            data:{
                action:'remove',
                postId:id
            },
            success: function(responseText){
                if(responseText === 'success'){
                    $(target.childNodes[0]).removeClass("fas");
                    $(target.childNodes[0]).addClass("far");
                    var currentLikes = +$("#dislikes-" + id).text();
                    $("#dislikes-" + id).text(currentLikes - 1);
                }
            }
        })
    }else{
        //moramo provjeriti da li je post lajkovan, ako jeste mora se i to ukloniti
        var likeButton = document.getElementById("like-" + id);
        var likeButtonIcon = likeButton.childNodes[0];
        if(likeButtonIcon.classList.contains("fas")){
            $.ajax({
                method:'post',
                url:'Feedback',
                data:{
                    action:'remove',
                    postId:id
                },
                success: function(response){
                    if(response === 'success'){
                        $(likeButtonIcon).removeClass("fas");
                        $(likeButtonIcon).addClass("far");
                        var currentDislikes = +$("#likes-" + id).text();
                        $("#likes-" + id).text(currentDislikes - 1);
                        $("#dislike-" + id).trigger('click');
                    }
                }
            })
        }else{
            $.ajax({
                method:'post',
                url:'Feedback',
                data:{
                    action:'dislike',
                    postId:id
                },
                success: function(response){
                    if(response === 'success'){
                        $(target.childNodes[0]).removeClass("far");
                        $(target.childNodes[0]).addClass("fas");
                        var currentLikes = +$("#dislikes-" + id).text();
                        $("#dislikes-" + id).text(currentLikes + 1);
                    }
                }
            })
        }
    }
}

function bodyOnLoad(){
    var windowWidth = $(window).width();
    if(windowWidth>768){
        $("#small-screen-online-users").hide();
        $("#onlineUsersDiv").show();
    }else{
        $("#small-screen-online-users").show();
        $("#onlineUsersDiv").hide();
    }
}

function attachButtonEvents(){
    
}

//azuriranje sadrzaja glavne starnice svakih 30 sekundi
setInterval(() => {
    $("#small-screen-online-users").load("Home #small-screen-online-row");
    $("#onlineUsersDiv").load("Home #onlineUsersContent");    
}, 10000);

setInterval(() => {
    refreshHomePosts();
}, 30000);

function refreshHomePosts(){
    $("#content-container").load("Home #content-container", function(){
        //kad se ucitaju svi
        refreshEventHandlers();
    });
}

function refreshEventHandlers(){
    $(".like-button").off();
    $(".like-button").on('click', function(e){
        likeButton(e);
    });
    $(".dislike-button").off();
    $(".dislike-button").on('click', function(e){
        dislikeButton(e);
    });
}