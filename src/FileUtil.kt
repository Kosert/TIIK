import com.sun.org.apache.xpath.internal.operations.Bool
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*

object FileUtil {

    fun saveToFile(fileName: String, saveModel: SaveModel) {
        ObjectOutputStream(FileOutputStream(fileName)).use {
            it.writeObject(saveModel)
        }
    }

    fun fromFile(fileName: String): SaveModel {
        ObjectInputStream(FileInputStream(fileName)).use { it ->
            val read = it.readObject()
            return read as SaveModel
        }
    }

}

fun String.toBitSet(): BitSet {
    val bitSet = BitSet(this.length)
    this.forEachIndexed { index, value ->
        bitSet.set(index, value == '1')
    }
    return bitSet
}

fun BitSet.toBinaryString(): String {

    return buildString {
        repeat(size()) {
            append(if (this@toBinaryString[it]) '1' else '0')
        }
    }
}

fun String.toBoolArray(): BooleanArray {

    return BooleanArray(this.length).apply {
        this@toBoolArray.forEachIndexed { index, it ->
            this[index] = it == '1'
        }
    }
}

fun BooleanArray.toBinaryString(): String {
    return buildString {
        this@toBinaryString.forEach {
            append(if (it) '1' else '0')
        }
    }
}

