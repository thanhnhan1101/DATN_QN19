package poly.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import poly.entity.QuangCao;
import poly.service.QuangCaoService;

@Controller
@RequestMapping("/admin/quangcao")
public class AdminQuangCaoController {

    @Autowired
    private QuangCaoService quangCaoService;

    @Autowired
    private poly.service.EmailService emailService;

    @GetMapping
    public String listQuangCao(Model model) {
        model.addAttribute("listQuangCao", quangCaoService.findAll());
        return "Admin/quangcao";
    }

    @PostMapping("/reject")
    public String reject(@RequestParam("id") Long id, @RequestParam("lyDoTuChoi") String lyDo) {
        var qc = quangCaoService.findById(id);
        if (qc != null) {
            qc.setTrangThai("Từ chối");
            qc.setLyDoTuChoi(lyDo);
            quangCaoService.save(qc);
        }
        return "redirect:/admin/quangcao";
    }

    @PostMapping("/accept")
    public String acceptAd(@RequestParam Long id) {
        QuangCao qc = quangCaoService.findById(id);
        if ("Chờ xác nhận".equals(qc.getTrangThai())) {
            qc.setTrangThai("Đã xuất bản");
        } else {
            qc.setTrangThai("Chờ thanh toán");
            // Gửi email cho người dùng như cũ
            String link = "http://localhost:8080/quangcao/thanh-toan/" + qc.getMaQuangCao();
            String subject = "Thanh toán quảng cáo MSN";
            String content = "Quảng cáo của bạn đã được duyệt. Vui lòng thanh toán tại đường link sau:\n" + link;
            emailService.send(qc.getEmail(), subject, content);
        }
        quangCaoService.save(qc);
        return "redirect:/admin/quangcao";
    }

    @GetMapping("/view/{id}")
    public String viewQuangCao(@PathVariable("id") Long id, Model model) {
        var qc = quangCaoService.findById(id);
        if (qc == null) {
            return "redirect:/admin/quangcao";
        }
        model.addAttribute("qc", qc);
        return "Admin/quangcao-detail";
    }
}