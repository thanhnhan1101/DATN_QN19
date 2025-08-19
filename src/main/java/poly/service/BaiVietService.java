package poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import poly.entity.BaiViet;
import poly.entity.NguoiDung;
import poly.repository.BaiVietRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BaiVietService {

    @Autowired
    private BaiVietRepository baiVietRepository;

    // Lấy tất cả bài viết
    public List<BaiViet> getAll() {
        return baiVietRepository.findAll();
    }

    // Tìm bài viết theo ID
    public Optional<BaiViet> findById(Long id) {
        return baiVietRepository.findById(id);
    }

    // Lưu hoặc cập nhật bài viết
    public BaiViet save(BaiViet baiViet) {
        return baiVietRepository.save(baiViet);
    }

    // Xóa bài viết
    public void deleteById(Long id) {
        baiVietRepository.deleteById(id);
    }

    // Tìm kiếm, lọc bài viết (ví dụ: theo tiêu đề, trạng thái, danh mục)
    public List<BaiViet> search(String keyword, String trangThai, Integer maDanhMuc) {
        // Bạn có thể viết custom query ở repository, ví dụ:
        // return baiVietRepository.search(keyword, trangThai, maDanhMuc);
        // Ở đây trả về tất cả để bạn tự bổ sung sau
        return baiVietRepository.findAll();
    }

    // Duyệt bài viết (Admin)
    public void approve(Long id) {
        Optional<BaiViet> opt = baiVietRepository.findById(id);
        if (opt.isPresent()) {
            BaiViet bv = opt.get();
            bv.setTrangThai("Đã đăng");
            baiVietRepository.save(bv);
        }
    }

    // Từ chối bài viết (Admin)
    public void reject(Long id) {
        Optional<BaiViet> opt = baiVietRepository.findById(id);
        if (opt.isPresent()) {
            BaiViet bv = opt.get();
            bv.setTrangThai("Từ chối");
            baiVietRepository.save(bv);
        }
    }

    // Đếm tổng số bài viết của reporter
    public long countByTacGia(NguoiDung tacGia) {
        return baiVietRepository.countByTacGia(tacGia);
    }

    // Đếm số bài viết theo trạng thái của reporter
    public long countByTacGiaAndTrangThai(NguoiDung tacGia, String trangThai) {
        return baiVietRepository.countByTacGiaAndTrangThai(tacGia, trangThai);
    }

    // Tính tổng lượt xem của tất cả bài viết của reporter
    public long sumLuotXemByTacGia(NguoiDung tacGia) {
        return baiVietRepository.sumLuotXemByTacGia(tacGia);
    }

    // Lấy danh sách bài viết có phân trang của reporter
    public Page<BaiViet> findByTacGia(NguoiDung tacGia, Pageable pageable) {
        return baiVietRepository.findByTacGia(tacGia, pageable);
    }

    // Đếm số bài viết theo trạng thái
    public long countByTrangThai(String trangThai) {
        return baiVietRepository.countByTrangThai(trangThai);
    }

    public long count() {
        return baiVietRepository.count();
    }
    // TODO: Xem lịch sử chỉnh sửa (cần thêm bảng lịch sử)

    public List<BaiViet> getBaiVietPublic() {
        return baiVietRepository.findPublicPosts();
    }

    // Get monthly statistics for author
    public List<Object[]> getMonthlyStatsByAuthor(Long maNguoiDung) {
        return baiVietRepository.getMonthlyStatsByAuthor(maNguoiDung);
    }

    // Get category statistics for author
    public List<Object[]> getCategoryStatsByAuthor(Long maNguoiDung) {
        return baiVietRepository.getCategoryStatsByAuthor(maNguoiDung);
    }
}