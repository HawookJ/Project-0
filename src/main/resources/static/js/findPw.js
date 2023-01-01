$("#errorMsg2").hide();


function findPw() {
    console.log($(".findPw").val())
    $.ajax({
        type: "POST",
        url: "/sendEmail",
        data: ({
            "userEmail" : $(".findPw").val()
        }),

    }).done(function (result){
    console.log("성공 : 0" + result)
    }).fail(function (jqXHR) {
        console.log(jqXHR);
        $("#errorMsg2").show();

    }).always(function () {
        console.log("실행되는지 확인")


    })
}