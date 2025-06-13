package poly.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import poly.entity.BaiViet;
import poly.entity.NguoiDung;
import poly.service.BaiVietService;
import poly.service.DanhMucService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;

@Controller
@RequestMapping("/admin/baiviet")
public class BaiVietController {

    @Autowired
    private BaiVietService baiVietService;

    @Autowired
    private DanhMucService danhMucService;

  
    
    // ✅ Trang danh sách + form
    @GetMapping
    public String hienThi(Model model, HttpSession session,
                         @RequestParam(value = "keyword", required = false) String keyword,
                         @RequestParam(value = "maDanhMuc", required = false) Integer maDanhMuc,
                         @RequestParam(value = "trangThai", required = false) String trangThai) {
        NguoiDung user = (NguoiDung) session.getAttribute("user");
        if (user == null) return "redirect:/auth/login";

        if ("reporter".equals(user.getVaiTro())) {
            // Chỉ lấy bài viết của phóng viên này
            model.addAttribute("baiViets", baiVietService.findByTacGia(user));
        } else {
            // Admin: lấy tất cả hoặc theo filter
            if (keyword != null && !keyword.isEmpty()) {
                model.addAttribute("baiViets", baiVietService.findByTieuDeContaining(keyword));
            } else if (maDanhMuc != null) {
                model.addAttribute("baiViets", baiVietService.findByDanhMuc_MaDanhMuc(maDanhMuc));
            } else if (trangThai != null && !trangThai.isEmpty()) {
                model.addAttribute("baiViets", baiVietService.findByTrangThai(trangThai));
            } else {
                model.addAttribute("baiViets", baiVietService.getAllBaiViet());
            }
        }

        model.addAttribute("baiviet", new BaiViet());
        model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
       
        
        return "admin/baiviet";
    }

    // ✅ Thêm hoặc cập nhật bài viết
    @PostMapping
    public String save(@ModelAttribute("baiviet") BaiViet baiViet,
                      @RequestParam("imageFile") MultipartFile file,
                      @RequestParam(value = "danhMuc.maDanhMuc", required = false) Integer danhMucId,
                      HttpSession session,
                      RedirectAttributes redirect) {
        try {
            // Lấy user từ session
            NguoiDung user = (NguoiDung) session.getAttribute("user");
            if (user == null) {
                redirect.addFlashAttribute("message", "Bạn cần đăng nhập!");
                redirect.addFlashAttribute("messageType", "error");
                return "redirect:/auth/login";
            }
            // Gán tác giả cho bài viết
            baiViet.setTacGia(user);

            // Nếu là phóng viên, trạng thái mặc định là "Chờ duyệt"
            if ("reporter".equals(user.getVaiTro())) {
                baiViet.setTrangThai("Chờ duyệt");
            }

            // Generate slug before saving
            if (baiViet.getTieuDe() == null || baiViet.getTieuDe().isEmpty()) {
                throw new RuntimeException("Tiêu đề không được để trống");
            }
            
            // Only generate new slug for new articles
            if (baiViet.getMaBaiViet() == null) {
                String slug = createSlug(baiViet.getTieuDe());
                baiViet.setDuongDan(slug);
            } else {
                // For updates, get the existing article
                BaiViet existingBaiViet = baiVietService.findById(baiViet.getMaBaiViet());
                if (existingBaiViet != null) {
                    // Keep existing duongDan
                    baiViet.setDuongDan(existingBaiViet.getDuongDan());
                    // Keep existing image if no new one uploaded
                    if (file.isEmpty()) {
                        baiViet.setHinhAnh(existingBaiViet.getHinhAnh());
                    }
                }
            }

            // Handle new image upload
            if (!file.isEmpty()) {
                byte[] bytes = file.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                baiViet.setHinhAnh(base64Image);
            }

            // Set category
            if (danhMucId != null) {
                baiViet.setDanhMuc(danhMucService.findById(danhMucId));
            }

           

            // Set dates
            LocalDateTime now = LocalDateTime.now();
            if (baiViet.getMaBaiViet() == null) {
                baiViet.setNgayTao(now);
                baiViet.setNgayDang(now);
                baiViet.setLuotXem(0);
            } else {
                baiViet.setNgaySua(now);
            }

            baiVietService.save(baiViet, file);
            redirect.addFlashAttribute("message", "Lưu bài viết thành công!");
            redirect.addFlashAttribute("messageType", "success");

        } catch (Exception e) {
            redirect.addFlashAttribute("message", "Lỗi lưu bài viết: " + e.getMessage());
            redirect.addFlashAttribute("messageType", "error");
        }

        return "redirect:/admin/baiviet";
    }

