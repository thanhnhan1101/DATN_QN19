package poly.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import poly.entity.BaiViet;
import poly.entity.NguoiDung;
import poly.service.BaiVietService;
import poly.service.EmailService;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/share")
public class ShareController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private BaiVietService baiVietService;

    @PostMapping("/email")
    @ResponseBody
    public ResponseEntity<?> shareViaEmail(
            @RequestParam Integer articleId,
            @RequestParam String email,
            HttpSession session) {
        
        try {
            // Lấy thông tin bài viết
            BaiViet baiViet = baiVietService.findById(articleId);
            if (baiViet == null) {
                return ResponseEntity.badRequest().body("Không tìm thấy bài viết");
            }

            // Lấy thông tin người chia sẻ
            NguoiDung currentUser = (NguoiDung) session.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.badRequest().body("Vui lòng đăng nhập để chia sẻ");
            }

            // Tạo URL bài viết
            String articleUrl = "http://localhost:8080/bai-viet/" + baiViet.getDuongDan();

            // Gửi email
            emailService.sendSharedArticle(
                email,
                baiViet.getTieuDe(),
                articleUrl,
                currentUser.getHoTen()
            );

            Map<String, String> response = new HashMap<>();
            response.put("message", "Đã gửi email chia sẻ thành công!");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Lỗi khi gửi email: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 