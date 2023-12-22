
function getMyPageData(){
    function success(res){
        res.then((data)=> {
            let memberId = document.getElementById('member-id');
            let memberNickname = document.getElementById('member-nickname');
            let memberCreatedAt = document.getElementById('member-created-at');
            console.log(data);
            memberId.textContent = data.email;
            memberNickname.textContent = data.nickname;
            memberCreatedAt.textContent = formatDate(new Date(data.createdAt));

            if(data.oauth){
                const changePasswordBtnBox = document.getElementById("changePasswordBtnBox");
                changePasswordBtnBox.style.display = "none";
            }
        })
    }

     httpRequest('GET','/api/user/my-page',null,success,null);
}

// edit modal open
function passwordEditModal(){
    let passwordEditModal = document.getElementById('password-edit-modal');
    passwordEditModal.className = 'modal d-block';
}

// edit modal close
let passwordEditModalClose = document.getElementById('password-edit-modal-close');
if (passwordEditModalClose){
    passwordEditModalClose.addEventListener('click',()=>{
        let commentEditModal = document.getElementById('password-edit-modal');
        commentEditModal.className = 'modal';
    })
}
function changePassword (){
    let oldPasswordValue = document.getElementById('old-password-edit-body').value;
    let newPasswordValue = document.getElementById('new-password-edit-body').value;
    let newPasswordValueCheck = document.getElementById('new-password-edit-body-check').value;

    if(newPasswordValue !== newPasswordValueCheck){
        alert("변경할 비밀번호가 일치하지 않습니다.");
    } else {
        let body = JSON.stringify({
            existingPassword:oldPasswordValue,
            newPassword:newPasswordValue
        });
        function success(){
            alert("비밀번호 변경 성공.\n다시 로그인 해주세요.");
            document.cookie = "refresh_token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
            localStorage.removeItem("access_token");
            window.location.reload();
        }
        function fail(error){alert("잘못된 접근입니다.\n다시 로그인하여 시도해주세요." + error)}

        httpRequest('POST', '/api/user/new-password', body, success, fail);
    }
}

// edit modal open
function nicknameEditModal(){
    let nicknameEditModal = document.getElementById('nickname-edit-modal');
    nicknameEditModal.className = 'modal d-block';
}

// edit modal close
let nicknameEditModalClose = document.getElementById('nickname-edit-modal-close');
if (nicknameEditModalClose){
    nicknameEditModalClose.addEventListener('click',()=>{
        let commentEditModal = document.getElementById('nickname-edit-modal');
        commentEditModal.className = 'modal';
    })
}
function changeNickname (){
    let nicknameEditBody = document.getElementById('nickname-edit-body');
    let body = JSON.stringify({newNickname:nicknameEditBody.value});
    function success(){alert("닉네임 변경 성공."); window.location.reload();}
    function fail(){alert("잘못된 접근입니다.\n다시 로그인하여 시도해주세요.")}

    httpRequest('POST', '/api/user/new-nickname', body, success, fail);
}


getMyPageData();