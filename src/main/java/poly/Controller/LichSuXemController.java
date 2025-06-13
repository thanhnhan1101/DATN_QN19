package poly.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import poly.entity.LichSuXem;
import poly.entity.NguoiDung;
import poly.service.LichSuXemService;
import poly.service.DanhMucService;

@Controller
public class LichSuXemController {
    
    @Autowired
    private LichSuXemService lichSuXemService;

    @Autowired
    private DanhMucService danhMucService;

    // Hiển thị trang lịch sử xem bài
    @GetMapping("/lich-su-xem")
    public String viewHistory(Model model, HttpSession session) {
        NguoiDung user = (NguoiDung) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        // Thêm danh mục cho navigation
        model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
        
        // Lấy lịch sử xem bài của người dùng
        List<LichSuXem> lichSuXem = lichSuXemService.findByNguoiDungId(user.getMaNguoiDung());
        model.addAttribute("lichSuXem", lichSuXem);
        
        return "history";
    }

    // API xóa một lịch sử xem bài
    @DeleteMapping("/api/lich-su-xem/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteHistory(@PathVariable Long id, HttpSession session) {
        NguoiDung user = (NguoiDung) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        LichSuXem lichSuXem = lichSuXemService.findById(id);
        if (lichSuXem != null && lichSuXem.getNguoiDung().getMaNguoiDung().equals(user.getMaNguoiDung())) {
            lichSuXemService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).body("Forbidden");
    }

    // API xóa tất cả lịch sử xem bài
    @DeleteMapping("/api/lich-su-xem/clear")
    @ResponseBody
    public ResponseEntity<?> clearHistory(HttpSession session) {
        NguoiDung user = (NguoiDung) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        List<LichSuXem> lichSuXem = lichSuXemService.findByNguoiDungId(user.getMaNguoiDung());
        for (LichSuXem lsx : lichSuXem) {
            lichSuXemService.deleteById(lsx.getMaLichSu());
        }
        return ResponseEntity.ok().build();
    }

    // API lưu lịch sử xem bài
    @PostMapping("/api/lich-su-xem")
    @ResponseBody
    public ResponseEntity<LichSuXem> saveLichSuXem(@RequestBody LichSuXem lichSuXem) {
        LichSuXem savedLichSuXem = lichSuXemService.save(lichSuXem);
        return ResponseEntity.ok(savedLichSuXem);
    }
} 