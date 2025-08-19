package poly.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import poly.entity.NguoiDung;
import poly.entity.YeuThich;
import poly.service.YeuThichService;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private YeuThichService yeuThichService;
    
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
}

