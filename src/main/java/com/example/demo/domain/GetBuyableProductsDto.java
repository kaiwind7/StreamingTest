package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONArray;

public class GetBuyableProductsDto {
    @Getter
    @Setter
    @JsonProperty("SellerSKU")
    private String sellerSKU;						//제품그룹의 고유 판매자 SKU

    @Getter
    @Setter
    @JsonProperty("Quantity")
    private int quantity;

    @Getter @Setter
    @JsonProperty("ListingStatus")
    private String listingStatus;				//상품상태 (Live/NotLive/Pending)

    @Getter @Setter
    @JsonProperty("Fields")
    private JSONArray fields;

    /**
     * 생성자 초기화
     */
    public GetBuyableProductsDto(){
        sellerSKU = null;
        listingStatus = null;
        quantity = 0;
        fields = new JSONArray();
    }
}
