package poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.entity.BaiViet;
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

    // TODO: Xem lịch sử chỉnh sửa (cần thêm bảng lịch sử)
}