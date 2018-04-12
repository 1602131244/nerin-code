package com.nerin.nims.opt.cadi.dto;

/**
 * Created by yinglgu on 2016/12/16.
 */
public class CatConfigHeaderDTO {
    private String Plt_Order_Header_Id;//, -- 文印选项iD
    private String Project_Num;//, --项目编号
    private String Project_Name;//, --项目名称
    private String Task_Name;//, --系统子项名称
    private String Task_Code;//, --子项
    private String Main_Speciality;//, -- 专业
    private String Plt_Num;//, --文印单号
    private String Plt_Content;//, -- 文印内容
    private String Plt_Status;//, --文印状态
    private String Plt_Category;//, -- 文印类型
    private String Plt_Department;//, --部门
    private String Plt_Requestor;//, --申请人工号
    private String Creation_Date;//, -- 创建时间
    private String Project_Res_By;//, -- 项目负责人
    private String Spec_Manager;//, -- 专业负责人
    private String Spec_Manager_Name;//, -- 专业负责人
    private String Project_Secretary;//, --项目秘书
    private String Equipment_Name;//, -- 设备名称
    private String Print_Channel;//, -- 文印途径
    private String Plt_Requestor_Name;//, --申请人姓名
    private String Contract_Num;//, -- 合同份数
    private String Send_Owner;//, --发送业主
    private String Sg_Service;//, -- 施工服务
    private String Archive_Flag;//, -- 是否归档
    private String Paper_Type;//, -- 图类型
    private String New_Num;//, --新图
    private String New_Std_Num;//, -- 新图折A1
    private String Ink_Num;//, -- 底图
    private String Ink_Std_Num;//, -- 底图折A1

    private String print_size;// --文印幅面
    private String his_typeset;//, --需要排版
    private String bindiing_type;//, --装订要求
    private String is_inparts;//, --是否分册
    private String plt_comment;//, --其他文印要求

    private String Typeset_Total_Price;//, -- 核定价
    private String Plt_Total_Price;//, --文印总价
    private String Plt_Status_Name;//, -- 文印状态名称
    private String Plt_Category_Name;//, --文印类型名称
    private String Viewoa_Link;//, -- 查看OA审批链接
    private String Project_Id;

    public String getPrint_size() {
        return print_size;
    }

    public void setPrint_size(String print_size) {
        this.print_size = print_size;
    }

    public String getHis_typeset() {
        return his_typeset;
    }

    public void setHis_typeset(String his_typeset) {
        this.his_typeset = his_typeset;
    }

    public String getBindiing_type() {
        return bindiing_type;
    }

    public void setBindiing_type(String bindiing_type) {
        this.bindiing_type = bindiing_type;
    }

    public String getIs_inparts() {
        return is_inparts;
    }

    public void setIs_inparts(String is_inparts) {
        this.is_inparts = is_inparts;
    }

    public String getPlt_comment() {
        return plt_comment;
    }

    public void setPlt_comment(String plt_comment) {
        this.plt_comment = plt_comment;
    }

    public String getSpec_Manager_Name() {
        return Spec_Manager_Name;
    }

    public void setSpec_Manager_Name(String spec_Manager_Name) {
        Spec_Manager_Name = spec_Manager_Name;
    }

    public String getPlt_Order_Header_Id() {
        return Plt_Order_Header_Id;
    }

    public void setPlt_Order_Header_Id(String plt_Order_Header_Id) {
        Plt_Order_Header_Id = plt_Order_Header_Id;
    }

    public String getProject_Num() {
        return Project_Num;
    }

    public void setProject_Num(String project_Num) {
        Project_Num = project_Num;
    }

    public String getProject_Name() {
        return Project_Name;
    }

    public void setProject_Name(String project_Name) {
        Project_Name = project_Name;
    }

    public String getTask_Name() {
        return Task_Name;
    }

    public void setTask_Name(String task_Name) {
        Task_Name = task_Name;
    }

    public String getTask_Code() {
        return Task_Code;
    }

    public void setTask_Code(String task_Code) {
        Task_Code = task_Code;
    }

    public String getMain_Speciality() {
        return Main_Speciality;
    }

    public void setMain_Speciality(String main_Speciality) {
        Main_Speciality = main_Speciality;
    }

    public String getPlt_Num() {
        return Plt_Num;
    }

    public void setPlt_Num(String plt_Num) {
        Plt_Num = plt_Num;
    }

    public String getPlt_Content() {
        return Plt_Content;
    }

    public void setPlt_Content(String plt_Content) {
        Plt_Content = plt_Content;
    }

