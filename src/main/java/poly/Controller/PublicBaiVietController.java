package poly.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import poly.entity.BaiViet;
import poly.entity.LichSuXem;
import poly.entity.NguoiDung;
import poly.entity.TuongTac;
import poly.service.BaiVietService;
import poly.service.DanhMucService;
import poly.service.LichSuXemService;
import poly.service.TuongTacService;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class PublicBaiVietController {
    private static final Logger logger = LoggerFactory.getLogger(PublicBaiVietController.class);

    @Autowired
    private BaiVietService baiVietService;

    @Autowired
    private DanhMucService danhMucService;

    @Autowired
    private LichSuXemService lichSuXemService;

    @Autowired
    private TuongTacService tuongTacService;

    @GetMapping("/baiviet/{duongDan}")
    public String viewBaiViet(@PathVariable("duongDan") String duongDan, Model model, HttpSession session) {
        try {
            logger.info("Viewing article with path: {}", duongDan);
            
            // Add categories for navigation
            model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());

            BaiViet baiViet = baiVietService.findByDuongDan(duongDan);
            if (baiViet == null) {
                logger.warn("Article not found: {}", duongDan);
                model.addAttribute("error", "Không tìm thấy bài viết");
                return "error";
            }

            // Kiểm tra trạng thái bài viết
            if (!"Đã đăng".equals(baiViet.getTrangThai())) {
                logger.warn("Article not published: {}", duongDan);
                model.addAttribute("error", "Bài viết chưa được duyệt");
                return "error";
            }

            // Get like/dislike counts
            List<TuongTac> interactions = tuongTacService.findByBaiVietId(baiViet.getMaBaiViet());
            long likeCount = interactions.stream()
                .filter(t -> "like".equals(t.getLoaiTuongTac()))
                .count();
            long dislikeCount = interactions.stream()
                .filter(t -> "dislike".equals(t.getLoaiTuongTac()))
                .count();

            model.addAttribute("likeCount", likeCount);
            model.addAttribute("dislikeCount", dislikeCount);

            // Increment view count
            baiViet.setLuotXem(baiViet.getLuotXem() + 1);
            baiVietService.save(baiViet);
            logger.info("Incremented view count for article: {}", baiViet.getMaBaiViet());

            // Lưu lịch sử xem bài nếu người dùng đã đăng nhập
            NguoiDung user = (NguoiDung) session.getAttribute("user");
            logger.info("User from session: {}", user != null ? user.getEmail() : "Not logged in");
            
            if (user != null) {
                try {
                    LichSuXem lichSuXem = new LichSuXem();
                    lichSuXem.setNguoiDung(user);
                    lichSuXem.setBaiViet(baiViet);
                    LichSuXem savedLichSuXem = lichSuXemService.save(lichSuXem);
                    logger.info("Saved view history for user {} and article {}", user.getEmail(), baiViet.getMaBaiViet());
                } catch (Exception e) {
                    logger.error("Error saving view history: ", e);
                }
            } else {
                logger.info("User not logged in, skipping view history save");
            }

            // Get related posts
            List<BaiViet> relatedPosts = baiVietService.findByDanhMuc_MaDanhMuc(
                baiViet.getDanhMuc().getMaDanhMuc()
            ).stream()
             .filter(p -> !p.getMaBaiViet().equals(baiViet.getMaBaiViet()))
             .filter(p -> "Đã đăng".equals(p.getTrangThai()))
             .limit(5)
             .collect(Collectors.toList());

            model.addAttribute("baiViet", baiViet);
            model.addAttribute("relatedPosts", relatedPosts);
            
            return "post-detail";
        } catch (Exception e) {
            logger.error("Error in viewBaiViet: ", e);
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "error";
        }
    }
}