import java.io.Serializable
import java.util.*

data class SaveModel(
        val codeTable: List<Pair<Byte, String>>,
        val bitSet: BitSet,
        val size: Int
) : Serializable