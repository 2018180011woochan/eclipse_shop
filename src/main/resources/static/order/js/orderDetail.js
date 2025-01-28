document.addEventListener("DOMContentLoaded", function() {

    const orderIdField = document.getElementById("orderId");   // hidden input
    const orderStatusField = document.getElementById("orderStatus"); // select
    const statusMessage = document.getElementById("statusMessage");

    // 페이지 로드 시 현재 주문 ID
    const orderId = orderIdField.value;

    // (1) orderStatus가 변경될 때마다 서버에 상태 업데이트 요청
    orderStatusField.addEventListener("change", function() {

        // 새로 선택된 상태 값
        const newStatus = orderStatusField.value;

        // fetch를 통해 POST 요청 (쿠키 인증 시 credentials:'include' 고려)
        fetch('/admin/api/orders/' + orderId + '/update-status', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            // 쿠키 기반 인증이라면 쿠키 전송
            // credentials: 'include', 
            body: new URLSearchParams({ orderStatus: newStatus })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('서버 응답 실패: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            // 성공
            statusMessage.textContent = data.message; 
            // data.newStatus 값이 정상적으로 돌아오면
            // orderStatusField.value = data.newStatus; // 굳이 변경할 필요 없지만 필요 시 재설정
        })
        .catch(error => {
            // 실패
            statusMessage.textContent = '상태 업데이트에 실패했습니다.';
            console.error(error);
        });
    });
});