    public String getPlt_Status() {
        return Plt_Status;
    }

    public void setPlt_Status(String plt_Status) {
        Plt_Status = plt_Status;
    }

    public String getPlt_Category() {
        return Plt_Category;
    }

    public void setPlt_Category(String plt_Category) {
        Plt_Category = plt_Category;
    }

    public String getPlt_Department() {
        return Plt_Department;
    }

    public void setPlt_Department(String plt_Department) {
        Plt_Department = plt_Department;
    }

    public String getPlt_Requestor() {
        return Plt_Requestor;
    }

    public void setPlt_Requestor(String plt_Requestor) {
        Plt_Requestor = plt_Requestor;
    }

    public String getCreation_Date() {
        return Creation_Date;
    }

    public void setCreation_Date(String creation_Date) {
        Creation_Date = creation_Date;
    }

    public String getProject_Res_By() {
        return Project_Res_By;
    }

    public void setProject_Res_By(String project_Res_By) {
        Project_Res_By = project_Res_By;
    }

    public String getSpec_Manager() {
        return Spec_Manager;
    }

    public void setSpec_Manager(String spec_Manager) {
        Spec_Manager = spec_Manager;
    }

    public String getProject_Secretary() {
        return Project_Secretary;
    }

    public void setProject_Secretary(String project_Secretary) {
        Project_Secretary = project_Secretary;
    }

    public String getEquipment_Name() {
        return Equipment_Name;
    }

    public void setEquipment_Name(String equipment_Name) {
        Equipment_Name = equipment_Name;
    }

    public String getPrint_Channel() {
        return Print_Channel;
    }

    public void setPrint_Channel(String print_Channel) {
        Print_Channel = print_Channel;
    }

    public String getPlt_Requestor_Name() {
        return Plt_Requestor_Name;
    }

    public void setPlt_Requestor_Name(String plt_Requestor_Name) {
        Plt_Requestor_Name = plt_Requestor_Name;
    }

    public String getContract_Num() {
        return Contract_Num;
    }

    public void setContract_Num(String contract_Num) {
        Contract_Num = contract_Num;
    }

    public String getSend_Owner() {
        return Send_Owner;
    }

    public void setSend_Owner(String send_Owner) {
        Send_Owner = send_Owner;
    }

    public String getSg_Service() {
        return Sg_Service;
    }

    public void setSg_Service(String sg_Service) {
        Sg_Service = sg_Service;
    }

    public String getArchive_Flag() {
        return Archive_Flag;
    }

    public void setArchive_Flag(String archive_Flag) {
        Archive_Flag = archive_Flag;
    }

    public String getPaper_Type() {
        return Paper_Type;
    }

    public void setPaper_Type(String paper_Type) {
        Paper_Type = paper_Type;
    }

    public String getNew_Num() {
        return New_Num;
    }

    public void setNew_Num(String new_Num) {
        New_Num = new_Num;
    }

    public String getNew_Std_Num() {
        return New_Std_Num;
    }

    public void setNew_Std_Num(String new_Std_Num) {
        New_Std_Num = new_Std_Num;
    }

    public String getInk_Num() {
        return Ink_Num;
    }

    public void setInk_Num(String ink_Num) {
        Ink_Num = ink_Num;
    }

    public String getInk_Std_Num() {
        return Ink_Std_Num;
    }

    public void setInk_Std_Num(String ink_Std_Num) {
        Ink_Std_Num = ink_Std_Num;
    }

    public String getTypeset_Total_Price() {
        return Typeset_Total_Price;
    }

    public void setTypeset_Total_Price(String typeset_Total_Price) {
        Typeset_Total_Price = typeset_Total_Price;
    }

    public String getPlt_Total_Price() {
        return Plt_Total_Price;
    }

    public void setPlt_Total_Price(String plt_Total_Price) {
        Plt_Total_Price = plt_Total_Price;
    }

    public String getPlt_Status_Name() {
        return Plt_Status_Name;
    }

    public void setPlt_Status_Name(String plt_Status_Name) {
        Plt_Status_Name = plt_Status_Name;
    }

    public String getPlt_Category_Name() {
        return Plt_Category_Name;
    }

    public void setPlt_Category_Name(String plt_Category_Name) {
        Plt_Category_Name = plt_Category_Name;
    }

    public String getViewoa_Link() {
        return Viewoa_Link;
    }

    public void setViewoa_Link(String viewoa_Link) {
        Viewoa_Link = viewoa_Link;
    }

    public String getProject_Id() {
        return Project_Id;
    }

    public void setProject_Id(String project_Id) {
        Project_Id = project_Id;
    }
}
