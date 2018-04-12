package com.nerin.nims.opt.wbsp.service;

import com.nerin.nims.opt.wbsp.common.WbspParm;
import com.nerin.nims.opt.wbsp.dto.*;
import com.nerin.nims.opt.wbsp.module.PaElementsEntity;
import com.nerin.nims.opt.wbsp.module.PaIndustriesEntity;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2016/7/7.
 */
@Component
@Transactional
public class PaLevTwoPlanService {
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;


    private PaElementsEntity elmentsDtoToEntity(PaElementsDTO paElementsDTO) {
        PaElementsEntity entity = new PaElementsEntity();
        try {
            PropertyUtils.copyProperties(entity, paElementsDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private String getLevel(int type) {
        String level = new String();
        switch (type) {
            case 10003:
                level = "root";
                break;
            case 10004:
                level = "sys";
                break;
            case 10005:
                level = "div";
                break;
            case 10006:
                level = "spe";
                break;

        }

        return level;
    }

    private long getTypeId(String level) {
        long typeId = 0;
        switch (level) {
            case "root":
                typeId = 10003;
                break;
            case "sys":
                typeId = 10004;
                break;
            case "div":
                typeId = 10005;
                break;
            case "spe":
                typeId = 10006;
                break;

        }
        return typeId;
    }

    private Boolean getBool(String type) {
        Boolean result = false;
        switch (type) {
            case "Y":
                result = true;
                break;
            case "N":
                result = false;
                break;
        }
        return result;
    }

    private String getCode(String name, List<PaIndustriesAllDTO> paIndustriesAllDTOs) {
        String code = new String();
        for (int i = 0; i < paIndustriesAllDTOs.size(); i++) {
            if (paIndustriesAllDTOs.get(i).getName().equals(name)) {
                code = paIndustriesAllDTOs.get(i).getCode();
            }
        }
        return code;
    }

    /*public TreeListDTO treeGrid(List<? extends TreeDataDTO> treeDataDTO) {
        TreeListDTO treeListDTO = new TreeListDTO();
        Long parentId;
        for (int i = 0; i < treeDataDTO.size(); i++) {
            Long id = treeDataDTO.get(i).getPhaseID();
            Long pId = treeDataDTO.get(i).getParentId();
            if (id.equals(pId)) {
                treeListDTO.setTreeDataDTO(treeDataDTO.get(i));
                parentId = id;
                treeListDTO = findChild(treeListDTO , treeDataDTO, parentId);
            }
        }
        return treeListDTO;
    }
    private TreeListDTO findChild(TreeListDTO treeListDTO, List<? extends TreeDataDTO> treeDataDTO, Long parentId) {
        TreeListDTO tmpData = new TreeListDTO();
        TreeListDTO tmpData1 = new TreeListDTO();
        List<TreeListDTO> tmpList = new ArrayList<TreeListDTO>();
        tmpList = treeListDTO.getChildren();
        Long nextParent;
        for (int i = 0; i < treeDataDTO.size(); i++) {
            if (parentId.equals(treeDataDTO.get(i).getParentId()) && !parentId.equals(treeDataDTO.get(i).getPhaseID()) ) {
                //tmpData = new TreePaPublishStructureDetailsDTO();
                tmpData.setTreeDataDTO(treeDataDTO.get(i));
                nextParent = treeDataDTO.get(i).getPhaseID();
                tmpData1 = findChild(tmpData, treeDataDTO, nextParent);
                tmpList.add(tmpData1);
                treeListDTO.setChildren(tmpList);
            }
        }
        return treeListDTO;
    }*/
    public List<TreePaPublishStructureDetailsDTO> treePublishList(List<PaPublishStructureDetailsDTO> paPublishStructureDetailsDTOs, String level) {
        List<TreePaPublishStructureDetailsDTO> treePaPublishStructureDetailsDTOs = new ArrayList<TreePaPublishStructureDetailsDTO>();
        TreePaPublishStructureDetailsDTO treePaPublishStructureDetailsDTO = new TreePaPublishStructureDetailsDTO();
        Long parentId;
        for (int i = 0; i < paPublishStructureDetailsDTOs.size(); i++) {
            Long id = paPublishStructureDetailsDTOs.get(i).getId();
            Long pId = paPublishStructureDetailsDTOs.get(i).getParentId();
            if (id.equals(pId)) {
                treePaPublishStructureDetailsDTO.setId(paPublishStructureDetailsDTOs.get(i).getId());
                treePaPublishStructureDetailsDTO.setParentId(paPublishStructureDetailsDTOs.get(i).getParentId());
                treePaPublishStructureDetailsDTO.setDualName(paPublishStructureDetailsDTOs.get(i).getTaskBilingual());
                treePaPublishStructureDetailsDTO.setTaskElementId(paPublishStructureDetailsDTOs.get(i).getTaskElementId());
                treePaPublishStructureDetailsDTO.setName(paPublishStructureDetailsDTOs.get(i).getTaskName());
                treePaPublishStructureDetailsDTO.setStartDate(paPublishStructureDetailsDTOs.get(i).getTaskStartDate());
                treePaPublishStructureDetailsDTO.setEndDate(paPublishStructureDetailsDTOs.get(i).getTaskEndDate());
                treePaPublishStructureDetailsDTO.setStatus(paPublishStructureDetailsDTOs.get(i).getTaskStatusCode());
                //treePaPublishStructureDetailsDTO.setTaskStructureName(paPublishStructureDetailsDTOs.get(i).getTaskStructureName());
                treePaPublishStructureDetailsDTO.setTaskStructureVerId(paPublishStructureDetailsDTOs.get(i).getTaskStructureVerId());
                treePaPublishStructureDetailsDTO.setTaskTypeId(paPublishStructureDetailsDTOs.get(i).getTaskTypeId());
                treePaPublishStructureDetailsDTO.setCode(paPublishStructureDetailsDTOs.get(i).getTaskWbsNumber());
                treePaPublishStructureDetailsDTO.setTaskType(paPublishStructureDetailsDTOs.get(i).getTaskType());
                treePaPublishStructureDetailsDTO.setWorkCoef(paPublishStructureDetailsDTOs.get(i).getTaskWorkload());
                treePaPublishStructureDetailsDTO.setLevel(paPublishStructureDetailsDTOs.get(i).getLevel());
                // treePaPublishStructureDetailsDTO.setTaskWbsLevel(paPublishStructureDetailsDTOs.get(i).getTaskWbsLevel());
                parentId = id;
                treePaPublishStructureDetailsDTO = findChild(treePaPublishStructureDetailsDTO, paPublishStructureDetailsDTOs, parentId, level);
                treePaPublishStructureDetailsDTOs.add(treePaPublishStructureDetailsDTO);
            }

        }
        return treePaPublishStructureDetailsDTOs;
    }

    private TreePaPublishStructureDetailsDTO findChild(TreePaPublishStructureDetailsDTO treePaPublishStructureDetailsDTO, List<PaPublishStructureDetailsDTO> paPublishStructureDetailsDTOs, Long parentId, String level) {
        TreePaPublishStructureDetailsDTO tmpData = new TreePaPublishStructureDetailsDTO();
        //TreePaPublishStructureDetailsDTO tmpData1;
        List<TreePaPublishStructureDetailsDTO> tmpList = new ArrayList<TreePaPublishStructureDetailsDTO>();
        tmpList = treePaPublishStructureDetailsDTO.getChildren();
        Long nextParent;
        for (int i = 0; i < paPublishStructureDetailsDTOs.size(); i++) {
            if (parentId.equals(paPublishStructureDetailsDTOs.get(i).getParentId()) && !parentId.equals(paPublishStructureDetailsDTOs.get(i).getId())) {
                nextParent = paPublishStructureDetailsDTOs.get(i).getId();
                tmpData = new TreePaPublishStructureDetailsDTO();
                if ((paPublishStructureDetailsDTOs.get(i).getLevel().equals("sys") && level.equals("SUB"))
                        || ((paPublishStructureDetailsDTOs.get(i).getLevel().equals("sys") || paPublishStructureDetailsDTOs.get(i).getLevel().equals("div"))
                        && level.equals("SPEC"))
                        ) {
                    if (paPublishStructureDetailsDTOs.get(i).getDefaultFlag() != null && paPublishStructureDetailsDTOs.get(i).getDefaultFlag().equals("default")) {
                        TreePaPublishStructureDetailsDTO tmp = findChild(treePaPublishStructureDetailsDTO, paPublishStructureDetailsDTOs, nextParent, level);
                        return tmp;
                    }

                } else {
                    tmpData.setId(paPublishStructureDetailsDTOs.get(i).getId());
                    tmpData.setParentId(paPublishStructureDetailsDTOs.get(i).getParentId());
                    tmpData.setDualName(paPublishStructureDetailsDTOs.get(i).getTaskBilingual());
                    tmpData.setTaskElementId(paPublishStructureDetailsDTOs.get(i).getTaskElementId());
                    tmpData.setName(paPublishStructureDetailsDTOs.get(i).getTaskName());
                    tmpData.setStartDate(paPublishStructureDetailsDTOs.get(i).getTaskStartDate());
                    tmpData.setEndDate(paPublishStructureDetailsDTOs.get(i).getTaskEndDate());
                    tmpData.setStatus(paPublishStructureDetailsDTOs.get(i).getTaskStatusCode());
                    //tmpData.setTaskStructureName(paPublishStructureDetailsDTOs.get(i).getTaskStructureName());
                    tmpData.setTaskStructureVerId(paPublishStructureDetailsDTOs.get(i).getTaskStructureVerId());
                    tmpData.setTaskTypeId(paPublishStructureDetailsDTOs.get(i).getTaskTypeId());
                    tmpData.setCode(paPublishStructureDetailsDTOs.get(i).getTaskWbsNumber());
                    tmpData.setTaskType(paPublishStructureDetailsDTOs.get(i).getTaskType());
                    tmpData.setWorkCoef(paPublishStructureDetailsDTOs.get(i).getTaskWorkload());
                    //tmpData.setTaskWbsLevel(paPublishStructureDetailsDTOs.get(i).getTaskWbsLevel());
                    tmpData.setLevel(paPublishStructureDetailsDTOs.get(i).getLevel());

                    tmpList.add(findChild(tmpData, paPublishStructureDetailsDTOs, nextParent, level));
                    treePaPublishStructureDetailsDTO.setChildren(tmpList);
                    if (treePaPublishStructureDetailsDTO.getId() != null) {
                        treePaPublishStructureDetailsDTO.setChildren(tmpList);
                    } else {
                        return tmpData;
                    }
                }
            }
        }

        return treePaPublishStructureDetailsDTO;
    }

    public List<TreePaWorkingStructureDetailsDTO> treePaWorkingList(List<PaWorkingStructureDetailsDTO> paWorkingStructureDetailsDTOs, String level) {
        List<TreePaWorkingStructureDetailsDTO> treePaWorkingStructureDetailsDTOs = new ArrayList<TreePaWorkingStructureDetailsDTO>();
        TreePaWorkingStructureDetailsDTO treePaWorkingStructureDetailsDTO = new TreePaWorkingStructureDetailsDTO();
        Long parentId;
        for (int i = 0; i < paWorkingStructureDetailsDTOs.size(); i++) {
            Long id = paWorkingStructureDetailsDTOs.get(i).getId();
            Long pId = paWorkingStructureDetailsDTOs.get(i).getParentId();
            if (id.equals(pId)) {
                treePaWorkingStructureDetailsDTO.setId(paWorkingStructureDetailsDTOs.get(i).getId());
                treePaWorkingStructureDetailsDTO.setParentId(paWorkingStructureDetailsDTOs.get(i).getParentId());
                treePaWorkingStructureDetailsDTO.setDualName(paWorkingStructureDetailsDTOs.get(i).getTaskBilingual());
                treePaWorkingStructureDetailsDTO.setProjElementId(paWorkingStructureDetailsDTOs.get(i).getTaskElementId());
                treePaWorkingStructureDetailsDTO.setName(paWorkingStructureDetailsDTOs.get(i).getTaskName());
                treePaWorkingStructureDetailsDTO.setStartDate(paWorkingStructureDetailsDTOs.get(i).getTaskStartDate());
                treePaWorkingStructureDetailsDTO.setEndDate(paWorkingStructureDetailsDTOs.get(i).getTaskEndDate());
                treePaWorkingStructureDetailsDTO.setStatus(paWorkingStructureDetailsDTOs.get(i).getTaskStatusCode());
                treePaWorkingStructureDetailsDTO.setSrc(paWorkingStructureDetailsDTOs.get(i).getTaskSourceStructurename());
                treePaWorkingStructureDetailsDTO.setSrcID(paWorkingStructureDetailsDTOs.get(i).getTaskSourceStructureVerId());
                treePaWorkingStructureDetailsDTO.setTaskTypeId(paWorkingStructureDetailsDTOs.get(i).getTaskTypeId());
                treePaWorkingStructureDetailsDTO.setCode(paWorkingStructureDetailsDTOs.get(i).getTaskWbsNumber());
                treePaWorkingStructureDetailsDTO.setTaskType(paWorkingStructureDetailsDTOs.get(i).getTaskType());
                treePaWorkingStructureDetailsDTO.setWorkCoef(paWorkingStructureDetailsDTOs.get(i).getTaskWorkload());
                treePaWorkingStructureDetailsDTO.setFlag(paWorkingStructureDetailsDTOs.get(i).getFlag());
                treePaWorkingStructureDetailsDTO.setLevel(paWorkingStructureDetailsDTOs.get(i).getLevel());
                // treePaWorkingStructureDetailsDTO.setTaskWbsLevel(paWorkingStructureDetailsDTOs.get(i).getTaskWbsLevel());
                parentId = id;
                treePaWorkingStructureDetailsDTO = findChild(treePaWorkingStructureDetailsDTO, paWorkingStructureDetailsDTOs, parentId, level);
                treePaWorkingStructureDetailsDTOs.add(treePaWorkingStructureDetailsDTO);
            }
        }
        return treePaWorkingStructureDetailsDTOs;
    }

    private TreePaWorkingStructureDetailsDTO findChild(TreePaWorkingStructureDetailsDTO treePaWorkingStructureDetailsDTO, List<PaWorkingStructureDetailsDTO> paWorkingStructureDetailsDTOs, Long parentId, String level) {

        TreePaWorkingStructureDetailsDTO tmpData;
        List<TreePaWorkingStructureDetailsDTO> tmpList = new ArrayList<TreePaWorkingStructureDetailsDTO>();
        tmpList = treePaWorkingStructureDetailsDTO.getChildren();
        Long nextParent;
        for (int i = 0; i < paWorkingStructureDetailsDTOs.size(); i++) {
            if (parentId.equals(paWorkingStructureDetailsDTOs.get(i).getParentId()) && !parentId.equals(paWorkingStructureDetailsDTOs.get(i).getId())) {
                nextParent = paWorkingStructureDetailsDTOs.get(i).getId();
                tmpData = new TreePaWorkingStructureDetailsDTO();
                if ((paWorkingStructureDetailsDTOs.get(i).getLevel().equals("sys") && level.equals("SUB"))
                        || ((paWorkingStructureDetailsDTOs.get(i).getLevel().equals("sys") || paWorkingStructureDetailsDTOs.get(i).getLevel().equals("div"))
                        && level.equals("SPEC"))
                        ) {
                    if (paWorkingStructureDetailsDTOs.get(i).getDefaultFlag() != null && paWorkingStructureDetailsDTOs.get(i).getDefaultFlag().equals("default")) {
                        TreePaWorkingStructureDetailsDTO tmp = findChild(treePaWorkingStructureDetailsDTO, paWorkingStructureDetailsDTOs, nextParent, level);
                       return tmp;
                    }

                } else {
                    tmpData.setId(paWorkingStructureDetailsDTOs.get(i).getId());
                    tmpData.setParentId(paWorkingStructureDetailsDTOs.get(i).getParentId());
                    tmpData.setDualName(paWorkingStructureDetailsDTOs.get(i).getTaskBilingual());
                    tmpData.setProjElementId(paWorkingStructureDetailsDTOs.get(i).getTaskElementId());
                    tmpData.setName(paWorkingStructureDetailsDTOs.get(i).getTaskName());
                    tmpData.setStartDate(paWorkingStructureDetailsDTOs.get(i).getTaskStartDate());
                    tmpData.setEndDate(paWorkingStructureDetailsDTOs.get(i).getTaskEndDate());
                    tmpData.setStatus(paWorkingStructureDetailsDTOs.get(i).getTaskStatusCode());
                    tmpData.setSrc(paWorkingStructureDetailsDTOs.get(i).getTaskSourceStructurename());
                    tmpData.setSrcID(paWorkingStructureDetailsDTOs.get(i).getTaskSourceEleVerId());
                    tmpData.setTaskTypeId(paWorkingStructureDetailsDTOs.get(i).getTaskTypeId());
                    tmpData.setCode(paWorkingStructureDetailsDTOs.get(i).getTaskWbsNumber());
                    tmpData.setCode(paWorkingStructureDetailsDTOs.get(i).getTaskWbsNumber());
                    tmpData.setTaskType(paWorkingStructureDetailsDTOs.get(i).getTaskType());
                    tmpData.setWorkCoef(paWorkingStructureDetailsDTOs.get(i).getTaskWorkload());
                    tmpData.setFlag(paWorkingStructureDetailsDTOs.get(i).getFlag());
                    tmpData.setLevel(paWorkingStructureDetailsDTOs.get(i).getLevel());
                    tmpList.add(findChild(tmpData, paWorkingStructureDetailsDTOs, nextParent, level));
                    if (treePaWorkingStructureDetailsDTO.getId() != null) {
                        treePaWorkingStructureDetailsDTO.setChildren(tmpList);
                    } else {
                        return tmpData;
                    }
                }
            }
        }

        return treePaWorkingStructureDetailsDTO;
    }

    public List<TreePaHistoryWbsDetailsDTO> treeHisWbsDetailsList(List<PaHistoryWbsDetailsDTO> paHistoryWbsDetailsDTO, String level) {
        List<TreePaHistoryWbsDetailsDTO> treePaHistoryWbsDetailsDTOs = new ArrayList<TreePaHistoryWbsDetailsDTO>();
        TreePaHistoryWbsDetailsDTO treePaHistoryWbsDetailsDTO = new TreePaHistoryWbsDetailsDTO();
        Long parentId;
        for (int i = 0; i < paHistoryWbsDetailsDTO.size(); i++) {
            Long id = paHistoryWbsDetailsDTO.get(i).getId();
            Long pId = paHistoryWbsDetailsDTO.get(i).getParentId();
            if (id.equals(pId)) {
                treePaHistoryWbsDetailsDTO.setId(paHistoryWbsDetailsDTO.get(i).getId());
                treePaHistoryWbsDetailsDTO.setParentId(paHistoryWbsDetailsDTO.get(i).getParentId());
                treePaHistoryWbsDetailsDTO.setDualName(paHistoryWbsDetailsDTO.get(i).getDualName());
                treePaHistoryWbsDetailsDTO.setName(paHistoryWbsDetailsDTO.get(i).getName());
                treePaHistoryWbsDetailsDTO.setEndDate(paHistoryWbsDetailsDTO.get(i).getEndDate());
                treePaHistoryWbsDetailsDTO.setStartDate(paHistoryWbsDetailsDTO.get(i).getStartDate());
                treePaHistoryWbsDetailsDTO.setCode(paHistoryWbsDetailsDTO.get(i).getCode());
                treePaHistoryWbsDetailsDTO.setLevel(paHistoryWbsDetailsDTO.get(i).getLevel());
                treePaHistoryWbsDetailsDTO.setWorkCoef(paHistoryWbsDetailsDTO.get(i).getWorkCoef());
                treePaHistoryWbsDetailsDTO.setStatus(paHistoryWbsDetailsDTO.get(i).getTaskStatusCode());
                //treePaHistoryWbsDetailsDTO.setPbsOrWbs(paHistoryWbsDetailsDTO.get(i).getPbsOrWbs());
                treePaHistoryWbsDetailsDTO.setWbsId(paHistoryWbsDetailsDTO.get(i).getWbsId());
                //   treePaHistoryWbsDetailsDTO.setTaskWbsLevel(paHistoryWbsDetailsDTO.get(i).getTaskWbsLevel());
                parentId = id;
                treePaHistoryWbsDetailsDTO = findChild(treePaHistoryWbsDetailsDTO, paHistoryWbsDetailsDTO, parentId, level);
                treePaHistoryWbsDetailsDTOs.add(treePaHistoryWbsDetailsDTO);
            }

        }
        return treePaHistoryWbsDetailsDTOs;
    }

    private TreePaHistoryWbsDetailsDTO findChild(TreePaHistoryWbsDetailsDTO treePaHistoryWbsDetailsDTO, List<PaHistoryWbsDetailsDTO> paHistoryWbsDetailsDTO, Long parentId, String level) {
        TreePaHistoryWbsDetailsDTO tmpData;
        List<TreePaHistoryWbsDetailsDTO> tmpList = new ArrayList<TreePaHistoryWbsDetailsDTO>();
        tmpList = treePaHistoryWbsDetailsDTO.getChildren();
        Long nextParent;
        for (int i = 0; i < paHistoryWbsDetailsDTO.size(); i++) {
            if (parentId.equals(paHistoryWbsDetailsDTO.get(i).getParentId()) && !parentId.equals(paHistoryWbsDetailsDTO.get(i).getId())) {
                nextParent = paHistoryWbsDetailsDTO.get(i).getId();
                tmpData = new TreePaHistoryWbsDetailsDTO();
                if ((paHistoryWbsDetailsDTO.get(i).getLevel().equals("sys") && level.equals("SUB"))
                        || ((paHistoryWbsDetailsDTO.get(i).getLevel().equals("sys") || paHistoryWbsDetailsDTO.get(i).getLevel().equals("div"))
                        && level.equals("SPEC"))
                        ) {
                    if (paHistoryWbsDetailsDTO.get(i).getDefaultFlag() != null && paHistoryWbsDetailsDTO.get(i).getDefaultFlag().equals("default")) {
                        TreePaHistoryWbsDetailsDTO tmp = findChild(treePaHistoryWbsDetailsDTO, paHistoryWbsDetailsDTO, nextParent, level);
                        return tmp;
                    }

                } else {
                    tmpData.setId(paHistoryWbsDetailsDTO.get(i).getId());
                    tmpData.setParentId(paHistoryWbsDetailsDTO.get(i).getParentId());
                    tmpData.setDualName(paHistoryWbsDetailsDTO.get(i).getDualName());
                    tmpData.setName(paHistoryWbsDetailsDTO.get(i).getName());
                    tmpData.setEndDate(paHistoryWbsDetailsDTO.get(i).getEndDate());
                    tmpData.setStartDate(paHistoryWbsDetailsDTO.get(i).getStartDate());
                    tmpData.setCode(paHistoryWbsDetailsDTO.get(i).getCode());
                    tmpData.setLevel(paHistoryWbsDetailsDTO.get(i).getLevel());
                    tmpData.setStatus(paHistoryWbsDetailsDTO.get(i).getTaskStatusCode());
                    treePaHistoryWbsDetailsDTO.setWorkCoef(paHistoryWbsDetailsDTO.get(i).getWorkCoef());
                    //tmpData.setPbsOrWbs(paHistoryWbsDetailsDTO.get(i).getPbsOrWbs());
                    tmpData.setWbsId(paHistoryWbsDetailsDTO.get(i).getWbsId());
                    //    tmpData.setTaskWbsLevel(paHistoryWbsDetailsDTO.get(i).getTaskWbsLevel());
                    nextParent = paHistoryWbsDetailsDTO.get(i).getId();
                    tmpList.add(findChild(tmpData, paHistoryWbsDetailsDTO, nextParent, level));
                    treePaHistoryWbsDetailsDTO.setChildren(tmpList);
                    if (treePaHistoryWbsDetailsDTO.getId() != null) {
                        treePaHistoryWbsDetailsDTO.setChildren(tmpList);
                    } else {
                        return tmpData;
                    }
                }
            }
        }

        return treePaHistoryWbsDetailsDTO;
    }

    public List<TreePaHistoryPbsDetailsDTO> treeHisPbsDetailsList(List<PaHistoryPbsDetailsDTO> paHistoryPbsDetailsDTOs) {
        List<TreePaHistoryPbsDetailsDTO> treePaHistoryPbsDetailsDTOs = new ArrayList<TreePaHistoryPbsDetailsDTO>();
        TreePaHistoryPbsDetailsDTO treePaHistoryPbsDetailsDTO = new TreePaHistoryPbsDetailsDTO();
        Long parentId;
        for (int i = 0; i < paHistoryPbsDetailsDTOs.size(); i++) {
            Long id = paHistoryPbsDetailsDTOs.get(i).getId();
            Long pId = paHistoryPbsDetailsDTOs.get(i).getParentId();
            if (id.equals(pId)) {
                treePaHistoryPbsDetailsDTO.setId(paHistoryPbsDetailsDTOs.get(i).getId());
                treePaHistoryPbsDetailsDTO.setParentId(paHistoryPbsDetailsDTOs.get(i).getParentId());
                treePaHistoryPbsDetailsDTO.setDualName(paHistoryPbsDetailsDTOs.get(i).getDualName());
                treePaHistoryPbsDetailsDTO.setName(paHistoryPbsDetailsDTOs.get(i).getName());
                treePaHistoryPbsDetailsDTO.setCode(paHistoryPbsDetailsDTOs.get(i).getCode());
                treePaHistoryPbsDetailsDTO.setLevel(paHistoryPbsDetailsDTOs.get(i).getLevel());
                //treePaHistoryWbsDetailsDTO.setPbsOrWbs(paHistoryWbsDetailsDTO.get(i).getPbsOrWbs());
                treePaHistoryPbsDetailsDTO.setPbsId(paHistoryPbsDetailsDTOs.get(i).getPbsID());
                //   treePaHistoryWbsDetailsDTO.setTaskWbsLevel(paHistoryWbsDetailsDTO.get(i).getTaskWbsLevel());
                parentId = id;
                treePaHistoryPbsDetailsDTO = findChild(treePaHistoryPbsDetailsDTO, paHistoryPbsDetailsDTOs, parentId);
                treePaHistoryPbsDetailsDTOs.add(treePaHistoryPbsDetailsDTO);
            }

        }
        return treePaHistoryPbsDetailsDTOs;
    }

    private TreePaHistoryPbsDetailsDTO findChild(TreePaHistoryPbsDetailsDTO treePaHistoryPbsDetailsDTO, List<PaHistoryPbsDetailsDTO> paHistoryPbsDetailsDTOs, Long parentId) {
        TreePaHistoryPbsDetailsDTO tmpData;
        List<TreePaHistoryPbsDetailsDTO> tmpList = new ArrayList<TreePaHistoryPbsDetailsDTO>();
        tmpList = treePaHistoryPbsDetailsDTO.getChildren();
        Long nextParent;
        for (int i = 0; i < paHistoryPbsDetailsDTOs.size(); i++) {
            if (parentId.equals(paHistoryPbsDetailsDTOs.get(i).getParentId()) && !parentId.equals(paHistoryPbsDetailsDTOs.get(i).getId())) {
                tmpData = new TreePaHistoryPbsDetailsDTO();
                tmpData.setId(paHistoryPbsDetailsDTOs.get(i).getId());
                tmpData.setParentId(paHistoryPbsDetailsDTOs.get(i).getParentId());
                tmpData.setDualName(paHistoryPbsDetailsDTOs.get(i).getDualName());
                tmpData.setName(paHistoryPbsDetailsDTOs.get(i).getName());
                tmpData.setCode(paHistoryPbsDetailsDTOs.get(i).getCode());
                tmpData.setLevel(paHistoryPbsDetailsDTOs.get(i).getLevel());
                //tmpData.setPbsOrWbs(paHistoryWbsDetailsDTO.get(i).getPbsOrWbs());
                tmpData.setPbsId(paHistoryPbsDetailsDTOs.get(i).getPbsID());
                //    tmpData.setTaskWbsLevel(paHistoryWbsDetailsDTO.get(i).getTaskWbsLevel());
                nextParent = paHistoryPbsDetailsDTOs.get(i).getId();
                tmpList.add(findChild(tmpData, paHistoryPbsDetailsDTOs, nextParent));
                treePaHistoryPbsDetailsDTO.setChildren(tmpList);
            }
        }

        return treePaHistoryPbsDetailsDTO;
    }
    /*public List<TreePaHistoryWbsDetailsDTO> treeHisDetailsList(List<PaHistoryWbsDetailsDTO> paHistoryStructureDetailsDTO) {
        List<TreePaHistoryWbsDetailsDTO> treePaHistoryStructureDetailsDTOs = new ArrayList<TreePaHistoryWbsDetailsDTO>();
        TreePaHistoryWbsDetailsDTO treePaHistoryStructureDetailsDTO = new TreePaHistoryWbsDetailsDTO();
        Long parentId;
        for (int i = 0; i < paHistoryStructureDetailsDTO.size(); i++) {
            Long id = paHistoryStructureDetailsDTO.get(i).getId();
            Long pId = paHistoryStructureDetailsDTO.get(i).getParentId();
            if (id.equals(pId)) {
                treePaHistoryStructureDetailsDTO.setId(paHistoryStructureDetailsDTO.get(i).getId());
                treePaHistoryStructureDetailsDTO.setParentId(paHistoryStructureDetailsDTO.get(i).getParentId());
                treePaHistoryStructureDetailsDTO.setDualName(paHistoryStructureDetailsDTO.get(i).getDualName());
                treePaHistoryStructureDetailsDTO.setName(paHistoryStructureDetailsDTO.get(i).getName());
                treePaHistoryStructureDetailsDTO.setEndDate(paHistoryStructureDetailsDTO.get(i).getEndDate());
                treePaHistoryStructureDetailsDTO.setStartDate(paHistoryStructureDetailsDTO.get(i).getStartDate());
                treePaHistoryStructureDetailsDTO.setCode(paHistoryStructureDetailsDTO.get(i).getCode());
                treePaHistoryStructureDetailsDTO.setLevel(paHistoryStructureDetailsDTO.get(i).getLevel());
                treePaHistoryStructureDetailsDTO.setPbsOrWbs(paHistoryStructureDetailsDTO.get(i).getPbsOrWbs());
                treePaHistoryStructureDetailsDTO.setStructureVerId(paHistoryStructureDetailsDTO.get(i).getStructureVerId());
                //   treePaHistoryStructureDetailsDTO.setTaskWbsLevel(paHistoryStructureDetailsDTO.get(i).getTaskWbsLevel());
                parentId = id;
                treePaHistoryStructureDetailsDTO = findChild(treePaHistoryStructureDetailsDTO, paHistoryStructureDetailsDTO, parentId);
                treePaHistoryStructureDetailsDTOs.add(treePaHistoryStructureDetailsDTO);
            }

        }
        return treePaHistoryStructureDetailsDTOs;
    }

    private TreePaHistoryWbsDetailsDTO findChild(TreePaHistoryWbsDetailsDTO treePaHistoryStructureDetailsDTO, List<PaHistoryWbsDetailsDTO> paHistoryStructureDetailsDTO, Long parentId) {
        TreePaHistoryWbsDetailsDTO tmpData;
        List<TreePaHistoryWbsDetailsDTO> tmpList = new ArrayList<TreePaHistoryWbsDetailsDTO>();
        tmpList = treePaHistoryStructureDetailsDTO.getChildren();
        Long nextParent;
        for (int i = 0; i < paHistoryStructureDetailsDTO.size(); i++) {
            if (parentId.equals(paHistoryStructureDetailsDTO.get(i).getParentId()) && !parentId.equals(paHistoryStructureDetailsDTO.get(i).getId())) {
                tmpData = new TreePaHistoryWbsDetailsDTO();
                tmpData.setId(paHistoryStructureDetailsDTO.get(i).getId());
                tmpData.setParentId(paHistoryStructureDetailsDTO.get(i).getParentId());
                tmpData.setDualName(paHistoryStructureDetailsDTO.get(i).getDualName());
                tmpData.setName(paHistoryStructureDetailsDTO.get(i).getName());
                tmpData.setEndDate(paHistoryStructureDetailsDTO.get(i).getEndDate());
                tmpData.setStartDate(paHistoryStructureDetailsDTO.get(i).getStartDate());
                tmpData.setCode(paHistoryStructureDetailsDTO.get(i).getCode());
                tmpData.setLevel(paHistoryStructureDetailsDTO.get(i).getLevel());
                tmpData.setPbsOrWbs(paHistoryStructureDetailsDTO.get(i).getPbsOrWbs());
                tmpData.setStructureVerId(paHistoryStructureDetailsDTO.get(i).getStructureVerId());
                //    tmpData.setTaskWbsLevel(paHistoryStructureDetailsDTO.get(i).getTaskWbsLevel());
                nextParent = paHistoryStructureDetailsDTO.get(i).getId();
                tmpList.add(findChild(tmpData, paHistoryStructureDetailsDTO, nextParent));
                treePaHistoryStructureDetailsDTO.setChildren(tmpList);
            }
        }

        return treePaHistoryStructureDetailsDTO;
    }*/

    public List<TreePaHistoryStructureDTO> treePaHistoryList(List<PaHistoryStructureDTO> paHistoryStructureDTO) {
        List<TreePaHistoryStructureDTO> treePaHistoryStructureDTOs = new ArrayList<TreePaHistoryStructureDTO>();
        TreePaHistoryStructureDTO treePaHistoryStructureDTO;
        Long parentId;
        for (int i = 0; i < paHistoryStructureDTO.size(); i++) {
            Long id = paHistoryStructureDTO.get(i).getId();
            String name = paHistoryStructureDTO.get(i).getStrClass();
            if (name.equals("ROOT")) {
                treePaHistoryStructureDTO = new TreePaHistoryStructureDTO();
                treePaHistoryStructureDTO.setId(paHistoryStructureDTO.get(i).getId());
                treePaHistoryStructureDTO.setParentId(paHistoryStructureDTO.get(i).getParentId());
                treePaHistoryStructureDTO.setPhaseID(paHistoryStructureDTO.get(i).getPhasecode());
                treePaHistoryStructureDTO.setTime(paHistoryStructureDTO.get(i).getTime());
                treePaHistoryStructureDTO.setOperator(paHistoryStructureDTO.get(i).getOperator());
                treePaHistoryStructureDTO.setPhaseList(paHistoryStructureDTO.get(i).getPhaseList());
                treePaHistoryStructureDTO.setProjectId(paHistoryStructureDTO.get(i).getProjectId());
                treePaHistoryStructureDTO.setStrClass(paHistoryStructureDTO.get(i).getStrClass());
                //treePaHistoryStructureDTO.setStrClassCode(paHistoryStructureDTO.get(i).getStrClassCode());
                treePaHistoryStructureDTO.setStructureId(paHistoryStructureDTO.get(i).getStructureId());
                treePaHistoryStructureDTO.setName(paHistoryStructureDTO.get(i).getName());
                parentId = id;
                treePaHistoryStructureDTO = findChild(treePaHistoryStructureDTO, paHistoryStructureDTO, parentId);
                treePaHistoryStructureDTOs.add(treePaHistoryStructureDTO);
            }
        }
        return treePaHistoryStructureDTOs;
    }

    private TreePaHistoryStructureDTO findChild(TreePaHistoryStructureDTO treePaHistoryStructureDTO, List<PaHistoryStructureDTO> paHistoryStructureDTO, Long parentId) {
        TreePaHistoryStructureDTO tmpData;
        List<TreePaHistoryStructureDTO> tmpList = new ArrayList<TreePaHistoryStructureDTO>();
        tmpList = treePaHistoryStructureDTO.getChildren();
        Long nextParent;
        for (int i = 0; i < paHistoryStructureDTO.size(); i++) {
            if (parentId.equals(paHistoryStructureDTO.get(i).getParentId()) && !parentId.equals(paHistoryStructureDTO.get(i).getId())) {
                tmpData = new TreePaHistoryStructureDTO();
                tmpData.setId(paHistoryStructureDTO.get(i).getId());
                tmpData.setParentId(paHistoryStructureDTO.get(i).getParentId());
                tmpData.setPhaseID(paHistoryStructureDTO.get(i).getPhasecode());
                tmpData.setTime(paHistoryStructureDTO.get(i).getTime());
                tmpData.setOperator(paHistoryStructureDTO.get(i).getOperator());
                tmpData.setPhaseList(paHistoryStructureDTO.get(i).getPhaseList());
                tmpData.setProjectId(paHistoryStructureDTO.get(i).getProjectId());
                tmpData.setStrClass(paHistoryStructureDTO.get(i).getStrClass());
                //tmpData.setStrClassCode(paHistoryStructureDTO.get(i).getStrClassCode());
                tmpData.setStructureId(paHistoryStructureDTO.get(i).getStructureId());
                tmpData.setName(paHistoryStructureDTO.get(i).getName());
                nextParent = paHistoryStructureDTO.get(i).getId();
                tmpList.add(findChild(tmpData, paHistoryStructureDTO, nextParent));
                treePaHistoryStructureDTO.setChildren(tmpList);
                if (tmpList.size() > 0) {
                    treePaHistoryStructureDTO.setState("closed");
                }
            }
        }

        return treePaHistoryStructureDTO;
    }

    private PaIndustriesEntity paIndustriesDtoToEntity(PaIndustriesDTO paIndustriesDTO, long projectId, long userId) {
        PaIndustriesEntity entity = new PaIndustriesEntity();
        try {
            PropertyUtils.copyProperties(entity, paIndustriesDTO);
            entity.setProjectId(projectId);
            entity.setCreatedBy(userId);
            entity.setCreationDate(new Date());
            entity.setLastUpdateDate(new Date());
            entity.setLastUpdatedBy(userId);
            entity.setLastUpdateLogin(userId);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    /**
     * 二级策划界面项目切换模糊查找项目列表及信息（包括各项目阶段列表）
     *
     * @param params   模糊查找参数集 文本类型（支持多选，用','隔开） 项目简称、全称、编号、项目经理
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getSearchProjectList(String params, long userId, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<SearchProjectListDTO> resultList = (List<SearchProjectListDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_Project_List(:1,:2,:3,:4,:5,:6)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, params);// 设置输入参数的值
                        cs.setLong(2, userId);// 设置输入参数的值
                        cs.setLong(3, pageNo);// 设置输入参数的值
                        cs.setLong(4, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(5, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(6, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<SearchProjectListDTO> results = new ArrayList<SearchProjectListDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        dt.setTotal(cs.getLong(6));
                        SearchProjectListDTO tmp = null;
                        List<PaPhaseListDTO> phaseListDTOs = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new SearchProjectListDTO();
                            tmp.setProjID(rs.getLong("Project_Id"));
                            tmp.setProjNumber(rs.getString("Project_Number"));
                            tmp.setProjName(rs.getString("Project_Name"));
                            tmp.setProjLongName(rs.getString("Project_Long_Name"));
                            tmp.setType(rs.getString("Project_Type"));
                            // tmp.setProjectStatusCode(rs.getString("Project_Status_Code"));
                            tmp.setStatus(rs.getString("Project_Status_Name"));
                            tmp.setStartDate(rs.getDate("Project_Start_Date"));
                            tmp.setEndDate(rs.getDate("Project_End_Date"));
                            // tmp.setProjectCarryingOutOrgId(rs.getLong("Project_Carrying_Out_Org_Id"));
                            tmp.setOrgName(rs.getString("Project_Carrying_Out_Org_Name"));
                            tmp.setMgr(rs.getString("Project_Manager_Name"));
                            //  tmp.setProjectManagerId(rs.getLong("Project_Manager_Id"));
                            tmp.setCustomer(rs.getString("Project_Customer_Name"));
                            // tmp.setProjectCustomerId(rs.getLong("Project_Customer_Id"));
                            phaseListDTOs = new ArrayList<PaPhaseListDTO>();
                            phaseListDTOs = getProjectPhaseList(rs.getLong("Project_Id"));
                            tmp.setPhaseList(phaseListDTOs);
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setRows(resultList);
        return dt;
    }


    /**
     * 二级策划界面项目切换当前项目阶段列表
     *
     * @param projectId number
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PaPhaseListDTO> getProjectPhaseList(long projectId) {
        List<PaPhaseListDTO> resultList = (List<PaPhaseListDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_Project_Phase_List(:1,:2)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaPhaseListDTO> results = new ArrayList<PaPhaseListDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);
                        PaPhaseListDTO tmp = null;
                        while (rs.next()) {
                            tmp = new PaPhaseListDTO();
                            tmp.setPhaseID(rs.getString("Phase_Code"));
                            tmp.setPhaseName(rs.getString("Phase_Name"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    /**
     * 历史版本的策划过的阶段列表
     *
     * @param structureId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PaPhaseListDTO> getWbsPhaseList(long structureId) {
        List<PaPhaseListDTO> resultList = (List<PaPhaseListDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_Wbs_Phase_List(:1,:2)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, structureId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaPhaseListDTO> results = new ArrayList<PaPhaseListDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);
                        PaPhaseListDTO tmp = null;
                        while (rs.next()) {
                            tmp = new PaPhaseListDTO();
                            tmp.setPhaseID(rs.getString("Phase_Code"));
                            tmp.setPhaseName(rs.getString("Phase_Name"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    private List<PaElementsEntity> initDlvrEntitys(List<PaElementsDTO> paElementsDTOs, String Operate) {
        List<PaElementsEntity> dataList = new ArrayList<PaElementsEntity>();
        for (int i = 0; i < paElementsDTOs.size(); i++) {
            PaElementsEntity paDeliverableEntity = new PaElementsEntity();
            paDeliverableEntity = this.elmentsDtoToEntity(paElementsDTOs.get(i));
            paDeliverableEntity.setParentId(paElementsDTOs.get(i).getParentId());
            paDeliverableEntity.setOperate(Operate);
            paDeliverableEntity.setTypeId(getTypeId(paElementsDTOs.get(i).getLevel()));
            /*if(paElementsDTOs.get(i).getLevel().equals("spe")){
                paDeliverableEntity.setCode(paDeliverableEntity.getParentId()+"_"+paDeliverableEntity.getCode());
            } */
            dataList.add(paDeliverableEntity);
        }
        return dataList;
    }

    /**
     * 批量插入WBS任务列表
     *
     * @param elementSaveDTO
     * @param userId
     * @return
     */

    @SuppressWarnings("unchecked")
    //@Transactional(rollbackFor = Exception.class)
    public Map addTask(ElementSaveDTO elementSaveDTO, Long userId) {
        List<PaElementsEntity> dataList = new ArrayList<PaElementsEntity>();
        if (elementSaveDTO.getAddRows().size() > 0) {
            dataList.addAll(initDlvrEntitys(elementSaveDTO.getAddRows(), "INSERT"));
        }
        if (elementSaveDTO.getUpdateRows().size() > 0) {
            dataList.addAll(initDlvrEntitys(elementSaveDTO.getUpdateRows(), "UPDATE"));
        }
        if (elementSaveDTO.getDeleteRows().size() > 0) {
            for (int i = 0; i < elementSaveDTO.getDeleteRows().size(); i++) {
                PaElementsEntity paDeliverableEntity = new PaElementsEntity();
                paDeliverableEntity.setId(elementSaveDTO.getDeleteRows().get(i).getId());
                paDeliverableEntity.setOperate("DELETE");
            /*if(paElementsDTOs.get(i).getLevel().equals("spe")){
                paDeliverableEntity.setCode(paDeliverableEntity.getParentId()+"_"+paDeliverableEntity.getCode());
            } */
                dataList.add(paDeliverableEntity);
            }
        }
        long projectId = elementSaveDTO.getProjID();
        String phaseCode = elementSaveDTO.getPhaseID();
        long structureVersionId = elementSaveDTO.getDraftwbsID();
        String division;
        if (elementSaveDTO.getDivisin().equals("false")) {
            division = "N";
        } else {
            division = "Y";
        }
        String headOrbranch = elementSaveDTO.getHeadOrbranch();
        Map map1 = new HashMap();
        if (dataList.size() < 1) {
            map1.put("saveDraft", "none");
            return map1;
        }
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Invoke_Save_Tasks(:1,:2,:3,:4,:5,:6,:7,:8,:9,:10)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.WBSP_ELE_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                        cs.setLong(1, projectId);
                        cs.setString(2, phaseCode);
                        cs.setLong(3, structureVersionId);
                        cs.setArray(4, vArray);
                        cs.setLong(5, userId);
                        cs.setString(6, headOrbranch);//总部类型项目、分公司类型项目
                        cs.setString(7, division);//是否分子项管理
                        cs.registerOutParameter(8, OracleTypes.NUMBER);
                        cs.registerOutParameter(9, OracleTypes.VARCHAR);
                        cs.registerOutParameter(10, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        if (cs.getString(10).equals("S")) {
                            tmp.put("saveDraft", "done");
                        } else {
                            tmp.put("saveDraft", cs.getString(9));
                        }
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 取当前项目发布版
     *
     * @param projectId
     * @param pageNo
     * @param pageSize
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getPaPubilshStructureList(long projectId, String phaseCode, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<PaPubilshStructureDTO> resultList = (List<PaPubilshStructureDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_Pa_Pubilsh_Structure_List(:1,:2,:3,:4,:5,:6,:7,:8)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入项目ID
                        cs.setString(2, phaseCode);//
                        cs.setLong(3, pageNo);// 设置输入参数的值
                        cs.setLong(4, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(5, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(6, OracleTypes.NUMBER);// 注册输出参数的类型
                        cs.registerOutParameter(7, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(8, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaPubilshStructureDTO> results = new ArrayList<PaPubilshStructureDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        dt.setTotal(cs.getLong(6));
                        dt.setReturnStatus(cs.getString(7));
                        dt.setMsgData(cs.getString(8));
                        PaPubilshStructureDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            Date time1 = new Date(rs.getTimestamp("Publisheddate").getTime());
                            SimpleDateFormat formattime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String publisheddate = formattime.format(time1);
                            tmp = new PaPubilshStructureDTO();
                            tmp.setPubwbsName(rs.getString("Strname"));
                            tmp.setPubTime(publisheddate);
                            tmp.setPubOperator(rs.getString("Pername"));
                            tmp.setStructureId(rs.getLong("Structureid"));
                            tmp.setStrPhaseCode(rs.getString("Strphasecode"));

                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setRows(resultList);
        return dt;
    }

    /**
     * 取发布项目的系统子项     *
     *
     * @param phaseCode
     * @param structureVerId
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PaPublishStructureDetailsDTO> getPublishStructureDetails(String phaseCode, long structureVerId) {
        List<PaPublishStructureDetailsDTO> resultList = (List<PaPublishStructureDetailsDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_Publish_Structure_Details(:1,:2,:3,:4,:5)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        //cs.setLong(1, projectId);// 设置输入项目ID
                        cs.setString(1, phaseCode);// 设置输入参数的值
                        cs.setLong(2, structureVerId);// 设置输入参数的值
                        cs.registerOutParameter(3, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaPublishStructureDetailsDTO> results = new ArrayList<PaPublishStructureDetailsDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        PaPublishStructureDetailsDTO tmp = null;
                        int type;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PaPublishStructureDetailsDTO();
                            tmp.setTaskElementId(rs.getLong("Task_Element_Id"));
                            tmp.setId(rs.getLong("Task_Element_Ver_Id"));
                            tmp.setTaskWbsLevel(rs.getLong("Task_Wbs_Level"));
                            tmp.setTaskStructureVerId(rs.getLong("Task_Structure_Ver_Id"));
                            //tmp.setTaskStructureName(rs.getString("Task_Structure_Name"));
                            tmp.setTaskWbsNumber(rs.getString("Task_Wbs_Number"));
                            tmp.setTaskName(rs.getString("Task_Name"));
                            tmp.setTaskTypeId(rs.getLong("Task_Type_Id"));
                            tmp.setTaskType(rs.getString("Task_Type"));
                            tmp.setTaskStartDate(rs.getDate("Task_Start_Date"));
                            tmp.setTaskEndDate(rs.getDate("Task_End_Date"));
                            tmp.setTaskBilingual(rs.getString("Task_Bilingual"));
                            tmp.setTaskWorkload(rs.getString("Task_Workload"));
                            tmp.setTaskStatusCode(rs.getString("Task_Status_Code"));
                            tmp.setParentId(rs.getLong("Parents_Id"));
                            tmp.setDefaultFlag(rs.getString("Default_Flag"));
                            type = (int) rs.getLong("Task_Type_Id");
                            tmp.setLevel(getLevel(type));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

   /* *//**
     * 取历史项目各版
     *
     * @param parentsId
     * @param projectId
     * @param tgId
     * @return
     *//*
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<TreePaHistoryStructureDTO> getPaHistoryStructureList(long parentsId, long projectId, long tgId) {
        //PaHistoryStructureListDTO paHistoryStructureListDTO =new PaHistoryStructureListDTO();
        List<PaHistoryStructureDTO> resultList = (List<PaHistoryStructureDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_History_Structure_List(:1,:2,:3,:4,:5,:6)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置查询条件
                        cs.setLong(2, parentsId);// 设置查询条件
                        cs.setLong(3, tgId);// 设置查询条件
                        cs.registerOutParameter(4, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaHistoryStructureDTO> results = new ArrayList<PaHistoryStructureDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                        //paHistoryStructureListDTO.setTgid(cs.getLong(4));
                        PaHistoryStructureDTO tmp = null;
                        List<PaPhaseListDTO> phaseListDTOs = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PaHistoryStructureDTO();
                            phaseListDTOs = new ArrayList<PaPhaseListDTO>();
                            //tmp.setSessionId(rs.getLong("SESSION_ID"));
                            tmp.setId(rs.getLong("LINEID"));
                            tmp.setName(rs.getString("LINENAME"));
                            tmp.setTime(rs.getDate("LINEDATE"));
                            tmp.setOperator(rs.getString("PERSONNAME"));
                            //tmp.setProjID(rs.getLong("PROJECTID"));
                            tmp.setStructureId(rs.getLong("STRUCTUREID"));
                            tmp.setStrClass(rs.getString("STRCLASS"));
                            tmp.setPhasecode(rs.getString("Phasecode"));
                            tmp.setParentId(rs.getLong("PARENTID"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        //dt.setRows(resultList);
        List<TreePaHistoryStructureDTO> treePaHistoryStructureDTOs = new ArrayList<TreePaHistoryStructureDTO>();
        treePaHistoryStructureDTOs = treePaHistoryList(resultList);
        //paHistoryStructureListDTO.setRows(treePaHistoryStructureDTOs);
        return treePaHistoryStructureDTOs;
    }

    *//**
     * 取历史项目
     *
     * @param queryTerm 查询条件项目名编号等
     * @param pClass    行业类型
     * @param pageNo
     * @param pageSize
     *//*
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PaHistoryProjectDTO> queryHistoryProjectList(String queryTerm, String pClass, long tgId, long userId, long pageNo, long pageSize) {
        List<PaHistoryProjectDTO> resultList = (List<PaHistoryProjectDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_History_Projcet_List(:1,:2,:3,:4,:5,:6,:7,:8,:9)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, queryTerm);// 设置查询条件
                        cs.setString(2, pClass);// 设置行业类别
                        cs.setLong(3, userId);// 设置行业类别
                        cs.setLong(4, pageNo);// 设置输入参数的值
                        cs.setLong(5, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(6, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(7, OracleTypes.NUMBER);// 注册输出参数的类型
                        cs.registerOutParameter(8, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(9, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaHistoryProjectDTO> results = new ArrayList<PaHistoryProjectDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(6);// 获取游标一行的值
                        //dt.setTotal(cs.getLong(7));
                        //dt.setReturnStatus(cs.getString(8));
                        //dt.setMsgData(cs.getString(9));
                        PaHistoryProjectDTO tmp = null;
                        long i = tgId;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PaHistoryProjectDTO();
                            //tmp.setSessionId(rs.getLong("SESSION_ID"));
                            tmp.setProjID(rs.getLong("Project_Id"));
                            tmp.setId(i);
                            tmp.setName(rs.getString("Pname"));
                            tmp.setState("closed");
                            results.add(tmp);
                            i++;
                        }
                        rs.close();
                        return results;
                    }
                });
        //dt.setRows(resultList);
        return resultList;
    }
*/

    /**
     * 取历史项目各版
     *
     * @param queryTerm
     * @param pClass
     * @param tgId
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO queryHistoryProjectList(String queryTerm, String pClass, long tgId, long userId, long pageNo, long pageSize) {
        DataTablesDTO dataDTO = new DataTablesDTO();
        List<PaHistoryStructureDTO> resultList = (List<PaHistoryStructureDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_History_Projcet_List(:1,:2,:3,:4,:5,:6,:7,:8,:9,:10)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, queryTerm);// 设置查询条件
                        cs.setString(2, pClass);// 设置行业类别
                        cs.setLong(3, userId);// 设置行业类别
                        cs.setLong(4, pageNo);// 设置输入参数的值
                        cs.setLong(5, pageSize);// 设置输入参数的值
                        cs.setLong(6, tgId);// 设置输入参数的值
                        cs.registerOutParameter(7, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(8, OracleTypes.NUMBER);// 注册输出参数的类型
                        cs.registerOutParameter(9, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(10, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaHistoryStructureDTO> results = new ArrayList<PaHistoryStructureDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(7);// 获取游标一行的值
                        dataDTO.setTotal(cs.getLong(8));
                        PaHistoryStructureDTO tmp = null;
                        List<PaPhaseListDTO> phaseListDTOs = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PaHistoryStructureDTO();
                            phaseListDTOs = new ArrayList<PaPhaseListDTO>();
                            //tmp.setSessionId(rs.getLong("SESSION_ID"));
                            tmp.setId(rs.getLong("LINEID"));
                            tmp.setName(rs.getString("LINENAME"));
                            tmp.setTime(rs.getDate("LINEDATE"));
                            tmp.setOperator(rs.getString("PERSONNAME"));
                            //tmp.setProjID(rs.getLong("PROJECTID"));
                            tmp.setStructureId(rs.getLong("STRUCTUREID"));
                            tmp.setStrClass(rs.getString("STRCLASS"));
                            tmp.setPhasecode(rs.getString("Phasecode"));
                            tmp.setParentId(rs.getLong("PARENTID"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        //dt.setRows(resultList);
        List<TreePaHistoryStructureDTO> treePaHistoryStructureDTOs = new ArrayList<TreePaHistoryStructureDTO>();
        treePaHistoryStructureDTOs = treePaHistoryList(resultList);
        //paHistoryStructureListDTO.setRows(treePaHistoryStructureDTOs);
        dataDTO.setRows(treePaHistoryStructureDTOs);
        return dataDTO;
    }

    /**
     * 取历史WBS项目的系统子项
     *
     * @param structureId 结构ID
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PaHistoryWbsDetailsDTO> getHistoryWbsStructureDetails(long structureId) {
        List<PaHistoryWbsDetailsDTO> resultList = (List<PaHistoryWbsDetailsDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_History_Wbs_Details(:1,:2,:3,:4)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, structureId);// 设置输入参数的值
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaHistoryWbsDetailsDTO> results = new ArrayList<PaHistoryWbsDetailsDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        PaHistoryWbsDetailsDTO tmp = null;
                        int type;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PaHistoryWbsDetailsDTO();
                            tmp.setId(rs.getLong("Task_Id"));
                            tmp.setWbsId(rs.getLong("Structure_Id"));
                            tmp.setCode(rs.getString("Task_Num"));
                            tmp.setName(rs.getString("Task_Name"));
                            tmp.setLevel(rs.getLong("Task_Type_Id"));
                            tmp.setStartDate(rs.getDate("Task_Start_Date"));
                            tmp.setEndDate(rs.getDate("Task_End_Date"));
                            tmp.setDualName(rs.getString("Task_Bilingual"));
                            tmp.setWorkCoef(rs.getString("Task_Workload"));
                            tmp.setTaskStatusCode(rs.getString("Task_Status_Code"));
                            //tmp.setPbsOrWbs(rs.getString("Pbs_Or_Wbs"));
                            tmp.setParentId(rs.getLong("Parent_Id"));
                            tmp.setDefaultFlag(rs.getString("Default_Flag"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    /**
     * 取历史PBS项目的系统子项
     *
     * @param structureId 结构ID
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PaHistoryPbsDetailsDTO> getHistoryPbsStructureDetails(long structureId) {
        List<PaHistoryPbsDetailsDTO> resultList = (List<PaHistoryPbsDetailsDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_History_Pbs_Details(:1,:2,:3,:4)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, structureId);// 设置输入参数的值
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaHistoryPbsDetailsDTO> results = new ArrayList<PaHistoryPbsDetailsDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        PaHistoryPbsDetailsDTO tmp = null;
                        int type;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PaHistoryPbsDetailsDTO();
                            tmp.setId(rs.getLong("Task_Id"));
                            tmp.setPbsID(rs.getLong("Structure_Id"));
                            tmp.setCode(rs.getString("Task_Num"));
                            tmp.setName(rs.getString("Task_Name"));
                            tmp.setLevel(rs.getLong("Task_Type_Id"));
                            tmp.setDualName(rs.getString("Task_Bilingual"));
                            //tmp.setPbsOrWbs(rs.getString("Pbs_Or_Wbs"));
                            tmp.setParentId(rs.getLong("Parent_Id"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    /**
     * 取当前正在编辑版结构
     *
     * @param strucVerId
     * @param phaseCode
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public PaWorkingStructureDTO getWorkingStructure(long strucVerId, String phaseCode) {
        PaWorkingStructureDTO ResultsDTO = (PaWorkingStructureDTO) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_Working_Structure(:1,:2,:3,:4,:5,:6,:7,:8,:9,:10,:11,:12,:13)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, strucVerId);// 设置输入项目ID
                        cs.setString(2, phaseCode);// 设置输入参数的值
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(4, OracleTypes.NUMBER);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.NUMBER);// 注册输出参数的类型
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(7, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(8, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(9, OracleTypes.NUMBER);// 注册输出参数的类型
                        cs.registerOutParameter(10, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(11, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(12, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(13, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        PaWorkingStructureDTO paWorkingStructureDTO = new PaWorkingStructureDTO();
                        cs.execute();
                        paWorkingStructureDTO.setDraftwbsName(cs.getString(3)); //获取对应输出结果
                        paWorkingStructureDTO.setStructrueId(cs.getLong(4)); //获取对应输出结果
                        paWorkingStructureDTO.setStructrueVersionId(cs.getLong(5)); //获取对应输出结果
                        paWorkingStructureDTO.setTime(cs.getString(6)); //获取对应输出结果
                        paWorkingStructureDTO.setOperator(cs.getString(7)); //获取对应输出结果
                        paWorkingStructureDTO.setCheckout(cs.getString(8)); //获取对应输出结果
                        paWorkingStructureDTO.setCheckoutId(cs.getString(9)); //获取对应输出结果
                        paWorkingStructureDTO.setHeadOrbranch(cs.getString(10)); //获取对应输出结果
                        paWorkingStructureDTO.setDivision(cs.getString(11)); //获取对应输出结果
                        paWorkingStructureDTO.setReturnStatus(cs.getString(12)); //获取对应输出结果
                        paWorkingStructureDTO.setMsgData(cs.getString(13)); //获取对应输出结果

                        return paWorkingStructureDTO;
                    }
                });

        return ResultsDTO;
    }

    /**
     * 取正在编辑版的系统子项
     *
     * @param phaseCode      阶段代字
     * @param structureVerId 结构版本ID
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PaWorkingStructureDetailsDTO> getWorkingStructureDetails(String phaseCode, long structureVerId) {
        List<PaWorkingStructureDetailsDTO> resultList = (List<PaWorkingStructureDetailsDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_Working_Structure_Details(:1,:2,:3,:4,:5)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        // cs.setLong(1, projectId);// 设置输入项目ID
                        cs.setString(1, phaseCode);// 设置输入参数的值
                        cs.setLong(2, structureVerId);// 设置输入参数的值
                        cs.registerOutParameter(3, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaWorkingStructureDetailsDTO> results = new ArrayList<PaWorkingStructureDetailsDTO>();
                        List<PaIndustriesAllDTO> paIndustriesAllDTOs = getAllSpec();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        PaWorkingStructureDetailsDTO tmp = null;
                        int type;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PaWorkingStructureDetailsDTO();
                            tmp.setTaskElementId(rs.getLong("Task_Element_Id"));
                            tmp.setId(rs.getLong("Task_Element_Ver_Id"));
                            tmp.setTaskWbsLevel(rs.getLong("Task_Wbs_Level"));
                            tmp.setTaskWbsNumber(rs.getString("Task_Wbs_Number"));
                            tmp.setTaskName(rs.getString("Task_Name"));
                            tmp.setTaskTypeId(rs.getLong("Task_Type_Id"));
                            tmp.setTaskType(rs.getString("Task_Type"));
                            tmp.setTaskStartDate(rs.getDate("Task_Start_Date"));
                            tmp.setTaskEndDate(rs.getDate("Task_End_Date"));
                            tmp.setTaskBilingual(rs.getString("Task_Bilingual"));
                            tmp.setTaskWorkload(rs.getString("Task_Workload"));
                            tmp.setTaskStatusCode(rs.getString("Task_Status_Code"));
                            tmp.setTaskSourceClass(rs.getString("Task_Source_Class"));
                            tmp.setTaskSourceStructureVerId(rs.getString("Task_Source_Structure_Ver_Id"));
                            tmp.setTaskSourceStructurename(rs.getString("Task_Source_Structure_Name"));
                            tmp.setTaskSourceEleVerId(rs.getString("Task_Source_Ele_Ver_Id"));
                            tmp.setParentId(rs.getLong("Parents_Id"));
                            tmp.setFlag(rs.getString("Flag"));
                            tmp.setDefaultFlag(rs.getString("Default_Flag"));
                            type = (int) rs.getLong("Task_Type_Id");
                            tmp.setLevel(getLevel(type));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    /**
     * 取历史系统
     *
     * @param sysName  查询条件系统名称
     * @param pClass   行业类型
     * @param pageNo
     * @param pageSize
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getHistorySystemList(String sysName, String pClass, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<PaHistorySysDTO> resultList = (List<PaHistorySysDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_History_System_List(:1,:2,:3,:4,:5,:6)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, sysName);// 设置查询条件
                        cs.setString(2, pClass);// 设置行业类别
                        cs.setLong(3, pageNo);// 设置输入参数的值
                        cs.setLong(4, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(5, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(6, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaHistorySysDTO> results = new ArrayList<PaHistorySysDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        dt.setTotal(cs.getLong(6));
                        PaHistorySysDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PaHistorySysDTO();
                            tmp.setName(rs.getString("Sysname"));
                            tmp.setIndustry(rs.getString("Sysindustry"));
                            tmp.setDual(rs.getString("Sysenglish"));
                            //tmp.setStructureName(rs.getString("Structure_Name"));
                            // tmp.setStructureVerId(rs.getLong("Structure_Ver_Id"));//结构版本ID
                            // tmp.setElementVersionId(rs.getLong("Element_Version_Id"));//节点版本ID
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setRows(resultList);
        return dt;
    }

    /**
     * 取历史子项
     *
     * @param subName  查询条件系统名称
     * @param pClass   行业类型
     * @param pageNo
     * @param pageSize
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getHistorySubList(String subName, String pClass, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<PaHistorySubDTO> resultList = (List<PaHistorySubDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_History_Sub_List(:1,:2,:3,:4,:5,:6)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, subName);// 设置查询条件
                        cs.setString(2, pClass);// 设置行业类别
                        cs.setLong(3, pageNo);// 设置输入参数的值
                        cs.setLong(4, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(5, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(6, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaHistorySubDTO> results = new ArrayList<PaHistorySubDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        dt.setTotal(cs.getLong(6));
                        PaHistorySubDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PaHistorySubDTO();
                            tmp.setName(rs.getString("Subname"));
                            tmp.setIndustry(rs.getString("Subindustry"));
                            tmp.setDual(rs.getString("Subenglish"));
                            //  tmp.setStructureName(rs.getString("Structure_Name"));
                            //  tmp.setStructureVerId(rs.getLong("Structure_Ver_Id"));//结构版本ID
                            //  tmp.setElementVersionId(rs.getLong("Element_Version_Id"));//节点版本ID
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setRows(resultList);
        return dt;
    }

    /**
     * 取所有的专业信息
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PaIndustriesAllDTO> getSpecListAll(String projectId) {
        List<PaIndustriesAllDTO> resultList = (List<PaIndustriesAllDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_Spec_List_All(:1,:2,:3)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, projectId);// 设置查询条件
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(3, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaIndustriesAllDTO> results = new ArrayList<PaIndustriesAllDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        PaIndustriesAllDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PaIndustriesAllDTO();
                            tmp.setCode(rs.getString("Disp_Code"));
                            tmp.setName(rs.getString("Industry_Name"));
                            tmp.setIndustryCode(rs.getString("Industry_Code"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    /**
     * 取所有的专业信息
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PaIndustriesAllDTO> getAllSpec() {
        List<PaIndustriesAllDTO> resultList = (List<PaIndustriesAllDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_All_Spec(:1)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaIndustriesAllDTO> results = new ArrayList<PaIndustriesAllDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(1);// 获取游标一行的值
                        PaIndustriesAllDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PaIndustriesAllDTO();
                            tmp.setCode(rs.getString("Disp_Code"));
                            tmp.setName(rs.getString("Industry_Name"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    /**
     * 取项目行业
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PaIndustryDTO> getIndustryList(long userId) {
        List<PaIndustryDTO> resultList = (List<PaIndustryDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_Industry_List(:1,:2)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, userId);// 设置查询条件
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaIndustryDTO> results = new ArrayList<PaIndustryDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        PaIndustryDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PaIndustryDTO();
                            tmp.setIndustryName(rs.getString("Industry_Name"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    /**
     * 取项目全部行业
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PaIndustryDTO> getAllIndustryList() {
        List<PaIndustryDTO> resultList = (List<PaIndustryDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_Industry_List_All(:1)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaIndustryDTO> results = new ArrayList<PaIndustryDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(1);// 获取游标一行的值
                        PaIndustryDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PaIndustryDTO();
                            tmp.setIndustryName(rs.getString("Industry_Name"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    /**
     * 当前阶段的专业信息
     *
     * @param projectId
     * @param phaseCode
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PaIndustriesDTO> getSpecList(long projectId, String phaseCode, long userId) {
        List<PaIndustriesDTO> resultList = (List<PaIndustriesDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_Spec_List(:1,:2,:3,:4,:5)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置项目ID
                        cs.setString(2, phaseCode);// 设置阶段
                        cs.setLong(3, userId);// 设置阶段
                        cs.registerOutParameter(4, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaIndustriesDTO> results = new ArrayList<PaIndustriesDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                        PaIndustriesDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PaIndustriesDTO();
                            tmp.setCode(rs.getString("Disp_Code"));
                            tmp.setName(rs.getString("Industry_Name"));
                            tmp.setId(rs.getLong("PROJECT_INDUSTRY_ID"));
                            tmp.setIndustryCode(rs.getString("INDUSTRY_CODE"));
                            tmp.setRatio(rs.getDouble("INDUSTRY_RATE"));
                            /*tmp.setCreationDate(rs.getDate("CREATION_DATE"));
                            tmp.setCreatedBy(rs.getLong("CREATED_BY"));
                            tmp.setLastUpdateDate(rs.getDate("LAST_UPDATE_DATE"));
                            tmp.setLastUpdatedBy(rs.getLong("LAST_UPDATED_BY"));
                            tmp.setLastUpdateLogin(rs.getLong("LAST_UPDATE_LOGIN"));
                            tmp.setPhaseCode(rs.getString("PHASE_CODE"));
                            tmp.setEnableFlag(rs.getString("ENABLE_FLAG"));*/
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    /**
     * 签出/锁定  初始化阶段
     *
     * @param projectId
     * @param phaseCode 阶段代字
     * @param userId
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map lockWorkingStructure(long projectId, String phaseCode, long userId) {
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Lock_Working_Structure(:1,:2,:3,:4,:5)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入项目ID
                        cs.setString(2, phaseCode);// 设置输入参数的值
                        cs.setLong(3, userId);// 设置输入参数的值
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        //tmp.put(WbspParm.DB_STATE, cs.getString(4));
                        //tmp.put(WbspParm.DB_MSG, cs.getString(5));
                        if (cs.getString(4).equals("S") & !cs.getString(4).isEmpty()) {
                            tmp.put("checkOut", "done");
                        } else {
                            //tmp.put("checkOut", "fail");
                            tmp.put("fail", cs.getString(5));
                        }
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 签入/解锁
     *
     * @param projectId
     * @param userId
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map unlockWorkingStructure(long projectId, long userId) {
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Unlock_Working_Structure(:1,:2,:3,:4,:5)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入项目ID
                        cs.setLong(2, userId);// 设置输入参数的值
                        cs.setString(3, "N");// 设置输入参数的值
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(WbspParm.DB_STATE, cs.getString(4));
                        tmp.put(WbspParm.DB_MSG, cs.getString(5));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 保存版本信息及扩展内容
     *
     * @param projectId
     * @param phaseCode
     * @param userId
     * @param structureClass
     * @param onlySpec
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map saveWorkingStructure(long projectId, String phaseCode, long userId, String structureClass, String onlySpec) {
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Save_Working_Structure(:1,:2,:3,:4,:5,:6,:7)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入项目ID
                        cs.setString(2, phaseCode);// 设置输入参数的值
                        cs.setLong(3, userId);// 设置输入参数的值
                        cs.setString(4, structureClass);// 设置输入参数的值
                        cs.setString(5, onlySpec);// 设置输入参数的值
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(7, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(WbspParm.DB_MSG, cs.getString(6));
                        tmp.put(WbspParm.DB_STATE, cs.getString(7));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 保存版本信息及扩展内容
     *
     * @param structureVerId
     * @param phaseCode
     * @param userId
     * @param onlySpec
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map updateWorkingStructure(long structureVerId, String phaseCode, long userId, String onlySpec) {
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.update_Working_Structure(:1,:2,:3,:4,:5,:6)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, structureVerId);// 设置输入项目ID
                        cs.setString(2, phaseCode);// 设置输入参数的值
                        cs.setLong(3, userId);// 设置输入参数的值
                        cs.setString(4, onlySpec);// 设置输入参数的值
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        if (cs.getString(5).equals("S")) {
                            tmp.put("saveWork", "done");
                        } else {
                            //tmp.put("checkOut", "fail");
                            tmp.put("fail", cs.getString(6));
                        }

                        return tmp;
                    }
                });
        return map;

    }

    /**
     * 发布版本，更新扩展内容
     *
     * @param projectId
     * @param phaseCode
     * @param userId
     * @param structureClass
     * @param onlySpec
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map publishStructure(long projectId, String phaseCode, long userId, String structureClass, String onlySpec) {
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Publish_Structure(:1,:2,:3,:4,:5,:6,:7)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入项目ID
                        cs.setString(2, phaseCode);// 设置输入参数的值
                        cs.setLong(3, userId);// 设置输入参数的值
                        cs.setString(4, structureClass);// 设置输入参数的值
                        cs.setString(5, onlySpec);// 设置输入参数的值
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(7, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        if (cs.getString(7).equals("S")) {
                            tmp.put("returnMsg", "成功");
                        } else {
                            tmp.put("returnMsg", "失败");
                            tmp.put(WbspParm.DB_MSG, cs.getString(6));
                        }
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 创建/修改专业属性
     *
     * @param paIndustriesResponDTO
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map creatIndustriesRate(PaIndustriesResponDTO paIndustriesResponDTO, long userId) {
        List<PaIndustriesEntity> dataList = new ArrayList<PaIndustriesEntity>();
        long projectId = paIndustriesResponDTO.getProjID();
        List<PaIndustriesDTO> paIndustriesDTO = new ArrayList<PaIndustriesDTO>();
        paIndustriesDTO = paIndustriesResponDTO.getSpecialtyRatio();
        for (int i = 0; i < paIndustriesDTO.size(); i++) {
            dataList.add(this.paIndustriesDtoToEntity(paIndustriesDTO.get(i), projectId, userId));
        }
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Creat_Industries_Rate(:1,:2,:3)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.WBSP_PA_INDUSTRIES_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                        cs.setArray(1, vArray);
                        cs.registerOutParameter(2, OracleTypes.VARCHAR);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        if (cs.getString(2).equals("S")) {
                            tmp.put("speRatio", "done");
                        } else {
                            tmp.put("speRatio", "fail");
                            tmp.put(WbspParm.DB_MSG, cs.getString(3));
                        }
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 初始化项目所需信息
     *
     * @param projectId
     * @param phaseCode
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public PaInfo2RepDTO getProjectInfo(long projectId, String phaseCode, long userId) {
        PaInfo2RepDTO ResultsDTO = (PaInfo2RepDTO) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_Project_Info(:1,:2,:3,:4,:5,:6)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入项目ID
                        cs.setString(2, phaseCode);// 设置输入参数的值
                        cs.setLong(3, userId);// 设置输入项目ID
                        cs.registerOutParameter(4, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        PaInfo2DTO paInfo2DTO = new PaInfo2DTO();
                        PaInfo2RepDTO paInfo2RepDTO = new PaInfo2RepDTO();
                        cs.execute();
                        paInfo2RepDTO.setReturnStatus(cs.getString(5));
                        paInfo2RepDTO.setMsgData(cs.getString(6));
                        if (cs.getString(5).equals("S")) {
                            ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                            while (rs.next()) {// 转换每行的返回值到Map中
                                paInfo2DTO.setProjID(rs.getLong("Project_Id")); //获取对应输出结果
                                paInfo2DTO.setProjName(rs.getString("Project_Name")); //获取对应输出结果
                                paInfo2DTO.setProjNumber(rs.getString("Project_Number")); //获取对应输出结果
                                paInfo2DTO.setPhaseName(rs.getString("Phase_Name")); //获取对应输出结果
                                paInfo2DTO.setMgr(rs.getString("Project_Manager_Name")); //获取对应输出结果
                                paInfo2DTO.setDraftwbsID(rs.getLong("Working_Ver_Id")); //获取对应输出结果
                                paInfo2DTO.setPubwbsID(rs.getLong("Latest_Published_Ver")); //获取对应输出结果
                                paInfo2DTO.setHeadOrbranch(rs.getString("Structure_Class")); //获取对应输出结果
                                paInfo2DTO.setDivision(rs.getString("Only_Spec")); //获取对应输出结果
                                paInfo2DTO.setPubwbsName(rs.getString("Str_Name")); //获取对应输出结果
                                paInfo2DTO.setPhaseStart(rs.getDate("Phasestart")); //获取对应输出结果
                                paInfo2DTO.setPhaseEnd(rs.getDate("Phaseend")); //获取对应输出结果
                                paInfo2DTO.setDraftwbsCheckout(rs.getLong("Draftwbscheckout")); //获取对应输出结果
                                paInfo2DTO.setDual(rs.getString("Dual")); //获取对应输出结果
                                paInfo2DTO.setCheckoutable(getBool(rs.getString("Checkoutable"))); //获取对应输出结果
                                paInfo2DTO.setUserID(rs.getLong("User_Id"));
                                paInfo2DTO.setOldPhase(getBool(rs.getString("Is_Old_Phase"))); //获取对应输出结果
                            }
                            rs.close();
                        }
                        paInfo2RepDTO.setData(paInfo2DTO);
                        return paInfo2RepDTO;
                    }
                });
        return ResultsDTO;
    }

    /**
     * 获取图纸状态变化列表
     *
     * @param projectId
     * @param phaseCode
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DwgStatusChangeResponseDTO getDwgChangeList(long projectId, String phaseCode, long userId) {
        DwgStatusChangeResponseDTO resultsDTO = (DwgStatusChangeResponseDTO) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Save_Change(:1,:2,:3,:4)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入项目ID
                        cs.setString(2, phaseCode);// 设置输入参数的值
                        cs.setLong(3, userId);// 设置输入项目ID
                        cs.registerOutParameter(4, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        DwgStatusChangeResponseDTO dlvrResponseListDTO = new DwgStatusChangeResponseDTO();
                        List<DwgStatusChangeDTO> dwgStatusChangeDTOs = new ArrayList<DwgStatusChangeDTO>();
                        DwgStatusChangeDTO dwgStatusChangeDTO;
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                        Map tmp = new HashMap();
                        tmp.put("status", cs.getString(5));
                        tmp.put("err", cs.getString(6));
                        while (rs.next()) {// 转换每行的返回值到Map中
                            dwgStatusChangeDTO = new DwgStatusChangeDTO();
                            dwgStatusChangeDTO.setWorkId(rs.getLong("Dlvr_Id"));//工作包号
                            dwgStatusChangeDTO.setDivisionNum(rs.getString("Sub_Num"));//工作包所属子项号变更描述
                            dwgStatusChangeDTO.setDivisionName(rs.getString("Sub_Name"));//工作包所属子项名称变更描述
                            dwgStatusChangeDTO.setSpecialty(rs.getString("Spec_Name")); //工作包所属专业
                            dwgStatusChangeDTO.setWorkName(rs.getString("Dlvr_Name")); //工作包名称变更描述
                            dwgStatusChangeDTO.setStatus(rs.getString("Status_Name")); //图纸状态变更描述
                            dwgStatusChangeDTO.setDesign(rs.getString("Design_Name")); //设计人变更描述
                            dwgStatusChangeDTO.setCheck(rs.getString("Check_Name")); //校核人变更描述
                            dwgStatusChangeDTO.setReview(rs.getString("Verify_Name")); //审核人变更描述
                            dwgStatusChangeDTO.setApprove(rs.getString("Approve_Name")); //审定人变更描述
                            dwgStatusChangeDTO.setCertified(rs.getString("Reg_Engineer_Name")); //注册工程师变更描述
                            dwgStatusChangeDTO.setGrandNum(rs.getString("Grandnum")); //专业孙项号变更描述
                            dwgStatusChangeDTO.setGrandNum(rs.getString("Workgrandnum")); //工作包孙项
                            dwgStatusChangeDTO.setDual(rs.getString("Bilingual")); //子项双语名称变更描述
                            dwgStatusChangeDTO.setMatCode(rs.getString("Matcode")); //关联物料编码变更描述
                            dwgStatusChangeDTO.setMatName(rs.getString("Matname")); //关联物料名称变更描述
                            dwgStatusChangeDTOs.add(dwgStatusChangeDTO);
                        }
                        rs.close();
                        dlvrResponseListDTO.setRows(dwgStatusChangeDTOs);
                        return dlvrResponseListDTO;
                    }
                });
        return resultsDTO;
    }

    /**
     * 确认图纸状态变更列表
     *
     * @param projectId
     * @param phaseCode
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map confirmChange(Long projectId, String phaseCode) {
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Confirm_Change(:1,:2,:3,:4)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.setString(2, phaseCode);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(WbspParm.DB_STATE, cs.getString(3));
                        tmp.put(WbspParm.DB_MSG, cs.getString(4));
                        return tmp;
                    }
                });
        return map;
    }


    /**
     * 图纸变更状态历史列表
     *
     * @param projectId
     * @param phaseCode
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<DwgHisStatusChangeDTO> getHistoryChange(Long projectId, String phaseCode) {
        List<DwgHisStatusChangeDTO> dwgHisStatusChangeDTOs = (List<DwgHisStatusChangeDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_History_Change(:1,:2,:3)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.setString(2, phaseCode);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<DwgHisStatusChangeDTO> dwgHisStatusChangeDTOs = new ArrayList<DwgHisStatusChangeDTO>();
                        DwgHisStatusChangeDTO dwgHisStatusChangeDTO;
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                        while (rs.next()) {// 转换每行的返回值到Map中
                            dwgHisStatusChangeDTO = new DwgHisStatusChangeDTO();
                            dwgHisStatusChangeDTO.setWorkId(rs.getLong("Dlvr_Id"));//工作包号
                            dwgHisStatusChangeDTO.setDivisionNum(rs.getString("Sub_Num"));//工作包所属子项号变更描述
                            dwgHisStatusChangeDTO.setDivisionName(rs.getString("Sub_Name"));//工作包所属子项名称变更描述
                            dwgHisStatusChangeDTO.setSpecialty(rs.getString("Spec_Name")); //工作包所属专业
                            dwgHisStatusChangeDTO.setWorkName(rs.getString("Dlvr_Name")); //工作包名称变更描述
                            dwgHisStatusChangeDTO.setStatus(rs.getString("Status_Name")); //图纸状态变更描述
                            dwgHisStatusChangeDTO.setDesign(rs.getString("Design_Name")); //设计人变更描述
                            dwgHisStatusChangeDTO.setCheck(rs.getString("Check_Name")); //校核人变更描述
                            dwgHisStatusChangeDTO.setReview(rs.getString("Verify_Name")); //审核人变更描述
                            dwgHisStatusChangeDTO.setApprove(rs.getString("Approve_Name")); //审定人变更描述
                            dwgHisStatusChangeDTO.setCertified(rs.getString("Reg_Engineer_Name")); //注册工程师变更描述
                            dwgHisStatusChangeDTO.setGrandNum(rs.getString("Grandnum")); //专业孙项号变更描述
                            dwgHisStatusChangeDTO.setGrandNum(rs.getString("Workgrandnum")); //工作包孙项
                            dwgHisStatusChangeDTO.setDual(rs.getString("Bilingual")); //子项双语名称变更描述
                            dwgHisStatusChangeDTO.setMatCode(rs.getString("Matcode")); //关联物料编码变更描述
                            dwgHisStatusChangeDTO.setMatName(rs.getString("Matname")); //关联物料名称变更描述
                            dwgHisStatusChangeDTO.setUpdateDate(rs.getDate("Creation_Date")); //关联物料编码变更描述
                            dwgHisStatusChangeDTO.setVersion(rs.getLong("Structure_No")); //关联物料名称变更描述
                            dwgHisStatusChangeDTOs.add(dwgHisStatusChangeDTO);
                        }
                        return dwgHisStatusChangeDTOs;
                    }
                });
        return dwgHisStatusChangeDTOs;
    }

    /**
     * 给JS传taskID
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public ResponseTaskIdDTO getTaskId() {
        ResponseTaskIdDTO responseTaskIdDTO = (ResponseTaskIdDTO) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.New_Structure_Id(:1,:2)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.NUMBER);
                        cs.registerOutParameter(2, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        ResponseTaskIdDTO TaskIds = new ResponseTaskIdDTO();
                        cs.execute();
                        TaskIds.setTaskElementId(cs.getLong(1));
                        TaskIds.setId(cs.getLong(2));
                        return TaskIds;
                    }
                });
        return responseTaskIdDTO;
    }

    /**
     * 新ID
     *
     * @param num
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<ResponseNewIdsDTO> getTaskIds(long num) {
        List<ResponseNewIdsDTO> resultList = (List<ResponseNewIdsDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.New_Structure_Ids(:1,:2)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, num);// 设置输入参数的值
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ResponseNewIdsDTO> results = new ArrayList<ResponseNewIdsDTO>();
                        ResponseNewIdsDTO dt;
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        while (rs.next()) {// 转换每行的返回值到Map中
                            dt = new ResponseNewIdsDTO();
                            dt.setId(rs.getLong("Task_Ver_Id"));
                            dt.setTaskId(rs.getLong("Task_Id"));
                            results.add(dt);
                        }
                        rs.close();
                        return results;
                    }
                });

        return resultList;
    }

    /**
     * 是否是本人签出
     *
     * @param projectID
     * @param userID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public String isLockedByUser(long projectID, long userID) {
        String results = (String) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Utils.Is_Struc_Ver_Locked_By_User(:1,:2,:3)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectID);// 设置输入参数的值
                        cs.setLong(2, userID);// 设置输入参数的值
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        String result = cs.getString(3);// 获取游标一行的值
                        return result;
                    }
                });

        return results;
    }

    /**
     * 锁定
     *
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map lockStru(String sessionId, long userId, long projId) {
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Lock_Stru(:1,:2,:3,:4)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, sessionId);
                        cs.setLong(2, userId);
                        cs.setLong(3, projId);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(WbspParm.DB_STATE, cs.getString(4));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 存session
     *
     * @param sessionId
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public void saveSession(String sessionId) {
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Save_Session(:1)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, sessionId);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        return tmp;
                    }
                });

    }

    /**
     * 删除session
     *
     * @param sessionId
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public void deleteSession(String sessionId) {
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Delete_Session(:1)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, sessionId);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        return tmp;
                    }
                });

    }

    /**
     * 根据是否分子项，本部还是分公司，获取要显示的层级
     *
     * @param structureVerId
     * @param phaseCode
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public  StuctureExtInfoDTO structureExtInfo(long structureVerId, String phaseCode, String isVer) {
        StuctureExtInfoDTO result = (StuctureExtInfoDTO) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_Structure_Ext_Info(:1,:2,:3,:4,:5)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, structureVerId);
                        cs.setString(2, phaseCode);
                        cs.setString(3, isVer);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        StuctureExtInfoDTO tmp = new StuctureExtInfoDTO();
                        cs.execute();
                        tmp.setFlag(cs.getString(4));
                        tmp.setDivsion(cs.getString(5));
                        return tmp;
                    }
                });
        return result;
    }

    /**
     * 根据是否分子项，本部还是分公司，获取要显示的层级
     *
     * @param structureVerId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public StuctureExtInfoDTO structureExtInfo1(long structureVerId) {
        StuctureExtInfoDTO result = (StuctureExtInfoDTO) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev2_Plan.Get_Structure_Ext_Info1(:1,:2,:3)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, structureVerId);
                        cs.registerOutParameter(2, OracleTypes.VARCHAR);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        StuctureExtInfoDTO tmp = new StuctureExtInfoDTO();
                        cs.execute();
                        tmp.setFlag(cs.getString(2));
                        tmp.setDivsion(cs.getString(3));
                        return tmp;
                    }
                });
        return result;
    }
}