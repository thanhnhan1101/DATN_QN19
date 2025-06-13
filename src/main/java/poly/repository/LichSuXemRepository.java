package poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import poly.entity.LichSuXem;

@Repository
public interface LichSuXemRepository extends JpaRepository<LichSuXem, Long> {
    List<LichSuXem> findByNguoiDung_MaNguoiDung(Long maNguoiDung);
    List<LichSuXem> findByBaiViet_MaBaiViet(Integer maBaiViet);
    
}
