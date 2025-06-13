package poly.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import poly.entity.NguoiDung;
import poly.service.NguoiDungService;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private NguoiDungService nguoiDungService;

    @PostMapping("/register")
    public String register(@ModelAttribute NguoiDung nguoiDung, 
                          @RequestParam String xacNhanMatKhau,
                          RedirectAttributes ra) {
        try {
            // Kiểm tra mật khẩu xác nhận. ĐÂY LÀ THAY ĐỔI CỦA TÔI 1 , THAY ĐÔỔI 2, THAY ĐÔ 3S, THAY ĐỔI 4
            if (!nguoiDung.getMatKhau().equals(xacNhanMatKhau)) {
                ra.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
                return "redirect:/";
            }

            // Kiểm tra email đã tồn tại
            if (nguoiDungService.findByEmail(nguoiDung.getEmail()).isPresent()) {
                ra.addFlashAttribute("error", "Email đã tồn tại!");
                return "redirect:/";
            }

            // Lưu người dùng mới
            nguoiDungService.save(nguoiDung);
            ra.addFlashAttribute("message", "Đăng ký thành công! Vui lòng đăng nhập.");
            return "redirect:/";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi đăng ký: " + e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/login")
    public String loginForm(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,
                            RedirectAttributes ra) {
        Optional<NguoiDung> userOpt = nguoiDungService.login(email, password);
        if (userOpt.isPresent()) {
            session.setAttribute("user", userOpt.get());
            ra.addFlashAttribute("message", "Đăng nhập thành công!");
            return "redirect:/";
        } else {
            ra.addFlashAttribute("error", "Email hoặc mật khẩu không đúng!");
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes ra) {
        session.removeAttribute("user");
        ra.addFlashAttribute("message", "Đăng xuất thành công!");
        return "redirect:/";
    }

} 