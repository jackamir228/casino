package org.example.casino.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.casino.dto.SlotDtoRequest;
import org.example.casino.dto.SlotDtoResponse;
import org.example.casino.entity.Slot;
import org.example.casino.enums.SlotSqlFieldName;
import org.example.casino.mapper.SlotMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SlotRepository implements AdvancedRepository<Slot>, SlotMapper {

    private final JdbcTemplate jdbcTemplate;


    private final TransactionTemplate transactionTemplate;

    public SlotDtoResponse create(Slot slot) {
        String sql = "INSERT INTO game (%s, %s, %s) values(?, ?, ?)".formatted(
                SlotSqlFieldName.FIRST_SYM.getSqlFieldName(),
                SlotSqlFieldName.SECOND_SYM.getSqlFieldName(),
                SlotSqlFieldName.THIRD_SYM.getSqlFieldName()
        );

        transactionTemplate.executeWithoutResult((transactionStatus) -> {
            String symbolCombination = slot.getFirstSymbol() + slot.getSecondSymbol() + slot.getThirdSymbol();
            int rowsUpdate = jdbcTemplate.update(
                    sql,
                    preparedStatement -> {
                        //точка сохранения
                        Object savepoint = transactionStatus.createSavepoint();
                        preparedStatement.setString(1, slot.getFirstSymbol());
                        preparedStatement.setString(2, slot.getSecondSymbol());
                        preparedStatement.setString(3, slot.getThirdSymbol());

                        if ("AFD".equals(symbolCombination)) {
                            log.info("rollback symbol inserting");
                            transactionStatus.rollbackToSavepoint(savepoint);
                        }
                    });
            log.info("rows was updated: {}", rowsUpdate);
        });
        return convertEntityToResponse(slot);
    }

    public List<SlotDtoResponse> findAll() {
        String sql = "select * from game";

        return jdbcTemplate.query(sql, this::slotRowMapper).stream()
                .map(this::convertEntityToResponse)
                .toList();
    }

    public List<SlotDtoResponse> findHistorySlots() {
        String sql = "select * from game order by id desc limit 5;";
        return jdbcTemplate.query(sql, this::slotRowMapper).stream()
                .map(this::convertEntityToResponse)
                .toList();
    }

    public void deleteAll() {
        String sql = "TRUNCATE TABLE game";
        int updateRows = jdbcTemplate.update(sql);
        log.info("Rows was update: {}", updateRows);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM game WHERE id=?";

        int countUpdateRows = jdbcTemplate.update(sql, id);
        jdbcTemplate.update(sql, preparedStatement -> preparedStatement.setLong(1, id));

        if (countUpdateRows == 0) {
            log.warn("Entity with id {} does not exist", id);
        }
    }

    @Override
    public Optional<Slot> findById(Long id) {
        String sql = "SELECT * FROM game where id = ?";


        List<Slot> query = jdbcTemplate.query(
                sql,
                preparedStatement -> preparedStatement.setLong(1, id),
                this::slotRowMapper);
        Slot slots = query.getFirst();
        return Optional.of(slots);

    }

    private Slot slotRowMapper(ResultSet resultSet, int rowNum) throws SQLException {
        Slot slot = new Slot();
        slot.setId(resultSet.getLong("id"));
        slot.setFirstSymbol(resultSet.getString("first_sym"));
        slot.setSecondSymbol(resultSet.getString("second_sym"));
        slot.setThirdSymbol(resultSet.getString("third_sym"));
        return slot;
    }


    /**
     * Mapping methods
     */

    @Override
    public Slot convertRequestToEntity(SlotDtoRequest request) {
        return SlotMapper.super.convertRequestToEntity(request);
    }

    @Override
    public Slot convertResponseToEntity(SlotDtoResponse response) {
        return SlotMapper.super.convertResponseToEntity(response);
    }

    @Override
    public SlotDtoResponse convertEntityToResponse(Slot entity) {
        return SlotMapper.super.convertEntityToResponse(entity);
    }
}
