document.addEventListener("DOMContentLoaded", function () {
    const chatbotIcon = document.createElement("div");
    chatbotIcon.id = "chatbot-icon";
    chatbotIcon.style.position = "fixed";
    chatbotIcon.style.bottom = "20px";
    chatbotIcon.style.right = "20px";
    chatbotIcon.style.width = "60px";
    chatbotIcon.style.height = "60px";
    chatbotIcon.style.background = "purple";
    chatbotIcon.style.borderRadius = "50%";
    chatbotIcon.style.cursor = "pointer";
    chatbotIcon.style.display = "flex";
    chatbotIcon.style.alignItems = "center";
    chatbotIcon.style.justifyContent = "center";
    chatbotIcon.style.color = "white";
    chatbotIcon.innerText = "💬";
    document.body.appendChild(chatbotIcon);

    // 챗봇 팝업 창 열기
    chatbotIcon.addEventListener("click", function () {
        const chatbotWindow = window.open(
            "/chatbot.html",  // 챗봇 UI가 있는 파일
            "Chatbot",
            "width=400,height=500,resizable=no"
        );
        if (!chatbotWindow) {
            alert("팝업이 차단되었습니다. 팝업 차단을 해제해주세요.");
        }
    });
});
