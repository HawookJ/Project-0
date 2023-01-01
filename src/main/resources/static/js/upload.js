if (localStorage.hasOwnProperty("accessToken"))
   {
       console.log("allowed");
}else {

    alert("로그인해주세요")
    history.back();
}
let urlpath= window.location.pathname.split('/');
const postId = urlpath[3];
const board = urlpath[1];
console.log("url 확인 : " + board+" and :" + postId );
const uploadUrl = ("/"+board+"/"+postId+"/form");


function formUpdate(){

    const adminPostRequest = {

    }
    $.ajax({
        type:"POST",
        url:uploadUrl,
        dataType: "json",
        headers: {"Authorization": accessToken},
        contentType : "application/json",
        data: JSON.stringify({
            "homeName": $("#homeName").val(),
            "homeTitle": $("#homeTitle").val(),
            "homeVideo": $("#homeVideo").val(),
            "homeAddress": $("#homeAddress").val(),
            "homeRegister": $("#homeRegister").val(),
            "homeChildren": $("#homeChildren").val(),
            "homeNumber": $("#homeNumber").val(),
            "homeMeal": $("#homeMeal").val(),
            "homeDetails": $("#homeDetails").val(),
            "homeCCTV": $("#homeCCTV").val(),
            "homeSize": $("#homeSize").val()
        }),

    }).done(function (result){
        console.log(result)
    }).fail(function (jqXHR) {
        console.log(jqXHR);
        $("#errorMsg").show();
    }).always(function () {
        console.log("실행되는지 확인")
    })
}



function formSend(){

    $.ajax({
        type:"POST",
        url:"/homes/formSend",
        headers: {"Authorization": accessToken},
        contentType : "application/json",
        data: JSON.stringify({
            "homeName": $("#homeName").val(),
            "homeTitle": $("#homeTitle").val(),
            "homeVideo": $("#homeVideo").val(),
            "homeAddress": $("#homeAddress").val(),
            "homeRegister": $("#homeRegister").val(),
            "homeChildren": $("#homeChildren").val(),
            "homeNumber": $("#homeNumber").val(),
            "homeMeal": $("#homeMeal").val(),
            "homeDetails": $("#homeDetails").val(),
            "homeCCTV": $("#homeCCTV").val(),
            "homeSize": $("#homeSize").val()
        }),

    }).done(function (result){
console.log("확인")
window.location.replace('http://localhost:9011/homes/posts');

    }).fail(function ( jqXHR) {
        console.log(jqXHR);


        console.log("failed")
        $("#errorMsg").show();
    }).always(function () {
        console.log("실행되는지 확인")
    })
}

