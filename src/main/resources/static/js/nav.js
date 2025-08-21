document.addEventListener('DOMContentLoaded', function() {
    // Menu Toggle
    const menuToggle = document.getElementById('menu-toggle');
    const sideMenu = document.getElementById('side-menu');
    const menuOverlay = document.getElementById('menu-overlay');
    const sideMenuClose = document.getElementById('side-menu-close');
    const authModal = document.getElementById('authModal');
    const loginBtn = document.querySelector('.signin-btn');
    const closeModal = document.querySelector('.close-modal');
    const tabBtns = document.querySelectorAll('.auth-tab-btn');
    const authForms = document.querySelectorAll('.auth-form');
    
    // Form elements
    const loginForm = document.getElementById('loginForm').querySelector('form');
    const registerForm = document.getElementById('registerForm').querySelector('form');
    
    // Kiểm tra các elements tồn tại
    if(menuToggle && sideMenu && menuOverlay && sideMenuClose) {
        // Menu functions
        function openMenu() {
            sideMenu.classList.add('active');
            menuOverlay.classList.add('active');
            document.body.style.overflow = 'hidden';
        }

        function closeMenu() {
            sideMenu.classList.remove('active');
            menuOverlay.classList.remove('active');
            document.body.style.overflow = '';
        }

        // Menu event listeners
        menuToggle.addEventListener('click', openMenu);
        sideMenuClose.addEventListener('click', closeMenu);
        menuOverlay.addEventListener('click', closeMenu);
    }

    // Auth modal functions
    if(loginBtn && authModal && closeModal) {
        loginBtn.addEventListener('click', function() {
            authModal.style.display = 'block';
            document.body.style.overflow = 'hidden';
        });

        closeModal.addEventListener('click', function() {
            authModal.style.display = 'none';
            document.body.style.overflow = '';
        });

        window.addEventListener('click', function(e) {
            if (e.target === authModal) {
                authModal.style.display = 'none';
                document.body.style.overflow = '';
            }
        });
    }

    // Tab switching
    if(tabBtns && authForms) {
        tabBtns.forEach(btn => {
            btn.addEventListener('click', function() {
                tabBtns.forEach(b => b.classList.remove('active'));
                authForms.forEach(f => f.classList.remove('active'));
                
                this.classList.add('active');
                document.getElementById(this.dataset.tab + 'Form').classList.add('active');
            });
        });
    }

    // Form validation and submission
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const email = this.querySelector('input[name="email"]').value;
            const matKhau = this.querySelector('input[name="matKhau"]').value;
            
            if (!email || !matKhau) {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi',
                    text: 'Vui lòng điền đầy đủ thông tin!'
                });
                return;
            }
            
            // Thêm loading state
            const submitBtn = this.querySelector('button[type="submit"]');
            submitBtn.disabled = true;
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang xử lý...';
            
            this.submit();
        });
    }

    if (registerForm) {
        registerForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const email = this.querySelector('input[name="email"]').value;
            const hoTen = this.querySelector('input[name="hoTen"]').value; // Đã đúng với name trong form
            const matKhau = this.querySelector('input[name="matKhau"]').value;
            const xacNhanMatKhau = this.querySelector('input[name="xacNhanMatKhau"]').value;
            
            if (!email || !hoTen || !matKhau || !xacNhanMatKhau) {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi',
                    text: 'Vui lòng điền đầy đủ thông tin!'
                });
                return;
            }
            
            if (matKhau !== xacNhanMatKhau) {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi',
                    text: 'Mật khẩu xác nhận không khớp!'
                });
                return;
            }
            
            // Thêm loading state
            const submitBtn = this.querySelector('button[type="submit"]');
            submitBtn.disabled = true;
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang xử lý...';
            
            this.submit();
        });
    }

    // Theme Toggle
    const themeToggle = document.getElementById('theme-toggle');
    if(themeToggle) {
        themeToggle.addEventListener('click', () => {
            document.body.classList.toggle('dark-theme');
            const isDark = document.body.classList.contains('dark-theme');
            localStorage.setItem('theme', isDark ? 'dark' : 'light');
            themeToggle.innerHTML = isDark ? 
                '<i class="fas fa-sun"></i>' : 
                '<i class="fas fa-moon"></i>';
        });
    }

    // Close modals on ESC
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            if(sideMenu && sideMenu.classList.contains('active')) {
                closeMenu();
            }
            if(authModal && authModal.style.display === 'block') {
                authModal.style.display = 'none';
                document.body.style.overflow = '';
            }
        }
    });

    // Hiển thị thông báo nếu có
    const message = document.querySelector('#message');
    const error = document.querySelector('#error');
    
    if(message) {
        Swal.fire({
            icon: 'success',
            title: 'Thành công',
            text: message.innerText,
            timer: 1500
        });
    }
    
    if(error) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi',
            text: error.innerText
        });
    }

    // Nếu đã đăng nhập thì ẩn form
    const userInfo = document.querySelector('.user-menu');
    if(userInfo) {
        const authModal = document.getElementById('authModal');
        if(authModal) {
            authModal.style.display = 'none';
        }
    }

    // User Menu Dropdown
    const userButton = document.getElementById('userMenuButton');
    const userDropdown = document.getElementById('userDropdown');

    if (userButton && userDropdown) {
        userButton.addEventListener('click', function(e) {
            e.stopPropagation(); // Ngăn sự kiện click lan ra document
            userDropdown.classList.toggle('show');
        });

        // Đóng dropdown khi click ra ngoài
        document.addEventListener('click', function(e) {
            if (!userButton.contains(e.target) && !userDropdown.contains(e.target)) {
                userDropdown.classList.remove('show');
            }
        });
    }

    // Notification Handling
    const notificationButton = document.getElementById('notificationButton');
    const notificationDropdown = document.getElementById('notificationDropdown');

    if (notificationButton && notificationDropdown) {
        // Toggle notification dropdown
        notificationButton.addEventListener('click', function(e) {
            e.stopPropagation();
            notificationDropdown.classList.toggle('show');
            loadNotifications();
        });

        // Close dropdown when clicking outside
        document.addEventListener('click', function(e) {
            if (!notificationDropdown.contains(e.target) && !notificationButton.contains(e.target)) {
                notificationDropdown.classList.remove('show');
            }
        });

        // Load notifications
        function loadNotifications() {
            fetch('/api/thongbao/list')
                .then(response => response.json())
                .then(notifications => {
                    const notificationList = document.querySelector('.notification-list');
                    if (!notifications || notifications.length === 0) {
                        notificationList.innerHTML = '<div class="no-notifications">Không có thông báo mới</div>';
                        return;
                    }

                    notificationList.innerHTML = notifications.map(notification => `
                        <div class="notification-item ${notification.trangThai === 'Chưa đọc' ? 'unread' : ''}">
                            <div class="notification-content">
                                <h4>${notification.tieuDe}</h4>
                                <p>${notification.noiDung}</p>
                                <span class="notification-time">
                                    ${new Date(notification.ngayGui).toLocaleString('vi-VN')}
                            </div>
                        </div>
                    `).join('');

                    // Update badge count
                    const badge = document.querySelector('.notification-badge');
                    const unreadCount = notifications.filter(n => n.trangThai === 'Chưa đọc').length;
                    if (unreadCount > 0) {
                        badge.textContent = unreadCount;
                        badge.style.display = 'block';
                    } else {
                        badge.style.display = 'none';
                    }
                })
                .catch(error => {
                    console.error('Error loading notifications:', error);
                });
        }

        // Mark all as read
        const markAllReadButton = document.querySelector('.mark-all-read');
        if (markAllReadButton) {
            markAllReadButton.addEventListener('click', function() {
                fetch('/api/thongbao/mark-all-read', {
                    method: 'POST'
                })
                .then(response => response.json())
                .then(data => {
                    loadNotifications(); // Reload notifications
                    const badge = document.querySelector('.notification-badge');
                    badge.style.display = 'none';
                    // Show success message
                    Swal.fire({
                        icon: 'success',
                        title: 'Thành công',
                        text: 'Đã đánh dấu tất cả thông báo là đã đọc',
                        timer: 1500
                    });
                })
                .catch(error => {
                    console.error('Error marking notifications as read:', error);
                });
            });
        }
    }

    // Auto check for new notifications every minute
    setInterval(() => {
        if (notificationButton && notificationDropdown.classList.contains('show')) {
            loadNotifications();
        }
    }, 60000);
});