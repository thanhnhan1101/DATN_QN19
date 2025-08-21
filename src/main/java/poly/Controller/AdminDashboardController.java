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
        
        // Data cho tab người dùng
        model.addAttribute("nguoiDungs", nguoiDungRepository.findAll());
        model.addAttribute("nguoiDung", new NguoiDung()); // for form

        return "admin/dashboard";
    }

   
}
