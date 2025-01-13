document.addEventListener('DOMContentLoaded', () => {
    let currentPage = 0;   // 현재 페이지 (0-based)
    let totalPages = 1;    // 전체 페이지 수 (API 응답으로 업데이트)
    let currentSort = 'newest'; // 기본 정렬
    let currentKeyword = '';    // 검색어 (검색 모드일 때 사용)
    const size = 8;       // 한 페이지에 표시할 상품 수

    const productContainer = document.getElementById('product-container');
    const paginationUl = document.getElementById('paginationUl');

    // 드롭다운 (정렬)
    const sortLinks = document.querySelectorAll('.dropdown-content a');
    sortLinks.forEach(link => {
        link.addEventListener('click', event => {
            //event.preventDefault();
            currentSort = link.getAttribute('data-sort'); // newest or oldest
            currentPage = 0; // 정렬 바꾸면 첫 페이지로
            currentKeyword = ''; // 검색어 비움(정렬 모드)
            loadProducts(currentSort, currentPage);
        });
    });

    // 검색 버튼
    const searchBtn = document.getElementById('searchBtn');
    searchBtn.addEventListener('click', () => {
        const keywordInput = document.getElementById('searchKeyword');
        currentKeyword = keywordInput.value.trim();
        currentPage = 0; // 검색시 첫 페이지
        loadSearch(currentKeyword, currentPage);
    });

    // 상품 목록 불러오기 (정렬용)
    function loadProducts(sortParam, page) {
        fetch(`/api/products?sort=${sortParam}&page=${page}&size=${size}`)
            .then(response => response.json())
            .then(pageData => {
                // pageData.content : 상품 목록
                // pageData.totalPages, pageData.number, ...
                totalPages = pageData.totalPages;
                currentPage = pageData.number;

                renderProducts(pageData.content);
                renderPagination(pageData); // 페이지네이션 렌더
            })
            .catch(error => console.error("Error fetching products:", error));
    }

    // 상품 목록 불러오기 (검색용)
    function loadSearch(keyword, page) {
        fetch(`/api/products/search?keyword=${keyword}&page=${page}&size=${size}`)
            .then(response => response.json())
            .then(pageData => {
                totalPages = pageData.totalPages;
                currentPage = pageData.number;

                renderProducts(pageData.content);
                renderPagination(pageData);
            })
            .catch(err => console.error("Error fetching search results:", err));
    }

    // 상품 목록 렌더
    function renderProducts(productList) {
        productContainer.innerHTML = '';
		
        productList.forEach(item => {
            const productDiv = document.createElement('div');
            productDiv.classList.add('product-item');
            productDiv.innerHTML = `
				<a href="/products/${item.productId}" style="text-decoration: none; color: inherit;">
	                <img src="${item.thumbnailUrl}" alt="썸네일" />
	                <div class="product-name">${item.name}</div>
	                <div class="product-price">₩ ${item.price}</div>
				</a>
            `;
            productContainer.appendChild(productDiv);
        });
    }

    // Bootstrap 페이지네이션 렌더
    function renderPagination(pageData) {
        paginationUl.innerHTML = ''; // 기존 버튼 제거

        const currentPageNum = pageData.number;      // 현재 페이지 (0-based)
        const totalPageCount = pageData.totalPages;  // 총 페이지 수

        // 맨앞 페이지
        const firstLi = createPageItem('&laquo;', 0, currentPageNum > 0);
        paginationUl.appendChild(firstLi);

        // 이전 페이지
        const prevLi = createPageItem('&lsaquo;', currentPageNum - 1, currentPageNum > 0);
        paginationUl.appendChild(prevLi);

        // 페이지 번호들 (간단 버전: 전부)
        for (let i = 0; i < totalPageCount; i++) {
            const pageNum = i;
            const li = createPageItem(`${i + 1}`, pageNum, true, (i === currentPageNum));
            paginationUl.appendChild(li);
        }

        // 다음 페이지
        const nextLi = createPageItem('&rsaquo;', currentPageNum + 1, currentPageNum < totalPageCount - 1);
        paginationUl.appendChild(nextLi);

        // 맨뒤 페이지
        const lastLi = createPageItem('&raquo;', totalPageCount - 1, currentPageNum < totalPageCount - 1);
        paginationUl.appendChild(lastLi);
    }

    // 페이지아이템 생성 함수
    function createPageItem(label, pageTarget, clickable, isActive = false) {
        const li = document.createElement('li');
        li.classList.add('page-item');

        if (!clickable) {
            // 클릭 불가 상태(비활성)
            li.classList.add('disabled');
        }

        if (isActive) {
            // 현재 페이지
            li.classList.add('active');
        }

        // a 태그 생성
        const aTag = document.createElement('a');
        aTag.classList.add('page-link');
        aTag.innerHTML = label; // HTML 엔티티 사용(&laquo; 등)

        // 클릭 이벤트
        if (clickable) {
            aTag.addEventListener('click', e => {
                //e.preventDefault();
                // 만약 지금 검색 모드라면 loadSearch, 아니면 loadProducts
                if (currentKeyword) {
                    // 검색 모드
                    loadSearch(currentKeyword, pageTarget);
                } else {
                    // 정렬 모드
                    loadProducts(currentSort, pageTarget);
                }
            });
        } else {
            // disabled link
            aTag.tabIndex = -1;
            aTag.setAttribute('aria-disabled', 'true');
        }

        li.appendChild(aTag);
        return li;
    }

    // 페이지 로드 시: newest 정렬, 첫 페이지
    loadProducts('newest', 0);
});
