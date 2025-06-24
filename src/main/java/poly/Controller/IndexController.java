package poly.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import poly.entity.BaiViet;
import poly.entity.DanhMuc;
import poly.service.BaiVietService;
import poly.service.DanhMucService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    @Autowired
    private DanhMucService danhMucService;

    @Autowired
    private BaiVietService baiVietService;

    @GetMapping("/")
    public String home(Model model) {
        // Thêm danh mục cho navigation
        model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());

        // Lấy danh sách bài viết đã đăng
        List<BaiViet> allPosts = baiVietService.getLatestPublishedPosts(10); // Lấy 4 bài viết mới nhất
        model.addAttribute("allPosts", allPosts); // Thêm dòng này
        
        if (!allPosts.isEmpty()) {
            // Bài viết chính
            model.addAttribute("mainPost", allPosts.get(0));
            
            // Bài viết phụ lớn
            if (allPosts.size() > 1) {
                model.addAttribute("secondPost", allPosts.get(1));
            }
            
            // Hai bài viết nhỏ
            if (allPosts.size() > 2) {
                List<BaiViet> smallPosts = allPosts.subList(2, Math.min(4, allPosts.size()));
                model.addAttribute("smallPosts", smallPosts);
            }
        }

        // Get top viewed posts
        model.addAttribute("topPosts", baiVietService.getMostViewedPosts(5));
        
        // Get categories and their posts
        List<DanhMuc> categories = danhMucService.getAllDanhMuc();
        model.addAttribute("categories", categories);

 
        
        // Get posts for each category with trangThai = "Đã đăng"
        Map<Integer, List<BaiViet>> categoryPosts = new HashMap<>();
        for (DanhMuc category : categories) {
            List<BaiViet> posts = baiVietService.getLatestPostsByCategory(
                category.getMaDanhMuc(), 
                "Đã đăng",  // Thêm trạng thái
                3
            );
            categoryPosts.put(category.getMaDanhMuc(), posts);
        }
        model.addAttribute("categoryPosts", categoryPosts);
        
        return "index";
    }
}