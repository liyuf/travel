package com.heima.travel.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Data
public class PageBean {
//    private int cid;//路线id
    private int start;//起始页                  无须定义
    private int currentPage;//当前页
    private int end;//末页                      无须定义
    private int count;//显示的条数
    private int total;//总条数
    private int totalPages;//总页数
    private List<? extends Object> list;//当页内容



    public int getStart(){
        showPages();
        return start;
    }
    public int getEnd(){
        showPages();
        return end;
    }
    //分页方法
    public void showPages(){
        if(totalPages<=10){
            start=1;
            end=totalPages;
        }else{
            if(currentPage<=5){
                start=1;
                end=10;
            }else if (currentPage>=totalPages-5){
                start=totalPages-9;
                end=totalPages;
            }else{
                start=currentPage-4;
                end=currentPage+5;
            }
        }
    }
}
