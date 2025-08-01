package poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import poly.entity.NguoiDung;
import poly.repository.NguoiDungRepository;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class NguoiDungService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<NguoiDung> findAll() {
        return nguoiDungRepository.findAll();
    }

    public Optional<NguoiDung> findById(Long id) {
        return nguoiDungRepository.findById(id);
    }

    public NguoiDung save(NguoiDung nguoiDung) {
        // Basic validation
        if (nguoiDung.getMaNguoiDung() == null && nguoiDungRepository.existsByEmail(nguoiDung.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }
        
        // Mã hóa mật khẩu nếu là người dùng mới
        if (nguoiDung.getMaNguoiDung() == null) {
            nguoiDung.setMatKhau(passwordEncoder.encode(nguoiDung.getMatKhau()));
        }
        
        return nguoiDungRepository.save(nguoiDung);
    }

    public void deleteById(Long id) {
        nguoiDungRepository.deleteById(id);
    }

    public Optional<NguoiDung> findByEmail(String email) {
        return nguoiDungRepository.findByEmail(email);
    }

    public Optional<NguoiDung> login(String email, String password) {
        Optional<NguoiDung> userOpt = nguoiDungRepository.findByEmail(email);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getMatKhau())) {
            return userOpt;
        }
        return Optional.empty();
    }

    public String compressAndEncodeImage(MultipartFile file) throws IOException {
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

    // Thêm phương thức đếm tổng số người dùng
    public long countAll() {
        return nguoiDungRepository.count();
    }
}