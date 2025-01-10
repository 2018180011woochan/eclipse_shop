document.addEventListener('DOMContentLoaded', () => {
    // 드롭다운 내의 정렬 옵션
    const sortLinks = document.querySelectorAll('.dropdown-content a');
    const productContainer = document.getElementById('product-container');

    sortLinks.forEach(link => {
        link.addEventListener('click', event => {
            event.preventDefault(); // a태그 기본 이동 방지
            const sortParam = link.getAttribute('data-sort'); // newest, oldest

            // Ajax 요청
            fetch(`/api/products?sort=${sortParam}`)
                .then(response => response.json())
                .then(data => {
                    // 기존 목록 제거
                    productContainer.innerHTML = '';

                    // 새 상품 목록 구성
                    data.forEach(item => {
                        const productDiv = document.createElement('div');
                        productDiv.classList.add('product-item');

                        productDiv.innerHTML = `
                            <img src="${item.thumbnailUrl}" alt="썸네일" />
                            <div class="product-name">${item.name}</div>
                            <div class="product-price">₩ ${item.price}</div>
                        `;
                        productContainer.appendChild(productDiv);
                    });
                })
                .catch(error => console.error("Error fetching products:", error));
        });
    });
});
