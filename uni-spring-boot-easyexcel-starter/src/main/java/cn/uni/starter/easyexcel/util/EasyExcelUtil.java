package cn.uni.starter.easyexcel.util;

import cn.uni.starter.autoconfigure.exception.UniException;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Easy Excel 工具类
 *
 * @author CloudS3n
 * @date 2021-05-28 16:16
 */
public final class EasyExcelUtil {

    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";

    public static OutputStream outputStream(HttpServletResponse response, String fileName) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        fileName = encodeFileName(fileName).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        return response.getOutputStream();
    }

    public static void preCheckExcel(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            throw new UniException("文件名不能为空");
        }
        String fileNameSuffix = FilenameUtils.getExtension(decodeFileName(originalFilename));
        if (!(XLS.equalsIgnoreCase(fileNameSuffix) || XLSX.equalsIgnoreCase(fileNameSuffix))) {
            throw new UniException("请导入xls或xlsx格式文件");
        }
    }

    public static String encodeFileName(String fileName) {
        try {
            return URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UniException("不支持的文件编码", e);
        }
    }

    public static String decodeFileName(String fileName) {
        try {
            return URLDecoder.decode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UniException("不支持的文件编码", e);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static String getMultipartFileName(MultipartFile file) {
        preCheckExcel(file);
        String originalFilename = file.getOriginalFilename();
        return originalFilename.substring(0, originalFilename.lastIndexOf("."));
    }

    public static WriteCellStyle buildScoreExcelHeaderContentStyle(short fontSize) {
        // 字体
        WriteFont cellWriteFont = new WriteFont();
        cellWriteFont.setFontName("黑体");
        cellWriteFont.setBold(true);
        cellWriteFont.setFontHeightInPoints(fontSize);
        cellWriteFont.setColor(IndexedColors.BLACK.getIndex());
        // 单元格
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setWriteFont(cellWriteFont);
        contentWriteCellStyle.setBorderTop(BorderStyle.NONE);
        contentWriteCellStyle.setBorderBottom(BorderStyle.NONE);
        contentWriteCellStyle.setBorderLeft(BorderStyle.NONE);
        contentWriteCellStyle.setBorderRight(BorderStyle.NONE);
        return contentWriteCellStyle;
    }
}
