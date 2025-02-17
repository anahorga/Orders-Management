package model;

import java.time.LocalDateTime;

/**
 * Este definita o clasa imutabila folosid java Records
 * @param idComanda
 * @param pretTotal
 * @param data
 */
public record Bill(int idComanda, int pretTotal, LocalDateTime data) {


}
