package poly.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import poly.service.QuangCaoService;

@Controller
@RequestMapping("/admin/quangcao")
public class AdminQuangCaoController {

    @Autowired
    private QuangCaoService quangCaoService;

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
    public String accept(@RequestParam("id") Long id) {
        var qc = quangCaoService.findById(id);
        if (qc != null) {
            qc.setTrangThai("Đã xuất bản"); // Đúng với constraint mới
            quangCaoService.save(qc);
        }
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