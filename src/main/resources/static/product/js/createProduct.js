document.addEventListener("DOMContentLoaded", function() {
    // 서브 카테고리 동적 로딩 코드
    const mainCategorySelect = document.getElementById("mainCategory");
    mainCategorySelect.addEventListener("change", function () {
        const mainCategoryId = this.value;
        fetch(`/api/admin/category/${mainCategoryId}/subcategorys`)
            .then(response => response.json())
            .then(data => {
				console.log("서브카테고리 응답:", data);
                const subCategorySelect = document.getElementById("subCategory");
                subCategorySelect.innerHTML = '<option value="">서브 카테고리를 선택하세요</option>';
                data.forEach(subCategory => {
                    const option = document.createElement("option");
                    option.value = subCategory.categoryId;
                    option.textContent = subCategory.name;
                    subCategorySelect.appendChild(option);
                });
            })
            .catch(error => console.error("서브 카테고리 로딩 에러:", error));
    });

	// 옵션 추가 코드
	document.getElementById('addOption').addEventListener('click', function () {
	    const optionDiv = document.getElementById('options');
	    const optionCount = optionDiv.querySelectorAll('.option').length;
	    const newOption = `
	        <div class="option">
	            <label>사이즈</label>
	            <select name="options[${optionCount}].size" required>
	                <option value="S">S</option>
	                <option value="M">M</option>
	                <option value="L">L</option>
	                <option value="XL">XL</option>
	            </select>
	            <label>색상</label>
	            <input type="text" name="options[${optionCount}].color" placeholder="색상을 입력하세요" required>
	            <label>재고 수량</label>
	            <input type="number" name="options[${optionCount}].stockQuantity" min="1" required>
	        </div>
	    `;
	    optionDiv.insertAdjacentHTML('beforeend', newOption);
	});
});
