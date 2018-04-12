package com.nerin.nims.opt.wbsp.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */
public class TreeListDTO {
    private TreeDataDTO treeDataDTO;
    private List<TreeListDTO> children = new ArrayList<TreeListDTO>();

    public TreeDataDTO getTreeDataDTO() {
        return treeDataDTO;
    }

    public void setTreeDataDTO(TreeDataDTO treeDataDTO) {
        this.treeDataDTO = treeDataDTO;
    }

    public List<TreeListDTO> getChildren() {
        return children;
    }

    public void setChildren(List<TreeListDTO> children) {
        this.children = children;
    }
}
