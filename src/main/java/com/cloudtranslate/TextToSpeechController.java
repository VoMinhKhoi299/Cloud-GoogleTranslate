package com.cloudtranslate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.OutputStream;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;

@WebServlet("/textToSpeech")
public class TextToSpeechController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        
        try {
            String text = request.getParameter("text");
            String languageCode = request.getParameter("language");

            if (text == null || text.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Văn bản không được để trống\"}");
                return;
            }

            // Tạo audio từ text
            byte[] audioContent = synthesizeSpeech(text, languageCode);

            // Trả về file audio
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

    private byte[] synthesizeSpeech(String text, String languageCode) throws Exception {
        try (TextToSpeechClient client = TextToSpeechClient.create()) {
            
            // Cấu hình input text
            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(text)
                    .build();

            // Chọn giọng nói dựa vào ngôn ngữ
            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode(languageCode)
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                    .build();

            // Cấu hình audio output
            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .setSpeakingRate(1.0)
                    .setPitch(0.0)
                    .build();

            // Gọi API Text-to-Speech
            SynthesizeSpeechResponse response = client.synthesizeSpeech(
                    input, voice, audioConfig);

            // Trả về audio content
            ByteString audioContents = response.getAudioContent();
            return audioContents.toByteArray();
        }
    }
}