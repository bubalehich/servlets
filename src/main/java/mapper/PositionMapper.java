package mapper;

import dto.PositionDto;

public class PositionMapper {

    private static final String DELIMITER = "-";

    public PositionDto mapToPositionDtoFromString(String position) {
        var positionArray = position.split(DELIMITER);

        return new PositionDto(positionArray[0], Integer.parseInt(positionArray[1]));
    }
}
