// /chatbot/js/chatbot.js
document.addEventListener("DOMContentLoaded", () => {
  // 상태관리 변수
  let isCounseling = false,
      isCounselingRequest = false,
      stompClient = null,
      currentRoomId = null,
      userEmail = "";

  // DOM 참조
  const qBar   = document.getElementById("questions-bar");
  const qCont  = document.getElementById("questions-container");
  const chatC  = document.getElementById("chat-content");
  const msgIn  = document.getElementById("message");
  const sendB  = document.getElementById("send-btn");

  // 1) 로그인 유저 이메일 가져오기
  fetch("/api/v1/members/me", { credentials: 'include' })
    .then(res => {
      if (!res.ok) throw new Error("사용자 정보 로드 실패");
      return res.json();
    })
    .then(data => {
      userEmail = data.email;
      console.log("로그인된 이메일:", userEmail);
    })
    .catch(console.error)
    .finally(() => deactivateCounselingMode());

  // 2) 질문 바 접기/펼치기
  qBar.addEventListener("click", () => {
    qCont.classList.toggle("hidden");
    qBar.textContent = qCont.classList.contains("hidden") ? "펼치기" : "접기";
  });

  // 3) 자동응답 버튼
  document.querySelectorAll(".chatbot-question-btn").forEach(btn => {
    btn.addEventListener("click", () => {
      const txt = btn.textContent.trim();
      if (txt === "상담사 연결") {
        connectToAdmin();
      }
      else if (!isCounseling) {
        autoResponse(txt);
      }
    });
  });

  // 4) 직접 입력 전송(클릭/엔터)
  sendB.addEventListener("click", sendUserMsg);
  msgIn.addEventListener("keypress", e => {
    if (e.key === "Enter") sendUserMsg();
  });

  // ———————————————————————————————————
  // 함수 영역
  // ———————————————————————————————————

  // 자동응답 요청
  function autoResponse(question) {
    appendUser(question);

    if (isCounseling) return;

    console.log("▶ 자동응답 요청 보내기:", question);

    fetch("/api/v1/chatbot", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      // ← 반드시 백틱(`)으로 감쌔야 함
      body: `message=${encodeURIComponent(question)}`
    })
    .then(res => {
      console.log("◀ 자동응답 응답 상태:", res.status, res.statusText);
      if (!res.ok) {
        // 400, 404, 500 등 에러코드일 때
        return res.text().then(t => Promise.reject(`HTTP ${res.status}: ${t}`));
      }
      return res.json();
    })
    .then(json => {
      console.log("◀ 자동응답 JSON:", json);
      appendBot(json.response);
    })
    .catch(err => {
      console.error("⚠️ 자동응답 에러:", err);
      appendBot("에러가 발생했습니다.");
    });
  }

  // 1:1 상담사 연결
  function connectToAdmin() {
    if (isCounseling || isCounselingRequest) {
      alert("이미 상담 중이거나 요청 중입니다."); return;
    }
    if (!userEmail) {
      alert("1:1 상담은 로그인 후 가능합니다."); return;
    }
    isCounselingRequest = true;
    appendBot("상담사를 연결중입니다. 잠시만 기다려주세요.");

    fetch("/api/v1/chat/connection", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(userEmail)
    })
    .then(res => {
      if (!res.ok) throw new Error("방 생성 실패");
      return res.json();
    })
    .then(room => {
      currentRoomId = room.roomId;
      console.log("roomId:", currentRoomId);
      stompConnect();
    })
    .catch(err => console.error("연결 오류:", err));
  }

  // STOMP 연결
  function stompConnect() {
    const sock = new SockJS('/ws');
    stompClient = Stomp.over(sock);
    stompClient.connect({}, () => {
      stompClient.subscribe("/chatroom/messages", handleMsg);
      stompClient.subscribe("/chatroom/chat/end", handleEnd);
    });
  }

  // STOMP 메시지 수신
  function handleMsg(frame) {
    const msg = JSON.parse(frame.body);
    if (msg.roomId !== currentRoomId) return;
    // 내가 보낸 건 다시 안 찍히게 필터링
    if (msg.sender === userEmail) return;

    if (msg.content === "상담이 시작되었습니다.") {
      activateCounselingMode();
      appendBot(msg.content);
    } else {
      appendBot(msg.content);
    }
  }

  // 상담 종료 알림
  function handleEnd(frame) {
    const msg = JSON.parse(frame.body);
    if (msg.roomId === currentRoomId) {
      alert("상담이 종료되었습니다.");
      closeChat();
    }
  }

  // 유저 직접 메시지 전송
  function sendUserMsg() {
    if (!isCounseling || !stompClient) return;
    const txt = msgIn.value.trim();
    if (!txt) return;
    appendUser(txt);
    stompClient.send("/app/chat/send", {}, JSON.stringify({
      roomId: currentRoomId,
      sender: userEmail,
      content: txt
    }));
    msgIn.value = "";
  }

  // 메시지 append 유틸
  function appendBot(text) {
    const d = document.createElement("div");
    d.classList.add("message","bot");
    d.textContent = text;
    chatC.appendChild(d);
    chatC.scrollTop = chatC.scrollHeight;
  }
  function appendUser(text) {
    const d = document.createElement("div");
    d.classList.add("message","user");
    d.textContent = text;
    chatC.appendChild(d);
    chatC.scrollTop = chatC.scrollHeight;
  }

  // 모드 전환
  function activateCounselingMode() {
    isCounseling = true;
    qCont.classList.add("hidden");
    msgIn.disabled = sendB.disabled = false;
  }
  function deactivateCounselingMode() {
    isCounseling = isCounselingRequest = false;
    qCont.classList.remove("hidden");
    msgIn.disabled = sendB.disabled = true;
    if (stompClient) { stompClient.disconnect(); stompClient = null; }
  }
  function closeChat() {
    currentRoomId = null;
    deactivateCounselingMode();
  }
});
