package com.fpt.edu.linkresource;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class EndPointDef {
    private   String END_POINT_PATH = "src/main/resources/schema-definition/endPoint.csv";
    private List<EndPoint> listEndpoints;

    private LinkDef linkDef;

    public EndPointDef(LinkDef linkDef) {
        END_POINT_PATH= END_POINT_PATH.replace("/", File.separator);
        this.linkDef=linkDef;
        listEndpoints= new ArrayList<>();
        Reader reader = null;
        try {
            reader = Files.newBufferedReader(Paths.get(END_POINT_PATH));
            CsvToBean<EndPoint> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(EndPoint.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            Iterator<EndPoint> csvEndPointIterator = csvToBean.iterator();

            while (csvEndPointIterator.hasNext()) {
                EndPoint endPoint = csvEndPointIterator.next();
                listEndpoints.add(endPoint);
            }
            for (int i = 0; i < listEndpoints.size(); i++) {
                EndPoint endPoint = listEndpoints.get(i);
                endPoint.setLinkList(getLinkByEndPointSublinkKey(endPoint.getSubLinkKey()));
            }



        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public List<Link> getLinkByEndPointSublinkKey(String keyName){
        ArrayList<Link> result= new ArrayList<>();

        for (int i = 0; i <linkDef.getListLinks().size() ; i++) {
                Link currentLink=linkDef.getListLinks().get(i);
                if(keyName.equals(currentLink.getEndPointKey())){
                    result.add(currentLink) ;
                }
        }
        return result;
    }

    public List<EndPoint> getListEndpoints() {
        return listEndpoints;
    }

    public void setListEndpoints(List<EndPoint> listEndpoints) {
        this.listEndpoints = listEndpoints;
    }
}
