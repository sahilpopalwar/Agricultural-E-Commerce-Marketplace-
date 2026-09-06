// Page Navigation
function showPage(pageId) {
    // Hide all pages
    document.querySelectorAll('.page-content').forEach(page => {
        page.classList.remove('active');
    });
    // Show the selected page
    document.getElementById(pageId).classList.add('active');
    // Update active nav link
    document.querySelectorAll('nav ul li a').forEach(link => {
        link.classList.remove('active');
    });
    // Set contact as active if not profile/orders/wishlist/settings
    if (!['profile-page', 'orders-page', 'wishlist-page', 'settings-page'].includes(pageId)) {
        document.querySelector('nav ul li a[href="/usercontact"]').classList.add('active');
    }
    // Scroll to top
    window.scrollTo(0, 0);
}
// Contact Form Submission
document.getElementById('contactForm').addEventListener('submit', function (e) {
    e.preventDefault();
    // Get form values
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const message = document.getElementById('message').value;
    // Here you would typically send this data to a server
    console.log('Form submitted:', { name, email, message });
    // Show success message
    alert('Thank you for your message! We will get back to you soon.');
    // Reset form
    this.reset();
});
// Suggestion Submission
function submitSuggestion() {
    const suggestion = document.getElementById('suggestion-input').value;
    if (suggestion.trim() === '') {
        alert('Please enter your suggestion');
        return;
    }
    // Here you would typically send this data to a server
    console.log('Suggestion submitted:', suggestion);
    // Show success message
    alert('Thank you for your suggestion!');
    // Clear input
    document.getElementById('suggestion-input').value = '';
}
// Clear Wishlist
function clearWishlist() {
    if (confirm('Are you sure you want to clear your entire wishlist?')) {
        // Here you would typically send request to server
        document.querySelector('.wishlist-grid').innerHTML = '<p>Your wishlist is empty</p>';
        alert('Wishlist cleared successfully');
    }
}
// Profile dropdown toggle for mobile
document.querySelector('.profile-btn').addEventListener('click', (e) => {
    e.stopPropagation();
    const dropdown = document.getElementById('dropdownMenu');
    dropdown.style.display = dropdown.style.display === 'block' ? 'none' : 'block';
});
// Close dropdown when clicking outside
document.addEventListener('click', () => {
    const dropdown = document.getElementById('dropdownMenu');
    if (dropdown) {
        dropdown.style.display = 'none';
    }
});
// Initialize map (this would be more complex with Google Maps API)
function initMap() {
    console.log('Map would be initialized here with Google Maps API');
}
// Call initMap when the page loads
window.onload = initMap;
//# sourceMappingURL=usercontact.js.map