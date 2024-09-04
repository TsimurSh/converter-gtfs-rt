package pl.goeuropa.converter.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class VehiclesDto {

    List<Map<String, Object>> list;

}
