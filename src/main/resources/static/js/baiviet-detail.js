// Article Detail Page JavaScript

// Convert base64 data to image src
function convertBase64ToImage() {
    const images = document.querySelectorAll('img[data-src]');
    images.forEach(img => {
        const base64Data = img.getAttribute('data-src');
        if (base64Data && base64Data.trim() !== '') {
            img.src = 'data:image/jpeg;base64,' + base64Data;
        }
    });
}

// Share functions
function shareOnFacebook() {
    const url = encodeURIComponent(window.location.href);
    const title = encodeURIComponent(document.title);
    const shareUrl = `https://www.facebook.com/sharer/sharer.php?u=${url}`;
    window.open(shareUrl, '_blank', 'width=600,height=400');
}

function shareOnTwitter() {
    const url = encodeURIComponent(window.location.href);
    const title = encodeURIComponent(document.title);
    const shareUrl = `https://twitter.com/intent/tweet?url=${url}&text=${title}`;
    window.open(shareUrl, '_blank', 'width=600,height=400');
}

function shareOnLinkedIn() {
    const url = encodeURIComponent(window.location.href);
    const title = encodeURIComponent(document.title);
    const shareUrl = `https://www.linkedin.com/sharing/share-offsite/?url=${url}`;
    window.open(shareUrl, '_blank', 'width=600,height=400');
}

// Copy link function
function copyToClipboard() {
    const url = window.location.href;
    navigator.clipboard.writeText(url).then(function() {
        // Show success message
        showNotification('Đã sao chép link vào clipboard!', 'success');
    }, function(err) {
        console.error('Could not copy text: ', err);
        showNotification('Không thể sao chép link!', 'error');
    });
}

// Show notification
function showNotification(message, type) {
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;
    
    // Style the notification
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        border-radius: 5px;
        color: white;
        font-weight: 500;
        z-index: 1000;
        animation: slideIn 0.3s ease-out;
        max-width: 300px;
    `;
    
    if (type === 'success') {
        notification.style.backgroundColor = '#28a745';
    } else if (type === 'error') {
        notification.style.backgroundColor = '#dc3545';
    }
    
    // Add to page
    document.body.appendChild(notification);
    
    // Remove after 3 seconds
    setTimeout(() => {
        notification.style.animation = 'slideOut 0.3s ease-in';
        setTimeout(() => {
            if (notification.parentNode) {
                notification.parentNode.removeChild(notification);
            }
        }, 300);
    }, 3000);
}

// Add CSS animations
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(100%);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes slideOut {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(100%);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);

// Image lightbox functionality
document.addEventListener('DOMContentLoaded', function() {
    // Add click event to images for lightbox
    const images = document.querySelectorAll('.featured-image, .gallery-image');
    
    images.forEach(img => {
        img.addEventListener('click', function() {
            openLightbox(this.src, this.alt);
        });
        
        // Add cursor pointer to indicate clickable
        img.style.cursor = 'pointer';
    });
});

function openLightbox(src, alt) {
    // Create lightbox overlay
    const overlay = document.createElement('div');
    overlay.className = 'lightbox-overlay';
    overlay.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.9);
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 2000;
        cursor: pointer;
    `;
    
    // Create image container
    const imgContainer = document.createElement('div');
    imgContainer.style.cssText = `
        max-width: 90%;
        max-height: 90%;
        position: relative;
    `;
    
    // Create image
    const img = document.createElement('img');
    img.src = src;
    img.alt = alt;
    img.style.cssText = `
        max-width: 100%;
        max-height: 100%;
        object-fit: contain;
        border-radius: 8px;
    `;
    
    // Create close button
    const closeBtn = document.createElement('button');
    closeBtn.innerHTML = '&times;';
    closeBtn.style.cssText = `
        position: absolute;
        top: -40px;
        right: 0;
        background: none;
        border: none;
        color: white;
        font-size: 30px;
        cursor: pointer;
        padding: 5px;
    `;
    
    // Add elements to page
    imgContainer.appendChild(img);
    imgContainer.appendChild(closeBtn);
    overlay.appendChild(imgContainer);
    document.body.appendChild(overlay);
    
    // Close lightbox on click
    overlay.addEventListener('click', function(e) {
        if (e.target === overlay || e.target === closeBtn) {
            document.body.removeChild(overlay);
        }
    });
    
    // Close on escape key
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            if (document.body.contains(overlay)) {
                document.body.removeChild(overlay);
            }
        }
    });
}

// Smooth scroll to top
function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
}

// Add scroll to top button
document.addEventListener('DOMContentLoaded', function() {
    const scrollBtn = document.createElement('button');
    scrollBtn.innerHTML = '<i class="fas fa-arrow-up"></i>';
    scrollBtn.className = 'scroll-to-top';
    scrollBtn.style.cssText = `
        position: fixed;
        bottom: 20px;
        right: 20px;
        width: 50px;
        height: 50px;
        border-radius: 50%;
        background: #007bff;
        color: white;
        border: none;
        cursor: pointer;
        display: none;
        align-items: center;
        justify-content: center;
        font-size: 18px;
        z-index: 1000;
        transition: all 0.3s;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
    `;
    
    document.body.appendChild(scrollBtn);
    
    // Show/hide button based on scroll position
    window.addEventListener('scroll', function() {
        if (window.pageYOffset > 300) {
            scrollBtn.style.display = 'flex';
        } else {
            scrollBtn.style.display = 'none';
        }
    });
    
    // Add click event
    scrollBtn.addEventListener('click', scrollToTop);
    
    // Hover effects
    scrollBtn.addEventListener('mouseenter', function() {
        this.style.transform = 'scale(1.1)';
        this.style.background = '#0056b3';
    });
    
    scrollBtn.addEventListener('mouseleave', function() {
        this.style.transform = 'scale(1)';
        this.style.background = '#007bff';
    });
    
    // Convert base64 images
    convertBase64ToImage();
});
