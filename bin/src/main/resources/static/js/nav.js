document.addEventListener('DOMContentLoaded', function() {
    // Menu Toggle
    const menuToggle = document.getElementById('menu-toggle');
    const sideMenu = document.getElementById('side-menu');
    const menuOverlay = document.getElementById('menu-overlay');
    const sideMenuClose = document.getElementById('side-menu-close');
    
    // Hàm mở menu
    function openMenu() {
        sideMenu.classList.add('active');
        menuOverlay.classList.add('active');
        document.body.style.overflow = 'hidden'; // Ngăn scroll trang khi menu mở
    }

    // Hàm đóng menu
    function closeMenu() {
        sideMenu.classList.remove('active');
        menuOverlay.classList.remove('active');
        document.body.style.overflow = ''; // Cho phép scroll lại
    }

    // Sự kiện click nút menu
    menuToggle.addEventListener('click', openMenu);

    // Sự kiện click nút đóng
    sideMenuClose.addEventListener('click', closeMenu);

    // Sự kiện click overlay
    menuOverlay.addEventListener('click', closeMenu);

    // Đóng menu khi ấn ESC
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') closeMenu();
    });

    // Theme Toggle
    const themeToggle = document.getElementById('theme-toggle');
    
    themeToggle.addEventListener('click', () => {
        document.body.classList.toggle('dark-theme');
        const isDark = document.body.classList.contains('dark-theme');
        localStorage.setItem('theme', isDark ? 'dark' : 'light');
        themeToggle.innerHTML = isDark ? 
            '<i class="fas fa-sun"></i>' : 
            '<i class="fas fa-moon"></i>';
    });

    // Weather Widget
    async function updateWeather() {
        try {
            const position = await new Promise((resolve, reject) => {
                navigator.geolocation.getCurrentPosition(resolve, reject);
            });
            
            const response = await fetch(`https://api.openweathermap.org/data/2.5/weather?lat=${position.coords.latitude}&lon=${position.coords.longitude}&appid=YOUR_API_KEY&units=metric`);
            const data = await response.json();
            
            document.getElementById('current-weather').innerHTML = `
                <i class="fas fa-cloud"></i>
                <span>${Math.round(data.main.temp)}°C ${data.name}</span>
            `;
        } catch (error) {
            console.error('Weather error:', error);
        }
    }

    updateWeather();
});