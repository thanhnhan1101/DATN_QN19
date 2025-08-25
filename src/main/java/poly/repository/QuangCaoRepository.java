package poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.entity.QuangCao;
import java.time.LocalDate;
import java.util.List;

public interface QuangCaoRepository extends JpaRepository<QuangCao, Long> {
    List<QuangCao> findByTrangThaiAndNgayBatDauLessThanEqualAndNgayKetThucGreaterThanEqual(
        String trangThai, LocalDate start, LocalDate end
    );

    List<QuangCao> findByTrangThaiAndViTriAndNgayBatDauLessThanEqualAndNgayKetThucGreaterThanEqual(
    String trangThai, String viTri, LocalDate start, LocalDate end
);
    QuangCao findByMaQuangCao(Long maQuangCao);
}
