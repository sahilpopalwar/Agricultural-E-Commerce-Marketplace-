        const products = [
           
        ];

        // Sample product data for tracking
        const trackingProducts = [
            { id: 1, name: "Organic Apples", category: "fruits", price: 2.99, image: "https://images.unsplash.com/photo-1568702846914-96b305d2aaeb?w=200" },
            { id: 2, name: "Fresh Carrots", category: "vegetables", price: 1.49, image: "https://images.unsplash.com/photo-1447175008436-054170c2e979?w=200" },
            { id: 3, name: "Free Range Eggs", category: "dairy", price: 3.99, image: "https://images.unsplash.com/photo-1587486913049-53fc88980bea?w=200" },
            { id: 4, name: "Whole Grain Bread", category: "bakery", price: 2.49, image: "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=200" },
            { id: 5, name: "Grass-Fed Beef", category: "meat", price: 8.99, image: "https://images.unsplash.com/photo-1546054454-aa26e2b734c7?w=200" },
            { id: 6, name: "Organic Bananas", category: "fruits", price: 0.69, image: "https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=200" },
            { id: 7, name: "Spinach", category: "vegetables", price: 1.99, image: "https://images.unsplash.com/photo-1576045057995-568f588f82fb?w=200" },
            { id: 8, name: "Greek Yogurt", category: "dairy", price: 3.49, image: "https://images.unsplash.com/photo-1550583724-b2692b85b150?w=200" }
        ];

        // Simulated user activity data
        const userActivity = [
            { type: "view", productId: 1, date: "2023-06-15", count: 3 },
            { type: "purchase", productId: 1, date: "2023-06-10", count: 2 },
            { type: "view", productId: 2, date: "2023-06-14", count: 5 },
            { type: "purchase", productId: 2, date: "2023-06-05", count: 1 },
            { type: "view", productId: 3, date: "2023-06-12", count: 2 },
            { type: "purchase", productId: 3, date: "2023-05-28", count: 1 },
            { type: "view", productId: 4, date: "2023-06-08", count: 1 },
            { type: "search", query: "organic fruits", date: "2023-06-13" },
            { type: "wishlist", productId: 5, date: "2023-06-01" },
            { type: "view", productId: 6, date: "2023-06-11", count: 4 },
            { type: "purchase", productId: 6, date: "2023-06-03", count: 3 },
            { type: "view", productId: 7, date: "2023-06-09", count: 2 },
            { type: "search", query: "healthy vegetables", date: "2023-06-07" },
            { type: "view", productId: 8, date: "2023-06-06", count: 1 }
        ];

        // Wishlist data
        let wishlist = [
            { id: 1, name: "Organic Apples", price: 2.99, image: "https://images.unsplash.com/photo-1568702846914-96b305d2aaeb?w=200" },
            { id: 4, name: "Whole Grain Bread", price: 2.49, image: "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=200" },
            { id: 7, name: "Spinach", price: 1.99, image: "https://images.unsplash.com/photo-1576045057995-568f588f82fb?w=200" }
        ];

        // Initialize Google Map
        function initMap() {
            const map = new google.maps.Map(document.getElementById("map"), {
                center: { lat: 19.0760, lng: 72.8777 }, // Mumbai coordinates
                zoom: 12,
                styles: [
                    {
                        "featureType": "all",
                        "elementType": "labels.text.fill",
                        "stylers": [
                            {
                                "saturation": 36
                            },
                            {
                                "color": "#333333"
                            },
                            {
                                "lightness": 40
                            }
                        ]
                    },
                    {
                        "featureType": "all",
                        "elementType": "labels.text.stroke",
                        "stylers": [
                            {
                                "visibility": "on"
                            },
                            {
                                "color": "#ffffff"
                            },
                            {
                                "lightness": 16
                            }
                        ]
                    },
                    {
                        "featureType": "all",
                        "elementType": "labels.icon",
                        "stylers": [
                            {
                                "visibility": "off"
                            }
                        ]
                    },
                    {
                        "featureType": "administrative",
                        "elementType": "geometry.fill",
                        "stylers": [
                            {
                                "color": "#fefefe"
                            },
                            {
                                "lightness": 20
                            }
                        ]
                    },
                    {
                        "featureType": "administrative",
                        "elementType": "geometry.stroke",
                        "stylers": [
                            {
                                "color": "#fefefe"
                            },
                            {
                                "lightness": 17
                            },
                            {
                                "weight": 1.2
                            }
                        ]
                    },
                    {
                        "featureType": "landscape",
                        "elementType": "geometry",
                        "stylers": [
                            {
                                "color": "#f5f5f5"
                            },
                            {
                                "lightness": 20
                            }
                        ]
                    },
                    {
                        "featureType": "poi",
                        "elementType": "geometry",
                        "stylers": [
                            {
                                "color": "#f5f5f5"
                            },
                            {
                                "lightness": 21
                            }
                        ]
                    },
                    {
                        "featureType": "road.highway",
                        "elementType": "geometry.fill",
                        "stylers": [
                            {
                                "color": "#ffffff"
                            },
                            {
                                "lightness": 17
                            }
                        ]
                    },
                    {
                        "featureType": "road.highway",
                        "elementType": "geometry.stroke",
                        "stylers": [
                            {
                                "color": "#ffffff"
                            },
                            {
                                "lightness": 29
                            },
                            {
                                "weight": 0.2
                            }
                        ]
                    },
                    {
                        "featureType": "road.arterial",
                        "elementType": "geometry",
                        "stylers": [
                            {
                                "color": "#ffffff"
                            },
                            {
                                "lightness": 18
                            }
                        ]
                    },
                    {
                        "featureType": "road.local",
                        "elementType": "geometry",
                        "stylers": [
                            {
                                "color": "#ffffff"
                            },
                            {
                                "lightness": 16
                            }
                        ]
                    },
                    {
                        "featureType": "transit",
                        "elementType": "geometry",
                        "stylers": [
                            {
                                "color": "#f2f2f2"
                            },
                            {
                                "lightness": 19
                            }
                        ]
                    },
                    {
                        "featureType": "water",
                        "elementType": "geometry",
                        "stylers": [
                            {
                                "color": "#e9e9e9"
                            },
                            {
                                "lightness": 17
                            }
                        ]
                    }
                ]
            });

            // Add delivery route
            const deliveryRoute = new google.maps.Polyline({
                path: [
                    { lat: 19.0760, lng: 72.8777 }, // Starting point (Mumbai)
                    { lat: 19.0765, lng: 72.8782 },
                    { lat: 19.0770, lng: 72.8787 },
                    { lat: 19.0775, lng: 72.8792 }, // Current location (simulated)
                    { lat: 19.0780, lng: 72.8797 }, // Destination
                ],
                geodesic: true,
                strokeColor: "#FF9900",
                strokeOpacity: 1.0,
                strokeWeight: 4,
            });

            deliveryRoute.setMap(map);

            // Add markers
            const startMarker = new google.maps.Marker({
                position: { lat: 19.0760, lng: 72.8777 },
                map: map,
                title: "Warehouse",
                icon: {
                    url: "https://maps.google.com/mapfiles/ms/icons/red-dot.png"
                }
            });

            const currentMarker = new google.maps.Marker({
                position: { lat: 19.0775, lng: 72.8792 },
                map: map,
                title: "Delivery in progress",
                icon: {
                    url: "https://maps.google.com/mapfiles/ms/icons/blue-dot.png"
                }
            });

            const endMarker = new google.maps.Marker({
                position: { lat: 19.0780, lng: 72.8797 },
                map: map,
                title: "Your Location",
                icon: {
                    url: "https://maps.google.com/mapfiles/ms/icons/green-dot.png"
                }
            });
        }

        // Profile dropdown toggle
        document.querySelector('.profile-btn').addEventListener('click', function(e) {
            e.stopPropagation();
            document.getElementById('dropdownMenu').classList.toggle('show');
        });

        // Close dropdown when clicking outside
        window.addEventListener('click', function() {
            const dropdowns = document.getElementsByClassName("dropdown-menu");
            for (let i = 0; i < dropdowns.length; i++) {
                const openDropdown = dropdowns[i];
                if (openDropdown.classList.contains('show')) {
                    openDropdown.classList.remove('show');
                }
            }
        });

        // Page navigation
        function showPage(pageId) {
            document.querySelectorAll('.page-content').forEach(page => {
                page.classList.remove('active');
            });
            document.getElementById(pageId).classList.add('active');
            
            // If showing products page, display all products
            if (pageId === 'products-page') {
                displayAllProducts();
            }
            
            // If showing tracking page, generate recommendations
            if (pageId === 'tracking-page') {
                generateRecommendations();
            }
            
            // If showing wishlist page, display wishlist items
            if (pageId === 'wishlist-page') {
                displayWishlist();
            }
            
            // Close dropdown if open
            document.getElementById('dropdownMenu').classList.remove('show');
            
            // Scroll to top
            window.scrollTo(0, 0);
        }

        function displayProducts() {
            const productList = document.getElementById("product-list");
            productList.innerHTML = "";
            
            // Display only 3 products on home page
            const homeProducts = products.slice(0, 3);
            
            homeProducts.forEach(product => {
                const productElement = createProductElement(product);
                productList.innerHTML += productElement;
            });
        }

        function displayAllProducts() {
            const productList = document.getElementById("product-list-full");
            productList.innerHTML = "";
            
            products.forEach(product => {
                const productElement = createProductElement(product);
                productList.innerHTML += productElement;
            });
        }

        function createProductElement(product) {
            return `
                <div class='product'>
                    <i class="fas fa-heart like-icon ${product.liked ? 'liked' : ''}" 
                       onclick="toggleLike(${product.id})"></i>
                    <img src='${product.image}' alt='${product.name}'>
                    <h3>${product.name}</h3>
                    <p>${product.description}</p>
                    <p class="price">$${product.price}</p>
                    <div class="button-group">
                        <button class="add-to-cart" onclick="addToCart('${product.name}', ${product.price})">
                            Add to Cart
                        </button>
                        <button class="buy-now-icon" onclick="buyNow('${product.name}', ${product.price})">
                            <i class="fas fa-shopping-cart"></i>
                        </button>
                    </div>
                </div>
            `;
        }

        function searchProducts() {
            const searchValue = document.getElementById("search").value.toLowerCase();
            const filteredProducts = products.filter(product => 
                product.name.toLowerCase().includes(searchValue) ||
                product.description.toLowerCase().includes(searchValue)
            );
            
            const productList = document.getElementById("product-list");
            productList.innerHTML = "";
            
            filteredProducts.forEach(product => {
                const productElement = createProductElement(product);
                productList.innerHTML += productElement;
            });
        }

        function addToCart(productName, price) {
            alert(`${productName} added to cart! Price: $${price}`);
            // In a real app, you would add to a cart array or make an API call
        }

        function buyNow(productName, price) {
            alert(`Proceeding to checkout for ${productName}! Price: $${price}`);
            // In a real app, you would redirect to checkout page
        }

        function toggleLike(productId) {
            const product = products.find(p => p.id === productId);
            if (product) {
                product.liked = !product.liked;
                
                // Update display on both product lists
                displayProducts();
                if (document.getElementById("products-page").classList.contains('active')) {
                    displayAllProducts();
                }
            }
        }

        // Function to render products in a grid for tracking
        function renderProducts(containerId, productIds) {
            const container = document.getElementById(containerId);
            container.innerHTML = '';
            
            productIds.forEach(productId => {
                const product = trackingProducts.find(p => p.id === productId);
                if (product) {
                    const productCard = document.createElement('div');
                    productCard.className = 'product-card';
                    const image = document.createElement('img');
                    image.src = product.image;
                    image.alt = product.name;
                    image.className = 'product-img';
                    const name = document.createElement('div');
                    name.className = 'product-name';
                    name.textContent = product.name;
                    const price = document.createElement('div');
                    price.className = 'product-price';
                    price.textContent = `$${product.price.toFixed(2)}`;
                    productCard.append(image, name, price);
                    container.appendChild(productCard);
                }
            });
        }

        // Function to render recent activity
        function renderRecentActivity() {
            const container = document.getElementById('recent-activity');
            container.innerHTML = '';
            
            // Sort activities by date (newest first)
            const sortedActivities = [...userActivity].sort((a, b) => new Date(b.date) - new Date(a.date));
            
            // Display only the 5 most recent activities
            sortedActivities.slice(0, 5).forEach(activity => {
                const activityItem = document.createElement('div');
                activityItem.className = 'activity-item';
                
                let activityText = '';
                let icon = '';
                
                switch(activity.type) {
                    case 'view':
                        activityText = `Viewed ${trackingProducts.find(p => p.id === activity.productId)?.name || 'product'} ${activity.count} times`;
                        icon = '<i class="fas fa-eye"></i>';
                        break;
                    case 'purchase':
                        activityText = `Purchased ${trackingProducts.find(p => p.id === activity.productId)?.name || 'product'} (${activity.count} items)`;
                        icon = '<i class="fas fa-shopping-cart"></i>';
                        break;
                    case 'search':
                        activityText = `Searched for "${activity.query}"`;
                        icon = '<i class="fas fa-search"></i>';
                        break;
                    case 'wishlist':
                        activityText = `Added ${trackingProducts.find(p => p.id === activity.productId)?.name || 'product'} to wishlist`;
                        icon = '<i class="fas fa-heart"></i>';
                        break;
                }
                
                const activityContent = document.createElement('div');
                activityContent.textContent = `${activityText} ${new Date(activity.date).toLocaleDateString()}`;
                activityItem.appendChild(activityContent);
                
                container.appendChild(activityItem);
            });
        }

        // Function to analyze user activity and generate recommendations
        function generateRecommendations() {
            // Get frequently viewed products (top 3)
            const viewedProducts = userActivity
                .filter(a => a.type === 'view')
                .reduce((acc, curr) => {
                    const existing = acc.find(item => item.productId === curr.productId);
                    if (existing) {
                        existing.count += curr.count;
                    } else {
                        acc.push({ ...curr });
                    }
                    return acc;
                }, [])
                .sort((a, b) => b.count - a.count)
                .slice(0, 3)
                .map(item => item.productId);
            
            // Get frequently purchased products (top 3)
            const purchasedProducts = userActivity
                .filter(a => a.type === 'purchase')
                .reduce((acc, curr) => {
                    const existing = acc.find(item => item.productId === curr.productId);
                    if (existing) {
                        existing.count += curr.count;
                    } else {
                        acc.push({ ...curr });
                    }
                    return acc;
                }, [])
                .sort((a, b) => b.count - a.count)
                .slice(0, 3)
                .map(item => item.productId);
            
            // Generate recommendations based on categories of frequently viewed/purchased items
            const preferredCategories = [];
            
            viewedProducts.forEach(productId => {
                const product = trackingProducts.find(p => p.id === productId);
                if (product && !preferredCategories.includes(product.category)) {
                    preferredCategories.push(product.category);
                }
            });
            
            purchasedProducts.forEach(productId => {
                const product = trackingProducts.find(p => p.id === productId);
                if (product && !preferredCategories.includes(product.category)) {
                    preferredCategories.push(product.category);
                }
            });
            
            const recommendedProducts = trackingProducts
                .filter(p => preferredCategories.includes(p.category))
                .filter(p => !viewedProducts.includes(p.id) && !purchasedProducts.includes(p.id))
                .slice(0, 4)
                .map(p => p.id);
            
            // Render all sections
            renderProducts('recently-viewed', viewedProducts);
            renderProducts('frequently-purchased', purchasedProducts);
            renderProducts('recommendations', recommendedProducts);
            renderRecentActivity();
        }

        // Wishlist functions
        function displayWishlist() {
            const wishlistContainer = document.getElementById('wishlist-items');
            wishlistContainer.innerHTML = '';
            
            if (wishlist.length === 0) {
                wishlistContainer.innerHTML = '<p style="grid-column: 1/-1; text-align: center;">Your wishlist is empty</p>';
                return;
            }
            
            wishlist.forEach(item => {
                const product = trackingProducts.find(p => p.id === item.id) || item;
                const wishlistItem = document.createElement('div');
                wishlistItem.className = 'wishlist-item';
                const image = document.createElement('img');
                image.src = product.image;
                image.alt = product.name;
                image.className = 'wishlist-img';
                const details = document.createElement('div');
                details.className = 'wishlist-details';
                const title = document.createElement('h4');
                title.textContent = product.name;
                const price = document.createElement('div');
                price.className = 'price';
                price.textContent = `$${product.price.toFixed(2)}`;
                details.append(title, price);
                const actions = document.createElement('div');
                actions.className = 'wishlist-actions';
                const addButton = document.createElement('button');
                addButton.className = 'btn btn-primary';
                addButton.textContent = 'Add to Cart';
                addButton.addEventListener('click', () => addToCart(product.name, product.price));
                const removeButton = document.createElement('button');
                removeButton.className = 'btn btn-danger';
                removeButton.textContent = 'Remove';
                removeButton.addEventListener('click', () => removeFromWishlist(product.id));
                actions.append(addButton, removeButton);
                wishlistItem.append(image, details, actions);
                wishlistContainer.appendChild(wishlistItem);
            });
        }

        function removeFromWishlist(productId) {
            wishlist = wishlist.filter(item => item.id !== productId);
            displayWishlist();
        }

        function clearWishlist() {
            if (confirm('Are you sure you want to clear your wishlist?')) {
                wishlist = [];
                displayWishlist();
            }
        }

        // Initialize the page
        window.onload = function() {
            displayProducts();
            showPage('home-page');
            
            // Filter functionality for tracking
            document.getElementById('apply-filters').addEventListener('click', function() {
                const timePeriod = parseInt(document.getElementById('time-period').value);
                const activityType = document.getElementById('activity-type').value;
                const category = document.getElementById('category').value;
                
                // In a real application, this would filter the data from the server
                alert(`Filters applied:\nTime Period: Last ${timePeriod} days\nActivity Type: ${activityType}\nCategory: ${category}`);
            });
        };