document.addEventListener('DOMContentLoaded', () => {
    const cartItemsContainer = document.getElementById('cartItemsContainer');
    const totalItemsElement = document.getElementById('totalItems');
    const totalAmountElement = document.getElementById('totalAmount');
    const finalAmountElement = document.getElementById('finalAmount');

    const cartItems = JSON.parse(localStorage.getItem('cartItems')) || [];

    function renderCartItems() {
        cartItemsContainer.innerHTML = '';
        let totalItems = 0;
        let totalAmount = 0;

        cartItems.forEach((item, index) => {
            totalItems += item.quantity;
            totalAmount += item.quantity * item.price;

            const cartItemDiv = document.createElement('div');
            cartItemDiv.className = 'cart-item';

            cartItemDiv.innerHTML = `
                <img src="${item.thumbnailUrl}" alt="${item.name}" style="width: 100px; height: 100px; object-fit: cover;">
                <div class="cart-item-details">
                    <h5>${item.name}</h5>
                    <p>${item.color} / ${item.size}</p>
                    <p>${item.price.toLocaleString()}원</p>
                </div>
                <div class="cart-item-actions">
                    <button class="btn btn-sm btn-outline-secondary" data-index="${index}" data-action="decrease">-</button>
                    <span>${item.quantity}</span>
                    <button class="btn btn-sm btn-outline-secondary" data-index="${index}" data-action="increase">+</button>
                    <button class="btn btn-sm btn-outline-danger" data-index="${index}" data-action="delete">삭제</button>
                </div>
            `;

            cartItemsContainer.appendChild(cartItemDiv);
        });

        totalItemsElement.textContent = totalItems;
        totalAmountElement.textContent = totalAmount.toLocaleString();
        finalAmountElement.textContent = totalAmount.toLocaleString();
    }

    cartItemsContainer.addEventListener('click', (event) => {
        const index = event.target.dataset.index;
        const action = event.target.dataset.action;

        if (!index || !action) return;

        if (action === 'increase') {
            cartItems[index].quantity++;
        } else if (action === 'decrease') {
            if (cartItems[index].quantity > 1) {
                cartItems[index].quantity--;
            } else {
                alert('최소 수량은 1개입니다.');
            }
        } else if (action === 'delete') {
            cartItems.splice(index, 1);
        }

        localStorage.setItem('cartItems', JSON.stringify(cartItems));
        renderCartItems();
    });
	
	orderButton.addEventListener('click', () => {
	    location.href = '/order';
	});

    renderCartItems();
});
