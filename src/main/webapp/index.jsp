<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dịch Văn Bản Đa Ngôn Ngữ</title>
    <link rel="stylesheet" href="main.css">
</head>
<body>
<div class="container">
    <div class="header">
        <h1> Project CLOUD: DỊCH VĂN BẢN ĐA NGÔN NGỮ</h1>
        <p>Hỗ trợ 30+ ngôn ngữ - Dịch văn bản</p>
    </div>

    <form action="translate" method="post">
        <div class="language-bar">
            <div class="language-selector">
                <span class="lang-flag">Ngôn ngữ nguồn: </span>
                <select name="source" id="sourceLang">
                    <option value="vi" ${sourceLang == 'vi' ? 'selected' : ''}>Tiếng Việt</option>
                    <option value="en" ${sourceLang == 'en' ? 'selected' : ''}>English</option>
                    <option value="zh-CN" ${sourceLang == 'zh-CN' ? 'selected' : ''}>中文 (Chinese)</option>
                    <option value="ja" ${sourceLang == 'ja' ? 'selected' : ''}>日本語 (Japanese)</option>
                    <option value="ko" ${sourceLang == 'ko' ? 'selected' : ''}>한국어 (Korean)</option>
                    <option value="es" ${sourceLang == 'es' ? 'selected' : ''}>Español</option>
                    <option value="fr" ${sourceLang == 'fr' ? 'selected' : ''}>Français</option>
                    <option value="de" ${sourceLang == 'de' ? 'selected' : ''}>Deutsch</option>
                    <option value="ru" ${sourceLang == 'ru' ? 'selected' : ''}>Русский</option>
                    <option value="th" ${sourceLang == 'th' ? 'selected' : ''}>ภาษาไทย</option>
                    <option value="ar" ${sourceLang == 'ar' ? 'selected' : ''}>العربية</option>
                    <option value="pt" ${sourceLang == 'pt' ? 'selected' : ''}>Português</option>
                </select>
            </div>

            <div class="language-selector">
                <span class="lang-flag">Ngôn ngữ đích: </span>
                <select name="target" id="targetLang">
                    <option value="en" ${targetLang == 'en' ? 'selected' : ''}>English</option>
                    <option value="vi" ${targetLang == 'vi' ? 'selected' : ''}>Tiếng Việt</option>
                    <option value="zh-CN" ${targetLang == 'zh-CN' ? 'selected' : ''}>中文 (Chinese)</option>
                    <option value="ja" ${targetLang == 'ja' ? 'selected' : ''}>日本語 (Japanese)</option>
                    <option value="ko" ${targetLang == 'ko' ? 'selected' : ''}>한국어 (Korean)</option>
                    <option value="es" ${targetLang == 'es' ? 'selected' : ''}>Español</option>
                    <option value="fr" ${targetLang == 'fr' ? 'selected' : ''}>Français</option>
                    <option value="de" ${targetLang == 'de' ? 'selected' : ''}>Deutsch</option>
                    <option value="ru" ${targetLang == 'ru' ? 'selected' : ''}>Русский</option>
                    <option value="th" ${targetLang == 'th' ? 'selected' : ''}>ภาษาไทย</option>
                    <option value="ar" ${targetLang == 'ar' ? 'selected' : ''}>العربية</option>
                    <option value="pt" ${targetLang == 'pt' ? 'selected' : ''}>Português</option>
                </select>
            </div>
        </div>

        <div class="translation-section">
            <div class="text-panel">
                <div class="panel-header">
                    <div class="panel-title">Văn bản gốc</div>
                </div>
                <textarea name="text" id="sourceText" placeholder="Nhập nội dung cần dịch tại đây...">${sourceText}</textarea>
                <div class="char-count" id="charCount">0 / 5000 ký tự</div>
            </div>

            <button class="swap-button" id="swapBtn" type="button">⇄</button>

            <div class="text-panel">
                <div class="panel-header">
                    <div class="panel-title">Bản dịch</div>
                </div>
                <textarea id="targetText" placeholder="Kết quả dịch sẽ xuất hiện ở đây..." readonly>${translatedText}</textarea>
                <div class="char-count">Sẵn sàng dịch</div>
            </div>
        </div>

        <div class="top-actions">
            <button class="btn btn-translate" id="translateBtn" type="submit">
                <span>Dịch ngay</span>
            </button>
        </div>
    </form>

    <c:if test="${not empty error}">
        <p style="color: red; margin-top: 10px;">${error}</p>
    </c:if>

</div>

<script>
    // Tính số ký tự nhập vào
    const textArea = document.getElementById('sourceText');
    const charCount = document.getElementById('charCount');
    textArea.addEventListener('input', () => {
        charCount.textContent = `${textArea.value.length} / 5000 ký tự`;
    });

    // Nút hoán đổi ngôn ngữ
    document.getElementById('swapBtn').addEventListener('click', () => {
        const src = document.getElementById('sourceLang');
        const tgt = document.getElementById('targetLang');
        const srcText = document.getElementById('sourceText');
        const tgtText = document.getElementById('targetText');

        // Hoán đổi ngôn ngữ
        const tempLang = src.value;
        src.value = tgt.value;
        tgt.value = tempLang;

        // Hoán đổi nội dung giữa hai ô text
        const tempText = srcText.value;
        srcText.value = tgtText.value;
        tgtText.value = tempText;

        // Cập nhật lại bộ đếm ký tự
        const charCount = document.getElementById('charCount');
        charCount.textContent = `${srcText.value.length} / 5000 ký tự`;
    });
</script>

</body>
</html>
