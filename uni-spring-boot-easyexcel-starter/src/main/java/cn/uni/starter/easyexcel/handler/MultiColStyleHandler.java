package cn.uni.starter.easyexcel.handler;

import cn.uni.starter.autoconfigure.exception.UniException;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * @author CloudS3n
 * @date 2021-06-09 13:13
 */
@RequiredArgsConstructor
public class MultiColStyleHandler implements CellWriteHandler {

    private final Integer row;
    private final List<Integer> colList;
    private final Integer cellHeight;
    private final WriteCellStyle writeCellStyle;

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer integer, Integer integer1, Boolean aBoolean) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {
        if (CollectionUtils.isEmpty(colList)) {
            throw new UniException("列不能为空");
        }
        int currentRow = cell.getRowIndex();
        int currentCol = cell.getColumnIndex();
        if (row == currentRow && colList.contains(currentCol)) {
            Workbook workbook = cell.getSheet().getWorkbook();
            writeSheetHolder.getSheet().getRow(cell.getRowIndex()).setHeight((short) (cellHeight * 256));
            cell.setCellStyle(StyleUtil.buildHeadCellStyle(workbook, writeCellStyle));
        }
    }
}
