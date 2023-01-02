package com.nowcoder.community.entity;

import lombok.Data;

/**
 * @Description:封装分页相关信息
 * @Author:DDD_coder
 * @Date:2023/1/2 10:49
 */

@Data
public class Page {

    //当前页码
    private int current = 1;

    //显示上限
    private int limit = 10;

    //数据总数
    private int rows;

    //查询路径
    private String path;

    public void setCurrent(int current) {
        if(current >= 1) {
            this.current = current;
        }
    }

    public void setLimit(int limit) {
        if(limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public void setRows(int rows){
        if(rows >= 0){
            this.rows = rows;
        }
    }

    /**
    * Description:获取偏移量
    * date: 2023/1/2 10:56
    * @author: fyd
    */
    public int getOffset(){
        return (current - 1) * limit;
    }

    /**
    * Description:获取总页数
    * date: 2023/1/2 10:58
    * @author: fyd
    */
    public int getTotal(){
        return rows%limit == 0 ? rows/limit : rows/limit + 1;
    }

    /**
    * Description:获取显示的起始页数
    * date: 2023/1/2 11:00
    * @author: fyd
    */
    public int getFrom(){
        int from = current - 2;
        return from < 1 ? 1 : from;
    }

    /**
    * Description:获取显示的末尾页数
    * date: 2023/1/2 11:01
    * @author: fyd
    */
    public int getTo(){
        int to = current + 2;
        int total = getTotal();
        return to > total ? total : to;
    }
}
