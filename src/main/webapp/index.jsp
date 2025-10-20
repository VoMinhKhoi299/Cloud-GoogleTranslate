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
                <span class="lang-flag">Ngôn ngữ: </span>
                <select name="source" id="sourceLang">
                    <option value="vi">Tiếng Việt</option>
                    <option value="en">English</option>
                    <option value="zh-CN">中文 (Chinese)</option>
                    <option value="ja">日本語 (Japanese)</option>
                    <option value="ko">한국어 (Korean)</option>
                    <option value="es">Español</option>
                    <option value="fr">Français</option>
                    <option value="de">Deutsch</option>
                    <option value="ru">Русский</option>
                    <option value="th">ภาษาไทย</option>
                    <option value="ar">العربية</option>
                    <option value="pt">Português</option>
                </select>
            </div>

            <div class="language-selector">
                <span class="lang-flag">Ngôn ngữ: </span>
                <select name="target" id="targetLang">
                    <option value="en">English</option>
                    <option value="vi">Tiếng Việt</option>
                    <option value="zh-CN">中文 (Chinese)</option>
                    <option value="ja">日本語 (Japanese)</option>
                    <option value="ko">한국어 (Korean)</option>
                    <option value="es">Español</option>
                    <option value="fr">Français</option>
                    <option value="de">Deutsch</option>
                    <option value="ru">Русский</option>
                    <option value="th">ภาษาไทย</option>
                    <option value="ar">العربية</option>
                    <option value="pt">Português</option>
                </select>
            </div>
        </div>
        <div class="translation-section">
            <div class="text-panel">
                <div class="panel-header">
                    <div class="panel-icon"></div>
                    <div class="panel-title">Văn bản gốc</div>
                </div>
                <textarea name="text" id="sourceText" placeholder="Nhập nội dung cần dịch tại đây..."></textarea>
                <div class="char-count" id="charCount">0 / 5000 ký tự</div>
            </div>

            <button class="swap-button" id="swapBtn">⇄</button>

            <div class="text-panel">
                <div class="panel-header">
                    <div class="panel-icon">✨</div>
                    <div class="panel-title">Bản dịch</div>
                </div>
                <textarea id="targetText" placeholder="Kết quả dịch sẽ xuất hiện ở đây..." readonly>
                    ${translatedText}
                </textarea>
                <div class="char-count">Sẵn sàng dịch</div>
            </div>
        </div>

        <div class="top-actions">
            <button class="btn btn-translate" id="translateBtn" type="submit">
                <span>Dịch ngay</span>
            </button>
        </div>
    </form>
</div>

</body>
</html>