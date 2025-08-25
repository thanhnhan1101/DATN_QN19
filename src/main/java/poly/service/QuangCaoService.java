package poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import poly.entity.QuangCao;
import poly.repository.QuangCaoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class QuangCaoService {
    @Autowired
    private QuangCaoRepository quangCaoRepository;

    public List<QuangCao> getAll() {
        return quangCaoRepository.findAll();
    }

    public QuangCao save(QuangCao qc) {
        return quangCaoRepository.save(qc);
    }

    public void deleteById(Long id) {
        quangCaoRepository.deleteById(id);
    }

    public void approve(Long id) {
        Optional<QuangCao> qcOpt = quangCaoRepository.findById(id);
        if (qcOpt.isPresent()) {
            QuangCao qc = qcOpt.get();
            qc.setTrangThai("Đã xuất bản"); // Đúng với constraint mới
            quangCaoRepository.save(qc);
        }
    }

    public void reject(Long id, String lyDo) {
        Optional<QuangCao> qcOpt = quangCaoRepository.findById(id);
        if (qcOpt.isPresent()) {
            QuangCao qc = qcOpt.get();
            qc.setTrangThai("Từ chối"); // Đúng với constraint
            qc.setLyDoTuChoi(lyDo);
            quangCaoRepository.save(qc);
        }
    }

    public List<QuangCao> getQuangCaoHienThi() {
        return quangCaoRepository.findByTrangThaiAndNgayBatDauLessThanEqualAndNgayKetThucGreaterThanEqual(
            "Đã xuất bản", java.time.LocalDate.now(), java.time.LocalDate.now()
        );
    }

    public List<QuangCao> findAll() {
        return quangCaoRepository.findAll();
    }

    public QuangCao findById(Long id) {
        return quangCaoRepository.findById(id).orElse(null);
    }

    public List<QuangCao> getQuangCaoTheoViTri(String viTri) {
        return quangCaoRepository.findByTrangThaiAndViTriAndNgayBatDauLessThanEqualAndNgayKetThucGreaterThanEqual(
            "Đã xuất bản", viTri, java.time.LocalDate.now(), java.time.LocalDate.now()
        );
    }

    public QuangCao findByMaQuangCao(String maQuangCao) {
        try {
            Long id = Long.parseLong(maQuangCao);
            return quangCaoRepository.findByMaQuangCao(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void saveHoaDon(Long maQuangCao, MultipartFile hoaDon) {
        QuangCao qc = quangCaoRepository.findByMaQuangCao(maQuangCao);
        if (qc != null && hoaDon != null && !hoaDon.isEmpty()) {
            try {
                byte[] bytes = hoaDon.getBytes();
                String base64 = java.util.Base64.getEncoder().encodeToString(bytes);
                qc.setHoaDon(base64); // Giả sử QuangCao có trường hoaDon kiểu String (base64)
                quangCaoRepository.save(qc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateTrangThai(Long maQuangCao, String trangThai) {
        QuangCao qc = quangCaoRepository.findByMaQuangCao(maQuangCao);
        if (qc != null) {
            qc.setTrangThai(trangThai);
            quangCaoRepository.save(qc);
        }
    }

}