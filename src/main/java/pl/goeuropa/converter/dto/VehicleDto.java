package pl.goeuropa.converter.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehicleDto {

    @CsvBindByName(column = "_id")
    private String id;

    @CsvBindByName(column = "Nr_Boczny")
    private String sideNumber;

    @CsvBindByName(column = "Nr_Rej")
    private String registrationNumber;

    @CsvBindByName(column = "Brygada")
    private String squad;

    @CsvBindByName(column = "Nazwa_Linii")
    private String trip;

    @CsvBindByName(column = "Ostatnia_Pozycja_Szerokosc")
    private double lat;

    @CsvBindByName(column = "Ostatnia_Pozycja_Dlugosc")
    private double lon;

    @CsvBindByName(column = "Data_Aktualizacji")
    private String lastUpdate;
}
