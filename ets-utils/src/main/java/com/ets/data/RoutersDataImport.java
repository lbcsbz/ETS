package com.ets.data;

import com.ets.pojo.Routers;
import com.ets.pojo.Sites;
import org.apache.poi.ss.formula.functions.Now;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Date;
import java.util.ArrayList;
//import java.util.Date;

public class RoutersDataImport {
    public static ArrayList<Routers> getInvestDataByExcel(String file) throws Exception {
        ArrayList<Routers> dataList = new ArrayList<>();
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
                String router_id = "";
                String origin_site = "";
                String origin_site_id = "";
                String target_site = "";
                String target_site_id = "";
                String plan_origin_time = "";
                String plan_target_time = "";
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
                                router_id = cell.toString().trim();
                                break;
                            case 3:
                                origin_site = cell.toString().trim();
                                break;
                            case 4:
                                origin_site_id = cell.toString().trim();
                                break;
                            case 5:
                                target_site = cell.toString().trim();
                                break;
                            case 6:
                                target_site_id = cell.toString().trim();
                                break;
                            case 7:
                                plan_origin_time = cell.toString().trim();
                                break;
                            case 8:
                                plan_target_time = cell.toString().trim();
                                break;
                            case 9:
                                created = new Date(cell.getDateCellValue().getTime());
                                break;
                            case 10:
                                updated = new Date(cell.getDateCellValue().getTime());

                                break;
                            default:
                                break;
                        }
                    }
                }
                dataList.add(new Routers(id,name,router_id,origin_site,origin_site_id,target_site,target_site_id,plan_origin_time,plan_target_time,created,updated));
            }

        }
        return dataList;
    }
}
