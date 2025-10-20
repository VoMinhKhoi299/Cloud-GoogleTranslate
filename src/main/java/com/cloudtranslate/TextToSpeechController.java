package com.cloudtranslate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.OutputStream;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;

// Servlet này CHỈ xử lý đường dẫn "/textToSpeech"
@WebServlet("/textToSpeech")
public class TextToSpeechController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Gọi hàm xử lý logic chính
        handleTextToSpeech(request, response);
    }

    /**
     * Xử lý yêu cầu POST từ JavaScript (fetch) để tạo âm thanh.
     */
    private void handleTextToSpeech(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // Lấy văn bản và ngôn ngữ từ yêu cầu
            String text = request.getParameter("text");
            String languageCode = request.getParameter("language");

            // Kiểm tra đầu vào
            if (text == null || text.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                // Gửi lỗi về dạng JSON (vì JavaScript đang mong đợi)
                response.getWriter().write("{\"error\": \"Văn bản không được để trống\"}");
                return;
            }

            // Gọi hàm gọi Google API
            byte[] audioContent = synthesizeSpeech(text, languageCode);

            // Trả về file âm thanh MP3
            response.setContentType("audio/mpeg");
            response.setHeader("Content-Disposition", "inline; filename=\"speech.mp3\"");
            response.setContentLength(audioContent.length);

            OutputStream out = response.getOutputStream();
            out.write(audioContent);
            out.flush();

        } catch (Exception e) {
            // Gửi lỗi về dạng JSON nếu API Google hoặc quá trình khác thất bại
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Gọi API Google Text-to-Speech để chuyển văn bản thành mảng byte âm thanh.
     */
    private byte[] synthesizeSpeech(String text, String languageCode) throws Exception {
        // Sử dụng try-with-resources để tự động đóng client
        try (TextToSpeechClient client = TextToSpeechClient.create()) {

            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(text)
                    .build();

            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode(languageCode)
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL) // Chọn giọng trung tính
                    .build();

            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3) // Định dạng âm thanh
                    .setSpeakingRate(1.0) // Tốc độ đọc (1.0 là bình thường)
                    .setPitch(0.0) // Cao độ giọng (0.0 là bình thường)
                    .build();

            // Gửi yêu cầu
            SynthesizeSpeechResponse response = client.synthesizeSpeech(
                    input, voice, audioConfig);

            // Lấy nội dung âm thanh
            ByteString audioContents = response.getAudioContent();
            return audioContents.toByteArray();
        }
    }
}
