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