package poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.entity.YeuThich;
import poly.entity.NguoiDung;
import poly.entity.BaiViet;
import poly.repository.YeuThichRepository;
import poly.repository.NguoiDungRepository;
import poly.repository.BaiVietRepository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class YeuThichService {

    @Autowired
    private YeuThichRepository yeuThichRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private BaiVietRepository baiVietRepository;

    // Thích hoặc bỏ thích bài viết
    public boolean toggleLike(Long userId, Long baiVietId) {
        try {
            YeuThich yeuThich = yeuThichRepository.findByNguoiDungMaNguoiDungAndBaiVietMaBaiViet(userId, baiVietId);

            if (yeuThich != null) {
                // Nếu đã yêu thích thì xóa
                yeuThichRepository.delete(yeuThich);
                return false;
            }

            // Lấy đối tượng NguoiDung và BaiViet từ database
            Optional<NguoiDung> nguoiDungOpt = nguoiDungRepository.findById(userId);
            Optional<BaiViet> baiVietOpt = baiVietRepository.findById(baiVietId);

            if (nguoiDungOpt.isPresent() && baiVietOpt.isPresent()) {
                YeuThich newYeuThich = new YeuThich();
                newYeuThich.setNguoiDung(nguoiDungOpt.get());
                newYeuThich.setBaiViet(baiVietOpt.get());
                
                // Set the current date/time using the correct field name
                newYeuThich.setNgayTao(LocalDateTime.now()); // Changed from setNgayYeuThich to setNgayTao
                
                yeuThichRepository.save(newYeuThich);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Thêm phương thức like bài viết
    public boolean likeBaiViet(Long userId, Long baiVietId) {
        try {
            // Kiểm tra xem đã like chưa
            YeuThich existing = yeuThichRepository.findByNguoiDungMaNguoiDungAndBaiVietMaBaiViet(userId, baiVietId);
            if (existing != null) {
                return false; // Đã like rồi
            }

            // Lấy đối tượng NguoiDung và BaiViet
            Optional<NguoiDung> nguoiDungOpt = nguoiDungRepository.findById(userId);
            Optional<BaiViet> baiVietOpt = baiVietRepository.findById(baiVietId);

            if (nguoiDungOpt.isPresent() && baiVietOpt.isPresent()) {
                YeuThich yeuThich = new YeuThich();
                yeuThich.setNguoiDung(nguoiDungOpt.get());
                yeuThich.setBaiViet(baiVietOpt.get());
                yeuThich.setNgayTao(LocalDateTime.now());
                yeuThichRepository.save(yeuThich);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Thêm phương thức unlike bài viết
    public boolean unlikeBaiViet(Long userId, Long baiVietId) {
        try {
            YeuThich yeuThich = yeuThichRepository.findByNguoiDungMaNguoiDungAndBaiVietMaBaiViet(userId, baiVietId);
            if (yeuThich != null) {
                yeuThichRepository.delete(yeuThich);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Đếm số lượt thích của bài viết
    public long countLikesByBaiViet(Long baiVietId) {
        return yeuThichRepository.countByBaiVietMaBaiViet(baiVietId);
    }

    // Kiểm tra người dùng đã thích bài viết chưa
    public boolean isLiked(Long userId, Long baiVietId) {
        return yeuThichRepository.findByNguoiDungMaNguoiDungAndBaiVietMaBaiViet(userId, baiVietId) != null;
    }

    // Lấy danh sách bài viết yêu thích của người dùng
    public List<YeuThich> getYeuThichByNguoiDung(Long nguoiDungId) {
        try {
            Optional<NguoiDung> nguoiDungOpt = nguoiDungRepository.findById(nguoiDungId);
            if (nguoiDungOpt.isPresent()) {
                return yeuThichRepository.findByNguoiDungOrderByNgayTaoDesc(nguoiDungOpt.get());
            }
            return List.of();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}

