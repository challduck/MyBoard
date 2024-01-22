function signup(){

    let nickname = document.getElementById("nickname").value;
    let email = document.getElementById("email").value;
    let password = document.getElementById("password").value;

    if (nickname === "") {
        return alert("닉네임을 입력해 주세요.");
    }
    if (email === "") {
        return alert("이메일을 입력해 주세요.");
    }
    if (password === "") {
        return alert("비밀번호를 입력해 주세요.");
    }

    let formData = {
        nickname:nickname,
        email:email,
        password:password
    }

    fetch('/api/user/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (response.status === 201){
                return response.json();
            }
            else {
                return response.text();
            }
        })
        .then(data => {
            // 서버 응답 처리
            if(data.status === 201){
                window.location.href = "/login";
            }
            else {
                alert(data);
            }
        })
        .catch(error => {
            // 오류 처리
            console.log(error);
        });
}
function login(){
    let email = document.getElementById("email").value;
    let password = document.getElementById("password").value;

    if(email === "" || password === ""){
        return alert("이메일 또는 비밀번호를 입력해 주세요.");
    }

    let formData = {
        email: email,
        password: password
    }

    fetch('/api/user/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (response.status === 200){
                const header = response.headers.get('Authorization');
                const token = header.split(" ")[1];
                if (token !== null){
                    localStorage.setItem("access_token", token);
                    window.location.href = "/";
                    return response.json();
                }
            }
            else {
                return response.text();
            }
        })
        .then(data => {
            // 서버 응답 처리
            alert(data);
        })
        .catch(error => {
            // 오류 처리
            console.log(error);
        });
}

function logout(){
    function success(){
        document.cookie = "refresh_token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        localStorage.removeItem("access_token");
        window.location.href = "/";
    }
    function fail(){
        alert("로그아웃 실패. 다시 시도해주세요.");
    }
    httpRequest("GET", "/api/user/logout", null, success, fail );
}