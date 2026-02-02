package pl.goeuropa.converter.dto;

import lombok.Data;

@Data
public class VehicleDto {

        String name;
        String type;
        float x;
        float y;
        long k;

    public boolean validateLatLon() {
        if(this.x < -90 || this.x > 90)
            return false;
        if(this.y < -180 || this.y > 180)
            return false;
        else
            return true;
    }
}
