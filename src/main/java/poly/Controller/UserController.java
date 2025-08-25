package poly.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import poly.entity.LichSuDangNhap;
import poly.entity.NguoiDung;
import poly.entity.YeuThich;
import poly.service.YeuThichService;

@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private YeuThichService yeuThichService;
    
    @Autowired
    private poly.repository.LichSuDangNhapRepository lichSuDangNhapRepository;
    
    @GetMapping("/saved-posts")
    public String savedPosts(Model model, HttpSession session) {
        NguoiDung user = (NguoiDung) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        List<YeuThich> yeuThichList = yeuThichService.getYeuThichByNguoiDung(user.getMaNguoiDung());
        
        model.addAttribute("yeuThichList", yeuThichList);
        model.addAttribute("user", user);
        
        return "user/saved-posts";
    }
    
    @GetMapping("/login-history")
    public String loginHistory(HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("user");
        if (nguoiDung == null) return "redirect:/auth/login";
        List<LichSuDangNhap> lichSu = lichSuDangNhapRepository.findByNguoiDungOrderByThoiGianDesc(nguoiDung);
        model.addAttribute("lichSu", lichSu);
        return "login-history";
    }
    
    @GetMapping("/profile")
    public String userProfile(HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("user");
        model.addAttribute("nguoiDung", nguoiDung);
        return "thongtincanhan";
    }
}

