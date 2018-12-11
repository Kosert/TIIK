import java.io.File
import java.util.*
import kotlin.math.log

const val INPUT_FILENAME = "pliczek.txt"
const val COMPRESSED_FILENAME = "compressed"
const val DECOMPRESSED_FILENAME = "output.txt"

fun main(vararg args: String) {

    //val text = "ABBCCCDDDD"
    //val bytes = text.toByteArray()
    val bytes = File(INPUT_FILENAME).readBytes()
    println("Uncompressed: ${bytes.size} bytes")

    //printEachCount(text)
    //printEntropy(text)

    val codeTable = Huffman.getCodeTable(bytes)
    println("Codetable: $codeTable")

    val compressed = Compressor.with(codeTable).compress(bytes)
    println("Compressed: ${compressed.length / 8} bytes")

    FileUtil.saveToFile(
            COMPRESSED_FILENAME,
            SaveModel(codeTable, compressed.toBitSet(), compressed.length)
    )

    val decompressed = FileUtil.fromFile(COMPRESSED_FILENAME).let {
        Compressor.with(it.codeTable).decompress(it.bitSet.toBinaryString().take(it.size))
    }

    File(DECOMPRESSED_FILENAME).writeBytes(decompressed)
    println("Decompressed saved to $DECOMPRESSED_FILENAME")
}


fun printEachCount(text: String) {
    val pairs = text.groupingBy { it }.eachCount().toList().sortedByDescending { (_, value) -> value }
    pairs.map {
        val percent = (it.second / text.length.toFloat() * 100)//.toString().take(5)
        println("${it.first} - count=${it.second} ($percent%)")
        Pair(it.first, percent)
    }
}

fun printEntropy(text: String) {
    val pairs = text.groupingBy { it }.eachCount().toList().sortedByDescending { (_, value) -> value }
    val entropy = pairs.sumByDouble {
        val p = it.second / text.length.toDouble()
        p * log(1 / p, 2.0)
    }
    println("Entropy = $entropy")
}