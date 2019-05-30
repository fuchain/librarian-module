package com.fpt.edu.linkresource;

import com.opencsv.bean.CsvBindByName;


public class Link {
    @CsvBindByName(column = "LINK_NAME", required = true)
    private String linkName;
    @CsvBindByName(column = "END_POINT_KEY", required = true)
    private String endPointKey;
    @CsvBindByName(column = "TITLE", required = true)
    private String title;
    @CsvBindByName(column = "HREF", required = true)
    private String href;


    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getEndPointKey() {
        return endPointKey;
    }

    public void setEndPointKey(String endPointKey) {
        this.endPointKey = endPointKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
