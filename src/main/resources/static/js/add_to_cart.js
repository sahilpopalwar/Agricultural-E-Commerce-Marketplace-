let cart = [];
function addToCart(name, price) {
    cart.push({ name, price });
    updateCart();
}
function updateCart() {
    const cartItems = document.getElementById('cart-items');
    const totalElement = document.getElementById('total');
    cartItems.innerHTML = '';
    let total = 0;
    cart.forEach(item => {
        const li = document.createElement('li');
        li.textContent = `${item.name} - $${item.price}`;
        cartItems.appendChild(li);
        total += item.price;
    });
    totalElement.textContent = total;
}
//# sourceMappingURL=add_to_cart.js.map