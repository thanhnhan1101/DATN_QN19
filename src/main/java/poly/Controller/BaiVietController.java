package poly.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;
import poly.entity.BaiViet;
import poly.entity.NguoiDung;
import poly.service.BaiVietService;
import poly.service.DanhMucService;

import java.util.Base64;
import java.util.List;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/baiviet")
public class BaiVietController {

    @Autowired
    private BaiVietService baiVietService;

    @Autowired
    private DanhMucService danhMucService;

    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) String trangThai,
                       @RequestParam(required = false) Integer maDanhMuc,
                       Model model) {
        List<BaiViet> baiViets = baiVietService.getAll(); // hoặc search nếu có filter
        model.addAttribute("baiViets", baiViets);

        // Truyền thêm các biến khác nếu cần
        model.addAttribute("baiviet", new BaiViet());
        model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());

        return "Admin/baiviet";
    }

    @PostMapping
    public String save(@ModelAttribute BaiViet baiViet,
                       @RequestParam("imageFile1") MultipartFile imageFile1,
                       @RequestParam("imageFile2") MultipartFile imageFile2,
                       @RequestParam("imageFile3") MultipartFile imageFile3,
                       HttpSession session) {
        try {
            // Kiểm tra và gán trạng thái mặc định nếu chưa có
            if (baiViet.getTrangThai() == null || baiViet.getTrangThai().isEmpty()) {
                baiViet.setTrangThai("Nháp"); // Hoặc giá trị mặc định phù hợp
            }

            // Xử lý ngày tạo/cập nhật
            if (baiViet.getMaBaiViet() == null) {
                baiViet.setNgayTao(LocalDateTime.now());
            }
            baiViet.setNgayCapNhat(LocalDateTime.now());

            // Xử lý lượt xem
            if (baiViet.getLuotXem() == null) {
                baiViet.setLuotXem(0);
            }

            // Xử lý ảnh như cũ
            if (!imageFile1.isEmpty()) {
                baiViet.setDuongDanAnh1(Base64.getEncoder().encodeToString(imageFile1.getBytes()));
            }
            if (!imageFile2.isEmpty()) {
                baiViet.setDuongDanAnh2(Base64.getEncoder().encodeToString(imageFile2.getBytes()));
            }
            if (!imageFile3.isEmpty()) {
                baiViet.setDuongDanAnh3(Base64.getEncoder().encodeToString(imageFile3.getBytes()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Lấy người dùng từ session
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("user");
        baiViet.setTacGia(nguoiDung);

        baiVietService.save(baiViet);
        return "redirect:/admin/baiviet?message=Lưu bài viết thành công";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        BaiViet baiViet = baiVietService.findById(id).orElse(null);
        model.addAttribute("baiviet", baiViet != null ? baiViet : new BaiViet());
        model.addAttribute("baiViets", baiVietService.getAll());
        model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
        return "Admin/baiviet";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        baiVietService.deleteById(id);
        return "redirect:/admin/baiviet";
    }

    // Duyệt bài viết
    @GetMapping("/approve/{id}")
    public String approve(@PathVariable Long id) {
        BaiViet baiViet = baiVietService.findById(id).orElse(null);
        if (baiViet != null) {
            baiViet.setTrangThai("Đã đăng");
            baiVietService.save(baiViet);
        }
        return "redirect:/admin/baiviet";
    }

    @GetMapping("/reject/{id}")
    public String reject(@PathVariable Long id) {
        BaiViet baiViet = baiVietService.findById(id).orElse(null);
        if (baiViet != null) {
            baiViet.setTrangThai("Từ chối");
            baiVietService.save(baiViet);
        }
        return "redirect:/admin/baiviet";
    }

    // Lịch sử chỉnh sửa: cần thêm bảng/logic lưu lịch sử nếu muốn

    @GetMapping("/toggle/{id}")
    public String toggleVisibility(@PathVariable Long id) {
        baiVietService.toggleVisibility(id);
        return "redirect:/admin/baiviet";
    }
}