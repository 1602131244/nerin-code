package com.nerin.nims.opt.cqs.api;
import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.cqs.dto.*;
import com.nerin.nims.opt.cqs.service.IndexerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
/**
 * Created by user on 16/7/4.
 */
@RestController
@RequestMapping("/api/indexer")
public class IndexerRestController {
    @Autowired
    private IndexerService indexerService;


    /**
     * 适用介质
     * @return
     */
    @RequestMapping(value = "service", method = RequestMethod.GET)
    public List<ServiceDTO> getServiceList() {
        return indexerService.getServiceList();
    }
    /**
     * 基本材料
     * @return
     */
    @RequestMapping(value = "basicMaterial", method = RequestMethod.GET)
    public List<BasicMaterialDTO> getBasicMaterialList() {
        return indexerService.getBasicMaterialList();
    }
    /**
     * 压力等级
     * @return
     */
    @RequestMapping(value = "rating", method = RequestMethod.GET)
    public List<RatingDTO> getRatingList() {
        return indexerService.getRatingList();
    }
    /**
     * 管道压力等级
     * @return
     */
    @RequestMapping(value = "pipingMatlClass", method = RequestMethod.GET)
    public List<PipingMatlClassDTO> getPipingMatlClassList() {
        return indexerService.getPipingMatlClassList();
    }

    /**
     * 索引表
     * @param service
     * @param designTempValue
     * @param basicMaterial
     * @param pipingMatlClass
     * @param designPresValue
     * @param rating
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "indexer", method = RequestMethod.GET)
    public List<IndexerDTO> getIndexerList(@RequestParam(value = "service", required = false, defaultValue = "") String service,
                                           @RequestParam(value = "designTempValue", required = false, defaultValue = "")  Double designTempValue,
                                           @RequestParam(value = "basicMaterial", required = false, defaultValue = "") String basicMaterial,
                                           @RequestParam(value = "pipingMatlClass", required = false, defaultValue = "") String pipingMatlClass,
                                           @RequestParam(value = "designPresValue", required = false, defaultValue = "")  Double designPresValue,
                                           @RequestParam(value = "rating", required = false, defaultValue = "") String rating,
                                           @RequestParam(value = "pageNo", required = false, defaultValue = "1") Long pageNo,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "300") Long pageSize) {
        return indexerService.getIndexerList(service,designTempValue,basicMaterial,pipingMatlClass,designPresValue,rating);
    }
    /**
     * 管道材料等级头表
     * @param pipingMatlClass
     * @return
     */
    @RequestMapping(value = "pipingMatlClassHdr", method = RequestMethod.GET)
    public List<PipingMatlClassHdrDTO> getPipingMatlClassHdr(@RequestParam(value = "pipingMatlClass", required = true) String pipingMatlClass) {
        return indexerService.getPipingMatlClassHdr(pipingMatlClass);
    }
    /**
     * 温度压力额定表
     * @param pipingMatlClass
     * @return
     */
    @RequestMapping(value = "ptRatingTbl", method = RequestMethod.GET)
    public List<PtRatingTblDTO> getPtRatingTbl(@RequestParam(value = "pipingMatlClass", required = true) String pipingMatlClass) {
        return indexerService.getPtRatingTbl(pipingMatlClass);
    }
    /**
     * 元件表
     * @param pipingMatlClass
     * @return
     */
    @RequestMapping(value = "itemsTbl", method = RequestMethod.GET)
    public List<ItemsTblDTO> getItemsTbl(@RequestParam(value = "pipingMatlClass", required = true) String pipingMatlClass) {
        return indexerService.getItemsTbl(pipingMatlClass);
    }
    /**
     * 壁厚额定表
     * @param pipingMatlClass
     * @return
     */
    @RequestMapping(value = "pipeThicknessTbl", method = RequestMethod.GET)
    public List<PipeThicknessTblDTO> getPipeThicknessTbl(@RequestParam(value = "pipingMatlClass", required = true) String pipingMatlClass) {
        return indexerService.getPipeThicknessTbl(pipingMatlClass);
    }

    /**
     * 支管连接表
     * @param pipingMatlClass
     * @return
     */
    @RequestMapping(value = "branchConnectTbl", method = RequestMethod.GET)
    public List<BranchConnectTblDTO> getBranchConnectTbl(@RequestParam(value = "pipingMatlClass", required = true) String pipingMatlClass) {
        return indexerService.getBranchConnectTbl(pipingMatlClass);
    }

    /**
     * 加入收藏
     * @param p_piping_matl_class
     * @param p_flange_facing
     * @param p_basic_material
     * @param p_services
     * @param p_ca
     * @param p_design_temp_source
     * @param p_design_pres_source
     * @param request
     * @return
     */
    @RequestMapping(value = "addFavorite", method = RequestMethod.POST)
    public String addFavorite(@RequestParam(value = "p_piping_matl_class", required = false, defaultValue = "") String p_piping_matl_class
                              , @RequestParam(value = "p_flange_facing", required = false, defaultValue = "") String p_flange_facing
                              , @RequestParam(value = "p_basic_material", required = false, defaultValue = "") String p_basic_material
                              , @RequestParam(value = "p_services", required = false, defaultValue = "") String p_services
                              , @RequestParam(value = "p_ca", required = false, defaultValue = "") String p_ca
                              , @RequestParam(value = "p_design_temp_source", required = false, defaultValue = "") String p_design_temp_source
                              , @RequestParam(value = "p_design_pres_source", required = false, defaultValue = "") String p_design_pres_source
                              , HttpServletRequest request) {
        indexerService.addFavorite(SessionUtil.getLdapUserInfo(request).getEmployeeNo(), p_piping_matl_class, p_flange_facing, p_basic_material, p_services
        , p_ca, p_design_temp_source, p_design_pres_source);
        return "true";
    }

    /**
     * 删除收藏
     * @param p_piping_matl_class
     * @param request
     * @return
     */
    @RequestMapping(value = "delFavorite", method = RequestMethod.POST)
    public String addFavorite(@RequestParam(value = "p_piping_matl_class", required = false, defaultValue = "") String p_piping_matl_class
                              , HttpServletRequest request) {
        indexerService.delFavorite(SessionUtil.getLdapUserInfo(request).getEmployeeNo(), p_piping_matl_class);
        return "true";
    }

    /**
     * 查询我的收藏列表
     * @param request
     * @return
     */
    @RequestMapping(value = "getFavorite", method = RequestMethod.GET)
    public List<IndexerDTO> getFavorite(HttpServletRequest request) {
        return indexerService.getFavorite(SessionUtil.getLdapUserInfo(request).getEmployeeNo());
    }

    @RequestMapping(value = "getStandardNumberSource", method = RequestMethod.GET)
    public List<StandardNumberDTO> getStandardNumberSource(HttpServletRequest request) {
        return indexerService.getStandardNumberSource();
    }

    /**
     * 查询是否已收藏
     * @param request
     * @return
     */
    @RequestMapping(value = "exsitsFavorite", method = RequestMethod.GET)
    public int exsitsFavorite(@RequestParam(value = "p_piping_matl_class", required = false, defaultValue = "") String p_piping_matl_class
                                           , HttpServletRequest request) {
        return indexerService.exsitsFavorite(SessionUtil.getLdapUserInfo(request).getEmployeeNo(), p_piping_matl_class);
    }
}
