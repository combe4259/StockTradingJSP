package org.zerock.stocktrading.util;

public class QueryStringBuilder {
    private final StringBuilder queryBuilder = new StringBuilder("?");
    private boolean firstParam = true;

    public QueryStringBuilder addParam(String key, String value){
        if(firstParam){
            firstParam = false;
        }else {
            queryBuilder.append("&");
        }
        queryBuilder.append(key).append("=").append(value);
        return this;
    }

    public String build(){
        return queryBuilder.toString();
    }
}
