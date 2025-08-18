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

@Service
public class YeuThichService {
    
    @Autowired
    private YeuThichRepository yeuThichRepository;
    
    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    
    @Autowired
    private BaiVietRepository baiVietRepository;
    
    // Thích bài viết
    public boolean likeBaiViet(Long nguoiDungId, Long baiVietId) {
        try {
            Optional<NguoiDung> nguoiDungOpt = nguoiDungRepository.findById(nguoiDungId);
            Optional<BaiViet> baiVietOpt = baiVietRepository.findById(baiVietId);
            
            if (nguoiDungOpt.isPresent() && baiVietOpt.isPresent()) {
                NguoiDung nguoiDung = nguoiDungOpt.get();
                BaiViet baiViet = baiVietOpt.get();
                
                // Kiểm tra đã thích chưa
                if (yeuThichRepository.existsByNguoiDungAndBaiViet(nguoiDung, baiViet)) {
                    return false; // Đã thích rồi
                }
                
                // Tạo yêu thích mới
                YeuThich yeuThich = new YeuThich();
                yeuThich.setNguoiDung(nguoiDung);
                yeuThich.setBaiViet(baiViet);
                yeuThich.setTrangThai("Đã thích");
                
                yeuThichRepository.save(yeuThich);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Bỏ thích bài viết
    public boolean unlikeBaiViet(Long nguoiDungId, Long baiVietId) {
        try {
            Optional<NguoiDung> nguoiDungOpt = nguoiDungRepository.findById(nguoiDungId);
            Optional<BaiViet> baiVietOpt = baiVietRepository.findById(baiVietId);
            
            if (nguoiDungOpt.isPresent() && baiVietOpt.isPresent()) {
                NguoiDung nguoiDung = nguoiDungOpt.get();
                BaiViet baiViet = baiVietOpt.get();
                
                yeuThichRepository.deleteByNguoiDungAndBaiViet(nguoiDung, baiViet);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Kiểm tra người dùng đã thích bài viết chưa
    public boolean isLiked(Long nguoiDungId, Long baiVietId) {
        try {
            Optional<NguoiDung> nguoiDungOpt = nguoiDungRepository.findById(nguoiDungId);
            Optional<BaiViet> baiVietOpt = baiVietRepository.findById(baiVietId);
            
            if (nguoiDungOpt.isPresent() && baiVietOpt.isPresent()) {
                return yeuThichRepository.existsByNguoiDungAndBaiViet(nguoiDungOpt.get(), baiVietOpt.get());
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Đếm số lượt thích của bài viết
    public long countLikesByBaiViet(Long baiVietId) {
        try {
            Optional<BaiViet> baiVietOpt = baiVietRepository.findById(baiVietId);
            if (baiVietOpt.isPresent()) {
                return yeuThichRepository.countByBaiViet(baiVietOpt.get());
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
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

