package poly.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import poly.entity.BaiViet;
import poly.entity.LichSuChinhSua;
import poly.entity.NguoiDung;
import poly.service.BaiVietService;
import poly.service.DanhMucService;
import poly.service.LichSuChinhSuaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

@Controller
@RequestMapping("/reporter")
public class ReporterDashboardController {
    @Autowired
    private BaiVietService baiVietService;
    
    @Autowired
    private DanhMucService danhMucService;

    @Autowired
    private LichSuChinhSuaService lichSuChinhSuaService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        NguoiDung reporter = (NguoiDung) session.getAttribute("user");
        
        // Lấy thống kê
        model.addAttribute("totalPosts", baiVietService.countByTacGia(reporter));
        model.addAttribute("pendingPosts", baiVietService.countByTacGiaAndTrangThai(reporter, "Chờ duyệt"));
        model.addAttribute("totalViews", baiVietService.sumLuotXemByTacGia(reporter));
        
        // Lấy danh sách bài viết của phóng viên
        Page<BaiViet> baiViets = baiVietService.findByTacGia(
            reporter,
            PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "ngayTao"))
        );
        model.addAttribute("baiViets", baiViets);
        
        // Form data
        model.addAttribute("baiviet", new BaiViet());
        model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
        
        return "reporter/dashboard";
    }

    @GetMapping("/articles")
    public String showArticles(Model model, HttpSession session) {
        NguoiDung reporter = (NguoiDung) session.getAttribute("user");
        
        // Form data for new article - always create new instance
        model.addAttribute("baiviet", new BaiViet()); // This ensures empty form
        model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
        
        // Get paginated articles
        Page<BaiViet> baiViets = baiVietService.findByTacGia(
            reporter,
            PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "ngayTao"))
        );
        model.addAttribute("baiViets", baiViets);
        
        return "Reporter/articles";
    }

    @PostMapping("/baiviet/save")
    public String saveBaiViet(@ModelAttribute BaiViet baiViet,
                             @RequestParam(value = "imageFile1", required = false) MultipartFile imageFile1,
                             @RequestParam(value = "imageFile2", required = false) MultipartFile imageFile2,
                             @RequestParam(value = "imageFile3", required = false) MultipartFile imageFile3,
                             HttpSession session,
                             RedirectAttributes ra) {
        try {
            NguoiDung reporter = (NguoiDung) session.getAttribute("user");
            baiViet.setTacGia(reporter);

            // Nếu là cập nhật, lấy thông tin bài viết cũ
            if (baiViet.getMaBaiViet() != null) {
                BaiViet baiVietCu = baiVietService.findById(baiViet.getMaBaiViet()).orElse(null);
                if (baiVietCu != null) {
                    // Tạo object chứa nội dung cũ dưới dạng plain text thay vì JSON
                    String noiDungCu = String.format(
                        "Tiêu đề: %s\n" +
                        "Tóm tắt: %s\n" + 
                        "Nội dung: %s\n" + 
                        "Trạng thái: %s",
                        baiVietCu.getTieuDe(),
                        baiVietCu.getTomTat(),
                        baiVietCu.getNoiDung(),
                        baiVietCu.getTrangThai()
                    );
                    
                    // Lưu lịch sử
                    lichSuChinhSuaService.luuLichSu(baiVietCu, reporter, noiDungCu);

                    // Giữ lại các ảnh cũ nếu không upload ảnh mới
                    if (imageFile1 == null || imageFile1.isEmpty()) {
                        baiViet.setDuongDanAnh1(baiVietCu.getDuongDanAnh1());
                        baiViet.setNoiDungAnh1(baiVietCu.getNoiDungAnh1());
                    }
                    if (imageFile2 == null || imageFile2.isEmpty()) {
                        baiViet.setDuongDanAnh2(baiVietCu.getDuongDanAnh2());
                        baiViet.setNoiDungAnh2(baiVietCu.getNoiDungAnh2());
                    }
                    if (imageFile3 == null || imageFile3.isEmpty()) {
                        baiViet.setDuongDanAnh3(baiVietCu.getDuongDanAnh3());
                        baiViet.setNoiDungAnh3(baiVietCu.getNoiDungAnh3());
                    }

                    // Chuyển về nháp nếu đang chờ duyệt
                    if ("Chờ duyệt".equals(baiVietCu.getTrangThai())) {
                        baiViet.setTrangThai("Nháp");
                    } else {
                        baiViet.setTrangThai(baiVietCu.getTrangThai());
                    }
                }
            } else {
                // Bài viết mới luôn ở trạng thái nháp
                baiViet.setTrangThai("Nháp");
                baiViet.setNgayTao(LocalDateTime.now());
            }

            // Xử lý ảnh mới nếu có
            if (imageFile1 != null && !imageFile1.isEmpty()) {
                baiViet.setDuongDanAnh1(Base64.getEncoder().encodeToString(imageFile1.getBytes()));
            }
            if (imageFile2 != null && !imageFile2.isEmpty()) {
                baiViet.setDuongDanAnh2(Base64.getEncoder().encodeToString(imageFile2.getBytes()));
            }
            if (imageFile3 != null && !imageFile3.isEmpty()) {
                baiViet.setDuongDanAnh3(Base64.getEncoder().encodeToString(imageFile3.getBytes()));
            }

            // Cập nhật ngày sửa
            baiViet.setNgayCapNhat(LocalDateTime.now());

            // Lưu bài viết
            baiVietService.save(baiViet);
            ra.addFlashAttribute("message", "Lưu bài viết thành công!");

        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi khi lưu bài viết: " + e.getMessage());
        }
        return "redirect:/reporter/articles";
    }

    @GetMapping("/baiviet/edit/{id}")
    public String editBaiViet(@PathVariable Long id, Model model, HttpSession session) {
        NguoiDung reporter = (NguoiDung) session.getAttribute("user");
        BaiViet baiViet = baiVietService.findById(id).orElse(null);
        
        if (baiViet != null && baiViet.getTacGia().equals(reporter)) {
            model.addAttribute("baiviet", baiViet);
            model.addAttribute("baiViets", baiVietService.findByTacGia(
                reporter,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "ngayTao"))
            ));
            model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
            // Sửa lại return về articles thay vì edit
            return "Reporter/articles";  
        }
        
        return "redirect:/reporter/articles";
    }

    @PostMapping("/baiviet/delete/{id}")
    public String deleteBaiViet(@PathVariable Long id, HttpSession session) {
        NguoiDung reporter = (NguoiDung) session.getAttribute("user");
        BaiViet baiViet = baiVietService.findById(id).orElse(null);
        
        if (baiViet != null && baiViet.getTacGia().equals(reporter)) {
            baiVietService.deleteById(id);
        }
        
        return "redirect:/reporter/articles";  // Changed from dashboard to articles
    }

    @PostMapping("/baiviet/submit/{id}")
    public String submitBaiViet(@PathVariable Long id, HttpSession session) {
        NguoiDung reporter = (NguoiDung) session.getAttribute("user");
        BaiViet baiViet = baiVietService.findById(id).orElse(null);
        
        if (baiViet != null && baiViet.getTacGia().equals(reporter)) {
            baiViet.setTrangThai("Chờ duyệt");
            baiVietService.save(baiViet);
        }
        
        return "redirect:/reporter/articles";  // Changed from dashboard to articles
    }

    // Add method to get chart data
    @GetMapping("/api/stats")
    @ResponseBody
    public Map<String, Object> getStats(HttpSession session) {
        NguoiDung reporter = (NguoiDung) session.getAttribute("user");
        Map<String, Object> stats = new HashMap<>();
        
        // Chuyển đổi getMaNguoiDung() sang Long
        stats.put("monthlyStats", baiVietService.getMonthlyStatsByAuthor(reporter.getMaNguoiDung().longValue()));
        stats.put("categoryStats", baiVietService.getCategoryStatsByAuthor(reporter.getMaNguoiDung().longValue()));
        
        return stats;
    }

    @GetMapping("/baiviet/{id}/history")
    @ResponseBody
    public List<Map<String, Object>> getBaiVietHistory(@PathVariable Long id) {
        BaiViet baiViet = baiVietService.findById(id).orElse(null);
        if (baiViet != null) {
            List<LichSuChinhSua> lichSu = lichSuChinhSuaService.getLichSuByBaiViet(baiViet);
            return lichSu.stream().map(ls -> {
                Map<String, Object> item = new HashMap<>();
                item.put("ngayChinhSua", ls.getNgayChinhSua().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
                item.put("nguoiSua", ls.getNguoiSua().getHoTen());
                item.put("noiDungCu", ls.getNoiDungCu());
                return item;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @PostMapping("/baiviet/resubmit/{id}")
    @ResponseBody
    public ResponseEntity<?> resubmitArticle(@PathVariable Long id, HttpSession session) {
        try {
            NguoiDung reporter = (NguoiDung) session.getAttribute("user");
            BaiViet baiViet = baiVietService.findById(id).orElse(null);
            
            if (baiViet != null && baiViet.getTacGia().equals(reporter)) {
                baiViet.setTrangThai("Chờ duyệt");
                baiViet.setLyDoTuChoi(null); // Clear rejection reason
                baiViet.setNgayCapNhat(LocalDateTime.now());
                baiVietService.save(baiViet);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}