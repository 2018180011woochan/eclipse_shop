window.isCounseling = false;
window.isCounselingRequest = false;
let stompClient = null;
let currentRoomId = null;
let userEmail = "";


// 페이지 로드 시 현재 로그인 정보 불러오기
document.addEventListener("DOMContentLoaded", function() {
  fetch("/api/v1/members/me") 
    .then(res => {
      if (!res.ok) throw new Error("사용자 정보를 불러오는 데 실패했습니다.");
      return res.json();
    })
    .then(data => {
      userEmail = data.email; 
      console.log("로그인된 유저 이메일:", userEmail);
    })
    .catch(err => console.error("Error fetching user info:", err));
	
	// 초기에는 자동응답 모드이므로 채팅창 비활성화
	deactivateCounselingMode(); 
});

// 상담사 연결 함수 글로벌 정의
window.connectToAdmin = function() {
  if (window.isCounseling) {
    alert("이미 상담 중입니다.");
    return;
  }
  
  if (window.isCounselingRequest) {
	alert("현재 상담요청이 진행중입니다.");
	return;
  }
  window.isCounselingRequest = true;
  
  if (!userEmail) {
    alert("로그인 정보(유저 이메일)를 불러오지 못했습니다. 다시 시도해주세요.");
    return;
  }

  appendBotMessage("상담사를 연결중입니다. 잠시만 기다려주세요.");
  
  fetch("/api/v1/chat/connection", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(userEmail)
  })
  .then(res => {
    if (!res.ok) throw new Error("방 생성에 실패했습니다.");
    return res.json();
  })
  .then(room => {
    currentRoomId = room.roomId;
    console.log("상담 방 생성 완료, roomId:", currentRoomId);
    connectStomp(); // WebSocket 연결
  })
  .catch(err => console.error("상담 연결 오류:", err));
};

// 웹소켓 연결
function connectStomp() {
  const socket = new SockJS('/ws');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function(frame) {
    console.log("User Connected:", frame);

    // 메시지 수신 구독
    stompClient.subscribe("/chatroom/messages", function(msg) {
      const messageBody = JSON.parse(msg.body);
      // 방 필터링
      if (messageBody.roomId === currentRoomId) {
		if (messageBody.content != "상담이 시작되었습니다.")
        	appendMessage(messageBody.content, messageBody.sender);
		
		// 상담 시작 메시지 수신 시 상담 모드 활성화
		else if (messageBody.content === "상담이 시작되었습니다.") {
		  activateCounselingMode();
		  appendBotMessage(messageBody.content);
		}
      }
    });

    // 상담 종료 알림 구독
    stompClient.subscribe("/chatroom/chat/end", function(msg) {
      const endBody = JSON.parse(msg.body);
      if (endBody.roomId === currentRoomId) {
        alert("상담이 종료되었습니다.");
        closeChat();
      }
    });
  });
}

// 메시지 전송
document.getElementById("send-btn").addEventListener("click", sendUserMsg);
document.getElementById("message").addEventListener("keypress", function(e) {
  if (e.key === "Enter") {
    sendUserMsg();
  }
});

function sendUserMsg() {
	
  if (!window.isCounseling) {
	  console.log("현재 상담 모드가 아닙니다. 버튼식 질문만 사용 가능.");
	  return;
  }
	
  const content = document.getElementById("message").value.trim();
  if (!content) return;      // 빈 메시지는 무시
  if (!stompClient) return;  // 아직 연결 안 됐다면 무시

  stompClient.send("/app/chat/send", {}, JSON.stringify({
    roomId: currentRoomId,
    sender: userEmail,  // 보낸 사람
    content: content
  }));
  document.getElementById("userMsg").value = "";
}

// 상담 모드 활성화
function activateCounselingMode() {
  window.isCounseling = true;
  console.log("상담 모드 진입");
  // 버튼 영역 숨기기
  document.getElementById("questions-container").classList.add("hidden");
  // 입력창 활성화
  document.getElementById("message").disabled = false;
  document.getElementById("send-btn").disabled = false;
}

// 상담 모드 비활성화
function deactivateCounselingMode() {
  window.isCounseling = false;
  window.isCounselingRequest = false;
  console.log("상담 모드 종료");
  // 버튼 영역 보이기
  document.getElementById("questions-container").classList.remove("hidden");
  // 입력창 비활성화
  document.getElementById("message").disabled = true;
  document.getElementById("send-btn").disabled = true;
  // WebSocket 종료
  if (stompClient) {
    stompClient.disconnect(() => {
      console.log("WebSocket disconnected.");
    });
  }
  currentRoomId = null;
}

function appendMessage(msgContent, sender) {
  const chatContentDiv = document.getElementById("chat-content");
  const messageDiv = document.createElement("div");
  
  if (sender != userEmail)
  	messageDiv.textContent = sender + ": " + msgContent;
  chatContentDiv.appendChild(messageDiv);
  chatContentDiv.scrollTop = chatContentDiv.scrollHeight;
}

function appendBotMessage(msgContent) {
  const chatContentDiv = document.getElementById("chat-content");
  const messageDiv = document.createElement("div");
  
  messageDiv.textContent = msgContent;
  chatContentDiv.appendChild(messageDiv);
  chatContentDiv.scrollTop = chatContentDiv.scrollHeight;
}

function closeChat() {
  currentRoomId = null;
  if (stompClient) {
    stompClient.disconnect(() => {
      console.log("WebSocket disconnected.");
    });
  }
  
  deactivateCounselingMode();
}