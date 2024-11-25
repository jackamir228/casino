package org.example.casino.mapper;

import org.example.casino.dto.SlotDtoRequest;
import org.example.casino.dto.SlotDtoResponse;
import org.example.casino.entity.Slot;

public interface SlotMapper extends Mapper<Slot, SlotDtoRequest, SlotDtoResponse> {
    @Override
    default Slot convertRequestToEntity(SlotDtoRequest request) {
        return new Slot(null, request.firstSymbol(), request.secondSymbol(), request.thirdSymbol());
    }

    @Override
    default Slot convertResponseToEntity(SlotDtoResponse response) {
        return new Slot(response.id(), response.firstSymbol(), response.secondSymbol(), response.thirdSymbol());
    }

    @Override
    default SlotDtoResponse convertEntityToResponse(Slot entity) {
        return new SlotDtoResponse(
                entity.getId(),
                entity.getFirstSymbol(),
                entity.getSecondSymbol(),
                entity.getThirdSymbol());
    }
}
