        document.addEventListener('DOMContentLoaded', function() {
            // Cart functionality
            let cart = JSON.parse(localStorage.getItem('cart')) || [];
            const cartCount = document.getElementById('cart-count');
            const emptyCartMessage = document.getElementById('empty-cart-message');
            const orderSummary = document.getElementById('order-summary');
            const orderItems = document.getElementById('order-items');
            const orderTotal = document.getElementById('order-total');
            const checkoutForm = document.getElementById('checkout-form');
            
            // Update cart count
            function updateCartCount() {
                const count = cart.reduce((total, item) => total + item.quantity, 0);
                cartCount.textContent = count;
            }
            
            // Display cart items
            function displayCartItems() {
                if (cart.length === 0) {
                    emptyCartMessage.style.display = 'block';
                    orderSummary.style.display = 'none';
                    checkoutForm.style.display = 'none';
                } else {
                    emptyCartMessage.style.display = 'none';
                    orderSummary.style.display = 'block';
                    checkoutForm.style.display = 'grid';
                    
                    // Clear previous items
                    orderItems.innerHTML = '';
                    
                    let total = 0;
                    
                    // Add each item to the order summary
                    cart.forEach(item => {
                        const itemElement = document.createElement('div');
                        itemElement.className = 'order-item';
                        const nameElement = document.createElement('span');
                        nameElement.textContent = `${item.name} (${item.quantity})`;
                        const priceElement = document.createElement('span');
                        priceElement.textContent = `$${(item.price * item.quantity).toFixed(2)}`;
                        itemElement.append(nameElement, priceElement);
                        orderItems.appendChild(itemElement);
                        
                        total += item.price * item.quantity;
                    });
                    
                    // Update total
                    orderTotal.textContent = `$${total.toFixed(2)}`;
                }
            }
            
            // Initialize cart display
            updateCartCount();
            displayCartItems();
            
            // Login popup functionality
            const profileImg = document.getElementById('profile-img');
            const loginPopup = document.getElementById('loginPopup');
            const closeLogin = document.getElementById('closeLogin');
            const forgotPasswordLink = document.getElementById('forgotPasswordLink');
            const recoveryPopup = document.getElementById('recoveryPopup');
            const closeRecovery = document.getElementById('closeRecovery');
            
            if (profileImg) {
                profileImg.addEventListener('click', function() {
                    loginPopup.style.display = 'flex';
                });
            }
            
            if (closeLogin) {
                closeLogin.addEventListener('click', function() {
                    loginPopup.style.display = 'none';
                });
            }
            
            if (forgotPasswordLink) {
                forgotPasswordLink.addEventListener('click', function(e) {
                    e.preventDefault();
                    loginPopup.style.display = 'none';
                    recoveryPopup.style.display = 'flex';
                });
            }
            
            if (closeRecovery) {
                closeRecovery.addEventListener('click', function() {
                    recoveryPopup.style.display = 'none';
                });
            }
            
            // Close popups when clicking outside
            window.addEventListener('click', function(e) {
                if (e.target === loginPopup) {
                    loginPopup.style.display = 'none';
                }
                if (e.target === recoveryPopup) {
                    recoveryPopup.style.display = 'none';
                }
            });
            
            // Checkout form submission
            const confirmationMessage = document.getElementById('confirmation-message');
            
            checkoutForm.addEventListener('submit', function(e) {
                e.preventDefault();
                
                // Validate form
                const name = document.getElementById('name').value;
                const email = document.getElementById('email').value;
                const address = document.getElementById('address').value;
                const payment = document.getElementById('payment').value;
                
                if (!name || !email || !address || !payment) {
                    alert('Please fill in all required fields');
                    return;
                }
                
                // In a real application, you would process the payment here
                // For demo purposes, we'll just show a confirmation message
                
                confirmationMessage.textContent = `Thank you for your order, ${name}! Your order has been placed successfully.`;
                confirmationMessage.style.display = 'block';
                
                // Clear the cart
                cart = [];
                localStorage.setItem('cart', JSON.stringify(cart));
                updateCartCount();
                displayCartItems();
                
                // Clear the form
                checkoutForm.reset();
                
                // Scroll to the confirmation message
                confirmationMessage.scrollIntoView({ behavior: 'smooth' });
                
                // Hide the confirmation after 5 seconds
                setTimeout(function() {
                    confirmationMessage.style.display = 'none';
                }, 5000);
            });
            
            // Recovery form submission
            const recoveryForm = document.getElementById('recoveryForm');
            if (recoveryForm) {
                recoveryForm.addEventListener('submit', function(e) {
                    e.preventDefault();
                    const email = document.getElementById('recoveryEmail').value;
                    
                    // In a real app, you would send this to your backend
                    console.log('Password recovery requested for:', email);
                    
                    const recoveryMessage = document.getElementById('recoveryMessage');
                    recoveryMessage.textContent = `Password recovery link sent to ${email}`;
                    recoveryMessage.style.display = 'block';
                    recoveryMessage.style.backgroundColor = '#dff0d8';
                    recoveryMessage.style.color = '#3c763d';
                    
                    // Hide the message after 5 seconds
                    setTimeout(function() {
                        recoveryMessage.style.display = 'none';
                        recoveryPopup.style.display = 'none';
                    }, 5000);
                });
            }
            
            // Cart link functionality
            document.getElementById('cart-link').addEventListener('click', function(e) {
                e.preventDefault();
                if (cart.length > 0) {
                    // In a real app, you might want to show a cart popup or go to cart page
                    alert(`You have ${cart.reduce((total, item) => total + item.quantity, 0)} items in your cart`);
                } else {
                    alert('Your cart is empty');
                }
            });
        });