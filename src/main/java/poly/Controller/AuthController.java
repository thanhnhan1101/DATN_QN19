package poly.Controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import poly.entity.NguoiDung;
import poly.service.NguoiDungService;
import java.util.Optional;
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
    public String login(@RequestParam String email, 
                       @RequestParam String matKhau,
                       @RequestParam(defaultValue = "false") boolean rememberMe,
                       HttpServletResponse response,
                       HttpSession session,
                       RedirectAttributes ra) {
        try {
            Optional<NguoiDung> userOpt = nguoiDungService.login(email, matKhau);
            if (userOpt.isPresent()) {
                NguoiDung user = userOpt.get();
                session.setAttribute("user", user);
                
                if (rememberMe) {
                    Cookie cookie = new Cookie("rememberMe", user.getEmail());
                    cookie.setMaxAge(1800);
                    cookie.setPath("/");
                    cookie.setHttpOnly(true);
                    response.addCookie(cookie);
                }
                
                return "redirect:/";
            }
            ra.addFlashAttribute("error", "Email hoặc mật khẩu không đúng!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, 
                        HttpServletResponse response,
                        HttpSession session) {
        session.invalidate();
        
        // Delete remember-me cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("rememberMe".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }
        }
        
        return "redirect:/";
    }
}