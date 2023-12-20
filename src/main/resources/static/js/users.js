function signup(){

    let formData = {
        nickname: document.getElementById("nickname").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    }

    fetch('/api/user/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => response.json())
        .then(data => {
            // 서버 응답 처리
            console.log(data);
            if(data.status === 201){
                alert(data.message);
                window.location.href = "/login";
            }
        })
        .catch(error => {
            // 오류 처리
            console.error(error);
            alert("회원가입에 실패하였습니다.");
        });
}
function login(){
    let formData = {
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    }

    fetch('/api/user/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            const header = response.headers.get('Authorization');
            const token = header.split(" ")[1];
            if (response.status === 200 && token !== null){
                localStorage.setItem("access_token", token);
                window.location.href = "/";
            }

            response.json()
        })
        .then(data => {
            // 서버 응답 처리
            // console.log(data);
        })
        .catch(error => {
            // 오류 처리
            console.error(error);
        });
}
const logoutButton = document.getElementById("logout-btn")
logoutButton.addEventListener('click',()=>{
    function success(){
        document.cookie = "refresh_token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        localStorage.removeItem("access_token");
        alert("로그아웃 성공");
    }
    function fail(){alert("로그아웃 실패. 다시 시도해주세요.")}
    httpRequest("GET", "/api/user/logout", null, success, fail );
})