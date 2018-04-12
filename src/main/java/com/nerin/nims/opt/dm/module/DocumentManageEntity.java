package com.nerin.nims.opt.dm.module;

import com.nerin.nims.opt.base.rest.OracleBaseEntity;
import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Administrator on 2016/12/13.
 */
public class DocumentManageEntity extends OracleBaseEntity implements ORAData {
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

    public static final String _ORACLE_TYPE_NAME = "APPS.CUX_DOCUMENT_MANAGE_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = { OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR , OracleTypes.VARCHAR , OracleTypes.DATE, OracleTypes.NUMBER, OracleTypes.DATE
            , OracleTypes.NUMBER, OracleTypes.NUMBER};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public DocumentManageEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    };

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getHeaderId());
        _struct.setAttribute(1, this.getId());
        _struct.setAttribute(2, this.getDocumentName());
        _struct.setAttribute(3, this.getDocumentSize());
        _struct.setAttribute(4, this.getDocumentDate());
        _struct.setAttribute(5, this.getDocumnetDirectory());
        _struct.setAttribute(6, this.getDocumentLink());
        _struct.setAttribute(7, this.getCreationDate());
        _struct.setAttribute(8, this.getCreatedBy());
        _struct.setAttribute(9, this.getLastUpdateDate());
        _struct.setAttribute(10, this.getLastUpdatedBy());
        _struct.setAttribute(11, this.getLastUpdateLogin());

        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
