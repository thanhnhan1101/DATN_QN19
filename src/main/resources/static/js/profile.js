document.addEventListener('DOMContentLoaded', function() {
    // Xử lý thông báo
    const message = document.querySelector('.alert-success');
    const error = document.querySelector('.alert-danger');
    
    if (message) {
        Toastify({
            text: message.textContent,
            duration: 3000,
            gravity: "top",
            position: "right",
            backgroundColor: "#28a745",
        }).showToast();
    }
    
    if (error) {
        Toastify({
            text: error.textContent,
            duration: 3000,
            gravity: "top",
            position: "right",
            backgroundColor: "#dc3545",
        }).showToast();
    }

    // Xử lý form cập nhật
    const form = document.querySelector('.profile-update-form');
    if (form) {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            
            Swal.fire({
                title: 'Xác nhận cập nhật?',
                text: "Bạn có chắc muốn cập nhật thông tin cá nhân?",
                icon: 'question',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Cập nhật',
                cancelButtonText: 'Hủy'
            }).then((result) => {
                if (result.isConfirmed) {
                    form.submit();
                }
            });
        });
    }

    // Xử lý preview ảnh đại diện
    const avatarInput = document.querySelector('input[name="avatarFile"]');
    const avatarPreview = document.querySelector('.avatar-img');
    const avatarCircle = document.querySelector('.avatar-circle');

    if (avatarInput && (avatarPreview || avatarCircle)) {
        avatarInput.addEventListener('change', function(e) {
            const file = e.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    if (avatarPreview) {
                        avatarPreview.src = e.target.result;
                        avatarPreview.style.display = 'block';
                        if (avatarCircle) avatarCircle.style.display = 'none';
                    }
                };
                reader.readAsDataURL(file);
            }
        });
    }
}); 