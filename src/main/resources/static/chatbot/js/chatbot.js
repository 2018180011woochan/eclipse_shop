document.addEventListener("DOMContentLoaded", function () {
  const questionsContainer = document.getElementById("questions-container");
  const questionsBar = document.getElementById("questions-bar");
  const chatContent = document.getElementById("chat-content");
  const messageInput = document.getElementById("message");
  const sendButton = document.getElementById("send-btn");

  // 질문 바 토글
  questionsBar.addEventListener("click", () => {
    questionsContainer.classList.toggle("hidden");
    questionsBar.textContent = questionsContainer.classList.contains("hidden") ? "펼치기" : "접기";
  });

  // 기본 자동응답 버튼 클릭
  document.querySelectorAll(".chatbot-question-btn").forEach(button => {
    button.addEventListener("click", () => {
      const question = button.textContent.trim();
      if (question === "상담사 연결") {
        console.log("connect button clicked, userEmail =", window.userEmail);
        connectToAdmin();
      } else if (!window.isCounseling) {
        processMessage(question);
      }
    });
  });

  // 엔터/클릭 전송
  sendButton.addEventListener("click", sendUserMsg);
  messageInput.addEventListener("keypress", e => {
    if (e.key === "Enter") sendUserMsg();
  });

  // 자동응답 처리
  function processMessage(message) {
    appendMessage(message, "user");
    if (window.isCounseling) return;
    fetch("/api/v1/chatbot", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: `message=${encodeURIComponent(message)}`
    })
    .then(res => {
      if (!res.ok) throw new Error("Network response was not ok");
      return res.json();
    })
    .then(data => appendMessage(data.response, "bot"))
    .catch(_ => appendMessage("에러가 발생했습니다.", "bot"));
  }

  function sendUserMsg() {
    if (!window.isCounseling) return;
    const msg = messageInput.value.trim();
    if (!msg || !window.stompClient) return;
    window.stompClient.send("/app/chat/send", {}, JSON.stringify({
      roomId: window.currentRoomId,
      sender: window.userEmail,
      content: msg
    }));
    messageInput.value = "";
  }

  function appendMessage(content, sender) {
    const div = document.createElement("div");
    div.classList.add("message", sender === "user" ? "user" : "bot");
    div.textContent = sender === "user" ? content : content;
    chatContent.appendChild(div);
    chatContent.scrollTop = chatContent.scrollHeight;
  }
});
