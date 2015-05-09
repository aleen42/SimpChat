package com.java.ui.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IOUtil {
    public static BufferedReader buildBufferedReader(String str) {
        return new BufferedReader(new StringReader(str));
    }

    public static ByteArrayInputStream buildByteArrayInputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    public static ByteArrayInputStream buildByteArrayInputStream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    public static ObjectInputStream buildObjectInputStream(
            ByteArrayInputStream bis) throws IOException {
        return new ObjectInputStream(bis);
    }

    public static ObjectOutputStream buildObjectOutputStream(
            ByteArrayOutputStream bos) throws IOException {
        return new ObjectOutputStream(bos);
    }

    public static void copyFile(File src, File dest, boolean cover)
            throws IOException {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            if (!dest.exists()) {
                dest.createNewFile();
            } else if ((dest.exists()) && (cover)) {
                dest.delete();
                dest.createNewFile();
            } else {
                throw new IOException("File " + dest + " already exists");
            }

            in = new FileInputStream(src);
            out = new FileOutputStream(dest);
            in2OutStream(in, out, 1048576);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
    }

    public static void in2OutStream(InputStream in, OutputStream out,
                                    int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];

        for (int bytesRead = 0; (bytesRead = in.read(buffer)) != -1; ) {
            out.write(buffer, 0, bytesRead);
        }
    }

    public static void insertToFileAt(File file, byte[] content, long pos)
            throws IOException {
        long length = file.length();

        if (pos >= length) {
            writeToFileAt(file, content, pos);
        } else {
            RandomAccessFile raf = null;
            long firstLength = length;
            long lastLength = firstLength - pos;
            int insertLength = content.length;
            byte[] buffer = new byte[1048576];
            try {
                raf = new RandomAccessFile(file, "rw");

                while (lastLength > 0L) {
                    if (lastLength < 1048576L) {
                        buffer = new byte[(int) lastLength];
                    }

                    raf.seek(firstLength - buffer.length);
                    int bytesRead = raf.read(buffer);
                    firstLength -= bytesRead;
                    lastLength = firstLength - pos;
                    raf.seek(firstLength + insertLength);
                    raf.write(Arrays.copyOf(buffer, bytesRead));
                }

                raf.seek(pos);
                raf.write(content);
            } finally {
                if (raf != null) {
                    raf.close();
                }
            }
        }
    }

    public static byte[] readFileToByteArray(File file) throws IOException {
        ByteArrayOutputStream bos = null;
        try {
            bos = readFileToByteStream(file);
        } finally {
            if (bos != null) {
                bos.close();
            }
        }

        return bos == null ? null : bos.toByteArray();
    }

    public static byte[] readFileToByteArrayFrom(File file, long start)
            throws IOException {
        return readFileToByteArrayFrom(file, start, -1);
    }

    public static byte[] readFileToByteArrayFrom(File file, long start,
                                                 int length) throws IOException {
        RandomAccessFile raf = null;
        ByteArrayOutputStream bos = null;
        try {
            raf = new RandomAccessFile(file, "r");
            bos = new ByteArrayOutputStream();
            boolean all = length < 0;
            byte[] buffer = new byte[(!all) && (length < 1048576) ? length
                    : 1048576];
            int newLength = 0;
            raf.seek(start);

            for (int bytesRead = 0; (bytesRead = raf.read(buffer)) != -1; ) {
                newLength += bytesRead;

                if ((!all) && (newLength >= length)) {
                    bytesRead -= newLength - length;
                    bos.write(buffer, 0, bytesRead);
                    break;
                }

                bos.write(buffer, 0, bytesRead);
            }

        } finally {
            try {
                if (raf != null) {
                    raf.close();
                }
            } finally {
                if (bos != null) {
                    bos.close();
                }
            }
        }

        return bos == null ? null : bos.toByteArray();
    }

    public static ByteArrayOutputStream readFileToByteStream(File file)
            throws IOException {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            in2OutStream(fis, bos, 1048576);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

        return bos;
    }

    public static List<String> readFileToList(File file) throws IOException {
        return readFileToList(file, null);
    }

    public static List<String> readFileToList(File file, String charsetName)
            throws IOException {
        List<String> list = new ArrayList<String>();
        BufferedReader in = null;
        try {
            if (charsetName == null) {
                in = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file)));
            } else {
                in = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file), charsetName));
            }
            String line;
            while ((line = in.readLine()) != null) {

                list.add(line);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return list;
    }

    public static String readFileToString(File file) throws IOException {
        return readFileToString(file, null);
    }

    public static String readFileToString(File file, String charsetName)
            throws IOException {
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        try {
            if (charsetName == null) {
                in = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file)));
            } else {
                in = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file), charsetName));
            }

            char[] buffer = new char[1048576];

            for (int bytesRead = 0; (bytesRead = in.read(buffer)) != -1; ) {
                sb.append(Arrays.copyOf(buffer, bytesRead));
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return sb.toString();
    }

    public static String readStringFromSystemIn() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String str = br.readLine();
            return str;
        } finally {
            if (br != null) {
                br.close();
            }
        }

    }

    public static void removeFromFileAt(File file, long pos) throws IOException {
        if ((pos >= 0L) && (pos < file.length())) {
            RandomAccessFile raf = null;
            try {
                raf = new RandomAccessFile(file, "rw");
                raf.setLength(pos);
            } finally {
                if (raf != null) {
                    raf.close();
                }
            }
        }
    }

    public static void removeFromFileAt(File file, long pos, int length)
            throws IOException {
        long fileLength = file.length();
        RandomAccessFile raf = null;

        try {
            raf = new RandomAccessFile(file, "rw");

            if (fileLength <= pos + length) {
                raf.setLength(pos);
            } else {
                boolean hasNext = true;
                long pointer = pos + length;
                long lastLength = fileLength - pointer;
                byte[] buffer = new byte[10];

                while (hasNext) {
                    if (lastLength <= 10L) {
                        buffer = new byte[(int) lastLength];
                        hasNext = false;
                    }

                    raf.seek(pointer);
                    int bytesRead = raf.read(buffer);
                    raf.seek(pointer - length);
                    raf.write(buffer);
                    pointer += bytesRead;
                    lastLength = fileLength - pointer;
                }

                raf.setLength(fileLength - length);
            }

        } finally {
            if (raf != null) {
                raf.close();
            }
        }
    }

    public static void writeToFile(File file, byte[] content, boolean append)
            throws IOException {
        ByteArrayInputStream in = null;
        FileOutputStream out = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            in = new ByteArrayInputStream(content);
            out = new FileOutputStream(file, append);
            in2OutStream(in, out, 1048576);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
    }

    public static void writeToFile(File file, String content)
            throws IOException {
        writeToFile(file, content, null, false);
    }

    /**
     * 写入文件
     *
     * @param file
     * @param content     内容
     * @param charsetName 字符编码
     * @param append      是否覆盖
     * @throws IOException
     */
    public static void writeToFile(File file, String content,
                                   String charsetName, boolean append) throws IOException {
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            char[] buffer = new char[1048576];
            in = new BufferedReader(new StringReader(content));

            if (charsetName == null) {
                out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(file,
                                append))));
            } else {
                out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(file,
                                append), charsetName)));
            }

            for (int bytesRead = 0; (bytesRead = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, bytesRead);
            }
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
    }

    /**
     * 写入文件
     *
     * @param file
     * @param content 内容数组
     * @param pos     文件指针位置
     * @throws IOException
     */
    public static void writeToFileAt(File file, byte[] content, long pos)
            throws IOException {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            raf.seek(pos);
            raf.write(content);
        } finally {
            if (raf != null) {
                raf.close();
            }
        }
    }
}