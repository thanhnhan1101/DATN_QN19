package poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.entity.BinhLuan;
import java.util.List;

public interface BinhLuanRepository extends JpaRepository<BinhLuan, Long> {
    List<BinhLuan> findByBaiVietMaBaiVietOrderByNgayTaoDesc(Long maBaiViet);
}