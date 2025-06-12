package com.wyh.happyyousdk.Eventbus;

import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse;

public class BookmarkhealthtvEvent {
    public int  position;
    public  GetDashboardDataResponse.Data.HealthTv data;
    public  boolean isbookmarked;

    public BookmarkhealthtvEvent(int position, GetDashboardDataResponse.Data.HealthTv data, boolean isbookmarked) {
        this.position = position;
        this.data = data;
        this.isbookmarked = isbookmarked;
    }
}
