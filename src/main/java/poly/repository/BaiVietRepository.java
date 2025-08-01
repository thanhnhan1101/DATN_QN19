package poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.entity.BaiViet;

public interface BaiVietRepository extends JpaRepository<BaiViet, Long> {
    List<BaiViet> findByTrangThai(String trangThai);
}