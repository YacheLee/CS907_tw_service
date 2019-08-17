package uk.ac.warwick.site.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class NewsController {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/api/news")
    public ArrayNode getNews(@RequestParam String keyword){
        String sql = "select * from appledaily WHERE text LIKE :keyword";
        ArrayNode arrayNode = objectMapper.createArrayNode();
        Map<String, Object> map = new HashMap();
        map.put("keyword", "%"+keyword+"%");
        namedParameterJdbcTemplate.query(sql, map, rs->{
            arrayNode.add(rs.getString("text"));
        });
        return arrayNode;
    }
}