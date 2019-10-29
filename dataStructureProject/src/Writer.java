import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.util.HashMap;

public class Writer {


//    String outFileName;

    OutputStream outputStream;

    byte[] bytes = new byte[4096];// 缓冲输出
    int count = 0;


    Writer(String outFileName) {
        try {
            this.outputStream = new FileOutputStream(new File(outFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void writeBit(String s, boolean flag) {
        int bit;
        int buffer = 0;
        for (int i = 0; i < 8; i++) {
            bit = s.charAt(i) - '0';
            buffer = buffer << 1;
            if (bit == 1) buffer = buffer | 1;
        }
        bytes[count++] = (byte) buffer;
        //当缓冲数组满了或者读到文件末尾了 就write到文件

        if (count == 4096) {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            count = 0;
        }

        //读完文件了
        if (!flag) {
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
        }

    }

    void write(String readFileName, HashMap<Byte, String> hashMap, HashMap<String, Byte> oppositeHashMap) {

        try {
            //文件输出
            File file = new File(readFileName);
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));


            //write hashMap
            if(oppositeHashMap.size() == 256){//考虑到图片是256个ASCII码
                outputStream.write(1);
                outputStream.write(255);
            }
           else{
                outputStream.write(0);
                outputStream.write(oppositeHashMap.size());
            }

            for (String key : oppositeHashMap.keySet()) {

                outputStream.write(key.length());

                for (int i = 0; i < key.length(); i++) {
                    outputStream.write(key.charAt(i));
                }
                outputStream.write(oppositeHashMap.get(key));
            }


            //写出字节总数
            int totalBytesNumber = inputStream.available();

            StringBuilder s = new StringBuilder();
            while (totalBytesNumber > 0) {
                s.append(totalBytesNumber % 10);
                totalBytesNumber = totalBytesNumber / 10;
            }


            outputStream.write(s.length());

            for (int i = s.length() - 1; i >= 0; i--) {
                outputStream.write(s.charAt(i));
            }


            int ch;

            StringBuilder huffmanCodes = new StringBuilder("");
            while ((ch = inputStream.read()) != -1) {
//                System.out.println(ch);
                byte key = (byte) ch;

                //不断向huffmanCodes里写入字符串，每满8位就写入文件中
                huffmanCodes.append(hashMap.get(key));

                while (huffmanCodes.length() > 8) {
                    writeBit(huffmanCodes.substring(0, 8), true);
                    huffmanCodes.delete(0, 8);
                }
            }

            //对最后剩下的位数不足八位的补零
            int len = huffmanCodes.length();

            if (len < 8) {
                for (int i = 0; i < 8 - len; i++) {
                    huffmanCodes.append("0");
                }
            }
            writeBit(huffmanCodes.substring(0, 8), false);

            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void closeOutStream() {
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
