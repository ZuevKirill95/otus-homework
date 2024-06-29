package ru.otus.banknotes;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.otus.NominalType;

@EqualsAndHashCode
@ToString
public class BanknoteImpl implements Banknote {
    private final NominalType nominalType;

    public BanknoteImpl(NominalType nominalType) {
        this.nominalType = nominalType;
    }

    @Override
    public NominalType getNominal() {
        return nominalType;
    }
}
