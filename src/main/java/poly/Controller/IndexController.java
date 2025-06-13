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

        // Chỉ lấy các bài viết có trạng thái "Đã đăng"
        String trangThai = "Đã đăng";
        
        // Get main featured post
        model.addAttribute("mainPost", baiVietService.getLatestPublishedPost());
        
        // Get featured posts
        model.addAttribute("featuredPosts", baiVietService.getLatestPublishedPosts(2));
        
        // Get latest posts
        model.addAttribute("latestPosts", baiVietService.getLatestPublishedPosts(5));
        
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