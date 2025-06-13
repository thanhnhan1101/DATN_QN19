package poly.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import poly.entity.NguoiDung;
import poly.service.DanhMucService;
import poly.service.NguoiDungService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private DanhMucService danhMucService;

    @Autowired
    private NguoiDungService nguoiDungService;

    @GetMapping("/profile")
    public String userProfile(HttpSession session, Model model) {
        NguoiDung user = (NguoiDung) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        model.addAttribute("danhmucs", danhMucService.getAllDanhMuc());
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam("soDienThoai") String soDienThoai,
                              @RequestParam("diaChi") String diaChi,
                              @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
                              HttpSession session,
                              RedirectAttributes ra) {
        try {
            NguoiDung user = (NguoiDung) session.getAttribute("user");
            if (user == null) {
                ra.addFlashAttribute("error", "Vui lòng đăng nhập để cập nhật thông tin!");
                return "redirect:/";
            }

            // Cập nhật thông tin
            user.setSoDienThoai(soDienThoai);
            user.setDiaChi(diaChi);

            // Xử lý ảnh đại diện nếu có
            if (avatarFile != null && !avatarFile.isEmpty()) {
                String base64Image = nguoiDungService.compressAndEncodeImage(avatarFile);
                user.setAnhDaiDien(base64Image);
            }

            // Lưu thông tin
            nguoiDungService.save(user);
            
            // Cập nhật session
            session.setAttribute("user", user);
            
            ra.addFlashAttribute("message", "Cập nhật thông tin thành công!");
            return "redirect:/user/profile";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi khi cập nhật thông tin: " + e.getMessage());
            return "redirect:/user/profile";
        }
    }

    @GetMapping("/profile/update")
    public String redirectProfile() {
        return "redirect:/user/profile";
    }
}