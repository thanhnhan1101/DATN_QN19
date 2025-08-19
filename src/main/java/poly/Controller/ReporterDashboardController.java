package poly.Controller;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import poly.entity.BaiViet;
import poly.entity.NguoiDung;
import poly.service.BaiVietService;
import poly.service.DanhMucService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Controller
@RequestMapping("/reporter")
public class ReporterDashboardController {
    @Autowired
    private BaiVietService baiVietService;
    
    @Autowired
    private DanhMucService danhMucService;

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
        
        // Form data for new article
        model.addAttribute("baiviet", new BaiViet());
        model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
        
        // Get paginated articles
        Page<BaiViet> baiViets = baiVietService.findByTacGia(
            reporter,
            PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "ngayTao"))
        );
        model.addAttribute("baiViets", baiViets);
        
        // Add statistics for the sidebar
        model.addAttribute("totalPosts", baiVietService.countByTacGia(reporter));
        model.addAttribute("pendingPosts", baiVietService.countByTacGiaAndTrangThai(reporter, "Chờ duyệt"));
        model.addAttribute("totalViews", baiVietService.sumLuotXemByTacGia(reporter));
        
        return "Reporter/articles";
    }

    @PostMapping("/baiviet/save")
    public String saveBaiViet(@ModelAttribute BaiViet baiViet,
                             @RequestParam(value = "imageFile1", required = true) MultipartFile imageFile1,
                             @RequestParam(value = "imageFile2", required = false) MultipartFile imageFile2,
                             @RequestParam(value = "imageFile3", required = false) MultipartFile imageFile3,
                             HttpSession session,
                             RedirectAttributes ra) {
        try {
            NguoiDung reporter = (NguoiDung) session.getAttribute("user");
            baiViet.setTacGia(reporter);

            // Handle main image (required)
            if (imageFile1.isEmpty() && baiViet.getMaBaiViet() == null) {
                ra.addFlashAttribute("error", "Ảnh chính là bắt buộc!");
                return "redirect:/reporter/dashboard";
            }

            // Process main image if provided
            if (!imageFile1.isEmpty()) {
                String base64Image1 = Base64.getEncoder().encodeToString(imageFile1.getBytes());
                baiViet.setDuongDanAnh1(base64Image1);
            }

            // Process optional image 2
            if (imageFile2 != null && !imageFile2.isEmpty()) {
                String base64Image2 = Base64.getEncoder().encodeToString(imageFile2.getBytes());
                baiViet.setDuongDanAnh2(base64Image2);
            }

            // Process optional image 3
            if (imageFile3 != null && !imageFile3.isEmpty()) {
                String base64Image3 = Base64.getEncoder().encodeToString(imageFile3.getBytes());
                baiViet.setDuongDanAnh3(base64Image3);
            }

            // Set dates
            if (baiViet.getMaBaiViet() == null) {
                baiViet.setNgayTao(LocalDateTime.now());
                baiViet.setTrangThai("Nháp");
            }
            baiViet.setNgayCapNhat(LocalDateTime.now());

            // Save the article
            baiVietService.save(baiViet);
            ra.addFlashAttribute("message", "Lưu bài viết thành công!");

        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi khi lưu bài viết: " + e.getMessage());
        }

        return "redirect:/reporter/articles";  // Changed from dashboard to articles
    }

    @GetMapping("/baiviet/edit/{id}")
    public String editBaiViet(@PathVariable Long id, Model model, HttpSession session) {
        NguoiDung reporter = (NguoiDung) session.getAttribute("user");
        BaiViet baiViet = baiVietService.findById(id).orElse(null);
        
        if (baiViet != null && baiViet.getTacGia().equals(reporter)) {
            model.addAttribute("baiviet", baiViet);
            model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
            return "reporter/edit";
        }
        
        return "redirect:/reporter/articles";  // Changed from dashboard to articles
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
}