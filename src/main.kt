import java.io.File
import kotlin.math.log

const val FILENAME = "pliczek.txt"

fun main(vararg args: String) {

    val text = File(FILENAME).readText()
    val pairs = text.groupingBy { it }.eachCount().toList().sortedByDescending { (_, value) -> value }

    pairs.forEach {
        val percent = (it.second / text.length.toFloat() * 100)//.toString().take(5)
        println("${it.first} - count=${it.second} ($percent%)")
    }

    val entropy = pairs.sumByDouble {
        val p = it.second / text.length.toDouble()
        p * log(1 / p, 2.0)
    }
    println("Entropy = $entropy")
}