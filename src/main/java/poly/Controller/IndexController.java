package poly.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import poly.service.DanhMucService;
import poly.entity.BaiViet;
import poly.entity.DanhMuc;
import poly.entity.NguoiDung;
import poly.service.BaiVietService;
import poly.service.YeuThichService;
import poly.service.ThongBaoService;
import jakarta.servlet.http.HttpSession;

@Controller
public class IndexController {
    @Autowired
    private DanhMucService danhMucService;

    @Autowired 
    private BaiVietService baiVietService;

    @Autowired
    private YeuThichService yeuThichService;
    
    @Autowired
    private ThongBaoService thongBaoService;

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

    @GetMapping("/bai-viet/{id}")
    public String baiVietDetail(@PathVariable Long id, Model model, HttpSession session) {
        try {
            BaiViet baiViet = baiVietService.findById(id).orElse(null);
            
            if (baiViet == null || !"Đã xuất bản".equals(baiViet.getTrangThai())) {
                return "redirect:/";
            }
            
            // Tăng lượt xem
            baiViet.setLuotXem(baiViet.getLuotXem() + 1);
            baiVietService.save(baiViet);
            
            // Lấy các bài viết liên quan (cùng danh mục)
            List<BaiViet> relatedPosts = baiVietService.getAll().stream()
                .filter(post -> "Đã xuất bản".equals(post.getTrangThai()))
                .filter(post -> !post.getMaBaiViet().equals(id))
                .filter(post -> post.getDanhMuc() != null && 
                               baiViet.getDanhMuc() != null && 
                               post.getDanhMuc().getMaDanhMuc().equals(baiViet.getDanhMuc().getMaDanhMuc()))
                .limit(3)
                .collect(Collectors.toList());
            
            // Kiểm tra trạng thái like
            NguoiDung user = (NguoiDung) session.getAttribute("user");
            boolean isLiked = false;
            if (user != null) {
                isLiked = yeuThichService.isLiked(user.getMaNguoiDung(), id);
            }
            long likeCount = yeuThichService.countLikesByBaiViet(id);
            
            model.addAttribute("baiViet", baiViet);
            model.addAttribute("relatedPosts", relatedPosts);
            model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
            model.addAttribute("isLiked", isLiked);
            model.addAttribute("likeCount", likeCount);
            
            return "baiviet-detail";
            
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/danh-muc/{identifier}")
    public String danhMucDetail(@PathVariable String identifier, Model model) {
        try {
            DanhMuc danhMuc = null;
            Integer danhMucId = null;
            
            // Thử parse thành số trước
            try {
                danhMucId = Integer.parseInt(identifier);
                danhMuc = danhMucService.findById(danhMucId);
            } catch (NumberFormatException e) {
                // Nếu không phải số, tìm theo duongDan
                danhMuc = danhMucService.getAllDanhMuc().stream()
                    .filter(dm -> dm.getDuongDan() != null && 
                                 dm.getDuongDan().equals(identifier))
                    .findFirst()
                    .orElse(null);
                
                if (danhMuc != null) {
                    danhMucId = danhMuc.getMaDanhMuc();
                } else {
                    return "redirect:/";
                }
            }
            
            if (danhMuc == null) {
                return "redirect:/";
            }
            
            // Lấy bài viết theo danh mục
            final Integer finalDanhMucId = danhMucId;
            List<BaiViet> baiViets = baiVietService.getAll().stream()
                .filter(post -> "Đã xuất bản".equals(post.getTrangThai()))
                .filter(post -> post.getDanhMuc() != null && 
                               post.getDanhMuc().getMaDanhMuc().equals(finalDanhMucId))
                .collect(Collectors.toList());
            
            model.addAttribute("danhMuc", danhMuc);
            model.addAttribute("baiViets", baiViets);
            model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
            model.addAttribute("hasArticles", !baiViets.isEmpty());
            
            return "danhmuc-detail";
            
        } catch (Exception e) {
            return "redirect:/";
        }
    }
    
    // Helper method để tạo data URL cho ảnh
    public String getImageDataUrl(String base64Data) {
        if (base64Data == null || base64Data.trim().isEmpty()) {
            return "";
        }
        return "data:image/jpeg;base64," + base64Data;
    }
    
    @GetMapping("/image/{id}")
    public String getImageDataUrl(@PathVariable Long id, Model model) {
        try {
            BaiViet baiViet = baiVietService.findById(id).orElse(null);
            if (baiViet != null && baiViet.getDuongDanAnh1() != null) {
                return "redirect:data:image/jpeg;base64," + baiViet.getDuongDanAnh1();
            }
        } catch (Exception e) {
            // Ignore
        }
        return "redirect:/";
    }
    
    // Thêm vào các controller có sử dụng Nav
    @ModelAttribute
    public void addCommonAttributes(Model model, HttpSession session) {
        NguoiDung user = (NguoiDung) session.getAttribute("user");
        if (user != null) {
            long unreadCount = thongBaoService.demThongBaoChuaDoc(user.getMaNguoiDung());
            model.addAttribute("unreadNotifications", unreadCount);
        }
    }
}