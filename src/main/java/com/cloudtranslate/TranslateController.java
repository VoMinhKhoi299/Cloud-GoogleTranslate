package com.cloudtranslate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.OutputStream;
import org.json.JSONObject;
import com.google.cloud.translate.v3.*;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;

@WebServlet(urlPatterns = {"/translate", "/textToSpeech"})
public class TranslateController extends HttpServlet {

    private static final String PROJECT_ID = "khoi-project-475710";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();
        
        if ("/translate".equals(servletPath)) {
            handleTranslation(request, response);
        } else if ("/textToSpeech".equals(servletPath)) {
            handleTextToSpeech(request, response);
        }
    }

    // Xử lý dịch văn bản
    private void handleTranslation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        try {
            String sourceText = request.getParameter("text");
            String sourceLang = request.getParameter("source");
            String targetLang = request.getParameter("target");

            if (sourceText == null || sourceText.isEmpty()) {
                request.setAttribute("error", "Vui lòng nhập văn bản cần dịch");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                return;
            }

            String translatedText = translateTextV3(sourceText, sourceLang, targetLang);

            request.setAttribute("sourceText", sourceText);
            request.setAttribute("translatedText", translatedText);
            request.setAttribute("sourceLang", sourceLang);
            request.setAttribute("targetLang", targetLang);

            request.getRequestDispatcher("/index.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi dịch văn bản: " + e.getMessage());
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    // Xử lý Text-to-Speech
    private void handleTextToSpeech(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");
        
        try {
            String text = request.getParameter("text");
            String languageCode = request.getParameter("language");

            if (text == null || text.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Văn bản không được để trống\"}");
                return;
            }

            byte[] audioContent = synthesizeSpeech(text, languageCode);

            response.setContentType("audio/mpeg");
            response.setHeader("Content-Disposition", "inline; filename=\"speech.mp3\"");
            response.setContentLength(audioContent.length);

            OutputStream out = response.getOutputStream();
            out.write(audioContent);
            out.flush();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    // Google Cloud Translation API v3
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

    // Google Cloud Text-to-Speech API
    private byte[] synthesizeSpeech(String text, String languageCode) throws Exception {
        try (TextToSpeechClient client = TextToSpeechClient.create()) {
            
            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(text)
                    .build();

            // Chọn giọng nói phù hợp với ngôn ngữ
            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode(languageCode)
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                    .build();

            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .setSpeakingRate(1.0)  // Tốc độ đọc (0.25 - 4.0)
                    .setPitch(0.0)          // Cao độ (-20.0 - 20.0)
                    .build();

            SynthesizeSpeechResponse response = client.synthesizeSpeech(
                    input, voice, audioConfig);

            ByteString audioContents = response.getAudioContent();
            return audioContents.toByteArray();
        }
    }
}