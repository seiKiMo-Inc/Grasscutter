package io.grasscutter.utils.objects;

import io.grasscutter.data.DataSerializable;
import io.grasscutter.data.FieldType;
import io.grasscutter.data.Special;
import io.grasscutter.utils.interfaces.Serializable;

@DataSerializable(table = "counters")
public final class Counter implements Serializable {
    @Special(FieldType.ID)
    public String type = ""; // The counter's type.

    public long value = 0; // The counter's value.
}
