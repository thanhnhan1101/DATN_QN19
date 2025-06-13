document.addEventListener('DOMContentLoaded', function() {
    const messageInput = document.getElementById('message');
    const messageTypeInput = document.getElementById('messageType');
    const message = messageInput?.value || null;
    const messageType = messageTypeInput?.value || null;

    if (message) {
        showToast(message, messageType);
        // Xóa message khỏi DOM để reload không hiện lại
        messageInput.value = '';
        if (messageTypeInput) messageTypeInput.value = '';
    }
});

function showToast(message, type = 'success') {
    Toastify({
        text: message,
        duration: 3000,
        gravity: "top",
        position: "right",
        backgroundColor: type === 'success' ? "#4caf50" : "#f44336"
    }).showToast();
}

function confirmDelete(id) {
    Swal.fire({
        title: 'Xác nhận xóa?',
        text: "Bạn không thể hoàn tác sau khi xóa!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Xóa',
        cancelButtonText: 'Hủy'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = `/admin/baiviet/delete/${id}`;
        }
    });
}

function resetForm() {
    document.getElementById('baiVietForm').reset();
    // Reset hidden fields
    document.querySelector('input[name="maBaiViet"]').value = '';
    // Change button text back to "Thêm bài viết"
    document.querySelector('button[type="submit"] span').textContent = 'Thêm bài viết';
    // Clear image preview if exists
    const imgPreview = document.querySelector('.img-preview');
    if (imgPreview) imgPreview.remove();
}

function toggleVisibility(id) {
    Swal.fire({
        title: 'Xác nhận',
        text: 'Bạn có chắc muốn thay đổi trạng thái hiển thị của bài viết này?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Đồng ý',
        cancelButtonText: 'Hủy'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = `/admin/baiviet/toggle/${id}`;
        }
    });
}