package ru.otus;

import ru.otus.banknotes.Banknote;
import ru.otus.exceptions.NotEnoughMoneyInAtmException;
import ru.otus.exceptions.NotNeededNominalInAtmException;

import java.util.List;

public interface Atm {
    /**
     * Принимает банкноты, если есть соответсвующее слоты. Иначе возвращает непринятые банкноты.
     *
     * @param banknotes список банкнот.
     * @return банкноты, которые не принял ATM.
     */
    List<Banknote> acceptBanknotes(List<Banknote> banknotes);

    /**
     * Возвращает запрошенную сумму.
     *
     * @param sum запрашиваемая сумма.
     * @return список банкнот указанной суммы.
     * @throws NotNeededNominalInAtmException если нет нужного номинала.
     * @throws NotEnoughMoneyInAtmException   если недостаточно денег в банкомате.
     */
    List<Banknote> takeSum(int sum);

    /**
     * Возвращает остаток сумму в банкомате.
     *
     * @return остаток сумму в банкомате.
     */
    int getRemainingSum();
}
