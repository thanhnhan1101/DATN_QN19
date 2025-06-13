package poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.entity.BaiViet;
import poly.entity.NguoiDung;
import poly.repository.BaiVietRepository;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
@Transactional
public class BaiVietService {

    @Autowired
    private BaiVietRepository baiVietRepository;

    // Lấy danh sách tất cả bài viết với danh mục được tải
    public List<BaiViet> getAllBaiViet() {
        return baiVietRepository.findAllWithDanhMuc();
    }

    // Nén và mã hóa hình ảnh thành chuỗi base64
    private String compressAndEncodeImage(MultipartFile file) throws IOException {
        try {
            // Convert MultipartFile to byte array
            byte[] bytes = file.getBytes();
            
            // Basic validation
            if (bytes.length == 0) {
                throw new IOException("File is empty");
            }

            // Encode directly to base64 if image processing fails
            String base64Image = Base64.getEncoder().encodeToString(bytes);
            
            // Add data URI prefix based on file content type
            String contentType = file.getContentType();
            if (contentType == null) {
                contentType = "image/jpeg";
            }
            
            return "data:" + contentType + ";base64," + base64Image;
        } catch (IOException e) {
            throw new IOException("Error processing image: " + e.getMessage());
        }
    }

    // Thêm hoặc cập nhật bài viết
    public BaiViet save(BaiViet baiViet, MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                try {
                    String encodedImage = compressAndEncodeImage(file);
                    baiViet.setHinhAnh(encodedImage);
                } catch (IOException e) {
                    throw new RuntimeException("Lỗi xử lý hình ảnh: " + e.getMessage());
                }
            }
            return baiVietRepository.save(baiViet);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lưu bài viết: " + e.getMessage());
        }
    }

    // Thêm hoặc cập nhật bài viết không có hình ảnh
    public BaiViet save(BaiViet baiViet) {
        try {
            return baiVietRepository.save(baiViet);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lưu bài viết: " + e.getMessage());
        }
    }

    // Tìm bài viết theo ID với các quan hệ được tải
    public BaiViet findById(Integer id) {
        try {
            return baiVietRepository.findByIdWithRelations(id).orElse(null);
        } catch (Exception e) {
            // Log lỗi (nếu có logging cấu hình)
            throw new RuntimeException("Lỗi khi tìm bài viết với ID " + id + ": " + e.getMessage(), e);
        }
    }

    // Tìm bài viết theo đường dẫn
    public BaiViet findByDuongDan(String duongDan) {
        try {
            return baiVietRepository.findByDuongDan(duongDan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết với đường dẫn: " + duongDan));
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm bài viết theo đường dẫn: " + e.getMessage());
        }
    }

    // Xoá bài viết theo ID
    public void deleteById(Integer id) {
        try {
            baiVietRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa bài viết với ID " + id + ": " + e.getMessage(), e);
        }
    }

    // Tìm bài viết theo tiêu đề chứa từ khóa
    public List<BaiViet> findByTieuDeContaining(String keyword) {
        try {
            return baiVietRepository.findByTieuDeContaining(keyword);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm bài viết theo tiêu đề: " + e.getMessage(), e);
        }
    }

    // Tìm bài viết theo danh mục
    public List<BaiViet> findByDanhMuc_MaDanhMuc(Integer maDanhMuc) {
        try {
            return baiVietRepository.findByDanhMuc_MaDanhMuc(maDanhMuc);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm bài viết theo danh mục: " + e.getMessage(), e);
        }
    }

    // Tìm bài viết theo trạng thái
    public List<BaiViet> findByTrangThai(String trangThai) {
        try {
            return baiVietRepository.findByTrangThai(trangThai);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm bài viết theo trạng thái: " + e.getMessage(), e);
        }
    }

    private String createSlug(String title) {
        String slug = title.toLowerCase()
                .replaceAll("[áàảãạâấầẩẫậăắằẳẵặ]", "a")
                .replaceAll("[éèẻẽẹêếềểễệ]", "e")
                .replaceAll("[íìỉĩị]", "i")
                .replaceAll("[óòỏõọôốồổỗộơớờởỡợ]", "o")
                .replaceAll("[úùủũụưứừửữự]", "u")
                .replaceAll("[ýỳỷỹỵ]", "y")
                .replaceAll("[đ]", "d")
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
        return slug;
    }

    public BaiViet getLatestPublishedPost() {
        return baiVietRepository.findFirstByTrangThaiOrderByNgayDangDesc("Đã đăng");
    }

    public List<BaiViet> getLatestPublishedPosts(int limit) {
        return baiVietRepository.findByTrangThaiOrderByNgayDangDesc("Đã đăng", PageRequest.of(0, limit));
    }

    public List<BaiViet> getMostViewedPosts(int limit) {
        return baiVietRepository.findByTrangThaiOrderByLuotXemDesc("Đã đăng", PageRequest.of(0, limit));
    }

    public List<BaiViet> getLatestPostsByCategory(Integer categoryId, String trangThai, int limit) {
        try {
            return baiVietRepository.findByDanhMuc_MaDanhMucAndTrangThaiOrderByNgayDangDesc(
                categoryId,
                trangThai,
                PageRequest.of(0, limit)  // Thêm PageRequest để giới hạn số lượng kết quả
            );
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy bài viết theo danh mục: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void incrementViewCount(Integer id) {
        try {
            BaiViet baiViet = baiVietRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết với ID: " + id));
            baiViet.setLuotXem(baiViet.getLuotXem() + 1);
            baiVietRepository.save(baiViet);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật lượt xem: " + e.getMessage(), e);
        }
    }

    public List<BaiViet> findByTacGia(NguoiDung tacGia) {
        return baiVietRepository.findByTacGia(tacGia);
    }
}