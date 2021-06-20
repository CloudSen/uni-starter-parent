package cn.unic.starter.easyexcel.handler;

import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;

/**
 * 行格式
 *
 * @author CloudS3n
 * @date 2021-06-09 12:59
 */
@RequiredArgsConstructor
public class RowStyleHandler implements RowWriteHandler {

    private final Integer rowIndex;
    private final WriteCellStyle writeCellStyle;

    @Override
    public void beforeRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Integer integer, Integer integer1, Boolean aBoolean) {

    }

    @Override
    public void afterRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer integer, Boolean aBoolean) {
        int currentRow = row.getRowNum();
        if (rowIndex == currentRow) {
            row.setRowStyle(StyleUtil.buildHeadCellStyle(row.getSheet().getWorkbook(), writeCellStyle));
        }
    }
}
