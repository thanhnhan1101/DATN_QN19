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
        // Lấy danh sách bài viết đã đăng
        List<BaiViet> allPosts = baiVietService.getAll().stream()
            .filter(post -> "Đã đăng".equals(post.getTrangThai()))
            .collect(Collectors.toList());

        // Nếu không có bài viết nào, tạo các bài viết mẫu
        if (allPosts.isEmpty() || allPosts.size() < 4) {
            List<BaiViet> dummyPosts = new ArrayList<>();
            for (int i = allPosts.size(); i < 4; i++) {
                BaiViet dummy = new BaiViet();
                dummy.setTieuDe("Chưa có bài viết");
                dummy.setTomTat("Nội dung đang được cập nhật");
                dummy.setNgayXuatBan(LocalDateTime.now());
                dummyPosts.add(dummy);
            }
            allPosts.addAll(dummyPosts);
        }

        model.addAttribute("baiViets", allPosts);
        model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
        return "index";
    }
}