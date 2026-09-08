document.addEventListener('DOMContentLoaded', function () {
    // DOM Elements
    const profileImg = document.getElementById('profile-img');
    const loginPopup = document.getElementById('loginPopup');
    const closeLogin = document.getElementById('closeLogin');
    const recoveryPopup = document.getElementById('recoveryPopup');
    const closeRecovery = document.getElementById('closeRecovery');
    const forgotPasswordLink = document.getElementById('forgotPasswordLink');
    const recoveryForm = document.getElementById('recoveryForm');
    const recoveryMessage = document.getElementById('recoveryMessage');
    const loginForm = document.querySelector('#loginPopup form');
    const mainHeader = document.getElementById('mainHeader');
    const faqItems = document.querySelectorAll('.faq-item');
    const noop = { addEventListener() { }, classList: { add() { }, remove() { } }, reset() { }, querySelector() { return {}; } };
    const safeRecoveryPopup = recoveryPopup || noop;
    // Header scroll effect
    window.addEventListener('scroll', function () {
        if (window.scrollY > 50) {
            mainHeader.classList.add('scrolled');
        }
        else {
            mainHeader.classList.remove('scrolled');
        }
    });
    // Show login popup when profile image is clicked
    if (profileImg && loginPopup) {
        profileImg.addEventListener('click', function (e) {
            e.preventDefault();
            loginPopup.classList.add('active');
            document.body.style.overflow = 'hidden';
        });
    }
    // Close login popup
    if (closeLogin && loginPopup) {
        closeLogin.addEventListener('click', function () {
            loginPopup.classList.remove('active');
            document.body.style.overflow = 'auto';
        });
    }
    // Show recovery popup when forgot password link is clicked
    (forgotPasswordLink || noop).addEventListener('click', function (e) {
        e.preventDefault();
        loginPopup.classList.remove('active');
        safeRecoveryPopup.classList.add('active');
    });
    // Close recovery popup
    (closeRecovery || noop).addEventListener('click', function () {
        safeRecoveryPopup.classList.remove('active');
        document.body.style.overflow = 'auto';
    });
    // Close popups when clicking outside the content
    window.addEventListener('click', function (e) {
        if (e.target === loginPopup) {
            loginPopup.classList.remove('active');
            document.body.style.overflow = 'auto';
        }
        if (e.target === safeRecoveryPopup) {
            safeRecoveryPopup.classList.remove('active');
            document.body.style.overflow = 'auto';
        }
    });
    // Handle recovery form submission
    (recoveryForm || noop).addEventListener('submit', function (e) {
        e.preventDefault();
        const email = document.getElementById('recoveryEmail').value;
        // Show loading state
        const submitBtn = recoveryForm.querySelector('button');
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Sending...';
        submitBtn.disabled = true;
        // Simulate sending recovery email
        setTimeout(() => {
            recoveryMessage.textContent = `A password recovery link has been sent to ${email}`;
            recoveryMessage.classList.add('success');
            submitBtn.innerHTML = 'Send Recovery Link';
            submitBtn.disabled = false;
            // Reset form and hide message after 5 seconds
            setTimeout(() => {
                recoveryForm.reset();
                recoveryMessage.classList.remove('success');
                recoveryMessage.textContent = '';
                recoveryPopup.classList.remove('active');
                document.body.style.overflow = 'auto';
            }, 5000);
        }, 1500);
    });
    // Handle login form submission
    if (loginForm) {
        loginForm.addEventListener('submit', function (e) {
            if (loginForm.action.includes('/login/request-otp'))
                return;
            e.preventDefault();
            const submitBtn = loginForm.querySelector('button');
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Logging in...';
            submitBtn.disabled = true;
            setTimeout(() => {
                if (loginPopup) {
                    loginPopup.classList.remove('active');
                }
                document.body.style.overflow = 'auto';
                submitBtn.innerHTML = 'Login';
                submitBtn.disabled = false;
                showNotification('Login successful! Welcome back.', 'success');
            }, 2000);
        });
    }
    // FAQ functionality
    faqItems.forEach(item => {
        const question = item.querySelector('.faq-question');
        question.addEventListener('click', () => {
            const currentlyActive = document.querySelector('.faq-item.active');
            // If the clicked item is already active, close it
            if (currentlyActive && currentlyActive === item) {
                currentlyActive.classList.remove('active');
                return;
            }
            // Close any other open item
            if (currentlyActive) {
                currentlyActive.classList.remove('active');
            }
            // Open the clicked item
            item.classList.add('active');
        });
    });
    // Notification function
    function showNotification(message, type) {
        const notification = document.createElement('div');
        notification.className = `notification ${type}`;
        notification.innerHTML = `
                    <i class="fas fa-${type === 'success' ? 'check-circle' : 'exclamation-circle'}"></i>
                    ${message}
                `;
        document.body.appendChild(notification);
        // Add animation
        setTimeout(() => {
            notification.classList.add('show');
        }, 10);
        // Remove after 5 seconds
        setTimeout(() => {
            notification.classList.remove('show');
            setTimeout(() => {
                document.body.removeChild(notification);
            }, 300);
        }, 5000);
    }
    // Add hover effect to nav items
    const navItems = document.querySelectorAll('nav a');
    navItems.forEach(item => {
        item.addEventListener('mouseenter', function () {
            this.style.transform = 'translateY(-3px)';
        });
        item.addEventListener('mouseleave', function () {
            this.style.transform = 'translateY(0)';
        });
    });
});
//# sourceMappingURL=services.js.map