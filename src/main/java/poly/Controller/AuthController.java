package poly.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import poly.entity.LichSuDangNhap;
import poly.entity.NguoiDung;
import poly.service.NguoiDungService;
@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private poly.repository.LichSuDangNhapRepository lichSuDangNhapRepository;

    @PostMapping("/register")
    public String register(@ModelAttribute NguoiDung nguoiDung, 
                          @RequestParam String xacNhanMatKhau,
                          RedirectAttributes ra) {
        try {
            if (!nguoiDung.getMatKhau().equals(xacNhanMatKhau)) {
                ra.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
                return "redirect:/";
            }

            if (nguoiDungService.findByEmail(nguoiDung.getEmail()).isPresent()) {
                ra.addFlashAttribute("error", "Email này đã được sử dụng!");
                return "redirect:/";
            }

            nguoiDungService.save(nguoiDung);
            ra.addFlashAttribute("message", "Đăng ký thành công! Mời bạn đăng nhập.");
            return "redirect:/";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, 
                       @RequestParam String matKhau,
                       @RequestParam(defaultValue = "false") boolean rememberMe,
                       HttpServletResponse response,
                       HttpSession session,
                       RedirectAttributes ra,
                       HttpServletRequest request) {
        try {
            Optional<NguoiDung> userOpt = nguoiDungService.login(email, matKhau);
            if (userOpt.isPresent()) {
                NguoiDung user = userOpt.get();
                session.setAttribute("user", user);

                // Ghi nhận lịch sử đăng nhập
                String ip = request.getRemoteAddr();
                String agent = request.getHeader("User-Agent");
                LichSuDangNhap lichSu = new LichSuDangNhap();
                lichSu.setNguoiDung(user);
                lichSu.setDiaChiIP(ip);
                lichSu.setThietBi(agent);
                lichSuDangNhapRepository.save(lichSu);

                if (rememberMe) {
                    Cookie cookie = new Cookie("rememberMe", user.getEmail());
                    cookie.setMaxAge(1800);
                    cookie.setPath("/");
                    cookie.setHttpOnly(true);
                    response.addCookie(cookie);
                }
                
                ra.addFlashAttribute("message", "Xin chào " + user.getHoTen() + "! Chúc bạn một ngày tốt lành 🌟");
                return "redirect:/";
            }
            ra.addFlashAttribute("error", "Email hoặc mật khẩu không chính xác!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
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