package io.github.mojtab23.diaries.binding;

import jetbrains.exodus.ArrayByteIterable;
import jetbrains.exodus.bindings.ComparableBinding;
import jetbrains.exodus.util.LightOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by mojtab23 on 6/26/2017.
 */
public class ArrayByteIterableBinding extends ComparableBinding {
    public final static ArrayByteIterableBinding BINDING = new ArrayByteIterableBinding();

    private ArrayByteIterableBinding() {
    }


    @Override
    public Comparable readObject(@NotNull ByteArrayInputStream stream) {
        try {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[1000];

        while ((nRead = stream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }


            buffer.flush();
            return new ArrayByteIterable(buffer.toByteArray())
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeObject(@NotNull LightOutputStream output, @NotNull Comparable object) {

    }
}
