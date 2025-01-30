document.addEventListener("DOMContentLoaded", function () {
    const chatContent = document.getElementById("chat-content");
    const messageInput = document.getElementById("message");
    const sendButton = document.getElementById("send-btn");

    // 메시지 추가 함수
    function appendMessage(content, sender) {
        const messageDiv = document.createElement("div");
        messageDiv.style.padding = "8px";
        messageDiv.style.margin = "5px 0";
        messageDiv.style.borderRadius = "5px";
        messageDiv.style.maxWidth = "70%";

        if (sender === "user") {
            messageDiv.style.backgroundColor = "#4CAF50";
            messageDiv.style.color = "white";
            messageDiv.style.alignSelf = "flex-end";
        } else {
            messageDiv.style.backgroundColor = "#ddd";
            messageDiv.style.color = "black";
            messageDiv.style.alignSelf = "flex-start";
        }

        messageDiv.textContent = content;
        chatContent.appendChild(messageDiv);
        chatContent.scrollTop = chatContent.scrollHeight; // 스크롤을 아래로 이동
    }

    // 메시지 전송 이벤트
    sendButton.addEventListener("click", function () {
        sendMessage();
    });

    messageInput.addEventListener("keypress", function (event) {
        if (event.key === "Enter") {
            sendMessage();
        }
    });

    function sendMessage() {
        const message = messageInput.value.trim();
        if (!message) return;

        appendMessage(message, "user"); // 사용자 메시지 추가
        messageInput.value = "";

        // 서버로 메시지 전송
        fetch("/api/v1/chatbot", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `message=${encodeURIComponent(message)}`
        })
        .then(response => response.json())
        .then(data => {
            appendMessage(data.response, "bot"); // 챗봇 응답 표시
        })
        .catch(error => {
            console.error("Error:", error);
            appendMessage("에러가 발생했습니다.", "bot");
        });
    }
});
