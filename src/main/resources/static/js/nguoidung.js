function showToast(message, type = 'success') {
    Toastify({
        text: message,
        duration: 3000,
        gravity: "top",
        position: "right",
        backgroundColor: type === 'success' ? "#4caf50" : "#f44336",
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
            window.location.href = `/admin/nguoidung/delete/${id}`;
        }
    });
}

function resetForm() {
    document.getElementById('nguoiDungForm').reset();
    // Reset hidden id field
    document.querySelector('input[name="maNguoiDung"]').value = '';
    // Change button text back to "Thêm mới"
    document.querySelector('button[type="submit"] span').textContent = 'Thêm mới';
}