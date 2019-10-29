import java.io.File;
import java.io.IOException;


/*
存文件时 先存入fileType 以0代表该文件是一个文件 1代表文件夹
对于文件夹 存入fileType->1 存入文件夹里包含的文件个数 存入文件名的长度 再存入String文件名
对于文件 存入fileType->0  存入文件名的长度 存入文件名 存入文件内容
   对于文件内容  先存入hashMap:先存入hashMap.size(),再存入hashmap里的内容
                存文件字节的数目（方便之后解压读取的时候判断结束）
                 存入huffman Code;
*/
public class Compress {

    int fileType;
    Writer writer;


    void connect(String srcFileName){
        writer = new Writer(srcFileName+".yip");
    }

    boolean prepairCompress(String inputFileName) {
        File file = new File(inputFileName);

        if (file.isDirectory()) {
            File[] files = file.listFiles();

            fileType = 1;

            //先将文件夹里的文件个数写进去
            try {

                writer.outputStream.write(fileType);
                writer.outputStream.write(files.length);


                String str = file.getName();
                int len = str.length();

                writer.outputStream.write(len);

                for (int i=0;i<len;i++){
                  writer.outputStream.write(str.charAt(i));
                }


            }catch (IOException e){
                e.printStackTrace();
            }

            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    //如果是文件夹 则将文件夹名写入zip文件中；
                    prepairCompress(files[i].getAbsolutePath());
                } else {

                    fileType = 0;
                    try {
                        writer.outputStream.write(fileType);

                        String str = files[i].getName();
                        int len = str.length();

                        writer.outputStream.write(len);

                        for (int j=0;j<len;j++){
                            writer.outputStream.write(str.charAt(j));
                        }


                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    compress(files[i].getAbsolutePath());
                }
            }

        } else {

            fileType = 0;
            try {
                writer.outputStream.write(fileType);

                String str = file.getName();
                int len = str.length();

                writer.outputStream.write(len);

                for (int j=0;j<len;j++){
                    writer.outputStream.write(str.charAt(j));
                }

            }catch (IOException e){
                e.printStackTrace();
            }
            compress(inputFileName);
        }
        return true;
    }


   //所有文件写在一个zip里

    void compress(String srcFileName) {



        Reader reader = new Reader();
        reader.readFile(srcFileName);

        HuffmanTree huffmanTree = new HuffmanTree();
        huffmanTree.buildHuffmanTree(reader.cloneNodeArrayList);

        for (int i = 0; i < reader.nodeArrayList.size(); i++) {
            huffmanTree.inOrder(huffmanTree.root,"");
        }

        writer.write(srcFileName, huffmanTree.hashMap, huffmanTree.oppositeHashMap);


    }
}