    private String createSlug(String title) {
        if (title == null || title.isEmpty()) {
            return "bai-viet-" + System.currentTimeMillis();
        }
        
        String slug = title.toLowerCase()
                .replaceAll("[áàảãạâấầẩẫậăắằẳẵặ]", "a")
                .replaceAll("[éèẻẽẹêếềểễệ]", "e")
                .replaceAll("[íìỉĩị]", "i")
                .replaceAll("[óòỏõọôốồổỗộơớờởỡợ]", "o")
                .replaceAll("[úùủũụưứừửữự]", "u")
                .replaceAll("[ýỳỷỹỵ]", "y")
                .replaceAll("[đ]", "d")
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
        
        // Thêm timestamp để đảm bảo unique
        return slug + "-" + System.currentTimeMillis();
    }

    // ✅ Sửa bài viết
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        BaiViet baiViet = baiVietService.findById(id);
        if (baiViet != null) {
            model.addAttribute("baiviet", baiViet);
        } else {
            model.addAttribute("baiviet", new BaiViet());
        }
        model.addAttribute("baiViets", baiVietService.getAllBaiViet());
        model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
       
        return "admin/baiviet";
    }

    // ✅ Xoá bài viết
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirect) {
        baiVietService.deleteById(id);
        redirect.addFlashAttribute("message", "Đã xoá bài viết!");
        redirect.addFlashAttribute("messageType", "success");
        return "redirect:/admin/baiviet";
    }

    @GetMapping("/toggle/{id}")
    public String toggleVisibility(@PathVariable("id") Integer id, HttpSession session, RedirectAttributes redirect) {
        NguoiDung user = (NguoiDung) session.getAttribute("user");
        BaiViet baiViet = baiVietService.findById(id);
        if (baiViet != null && user != null) {
            if ("admin".equals(user.getVaiTro())) {
                // Admin: ẩn/hiện ngay
                baiViet.setTrangThai(baiViet.getTrangThai().equals("Ẩn") ? "Đã đăng" : "Ẩn");
                baiVietService.save(baiViet);
                String message = baiViet.getTrangThai().equals("Ẩn") ? "Đã ẩn bài viết!" : "Đã hiện bài viết!";
                redirect.addFlashAttribute("message", message);
                redirect.addFlashAttribute("messageType", "success");
            } else if ("reporter".equals(user.getVaiTro()) && baiViet.getTacGia().getMaNguoiDung().equals(user.getMaNguoiDung())) {
                // Phóng viên: gửi yêu cầu chờ ẩn
                baiViet.setTrangThai("Chờ ẩn");
                baiVietService.save(baiViet);
                redirect.addFlashAttribute("message", "Yêu cầu ẩn bài viết đã gửi, chờ quản trị viên phê duyệt!");
                redirect.addFlashAttribute("messageType", "success");
            } else {
                redirect.addFlashAttribute("message", "Bạn không có quyền thao tác bài viết này!");
                redirect.addFlashAttribute("messageType", "error");
            }
        }
        return "redirect:/admin/baiviet";
    }

    @GetMapping("/approve/{id}")
    public String approve(@PathVariable("id") Integer id, RedirectAttributes redirect) {
        BaiViet baiViet = baiVietService.findById(id);
        if (baiViet != null) {
            baiViet.setTrangThai("Đã đăng");
            baiVietService.save(baiViet);
            redirect.addFlashAttribute("message", "Bài viết đã được duyệt!");
            redirect.addFlashAttribute("messageType", "success");
        }
        return "redirect:/admin/baiviet";
    }

    @GetMapping("/reject/{id}")
    public String reject(@PathVariable("id") Integer id, RedirectAttributes redirect) {
        BaiViet baiViet = baiVietService.findById(id);
        if (baiViet != null) {
            baiViet.setTrangThai("Từ chối");
            baiVietService.save(baiViet);
            redirect.addFlashAttribute("message", "Bài viết đã bị từ chối!");
            redirect.addFlashAttribute("messageType", "error");
        }
        return "redirect:/admin/baiviet";
    }

    @GetMapping("/approve-hide/{id}")
    public String approveHide(@PathVariable("id") Integer id, RedirectAttributes redirect) {
        BaiViet baiViet = baiVietService.findById(id);
        if (baiViet != null) {
            baiViet.setTrangThai("Ẩn");
            baiVietService.save(baiViet);
            redirect.addFlashAttribute("message", "Bài viết đã được ẩn!");
            redirect.addFlashAttribute("messageType", "success");
        }
        return "redirect:/admin/baiviet";
    }
}