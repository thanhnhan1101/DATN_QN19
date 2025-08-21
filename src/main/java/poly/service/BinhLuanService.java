package poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.entity.BinhLuan;
import poly.repository.BinhLuanRepository;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class BinhLuanService {

    @Autowired
    private BinhLuanRepository binhLuanRepository;

    // Danh sách từ cấm
    private static final List<String> BANNED_WORDS = Arrays.asList(
        "đụ", "địt", "lồn", "cặc", "ku", "đm", "đmm", "clm", "cm", "vcl", "cc",
        "ngu", "súc vật", "phản động", "đcs", "cộng sản", "3 que", "3/", "ngụy"
        // Thêm các từ cấm khác
    );

    // Pattern để kiểm tra từ cấm
    private static final Pattern WORD_PATTERN = Pattern.compile("\\b(" + 
        String.join("|", BANNED_WORDS.stream()
            .map(w -> Pattern.quote(w))
            .toArray(String[]::new)) + 
        ")\\b", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public BinhLuan save(BinhLuan binhLuan) {
        // Kiểm tra và lọc nội dung trước khi lưu
        String filteredContent = filterContent(binhLuan.getNoiDung());
        binhLuan.setNoiDung(filteredContent);
        return binhLuanRepository.save(binhLuan);
    }

    public String filterContent(String content) {
        if (content == null) return null;
        
        // Kiểm tra từ cấm
        if (containsBannedWords(content)) {
            throw new RuntimeException("Bình luận chứa từ ngữ không phù hợp!");
        }
        
        return content.trim();
    }

    private boolean containsBannedWords(String content) {
        return WORD_PATTERN.matcher(content.toLowerCase()).find();
    }

    public List<BinhLuan> getByBaiViet(Long maBaiViet) {
        return binhLuanRepository.findByBaiVietMaBaiVietOrderByNgayTaoDesc(maBaiViet);
    }
}