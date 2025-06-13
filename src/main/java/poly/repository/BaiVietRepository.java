package poly.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import poly.entity.BaiViet;
import poly.entity.NguoiDung;

import java.util.List;
import java.util.Optional;

public interface BaiVietRepository extends JpaRepository<BaiViet, Integer> {
    // Tìm bài viết theo tiêu đề chứa từ khóa
    List<BaiViet> findByTieuDeContaining(String keyword);

    // Tìm bài viết theo danh mục
    List<BaiViet> findByDanhMuc_MaDanhMuc(Integer maDanhMuc);

    // Tìm bài viết theo trạng thái
    List<BaiViet> findByTrangThai(String trangThai);
 // Tìm bài viết theo ID với các quan hệ được preload
    @Query("SELECT bv FROM BaiViet bv JOIN FETCH bv.danhMuc LEFT JOIN FETCH bv.tacGia WHERE bv.maBaiViet = :id")
    Optional<BaiViet> findByIdWithRelations(Integer id);
    // Tải tất cả bài viết với quan hệ danhMuc được preload
    @Query("SELECT DISTINCT bv FROM BaiViet bv JOIN FETCH bv.danhMuc")
    List<BaiViet> findAllWithDanhMuc();
    Optional<BaiViet> findByDuongDan(String duongDan);

    BaiViet findFirstByTrangThaiOrderByNgayDangDesc(String trangThai);

    List<BaiViet> findByTrangThaiOrderByNgayDangDesc(String trangThai, Pageable pageable);

    List<BaiViet> findByTrangThaiOrderByLuotXemDesc(String trangThai, Pageable pageable);

    List<BaiViet> findByDanhMuc_MaDanhMucAndTrangThaiOrderByNgayDangDesc(
        Integer maDanhMuc,
        String trangThai,
        Pageable pageable  // Thêm tham số Pageable
    );

    List<BaiViet> findByTacGia(NguoiDung tacGia);
}