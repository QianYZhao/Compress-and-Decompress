import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Reader {

    //读取文件
    ArrayList<Node> nodeArrayList = new ArrayList<>();

    ArrayList<Node> cloneNodeArrayList = new ArrayList<>();

    HashMap<Byte, Node> characterNodeHashMap = new HashMap<>();//快速查找 避免去遍历nodeArrayList



    void readFile(String fileName) {
        try {
            File file = new File(fileName);
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));

            int va = 0;

            while ((va = inputStream.read()) != -1) {

                byte ch = (byte) va;//读取一个字节


                if (characterNodeHashMap.containsKey(ch)) {
                    Node node = characterNodeHashMap.get(ch);
                    node.weight += 1;

                } else {
                    Node newNode = new Node(ch, 1);
                    nodeArrayList.add(newNode);
                    cloneNodeArrayList.add(newNode);
                    characterNodeHashMap.put(ch, newNode);
                }
            }


            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
