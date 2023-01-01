function userSignIns(){
$.ajax({
    type:"POST",
    url:"/signup",
    dataType: "json",
    contentType : "application/json",
    data: JSON.stringify({
        "username" : $("#username").val(),
        "email": $("#email").val(),
        "password": $("#password").val(),
        "nickName": $("#nickname").val(),
        "child":$("#child").val(),
        "memo": $("#memo").val()
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