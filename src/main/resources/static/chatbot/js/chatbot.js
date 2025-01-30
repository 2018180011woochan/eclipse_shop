document.addEventListener("DOMContentLoaded", function () {
  const questionsContainer = document.getElementById("questions-container");
  const questionsBar = document.getElementById("questions-bar");
  const chatContent = document.getElementById("chat-content");
  const messageInput = document.getElementById("message");
  const sendButton = document.getElementById("send-btn");

  // 🔹 질문 바 클릭 시 접기/펼치기
  questionsBar.addEventListener("click", function() {
    if (questionsContainer.classList.contains("hidden")) {
      // 현재 숨겨져 있으면 펼치기
      questionsContainer.classList.remove("hidden");
      questionsBar.textContent = "질문 접기";
    } else {
      // 보이는 상태라면 숨기기
      questionsContainer.classList.add("hidden");
      questionsBar.textContent = "질문 펼치기";
    }
  });

  // 🔹 질문 버튼 클릭 시 자동 메시지 전송
  document.querySelectorAll(".chatbot-question-btn").forEach(button => {
    button.addEventListener("click", function () {
      const question = this.textContent;
      processMessage(question);
    });
  });

  // 🔹 메시지 전송 버튼 및 엔터키 처리
  sendButton.addEventListener("click", function() {
    const userMsg = messageInput.value.trim();
    if (!userMsg) return;
    processMessage(userMsg);
    messageInput.value = "";
  });
  messageInput.addEventListener("keypress", function(event) {
    if (event.key === "Enter") {
      const userMsg = messageInput.value.trim();
      if (!userMsg) return;
      processMessage(userMsg);
      messageInput.value = "";
    }
  });

  // 🔹 메시지 처리 함수
  function processMessage(message) {
    appendMessage(message, "user"); // 화면에 사용자 메시지 표시

    // 서버에 메시지 전송 (POST /api/v1/chatbot)
    fetch("/api/v1/chatbot", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: `message=${encodeURIComponent(message)}`
    })
    .then(response => {
      if (!response.ok) throw new Error("Network response was not ok");
      return response.json();
    })
    .then(data => {
      appendMessage(data.response, "bot");
    })
    .catch(error => {
      console.error("Error:", error);
      appendMessage("에러가 발생했습니다.", "bot");
    });
  }

  // 🔹 채팅창에 메시지 추가 함수
  function appendMessage(content, sender) {
    const messageDiv = document.createElement("div");
    messageDiv.classList.add("message");
    if (sender === "user") {
      messageDiv.classList.add("user");
    } else {
      messageDiv.classList.add("bot");
    }
    messageDiv.textContent = content;
    chatContent.appendChild(messageDiv);
    chatContent.scrollTop = chatContent.scrollHeight;
  }
});
