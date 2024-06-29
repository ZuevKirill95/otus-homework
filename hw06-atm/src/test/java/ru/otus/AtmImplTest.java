package ru.otus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.banknotes.Banknote;
import ru.otus.banknotes.BanknoteImpl;
import ru.otus.exceptions.NotEnoughMoneyInAtmException;
import ru.otus.exceptions.NotNeededNominalInAtmException;
import ru.otus.slots.SlotImpl;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AtmTest {
    @DisplayName("ATM должен иметь столько же, сколько в него внесли")
    @Test
    void atmMustHaveTheSameAmountAsItWasPutIntoIt() {
        Atm atm = new AtmImpl(Set.of(
                new SlotImpl(NominalType.FIFTY),
                new SlotImpl(NominalType.ONE_HUNDRED),
                new SlotImpl(NominalType.ONE_THOUSAND)
        ));

        // 50 + 100 + 100 + 1000
        List<Banknote> banknotes = List.of(
                new BanknoteImpl(NominalType.FIFTY),
                new BanknoteImpl(NominalType.ONE_HUNDRED),
                new BanknoteImpl(NominalType.ONE_HUNDRED),
                new BanknoteImpl(NominalType.ONE_THOUSAND)
        );

        atm.acceptBanknotes(banknotes);

        assertEquals(1250, atm.getRemainingSum());
    }

    @DisplayName("ATM должен вернуть 150 рублей. Если в банкомате есть 50, 100, 100 и 1000")
    @Test
    void atmMustReturn150Rubles() {
        Atm atm = new AtmImpl(Set.of(
                new SlotImpl(NominalType.FIFTY),
                new SlotImpl(NominalType.ONE_HUNDRED),
                new SlotImpl(NominalType.ONE_THOUSAND)
        ));

        // 50 + 100 + 100 + 1000 = 1250
        List<Banknote> banknotes = List.of(
                new BanknoteImpl(NominalType.FIFTY),
                new BanknoteImpl(NominalType.ONE_HUNDRED),
                new BanknoteImpl(NominalType.ONE_HUNDRED),
                new BanknoteImpl(NominalType.ONE_THOUSAND)
        );

        atm.acceptBanknotes(banknotes);

        List<Banknote> resBanknotes = atm.takeSum(150);

        assertEquals(2, resBanknotes.size());
        long countFifties = resBanknotes.stream()
                .filter(banknote -> banknote.getNominal() == NominalType.FIFTY)
                .count();
        long countOneHundreds = resBanknotes.stream()
                .filter(banknote -> banknote.getNominal() == NominalType.ONE_HUNDRED)
                .count();

        assertEquals(1, countFifties);
        assertEquals(1, countOneHundreds);
        assertEquals(1100, atm.getRemainingSum());
    }

    @DisplayName("ATM не должен вернуть 150 рублей, если есть только 1000 и 100")
    @Test
    void atmShouldNotReturn150RublesIfThereAreOnly1000And100() {
        Atm atm = new AtmImpl(Set.of(
                new SlotImpl(NominalType.FIFTY),
                new SlotImpl(NominalType.ONE_HUNDRED),
                new SlotImpl(NominalType.ONE_THOUSAND)
        ));

        // 1000 + 100 = 1_100
        List<Banknote> banknotes = List.of(
                new BanknoteImpl(NominalType.ONE_THOUSAND),
                new BanknoteImpl(NominalType.ONE_HUNDRED)
        );

        atm.acceptBanknotes(banknotes);

        assertThrows(NotNeededNominalInAtmException.class, () -> atm.takeSum(150));

        assertEquals(1100, atm.getRemainingSum());
    }

    @DisplayName("ATM не должен вернуть 150 рублей, если есть только 50")
    @Test
    void atmShouldNotReturn150RublesIfThereAreOnly50() {
        Atm atm = new AtmImpl(Set.of(
                new SlotImpl(NominalType.FIFTY),
                new SlotImpl(NominalType.ONE_HUNDRED),
                new SlotImpl(NominalType.ONE_THOUSAND)
        ));

        // 50
        List<Banknote> banknotes = List.of(
                new BanknoteImpl(NominalType.FIFTY)
        );

        atm.acceptBanknotes(banknotes);

        assertThrows(NotEnoughMoneyInAtmException.class, () -> atm.takeSum(150));

        assertEquals(50, atm.getRemainingSum());
    }

    @DisplayName("ATM со слотом 100 должен принять 100, но не принять 50")
    @Test
    void atmWithSlot100ShouldAccept100butNotAccept50() {
        Atm atm = new AtmImpl(Set.of(
                new SlotImpl(NominalType.ONE_HUNDRED)
        ));

        // 50 + 100
        List<Banknote> banknotes = List.of(
                new BanknoteImpl(NominalType.FIFTY),
                new BanknoteImpl(NominalType.ONE_HUNDRED)
        );

        List<Banknote> rejectedBanknotes = atm.acceptBanknotes(banknotes);

        assertEquals(1, rejectedBanknotes.size());
        assertEquals(NominalType.FIFTY, rejectedBanknotes.getFirst().getNominal());
        assertEquals(100, atm.getRemainingSum());
    }
}