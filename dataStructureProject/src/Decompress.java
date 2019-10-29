import java.io.*;
import java.util.HashMap;

public class Decompress {
    byte[] bytes = new byte[4096];//缓冲
    HashMap<String, Byte> stringByteHashMap;
    String bufString = "";
    int count = 0;

    int totalBytesNumber;
    int countBytes;

    BufferedInputStream inputStream;

    OutputStream outputStream;

    //按位读取
    boolean readBit(int buff) {
        int n = 7;
        while (n >= 0) {
            int bit = (buff >> n) & 1;
            if (findCharcter(bit)) return true;
            n--;
        }
        return false;
    }


    //还原
    boolean findCharcter(int bit) {
        bufString += bit;
        if (stringByteHashMap.containsKey(bufString)) {
//           System.out.println((char) ((byte) stringByteHashMap.get(bufString)));
            bytes[count++] = stringByteHashMap.get(bufString);
            bufString = "";
            countBytes++;
        }

        if (count == 4096) {
            try {
                outputStream.write(bytes);
                count = 0;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //读完文件了
        if (countBytes == totalBytesNumber) {
            byte[] temp = new byte[count];
            for (int i = 0; i < count; i++) {
                temp[i] = bytes[i];
            }

            try {
                outputStream.write(temp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            count = 0;
            countBytes = 0;
            return true;
        }
        return false;
    }

    void decompressFolder(String path) {
        try {
            int filesNumber = inputStream.read();

            int fileNameLength = inputStream.read();
            StringBuilder fileName = new StringBuilder();

            for (int i = 0; i < fileNameLength; i++) {
                fileName.append((char) inputStream.read());
            }

            //还原文件夹
            File file1;
            if (path.equals("")) {
                file1 = new File(fileName.substring(0, fileName.length()));
            } else {
                file1 = new File(path + "\\" + fileName.substring(0, fileName.length()));
            }
            file1.mkdir();
            for (int i = 0; i < filesNumber; i++) {
                decompressFile(file1.getAbsolutePath(), false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void decompressFile(String path, boolean ifReadType) {
        try {
            int fileType = 0;
            if (!ifReadType) {
                fileType = inputStream.read();
            }

            //如果是文件夹
            if (fileType == 1) {
                decompressFolder(path);
                return;
            }

            int fileNameLength = inputStream.read();
            StringBuilder fileName = new StringBuilder();

            for (int i = 0; i < fileNameLength; i++) {
                fileName.append((char) inputStream.read());
            }

            File file;
            if(path.equals("")){
                file = new File(fileName.substring(0, fileName.length()));
            }else {
                file = new File(path + "\\" + fileName.substring(0, fileName.length()));
            }
            outputStream = new FileOutputStream(file);

            stringByteHashMap = new HashMap<>();//每个文件都的对应一个新的hashmap;

            //读入hashMap
            int flag = inputStream.read();
            int hashMapSize;
            if(flag == 1)
             hashMapSize =inputStream.read()+1;
           else {
               hashMapSize = inputStream.read();
            }

            while (hashMapSize > 0) {
                int len = inputStream.read();
                String key = "";

                for (int i = 0; i < len; i++) {
                    key += (inputStream.read() - '0');
                }

                byte value = (byte) inputStream.read();
                stringByteHashMap.put(key, value);
                hashMapSize--;
            }

//            读取总字节数


            int totalBytesLen = inputStream.read();

            StringBuilder s = new StringBuilder("");
            for (int i = 0; i < totalBytesLen; i++) {
                s.append(inputStream.read() - '0');
            }

           if(s.substring(0,s.length()).equals(""))totalBytesNumber = 0;//空文件
           else totalBytesNumber = Integer.parseInt(s.substring(0, s.length()));

            int va = 0;
            while ((va = inputStream.read()) != -1) {
//                System.out.println((byte)va);
                if(totalBytesNumber == 0)break;//空文件时要先读补的00000000，然后结束。
                if (readBit(va)) break;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    void decompress(String inputFileName) {
        long start = System.currentTimeMillis();

        try {
            File file = new File(inputFileName);

            inputStream = new BufferedInputStream(new FileInputStream(file));


            int fileType = inputStream.read();//是文件夹还是文件；
            if (fileType == 1) {
                decompressFolder("");
                //是文件夹
            } else if (fileType == 0) {
                decompressFile("", true);
            }


            inputStream.close();
            if(outputStream!=null)
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("解压缩时间为： " + (end - start) + "ms");
    }
}
