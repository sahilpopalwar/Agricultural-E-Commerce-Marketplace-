document.addEventListener('DOMContentLoaded', function () {
    const header = document.querySelector('header');
    const contactForm = document.getElementById('contactForm');
    const suggestionInput = document.getElementById('suggestion-input');

    window.addEventListener('scroll', function () {
        if (header) {
            header.classList.toggle('scrolled', window.scrollY > 50);
        }
    });

    if (contactForm) {
        contactForm.addEventListener('submit', function (event) {
            event.preventDefault();
            alert('Thank you for your message! We will get back to you soon.');
            contactForm.reset();
        });
    }

    window.submitSuggestion = function () {
        if (!suggestionInput) return;
        const suggestion = suggestionInput.value.trim();
        if (!suggestion) {
            alert('Please enter a suggestion before submitting.');
            return;
        }
        alert('Thank you for your suggestion: ' + suggestion);
        suggestionInput.value = '';
    };
});
