function showPostDetail(postId) {
    // Gọi API lấy chi tiết bài viết
    fetch(`/admin/baiviet/detail/${postId}`)
        .then(response => response.json())
        .then(data => {
            // Cập nhật dữ liệu vào biến Thymeleaf
            window.selectedPost = data;
            
            // Hiển thị tab chi tiết
            $('#list-posts').removeClass('show active');
            $('#post-detail').addClass('show active');
        });
}

function showListPosts() {
    // Quay lại tab danh sách
    $('#post-detail').removeClass('show active');
    $('#list-posts').addClass('show active');
}

function approveBaiViet(id) {
    if (confirm('Bạn có chắc muốn duyệt bài viết này?')) {
        fetch(`/admin/baiviet/approve/${id}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Hiển thị thông báo thành công
                Swal.fire({
                    icon: 'success',
                    title: 'Thành công!',
                    text: data.message,
                    toast: true,
                    position: 'top-end',
                    showConfirmButton: false,
                    timer: 3000
                });
                
                // Cập nhật UI
                updateArticleStatus(id, 'Đã xuất bản');
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi!',
                    text: data.message
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Đã xảy ra lỗi khi duyệt bài viết'
            });
        });
    }
}

function rejectBaiViet(id) {
    Swal.fire({
        title: 'Từ chối bài viết',
        input: 'textarea',
        inputLabel: 'Lý do từ chối',
        inputPlaceholder: 'Nhập lý do từ chối...',
        showCancelButton: true,
        confirmButtonText: 'Từ chối',
        cancelButtonText: 'Hủy',
        showLoaderOnConfirm: true,
        preConfirm: (lyDo) => {
            if (!lyDo) {
                Swal.showValidationMessage('Vui lòng nhập lý do từ chối');
                return false;
            }
            
            const formData = new FormData();
            formData.append('lyDo', lyDo);
            
            return fetch(`/admin/baiviet/reject/${id}`, {
                method: 'POST',
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                if (!data.success) {
                    throw new Error(data.message)
                }
                return data;
            })
            .catch(error => {
                Swal.showValidationMessage(error.message);
            })
        },
        allowOutsideClick: () => !Swal.isLoading()
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: result.value.message,
                toast: true,
                position: 'top-end',
                showConfirmButton: false,
                timer: 3000
            });
            
            // Cập nhật UI
            updateArticleStatus(id, 'Từ chối');
        }
    });
}

function updateArticleStatus(id, status) {
    // Cập nhật trạng thái trong bảng/danh sách bài viết
    const statusCell = document.querySelector(`[data-article-id="${id}"] .article-status`);
    if (statusCell) {
        statusCell.textContent = status;
        statusCell.className = `article-status status-${status.toLowerCase().replace(' ', '-')}`;
    }
    
    // Ẩn các nút duyệt/từ chối nếu cần
    const actionButtons = document.querySelector(`[data-article-id="${id}"] .action-buttons`);
    if (actionButtons) {
        actionButtons.style.display = 'none';
    }
}