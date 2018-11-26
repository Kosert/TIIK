import java.io.File
import kotlin.math.log

const val FILENAME = "pliczek.txt"

fun main(vararg args: String) {

    val text = File(FILENAME).readText() //"ABBCCCDDDD"
    printEachCount(text)
    printEntropy(text)

    val codeTable = Huffman.getCodeTable(text)
    println("Codetable: $codeTable")

    val compressed = Compressor.with(codeTable).compress(text)
    println("Compressed: $compressed")

    val decompressed = Compressor.with(codeTable).decompress(compressed)
    println("Decompressed: $decompressed")
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