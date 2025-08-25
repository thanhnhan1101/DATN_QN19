package poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.entity.BinhLuan;
import poly.entity.BaiViet;
import java.util.List;

public interface BinhLuanRepository extends JpaRepository<BinhLuan, Long> {
    List<BinhLuan> findByBaiVietAndTrangThai(BaiViet baiViet, String trangThai);
}