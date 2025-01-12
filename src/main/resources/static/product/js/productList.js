document.addEventListener('DOMContentLoaded', () => {
    let currentPage = 0;  // 현재 페이지(0-based)
    let totalPages = 1;   // 총 페이지 수 (서버 응답 후 업데이트)
    const size = 8;       // 한 페이지에 보여줄 상품 수

    const productContainer = document.getElementById('product-container');

    // 정렬(드롭다운) 로직
    const sortLinks = document.querySelectorAll('.dropdown-content a');
    sortLinks.forEach(link => {
        link.addEventListener('click', event => {
            event.preventDefault();
            const sortParam = link.getAttribute('data-sort'); // newest, oldest
            currentPage = 0; // 정렬 바꿀 때 첫 페이지로 리셋
            loadProducts(sortParam, currentPage);
        });
    });

    // 검색 로직
    const searchBtn = document.getElementById('searchBtn');
    searchBtn.addEventListener('click', () => {
        const keyword = document.getElementById('searchKeyword').value;
        currentPage = 0; // 검색 시도 시에도 첫 페이지로
        loadSearch(keyword, currentPage);
    });

    // 페이지네이션 버튼
    const prevBtn = document.getElementById('prevPage');
    const nextBtn = document.getElementById('nextPage');
    const pageInfo = document.getElementById('pageInfo');

    if (prevBtn && nextBtn) {
        prevBtn.addEventListener('click', () => {
            if (currentPage > 0) {
                currentPage--;
                // 정렬/검색 상태에 따라 다르게 호출해야 할 수도 있음
                // 예시: 정렬 기반으로 재호출 (sortParam='newest' 가정)
                loadProducts('newest', currentPage);
            }
        });

        nextBtn.addEventListener('click', () => {
            if (currentPage < totalPages - 1) {
                currentPage++;
                // 동일하게 loadProducts() or loadSearch()...
                loadProducts('newest', currentPage);
            }
        });
    }

    // 실제 상품 불러오기 (정렬)
    function loadProducts(sortParam, page = 0) {
        fetch(`/api/products?sort=${sortParam}&page=${page}&size=${size}`)
            .then(response => response.json())
            .then(pageData => {
                // pageData.content : 상품 배열
                // pageData.totalPages : 전체 페이지 수
                totalPages = pageData.totalPages;
                renderProducts(pageData.content);

                // 페이지 정보 표시
                pageInfo.textContent = `${pageData.number + 1} / ${pageData.totalPages} pages`;
            })
            .catch(error => console.error("Error fetching products:", error));
    }

    // 검색 시
    function loadSearch(keyword, page = 0) {
        fetch(`/api/products/search?keyword=${keyword}&page=${page}&size=${size}`)
            .then(response => response.json())
            .then(pageData => {
                totalPages = pageData.totalPages;
                renderProducts(pageData.content);
                pageInfo.textContent = `${pageData.number + 1} / ${pageData.totalPages} pages`;
            })
            .catch(err => console.error(err));
    }

    // 렌더링 함수
    function renderProducts(productList) {
        productContainer.innerHTML = '';

        productList.forEach(item => {
            const productDiv = document.createElement('div');
            productDiv.classList.add('product-item');
            productDiv.innerHTML = `
                <img src="${item.thumbnailUrl}" alt="썸네일" />
                <div class="product-name">${item.name}</div>
                <div class="product-price">₩ ${item.price}</div>
            `;
            productContainer.appendChild(productDiv);
        });
    }

    // 페이지 로드 시, 기본적으로 최신순 0페이지 로딩
    loadProducts('newest', 0);
});
