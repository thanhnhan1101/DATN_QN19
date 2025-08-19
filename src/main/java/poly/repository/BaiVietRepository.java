package poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import poly.entity.BaiViet;
import poly.entity.NguoiDung;

@Repository
public interface BaiVietRepository extends JpaRepository<BaiViet, Long> {
    long countByTrangThai(String trangThai);
    List<BaiViet> findByTrangThai(String trangThai);
    List<BaiViet> findByTacGia(NguoiDung tacGia);
    long countByTacGiaAndTrangThai(NguoiDung tacGia, String trangThai);
    long countByTacGia(NguoiDung tacGia);
    @Query("SELECT COALESCE(SUM(b.luotXem), 0) FROM BaiViet b WHERE b.tacGia = :tacGia")
    long sumLuotXemByTacGia(@Param("tacGia") NguoiDung tacGia);
    
    Page<BaiViet> findByTacGia(NguoiDung tacGia, Pageable pageable);
    List<BaiViet> findByTrangThaiEquals(String trangThai);
    @Query("SELECT b FROM BaiViet b WHERE b.trangThai = 'Đã xuất bản'")
    List<BaiViet> findPublicPosts();
    List<BaiViet> findByTacGiaMaNguoiDung(Long tacGiaId);
    @Query("SELECT b FROM BaiViet b LEFT JOIN FETCH b.tacGia LEFT JOIN FETCH b.danhMuc")
    List<BaiViet> findAllWithTacGiaAndDanhMuc();

    @Query("SELECT FUNCTION('DATE_FORMAT', b.ngayTao, '%m/%Y') as month, COUNT(b) " +
           "FROM BaiViet b WHERE b.tacGia.maNguoiDung = :maNguoiDung " +
           "GROUP BY FUNCTION('DATE_FORMAT', b.ngayTao, '%m/%Y') " +
           "ORDER BY month")
    List<Object[]> getMonthlyStatsByAuthor(@Param("maNguoiDung") Long maNguoiDung);

    @Query("SELECT d.tenDanhMuc, COUNT(b) " +
           "FROM BaiViet b JOIN b.danhMuc d " +
           "WHERE b.tacGia.maNguoiDung = :maNguoiDung " +
           "GROUP BY d.tenDanhMuc")
    List<Object[]> getCategoryStatsByAuthor(@Param("maNguoiDung") Long maNguoiDung);
}