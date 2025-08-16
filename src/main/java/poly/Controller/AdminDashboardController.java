package poly.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import poly.repository.BaiVietRepository;
import poly.repository.DanhMucRepository;
import poly.repository.NguoiDungRepository;
import poly.entity.BaiViet;
import poly.entity.DanhMuc;
import poly.entity.NguoiDung;
import poly.service.DanhMucService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private BaiVietRepository baiVietRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Autowired
    private DanhMucService danhMucService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, @ModelAttribute("activeTab") String activeTab) {
        // Thống kê tổng số
        long totalPosts = baiVietRepository.count();
        long totalUsers = nguoiDungRepository.count();
        long totalCategories = danhMucRepository.count();

        // Thêm số liệu thống kê vào model
        model.addAttribute("totalPosts", totalPosts);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalCategories", totalCategories);
        model.addAttribute("totalAds", 0L);

        // Lấy 10 bài viết mới nhất cho overview
        model.addAttribute("recentPosts", 
            baiVietRepository.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "ngayTao"))
            ).getContent()
        );

        // Data cho tab bài viết
        model.addAttribute("baiViets", baiVietRepository.findAll());
        model.addAttribute("baiviet", new BaiViet()); // for form
        
        // Data cho tab danh mục
        model.addAttribute("danhmucs", danhMucRepository.findAll());
        model.addAttribute("danhmuc", new DanhMuc()); // for form
        
        // Data cho tab người dùng
        model.addAttribute("nguoiDungs", nguoiDungRepository.findAll());
        model.addAttribute("nguoiDung", new NguoiDung()); // for form

        // Set active tab from flash attribute
        if (activeTab != null && !activeTab.isEmpty()) {
            model.addAttribute("activeTab", activeTab);
        }
        
        return "admin/dashboard";
    }

    // Thêm mới/Cập nhật danh mục
    @PostMapping("/danhmuc/save")
    public String saveDanhMuc(@ModelAttribute DanhMuc danhMuc, RedirectAttributes ra) {
        try {
            boolean isEdit = danhMuc.getMaDanhMuc() != null;
            danhMucService.save(danhMuc);
            ra.addFlashAttribute("message", isEdit ? "Cập nhật danh mục thành công!" : "Thêm danh mục thành công!");
            ra.addFlashAttribute("messageType", "success");
            ra.addFlashAttribute("activeTab", "categories"); // Đảm bảo tab categories được active
        } catch (Exception e) {
            ra.addFlashAttribute("message", "Có lỗi xảy ra: " + e.getMessage());
            ra.addFlashAttribute("messageType", "error");
            ra.addFlashAttribute("activeTab", "categories");
        }
        return "redirect:/admin/dashboard#categories"; // Thêm #categories vào URL redirect
    }

    // Lấy danh mục để sửa
    @GetMapping("/danhmuc/edit/{id}")
    public String editDanhMuc(@PathVariable Integer id, Model model) {
        try {
            DanhMuc danhMuc = danhMucService.findById(id);
            if (danhMuc == null) {
                return "redirect:/admin/dashboard#categories";
            }
            
            // Thêm data cho view
            model.addAttribute("danhmuc", danhMuc);
            model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
            model.addAttribute("activeTab", "categories");
            
            return "admin/dashboard"; // Bỏ #categories
        } catch (Exception e) {
            return "redirect:/admin/dashboard#categories";
        }
    }

    // Xóa danh mục
    @GetMapping("/danhmuc/delete/{id}")
    public String deleteDanhMuc(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            danhMucService.deleteById(id);
            ra.addFlashAttribute("message", "Xóa danh mục thành công!");
            ra.addFlashAttribute("messageType", "success");
            ra.addFlashAttribute("activeTab", "categories");
        } catch (Exception e) {
            ra.addFlashAttribute("message", "Có lỗi xảy ra: " + e.getMessage());
            ra.addFlashAttribute("messageType", "error");
            ra.addFlashAttribute("activeTab", "categories");
        }
        return "redirect:/admin/dashboard#categories";
    }

    // Lấy danh sách danh mục (cho AJAX refresh)
    @GetMapping("/danhmuc/list")
    @ResponseBody
    public List<DanhMuc> getDanhMucs() {
        return danhMucService.getAllDanhMuc();
    }

    // Add
}
