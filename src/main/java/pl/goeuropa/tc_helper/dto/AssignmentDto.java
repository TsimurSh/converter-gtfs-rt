package pl.goeuropa.tc_helper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AssignmentDto {

    @NotNull
    private String key;
    @JsonProperty("vehicles")
    private List<Assignment> assignmentsList;

}


