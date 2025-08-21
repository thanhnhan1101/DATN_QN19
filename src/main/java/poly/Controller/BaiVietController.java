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
import poly.service.ThongBaoService;
import poly.service.YeuThichService;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/baiviet")
public class BaiVietController {

    @Autowired
    private BaiVietService baiVietService;

    @Autowired 
    private DanhMucService danhMucService;

    @Autowired
    private YeuThichService yeuThichService;

    @Autowired
    private ThongBaoService thongBaoService;

    @GetMapping
    public String list(@RequestParam(required = false) Long edit, Model model) {
        if (edit != null) {
            // Load bài viết cần edit
            BaiViet editPost = baiVietService.findById(edit).orElse(new BaiViet());
            model.addAttribute("baiviet", editPost);
        } else {
            // Form thêm mới
            model.addAttribute("baiviet", new BaiViet());
        }
        
        // Add other required attributes
        List<BaiViet> baiViets = baiVietService.getAll();
        model.addAttribute("baiViets", baiViets);
        model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
        model.addAttribute("totalPosts", baiVietService.count());
        model.addAttribute("pendingPosts", baiVietService.countByTrangThai("Chờ duyệt"));

        return "Admin/baiviet";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BaiViet baiViet,
                      @RequestParam("imageFile1") MultipartFile imageFile1,
                      @RequestParam(value = "imageFile2", required = false) MultipartFile imageFile2,
                      @RequestParam(value = "imageFile3", required = false) MultipartFile imageFile3,
                      HttpSession session,
                      RedirectAttributes ra) {
        try {
            // Set author
            NguoiDung nguoiDung = (NguoiDung) session.getAttribute("user");
            baiViet.setTacGia(nguoiDung);

            // Handle main image (required)
            if (!imageFile1.isEmpty()) {
                baiViet.setDuongDanAnh1(Base64.getEncoder().encodeToString(imageFile1.getBytes()));
            } else if (baiViet.getMaBaiViet() == null) {
                ra.addFlashAttribute("message", "Vui lòng chọn ảnh chính");
                ra.addFlashAttribute("messageType", "danger");
                return "redirect:/admin/baiviet";
            }

            // Handle optional images
            if (imageFile2 != null && !imageFile2.isEmpty()) {
                baiViet.setDuongDanAnh2(Base64.getEncoder().encodeToString(imageFile2.getBytes()));
            }
            if (imageFile3 != null && !imageFile3.isEmpty()) {
                baiViet.setDuongDanAnh3(Base64.getEncoder().encodeToString(imageFile3.getBytes()));
            }

            // Set dates
            if (baiViet.getMaBaiViet() == null) {
                baiViet.setNgayTao(LocalDateTime.now());
            }
            baiViet.setNgayCapNhat(LocalDateTime.now());

            // Set publish date if status is "Đã xuất bản"
            if ("Đã xuất bản".equals(baiViet.getTrangThai())) {
                baiViet.setNgayXuatBan(LocalDateTime.now());
            }

            // Save
            baiVietService.save(baiViet);

            ra.addFlashAttribute("message", "Lưu bài viết thành công!");
            ra.addFlashAttribute("messageType", "success");

        } catch (Exception e) {
            ra.addFlashAttribute("message", "Lỗi: " + e.getMessage());
            ra.addFlashAttribute("messageType", "danger");
        }

        return "redirect:/admin/baiviet";
    }

   

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            baiVietService.deleteById(id);
            ra.addFlashAttribute("message", "Xóa bài viết thành công!");
            ra.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            ra.addFlashAttribute("message", "Lỗi: " + e.getMessage());
            ra.addFlashAttribute("messageType", "danger");
        }
        return "redirect:/admin/baiviet";
    }

    @PostMapping("/approve/{id}")
    @ResponseBody
    public Map<String, Object> approveBaiViet(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            BaiViet baiViet = baiVietService.findById(id).orElse(null);
            if (baiViet != null) {
                baiViet.setTrangThai("Đã xuất bản");
                baiViet.setNgayXuatBan(LocalDateTime.now());
                baiVietService.save(baiViet);
                
                // Gửi thông báo cho tác giả
                thongBaoService.taoThongBao(
                    "Bài viết đã được duyệt",
                    "Bài viết \"" + baiViet.getTieuDe() + "\" của bạn đã được duyệt và xuất bản",
                    baiViet.getTacGia()
                );

                response.put("success", true);
                response.put("message", "Bài viết đã được duyệt và xuất bản!");
            } else {
                response.put("success", false);
                response.put("message", "Không tìm thấy bài viết!");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi duyệt bài viết: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/stats")
    @ResponseBody
    public Map<String, Long> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", baiVietService.count());
        stats.put("pending", baiVietService.countByTrangThai("Chờ duyệt"));
        return stats;
    }

    @GetMapping("/showReject/{id}")
    public String showRejectForm(@PathVariable Long id, Model model) {
        BaiViet baiViet = baiVietService.findById(id).orElse(null);
        if (baiViet == null) {
            return "redirect:/admin/baiviet";
        }
        model.addAttribute("baiViet", baiViet);
        return "Admin/baiviet/reject";
    }

    @PostMapping("/reject/{id}")
    @ResponseBody
    public Map<String, Object> rejectBaiViet(@PathVariable Long id, @RequestParam String lyDo) {
        Map<String, Object> response = new HashMap<>();
        try {
            BaiViet baiViet = baiVietService.findById(id).orElse(null);
            if (baiViet != null) {
                baiViet.setTrangThai("Từ chối");
                baiViet.setLyDoTuChoi(lyDo);
                baiVietService.save(baiViet);

                // Gửi thông báo cho tác giả
                thongBaoService.taoThongBao(
                    "Bài viết bị từ chối",
                    "Bài viết \"" + baiViet.getTieuDe() + "\" của bạn đã bị từ chối. Lý do: " + lyDo,
                    baiViet.getTacGia()
                );

                response.put("success", true);
                response.put("message", "Đã từ chối bài viết!");
            } else {
                response.put("success", false);
                response.put("message", "Không tìm thấy bài viết!");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi từ chối bài viết: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        try {
            BaiViet baiViet = baiVietService.findById(id).orElse(null);
            if (baiViet == null) {
                return "redirect:/admin/baiviet";
            }
            model.addAttribute("baiViet", baiViet);
            return "Admin/baiviet-detail";
        } catch (Exception e) {
            return "redirect:/admin/baiviet";
        }
    }

    @GetMapping("/getData/{id}")
    public String getData(@PathVariable Long id, Model model) {
        BaiViet baiViet = baiVietService.findById(id).orElse(new BaiViet());
        model.addAttribute("baiviet", baiViet);
        model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
        return "Admin/fragments/posts :: #editForm";
    }

    @GetMapping("/reset")
    public String reset(Model model) {
        // Tạo form mới
        model.addAttribute("baiviet", new BaiViet());
        model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
        return "Admin/fragments/posts :: #editForm";
    }

    @PostMapping("/toggle-visibility/{id}")
    public String toggleVisibility(@PathVariable Long id, RedirectAttributes ra) {
        try {
            BaiViet baiViet = baiVietService.findById(id).orElse(null);
            if (baiViet != null) {
                // Nếu đang ẩn thì chuyển về trạng thái "Đã xuất bản", ngược lại thì ẩn
                String newStatus = baiViet.getTrangThai().equals("Đã ẩn") ? "Đã xuất bản" : "Đã ẩn";
                baiViet.setTrangThai(newStatus);
                baiVietService.save(baiViet);
                ra.addFlashAttribute("message", 
                    newStatus.equals("Đã ẩn") ? "Đã ẩn bài viết!" : "Đã hiện bài viết!");
                ra.addFlashAttribute("messageType", "success");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("message", "Lỗi: " + e.getMessage());
            ra.addFlashAttribute("messageType", "danger");
        }
        return "redirect:/admin/baiviet";
    }

    @GetMapping("/detail/{id}")
    @ResponseBody
    public BaiViet getDetail(@PathVariable Long id) {
        return baiVietService.findById(id).orElse(null);
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        try {
            BaiViet baiViet = baiVietService.findById(id).orElse(new BaiViet());
            model.addAttribute("baiviet", baiViet);
            model.addAttribute("baiViets", baiVietService.getAll()); // Thêm danh sách bài viết
            model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
            return "Admin/baiviet"; // Sửa lại return về template chính xác
        } catch (Exception e) {
            return "redirect:/admin/baiviet";
        }
    }

    @GetMapping("/bai-viet/{id}")
    public String getBaiVietDetail(@PathVariable Long id, Model model, HttpSession session) {
        BaiViet baiViet = baiVietService.findById(id).orElse(null);
        if (baiViet != null) {
            // Add existing code...
            
            // Add like status
            NguoiDung user = (NguoiDung) session.getAttribute("user");
            boolean isLiked = false;
            if (user != null) {
                isLiked = yeuThichService.isLiked(user.getMaNguoiDung(), id);
            }
            long likeCount = yeuThichService.countLikesByBaiViet(id);
            
            model.addAttribute("isLiked", isLiked);
            model.addAttribute("likeCount", likeCount);
            
            return "baiviet-detail";
        }
        return "redirect:/";
    }
}