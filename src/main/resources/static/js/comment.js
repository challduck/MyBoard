function viewComment(articleId){
    fetch(`/api/articles/${articleId}/comments`, {
        method: "GET"
    })
        .then(response => response.json())
        .then(comments => {

            comments.forEach((comment)=>{
                // console.log(comment);
                let commentList = document.getElementById('comment-list');

                // Article Card 생성
                let commentCard = document.createElement('div');
                commentCard.className = "card m-2";
                commentCard.id = `comment-${comment.id}`

                let commentHeader = document.createElement('div');
                commentHeader.className = 'card-header';
                commentHeader.textContent = comment.nickname;

                let editButton = document.createElement('button');
                editButton.type = 'button';
                editButton.className = 'btn btn-sm btn-outline-primary mx-2';
                editButton.textContent = '수정';
                editButton.addEventListener('click',()=>{
                    viewEditModal(comment.body, comment.id);
                })

                let deleteButton = document.createElement('button');
                deleteButton.type = 'button';
                deleteButton.id = 'delete-btn';
                deleteButton.className = 'btn btn-sm btn-outline-danger comment-delete-btn mx-1';
                deleteButton.textContent = '삭제';
                deleteButton.addEventListener('click',()=>{
                    viewDeleteModal(comment.id);
                })

                let commentBody = document.createElement('div');
                commentBody.className = 'card-body';
                commentBody.textContent = comment.body;


                commentHeader.appendChild(editButton);
                commentHeader.appendChild(deleteButton);

                commentCard.appendChild(commentHeader);
                commentCard.appendChild(commentBody);

                commentList.appendChild(commentCard);

            });

        })
        .catch(err=> console.log(err));
}

function addCommentInput(articleId){
    let commentInputBox = document.getElementById('comment-input-box');
    commentInputBox.className = 'my-2';
    let commentInputForm = document.createElement('form');
    commentInputForm.className = 'mx-2 px-2 border'

    let commentInputBodyLabel = document.createElement('label')
    commentInputBodyLabel.textContent = '댓글 내용';
    commentInputBodyLabel.className = 'my-3'

    let commentInputBody = document.createElement('textarea');
    commentInputBody.rows = 3;
    commentInputBody.className = 'form-control mb-2';
    commentInputBody.id = 'new-comment-body';

    let commentCreateBtn = document.createElement('button');
    commentCreateBtn.textContent = '댓글 작성';
    commentCreateBtn.type = 'button';
    commentCreateBtn.className = 'btn btn-primary mb-2';
    commentCreateBtn.id = 'comment-create-btn';
    commentCreateBtn.addEventListener('click',()=>{
        if(localStorage.getItem('access_token')===null && getCookie('refresh_token') === null){
            alert('로그인 후 댓글을 작성 해주세요.');
        }
        else {
            createComment(articleId);
        }
    })

    commentInputForm.appendChild(commentInputBodyLabel);
    commentInputForm.appendChild(commentInputBody);
    commentInputForm.appendChild(commentCreateBtn);
    commentInputBox.appendChild(commentInputForm);
}

function createComment(articleId){
    let body = JSON.stringify({
       body:document.getElementById('new-comment-body').value
    });

    function success() {
        alert('댓글 작성이 완료되었습니다.');
        window.location.reload();
    }
    function fail() {
        alert('댓글 작성을 실패했습니다.\n지속적으로 실패하신다면 로그인을 다시 시도해 주십시오.');
    }

    httpRequest('POST',`/api/articles/${articleId}/comments`, body, success, fail);
}
function editComment(){
    let commentId = document.getElementById('comment-id-hidden-input').value;

    let body = JSON.stringify({
        body:document.getElementById('comment-edit-body').value
    });
    function success() {
        alert('댓글 수정이 완료되었습니다.');
        window.location.reload();
    }
    function fail() {
        alert('댓글 수정을 실패했습니다.\n본인이 작성한 댓글만 수정 가능합니다..');
    }
    httpRequest('PUT',`/api/comments/${commentId}`, body, success, fail);
}
function deleteComment(){
    let commentId = document.getElementById('comment-id-hidden-input').value;
    let body = JSON.stringify({
        body:document.getElementById('new-comment-body').value
    });

    function success() {
        alert('댓글을 삭제하였습니다.');
        window.location.reload();
    }
    function fail() {
        alert('댓글 삭제를 실패하였습니다. 본인이 작성한 댓글만 삭제 가능합니다!');
    }

    httpRequest('DELETE',`/api/comments/${commentId}`, null, success, fail);
}

// delete modal open
function viewDeleteModal(commentId){
    let commentDeleteModal = document.getElementById('comment-delete-modal');
    commentDeleteModal.className = 'modal d-block';
    let commentIdHiddenInput = document.createElement('input');
    commentIdHiddenInput.type = 'hidden';
    commentIdHiddenInput.style.display = 'none';
    commentIdHiddenInput.id = 'comment-id-hidden-input'
    commentIdHiddenInput.defaultValue = commentId;
    commentDeleteModal.appendChild(commentIdHiddenInput);
}

// edit modal close
let commentDeleteModalClose = document.getElementById('comment-delete-modal-close');
if (commentDeleteModalClose){
    commentDeleteModalClose.addEventListener('click',()=>{
        let commentDeleteModal = document.getElementById('comment-delete-modal');
        commentDeleteModal.className = 'modal';

        let commentId = document.getElementById('comment-id-hidden-input');
        commentId.remove();
    })
}

// edit modal open
function viewEditModal(commentBody, commentId){
    let commentEditModal = document.getElementById('comment-edit-modal');
    commentEditModal.className = 'modal d-block';
    let commentIdHiddenInput = document.createElement('input');
    commentIdHiddenInput.type = 'hidden';
    commentIdHiddenInput.style.display = 'none';
    commentIdHiddenInput.id = 'comment-id-hidden-input';
    commentIdHiddenInput.defaultValue = commentId;

    commentEditModal.appendChild(commentIdHiddenInput);
    let commentEditBody = document.getElementById('comment-edit-body');
    commentEditBody.textContent = commentBody;
}

// edit modal close
let commentEditModalClose = document.getElementById('comment-edit-modal-close');
if (commentEditModalClose){
    commentEditModalClose.addEventListener('click',()=>{
        let commentEditModal = document.getElementById('comment-edit-modal');
        commentEditModal.className = 'modal';

        let commentId = document.getElementById('comment-id-hidden-input');
        commentId.remove();
    })
}