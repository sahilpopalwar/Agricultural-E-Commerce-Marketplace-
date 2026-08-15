        document.addEventListener('DOMContentLoaded', function() {
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
            const searchInput = document.getElementById('search');
            const productCards = document.querySelectorAll('.product-card');
            const addToCartButtons = document.querySelectorAll('.add-to-cart');

            // Header scroll effect
            window.addEventListener('scroll', function() {
                if (window.scrollY > 50) {
                    mainHeader.classList.add('scrolled');
                } else {
                    mainHeader.classList.remove('scrolled');
                }
            });

            // Show login popup when profile image is clicked
            profileImg.addEventListener('click', function(e) {
                e.preventDefault();
                loginPopup.classList.add('active');
                document.body.style.overflow = 'hidden';
            });

            // Close login popup
            closeLogin.addEventListener('click', function() {
                loginPopup.classList.remove('active');
                document.body.style.overflow = 'auto';
            });

            // Show recovery popup when forgot password link is clicked
            forgotPasswordLink.addEventListener('click', function(e) {
                e.preventDefault();
                loginPopup.classList.remove('active');
                recoveryPopup.classList.add('active');
            });

            // Close recovery popup
            closeRecovery.addEventListener('click', function() {
                recoveryPopup.classList.remove('active');
                document.body.style.overflow = 'auto';
            });

            // Close popups when clicking outside the content
            window.addEventListener('click', function(e) {
                if (e.target === loginPopup) {
                    loginPopup.classList.remove('active');
                    document.body.style.overflow = 'auto';
                }
                if (e.target === recoveryPopup) {
                    recoveryPopup.classList.remove('active');
                    document.body.style.overflow = 'auto';
                }
            });

            // Handle recovery form submission
            recoveryForm.addEventListener('submit', function(e) {
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
            loginForm.addEventListener('submit', function(e) {
                e.preventDefault();
                
                // Show loading state
                const submitBtn = loginForm.querySelector('button');
                submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Logging in...';
                submitBtn.disabled = true;
                
                // Simulate login process
                setTimeout(() => {
                    // In a real application, you would send the form data
                    loginPopup.classList.remove('active');
                    document.body.style.overflow = 'auto';
                    submitBtn.innerHTML = 'Login';
                    submitBtn.disabled = false;
                    
                    // Show success notification
                    showNotification('Login successful! Welcome back.', 'success');
                }, 2000);
            });

            // Add to cart functionality
            addToCartButtons.forEach(button => {
                button.addEventListener('click', function(e) {
                    e.preventDefault();
                    const productCard = this.closest('.product-card');
                    const productName = productCard.querySelector('.product-title').textContent;
                    const productPrice = productCard.querySelector('.product-price').textContent;
                    
                    // Animate button
                    this.innerHTML = '<i class="fas fa-check"></i> Added!';
                    this.style.backgroundColor = '#27ae60';
                    
                    // Show notification
                    showNotification(`${productName} added to cart (${productPrice})`, 'success');
                    
                    // Reset button after delay
                    setTimeout(() => {
                        this.innerHTML = '<i class="fas fa-cart-plus"></i> Add to Cart';
                        this.style.backgroundColor = '';
                    }, 2000);
                });
            });

            // Search functionality
            function searchProducts() {
                const searchTerm = searchInput.value.toLowerCase();
                let foundProducts = false;
                
                productCards.forEach(card => {
                    const title = card.querySelector('.product-title').textContent.toLowerCase();
                    const description = card.querySelector('.product-description').textContent.toLowerCase();
                    
                    if (title.includes(searchTerm) || description.includes(searchTerm)) {
                        card.style.display = 'block';
                        foundProducts = true;
                        // Add animation
                        card.style.animation = 'bounce 0.5s';
                        setTimeout(() => {
                            card.style.animation = '';
                        }, 500);
                    } else {
                        card.style.display = 'none';
                    }
                });
                
                if (!foundProducts && searchTerm) {
                    showNotification('No products found matching your search', 'error');
                }
            }

            // Press Enter to search
            searchInput.addEventListener('keypress', function(e) {
                if (e.key === 'Enter') {
                    searchProducts();
                }
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

            // Add animation to product cards with delay
            productCards.forEach((card, index) => {
                card.style.animationDelay = `${index * 0.1}s`;
            });

            // Add hover effect to nav items
            const navItems = document.querySelectorAll('nav a');
            navItems.forEach(item => {
                item.addEventListener('mouseenter', function() {
                    this.style.transform = 'translateY(-3px)';
                });
                item.addEventListener('mouseleave', function() {
                    this.style.transform = 'translateY(0)';
                });
            });
        });

        // In a real application, this would be replaced with actual product loading
        function loadProducts() {
            // This would typically fetch products from an API
            console.log('Loading products...');
        }
