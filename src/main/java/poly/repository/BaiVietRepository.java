package poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.entity.BaiViet;

public interface BaiVietRepository extends JpaRepository<BaiViet, Long> {
    // Có thể thêm custom query nếu cần
}