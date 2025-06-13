package poly.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.entity.TuongTac;
import poly.entity.NguoiDung;
import poly.entity.BaiViet;
import poly.service.TuongTacService;
import poly.service.BaiVietService;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/tuongtac")
public class TuongTacController {

    @Autowired
    private TuongTacService tuongTacService;

    @Autowired
    private BaiVietService baiVietService;

    @PostMapping("/like/{baiVietId}")
    public ResponseEntity<?> likePost(@PathVariable Integer baiVietId, HttpSession session) {
        NguoiDung user = (NguoiDung) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        BaiViet baiViet = baiVietService.findById(baiVietId);
        if (baiViet == null) {
            return ResponseEntity.notFound().build();
        }

        // Check if user already disliked
        List<TuongTac> existingDislike = tuongTacService.findByNguoiDungId(user.getMaNguoiDung())
            .stream()
            .filter(t -> t.getBaiViet().getMaBaiViet().equals(baiVietId) && "dislike".equals(t.getLoaiTuongTac()))
            .toList();

        if (!existingDislike.isEmpty()) {
            tuongTacService.deleteById(existingDislike.get(0).getMaTuongTac());
        }

        // Check if user already liked
        List<TuongTac> existingLike = tuongTacService.findByNguoiDungId(user.getMaNguoiDung())
            .stream()
            .filter(t -> t.getBaiViet().getMaBaiViet().equals(baiVietId) && "like".equals(t.getLoaiTuongTac()))
            .toList();

        if (!existingLike.isEmpty()) {
            tuongTacService.deleteById(existingLike.get(0).getMaTuongTac());
            return ResponseEntity.ok(Map.of("action", "unliked"));
        }

        // Create new like
        TuongTac tuongTac = new TuongTac();
        tuongTac.setNguoiDung(user);
        tuongTac.setBaiViet(baiViet);
        tuongTac.setLoaiTuongTac("like");
        tuongTacService.save(tuongTac);

        return ResponseEntity.ok(Map.of("action", "liked"));
    }

    @PostMapping("/dislike/{baiVietId}")
    public ResponseEntity<?> dislikePost(@PathVariable Integer baiVietId, HttpSession session) {
        NguoiDung user = (NguoiDung) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        BaiViet baiViet = baiVietService.findById(baiVietId);
        if (baiViet == null) {
            return ResponseEntity.notFound().build();
        }

        // Check if user already liked
        List<TuongTac> existingLike = tuongTacService.findByNguoiDungId(user.getMaNguoiDung())
            .stream()
            .filter(t -> t.getBaiViet().getMaBaiViet().equals(baiVietId) && "like".equals(t.getLoaiTuongTac()))
            .toList();

        if (!existingLike.isEmpty()) {
            tuongTacService.deleteById(existingLike.get(0).getMaTuongTac());
        }

        // Check if user already disliked
        List<TuongTac> existingDislike = tuongTacService.findByNguoiDungId(user.getMaNguoiDung())
            .stream()
            .filter(t -> t.getBaiViet().getMaBaiViet().equals(baiVietId) && "dislike".equals(t.getLoaiTuongTac()))
            .toList();

        if (!existingDislike.isEmpty()) {
            tuongTacService.deleteById(existingDislike.get(0).getMaTuongTac());
            return ResponseEntity.ok(Map.of("action", "undisliked"));
        }

        // Create new dislike
        TuongTac tuongTac = new TuongTac();
        tuongTac.setNguoiDung(user);
        tuongTac.setBaiViet(baiViet);
        tuongTac.setLoaiTuongTac("dislike");
        tuongTacService.save(tuongTac);

        return ResponseEntity.ok(Map.of("action", "disliked"));
    }

    @GetMapping("/counts/{baiVietId}")
    public ResponseEntity<?> getInteractionCounts(@PathVariable Integer baiVietId) {
        List<TuongTac> interactions = tuongTacService.findByBaiVietId(baiVietId);
        
        long likeCount = interactions.stream()
            .filter(t -> "like".equals(t.getLoaiTuongTac()))
            .count();
            
        long dislikeCount = interactions.stream()
            .filter(t -> "dislike".equals(t.getLoaiTuongTac()))
            .count();

        Map<String, Long> counts = new HashMap<>();
        counts.put("likes", likeCount);
        counts.put("dislikes", dislikeCount);

        return ResponseEntity.ok(counts);
    }

    @GetMapping("/user-status/{baiVietId}")
    public ResponseEntity<?> getUserInteractionStatus(@PathVariable Integer baiVietId, HttpSession session) {
        NguoiDung user = (NguoiDung) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        List<TuongTac> userInteractions = tuongTacService.findByNguoiDungId(user.getMaNguoiDung())
            .stream()
            .filter(t -> t.getBaiViet().getMaBaiViet().equals(baiVietId))
            .toList();

        String status = "none";
        if (!userInteractions.isEmpty()) {
            status = userInteractions.get(0).getLoaiTuongTac();
        }

        return ResponseEntity.ok(Map.of("status", status));
    }
} 