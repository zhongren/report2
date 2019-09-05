package com.newproj.report.quota.action;

import com.newproj.core.page.PageParam;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.annotation.Get;
import com.newproj.core.rest.annotation.Post;
import com.newproj.core.rest.support.ParamModal;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.report.collection.dto.Collection;
import com.newproj.report.collection.service.CollectionService;
import com.newproj.report.context.trans.TransContext;
import com.newproj.report.eduinst.dto.Eduinst;
import com.newproj.report.eduinst.dto.EduinstCollectionMap;
import com.newproj.report.eduinst.service.EduinstCollectionMapService;
import com.newproj.report.eduinst.service.EduinstService;
import com.newproj.report.quota.dto.Quota;
import com.newproj.report.quota.service.QuotaService;
import com.newproj.report.reporting.service.ReportingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Api("/init")
public class EduinstCollectionMapAction extends RestActionSupporter {
    @Autowired
    private EduinstCollectionMapService eduinstCollectionMapService;
    @Autowired
    private ReportingService reportingService;
    @Autowired
    private EduinstService eduinstService;
    @Autowired
    private CollectionService collectionService;
    @Autowired
    private QuotaService quotaService;

    @Autowired
    private TransContext trans;

    @Post("/ecm")
    public String ecm() {
        List<Eduinst> eduinstList=eduinstService.findList(Eduinst.class);
        int reportId=reportingService.getPresent().getId();
        List<Collection> list0=collectionService.findListBy("type",1, Collection.class);
        List<Collection> list1=collectionService.findListBy("type",2, Collection.class);
        List<Collection> list2=collectionService.findListBy("type",3, Collection.class);
        List<Collection> list3=collectionService.findListBy("type",4, Collection.class);
        List<Collection> list=new ArrayList<>();
        list.addAll(list0);
        list.addAll(list2);
        list.addAll(list1);
        list.addAll(list3);
        for(Eduinst eduinst:eduinstList){
            int instId=eduinst.getId();
            //"COUNTY";
            String instType=eduinst.getType();
            for(Collection collection:list){
                EduinstCollectionMap eduinstCollectionMap=new EduinstCollectionMap();
                eduinstCollectionMap.setInstId(instId);
                eduinstCollectionMap.setReportId(reportId);
                eduinstCollectionMap.setInstType(instType);
                eduinstCollectionMap.setCollectionId(collection.getId());
                eduinstCollectionMap.setQuotaId(collection.getQuotaId());
                eduinstCollectionMapService.create(eduinstCollectionMap,EduinstCollectionMap.class);
            }
        }




		return success("成功");
    }
}
