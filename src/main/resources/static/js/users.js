function signup(){

    let formData = {
        nickname: document.getElementById("nickname").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    }

    fetch('/user', {
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
        })
        .catch(error => {
            // 오류 처리
            console.error(error);
        });

}

function login(){
    let formData = {
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    }

    fetch('/signin', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            response.json()
            if (response.status === 200){
                window.location.href = "/index";
            }
        })
        .then(data => {
            // 서버 응답 처리
            console.log(data);
        })
        .catch(error => {
            // 오류 처리
            console.error(error);
        });

}