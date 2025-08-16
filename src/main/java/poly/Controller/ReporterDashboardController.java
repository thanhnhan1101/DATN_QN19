package poly.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    @PostMapping("/baiviet/save")
    public String saveBaiViet(@ModelAttribute BaiViet baiViet,
                             @RequestParam(value = "imageFile1", required = false) MultipartFile imageFile1,
                             @RequestParam(value = "imageFile2", required = false) MultipartFile imageFile2,
                             @RequestParam(value = "imageFile3", required = false) MultipartFile imageFile3,
                             HttpSession session) {
        
        NguoiDung reporter = (NguoiDung) session.getAttribute("user");
        baiViet.setTacGia(reporter);
        
        // Xử lý hình ảnh
        try {
            if (imageFile1 != null && !imageFile1.isEmpty()) {
                String base64 = java.util.Base64.getEncoder().encodeToString(imageFile1.getBytes());
                baiViet.setDuongDanAnh1(base64);
            }
            // Tương tự cho ảnh 2 và 3
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        baiVietService.save(baiViet);
        return "redirect:/reporter/dashboard";
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
        
        return "redirect:/reporter/dashboard";
    }

    @PostMapping("/baiviet/delete/{id}")
    public String deleteBaiViet(@PathVariable Long id, HttpSession session) {
        NguoiDung reporter = (NguoiDung) session.getAttribute("user");
        BaiViet baiViet = baiVietService.findById(id).orElse(null);
        
        if (baiViet != null && baiViet.getTacGia().equals(reporter)) {
            baiVietService.deleteById(id);
        }
        
        return "redirect:/reporter/dashboard";
    }

    @PostMapping("/baiviet/submit/{id}")
    public String submitBaiViet(@PathVariable Long id, HttpSession session) {
        NguoiDung reporter = (NguoiDung) session.getAttribute("user");
        BaiViet baiViet = baiVietService.findById(id).orElse(null);
        
        if (baiViet != null && baiViet.getTacGia().equals(reporter)) {
            baiViet.setTrangThai("Chờ duyệt");
            baiVietService.save(baiViet);
        }
        
        return "redirect:/reporter/dashboard";
    }
}