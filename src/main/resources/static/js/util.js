// HTTP 요청을 보내는 함수
function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: { // 로컬 스토리지에서 액세스 토큰 값을 가져와 헤더에 추가
            Authorization: 'Bearer ' + localStorage.getItem('access_token'),
            'Content-Type': 'application/json',
        },
        body: body,
    }).then(response => {
        if (response.status === 200 || response.status === 201) {

            const contentType = response.headers.get("content-type");
            if (contentType && contentType.includes("application/json")){
                return success(response.json());
            }
            else {
                return success();
            }
        }
        const refresh_token = getCookie('refresh_token');
        if (response.status === 401 && refresh_token) {
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    refreshToken: getCookie('refresh_token'),
                }),
            })
                .then(res => {
                    if (res.ok) {
                        return res.json();
                    }
                })
                .then(result => { // 재발급이 성공하면 로컬 스토리지값을 새로운 액세스 토큰으로 교체
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequest(method, url, body, success, fail);
                })
                .catch(error => fail(error));
        } else {
            return fail();
        }
    });
}

// 쿠키 가져오는 메서드
function getCookie(key) {
    let result = null;
    let cookie = document.cookie.split(';');
    cookie.some((item)=> {
        item = item.replace(' ', '');
        let dic = item.split('=');
        if (key === dic[0]) {
            result = dic[1];
            return true;
        }
    });
    return result;
}

// 날짜 포맷
function formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}`;
}

function getArticleUrlPath(){
    const currentPath = window.location.pathname;
    const match = currentPath.match(/\articles\/(\d+)/);
    return match ? match[1] : null;
}
function getNewArticleUrlPath(){
    const currentPath = window.location.pathname;
    const match = currentPath.match(/\/new-article\/(\d+)/);
    return match ? match[1] : null;
}