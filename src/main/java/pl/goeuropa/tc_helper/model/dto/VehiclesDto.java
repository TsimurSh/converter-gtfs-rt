package pl.goeuropa.tc_helper.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class VehiclesDto {

    List<Map<String, Object>> list;
}
