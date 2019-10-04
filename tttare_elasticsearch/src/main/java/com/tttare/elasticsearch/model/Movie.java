package com.tttare.elasticsearch.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * ClassName: Movie <br/>
 * Description: <br/>
 * date: 2019/10/3 22:20<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Getter
@Setter
@Document(indexName = "item",type = "docs", shards = 1, replicas = 0)
//shards 分片;replicas 副本
public class Movie {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String name;

    @Field(type = FieldType.Keyword)
    private String type;//惊悚 悬疑 喜剧 动作 科幻 动画 英语剧 舞台剧

    @Field(type = FieldType.Keyword)
    private String director;//导演

    @Field(type = FieldType.Keyword)
    private String actor;//代表演员

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String brief;//电影简述

    @Field(index = false, type = FieldType.Keyword)
    private String filePath;

    @Field(index = false, type = FieldType.Integer)
    private int favCount;

    @Field(index = false, type = FieldType.Keyword)
    private String createDate;

    public Movie(){

    }

    public Movie(String id,String name,String type,String director,String actor,String brief,String filePath,int favCount,String createDate){
        this.id=id;
        this.name=name;
        this.type=type;
        this.director=director;
        this.actor=actor;
        this.brief=brief;
        this.filePath=filePath;
        this.favCount=favCount;
        this.createDate=createDate;
    }

}
