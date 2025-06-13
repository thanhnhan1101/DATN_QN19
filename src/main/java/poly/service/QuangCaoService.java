// package poly.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import poly.entity.QuangCao;
// import poly.repository.QuangCaoRepository;
// import java.time.LocalDateTime;
// import java.util.List;

// @Service
// public class QuangCaoService {
//    @Autowired
//    private QuangCaoRepository quangCaoRepository;
   
//    public QuangCao getActiveAdByPosition(String viTri) {
//        List<QuangCao> ads = quangCaoRepository.findActiveAdsByPosition(viTri, LocalDateTime.now());
//        return ads.isEmpty() ? null : ads.get(0);
//    }
   
//    public List<QuangCao> getAllActiveAds() {
//        // Thay vì chỉ lấy active ads, lấy tất cả để hiển thị trong admin
//        return quangCaoRepository.findAll();
//    }
   
//    public List<QuangCao> getActiveAdsByPosition(String viTri) {
//        // Thêm method mới để lấy quảng cáo theo vị trí
//        return quangCaoRepository.findActiveAdsByPosition(viTri, LocalDateTime.now());
//    }
   
//    public QuangCao save(QuangCao quangCao) {
//        return quangCaoRepository.save(quangCao);
//    }
   
//    public void incrementImpressions(Integer maQuangCao) {
//        QuangCao quangCao = quangCaoRepository.findById(maQuangCao)
//            .orElseThrow(() -> new RuntimeException("Không tìm thấy quảng cáo"));
//        quangCao.setLuotHienThi(quangCao.getLuotHienThi() + 1);
//        quangCaoRepository.save(quangCao);
//    }
   
//    public void incrementClicks(Integer maQuangCao) {
//        QuangCao quangCao = quangCaoRepository.findById(maQuangCao)
//            .orElseThrow(() -> new RuntimeException("Không tìm thấy quảng cáo"));
//        quangCao.setLuotClick(quangCao.getLuotClick() + 1);
//        quangCaoRepository.save(quangCao);
//    }
   
//    public void deleteById(Integer id) {
//        quangCaoRepository.deleteById(id);
//    }
   
//    public QuangCao findById(Integer id) {
//        return quangCaoRepository.findById(id)
//            .orElseThrow(() -> new RuntimeException("Không tìm thấy quảng cáo có ID: " + id));
//    }
// }