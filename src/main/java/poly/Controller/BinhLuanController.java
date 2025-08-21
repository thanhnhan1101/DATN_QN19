package poly.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import poly.entity.BaiViet;
import poly.entity.BinhLuan;
import poly.entity.NguoiDung;
import poly.service.BaiVietService;
import poly.service.BinhLuanService;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/binhluan")
public class BinhLuanController {

    @Autowired
    private BinhLuanService binhLuanService;
    
    @Autowired
    private BaiVietService baiVietService;

    @PostMapping("/add")
    public ResponseEntity<?> addComment(@RequestParam Long maBaiViet, 
                                      @RequestParam String noiDung,
                                      HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            NguoiDung user = (NguoiDung) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false, 
                                "message", "Vui lòng đăng nhập để bình luận!"));
            }

            BaiViet baiViet = baiVietService.findById(maBaiViet).orElse(null);
            if (baiViet == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false, 
                                "message", "Không tìm thấy bài viết!"));
            }

            BinhLuan binhLuan = new BinhLuan();
            binhLuan.setBaiViet(baiViet);
            binhLuan.setNguoiDung(user);
            binhLuan.setNoiDung(noiDung);
            
            binhLuan = binhLuanService.save(binhLuan);

            response.put("success", true);
            response.put("message", "Bình luận đã được gửi!");
            response.put("comment", binhLuan);
            
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("success", false, 
                            "message", e.getMessage()));
        }
    }
}