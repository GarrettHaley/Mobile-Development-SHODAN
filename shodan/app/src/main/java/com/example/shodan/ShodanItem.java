package com.example.shodan;

import java.io.Serializable;

/* Default class used to represent each location from the shodan api call
 */
public class ShodanItem implements Serializable {
    public Long ip;
    public Integer port;
    public String transport;
    public String city;
    public Double latitude;
    public Double longitude;
    public String countryCode;
    public String organization;
    public String title;
    public String isp;
    public String timestamp;
    public String product;

}
