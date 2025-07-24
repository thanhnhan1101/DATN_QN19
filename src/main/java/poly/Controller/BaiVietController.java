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
        // Nếu là cập nhật, lấy bài viết cũ để giữ lại ảnh
        if (baiViet.getMaBaiViet() != null) {
            BaiViet old = baiVietService.findById(baiViet.getMaBaiViet()).orElse(null);
            if (old != null) {
                if (imageFile1.isEmpty()) baiViet.setDuongDanAnh1(old.getDuongDanAnh1());
                if (imageFile2.isEmpty()) baiViet.setDuongDanAnh2(old.getDuongDanAnh2());
                if (imageFile3.isEmpty()) baiViet.setDuongDanAnh3(old.getDuongDanAnh3());
            }
        }

        try {
            if (!imageFile1.isEmpty()) {
                String base64 = Base64.getEncoder().encodeToString(imageFile1.getBytes());
                baiViet.setDuongDanAnh1(base64);
            }
            if (!imageFile2.isEmpty()) {
                String base64 = Base64.getEncoder().encodeToString(imageFile2.getBytes());
                baiViet.setDuongDanAnh2(base64);
            }
            if (!imageFile3.isEmpty()) {
                String base64 = Base64.getEncoder().encodeToString(imageFile3.getBytes());
                baiViet.setDuongDanAnh3(base64);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Lấy người dùng từ session
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("user");
        baiViet.setTacGia(nguoiDung);

        baiVietService.save(baiViet);
        return "redirect:/admin/baiviet";
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
}