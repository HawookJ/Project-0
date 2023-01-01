
function findUsers(){
    console.log($("#findUser").val())
  $.ajax({
      type:"POST",
      url:"/findUser",
      data:({
          "userEmail": $("#findUser").val()
      }),

  }).done(function(result){
      console.log(result)
      let nickName = result;

      $(".foundUser").replaceWith("아이디는 : "+ nickName + " 입니다");
  }).fail(function (jqXHR) {
      console.log(jqXHR);


  }).always(function () {
      console.log("실행되는지 확인")


  })
}
