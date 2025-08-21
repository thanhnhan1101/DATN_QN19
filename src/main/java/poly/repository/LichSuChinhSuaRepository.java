package poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import poly.entity.LichSuChinhSua;
import poly.entity.BaiViet;
import java.util.List;

@Repository
public interface LichSuChinhSuaRepository extends JpaRepository<LichSuChinhSua, Long> {
    List<LichSuChinhSua> findByBaiVietOrderByNgayChinhSuaDesc(BaiViet baiViet);
}