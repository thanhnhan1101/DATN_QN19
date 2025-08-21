package poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.entity.YeuThich;
import poly.entity.NguoiDung;
import poly.entity.BaiViet;

import java.util.List;
import java.util.Optional;

@Repository
public interface YeuThichRepository extends JpaRepository<YeuThich, Long> {
    
    // Tìm yêu thích theo người dùng và bài viết
    Optional<YeuThich> findByNguoiDungAndBaiViet(NguoiDung nguoiDung, BaiViet baiViet);
    
    // Kiểm tra người dùng đã thích bài viết chưa
    boolean existsByNguoiDungAndBaiViet(NguoiDung nguoiDung, BaiViet baiViet);
    
    // Lấy danh sách yêu thích của người dùng
    List<YeuThich> findByNguoiDungOrderByNgayTaoDesc(NguoiDung nguoiDung);
    
    // Đếm số lượt thích của bài viết
    long countByBaiViet(BaiViet baiViet);
    
    // Xóa yêu thích theo người dùng và bài viết
    void deleteByNguoiDungAndBaiViet(NguoiDung nguoiDung, BaiViet baiViet);
    
    // Tìm yêu thích theo mã người dùng và mã bài viết
    YeuThich findByNguoiDungMaNguoiDungAndBaiVietMaBaiViet(Long maNguoiDung, Long maBaiViet);
    
    // Đếm số lượt thích theo mã bài viết
    long countByBaiVietMaBaiViet(Long maBaiViet);
    
    // Lấy danh sách yêu thích của người dùng theo mã người dùng
    List<YeuThich> findByNguoiDungMaNguoiDungOrderByNgayTaoDesc(Long maNguoiDung);
}

