package com.fpt.edu.linkresource;

import com.opencsv.bean.CsvBindByName;

import java.util.List;

public class EndPoint {
    @CsvBindByName(column = "END_POINT_NAME", required = true)
    private String endPointName;
    @CsvBindByName(column = "LINK_SELF", required = true)
    private String linkSelf;
    @CsvBindByName(column = "METHOD", required = true)
    private String method;

    @CsvBindByName(column = "ITEMS_LINK", required = true)
    private String itemLink;

    public String getItemLink() {
        return itemLink;
    }

    public void setItemLink(String itemLink) {
        this.itemLink = itemLink;
    }

    public String getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(String isCollection) {
        this.isCollection = isCollection;
    }

    @CsvBindByName(column = "IS_COLLECTION", required = true)
    private String isCollection;

    @CsvBindByName(column = "SUB_LINK_KEY", required = true)
    private String subLinkKey;

    private List<Link> linkList;

    public List<Link> getLinkList() {
        return linkList;
    }

    public void setLinkList(List<Link> linkList) {
        this.linkList = linkList;
    }

    public String getEndPointName() {
        return endPointName;
    }

    public void setEndPointName(String endPointName) {
        this.endPointName = endPointName;
    }

    public String getLinkSelf() {
        return linkSelf;
    }

    public void setLinkSelf(String linkSelf) {
        this.linkSelf = linkSelf;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSubLinkKey() {
        return subLinkKey;
    }

    public void setSubLinkKey(String subLinkKey) {
        this.subLinkKey = subLinkKey;
    }
}