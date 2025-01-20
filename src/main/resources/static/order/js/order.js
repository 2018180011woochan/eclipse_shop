document.addEventListener('DOMContentLoaded', () => {
    const orderItemsContainer = document.getElementById('orderItemsContainer');
    const orderTotalAmount = document.getElementById('orderTotalAmount');
    const addressInput = document.getElementById('address');
    const paymentMethodSelect = document.getElementById('paymentMethod');
    const payButton = document.getElementById('payButton');
    const addressSearchButton = document.getElementById('addressSearchButton');

    // 장바구니 정보 로드
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

    // 주소 검색 버튼(카카오 API)
    addressSearchButton.addEventListener('click', () => {
        new daum.Postcode({
            oncomplete: function(data) {
                addressInput.value = data.address;
            }
        }).open();
    });

    // 결제하기 버튼 (IMP.request_pay → 성공 시 /api/order)
    payButton.addEventListener('click', () => {
        // 1) 장바구니와 주소 확인
        if (!cartItems || cartItems.length === 0) {
            alert("주문할 상품이 없습니다.");
            return;
        }
        const address = addressInput.value.trim();
        if (!address) {
            alert("주소를 입력하세요.");
            return;
        }

        // 2) 총 결제 금액 계산
        let totalPrice = 0;
        cartItems.forEach(item => {
            totalPrice += item.price * item.quantity;
        });

        // 3) 아임포트 결제창 파라미터
        const { IMP } = window;
        IMP.init("imp46747186");  // 또는 data-user-code 속성만으로도 OK

        const param = {
            pg: "kakaopay.TC0ONETIME",   // 카카오페이 테스트
            pay_method: "card",
            merchant_uid: "order_" + new Date().getTime(),
            name: "장바구니 결제",
            amount: totalPrice,
            buyer_email: "test@example.com",
            buyer_name: "홍길동",
            buyer_tel: "010-1234-5678",
            buyer_addr: address,
        };

        // 4) 결제창 호출
        IMP.request_pay(param, function(rsp) {
            if (rsp.success) {
                // 결제 완료 (아임포트 서버에서 '가상' 승인이 된 상태)
                alert("결제가 성공적으로 완료되었습니다.\n imp_uid: " + rsp.imp_uid);

                // (선택) 서버에 검증 로직을 추가하고 싶다면 여기서 먼저 /api/payments/verify 등
                // fetch -> portOneService.getPaymentData(rsp.imp_uid)...

                // 5) 결제 성공이 확인되었으므로 => /api/order 호출(주문 DB 저장)
                const orderRequest = {
                    memberId: 1,  // 예: 회원번호
                    address: address,
                    paymentMethod: "kakaopay", 
                    orderItems: cartItems.map(item => ({
                        productId: item.productId,
                        productName: item.name,
                        color: item.color,
                        size: item.size,
                        quantity: item.quantity,
                        unitPrice: item.price
                    })),
                    // 필요하다면 impUid, merchantUid도 함께
                    impUid: rsp.imp_uid,
                    merchantUid: rsp.merchant_uid,
                };

                fetch('/api/order', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(orderRequest)
                })
                .then(res => res.json())
                .then(data => {
                    alert(`주문이 완료되었습니다! DB orderId: ${data.orderId}`);
                    // 장바구니 비우고 페이지 이동
                    localStorage.removeItem('cartItems');
                    location.href = '/';
                })
                .catch(err => {
                    console.error(err);
                    alert("주문 저장 중 오류가 발생했습니다.");
                });

            } else {
                // 결제 실패 or 취소
                alert("결제가 취소되었거나 실패했습니다.\n" + rsp.error_msg);
            }
        });
    });

    renderOrderItems();
});
