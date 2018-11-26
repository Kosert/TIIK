object Huffman {

    fun getCodeTable(text: String): List<Pair<Char, String>> {

        val pairs = text.groupingBy { it }.eachCount().toList().sortedByDescending { (_, value) -> value }
        val nodes = pairs.map {
            val probability = it.second / text.length.toFloat()
            Node(it.first, probability)
        }

        generateTree(nodes)
        return nodes.map {
            Pair(it.char!!, buildBinary(it))
        }
    }

    private fun generateTree(nodes: List<Node>): Node {

        val trees = nodes.toMutableList()

        while (trees.size > 1) {
            val toRemove = trees.sortedBy { it.probability }.take(2)
            trees.remove(toRemove[0])
            trees.remove(toRemove[1])

            val totalProb = toRemove[0].probability + toRemove[1].probability
            val newNode = Node(null, totalProb).apply {
                leftChild = toRemove[0]
                rightChild = toRemove[1]
            }
            trees.add(newNode)
        }
        return trees.first()
    }

    private fun buildBinary(node: Node): String {
        return when {
            node.isLeftChild() -> buildBinary(node.parent!!) + "0"
            node.isRightChild() -> buildBinary(node.parent!!) + "1"
            else -> ""
        }
    }

}


