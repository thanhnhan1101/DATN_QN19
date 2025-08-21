package poly.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.service.YeuThichService;
import poly.entity.NguoiDung;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/yeuthich")
public class YeuThichController {

    @Autowired
    private YeuThichService yeuThichService;

    // API thích bài viết
    @PostMapping("/like/{baiVietId}")
    public ResponseEntity<Map<String, Object>> likeBaiViet(@PathVariable Long baiVietId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        // Kiểm tra đăng nhập
        NguoiDung user = (NguoiDung) session.getAttribute("user");
        if (user == null) {
            response.put("success", false);
            response.put("message", "Vui lòng đăng nhập để thực hiện chức năng này");
            return ResponseEntity.ok(response);
        }

        // Thực hiện like
        boolean success = yeuThichService.likeBaiViet(user.getMaNguoiDung(), baiVietId);

        if (success) {
            response.put("success", true);
            response.put("message", "Đã thích bài viết");
            response.put("liked", true);
            response.put("likeCount", yeuThichService.countLikesByBaiViet(baiVietId));
        } else {
            response.put("success", false);
            response.put("message", "Đã thích bài viết này trước đó");
            response.put("liked", true);
            response.put("likeCount", yeuThichService.countLikesByBaiViet(baiVietId));
        }

        return ResponseEntity.ok(response);
    }

    // API bỏ thích bài viết
    @PostMapping("/unlike/{baiVietId}")
    public ResponseEntity<Map<String, Object>> unlikeBaiViet(@PathVariable Long baiVietId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        // Kiểm tra đăng nhập
        NguoiDung user = (NguoiDung) session.getAttribute("user");
        if (user == null) {
            response.put("success", false);
            response.put("message", "Vui lòng đăng nhập để thực hiện chức năng này");
            return ResponseEntity.ok(response);
        }

        // Thực hiện unlike
        boolean success = yeuThichService.unlikeBaiViet(user.getMaNguoiDung(), baiVietId);

        if (success) {
            response.put("success", true);
            response.put("message", "Đã bỏ thích bài viết");
            response.put("liked", false);
            response.put("likeCount", yeuThichService.countLikesByBaiViet(baiVietId));
        } else {
            response.put("success", false);
            response.put("message", "Không thể bỏ thích bài viết");
            response.put("liked", true);
            response.put("likeCount", yeuThichService.countLikesByBaiViet(baiVietId));
        }

        return ResponseEntity.ok(response);
    }

    // API kiểm tra trạng thái like
    @GetMapping("/check/{baiVietId}")
    public ResponseEntity<Map<String, Object>> checkLikeStatus(@PathVariable Long baiVietId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        // Kiểm tra đăng nhập
        NguoiDung user = (NguoiDung) session.getAttribute("user");
        if (user == null) {
            response.put("liked", false);
            response.put("likeCount", yeuThichService.countLikesByBaiViet(baiVietId));
            return ResponseEntity.ok(response);
        }

        // Kiểm tra trạng thái like
        boolean isLiked = yeuThichService.isLiked(user.getMaNguoiDung(), baiVietId);
        long likeCount = yeuThichService.countLikesByBaiViet(baiVietId);

        response.put("liked", isLiked);
        response.put("likeCount", likeCount);

        return ResponseEntity.ok(response);
    }

    // API toggle like (thích/bỏ thích)
    @PostMapping("/toggle/{baiVietId}")
    public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable Long baiVietId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            NguoiDung user = (NguoiDung) session.getAttribute("user");
            if (user == null) {
                response.put("success", false);
                response.put("message", "Vui lòng đăng nhập để yêu thích bài viết");
                return ResponseEntity.ok(response);
            }

            boolean isLiked = yeuThichService.toggleLike(user.getMaNguoiDung(), baiVietId);
            long likeCount = yeuThichService.countLikesByBaiViet(baiVietId);

            response.put("success", true);
            response.put("liked", isLiked);
            response.put("likeCount", likeCount);
            response.put("message", isLiked ? "Đã thêm vào yêu thích" : "Đã bỏ yêu thích");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}

