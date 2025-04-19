window.isCounseling = false;
window.isCounselingRequest = false;
window.stompClient = null;
window.currentRoomId = null;
window.userEmail = "";

// 로그인의 토큰/쿠키 전송 포함하도록 수정
document.addEventListener("DOMContentLoaded", () => {
  fetch("/api/v1/members/me", {
    credentials: 'include'  // 쿠키 사용 시
    // headers: { "Authorization": `Bearer ${localStorage.getItem("ACCESS_TOKEN")}` } 
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

// WebSocket 연결
function connectStomp() {
  const socket = new SockJS('/ws');
  window.stompClient = Stomp.over(socket);
  window.stompClient.connect({}, frame => {
    console.log("User Connected:", frame);
    window.stompClient.subscribe("/chatroom/messages", onMessage);
    window.stompClient.subscribe("/chatroom/chat/end", onEnd);
  });
}

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

function onEnd(msg) {
  const body = JSON.parse(msg.body);
  if (body.roomId === window.currentRoomId) {
    alert("상담이 종료되었습니다.");
    closeChat();
  }
}

function activateCounselingMode() {
  window.isCounseling = true;
  document.getElementById("questions-container").classList.add("hidden");
  document.getElementById("message").disabled = false;
  document.getElementById("send-btn").disabled = false;
}

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

function appendBotMessage(content) {
  const chatContent = document.getElementById("chat-content");
  const div = document.createElement("div");
  div.classList.add("message", "bot");
  div.textContent = content;
  chatContent.appendChild(div);
  chatContent.scrollTop = chatContent.scrollHeight;
}

function closeChat() {
  window.currentRoomId = null;
  deactivateCounselingMode();
}
