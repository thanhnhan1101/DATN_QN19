package poly.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import poly.entity.NguoiDung;
import poly.service.DanhMucService;
import poly.service.NguoiDungService;



@Controller
@RequestMapping("/admin/nguoidung")
public class NguoiDungController {

    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private DanhMucService danhMucService; // Thêm DanhMucService

    @Autowired
    private HttpSession session;

    @GetMapping("")
    public String list(Model model) {
        // Thêm danh mục cho navigation
        model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
        
        // Existing code
        model.addAttribute("nguoiDungs", nguoiDungService.findAll());
        model.addAttribute("nguoiDung", new NguoiDung());
        return "Admin/nguoidung";
    }

    @GetMapping("/reset")
    public String reset() {
        return "redirect:/admin/nguoidung";
    }

    @PostMapping("")
    public String save(@ModelAttribute NguoiDung nguoiDung, RedirectAttributes ra) {
        try {
            nguoiDungService.save(nguoiDung);
            ra.addFlashAttribute("message", "Lưu người dùng thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/nguoidung";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        // Thêm danh mục cho navigation
        model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
        
        nguoiDungService.findById(id).ifPresent(nguoiDung -> {
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("nguoiDungs", nguoiDungService.findAll());
        });
        return "Admin/nguoidung";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            NguoiDung currentUser = (NguoiDung) session.getAttribute("user");
            if ("admin".equals(currentUser.getVaiTro())) {
                nguoiDungService.deleteById(id);
                ra.addFlashAttribute("message", "Xóa người dùng thành công!");
            } else {
                ra.addFlashAttribute("error", "Bạn không có quyền thực hiện thao tác này!");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/nguoidung";
    }

}