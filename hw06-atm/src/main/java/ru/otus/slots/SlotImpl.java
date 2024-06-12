package ru.otus.slots;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.otus.NominalType;
import ru.otus.banknotes.Banknote;

import java.util.LinkedList;
import java.util.SequencedCollection;

@Getter
@EqualsAndHashCode
@ToString
public class SlotImpl implements Slot {
    private final NominalType nominalType;

    public SlotImpl(NominalType nominalType) {
        this.nominalType = nominalType;
    }

    private final SequencedCollection<Banknote> banknotes = new LinkedList<>();

    @Override
    public NominalType getNominal() {
        return nominalType;
    }

    @Override
    public int getAmountBanknotes() {
        return banknotes.size();
    }

    @Override
    public int getSum() {
        return banknotes.size() * nominalType.getValue();
    }

    @Override
    public boolean putBanknote(Banknote banknote) {
        if (nominalType != banknote.getNominal()) {
            return false;
        }

        banknotes.add(banknote);
        return true;
    }

    @Override
    public Banknote takeBanknote() {
        return banknotes.removeLast();
    }
}
