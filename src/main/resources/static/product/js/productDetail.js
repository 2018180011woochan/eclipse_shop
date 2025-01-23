document.addEventListener('DOMContentLoaded', () => {
    const addToCartBtn = document.getElementById('addToCartBtn');
    const optionSelect = document.getElementById('optionSelect');
    const selectedOptionsContainer = document.getElementById('selectedOptionsContainer');

    // 상품 ID 및 기본 정보
    const productContainer = document.getElementById('productContainer');
    const productId = parseInt(productContainer.dataset.productId, 10); // data-product-id에서 추출
    const productName = productContainer.querySelector('h2').textContent.trim();
    const productPrice = parseInt(productContainer.querySelector('h4').textContent.replace('₩', '').trim());
    const thumbnailUrl = document.querySelector('.carousel-item.active img').src;

	// 문의
	const inquiryBtn = document.getElementById('inquiryBtn');
	
    let selectedOptions = [];

    optionSelect.addEventListener('change', () => {
        const selectedOptionId = optionSelect.value;
        const selectedText = optionSelect.options[optionSelect.selectedIndex].text;

        if (!selectedOptionId || selectedOptions.some(opt => opt.optionId === selectedOptionId)) {
            alert('이미 선택된 옵션이거나 옵션을 선택하지 않았습니다.');
            return;
        }

        selectedOptions.push({
            optionId: parseInt(selectedOptionId, 10),
            text: selectedText,
            quantity: 1
        });

        renderSelectedOptions();
    });

    function renderSelectedOptions() {
        selectedOptionsContainer.innerHTML = '';
        selectedOptions.forEach((option, index) => {
            const optionDiv = document.createElement('div');
            optionDiv.className = 'selected-option d-flex align-items-center mb-2';

            optionDiv.innerHTML = `
                <span class="me-3">${option.text}</span>
                <button class="btn btn-sm btn-outline-secondary me-2" data-index="${index}" data-action="decrease">-</button>
                <span class="me-2">${option.quantity}</span>
                <button class="btn btn-sm btn-outline-secondary me-2" data-index="${index}" data-action="increase">+</button>
                <button class="btn btn-sm btn-outline-danger" data-index="${index}" data-action="delete">X</button>
            `;

            selectedOptionsContainer.appendChild(optionDiv);
        });

        selectedOptionsContainer.addEventListener('click', handleOptionActions);
    }

    function handleOptionActions(event) {
        const index = parseInt(event.target.dataset.index, 10);
        const action = event.target.dataset.action;

        if (action === 'increase') {
            selectedOptions[index].quantity++;
        } else if (action === 'decrease') {
            if (selectedOptions[index].quantity > 1) {
                selectedOptions[index].quantity--;
            } else {
                alert('최소 수량은 1개입니다.');
            }
        } else if (action === 'delete') {
            selectedOptions.splice(index, 1);
        }

        renderSelectedOptions();
    }

	inquiryBtn.addEventListener('click', () => {
	    location.href = `/inquiry?productId=` + productId;
	});
	
    addToCartBtn.addEventListener('click', () => {
        if (selectedOptions.length === 0) {
            alert('옵션을 선택해주세요.');
            return;
        }

        let cartItems = JSON.parse(localStorage.getItem('cartItems')) || [];

        selectedOptions.forEach(option => {
            const existingItem = cartItems.find(item => item.productId === productId && item.optionId === option.optionId);

            if (existingItem) {
                existingItem.quantity += option.quantity;
            } else {
                cartItems.push({
                    productId: productId,
                    name: productName,
                    price: productPrice,
                    thumbnailUrl: thumbnailUrl,
                    optionId: option.optionId,
                    color: option.text.split('/')[0].trim(),
                    size: option.text.split('/')[1].trim(),
                    quantity: option.quantity
                });
            }
        });

        localStorage.setItem('cartItems', JSON.stringify(cartItems));
        selectedOptions = [];
        renderSelectedOptions();

        alert('장바구니에 담겼습니다!');
    });
});
