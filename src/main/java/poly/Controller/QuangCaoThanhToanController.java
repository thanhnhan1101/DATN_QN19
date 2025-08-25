package poly.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import poly.service.QuangCaoService;
import poly.entity.QuangCao;

@Controller
@RequestMapping("/quangcao/thanh-toan")
public class QuangCaoThanhToanController {

    @Autowired
    private QuangCaoService quangCaoService;

    @GetMapping("/{maQuangCao}")
    public String showThanhToan(@PathVariable String maQuangCao, Model model) {
        QuangCao qc = quangCaoService.findByMaQuangCao(maQuangCao);
        if (qc == null || !"Chờ thanh toán".equals(qc.getTrangThai())) {
            return "redirect:/";
        }
        model.addAttribute("qc", qc);
        return "quangcao-thanh-toan";
    }

    @PostMapping("/xac-nhan-thanh-toan")
    public String xacNhanThanhToan(@RequestParam Long maQuangCao,
                                   @RequestParam("hoaDon") MultipartFile hoaDon) {
        // Lưu ảnh hóa đơn vào quảng cáo (có thể lưu dưới dạng base64 hoặc file)
        quangCaoService.saveHoaDon(maQuangCao, hoaDon);
        // Chuyển trạng thái quảng cáo sang "Chờ admin xác nhận"
        quangCaoService.updateTrangThai(maQuangCao, "Chờ xác nhận");
        return "redirect:/quangcao/thanh-toan/" + maQuangCao;
    }
}