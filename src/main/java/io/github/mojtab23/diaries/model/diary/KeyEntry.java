package io.github.mojtab23.diaries.model.diary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by mojtab23 on 6/5/2017.
 */
public class KeyEntry implements Comparable<KeyEntry> {

    private final Id id;
    private final int fieldNumber;


    public KeyEntry(Id id, int fieldNumber) {
        this.id = id;
        this.fieldNumber = fieldNumber;
    }

    @Nullable
    public static KeyEntry readKeyEntry(DataInputStream inputStream) {
        try {
            final Id id = Id.readId(inputStream);
            final int fieldNumber = inputStream.readInt();
            return new KeyEntry(id, fieldNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Id getId() {
        return id;
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

    public byte[] toByteArray() {
        return ByteBuffer.allocate(Id.ID_BYTES + Integer.BYTES).put(id.toByteArray()).
                putInt(fieldNumber).array();
    }

    @Override
    public int compareTo(@NotNull KeyEntry o) {
        final int i = this.getId().compareTo(o.getId());
        if (i == 0) {
            return this.fieldNumber - o.fieldNumber;
        }
        return i;
    }
}
