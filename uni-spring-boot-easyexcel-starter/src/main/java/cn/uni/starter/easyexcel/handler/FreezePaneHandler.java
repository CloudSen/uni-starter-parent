package cn.uni.starter.easyexcel.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import lombok.RequiredArgsConstructor;

/**
 * 冻结单元格
 *
 * @author CloudS3n
 * @date 2021-06-03 16:53
 */
@RequiredArgsConstructor
public class FreezePaneHandler implements SheetWriteHandler {

    private final Integer cellNum;
    private final Integer rowNum;
    private final Integer firstCellNum;
    private final Integer firstRowNum;

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        if (firstCellNum != null && firstRowNum != null) {
            writeSheetHolder.getSheet().createFreezePane(cellNum, rowNum, firstCellNum, firstRowNum);
        } else {
            writeSheetHolder.getSheet().createFreezePane(cellNum, rowNum);
        }
    }

    public static FreezePaneHandler freezeFirstRow() {
        return new FreezePaneHandler(0, 1, null, null);
    }

    public static FreezePaneHandler freezeScoreExcelStuHeader() {
        return new FreezePaneHandler(0, 5, null, null);
    }

    public static FreezePaneHandler freezeSportScoreExcelHeader() {
        return new FreezePaneHandler(0, 2, null, null);
    }
}
