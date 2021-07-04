package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class GetProductGroupDto {
    @Getter
    @Setter
    @JsonProperty("SellerSKU")
    private String sellerSKU;						//제품그룹의 고유 판매자 SKU

    @Getter @Setter
    @JsonProperty("Fields")
    private JSONArray fields;

    @Getter @Setter
    @JsonProperty("BuyableProducts")
    private List<GetBuyableProductsDto> buyableProducts; //제품그룹에 속한 상품리스트

    /**
     * 생성자 초기화
     */
    public GetProductGroupDto(){
        sellerSKU = null;
        fields = new JSONArray();
        buyableProducts = new ArrayList<GetBuyableProductsDto>();
    }
}
