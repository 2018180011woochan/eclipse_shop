document.addEventListener('DOMContentLoaded', () => {
    // --------------------------------------------------
    // 1) 화면 요소
    // --------------------------------------------------
    // 장바구니 상품 표시
    const orderItemsContainer = document.getElementById('orderItemsContainer');
    const orderTotalAmount = document.getElementById('orderTotalAmount');

    // 멤버십 할인 표시
    const membershipDiscountElem = document.getElementById('membershipDiscountAmount');

    // 쿠폰 할인, 포인트 할인, 배송비, 최종 결제
    const couponDiscountAmountElem = document.getElementById('couponDiscountAmount');
    const pointDiscountAmountElem = document.getElementById('pointDiscountAmount');
    const deliveryFeeAmountElem = document.getElementById('deliveryFeeAmount');
    const finalPaymentAmountElem = document.getElementById('finalPaymentAmount');

    // 사용자 정보
    const emailInput = document.getElementById('email');
    const nameInput = document.getElementById('name');
    const phoneInput = document.getElementById('phone');
    const addressInput = document.getElementById('address');

    // 주소 버튼들
    const addressSearchButton = document.getElementById('addressSearchButton');
    const basicAddressBtn = document.getElementById('basicAddressBtn');
    const newAddressBtn = document.getElementById('newAddressBtn');

    // 결제 관련
    const paymentMethodSelect = document.getElementById('paymentMethod');
    const payButton = document.getElementById('payButton');

    // 포인트
    const availablePointsElem = document.getElementById('availablePoints');
    const usePointsInput = document.getElementById('usePoints');
    const useAllPointsBtn = document.getElementById('useAllPointsBtn');

    // 쿠폰
    const openCouponModalBtn = document.getElementById('openCouponModalBtn');
    const couponModal = document.getElementById('couponModal');
    const couponListContainer = document.getElementById('couponListContainer');
    const couponLabel = document.querySelector('label[for="openCouponModalBtn"]'); 

    // --------------------------------------------------
    // 2) 전역 변수
    // --------------------------------------------------
    let userData = null;
    let membership = 'BRONZE';       // 기본값, 서버에서 가져옴
    let selectedCouponId = null;     // 적용된 쿠폰 ID
    let selectedCouponRate = 0;      // 적용된 쿠폰 할인율
    let availablePoints = 0;         // 보유 포인트
    const DELIVERY_FEE = 3000;       // 배송비
    const cartItems = JSON.parse(localStorage.getItem('cartItems')) || [];

    // 멤버십 할인율 계산 함수
    function getMembershipDiscountRate(m) {
		console.log("현재 등급: " + m);
        switch (m) {
            case 'GOLD': return 0.05;      // 5%
            case 'DIAMOND': return 0.10;   // 10%
            default: return 0;
        }
    }

    // --------------------------------------------------
    // 3) 장바구니 상품 목록 & 금액 표시
    // --------------------------------------------------
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
                        <p>${itemTotalPrice.toLocaleString()} 원</p>
                    </div>
                </div>
            `;
            orderItemsContainer.appendChild(itemDiv);
        });

        orderTotalAmount.textContent = totalPrice.toLocaleString();
    }

    // --------------------------------------------------
    // 4) 최종 결제 금액 계산
    // --------------------------------------------------
    function recalcFinalAmount() {
        let totalPrice = 0;
        cartItems.forEach(item => {
            totalPrice += item.price * item.quantity;
        });

        // (1) 멤버십 할인 적용 (쿠폰/포인트 이전에!)
        const discountRate = getMembershipDiscountRate(membership);
        const membershipDiscount = Math.floor(totalPrice * discountRate);
        totalPrice -= membershipDiscount;  // 멤버십 할인 차감

        // (2) 쿠폰 할인액
        let couponDiscount = Math.floor(totalPrice * (selectedCouponRate / 100));

        // (3) 포인트 사용
        const inputUsePoints = parseInt(usePointsInput.value, 10) || 0;

        // (4) 최종 결제금액
        let finalAmount = totalPrice - couponDiscount - inputUsePoints + DELIVERY_FEE;
        if (finalAmount < 0) finalAmount = 0;

        // (5) UI 표시
        membershipDiscountElem.textContent = `-${membershipDiscount.toLocaleString()}`;
        couponDiscountAmountElem.textContent = `-${couponDiscount.toLocaleString()}`;
        pointDiscountAmountElem.textContent = `-${inputUsePoints.toLocaleString()}`;
        deliveryFeeAmountElem.textContent = `+${DELIVERY_FEE.toLocaleString()}`;
        finalPaymentAmountElem.textContent = finalAmount.toLocaleString();

        return finalAmount;
    }

    // --------------------------------------------------
    // 5) 사용자(멤버) 정보 가져오기
    // --------------------------------------------------
    fetch('/api/v1/members/me')
      .then(res => {
        if (!res.ok) throw new Error('로그인이 필요합니다.');
        return res.json();
      })
      .then(data => {
        userData = data;
        membership = data.membership || 'BRONZE'; // 예: GOLD, DIAMOND, etc.
        
        emailInput.value = data.email;
        nameInput.value = data.name;
        phoneInput.value = data.phone;
        addressInput.value = data.address;

        // 주소검색 버튼 비활성화(기존 주소가 있으면)
        if (data.address) {
          addressSearchButton.disabled = true;
        }

        recalcFinalAmount();
      })
      .catch(err => {
        console.log(err);
        alert('로그인 정보가 없거나 세션이 만료되었습니다.');
        location.href = '/login';
      });


    // --------------------------------------------------
    // 6) 보유 포인트 조회
    // --------------------------------------------------
    fetch('/api/v1/points/me')
     .then(res => {
       if (!res.ok) throw new Error('포인트 정보가 없습니다.');
       return res.json();
     })
     .then(pointData => {
       availablePoints = pointData.balance;  // 예: { balance: 10000 }
       availablePointsElem.textContent = availablePoints.toLocaleString();
     })
     .catch(err => {
       console.error(err);
       availablePoints = 0;
       availablePointsElem.textContent = '0';
     });


    // --------------------------------------------------
    // 7) 버튼 / 이벤트
    // --------------------------------------------------

    // (A) 포인트 전체 사용 버튼
    useAllPointsBtn.addEventListener('click', () => {
      usePointsInput.value = availablePoints;
      recalcFinalAmount();
    });

    // (B) 포인트 입력 변경 시 재계산
    usePointsInput.addEventListener('input', () => {
      recalcFinalAmount();
    });

    // (C) 기본 주소 버튼
    basicAddressBtn.addEventListener('click', () => {
      if (userData && userData.address) {
        addressInput.value = userData.address;
        addressSearchButton.disabled = true;
      } else {
        alert('회원 정보에 기본 주소가 없습니다.');
      }
    });

    // (D) 새 주소 버튼
    newAddressBtn.addEventListener('click', () => {
      addressInput.value = '';
      addressSearchButton.disabled = false;
      addressInput.focus();
    });

    // (E) 주소 검색
    addressSearchButton.addEventListener('click', () => {
      new daum.Postcode({
          oncomplete: function(data) {
              addressInput.value = data.address;
          }
      }).open();
    });

    // (F) 쿠폰 모달 열기
    openCouponModalBtn.addEventListener('click', () => {
      const couponBsModal = new bootstrap.Modal(couponModal);
      couponBsModal.show();

      // 쿠폰 목록 API
      fetch('/api/v1/coupons')
        .then(res => {
          if (!res.ok) throw new Error('쿠폰 정보를 가져올 수 없습니다.');
          return res.json();
        })
        .then(coupons => {
          renderCouponList(coupons);
        })
        .catch(err => {
          console.error(err);
          couponListContainer.innerHTML = '<p>쿠폰이 없습니다.</p>';
        });
    });

    // 쿠폰 목록 렌더링
    function renderCouponList(coupons) {
        couponListContainer.innerHTML = '';
        if (!coupons || coupons.length === 0) {
            couponListContainer.innerHTML = '<p>보유 쿠폰이 없습니다.</p>';
            return;
        }

        coupons.forEach(coupon => {
            if (coupon.isUsed) {
                // 이미 사용된 쿠폰
                const usedDiv = document.createElement('div');
                usedDiv.classList.add('alert','alert-secondary','mb-2');
                usedDiv.innerHTML = `
                  [사용됨] <strong>${coupon.reason}</strong> - 할인율: ${coupon.discountRate}%
                `;
                couponListContainer.appendChild(usedDiv);
            } else {
                // 사용 가능
                const couponDiv = document.createElement('div');
                couponDiv.classList.add('alert','alert-info','mb-2');
                couponDiv.style.cursor = 'pointer';
                couponDiv.dataset.couponId = coupon.id;
                couponDiv.dataset.discountRate = coupon.discountRate;
                couponDiv.dataset.reason = coupon.reason;

                couponDiv.innerHTML = `
                  <strong>${coupon.reason}</strong> 
                  <span>- 할인율: ${coupon.discountRate}%</span>
                  <span style="font-size:0.9em;color:gray;"> (지급일: ${coupon.createdDate})</span>
                `;

                couponDiv.addEventListener('click', function() {
                    selectedCouponId = parseInt(this.dataset.couponId);
                    selectedCouponRate = parseInt(this.dataset.discountRate);
                    
                    couponLabel.textContent = `쿠폰 적용 (${this.dataset.reason})`;

                    alert(`'${this.dataset.reason}' 쿠폰이 적용되었습니다.`);

                    // 모달 닫기
                    const bsModal = bootstrap.Modal.getInstance(couponModal);
                    bsModal.hide();

                    recalcFinalAmount();
                });

                couponListContainer.appendChild(couponDiv);
            }
        });
    }

    // (H) 결제하기
    payButton.addEventListener('click', () => {
        if (!cartItems || cartItems.length === 0) {
            alert('주문할 상품이 없습니다.');
            return;
        }
        const address = addressInput.value.trim();
        if (!address) {
            alert('주소를 입력하세요.');
            return;
        }

        // 원래 금액 (멤버십 할인 전)
        let rawTotalPrice = 0;
        cartItems.forEach(item => {
          rawTotalPrice += item.price * item.quantity;
        });

        // 결제 요청에서, 아임포트 금액 노출은 '상품가격(포인트/쿠폰 제외)' 
        // (멤버십 할인 여부는 비즈니스 규칙에 따라..)
        // 여기서는 '멤버십 할인 전' 금액으로 노출한다고 가정
        const paramAmount = rawTotalPrice;

        // 최종 결제금액 (recalcFinalAmount() 결과)
        const finalAmount = recalcFinalAmount();

        // 아임포트 설정
        const { IMP } = window;
        IMP.init('imp46747186');

        const param = {
            pg: 'kakaopay.TC0ONETIME',
            pay_method: 'card',
            merchant_uid: 'order_' + new Date().getTime(),
            name: '장바구니 결제',
            amount: paramAmount, 
            buyer_email: emailInput.value,
            buyer_name: nameInput.value,
            buyer_tel: phoneInput.value,
            buyer_addr: address,
        };

        IMP.request_pay(param, function(rsp) {
            if (rsp.success) {
                alert('결제가 성공적으로 완료되었습니다.\n imp_uid: ' + rsp.imp_uid);

                // 서버에 보낼 DTO
                const orderRequest = {
                    memberId: (userData && userData.id) ? userData.id : null,
                    address: address,
                    paymentMethod: paymentMethodSelect.value,
                    usedPoint: parseInt(usePointsInput.value, 10) || 0,
                    couponId: selectedCouponId || null,
                    orderItems: cartItems.map(item => ({
                        productId: item.productId,
                        productName: item.name,
                        color: item.color,
                        size: item.size,
                        quantity: item.quantity,
                        unitPrice: item.price
                    })),
                    impUid: rsp.imp_uid,
                    merchantUid: rsp.merchant_uid
                };

                fetch('/api/order', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(orderRequest)
                })
                .then(res => res.json())
                .then(data => {
                    alert(`주문이 완료되었습니다! DB orderId: ${data.orderId}`);
                    // 장바구니 클리어
                    localStorage.removeItem('cartItems');
                    // 페이지 이동
                    location.href = '/';
                })
                .catch(err => {
                    console.error(err);
                    alert('주문 저장 중 오류가 발생했습니다.');
                });

            } else {
                alert('결제가 취소되었거나 실패했습니다.\n' + rsp.error_msg);
            }
        });
    });

    // 페이지 처음 진입 시 초기 렌더링
    renderOrderItems();
    recalcFinalAmount();
});
