        // Profile dropdown functionality
        document.addEventListener('DOMContentLoaded', function() {
            const profileBtn = document.querySelector('.profile-btn');
            const dropdownMenu = document.querySelector('.dropdown-menu');
            
            // Toggle dropdown menu
            profileBtn.addEventListener('click', function(e) {
                e.stopPropagation();
                dropdownMenu.style.display = dropdownMenu.style.display === 'block' ? 'none' : 'block';
            });
            
            // Close dropdown when clicking outside
            document.addEventListener('click', function() {
                dropdownMenu.style.display = 'none';
            });
            
            // Prevent dropdown from closing when clicking inside it
            dropdownMenu.addEventListener('click', function(e) {
                e.stopPropagation();
            });
            
            // Active link highlighting
            const currentPage = window.location.pathname.split('/').pop() || 'index.html';
            document.querySelectorAll('nav a').forEach(link => {
                const linkPage = link.getAttribute('href').split('/').pop();
                if (currentPage === linkPage || 
                    (currentPage === '' && linkPage === 'index.html') ||
                    (currentPage === 'services' && linkPage === 'services')) {
                    link.classList.add('active');
                } else {
                    link.classList.remove('active');
                }
            });

            // FAQ Accordion functionality
            const faqQuestions = document.querySelectorAll('.faq-question');
            faqQuestions.forEach(question => {
                question.addEventListener('click', () => {
                    const answer = question.nextElementSibling;
                    const isActive = question.classList.contains('active');
                    
                    // Close all other FAQs
                    document.querySelectorAll('.faq-question').forEach(q => {
                        if (q !== question) {
                            q.classList.remove('active');
                            q.nextElementSibling.classList.remove('show');
                        }
                    });
                    
                    // Toggle current FAQ
                    question.classList.toggle('active');
                    answer.classList.toggle('show');
                });
            });

            // Initialize service card animations
            const serviceCards = document.querySelectorAll('.service-card');
            serviceCards.forEach(card => {
                const delay = card.style.animationDelay || '0s';
                card.style.animationDelay = delay;
            });
        });

        // Page navigation functionality
        function showPage(pageId) {
            // Hide all pages
            document.querySelectorAll('.page-content').forEach(page => {
                page.classList.remove('active');
            });
            
            // Show the selected page
            document.getElementById(pageId).classList.add('active');
            
            // Update active link in navigation
            document.querySelectorAll('nav a').forEach(link => {
                link.classList.remove('active');
            });
            
            // Don't update nav link if it's a profile page (since it's not in the main nav)
            if (!pageId.includes('profile') && !pageId.includes('orders') && 
                !pageId.includes('wishlist') && !pageId.includes('settings')) {
                document.querySelector(`nav a[href="/${pageId.replace('-page', '')}"]`).classList.add('active');
            }
            
            // Scroll to top
            window.scrollTo(0, 0);
        }

        // Initialize the page
        document.addEventListener('DOMContentLoaded', function() {
            // Set the services page as active by default
            showPage('services-page');
        });

        // Clear wishlist function
        function clearWishlist() {
            if (confirm('Are you sure you want to clear your wishlist?')) {
                document.getElementById('wishlist-items').innerHTML = '';
                alert('Wishlist cleared successfully!');
            }
        }