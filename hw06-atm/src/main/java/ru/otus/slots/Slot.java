package ru.otus.slots;

import ru.otus.NominalType;
import ru.otus.banknotes.Banknote;

public interface Slot {
    /**
     * Возвращает номинал слота.
     *
     * @return номинал слота.
     */
    NominalType getNominal();

    /**
     * Возвращает количество банкнот в слоте.
     *
     * @return количество банкнот в слоте.
     */
    int getAmountBanknotes();

    /**
     * Возвращает сумму денежных средств в слоте.
     *
     * @return сумма денежных средств в слоте.
     */
    int getSum();

    /**
     * Принимает банкноту в слот.
     *
     * @param banknote банкнота.
     * @return true, если банкнота принята. Иначе false.
     */
    boolean putBanknote(Banknote banknote);

    /**
     * Удаляет банкноту из слота.
     *
     * @return банкнота.
     */
    Banknote takeBanknote();
}
