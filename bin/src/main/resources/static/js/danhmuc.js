function showToast(message, type = 'success') {
    Toastify({
        text: message,
        duration: 3000,
        gravity: "top",
        position: "right",
        close: true,
        backgroundColor: type === 'success' ? '#4CAF50' : '#F44336',
        stopOnFocus: true,
        style: {
            minWidth: '300px',
            maxWidth: '500px',
            fontSize: '16px',
            padding: '15px 20px',
            margin: '15px',
            borderRadius: '8px',
            boxShadow: '0 4px 12px rgba(0,0,0,0.15)'
        }
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
            // Chuyển hướng và thêm thông báo sau khi xóa
            window.location.href = `/admin/danhmuc/delete/${id}`;
        }
    });
}

function resetForm() {
    // Reset form về trạng thái ban đầu
    const form = document.getElementById('danhMucForm');
    form.reset();

    // Clear input hidden maDanhMuc
    const maDanhMucInput = form.querySelector('input[name="maDanhMuc"]');
    if (maDanhMucInput) {
        maDanhMucInput.value = '';
    }

    // Reset các trường input
    form.querySelector('input[name="tenDanhMuc"]').value = '';
    form.querySelector('input[name="duongDan"]').value = '';
    
    // Reset select về option đầu tiên
    const select = form.querySelector('select[name="danhMucCha"]');
    if (select) {
        select.selectedIndex = 0;
    }

    // Clear textarea
    const moTaTextarea = form.querySelector('textarea[name="moTa"]');
    if (moTaTextarea) {
        moTaTextarea.value = '';
    }

    // Đổi text nút submit về "Lưu danh mục"
    const submitButton = form.querySelector('button[type="submit"] span');
    if (submitButton) {
        submitButton.textContent = 'Lưu danh mục';
    }
}