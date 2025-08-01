package poly.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import poly.service.DanhMucService;
import poly.entity.BaiViet;
import poly.service.BaiVietService;

@Controller
public class IndexController {
    @Autowired
    private DanhMucService danhMucService;

    @Autowired 
    private BaiVietService baiVietService;

    @GetMapping("/")
    public String index(Model model) {
        // Lấy danh sách bài viết đã xuất bản
        List<BaiViet> allPosts = baiVietService.getAll().stream()
            .filter(post -> "Đã xuất bản".equals(post.getTrangThai()))
            .collect(Collectors.toList());

        if (allPosts.isEmpty()) {
            allPosts = new ArrayList<>(); // Đảm bảo không null
        }
        
        model.addAttribute("baiViets", allPosts);
        model.addAttribute("hasArticles", !allPosts.isEmpty());
        model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
        return "index";
    }
}