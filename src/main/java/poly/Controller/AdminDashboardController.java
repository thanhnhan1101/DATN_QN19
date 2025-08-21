package poly.Controller;

import java.util.List;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import poly.repository.BaiVietRepository;
import poly.repository.DanhMucRepository;
import poly.repository.NguoiDungRepository;
import poly.entity.BaiViet;
import poly.entity.NguoiDung;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private BaiVietRepository baiVietRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private DanhMucRepository danhMucRepository;

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

        // Thống kê bài viết theo tháng
        List<Object[]> monthlyStats = baiVietRepository.getMonthlyPostStats();
        model.addAttribute("monthlyStats", monthlyStats);

        // Thống kê theo danh mục
        List<Object[]> categoryStats = baiVietRepository.getPostsByCategory();
        model.addAttribute("categoryStats", categoryStats);

        // Thống kê tương tác (lượt xem, yêu thích) trong 7 ngày gần nhất
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        List<Object[]> interactionStats = baiVietRepository.getInteractionStats(startDate);
        model.addAttribute("interactionStats", interactionStats);

        // Thống kê hoạt động phóng viên
        List<Object[]> reporterStats = baiVietRepository.getReporterActivityStats();
        model.addAttribute("reporterStats", reporterStats);

        return "admin/dashboard";
    }

   
}
