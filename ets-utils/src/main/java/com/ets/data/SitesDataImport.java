package com.ets.data;





import com.ets.pojo.Sites;
import lombok.Data;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Date;
import java.util.ArrayList;
//import java.util.Date;

public class SitesDataImport {
    public static ArrayList<Sites> getInvestDataByExcel(String file) throws Exception {
        ArrayList<Sites> dataList = new ArrayList<>();
        File excel = new File(file);
        if (!excel.isFile() || !excel.exists()) {
            System.out.println("文件路径错误或不存在！");
        }
        Workbook wb = WorkbookFactory.create(new FileInputStream(file));
        //开始解析
        Sheet sheet = wb.getSheetAt(0);
        int firstRowIndex = sheet.getFirstRowNum();
        int lastRowIndex = sheet.getLastRowNum();
        //遍历行
        for (int i = firstRowIndex; i < lastRowIndex; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                int id = 0;
                String name = "";
                String type = "";
                String site_id = "";
                int level = 0;
                String parent_id = "";
                Date created = new Date(System.currentTimeMillis());
                Date updated = new Date(System.currentTimeMillis());
                //遍历列
                for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    if (cell != null && !"".equals(cell.toString().trim())) {
                        switch (j) {
                            case 0:
                                id = (int)Double.parseDouble(cell.toString());
                                break;
                            case 1:
                                name = cell.toString().trim();
                                break;
                            case 2:
                                site_id = cell.toString().trim();
                                break;
                            case 3:
                                type = cell.toString().trim();
                                break;
                            case 4:
                                level = (int)Double.parseDouble(cell.toString());
                                break;
                            case 5:
                                parent_id = cell.toString().trim();
                                break;
                            case 6:
                                created = new Date(cell.getDateCellValue().getTime());
                                break;
                            case 7:
                                updated = new Date(cell.getDateCellValue().getTime());
                                break;
                            default:
                                break;
                        }
                    }
                }
                dataList.add(new Sites(id,name,type,site_id,level,parent_id,created,updated));
            }

        }
        return dataList;
    }
}
