package com.cloudtranslation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import org.json.JSONObject;
import com.google.cloud.translate.v3.*;

@WebServlet("/translate")
public class TranslateController extends HttpServlet {

    private static final String PROJECT_ID = "khoi-project-475710";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        try {
            String sourceText = request.getParameter("text");
            String sourceLang = request.getParameter("source");
            String targetLang = request.getParameter("target");

            if (sourceText == null || sourceText.isEmpty()) {
                sendError(response, "Vui lòng nhập văn bản cần dịch");
                return;
            }

            // Dịch văn bản bằng Cloud Translation v3
            String translatedText = translateTextV3(sourceText, sourceLang, targetLang);

            // Gửi kết quả về frontend
            JSONObject result = new JSONObject();
            result.put("success", true);
            result.put("translatedText", translatedText);
            result.put("model", "Cloud Translation v3 - NMT");
            response.getWriter().write(result.toString());

        } catch (Exception e) {
            sendError(response, "Lỗi khi dịch văn bản: " + e.getMessage());
        }
    }

    private String translateTextV3(String text, String sourceLang, String targetLang) throws IOException {
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            LocationName parent = LocationName.of(PROJECT_ID, "global");

            TranslateTextRequest request = TranslateTextRequest.newBuilder()
                    .setParent(parent.toString())
                    .setMimeType("text/plain")
                    .setSourceLanguageCode(sourceLang)
                    .setTargetLanguageCode(targetLang)
                    .addContents(text)
                    .build();

            TranslateTextResponse response = client.translateText(request);
            return response.getTranslations(0).getTranslatedText();
        }
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        JSONObject error = new JSONObject();
        error.put("success", false);
        error.put("error", message);
        response.getWriter().write(error.toString());
    }
}
