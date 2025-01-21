document.addEventListener('DOMContentLoaded', () => {
    // 주문 상품 목록
    const orderItemsContainer = document.getElementById('orderItemsContainer');
    const orderTotalAmount = document.getElementById('orderTotalAmount');

    // 사용자 정보
    const emailInput = document.getElementById('email');
    const nameInput = document.getElementById('name');
    const phoneInput = document.getElementById('phone');

    // 주소 관련
    const addressInput = document.getElementById('address');
    const addressSearchButton = document.getElementById('addressSearchButton');
    const basicAddressBtn = document.getElementById('basicAddressBtn');
    const newAddressBtn = document.getElementById('newAddressBtn');

    // 결제
    const paymentMethodSelect = document.getElementById('paymentMethod');
    const payButton = document.getElementById('payButton');

    let userData = null;

    // 1) 장바구니 로드
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

    renderOrderItems();

    // 2) 로그인 사용자 정보 가져오기
    fetch('/api/v1/members/me')
      .then(res => {
        if (!res.ok) throw new Error('로그인이 필요합니다.');
        return res.json();
      })
      .then(data => {
        userData = data;
        // userData = { email, name, phone, address, ... }

        emailInput.value = data.email;
        nameInput.value = data.name;
        phoneInput.value = data.phone;
        addressInput.value = data.address;
        
        // 시작 시에는 "기본 주소" 상태라고 가정 -> 검색 버튼 비활성화
        addressSearchButton.disabled = true;
      })
      .catch(err => {
        console.log(err);
        alert("로그인 정보가 없거나, 세션이 만료되었습니다.");
        location.href = "/login";
      });

    // 3) "기본 주소" 버튼: 기존 주소 세팅 & 검색 비활성화
    basicAddressBtn.addEventListener('click', () => {
      if (userData && userData.address) {
        addressInput.value = userData.address;
        addressSearchButton.disabled = true;  // 검색 버튼 비활성
      } else {
        alert("회원 정보에 기본 주소가 없습니다.");
      }
    });

    // 4) "새 주소" 버튼: 입력 칸 비우고 검색 버튼 활성화
    newAddressBtn.addEventListener('click', () => {
      addressInput.value = "";
      addressInput.focus();
      addressSearchButton.disabled = false; // 검색 버튼 활성
    });

    // 5) 주소 검색 버튼 (다음 우편번호 API)
    addressSearchButton.addEventListener('click', () => {
        new daum.Postcode({
            oncomplete: function(data) {
                addressInput.value = data.address;
            }
        }).open();
    });

    // 6) 결제하기
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

        let totalPrice = 0;
        cartItems.forEach(item => totalPrice += item.price * item.quantity);

        const { IMP } = window;
        IMP.init("imp46747186");

        const param = {
            pg: "kakaopay.TC0ONETIME",
            pay_method: "card",
            merchant_uid: "order_" + new Date().getTime(),
            name: "장바구니 결제",
            amount: totalPrice,
            buyer_email: emailInput.value,
            buyer_name: nameInput.value,
            buyer_tel: phoneInput.value,
            buyer_addr: address,
        };

        // 결제창 호출
        IMP.request_pay(param, function(rsp) {
            if (rsp.success) {
                alert("결제가 성공적으로 완료되었습니다.\n imp_uid: " + rsp.imp_uid);

                const orderRequest = {
                    memberId: 1, // 실제론 userData.id 등을 사용
                    address: address,
                    paymentMethod: paymentMethodSelect.value,
                    orderItems: cartItems.map(item => ({
                        productId: item.productId,
                        productName: item.name,
                        color: item.color,
                        size: item.size,
                        quantity: item.quantity,
                        unitPrice: item.price
                    })),
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
                    localStorage.removeItem('cartItems');
                    location.href = '/';
                })
                .catch(err => {
                    console.error(err);
                    alert("주문 저장 중 오류가 발생했습니다.");
                });
            } else {
                alert("결제가 취소되었거나 실패했습니다.\n" + rsp.error_msg);
            }
        });
    });
});
