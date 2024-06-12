package ru.otus;

import ru.otus.banknotes.Banknote;
import ru.otus.exceptions.NotEnoughMoneyInAtmException;
import ru.otus.exceptions.NotNeededNominalInAtmException;
import ru.otus.slots.Slot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AtmImpl implements Atm {
    private final Map<NominalType, Slot> slotsMap = new EnumMap<>(NominalType.class);
    private final List<NominalType> sortedNominalTypes;

    public AtmImpl(Set<Slot> slots) {
        slots.forEach(slot -> this.slotsMap.put(slot.getNominal(), slot));
        sortedNominalTypes = slotsMap.keySet().stream()
                .sorted(Comparator.comparingInt(NominalType::getValue))
                .toList()
                .reversed();
    }

    @Override
    public List<Banknote> acceptBanknotes(List<Banknote> banknotes) {
        ArrayList<Banknote> rejectedBanknotes = new ArrayList<>();

        for (Banknote banknote : banknotes) {
            NominalType nominal = banknote.getNominal();

            if (slotsMap.containsKey(nominal)) {
                Slot slot = slotsMap.get(nominal);

                slot.putBanknote(banknote);
            } else {
                rejectedBanknotes.add(banknote);
            }
        }

        return rejectedBanknotes;
    }

    @Override
    public List<Banknote> takeSum(int sum) {
        if (sum > getRemainingSum()) {
            throw new NotEnoughMoneyInAtmException();
        }

        int resultSum = 0;
        List<Banknote> resultBanknotes = new ArrayList<>();

        for (NominalType nominal : sortedNominalTypes) {
            if (sum < nominal.getValue()) {
                continue;
            }

            Slot slot = slotsMap.get(nominal);
            int amountBanknotes = slot.getAmountBanknotes();

            for (int i = 0; i < amountBanknotes; i++) {
                if (resultSum + nominal.getValue() > sum) {
                    break;
                }

                Banknote banknote = slot.takeBanknote();
                resultBanknotes.add(banknote);
                resultSum += nominal.getValue();
            }
        }

        if (resultSum != sum) {
            acceptBanknotes(resultBanknotes);
            throw new NotNeededNominalInAtmException();
        }

        return resultBanknotes;
    }

    @Override
    public int getRemainingSum() {
        return slotsMap.values().stream()
                .mapToInt(Slot::getSum)
                .sum();
    }
}
