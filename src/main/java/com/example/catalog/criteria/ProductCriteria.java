package com.example.catalog.criteria;

import com.example.catalog.enums.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCriteria {
    @Nullable
    public String name;

    @Nullable
    public Integer amount, amountLo, amountHi;

    @Nullable
    public Double price, priceLo, priceHi;

    @Nullable
    public Integer weight, weightLo, weightHi;

    @Nullable
    public Country country;

    @Nullable
    public Integer year, yearLo, yearHi;

    @Nullable
    public String sortBy;

    @Nullable
    public Integer limit;

    @Nullable
    public Integer offset;
}
