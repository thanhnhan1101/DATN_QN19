package poly.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import poly.entity.DanhMuc;
import poly.service.DanhMucService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/admin/danhmuc")
public class DanhMucController {
    @Autowired
    private DanhMucService danhMucService;

    @GetMapping
    public String list(@RequestParam(required = false) Integer edit, Model model) {
        if (edit != null) {
            DanhMuc editCategory = danhMucService.findById(edit);
            model.addAttribute("danhmuc", editCategory);
        } else {
            model.addAttribute("danhmuc", new DanhMuc());
        }
        
        List<DanhMuc> danhmucs = danhMucService.getAllDanhMuc();
        model.addAttribute("danhmucs", danhmucs);
        model.addAttribute("totalCategories", danhMucService.count());

        return "Admin/danhmuc";
    }

    @PostMapping("/api/save")
    @ResponseBody
    public Map<String, Object> save(@ModelAttribute DanhMuc danhMuc, Model model) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate dữ liệu
            if (danhMuc.getTenDanhMuc() == null || danhMuc.getTenDanhMuc().trim().isEmpty()) {
                throw new RuntimeException("Tên danh mục không được để trống");
            }
            if (danhMuc.getDuongDan() == null || danhMuc.getDuongDan().trim().isEmpty()) {
                throw new RuntimeException("Đường dẫn không được để trống");
            }
            
            // Lưu danh mục
            DanhMuc saved = danhMucService.save(danhMuc);
            
            response.put("success", true);
            response.put("message", "Lưu danh mục thành công!");
            response.put("data", saved);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/api/list")
    @ResponseBody
    public List<DanhMuc> getList() {
        return danhMucService.getAllDanhMuc();
    }

    @DeleteMapping("/api/delete/{id}")
    @ResponseBody
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            danhMucService.deleteById(id);
            response.put("success", true);
            response.put("message", "Xóa danh mục thành công!");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public DanhMuc getDanhMuc(@PathVariable Integer id) {
        return danhMucService.findById(id);
    }
}