document.addEventListener('DOMContentLoaded', function() {
    const likeBtn = document.querySelector('.like-btn');
    const dislikeBtn = document.querySelector('.dislike-btn');
    const postId = likeBtn.dataset.postId;

    // Check user's current interaction status
    fetch(`/api/tuongtac/user-status/${postId}`)
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error('Network response was not ok');
        })
        .then(data => {
            if (data.status === 'like') {
                likeBtn.classList.add('active');
            } else if (data.status === 'dislike') {
                dislikeBtn.classList.add('active');
            }
        })
        .catch(error => console.error('Error:', error));

    // Handle like button click
    likeBtn.addEventListener('click', function() {
        fetch(`/api/tuongtac/like/${postId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error('Network response was not ok');
        })
        .then(data => {
            // Update UI based on action
            if (data.action === 'liked') {
                likeBtn.classList.add('active');
                dislikeBtn.classList.remove('active');
            } else if (data.action === 'unliked') {
                likeBtn.classList.remove('active');
            }
            // Refresh counts
            updateCounts();
        })
        .catch(error => {
            console.error('Error:', error);
            if (error.message.includes('401')) {
                Swal.fire({
                    title: 'Thông báo',
                    text: 'Vui lòng đăng nhập để thực hiện chức năng này',
                    icon: 'warning',
                    confirmButtonText: 'Đăng nhập',
                    showCancelButton: true,
                    cancelButtonText: 'Hủy'
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.href = '/login';
                    }
                });
            }
        });
    });

    // Handle dislike button click
    dislikeBtn.addEventListener('click', function() {
        fetch(`/api/tuongtac/dislike/${postId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error('Network response was not ok');
        })
        .then(data => {
            // Update UI based on action
            if (data.action === 'disliked') {
                dislikeBtn.classList.add('active');
                likeBtn.classList.remove('active');
            } else if (data.action === 'undisliked') {
                dislikeBtn.classList.remove('active');
            }
            // Refresh counts
            updateCounts();
        })
        .catch(error => {
            console.error('Error:', error);
            if (error.message.includes('401')) {
                Swal.fire({
                    title: 'Thông báo',
                    text: 'Vui lòng đăng nhập để thực hiện chức năng này',
                    icon: 'warning',
                    confirmButtonText: 'Đăng nhập',
                    showCancelButton: true,
                    cancelButtonText: 'Hủy'
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.href = '/login';
                    }
                });
            }
        });
    });

    // Function to update like/dislike counts
    function updateCounts() {
        fetch(`/api/tuongtac/counts/${postId}`)
            .then(response => response.json())
            .then(data => {
                document.querySelector('.like-count').textContent = data.likes;
                document.querySelector('.dislike-count').textContent = data.dislikes;
            })
            .catch(error => console.error('Error:', error));
    }

    // Share functionality
    const shareBtn = document.querySelector('.share-btn');
    const shareModal = document.getElementById('shareModal');
    const closeShareModal = document.querySelector('.close-share-modal');
    const shareForm = document.getElementById('shareForm');

    if (shareBtn && shareModal && closeShareModal && shareForm) {
        // Open modal
        shareBtn.addEventListener('click', function() {
            shareModal.style.display = 'block';
        });

        // Close modal
        closeShareModal.addEventListener('click', function() {
            shareModal.style.display = 'none';
        });

        // Close modal when clicking outside
        window.addEventListener('click', function(e) {
            if (e.target === shareModal) {
                shareModal.style.display = 'none';
            }
        });

        // Handle form submission
        shareForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const email = this.querySelector('input[name="email"]').value;
            const postId = shareBtn.dataset.postId;

            // Send share request
            fetch('/share/email', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `articleId=${postId}&email=${encodeURIComponent(email)}`
            })
            .then(response => response.json())
            .then(data => {
                if (data.message) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Thành công!',
                        text: data.message
                    });
                    shareModal.style.display = 'none';
                    shareForm.reset();
                } else {
                    throw new Error(data.error || 'Có lỗi xảy ra');
                }
            })
            .catch(error => {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi!',
                    text: error.message
                });
            });
        });
    }
}); 