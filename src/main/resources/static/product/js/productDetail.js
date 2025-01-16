document.addEventListener('DOMContentLoaded', () => {
    const addToCartBtn = document.getElementById('addToCartBtn');
    const optionSelect = document.getElementById('optionSelect');
    const selectedOptionsContainer = document.getElementById('selectedOptionsContainer');

    // 현재 선택된 옵션들
    let selectedOptions = [];

    // 상품 ID (Thymeleaf 문법 예시)
    const productId = /*[[${product.productId}]]*/ 0;

    // 옵션 선택 시 추가 버튼
    optionSelect.addEventListener('change', () => {
        const selectedOptionId = optionSelect.value;
        const selectedText = optionSelect.options[optionSelect.selectedIndex].text;

        // 옵션이 선택되지 않았거나 이미 추가된 옵션이면 리턴
        if (!selectedOptionId || selectedOptions.some(opt => opt.optionId === selectedOptionId)) {
            alert('이미 선택된 옵션이거나 옵션을 선택하지 않았습니다.');
            return;
        }

        // 선택된 옵션 추가
        selectedOptions.push({
            optionId: selectedOptionId,
            text: selectedText,
            quantity: 1
        });

        // 화면에 선택된 옵션 표시
        renderSelectedOptions();
    });

    // 선택된 옵션 표시
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

        // 이벤트 추가
        selectedOptionsContainer.addEventListener('click', handleOptionActions);
    }

    // 옵션 수량 조절 및 삭제
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

    // 장바구니 담기
    addToCartBtn.addEventListener('click', () => {
        if (selectedOptions.length === 0) {
            alert('옵션을 선택해주세요.');
            return;
        }

        let cartItems = JSON.parse(localStorage.getItem('cartItems')) || [];

        selectedOptions.forEach(option => {
            const existingItem = cartItems.find(item => item.productId === productId && item.optionId === option.optionId);

            if (existingItem) {
                // 이미 장바구니에 있는 경우 수량 업데이트
                existingItem.quantity += option.quantity;
            } else {
                // 새로운 항목 추가
                cartItems.push({
                    productId: productId,
                    optionId: option.optionId,
                    quantity: option.quantity
                });
            }
        });

        // 로컬 스토리지 저장
        localStorage.setItem('cartItems', JSON.stringify(cartItems));

        // 초기화
        selectedOptions = [];
        renderSelectedOptions();

        alert('장바구니에 담겼습니다!');
    });
});
