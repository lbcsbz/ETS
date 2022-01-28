package com.ets.controller;


import com.ets.data.RoutersDataImport;
import com.ets.data.RoutersInsert;
import com.ets.data.SitesDataImport;
import com.ets.data.SitesInsert;
import com.ets.pojo.Routers;
import com.ets.pojo.Sites;

import java.util.ArrayList;

public class DataInsertController {
    public static void main(String[] args) throws Exception {

        String sitesfile="G:/ETS/databases/final/sites.xls";
        String routersfile="G:/ETS/databases/final/routers.xls";
        ArrayList<Sites> sitesList = SitesDataImport.getInvestDataByExcel(sitesfile);
        SitesInsert.insertSitesData(sitesList);
        ArrayList<Routers> routersList = RoutersDataImport.getInvestDataByExcel(routersfile);
        RoutersInsert.insertRoutersData(routersList);
    }
}
