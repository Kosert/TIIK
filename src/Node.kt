data class Node
@JvmOverloads constructor(
        val byte: Byte? = null,
        val probability: Float
) {
    var parent: Node? = null
        private set

    var leftChild: Node? = null
        set(value) {
            field = value
            value?.parent = this
        }

    var rightChild: Node? = null
        set(value) {
            field = value
            value?.parent = this
        }

    fun isLeftChild() = parent?.leftChild === this
    fun isRightChild() = parent?.rightChild === this
}