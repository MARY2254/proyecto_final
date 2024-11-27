package pe.edu.upeu.parisfx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ModeloDataAutocomplet {
    String idx;
    String nameDysplay;
    String otherData;
    @Override
    public String toString() {
        return nameDysplay+" "+idx+" " + otherData;
    }
}
