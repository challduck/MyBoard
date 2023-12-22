function getArticleList() {
    fetch("/api/articles", {
        method:"GET"
    })
        .then(response => response.json())
        .then(data => {
            const articleList = document.getElementById('article-card');
            data.forEach(article => {
                let card = document.createElement('div');
                card.className = "card mb-4";

                let cardHeader = document.createElement('div');
                cardHeader.className = 'card-header';
                cardHeader.textContent = article.id;

                let cardBody = document.createElement('div');
                cardBody.className = 'card-body';

                let title = document.createElement('h5');
                title.className = 'card-title';
                title.textContent = article.title;

                let content = document.createElement('p');
                content.className = 'card-text';
                content.textContent = article.content;

                let link = document.createElement('a');
                link.className = 'btn btn-primary';
                link.href = '/articles/' + article.id;
                link.textContent = '보러가기';

                cardBody.appendChild(title);
                cardBody.appendChild(content);
                cardBody.appendChild(link);

                card.appendChild(cardHeader);
                card.appendChild(cardBody);

                articleList.appendChild(card);
        })
    });
}

document.addEventListener('DOMContentLoaded', ()=>{
    let currentPage = window.location.pathname;
    if(currentPage === '/index' ||currentPage === '/' || currentPage === '/home'){
        getArticleList();
    }
});

function viewArticle(articleId) {
    fetch(`/api/articles/${articleId}`, {
        method: "GET"
    })
        .then(response => response.json())
        .then(article => {
            let articleViewBox = document.getElementById('article-view-box');

            // Article Card 생성
            let articleCard = document.createElement('article');
            articleCard.id = 'article-card';

            // Hidden input에 article id 설정
            let articleIdInput = document.createElement('input');
            articleIdInput.type = 'hidden';
            articleIdInput.id = 'article-id';
            articleIdInput.value = article.id;

            // Header 섹션 생성
            let headerSection = document.createElement('header');
            headerSection.className = 'mb-4';

            let titleHeader = document.createElement('h1');
            titleHeader.className = 'fw-bolder mb-1';
            titleHeader.textContent = article.title;

            let headerContainer = document.createElement('div');
            headerContainer.className = 'd-flex justify-content-between';

            let authorCreatedAt = document.createElement('div');
            authorCreatedAt.className = 'text-muted fst-italic mb-2';
            authorCreatedAt.textContent = `| Posted on ${formatDate(new Date(article.created_at))} By ${article.nickname} |`

            let hitCount = document.createElement('div');
            hitCount.className = 'fs-5';
            hitCount.textContent = `HitCount : ${article.hitCount}`;

            headerContainer.appendChild(authorCreatedAt);
            headerContainer.appendChild(hitCount);

            headerSection.appendChild(titleHeader);
            headerSection.appendChild(headerContainer);

            // Content 섹션 생성
            let contentSection = document.createElement('section');
            contentSection.className = 'mb-5';

            let contentParagraph = document.createElement('p');
            contentParagraph.className = 'fs-5 mb-4';
            contentParagraph.textContent = article.content;

            contentSection.appendChild(contentParagraph);

            // 수정 버튼 생성
            let modifyButton = document.createElement('button');
            modifyButton.type = 'button';
            modifyButton.id = 'modify-btn';
            modifyButton.className = 'btn btn-primary btn-sm mr-3';
            modifyButton.textContent = '수정';
            modifyButton.addEventListener('click',()=>{
                window.location.href = `/new-article/${articleId}`;
            })

            // 삭제 버튼 생성
            let deleteButton = document.createElement('button');
            deleteButton.type = 'button';
            deleteButton.id = 'delete-btn';
            deleteButton.className = 'btn btn-secondary btn-sm';
            deleteButton.textContent = '삭제';
            deleteButton.addEventListener('click',()=>{
                articleViewDeleteModal(articleId);
            })

            // 버튼들을 Article Card에 추가
            articleCard.appendChild(articleIdInput);
            articleCard.appendChild(headerSection);
            articleCard.appendChild(contentSection);
            articleCard.appendChild(modifyButton);
            articleCard.appendChild(deleteButton);

            articleViewBox.appendChild(articleCard);
        })
        .catch(error => {
            console.error('Error fetching article details:', error);
        });
}

function getArticleLike(articleId){
    let articleLikeNum = 0;
    let article = document.getElementById('article-like-box');
    fetch(`/api/articles/${articleId}/like`, {
        method: "GET"
    })
        .then(response => response.json())
        .then(data => {
            articleLikeNum = data.articleLike;

            let articleLikeButton = document.createElement('button');
            articleLikeButton.type = 'button';
            articleLikeButton.id = 'article-like-btn';
            articleLikeButton.className = 'btn btn-primary my-3';
            articleLikeButton.textContent = `게시글 추천 ${articleLikeNum}`
            articleLikeButton.addEventListener('click',()=>{
                function success(){
                    alert('게시글 추천 성공!');
                    window.location.reload();
                }
                function fail(){
                    alert('추천은 한 번만 가능 합니다.');
                }
                httpRequest('POST',`/api/articles/${articleId}/like`, null, success, fail);
            })

            article.appendChild(articleLikeButton);
        })
        .catch(err => console.log(err));

}


// 생성 기능
const createButton = document.getElementById('article-create-btn');

if (createButton) {
    createButton.addEventListener('click', event => {
        let body = JSON.stringify({
            title: document.getElementById('article-title').value,
            content: document.getElementById('article-content').value
        });
        function success() {
            alert('등록 완료되었습니다.');
            location.replace('/');
        }
        function fail() {
            alert('등록 실패했습니다.');
        }

        httpRequest('POST','/api/articles', body, success, fail);
    });
}

// 수정 기능
function articleEdit(articleId){
    const modifyButton = document.getElementById('article-modify-btn');

    if (modifyButton) {
        modifyButton.addEventListener('click', event => {

            let body = JSON.stringify({
                title: document.getElementById('article-title').value,
                content: document.getElementById('article-content').value
            })

            function success() {
                alert('수정 완료되었습니다.');
                location.replace(`/articles/${articleId}`);
            }

            function fail() {
                alert('수정 실패했습니다.\n본인이 작성한 게시글만 수정 가능합니다.');
                location.replace(`/articles/${articleId}`);
            }

            httpRequest('PUT',`/api/articles/${articleId}`, body, success, fail);
        });
    }

}


// article delete modal open
function articleViewDeleteModal(articleId){
    let articleDeleteModal = document.getElementById('article-delete-modal');
    articleDeleteModal.className = 'modal d-block';
}

// article edit modal close
let articleDeleteModalClose = document.getElementById('article-delete-modal-close');
if (articleDeleteModalClose){
    articleDeleteModalClose.addEventListener('click',()=>{
        let commentDeleteModal = document.getElementById('article-delete-modal');
        commentDeleteModal.className = 'modal';

        // let commentId = document.getElementById('article-id-hidden-input');
        // commentId.remove();
    })
}

// 삭제 기능
function deleteArticle(articleId){
    const articleDeleteButton = document.getElementById('article-delete-button');

    if (articleDeleteButton) {
        articleDeleteButton.addEventListener('click', event => {
            let id = document.getElementById('article-id').value;
            function success() {
                alert('삭제가 완료되었습니다.');
                location.replace('/');
            }

            function fail() {
                alert('삭제 실패했습니다.\n본인이 작성한 게시글만 삭제 가능합니다.');
            }

            httpRequest('DELETE',`/api/articles/${articleId}`, null, success, fail);
        });
    }


}
