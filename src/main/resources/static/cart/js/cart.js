document.addEventListener('DOMContentLoaded', () => {
    const cartItemsContainer = document.getElementById('cartItemsContainer');
    const totalItemsElement = document.getElementById('totalItems');
    const totalAmountElement = document.getElementById('totalAmount');
    const finalAmountElement = document.getElementById('finalAmount');
    const discountAmountElement = document.getElementById('discountAmount');
    const orderButton = document.getElementById('orderButton');

    const cartItems = JSON.parse(localStorage.getItem('cartItems')) || [];

    // 배송비 기본값 설정
    const shippingFee = 3000;

    function applyMembershipDiscount(price) {
		console.log("할인 적용 전 price:", price);
		console.log("현재 membership:", membership);
		console.log("membership === 'GOLD' ?", membership === "GOLD");
		if (membership === "GOLD") {
		    console.log("GOLD 멤버십으로 인식했습니다!");
		}
		
		console.log("membership 문자열 길이:", membership.length);
		for (let i = 0; i < membership.length; i++) {
		  console.log(
		    `인덱스 ${i}: 문자='${membership[i]}' / 코드=${membership.charCodeAt(i)}`
		  );
		}
		
        let discountedPrice = price;
        
        if (membership === "GOLD") {
            discountedPrice = Math.round(price * 0.95 / 10) * 10;  // 5% 할인 후 10단위 반올림
        } else if (membership === "DIAMOND") {
            discountedPrice = Math.round(price * 0.90 / 10) * 10;  // 10% 할인 후 10단위 반올림
        }

		console.log("할인 적용 후 price:", discountedPrice);
        return discountedPrice;
    }

	function renderCartItems() {
	    cartItemsContainer.innerHTML = '';
	    let totalItems = 0;
	    let totalAmount = 0;
	    let discountedTotalAmount = 0;

	    cartItems.forEach((item, index) => {
	        totalItems += item.quantity;
	        totalAmount += item.quantity * item.price;

	        // 멤버십 할인 가격 적용
	        const discountedPrice = applyMembershipDiscount(item.price);
	        const subtotal = discountedPrice * item.quantity;
	        discountedTotalAmount += subtotal;

	        // 할인 적용 여부 확인
	        const isDiscounted = discountedPrice < item.price;
			const originalPriceDisplay = isDiscounted 
			    ? `<span style="text-decoration: line-through;">₩ ${item.price.toLocaleString()}</span>` 
			    : "";
			const discountedPriceDisplay = isDiscounted 
			    ? `<span style="color: red; font-weight: bold;"> → ₩ ${discountedPrice.toLocaleString()}</span>` 
			    : `<span>₩ ${item.price.toLocaleString()}</span>`;

	        const cartItemDiv = document.createElement('div');
	        cartItemDiv.className = 'cart-item';

	        cartItemDiv.innerHTML = `
	            <img src="${item.thumbnailUrl}" alt="${item.name}" style="width: 100px; height: 100px; object-fit: cover;">
	            <div class="cart-item-details">
	                <h5>${item.name}</h5>
	                <p>${item.color} / ${item.size}</p>
	                <p>${originalPriceDisplay} ${discountedPriceDisplay}</p>
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

	    const totalDiscount = totalAmount - discountedTotalAmount;
	    const finalAmount = discountedTotalAmount + shippingFee;

	    totalItemsElement.textContent = totalItems;
	    totalAmountElement.textContent = totalAmount.toLocaleString();
	    discountAmountElement.textContent = totalDiscount > 0 ? totalDiscount.toLocaleString() : '0';
	    finalAmountElement.textContent = finalAmount.toLocaleString();

	    console.log("총 원가:", totalAmount);
	    console.log("할인된 가격:", discountedTotalAmount);
	    console.log("할인된 금액:", totalDiscount);
	    console.log("최종 결제 금액:", finalAmount);
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
