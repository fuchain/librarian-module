package com.fpt.edu.linkresource;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Component
public class LinkDef {
    private static final String LINK_PATH="src/main/resources/schema-definition/link.csv";
    private List<Link> listLinks;
    public LinkDef() {
        listLinks= new ArrayList<>();
        Reader reader = null;
        try {
            reader = Files.newBufferedReader(Paths.get(LINK_PATH));
            CsvToBean<Link> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(Link.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            Iterator<Link> csvLinkIterator = csvToBean.iterator();
            while (csvLinkIterator.hasNext()) {
                Link link = csvLinkIterator.next();
                listLinks.add(link);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public List<Link> getListLinks() {
        return listLinks;
    }

    public void setListLinks(List<Link> listLinks) {
        this.listLinks = listLinks;
    }
}
