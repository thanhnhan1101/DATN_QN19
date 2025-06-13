package poly.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import poly.entity.BaiViet;
import poly.entity.DanhMuc;
import poly.service.BaiVietService;
import poly.service.DanhMucService;

@Controller
@RequestMapping("/danh-muc")
public class CategoryController {
    @Autowired
    private DanhMucService danhMucService;
    
    @Autowired
    private BaiVietService baiVietService;

    @GetMapping("/{duongDan}")
    public String viewCategory(@PathVariable String duongDan, Model model) {
        // Lấy danh mục hiện tại
        DanhMuc danhMuc = danhMucService.findByDuongDan(duongDan);
        if (danhMuc == null) {
            return "redirect:/";
        }

        // Thêm danh mục cho navigation
        model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
        
        // Lấy bài viết đã đăng của danh mục (thay đổi dòng này)
        List<BaiViet> baiViets = baiVietService.getLatestPostsByCategory(
            danhMuc.getMaDanhMuc(), 
            "Đã đăng",
            10  // Số lượng bài viết muốn hiển thị
        );
        
        model.addAttribute("currentDanhMuc", danhMuc);
        model.addAttribute("baiViets", baiViets);
        
        return "category-detail";
    }
}