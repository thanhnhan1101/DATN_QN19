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
import poly.entity.QuangCao;
import poly.service.QuangCaoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Base64;

@Controller
@RequestMapping("/quangcao")
public class QuangCaoController {

    @Autowired
    private QuangCaoService quangCaoService;

    // Hiển thị form đăng ký quảng cáo
    @GetMapping("/dang-ky")
    public String showDangKyForm(Model model) {
        model.addAttribute("quangCao", new QuangCao());
        return "quangcao-dangky";
    }

    @PostMapping("/dang-ky")
    public String submitDangKyForm(
            @RequestParam("tenDoanhNghiep") String tenDoanhNghiep,
            @RequestParam("email") String email,
            @RequestParam("soDienThoai") String soDienThoai,
            @RequestParam("tieuDe") String tieuDe,
            @RequestParam("duongDanLienKet") String duongDanLienKet,
            @RequestParam("viTri") String viTri,
            @RequestParam("ngayBatDau") String ngayBatDau,
            @RequestParam("ngayKetThuc") String ngayKetThuc,
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam("chiPhi") BigDecimal chiPhi,
            RedirectAttributes ra
    ) {
        try {
            QuangCao qc = new QuangCao();
            qc.setTenDoanhNghiep(tenDoanhNghiep);
            qc.setEmail(email);
            qc.setSoDienThoai(soDienThoai);
            qc.setTieuDe(tieuDe);
            qc.setDuongDanLienKet(duongDanLienKet);
            qc.setViTri(viTri);
            qc.setNgayBatDau(LocalDate.parse(ngayBatDau));
            qc.setNgayKetThuc(LocalDate.parse(ngayKetThuc));
            qc.setChiPhi(chiPhi);
            qc.setTrangThai("Chờ duyệt");
            qc.setLuotHienThi(0);
            qc.setLuotClick(0);

            // Xử lý ảnh thành base64
            if (imageFile != null && !imageFile.isEmpty()) {
                byte[] bytes = imageFile.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                qc.setDuongDanAnh(base64Image);
            }

            quangCaoService.save(qc);
            ra.addFlashAttribute("message", "Đăng ký quảng cáo thành công!");
            return "redirect:/quangcao/dang-ky";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/quangcao/dang-ky";
        }
    }
}