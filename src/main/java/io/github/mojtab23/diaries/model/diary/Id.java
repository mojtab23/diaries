package io.github.mojtab23.diaries.model.diary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.NetworkInterface;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by mojtab23 on 6/4/2017.
 */

public class Id implements Comparable<Id>, Serializable {
    public static final int ID_BYTES = Long.BYTES + (3 * Integer.BYTES);
    private static final Logger LOGGER = LoggerFactory.getLogger(Id.class);
    private final static int machineId;
    private final static int processId;
    private final static SecureRandom secureRandom = new SecureRandom();
    private final static AtomicInteger nextCounter = new AtomicInteger(secureRandom.nextInt());
    private static final long serialVersionUID = 3954054651967389957L;

    static {

        machineId = createMachineIdentifier();
        processId = createProcessIdentifier();

    }

    private final long timestamp;
    private final int machineIdentifier;
    private final int processIdentifier;
    private final int counter;

    private Id(long epochSecond, int machineId, int processId, int counter) {
        timestamp = epochSecond;
        machineIdentifier = machineId;
        processIdentifier = processId;
        this.counter = counter;
    }

    public Id() {
        this(Instant.now().getEpochSecond(), machineId, processId, nextCounter.incrementAndGet());
    }

    private static int createProcessIdentifier() {
//in java 9
//        long pid = ProcessHandle.current().getPid();


        int processId;
        try {
            String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
            if (processName.contains("@")) {
                processId = Integer.parseInt(processName.substring(0, processName.indexOf('@')));
            } else {
                processId = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().hashCode();
            }

        } catch (Throwable t) {
            processId = secureRandom.nextInt();
            LOGGER.warn("Failed to get process identifier from JMX, using random number instead", t);
        }

        return processId;
    }

    private static int createMachineIdentifier() {
        // build a 2-byte machine piece based on NICs info
        int machinePiece;
        try {
            StringBuilder sb = new StringBuilder();
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface ni = e.nextElement();
                sb.append(ni.toString());
                byte[] mac = ni.getHardwareAddress();
                if (mac != null) {
                    ByteBuffer bb = ByteBuffer.wrap(mac);
                    try {
                        sb.append(bb.getChar());
                        sb.append(bb.getChar());
                        sb.append(bb.getChar());
                    } catch (BufferUnderflowException shortHardwareAddressException) { //NOPMD
                        // mac with less than 6 bytes. continue
                    }
                }
            }
            machinePiece = sb.toString().hashCode();
        } catch (Throwable t) {
            // exception sometimes happens with IBM JVM, use random
            machinePiece = (secureRandom.nextInt());
            LOGGER.warn("Failed to get machine identifier from network interface, using random number instead", t);
        }
        return machinePiece;
    }

    @Nullable
    public static Id readId(DataInputStream inputStream) {
        try {
            final long time = inputStream.readLong();
            final int machineId = inputStream.readInt();
            final int processId = inputStream.readInt();
            final int counter = inputStream.readInt();
            return new Id(time, machineId, processId, counter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    public static Id createId(byte[] bytes) {
        if (bytes.length != ID_BYTES) {
            LOGGER.warn("size miss match! expect:" + ID_BYTES + " but input size:" + bytes.length);
            throw new IllegalArgumentException("size miss match! expect:" + ID_BYTES + " but input size:" + bytes.length);
        } else {
            final ByteBuffer buffer = ByteBuffer.wrap(bytes);
            final long timestamp = buffer.getLong();
            final int machineId = buffer.getInt();
            final int processId = buffer.getInt();
            final int counter = buffer.getInt();

            return new Id(timestamp, machineId, processId, counter);

        }
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getMachineIdentifier() {
        return machineIdentifier;
    }

    public int getProcessIdentifier() {
        return processIdentifier;
    }

    public int getCounter() {
        return counter;
    }

    @Override
    public String toString() {
        return "Id{" + Long.toHexString(timestamp) +
                "." + Integer.toHexString(machineIdentifier) +
                "." + Integer.toHexString(processIdentifier) +
                "." + Integer.toHexString(counter) + '}';
    }

    @Override
    public int compareTo(@NotNull Id other) {

        byte[] byteArray = toByteArray();
        byte[] otherByteArray = other.toByteArray();
        for (int i = 0; i < ID_BYTES; i++) {
            if (byteArray[i] != otherByteArray[i]) {
                return ((byteArray[i] & 0xff) < (otherByteArray[i] & 0xff)) ? -1 : 1;
            }
        }
        return 0;

    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Id objectId = (Id) o;

        return counter == objectId.counter && machineIdentifier == objectId.machineIdentifier &&
                processIdentifier == objectId.processIdentifier && timestamp == objectId.timestamp;
    }

    @Override
    public int hashCode() {
        int result = (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + machineIdentifier;
        result = 31 * result + processIdentifier;
        result = 31 * result + counter;
        return result;
    }

    public byte[] toByteArray() {
        return ByteBuffer.allocate(ID_BYTES).putLong(timestamp).
                putInt(machineIdentifier).putInt(processIdentifier).putInt(counter).array();
    }

}
