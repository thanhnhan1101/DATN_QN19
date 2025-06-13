document.addEventListener("DOMContentLoaded", function () {
  // Menu Toggle
  const menuToggle = document.getElementById("menu-toggle");
  const sideMenu = document.getElementById("side-menu");
  const menuOverlay = document.getElementById("menu-overlay");
  const sideMenuClose = document.getElementById("side-menu-close");

  // Kiểm tra các elements tồn tại cho menu
  if (menuToggle && sideMenu && menuOverlay && sideMenuClose) {
    // Menu functions
    function openMenu() {
      sideMenu.classList.add("active");
      menuOverlay.classList.add("active");
      document.body.style.overflow = "hidden";
    }

    function closeMenu() {
      sideMenu.classList.remove("active");
      menuOverlay.classList.remove("active");
      document.body.style.overflow = "";
    }

    // Menu event listeners
    menuToggle.addEventListener("click", openMenu);
    sideMenuClose.addEventListener("click", closeMenu);
    menuOverlay.addEventListener("click", closeMenu);
  }

  // Search Toggle - xử lý riêng biệt
  const searchToggle = document.getElementById("searchToggle");
  const searchPopup = document.getElementById("searchPopup");

  if (searchToggle && searchPopup) {
    console.log("Search elements found, initializing search functionality");

    // Kiểm tra các sự kiện click
    searchToggle.addEventListener("click", function (e) {
      console.log("Search toggle clicked");
      e.preventDefault();
      e.stopPropagation();
      searchPopup.classList.toggle("active");

      // Focus vào input khi hiện form
      if (searchPopup.classList.contains("active")) {
        setTimeout(() => {
          const searchInput = searchPopup.querySelector("input");
          if (searchInput) searchInput.focus();
        }, 100);
      }
    });

    // Đóng popup khi click bên ngoài
    document.addEventListener("click", function (e) {
      if (
        searchPopup.classList.contains("active") &&
        !searchPopup.contains(e.target) &&
        e.target !== searchToggle
      ) {
        searchPopup.classList.remove("active");
      }
    });

    // Ngăn sự kiện click bên trong popup lan ra ngoài
    searchPopup.addEventListener("click", function (e) {
      e.stopPropagation();
    });

    // Xử lý tìm kiếm khi submit form
    const searchForm = searchPopup.querySelector("form");
    if (searchForm) {
      searchForm.addEventListener("submit", function (e) {
        e.preventDefault();
        const searchValue = this.querySelector("input").value.trim();
        if (searchValue) {
          // Thực hiện tìm kiếm (thay đổi URL hoặc gửi request)
          window.location.href =
            "/tim-kiem?q=" + encodeURIComponent(searchValue);
        }
      });
    }
  } else {
    console.log("Search elements not found");
  }

  // Weather widget
  const weatherWidget = document.getElementById("current-weather");
  if (weatherWidget) {
    // Lấy vị trí của người dùng và hiển thị thời tiết
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const lat = position.coords.latitude;
          const lon = position.coords.longitude;
          getWeather(lat, lon);
        },
        () => {
          // Nếu người dùng không cho phép vị trí, sử dụng vị trí mặc định (Hà Nội)
          getWeather(21.0285, 105.8542);
        }
      );
    } else {
      weatherWidget.innerHTML =
        '<i class="fas fa-cloud me-1"></i> Không hỗ trợ vị trí';
    }
  }

  // Lấy dữ liệu thời tiết
  function getWeather(lat, lon) {
    const apiKey = "YOUR_API_KEY"; // Thay bằng API key của bạn
    fetch(
      `https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&units=metric&appid=${apiKey}`
    )
      .then((response) => response.json())
      .then((data) => {
        const temp = Math.round(data.main.temp);
        const icon = data.weather[0].icon;
        weatherWidget.innerHTML = `
                    <img src="https://openweathermap.org/img/wn/${icon}.png" alt="Weather icon" width="25">
                    <span>${temp}°C</span>
                `;
      })
      .catch(() => {
        weatherWidget.innerHTML =
          '<i class="fas fa-cloud me-1"></i> Hà Nội, 25°C';
      });
  }

  // Auth modal với Bootstrap
  const signinBtn = document.querySelector(".signin-btn");
  if (signinBtn) {
    const authModal = new bootstrap.Modal(document.getElementById("authModal"));
    signinBtn.addEventListener("click", function () {
      authModal.show();
    });
  }

  // Form validation - Sử dụng Bootstrap validation
  const loginFormEl = document.getElementById("loginForm");
  if (loginFormEl) {
    const form = loginFormEl.querySelector("form");
    form.addEventListener("submit", function (e) {
      e.preventDefault();
      const email = this.querySelector('input[name="email"]').value;
      const password = this.querySelector('input[name="password"]').value;

      if (!email || !password) {
        showAlert("Vui lòng điền đầy đủ thông tin!", "error");
        return;
      }

      this.submit();
    });
  }

  const registerFormEl = document.getElementById("registerForm");
  if (registerFormEl) {
    const form = registerFormEl.querySelector("form");
    form.addEventListener("submit", function (e) {
      e.preventDefault();
      const email = this.querySelector('input[name="email"]').value;
      const hoTen = this.querySelector('input[name="hoTen"]').value;
      const matKhau = this.querySelector('input[name="matKhau"]').value;
      const xacNhanMatKhau = this.querySelector(
        'input[name="xacNhanMatKhau"]'
      ).value;

      if (!email || !hoTen || !matKhau || !xacNhanMatKhau) {
        showAlert("Vui lòng điền đầy đủ thông tin!", "error");
        return;
      }

      if (matKhau !== xacNhanMatKhau) {
        showAlert("Mật khẩu xác nhận không khớp!", "error");
        return;
      }

      this.submit();
    });
  }

  // Dark mode toggle
  const themeSwitcher = document.getElementById("theme-toggle");
  if (themeSwitcher) {
    // Kiểm tra nếu đã lưu theme trong localStorage
    const savedTheme = localStorage.getItem("theme");
    if (savedTheme === "dark") {
      document.body.classList.add("dark-theme");
      themeSwitcher.innerHTML = '<i class="fas fa-sun"></i>';
    }

    themeSwitcher.addEventListener("click", () => {
      document.body.classList.toggle("dark-theme");
      const isDark = document.body.classList.contains("dark-theme");
      localStorage.setItem("theme", isDark ? "dark" : "light");
      themeSwitcher.innerHTML = isDark
        ? '<i class="fas fa-sun"></i>'
        : '<i class="fas fa-moon"></i>';
    });
  }

  // Hiển thị thông báo sử dụng Bootstrap Toast
  function showAlert(message, type) {
    // Tạo toast container nếu chưa có
    let toastContainer = document.querySelector(".toast-container");
    if (!toastContainer) {
      toastContainer = document.createElement("div");
      toastContainer.className =
        "toast-container position-fixed bottom-0 end-0 p-3";
      document.body.appendChild(toastContainer);
    }

    // Tạo toast element
    const toastEl = document.createElement("div");
    toastEl.className = `toast align-items-center text-white bg-${
      type === "error" ? "danger" : "success"
    } border-0`;
    toastEl.setAttribute("role", "alert");
    toastEl.setAttribute("aria-live", "assertive");
    toastEl.setAttribute("aria-atomic", "true");

    toastEl.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        `;

    toastContainer.appendChild(toastEl);
    const toast = new bootstrap.Toast(toastEl, { autohide: true, delay: 3000 });
    toast.show();
  }

  // Hiển thị thông báo từ server
  const messageEl = document.querySelector("#message");
  const errorEl = document.querySelector("#error");

  if (messageEl) {
    showAlert(messageEl.innerText, "success");
  }

  if (errorEl) {
    showAlert(errorEl.innerText, "error");
  }

  // Hiệu ứng Lazy Loading cho hình ảnh
  if ("IntersectionObserver" in window) {
    const lazyImages = document.querySelectorAll("img[data-src]");
    const imageObserver = new IntersectionObserver((entries, observer) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          const img = entry.target;
          img.src = img.dataset.src;
          img.removeAttribute("data-src");
          imageObserver.unobserve(img);
        }
      });
    });

    lazyImages.forEach((img) => {
      imageObserver.observe(img);
    });
  }

  // Tooltip và Popover initialization
  const tooltipTriggerList = [].slice.call(
    document.querySelectorAll('[data-bs-toggle="tooltip"]')
  );
  tooltipTriggerList.map(function (tooltipTriggerEl) {
    return new bootstrap.Tooltip(tooltipTriggerEl);
  });

  const popoverTriggerList = [].slice.call(
    document.querySelectorAll('[data-bs-toggle="popover"]')
  );
  popoverTriggerList.map(function (popoverTriggerEl) {
    return new bootstrap.Popover(popoverTriggerEl);
  });
});
