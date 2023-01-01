let urlpath= window.location.pathname.split('/');
const postId = urlpath[3];
const board = urlpath[1];
console.log("url 확인 : " + postId +" and :" + board);
const deleteURL = ("/"+board+"/"+postId+"/delete");

function deleteForm() {
    $.ajax({
        type: "POST",
        url : deleteURL,
        dataType: "json",
        headers: {"Authorization": accessToken},

    }).done(function (result) {
        console.log(result)
        console.log("게시글 삭제 성공");
        window.location.replace('http://localhost:9011/homes/posts');
    }).fail(function (jqXHR) {
        console.log(jqXHR);
        $("#errorMsg").show();
    }).always(function () {
        console.log("실행되는지 확인")

    })
}

function commentWrite(parentCommentId) {
    console.log(parentCommentId)
    $.ajax({
        type: "POST",
        url : "/comments/new",
        contentType : "application/json",

        headers: {"Authorization": accessToken},
     data: JSON.stringify({
       "adminCommentContent" : $("#comment").val(),
         "adminPostId": postId,
         "parentCommentId": parentCommentId
}) ,
    }).done(function (result) {
        console.log(result)
        console.log("댓글 성공")
    }).fail(function (jqXHR) {
        console.log(jqXHR);
        $("#errorMsg").show();
    }).always(function () {
        console.log("실행되는지 확인")

    })
}

function childCommentWrite(parentCommentId) {
    console.log(parentCommentId)
    $.ajax({
        type: "POST",
        url : "/comments/new",
        contentType : "application/json",

        headers: {"Authorization": accessToken},
        data: JSON.stringify({
            "adminCommentContent" : $("#childComment").val(),
            "adminPostId": postId,
            "parentCommentId": parentCommentId
        }) ,
    }).done(function (result) {
        console.log(result)
        console.log("댓글 성공")
    }).fail(function (jqXHR) {
        console.log(jqXHR);
        $("#errorMsg").show();
    }).always(function () {
        console.log("실행되는지 확인")

    })
}



function commentDelete(inta){
    $.ajax({
        type:"POST",
        url: "/comments/"+inta+"/delete",

        headers:  {"Authorization": accessToken},
        data: ({
          "homeId": postId
        })
    }).done(function (result) {
        console.log(result)
        console.log("댓글 삭제 성공");

    }).fail(function (jqXHR) {
        console.log(jqXHR);
        $("#errorMsg").show();
    }).always(function () {
        console.log("실행되는지 확인")

    })
}