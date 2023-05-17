package org.example;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DES {

    static final byte[] PC_1 = {57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34,
            26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44,
            36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30,
            22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4};

    static final byte[] PC_2 = {14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44,
            49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32};

    static final byte[] IP = {58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20,
            12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40,
            32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43,
            35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7};

    static final byte[] E = {32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11,
            12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21, 22,
            23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1};

    static final byte[][] SBoxes = {{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7, // S1
            0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8,
            4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0,
            15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13},
            {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10, // S2
                    3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5,
                    0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15,
                    13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9},
            {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8, // S3
                    13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1,
                    13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7,
                    1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12},
            {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15, // S4
                    13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9,
                    10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4,
                    3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14},
            {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9, // S5
                    14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6,
                    4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14,
                    11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3},
            {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11, // S6
                    10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8,
                    9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6,
                    4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13},
            {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1, // S7
                    13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6,
                    1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2,
                    6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12},
            {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7, // S8
                    1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2,
                    7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8,
                    2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
    };

    static final byte[] P = {16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25};

    static final byte[] IPMinus1 = {40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25};

    private static final byte[] rotacje = {
            1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
    };


    public static String wygenerujKlucz() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        while (stringBuilder.length() < 16) {
            stringBuilder.append(Integer.toHexString(random.nextInt(16)));
        }
        return stringBuilder.toString();
    }

    private static final char[] HEX_ARRAY2 = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY2[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY2[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] permutacjaKlucza(byte[] oryginalnyKlucz, byte[] tablicaPermutacji) {
        byte[] permutowanyKlucz = new byte[tablicaPermutacji.length / 8];
        for (int i = 0; i < tablicaPermutacji.length; i++) {
            setBitAt(permutowanyKlucz, getBitAt(oryginalnyKlucz, tablicaPermutacji[i]), i + 1);
        }
        return permutowanyKlucz;
    }

    public static List<byte[]> generujPodklucze(byte[] permutowanyKlucz) {
        byte[] c = new byte[4];
        byte[] d = new byte[4];
        for (int i = 0; i < 28; i++) {
            setBitAt(c, getBitAt(permutowanyKlucz, i + 1), i + 1);    // split into 28-bit left and right (c and d) pairs.
            setBitAt(d, getBitAt(permutowanyKlucz, i + 29), i + 1);
        }
        List<byte[]> subKeys = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            rotateLeft(c, 28, rotacje[i]);
            rotateLeft(d, 28, rotacje[i]);
            byte[] subKey = new byte[8];
            for (int j = 0; j < 28; j++) {
                setBitAt(subKey, getBitAt(c, j + 1), j + 1);
                setBitAt(subKey, getBitAt(d, j + 1), j + 29);
            }
            subKeys.add(permutacjaKlucza(subKey, PC_2));
        }
        return subKeys;
    }

    public static byte[] obliczFunkcjeF(byte[] RKey, byte[] subKey) {
        byte[] permuteR = permutacjaKlucza(RKey, E); // funkcja rozpoczyna się od permutacji klucza z użyciem tablicy permutacji E
        permuteR = xor(subKey, permuteR);
        permuteR = applySBoxes(permuteR); //każde 6 bitów wyniku jest przekształcane na 4 bity z użyciem odpowiedniego SBoxa
        return permutacjaKlucza(permuteR, P);
    }

    public static byte[] applySBoxes(byte[] xorKey) {
        int SBoxIndex = 0;
        byte[] result = new byte[4];
        int index = 0;
        for (int i = 0; i < xorKey.length * 8; i += 6) {
            int row = (getBitAt(xorKey, i + 1) << 1) | (getBitAt(xorKey, i + 6));
            int column = (getBitAt(xorKey, i + 2) << 3) | (getBitAt(xorKey, i + 3) << 2) |
                    (getBitAt(xorKey, i + 4) << 1) | (getBitAt(xorKey, i + 5));
            for (int j = 0; j < 4; j++) {
                setBitAt(result, getBitAt(new byte[]{SBoxes[SBoxIndex][row * 16 + column]},
                        j + 5), index + 1);
                index++;
            }
            SBoxIndex++;
        }
        return result;
    }

    public static byte[] encodeMessage(byte[] data, String key, boolean decrypt) {
        List<byte[]> blocks = new ArrayList<>();
        for (int i = 0; i < data.length - data.length % 8; i += 8) {
            blocks.add(Arrays.copyOfRange(data, i, i + 8));
        }
        if (!decrypt) {
            blocks.add(addPadding(Arrays.copyOfRange(data, data.length - data.length % 8, data.length)));
        }
        for (int i = 0; i < blocks.size(); i++) {
            blocks.set(i, encodeBlock(blocks.get(i), key, decrypt));
        }
        if (decrypt) {
            blocks.set(blocks.size() - 1, trimPadding(blocks.get(blocks.size() - 1)));
        }
        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
        blocks.forEach(b -> {
            try {
                resultStream.write(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return resultStream.toByteArray();
    }


    public static byte[] encodeBlock(byte[] messageBlock, String key, boolean decrypt) {
        List<byte[]> subKeys = generujPodklucze(permutacjaKlucza(hexStringToByteArray(key), PC_1));
        if (decrypt) {
            Collections.reverse(subKeys);
        }
        byte[] poczatkowaPermutacja = permutacjaKlucza(messageBlock, IP);
        byte[] lewaPolowa = new byte[4];
        byte[] prawaPolowa = new byte[4];
        for (int i = 0; i < 32; i++) {
            setBitAt(lewaPolowa, getBitAt(poczatkowaPermutacja, i + 1), i + 1); // blok wejściowy rozdzielany jest na dwie 32-bitowe części: lewą oraz prawą
            setBitAt(prawaPolowa, getBitAt(poczatkowaPermutacja, i + 33), i + 1);
        }
        byte[] L = new byte[4];
        byte[] R = new byte[4];
        for (int i = 0; i < 16; i++) {
            L = prawaPolowa;
            R = xor(lewaPolowa, obliczFunkcjeF(prawaPolowa, subKeys.get(i)));
            prawaPolowa = R;
            lewaPolowa = L;
        }
        byte[] mergedKey = new byte[8];
        System.arraycopy(R, 0, mergedKey, 0, R.length);
        System.arraycopy(L, 0, mergedKey, R.length, L.length);
        return permutacjaKlucza(mergedKey, IPMinus1);
    }



    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);


    public static void rotateLeft(byte[] bytes, int bitSize, int loops) {
        for (int j = 0; j < loops; j++) {
            byte firstBit = getBitAt(bytes, 1);
            for (int i = 0; i < bytes.length - 1; i++) {
                bytes[i] <<= 1;
                setBitAt(bytes, getBitAt(bytes, (i + 1) * 8 + 1), (i + 1) * 8);
            }
            bytes[bytes.length - 1] <<= 1;
            setBitAt(bytes, firstBit, bitSize);
        }
    }

    public static String[] binSplitTo64Bits(String message) { return message.split("(?<=\\G.{7})");
    }

    public static byte[] addPadding(byte[] block) {
        int paddedValue = 8 - block.length;
        byte[] padded = Arrays.copyOf(block, 8);
        Arrays.fill(padded, block.length, 8, (byte) paddedValue);
        return padded;
    }

    public static byte[] trimPadding(byte[] paddedBlock) {
        int paddedValue = paddedBlock[7];
        for (int i = 6; i > 7 - paddedValue; i--) {
            if (paddedBlock[i] != paddedValue) {
                return paddedBlock;
            }
        }
        return Arrays.copyOfRange(paddedBlock, 0, 8 - paddedValue);
    }

    public static byte[] xor (byte[] s1, byte[] s2) {
        byte[] xorKey = new byte[s1.length];
        for (int i = 0; i < s1.length; i++) {
            xorKey[i] = (byte) (s1[i] ^ s2[i]);
        }
        return xorKey;
    }

    public static byte[] hexStringToByteArray(String s) {
        if (s.length() % 2 != 0) {
            throw new InputMismatchException("Input must be even");
        }
        s = s.toUpperCase();
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static void printBits(byte[] lol, int siekaj) {
        for (int i = 0; i < lol.length * 8; i++) {
            if (i % siekaj == 0 && i != 0) {
                System.out.print(" ");
            }
            System.out.print(getBitAt(lol, i + 1));
        }
        System.out.println();
    }

    public static byte getBitAt(byte[] bytes, int position) {
        byte chosenByte = bytes[(position - 1) / 8];
        return (byte) (chosenByte >> (7 - (position - 1) % 8) & 1);
    }

    public static void setBitAt(byte[] bytes, byte insertion, int position) {
        byte chosenByte = bytes[(position - 1) / 8];
        byte shiftedInsertion = (byte) (insertion << (7 - (position - 1) % 8));
        if (insertion == 1) {
            chosenByte = (byte) (shiftedInsertion | chosenByte);
        } else {
            chosenByte = (byte) (chosenByte & ~(1 << (7 - (position - 1) % 8)));
        }
        bytes[(position - 1) / 8] = chosenByte;
    }

    public static String byteArrayToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8).toLowerCase();
    }
}
