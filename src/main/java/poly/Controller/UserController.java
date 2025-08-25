package poly.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import poly.entity.NguoiDung;
import poly.entity.YeuThich;
import poly.service.NguoiDungService;
import poly.service.YeuThichService;

@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private YeuThichService yeuThichService;
    
    @Autowired
    private NguoiDungService nguoiDungService;
    
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

    @GetMapping("/profile")
    public String userProfile(HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("user");
        model.addAttribute("nguoiDung", nguoiDung);
        return "thongtincanhan";
    }

    // Hiển thị form chỉnh sửa
    @GetMapping("/edit-profile")
    public String editProfile(HttpSession session, Model model) {
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("user");
        if (nguoiDung == null) {
            return "redirect:/login";
        }
        model.addAttribute("nguoiDung", nguoiDung);
        return "edit-profile";
    }

    // Xử lý cập nhật thông tin
    @PostMapping("/edit-profile")
    public String updateProfile(@ModelAttribute("nguoiDung") NguoiDung nguoiDungMoi, HttpSession session, Model model) {
        String sdt = nguoiDungMoi.getSoDienThoai();
        if (sdt == null || !sdt.matches("^(0|84)[0-9]{8,9}$")) {
            model.addAttribute("error", "Số điện thoại phải bắt đầu bằng 0 hoặc 84 và có 9-11 số.");
            model.addAttribute("nguoiDung", nguoiDungMoi);
            return "edit-profile";
        }
        NguoiDung nguoiDungCu = (NguoiDung) session.getAttribute("user");
        if (nguoiDungCu != null) {
            nguoiDungCu.setHoTen(nguoiDungMoi.getHoTen());
            nguoiDungCu.setGioiTinh(nguoiDungMoi.getGioiTinh());
            nguoiDungCu.setSoDienThoai(nguoiDungMoi.getSoDienThoai());
            // Chỉ cập nhật ngày sinh nếu có nhập
            if (nguoiDungMoi.getNgaySinh() != null) {
                nguoiDungCu.setNgaySinh(nguoiDungMoi.getNgaySinh());
            }
            nguoiDungService.save(nguoiDungCu);
            session.setAttribute("user", nguoiDungCu);
        }
        return "redirect:/user/profile";
    }
}

