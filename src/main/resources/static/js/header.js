let accessToken = (localStorage.hasOwnProperty("accessToken")) ? localStorage.getItem("accessToken") : null;
let refreshToken = (localStorage.hasOwnProperty("refreshToken")) ? localStorage.getItem("refreshToken") : null;
let isLogged = new Boolean(false);
let userNamejwt = null;
let userId= null;

//프론트 쪽에서 decode해서 내보낼순 없을까...
const code = (parseJwt(accessToken));
let userName = code["userName"];
      userId = code["id"];
      console.log(code["userName"]);


//로그인
function userLogin() {
    const loginRequest = {
        "userName": $("#userId").val(),
        "userPassword": $("#userPassword").val()  //안되는이유는?
    };
    $.ajax({
        type: "POST",
        url: "/login",
        dataType: 'json',
        //   contentType: 'application/json',
        data: JSON.stringify({
            "userName": $("#userId").val(),
            "userPassword": $("#userPassword").val()
        }),
    }).done(function (result) {
        console.log(result);
        const accessToken = result["accessToken"];
        const refreshToken= result["refreshToken"];
        localStorage.setItem("accessToken", accessToken);
        localStorage.setItem("refreshToken", refreshToken);
        isLogged = true;
    }).fail(function (jqXHR) {
        console.log(jqXHR);
        $("#errorMsg").show();
    }).always(function () {
        console.log("실행되는지 확인")
    })
}






//매번 확인되는 function (로그인 여부/accesstoken 만료 시) useEffect 처럼 하면 좋겠는데 아직 모르겠다. 계속 토큰을 주고 받고 하는데 추후 다른 방법을 찾아야할것같다.
function check() {
       if (localStorage.hasOwnProperty("accessToken")) { // && localStorage.hasOwnProperty("refreshToken")
        const accessToken = localStorage.getItem("accessToken");
        const refreshToken = localStorage.getItem("refreshToken");
        const getJwt = parseJwt(accessToken);
        userNamejwt = getJwt.userName;
        userId= getJwt.id;
        console.log(getJwt.exp)
        console.log("check userName : " + userNamejwt + " and : " + userId);
//이런식 보다는 다른 방법을 찾아야한다. Interceptor를 새로 만들어서 방식을 바꿔야할듯하다.
        $.ajax({
            type: "GET",
            headers: { "Authorization" : refreshToken },
            url: "/refresh/"+userNamejwt,

            //흠 thymeleaf에 보내기 위해서는 addAttribute를 써야하는데 그럼..계속 전송해야하나?
    }).done(function (result){
        console.log(result)
            const accessToken = result["accessToken"];
            const refreshToken= result["refreshToken"];
            localStorage.setItem("accessToken", accessToken);
            localStorage.setItem("refreshToken", refreshToken);
            const getJwt = parseJwt(accessToken);
            userNamejwt = getJwt.userName;
        $(".option").replaceWith("<div class=\"option2\">\n" +
            "                <button  type=\"button\" class=\"my-button\" onclick=\"MyBtn()\" >" +userNamejwt+ "|</button>\n" +
            "                <ul class=\"myPage\">\n" +
            "                    <li><a href=\"http://localhost:9011/myPage\">마이페이지</a></li>\n" +
            "                </ul>\n" +
            "            </div>")}).fail(function (jqXHR) {
            console.log(jqXHR);

        });

}}
//현재 계속 accessToken을 재발급하고있다.
setInterval(check(),600000)



//토큰 해독제
function parseJwt(token) {
    let base64Url = token.split('.')[1];
    let base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    let jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    return JSON.parse(jsonPayload);
}

// Get the <span> element that closes the modal
let span = document.getElementsByClassName("close")[0];

// When the user clicks on the button, open the modal
function LoginBtn() {
    $("#LoginModal").show();
}

//When the user clicks on <span> (x), close the modal
/*span.onclick = function() {
    LoginModal.style.display = "none";
}*/


function closeBtn() {

    $("#LoginModal").hide();
}

function MyBtn() {
$(".myPage").show();}
