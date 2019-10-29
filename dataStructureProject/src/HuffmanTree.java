import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class HuffmanTree {

    Node root;
    HashMap<Byte, String> hashMap = new HashMap<>();

    HashMap<String,Byte> oppositeHashMap = new HashMap<>();
    ArrayList arrayListSort(ArrayList<Node> arrayList) {

        //Arraylist 排序；

        arrayList.sort(new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
               if (n1.weight < n2.weight) return -1;
                else if(n1.weight>n2.weight)return 1;
                else return 0;
            }
        });

        return arrayList;
    }

    void buildHuffmanTree(ArrayList<Node> arrayList) {


        arrayList = arrayListSort(arrayList);

        while (arrayList.size()>0) {

            if (arrayList.size() == 1) {
                Node newNode = new Node(arrayList.get(0).key, arrayList.get(0).weight);
                this.root = newNode;
                break;
            }


            if (arrayList.get(0) != null && arrayList.get(1) != null) {
                int newValue = arrayList.get(0).weight + arrayList.get(1).weight;
                Node newNode = new Node(newValue);


                newNode.left = arrayList.get(0);
                newNode.right = arrayList.get(1);

                // 设置根节点
                if (arrayList.size() == 2) {
                    this.root = newNode;
                    break;
                }

                //remove 最小的两个node
                arrayList.remove(arrayList.get(1));
                arrayList.remove(arrayList.get(0));

                //添加新Node
                arrayList.add(newNode);
                arrayList = arrayListSort(arrayList);
            }

        }

    }

    void inOrder(Node root, String s) {

        if (root.left != null) {
            inOrder(root.left, s + "0");
        }
        if (root.right != null) {
            inOrder(root.right, s + "1");
        }
        if (root.left==null && root.right==null) {
            hashMap.put(root.key,s);
            oppositeHashMap.put(s,root.key);
            return;
        }
    }
}
