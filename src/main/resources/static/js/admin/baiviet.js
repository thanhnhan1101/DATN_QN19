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