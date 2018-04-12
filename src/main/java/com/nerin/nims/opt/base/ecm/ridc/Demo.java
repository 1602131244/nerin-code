package com.nerin.nims.opt.base.ecm.ridc;

import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.IdcClientManager;
import oracle.stellent.ridc.IdcContext;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.model.TransferFile;
import oracle.stellent.ridc.protocol.ServiceResponse;

import java.io.*;

/**
 * Created by Zak on 2017/1/17.
 */
public class Demo {

    public void main(String[] args) throws IdcClientException {

        DataBinder mainBinder = RidcUtil.getDataBinder();

        /**
         * 打印包下载服务
         */
//        //打印包下载代码
//        mainBinder.putLocal("IdcService","GET_DOWNLOAD_PATH_PRINT");
//        mainBinder.putLocal("PLT_ORDER_HEADER_ID","721");
//        mainBinder.putLocal("PLT_NUM","PT-2015-0398");
//        mainBinder.putLocal("STA_FLAG","PRICING");
//        mainBinder.putLocal("Is_Text","1");
//        mainBinder.putLocal("PLT_CATEGORY","DOC_NEW_PRINT");
//        mainBinder.putLocal("DOWNLOAD_TYPES","1,2");
//        mainBinder.putLocal("TYPE_DOC","P");
//        mainBinder.putLocal("DOWNLOAD_HEADER_ID","721");
//        mainBinder.putLocal("Is_Link","");
//        mainBinder.putLocal("idString","1105340,1088086,1088080,1088081,1088076,1088077,1088082,1088090,1088087,1088088,1088089,1088084,1088083,1088085,1088074,1088078,1088075,1088073,1088079,1104837,1104838,1104839,1104840,1088069,1088070,1088071,");
//
//        // 执行服务
//        DataBinder serverBinder = RidcUtil.executeService(mainBinder);
//        // 将下载URL输出
//        System.out.println(serverBinder.getLocal("cux_filepath"));

        /**
         * 提交图纸目录服务调用代码（1） 获取打印模板信息
         * 在提交图纸目录前， 需要预览图纸，如果直接调用服务，则不需要此步骤，只需要将预览时
         * 所用到的 数据文件xmlid与模板文件tempid传入服务即可，
        */
//        //此服务用于获取相应的xmlid与tempid
//        mainBinder.putLocal("IdcService","GET_SOURCE_FILE_ID");
//        /**
//         * 下面是从我的图纸目录 代码中看到的对应的p_info_str参数固定值
//         * 此参数可以为：
//         * 工作包，分为施工图版和高阶段版本
//         * CUXDrawContentWPReport， CUXDrawContentGJDReport，
//         * 子项专业
//         * CUXDrawContentDevisionMajorReport
//         * 子项专业孙项
//         * CUXDrawContentDevisionMajorSEQReport
//         * 其它(不明确)
//         * CUXDrawContentENReport，CUXDrawContentWPENReport，CUXDrawContentGJDENReport，CUXDrawContentDevisionMajorENReport，
//         * CUXDrawContentDevisionMajorSEQENReport
//         */
//        mainBinder.putLocal("p_info_str","CUXDrawContentWPReport");
//
//        // 执行服务
//        DataBinder serverBinder = RidcUtil.executeService(mainBinder);
//        //将整个binder数据输出
//        System.out.println(serverBinder);
//        //将 tempid 与 xmlid 输出
//        System.out.println("tempid: "+serverBinder.getLocal("tempid"));
//        System.out.println("xmlid: "+serverBinder.getLocal("xmlid"));


        /**
         * 提交图纸目录服务调用代码（2） 提交图纸目录
         */
//        mainBinder.putLocal("IdcService","CUX_SubmmitCatalogue_S");
//        /**
//         * 关于xmlid与tempid应该取哪个值的问题， 如下是原来的代码获取xmlid1与获取xmlid2的源码，
//         * 可能跟业务流程有关，但是后面看到代码xmlid的获取与中、英文模板有关联
//         * <$if IsCreate$>
//         <$p_info_str="CUXDrawContentReport"$>
//         <$PAGE_COUNT=21$>
//         <$elseif Is_DLVR$><!--工作包，分为施工图版和高阶段版本-->
//         <$if strEquals(p_phase_code,'DD') or strEquals(p_phase_code,'ED') or strEquals(p_phase_code,'EE')$>
//         <$p_info_str="CUXDrawContentWPReport"$>
//         <$else$>
//         <$p_info_str="CUXDrawContentGJDReport"$>
//         <$endif$>
//         <$elseif Is_Devision$><!--子项专业-->
//         <$p_info_str="CUXDrawContentDevisionMajorReport"$>
//         <$elseif Is_Devision_SEQ$><!--子项专业孙项-->
//         <$p_info_str="CUXDrawContentDevisionMajorSEQReport"$>
//         <$endif$>
//         <$executeService("GET_SOURCE_FILE_ID")$>
//         <$xmlid1=xmlid$>
//         <$tempid1=tempid$>
//         <$if IsCreate$>
//         <$p_info_str="CUXDrawContentENReport"$>
//         <$PAGE_COUNT=15$>
//         <$elseif Is_DLVR$>
//         <$if strEquals(p_phase_code,'DD') or strEquals(p_phase_code,'ED') or strEquals(p_phase_code,'EE')$>
//         <$p_info_str="CUXDrawContentWPENReport"$>
//         <$else$>
//         <$p_info_str="CUXDrawContentGJDENReport"$>
//         <$endif$>
//         <$elseif Is_Devision$>
//         <$p_info_str="CUXDrawContentDevisionMajorENReport"$>
//         <$elseif Is_Devision_SEQ$>
//         <$p_info_str="CUXDrawContentDevisionMajorSEQENReport"$>
//         <$endif$>
//         <$executeService("GET_SOURCE_FILE_ID")$>
//         <$xmlid2=xmlid$>
//         <$tempid2=tempid$>
//         */
//        mainBinder.putLocal("tempid","829850");
//        mainBinder.putLocal("xmlid","839205");
//
////        mainBinder.putLocal("xmlid1",""); 上面的代码说明了xmlid1与xmlid2的获取方式， 在中文模板时， 使用1，中英文模板使用2
////        mainBinder.putLocal("tempid1","");
////        mainBinder.putLocal("xmlid2","");
////        mainBinder.putLocal("tempid2","");
//
//
//        mainBinder.putLocal("dInDate","2017-1-17 00:00:00Z");
//        mainBinder.putLocal("dDocAuthor","weblogic");
//        mainBinder.putLocal("dDocName","2A13A01BD0401PM1DL1-1");
//        mainBinder.putLocal("dDocTitle","图纸目录");
//        mainBinder.putLocal("dDocType","PJT-DRAW");
//        mainBinder.putLocal("dSecurityGroup","PROJECT");
//        mainBinder.putLocal("xDETAIL_TYPE","图纸目录");
//        mainBinder.putLocal("xPJT_REVISE_NUM","0");
//        mainBinder.putLocal("PAGE_COUNT","15");
//        mainBinder.putLocal("Is_DLVR","");
//        mainBinder.putLocal("Is_BD_DL","");
//        mainBinder.putLocal("Is_Devision","");
//        mainBinder.putLocal("Is_Devision_SEQ","");
//        mainBinder.putLocal("Is_CAD","");
//        mainBinder.putLocal("IsCreate","1");
//        mainBinder.putLocal("IS_CREATE","1");
//        mainBinder.putLocal("CAT_HEADER_ID","4054");
//        mainBinder.putLocal("DLVR_ID","");
//        mainBinder.putLocal("DLVR_VERSION","");
//        mainBinder.putLocal("isAllEM","0");
//        mainBinder.putLocal("idString","937143,");
//        mainBinder.putLocal("cpdBasketID","");
//        mainBinder.putLocal("role_type","1");
//        mainBinder.putLocal("xPROJECT_PNUM","2A13A01");
//        mainBinder.putLocal("xPROJECT_PNAME","广西南国铜冶炼工程");
//        mainBinder.putLocal("xCLIENT","广西南国铜业有限责任公司");
//        mainBinder.putLocal("PROJECT_NUM","2A13A010401");
//        mainBinder.putLocal("UNIT_TASK_CODE","0401");
//        mainBinder.putLocal("xDIVISION","");
//        mainBinder.putLocal("xREF_DWG_NO","2A13A01BD0401PM1");
//        mainBinder.putLocal("SPECIALITY_CLASS_NUMBER","PM");
//        mainBinder.putLocal("UNIT_SEQ","1");
//        mainBinder.putLocal("PHASE_CODE","BD");
//        mainBinder.putLocal("CAT_STATUS_FLAG","E");
//        mainBinder.putLocal("fParentGUID","");
//        mainBinder.putLocal("dDocAccount","ZY-2PM");
//        mainBinder.putLocal("PROJECT_DNAME","广西南国铜业有限责任公司150kt/a铜冶炼工程设计");
//        mainBinder.putLocal("xPROJECT_LNAME","广西南国铜业有限责任公司150kt/a铜冶炼工程设计");
//        mainBinder.putLocal("xPROJECT_SNAME","广西南国150kt/a铜冶炼");
//        mainBinder.putLocal("xDLVR_TYPE","WDDFDL");
//        mainBinder.putLocal("xSIZE","A4");
//        mainBinder.putLocal("xDESIGN_PHASE","BD");
//        mainBinder.putLocal("xSPECIALTY","PM");
//        mainBinder.putLocal("preview_flag","");
//        mainBinder.putLocal("TEMP","");
//        mainBinder.putLocal("xPROJECT_MANAGER_NAME","袁剑平");
//
//        // 执行服务
//        DataBinder serverBinder = RidcUtil.executeService(mainBinder);
//        //调用服务出现了异常， 应该是测试数据有问题， 但是我不知道应该用哪些数据才是正确的
//        System.out.println(serverBinder);


        /**
         * 向ECM中检入文件代码
         */
        mainBinder.putLocal("IdcService", "CHECKIN_UNIVERSAL");
        mainBinder.putLocal("dSecurityGroup", "A");//安全组
        mainBinder.putLocal("dDocAccount", "");//账户
        mainBinder.putLocal("dDocName", "66666666666");//内部编号
        mainBinder.putLocal("dDocType", "PJT-DESIGN-DOC");//文档类型
        mainBinder.putLocal("dDocAuthor", "weblogic");//文档作者

        mainBinder.putLocal("dDocTitle", "66666666666");//文档标题
        mainBinder.putLocal("dCollectionID", "");//文件夹ID
        mainBinder.putLocal("doFileCopy", "1");//是否从本地copy文件， 如果为0则会删除本地文件
//        mainBinder.putLocal("isFinished", "true");//跳过工作流处理阶段变成为完成状态

        //获取本地文件InputStream
        File file = new File("C:\\log_network.txt");
        InputStream attachment = null;
        try {
            attachment = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //构建ECM传输所需的文件对象
        TransferFile tf = null;
        try {
            tf = new TransferFile(attachment, file.getName(), attachment.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //把文件对象添加到binder中
        mainBinder.addFile("primaryFile", tf);
        //执行服务
        DataBinder serverBinder = RidcUtil.executeService(mainBinder);
        System.out.println(serverBinder);

    }
}
