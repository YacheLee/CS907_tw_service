package uk.ac.warwick.site.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class NewsController {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    JdbcTemplate jdbcTemplate;

    public String getQueryForYear(int year){
        return "timestamp between '" + year + "-01-01' and '" + year + "-12-31' AND ";
    }

    @GetMapping("/api/news")
    public ArrayNode getNews(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "true", value="is_tw_2016") boolean isTw2016,
            @RequestParam(defaultValue = "true", value="is_tw_2017") boolean isTw2017,
            @RequestParam(defaultValue = "true", value="is_tw_2018") boolean isTw2018){
        List list = new ArrayList();
        if(isTw2016){
            list.add(2016);
        }
        if(isTw2017){
            list.add(2017);
        }
        if(isTw2018){
            list.add(2018);
        }

        if(list.isEmpty()){
            list.add(3000);
        }

        String sql = "SELECT * FROM appledaily WHERE " +
                "date_part('year', timestamp) in (:year) " +
                "AND text LIKE :keyword";

        ArrayNode arrayNode = objectMapper.createArrayNode();
        Map<String, Object> map = new HashMap();
        map.put("year", list);
        map.put("keyword", "%"+keyword+"%");
        namedParameterJdbcTemplate.query(sql, map, rs->{
            arrayNode.add(rs.getString("title"));
        });
        return arrayNode;
    }
}