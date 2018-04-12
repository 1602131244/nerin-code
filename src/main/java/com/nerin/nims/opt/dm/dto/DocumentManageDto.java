package com.nerin.nims.opt.dm.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

/**
 * Created by Administrator on 2016/12/13.
 */
public class DocumentManageDto extends OracleBaseDTO {
    private Long headerId;
    private Long id;
    private String documentName;
    private String documentSize;
    private String documentDate;
    private String documentLink;
    private String documnetDirectory;

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentSize() {
        return documentSize;
    }

    public void setDocumentSize(String documentSize) {
        this.documentSize = documentSize;
    }

    public String getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    public String getDocumentLink() {
        return documentLink;
    }

    public void setDocumentLink(String documentLink) {
        this.documentLink = documentLink;
    }

    public String getDocumnetDirectory() {
        return documnetDirectory;
    }

    public void setDocumnetDirectory(String documnetDirectory) {
        this.documnetDirectory = documnetDirectory;
    }
}
