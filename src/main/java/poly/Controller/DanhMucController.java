package poly.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import poly.entity.DanhMuc;
import poly.service.DanhMucService;

@Controller
@RequestMapping("/admin/danhmuc")
public class DanhMucController {
    @Autowired
    private DanhMucService danhMucService;
    
    @GetMapping
    public String list(Model model) {
        model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
        model.addAttribute("danhmuc", new DanhMuc());
        return "Admin/danhmuc"; 
    }
    
    @PostMapping
    public String save(@ModelAttribute DanhMuc danhMuc, RedirectAttributes redirectAttributes) {
        try {
            boolean isEdit = danhMuc.getMaDanhMuc() != null;
            danhMucService.save(danhMuc);
            redirectAttributes.addFlashAttribute("message", 
                isEdit ? "Cập nhật danh mục thành công!" : "Thêm danh mục thành công!");
            redirectAttributes.addFlashAttribute("messageType", "success"); // Màu xanh
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error"); // Màu đỏ
        }
        return "redirect:/admin/danhmuc";
    }
    
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("danhmuc", danhMucService.findById(id));
        model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
        return "Admin/danhmuc"; 
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            danhMucService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Xóa danh mục thành công!");
            redirectAttributes.addFlashAttribute("messageType", "error"); // Màu đỏ cho xóa
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/admin/danhmuc";
    }
}