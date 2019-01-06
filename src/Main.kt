import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.io.File
import java.io.IOException
import java.io.ObjectOutputStream
import java.nio.file.Files
import java.util.*
import javax.swing.*
import kotlin.math.log

const val INPUT_FILENAME = "pliczek.txt"
const val COMPRESSED_FILENAME = "compressed"
const val DECOMPRESSED_FILENAME = "output.txt"



fun main(vararg args: String) {

    var inFilePath = ""
    val frame = JFrame("HuffmanCompress")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.setSize(500, 400)

    val pane = JPanel(GridBagLayout())
    frame.add(pane)
    val c = GridBagConstraints()

    val chosenFileField = JTextField("Proszę wybrać plik", 30)
    chosenFileField.isEditable = false
    val chosenFileLabel = JLabel("Wybrany plik")
    chosenFileLabel.labelFor = chosenFileField
    c.gridx = 0
    c.gridy = 0
    pane.add(chosenFileField, c)

    val fc = JFileChooser()
    val selectFileButton = JButton("Wybierz plik")
    selectFileButton.addActionListener { e ->
        val returnVal = fc.showDialog(pane, "Wybierz plik do kompresji/dekompreji")

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            val file = fc.selectedFile
            inFilePath = file.absolutePath
            chosenFileField.text = inFilePath
        }
    }
    c.gridx = 1
    pane.add(selectFileButton, c)

    val deCompressButton = JButton("Kompresuj/Dekompresuj")
    deCompressButton.addActionListener { e ->
        if (inFilePath != null) {
            val inFile = File(inFilePath)
            if (inFile.name.endsWith(".trojan")) {
                val decompressed = FileUtil.fromFile(inFilePath).let {
                    Compressor.with(it.codeTable).decompress(it.bitSet.toBinaryString().take(it.size))
                }
                File(inFilePath.substring(0, inFilePath.length - 7)).writeBytes(decompressed)
            } else {
                try {
                    val bytes = File(inFile.absolutePath).readBytes()
                    val codeTable = Huffman.getCodeTable(bytes)
                    val compressed = Compressor.with(codeTable).compress(bytes)
                    FileUtil.saveToFile(
                            inFile.absolutePath + ".trojan",
                            SaveModel(codeTable, compressed.toBitSet(), compressed.length)
                    )
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }

            }
        }

    }
    c.gridx = 0
    c.gridy = 1
    c.gridwidth = 2
    pane.add(deCompressButton, c)

    frame.isVisible = true

//    //val text = "ABBCCCDDDD"
//    //val bytes = text.toByteArray()
//    val bytes = File(INPUT_FILENAME).readBytes()
//    println("Uncompressed: ${bytes.size} bytes")
//
//    //printEachCount(text)
//    //printEntropy(text)
//
//    val codeTable = Huffman.getCodeTable(bytes)
//    println("Codetable: $codeTable")
//
//    val compressed = Compressor.with(codeTable).compress(bytes)
//    println("Compressed: ${compressed.length / 8} bytes")
//
//    FileUtil.saveToFile(
//            COMPRESSED_FILENAME,
//            SaveModel(codeTable, compressed.toBitSet(), compressed.length)
//    )
//
//    val decompressed = FileUtil.fromFile(COMPRESSED_FILENAME).let {
//        Compressor.with(it.codeTable).decompress(it.bitSet.toBinaryString().take(it.size))
//    }
//
//    File(DECOMPRESSED_FILENAME).writeBytes(decompressed)
//    println("Decompressed saved to $DECOMPRESSED_FILENAME")
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