package com.wyh.happyyousdk.model.request.graph;

public class FetchStepsRequest {

    private String tab;
    private DateRange dateRange;
    private DataPoints[] dataPoints;
    private String UUID;

    public FetchStepsRequest(String tab, DateRange dateRange, DataPoints[] dataPoints, String UUID) {
        this.tab = tab;
        this.dateRange = dateRange;
        this.dataPoints = dataPoints;
        this.UUID = UUID;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    public DataPoints[] getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(DataPoints[] dataPoints) {
        this.dataPoints = dataPoints;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public static class DataPoints {
        private String date;
        private Double point;
        private Double point2;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Double getPoint() {
            return point;
        }

        public void setPoint(Double point) {
            this.point = point;
        }

        public Double getPoint2() {
            return point2;
        }

        public void setPoint2(Double point2) {
            this.point2 = point2;
        }
    }

    public static class DateRange {
        private String endDate;
        private String startDate;

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }
    }

}
