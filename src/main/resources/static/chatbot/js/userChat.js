window.isCounseling = false;
window.isCounselingRequest = false;
window.stompClient = null;
window.currentRoomId = null;
window.userEmail = "";

// 페이지 로드 시 로그인 정보 가져오고 초기 모드 설정
document.addEventListener("DOMContentLoaded", () => {
  fetch("/api/v1/members/me", {
    credentials: 'include'
  })
    .then(res => {
      if (!res.ok) throw new Error("사용자 정보 로드 실패");
      return res.json();
    })
    .then(data => {
      window.userEmail = data.email;
      console.log("로그인된 유저 이메일:", window.userEmail);
    })
    .catch(err => console.error(err))
    .finally(() => deactivateCounselingMode());
});

// 상담사 연결
window.connectToAdmin = function() {
  if (window.isCounseling || window.isCounselingRequest) {
    alert("이미 상담 중이거나 요청 중입니다.");
    return;
  }
  if (!window.userEmail) {
    alert("1:1 상담은 로그인 후 가능합니다.");
    return;
  }
  window.isCounselingRequest = true;
  appendBotMessage("상담사를 연결중입니다. 잠시만 기다려주세요.");

  fetch("/api/v1/chat/connection", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(window.userEmail)
  })
    .then(res => {
      if (!res.ok) throw new Error("방 생성 실패");
      return res.json();
    })
    .then(room => {
      window.currentRoomId = room.roomId;
      console.log("상담 방 생성 완료, roomId:", room.roomId);
      connectStomp();
    })
    .catch(err => console.error("상담 연결 오류:", err));
};

// WebSocket 연결 및 구독
function connectStomp() {
  const socket = new SockJS('/ws');
  window.stompClient = Stomp.over(socket);
  window.stompClient.connect({}, frame => {
    console.log("User Connected:", frame);
    window.stompClient.subscribe("/chatroom/messages", onMessage);
    window.stompClient.subscribe("/chatroom/chat/end", onEnd);
  });
}

// 메시지 받을 때
function onMessage(msg) {
  const body = JSON.parse(msg.body);
  if (body.roomId !== window.currentRoomId) return;
  if (body.content === "상담이 시작되었습니다.") {
    activateCounselingMode();
    appendBotMessage(body.content);
  } else {
    appendBotMessage(body.content);
  }
}

// 상담 종료 알림 받을 때
function onEnd(msg) {
  const body = JSON.parse(msg.body);
  if (body.roomId === window.currentRoomId) {
    alert("상담이 종료되었습니다.");
    closeChat();
  }
}

// 상담 모드 활성화
function activateCounselingMode() {
  window.isCounseling = true;
  document.getElementById("questions-container").classList.add("hidden");
  document.getElementById("message").disabled = false;
  document.getElementById("send-btn").disabled = false;
}

// 상담 모드 비활성화
function deactivateCounselingMode() {
  window.isCounseling = false;
  window.isCounselingRequest = false;
  document.getElementById("questions-container").classList.remove("hidden");
  document.getElementById("message").disabled = true;
  document.getElementById("send-btn").disabled = true;
  if (window.stompClient) {
    window.stompClient.disconnect();
    window.stompClient = null;
  }
}

// 유저 메시지 화면에 추가
function appendUserMessage(content) {
  const chatContent = document.getElementById("chat-content");
  const div = document.createElement("div");
  div.classList.add("message", "user");
  div.textContent = content;
  chatContent.appendChild(div);
  chatContent.scrollTop = chatContent.scrollHeight;
}

// 상담사(봇) 메시지 화면에 추가
function appendBotMessage(content) {
  const chatContent = document.getElementById("chat-content");
  const div = document.createElement("div");
  div.classList.add("message", "bot");
  div.textContent = content;
  chatContent.appendChild(div);
  chatContent.scrollTop = chatContent.scrollHeight;
}

// 유저가 전송 버튼 누를 때
document.getElementById("send-btn").addEventListener("click", sendUserMsg);
document.getElementById("message").addEventListener("keypress", e => {
  if (e.key === "Enter") sendUserMsg();
});

function sendUserMsg() {
  if (!window.isCounseling) {
    console.log("현재 상담 모드가 아닙니다. 버튼식 질문만 사용 가능.");
    return;
  }
  const messageInput = document.getElementById("message");
  const content = messageInput.value.trim();
  if (!content || !window.stompClient) return;
  // 1) 화면에 유저 메시지 먼저 찍고
  appendUserMessage(content);
  // 2) 서버로 WebSocket 전송
  window.stompClient.send(
    "/app/chat/send",
    {},
    JSON.stringify({
      roomId: window.currentRoomId,
      sender: window.userEmail,
      content: content
    })
  );
  messageInput.value = "";
}

// 채팅 닫기
function closeChat() {
  window.currentRoomId = null;
  deactivateCounselingMode();
}
