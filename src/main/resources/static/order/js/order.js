document.addEventListener('DOMContentLoaded', () => {
    const orderItemsContainer = document.getElementById('orderItemsContainer');
    const orderTotalAmount = document.getElementById('orderTotalAmount');
    const addressInput = document.getElementById('address');
    const paymentMethodSelect = document.getElementById('paymentMethod');
    const payButton = document.getElementById('payButton');
    const addressSearchButton = document.getElementById('addressSearchButton');

    // 1. 장바구니 정보 로드
    const cartItems = JSON.parse(localStorage.getItem('cartItems')) || [];

    function renderOrderItems() {
        orderItemsContainer.innerHTML = '';
        let totalPrice = 0;

        cartItems.forEach(item => {
            const itemTotalPrice = item.price * item.quantity;
            totalPrice += itemTotalPrice;

            const itemDiv = document.createElement('div');
            itemDiv.classList.add('order-item', 'mb-2');
            itemDiv.innerHTML = `
                <div class="d-flex">
                    <img src="${item.thumbnailUrl}" alt="${item.name}"
                         style="width:80px;height:80px;object-fit:cover;margin-right:10px;">
                    <div>
                        <h6>${item.name}</h6>
                        <p>${item.color} / ${item.size} | 수량: ${item.quantity}</p>
                        <p>${(itemTotalPrice).toLocaleString()} 원</p>
                    </div>
                </div>
            `;
            orderItemsContainer.appendChild(itemDiv);
        });

        orderTotalAmount.textContent = totalPrice.toLocaleString();
    }

    // 2. 결제하기 버튼
    payButton.addEventListener('click', () => {
        if (!cartItems || cartItems.length === 0) {
            alert("주문할 상품이 없습니다.");
            return;
        }

        const address = addressInput.value.trim();
        if (!address) {
            alert("주소를 입력하세요.");
            return;
        }

        const paymentMethod = paymentMethodSelect.value;

        // API 전송할 데이터 (OrderRequestDto 형태)
        const orderRequest = {
            memberId: 1, // 예: 로그인 회원이라면 실제 memberId를 넣어야 함
            address: address,
            paymentMethod: paymentMethod,
            orderItems: cartItems.map(item => ({
                productId: item.productId,
                productName: item.name,
                color: item.color,
                size: item.size,
                quantity: item.quantity,
                unitPrice: item.price
            }))
        };

        // /api/order 로 POST
        fetch('/api/order', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(orderRequest)
        })
        .then(res => {
            if (!res.ok) {
                throw new Error('주문 요청 실패');
            }
            return res.json();
        })
        .then(data => {
            alert(`주문이 완료되었습니다! 주문번호: ${data.orderId}`);
            // 주문 완료 시 장바구니 비우고 메인이나 주문 완료 페이지로 이동
            localStorage.removeItem('cartItems');
            location.href = '/';
        })
        .catch(err => {
            console.error(err);
            alert("주문 중 오류가 발생했습니다.");
        });
    });

    // 3. 주소 검색 버튼 (카카오 주소 API 예시)
    addressSearchButton.addEventListener('click', () => {
        new daum.Postcode({
            oncomplete: function(data) {
                addressInput.value = data.address;
            }
        }).open();
    });

    renderOrderItems();
});
