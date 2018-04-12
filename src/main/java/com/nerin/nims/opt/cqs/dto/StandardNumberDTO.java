package com.nerin.nims.opt.cqs.dto;

/**
 * Created by yinglgu on 2017/3/16.
 */
public class StandardNumberDTO {
    private String standard_number;
    private String number_inexistent_year;
    private String search_key;

    public String getStandard_number() {
        return standard_number;
    }

    public void setStandard_number(String standard_number) {
        this.standard_number = standard_number;
    }

    public String getNumber_inexistent_year() {
        return number_inexistent_year;
    }

    public void setNumber_inexistent_year(String number_inexistent_year) {
        this.number_inexistent_year = number_inexistent_year;
    }

    public String getSearch_key() {
        return search_key;
    }

    public void setSearch_key(String search_key) {
        this.search_key = search_key;
    }
}
