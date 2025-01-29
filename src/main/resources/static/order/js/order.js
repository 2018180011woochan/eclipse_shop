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
	
	// 포인트
	const availablePointsElem = document.getElementById('availablePoints');
	const usePointsInput = document.getElementById('usePoints');
	const useAllPointsBtn = document.getElementById('useAllPointsBtn');

	// 쿠폰
	const openCouponModalBtn = document.getElementById('openCouponModalBtn');
	const couponModal = document.getElementById('couponModal');
	const couponListContainer = document.getElementById('couponListContainer');
	const couponDiscountAmountElem = document.getElementById('couponDiscountAmount');
	const finalPaymentAmountElem = document.getElementById('finalPaymentAmount');
	
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

	// 최종 결제금액 재계산하는 함수
	function recalcFinalAmount() {
	    // 총 상품금액
	    let totalPrice = 0;
	    cartItems.forEach(item => totalPrice += item.price * item.quantity);

	    // 포인트 사용
	    const inputUsePoints = parseInt(usePointsInput.value, 10) || 0;

	    // 쿠폰 할인액
	    let couponDiscount = 0;
	    if (selectedCouponRate > 0) {
			// 총 상품 금액 기준 할인
	        couponDiscount = Math.floor(totalPrice * (selectedCouponRate / 100));
	    }

	    // 표시
	    couponDiscountAmountElem.textContent = couponDiscount.toLocaleString();

	    // 최종 금액 = 총 상품금액 - 포인트 - 쿠폰할인
	    let finalAmount = totalPrice - inputUsePoints - couponDiscount;
	    if (finalAmount < 0) finalAmount = 0;

	    finalPaymentAmountElem.textContent = finalAmount.toLocaleString();
	    return finalAmount;
	}
	
    // 2) 로그인 사용자 정보 가져오기
    fetch('/api/v1/members/me')
      .then(res => {
        if (!res.ok) throw new Error('로그인이 필요합니다.');
        return res.json();
      })
      .then(data => {
        userData = data;
        emailInput.value = data.email;
        nameInput.value = data.name;
        phoneInput.value = data.phone;
        addressInput.value = data.address;
        
        addressSearchButton.disabled = true;
      })
      .catch(err => {
        console.log(err);
        alert("로그인 정보가 없거나, 세션이 만료되었습니다.");
        location.href = "/login";
      });

	// 2-2) Point 잔액
   	let availablePoints = 0;
   	fetch('/api/v1/points/me')
     .then(res => {
       if (!res.ok) throw new Error('포인트 정보가 없습니다.');
       return res.json();
     })
     .then(pointData => {
       availablePoints = pointData.balance;
       availablePointsElem.textContent = availablePoints.toLocaleString();
     })
     .catch(err => {
       console.error(err);
       availablePointsElem.textContent = "0";
     });
	 
	 // 포인트 입력 변경 시에도 재계산
	 usePointsInput.addEventListener('input', () => {
	     recalcFinalAmount();
	 });
	 
	 // 포인트 전체 사용 버튼
	 useAllPointsBtn.addEventListener('click', () => {
	   usePointsInput.value = availablePoints; 
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
	
	// 쿠폰 적용 버튼 클릭 → 모달 띄우기 & 쿠폰 목록 로드
	   openCouponModalBtn.addEventListener('click', () => {
	       // Bootstrap Modal show
	       const couponBsModal = new bootstrap.Modal(couponModal);
	       couponBsModal.show();
	
	       // 쿠폰 목록 fetch
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
	           couponListContainer.innerHTML = `<p>쿠폰이 없습니다.</p>`;
	         });
	   });

	   // 쿠폰 목록 렌더링 함수
	   function renderCouponList(coupons) {
	       couponListContainer.innerHTML = ''; // 초기화
	       if (!coupons || coupons.length === 0) {
	           couponListContainer.innerHTML = `<p>보유 쿠폰이 없습니다.</p>`;
	           return;
	       }
	       // 각 쿠폰을 버튼/리스트 형태로 표시
	       coupons.forEach(coupon => {
	           if (coupon.isUsed) {
	               // 이미 사용된 쿠폰은 비활성 표시
	               const usedDiv = document.createElement('div');
	               usedDiv.innerHTML = `
	                 <div class="alert alert-secondary mb-2">
	                   [사용됨] <strong>${coupon.reason}</strong> 
	                   - 할인율: ${coupon.discountRate}%
	                 </div>
	               `;
	               couponListContainer.appendChild(usedDiv);
	           } else {
	               // 사용 가능한 쿠폰
	               const couponDiv = document.createElement('div');
	               couponDiv.classList.add('alert', 'alert-info', 'mb-2');
	               couponDiv.style.cursor = 'pointer';
				   couponDiv.dataset.couponId = coupon.id;  // 쿠폰 ID 저장
				   couponDiv.dataset.discountRate = coupon.discountRate;  // 할인율 저장
	               couponDiv.innerHTML = `
	                   <strong>${coupon.reason}</strong> 
	                   <span>- 할인율: ${coupon.discountRate}%</span>
	                   <span style="font-size:0.9em;color:gray;"> (지급일: ${coupon.createdDate})</span>
	               `;
	               // 쿠폰 클릭 시 => 할인 적용
	               couponDiv.addEventListener('click', () => {
	                   selectedCouponId = coupon.id;
	                   selectedCouponRate = coupon.discountRate;
	                   alert(`'${coupon.reason}' 쿠폰이 적용되었습니다.`);

	                   // 모달 닫기
	                   const couponBsModal = bootstrap.Modal.getInstance(couponModal);
	                   couponBsModal.hide();

	                   // 할인액 재계산
	                   recalcFinalAmount();
	               });

	               couponListContainer.appendChild(couponDiv);
	           }
	       });
	   }

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

		// (라) 사용 포인트 입력값
		const inputUsePoints = parseInt(usePointsInput.value, 10) || 0;
		// 보유 포인트보다 많다면 alert
		if (inputUsePoints > availablePoints) {
		  alert("보유한 포인트를 초과했습니다.");
		  return;
		}
		
		// 쿠폰 할인 계산
		let couponDiscount = 0;
		 if (selectedCouponRate > 0) {
		     // 여기서는 총 상품금액 기준으로 쿠폰 할인
		     couponDiscount = Math.floor(totalPrice * (selectedCouponRate / 100));
		 }
		
		
		// 최종 결제 금액
		const finalAmount = totalPrice - inputUsePoints - couponDiscount;
		if (finalAmount < 0) {
		  alert("포인트가 주문 금액을 초과합니다.");
		  return;
		}
		
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
				console.log("사용 포인트 입력값:", inputUsePoints);
                const orderRequest = {
                    memberId: 1, // 실제론 userData.id 등을 사용
                    address: address,
                    paymentMethod: paymentMethodSelect.value,
					usedPoint: inputUsePoints, // 사용 포인트 추가
					couponId: selectedCouponId ? parseInt(selectedCouponId) : null, // 쿠폰id
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

				localStorage.removeItem("selectedCouponId");
				
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
