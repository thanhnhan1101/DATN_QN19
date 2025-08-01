package poly.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import poly.service.BaiVietService;
import poly.service.DanhMucService;
import poly.service.NguoiDungService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BaiVietService baiVietService;

    @Autowired
    private DanhMucService danhMucService;

    @Autowired
    private NguoiDungService nguoiDungService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Thêm thống kê
        model.addAttribute("totalBaiViet", baiVietService.countAll());
        model.addAttribute("totalDanhMuc", danhMucService.countAll());
        model.addAttribute("totalNguoiDung", nguoiDungService.countAll());
        
        // Thêm danh sách bài viết chờ duyệt
        model.addAttribute("pendingPosts", baiVietService.findPendingPosts());
        
        return "Admin/dashboard";
    }
}