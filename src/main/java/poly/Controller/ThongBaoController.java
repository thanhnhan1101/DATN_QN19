package poly.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.entity.NguoiDung;
import poly.service.ThongBaoService;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/thongbao")
public class ThongBaoController {

    @Autowired
    private ThongBaoService thongBaoService;

    @GetMapping("/list")
    public ResponseEntity<?> layDanhSachThongBao(HttpSession session) {
        NguoiDung user = (NguoiDung) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.badRequest().body("Chưa đăng nhập");
        }
        
        return ResponseEntity.ok(thongBaoService.layThongBaoNguoiDung(user.getMaNguoiDung()));
    }

    @PostMapping("/mark-all-read")
    public ResponseEntity<?> danhDauTatCaDaDoc(HttpSession session) {
        NguoiDung user = (NguoiDung) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.badRequest().body("Chưa đăng nhập");
        }

        thongBaoService.danhDauTatCaDaDoc(user.getMaNguoiDung());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Đã đánh dấu tất cả là đã đọc");
        response.put("unreadCount", thongBaoService.demThongBaoChuaDoc(user.getMaNguoiDung()));
        
        return ResponseEntity.ok(response);
    }
}