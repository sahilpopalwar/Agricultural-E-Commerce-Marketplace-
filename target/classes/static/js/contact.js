        // Header scroll effect
        window.addEventListener('scroll', function() {
            const header = document.querySelector('header');
            if (window.scrollY > 50) {
                header.classList.add('scrolled');
            } else {
                header.classList.remove('scrolled');
            }
        });

        // Profile image click to show login popup
        document.getElementById('profile-img').addEventListener('click', function() {
            document.getElementById('loginPopup').classList.add('active');
        });

        // Close login popup
        document.getElementById('closeLogin').addEventListener('click', function() {
            document.getElementById('loginPopup').classList.remove('active');
        });

        // Forgot password link
        document.getElementById('forgotPasswordLink').addEventListener('click', function(e) {
            e.preventDefault();
            document.getElementById('loginPopup').classList.remove('active');
            document.getElementById('recoveryPopup').classList.add('active');
        });

        // Close recovery popup
        document.getElementById('closeRecovery').addEventListener('click', function() {
            document.getElementById('recoveryPopup').classList.remove('active');
        });

        // Form submissions (prevent default for demo)
        document.getElementById('loginForm').addEventListener('submit', function(e) {
            e.preventDefault();
            alert('Login functionality would be implemented here');
            this.reset();
            document.getElementById('loginPopup').classList.remove('active');
        });

        document.getElementById('recoveryForm').addEventListener('submit', function(e) {
            e.preventDefault();
            const message = document.getElementById('recoveryMessage');
            message.textContent = 'Recovery link sent to your email!';
            message.classList.add('success');
            this.reset();
            setTimeout(function() {
                document.getElementById('recoveryPopup').classList.remove('active');
                message.textContent = '';
                message.classList.remove('success');
            }, 2000);
        });

        document.getElementById('contactForm').addEventListener('submit', function(e) {
            e.preventDefault();
            alert('Thank you for your message! We will get back to you soon.');
            this.reset();
        });

        function submitSuggestion() {
            const suggestion = document.getElementById('suggestion-input').value;
            if (suggestion.trim() !== '') {
                alert('Thank you for your suggestion: ' + suggestion);
                document.getElementById('suggestion-input').value = '';
            } else {
                alert('Please enter a suggestion before submitting.');
            }
        }

        // Animate elements when they come into view
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.style.opacity = 1;
                    entry.target.style.transform = 'translateY(0)';
                }
            });
        }, { threshold: 0.1 });

        const animatedElements = document.querySelectorAll('.contact-section, .map-container, .business-hours, .contact-form, .social-links, .suggestion');
        animatedElements.forEach(el => {
            el.style.opacity = 0;
            el.style.transform = 'translateY(20px)';
            el.style.transition = 'opacity 0.6s ease-out, transform 0.6s ease-out';
            observer.observe(el);
        });