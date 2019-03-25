package hx.widget.dialog.address;

/**
 * Created by Rose on 3/23/2017.
 */

public class Address {

    public static final String PROVINCE_SUFFIX = "Province";
    public static final String CITY_SUFFIX = "City";
    public static final String COUNTY_SUFFIX = "Area";

    private City province;
    private City city;
    private City county;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public City getCounty() {
        return county;
    }

    public void setCounty(City county) {
        this.county = county;
    }

    public City getProvince() {
        return province;
    }

    public void setProvince(City province) {
        this.province = province;
    }
}
