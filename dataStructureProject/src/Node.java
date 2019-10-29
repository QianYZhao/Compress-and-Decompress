class Node {
    byte key;//字符
    int weight = 0;//字符出现的次数


    Node left;
    Node right;

    Node(byte key, int weight) {
        this.key = key;
        this.weight = weight;
    }

    Node(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
}