object Compressor {

    interface ICompressor {
        fun compress(bytes: ByteArray): String
        fun decompress(binaryString: String): ByteArray
    }

    fun with(codeTable: List<Pair<Byte, String>>): ICompressor {
        return object : ICompressor {

            override fun compress(bytes: ByteArray): String {
                val builder = StringBuilder()
                bytes.forEach { byte ->
                    val code = codeTable.find { byte == it.first }?.second
                    code?.let {
                        builder.append(it)
                    } ?: IllegalArgumentException("Byte not present in code table: $byte")
                }
                return builder.toString()
            }

            override fun decompress(binaryString: String): ByteArray {

                val sortedCodeTable = codeTable.sortedByDescending { it.second.length }
                val bitList = binaryString.toCharArray().toMutableList()

                val outputBytes = mutableListOf<Byte>()

                while (bitList.size > 0) {

                    val matched = sortedCodeTable.firstOrNull {
                        it.second == bitList.take(it.second.length).joinToString("")
                    }

                    matched?.let {
                        outputBytes.add(it.first)
                        repeat(it.second.length) { bitList.removeAt(0) }
                    } ?: IllegalArgumentException("Unrecognized code")
                }
                return outputBytes.toByteArray()
            }
        }
    }

}